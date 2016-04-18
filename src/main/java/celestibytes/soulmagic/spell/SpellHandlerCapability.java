package celestibytes.soulmagic.spell;

import celestibytes.soulmagic.SoulMagic;
import celestibytes.soulmagic.api.spell.IContinuousSpell;
import celestibytes.soulmagic.api.spell.IInstantSpell;
import celestibytes.soulmagic.api.spell.IMagicManager;
import celestibytes.soulmagic.api.spell.ISpell;
import celestibytes.soulmagic.api.spell.ISpellHandler;
import celestibytes.soulmagic.api.spell.ext.IContinuousSpellExt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class SpellHandlerCapability {
	
	@CapabilityInject(IMagicManager.class)
	public static Capability<IMagicManager> SPELLHANDLER;
	
	public static ResourceLocation spellManagerKey = new ResourceLocation(SoulMagic.MODID, "sh");
	
	
	private static class SpellHandler implements IMagicManager, ICapabilityProvider, INBTSerializable<NBTTagCompound> {
		
		private EntityLivingBase ent;
		
		private IContinuousSpell activeContinuous = null;
		private int updatesSinceContinuous = 0;
		
		private int maxMana = 1000;
		private int mana = maxMana;
		private int manaRegen = 5; // per 1/4 second
		
		public SpellHandler(EntityLivingBase ent) {
			this.ent = ent;
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == SpellHandlerCapability.SPELLHANDLER;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if(capability == SPELLHANDLER) {
				return SPELLHANDLER.cast(this);
			}
			
			return null;
		}
		
		@Override
		public void onUpdate() {
			manaRegen = 5;
			if(mana < maxMana) {
				int old = mana;
				mana += manaRegen * (2f / (0.3f * ((float) mana / (float) maxMana * 10f) + .25f) + 0.385f);
				if(mana > maxMana) mana = maxMana;
//				if(ent instanceof EntityPlayer) ent.addChatMessage(new TextComponentString(old + " -> " + mana));
			}
			
			if(activeContinuous != null && updatesSinceContinuous > 10) {
				activeContinuous = null;
			}
		}

		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound ret = new NBTTagCompound();
			ret.setInteger("max_mana", maxMana);
			ret.setInteger("mana", mana);
			ret.setInteger("mana_regen", manaRegen);
			
			return ret;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			if(nbt.hasKey("max_mana", 3)) maxMana = nbt.getInteger("max_mana");
			if(nbt.hasKey("mana", 3)) mana = nbt.getInteger("mana");
			if(nbt.hasKey("mana_regen", 3)) manaRegen = nbt.getInteger("mana_regen");
		}

		@Override
		public void endSpell(boolean failed, ISpell spell) {
			if(spell instanceof IContinuousSpell) {
				activeContinuous = null;
			}
		}

		@Override
		public boolean consumeMana(int amount) {
			mana -= amount;
			if(mana < 0) mana = 0;
			if(mana > maxMana) mana = maxMana;
			return mana > 0;
		}

		@Override
		public boolean checkMana(int amount) {
			return amount < mana;
		}

		@Override
		public int getMaxMana() {
			return maxMana;
		}

		@Override
		public int getAvailableMana() {
			return mana;
		}
		
		@Override
		public void updateContinuous(World world, EntityLivingBase caster, IContinuousSpellExt ext) {
			if(activeContinuous != null) activeContinuous.onUpdate(world, null, caster, this, ext);
			updatesSinceContinuous = 0;
		}

		@Override
		public void castContinuous(IContinuousSpell spell, World world, EntityLivingBase caster, IContinuousSpellExt ext) {
			activeContinuous = spell;
			if(activeContinuous == null) return;
			
			activeContinuous.onCast(world, null, caster, this, ext);
			updateContinuous(world, caster, ext);
		}

		@Override
		public void stopContinuous() {
			activeContinuous = null;
		}
	}
	
	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent.Entity e) {
		Entity ent = e.getEntity();
		if(!ent.worldObj.isRemote && ent instanceof EntityLivingBase) {
			e.addCapability(spellManagerKey, new SpellHandler((EntityLivingBase) ent));
		}
	}
	
	public static void register() {
		CapabilityManager.INSTANCE.register(IMagicManager.class, new Capability.IStorage<IMagicManager>() {

			@Override
			public NBTBase writeNBT(Capability<IMagicManager> capability, IMagicManager instance, EnumFacing side) {
				return null;
			}

			@Override
			public void readNBT(Capability<IMagicManager> capability, IMagicManager instance, EnumFacing side, NBTBase nbt) {
				
			}
		}, SpellHandler.class);
		
		MinecraftForge.EVENT_BUS.register(new SpellHandlerCapability());
	}
	
	private static IInstantSpell createInstantSpell(Class<? extends IInstantSpell> spell) {
		try {
			return spell.newInstance();
		} catch (Exception e) { e.printStackTrace(); }
		
		return null;
	}
	
	private static IContinuousSpell createContinuousSpell(Class<? extends IContinuousSpell> spell) {
		try {
			return spell.newInstance();
		} catch (Exception e) { e.printStackTrace(); }
		
		return null;
	}
	
	private static ISpellHandler getSpellHandler(EntityLivingBase caster) {
		return caster.getCapability(SPELLHANDLER, null);
	}
	
	private static IMagicManager getMagicManager(EntityLivingBase caster) {
		return caster.getCapability(SPELLHANDLER, null);
	}
	
	public static void castInstantSpell(World world, EntityLivingBase caster, Class<? extends IInstantSpell> spellClass) {
		IInstantSpell spell = createInstantSpell(spellClass);
		if(spell == null) return;
		
		ISpellHandler magic = getSpellHandler(caster);
		if(magic == null) return;
		
		spell.onCast(world, caster, magic, null);
		spell.onCastNothing(world, caster, magic, null);
	}
	
	public static void castInstantSpell(BlockPos pos, EnumFacing facing, World world, EntityLivingBase caster, Class<? extends IInstantSpell> spellClass) {
		IInstantSpell spell = createInstantSpell(spellClass);
		if(spell == null) return;
		
		ISpellHandler magic = getSpellHandler(caster);
		if(magic == null) return;
		
		spell.onCast(world, caster, magic, null);
		spell.onCastBlock(pos, facing, world, caster, magic, null);
	}
	
	public static void castContinuousSpell(World world, EntityLivingBase caster, Class<? extends IContinuousSpell> spellClass) {
		IContinuousSpell spell = createContinuousSpell(spellClass);
		if(spell == null) return;
		
		IMagicManager magic = getMagicManager(caster);
		if(magic == null) return;
		
		magic.castContinuous(spell, world, caster, null);
	}
	
	public static void updateContinuousSpell(World world, EntityLivingBase caster) {
		IMagicManager magic = getMagicManager(caster);
		if(magic == null) return;
		
		magic.updateContinuous(world, caster, null);
	}
	
	public static void stopContinuousSpell(EntityLivingBase caster) {
		IMagicManager magic = getMagicManager(caster);
		if(magic == null) return;
		
		magic.stopContinuous();
	}
}
