package celestibytes.soulmagic.init.spells;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import celestibytes.soulmagic.api.spell.IContinuousSpell;
import celestibytes.soulmagic.api.spell.ISpellHandler;
import celestibytes.soulmagic.api.spell.ext.IContinuousSpellExt;

public class SpellFeed implements IContinuousSpell {
	
	private int ticks = 0;
	private Iterator<EntityAnimal> iter = null;
	private int casterInvIndex = 0;
	
	private boolean anyFound = false;

	@Override
	public Vec3d getPosition() {
		return Vec3d.ZERO;
	}

	@Override
	public void onCast(World world, Entity spell, EntityLivingBase caster, ISpellHandler magic, IContinuousSpellExt ext) {
//		world.playSound((EntityPlayer) caster, caster.getPosition(), SoundEvents.enchant_thorns_hit, SoundCategory.BLOCKS, 1f, 1f);
	}

	@Override
	public void onUpdate(World world, Entity spell, EntityLivingBase caster, ISpellHandler magic, IContinuousSpellExt ext) {
		EntityPlayer plr = null;
		if(caster instanceof EntityPlayer) {
			plr = (EntityPlayer) caster;
		} else {
			magic.endSpell(true, this);
			return;
		}
		
		if(ticks % 10 == 0) {
			if(iter != null && iter.hasNext()) {
				EntityAnimal ent = iter.next();
				if(ent == null) return;
				
				ItemStack stack = findBreedingItem(ent, plr);
				if(stack == null) {
					magic.endSpell(true, this);
					return;
				}
				
				if(ent.processInteract(plr, EnumHand.MAIN_HAND, stack)) {
					anyFound = true;
				}
			} else {
				if(ticks > 0 && !anyFound) {
					magic.endSpell(false, this);
					return;
				}
				
				anyFound = false;
				List<EntityAnimal> ents = world.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(plr.posX - 5d, plr.posY - 1d, plr.posZ - 5d, plr.posX + 5d, plr.posY + 1d, plr.posZ + 5d));
				if(ents.size() > 0) {
					iter = ents.iterator();
				} else {
					magic.endSpell(false, this);
				}
			}
		}
		
		ticks++;
	}
	
	private ItemStack findBreedingItem(EntityAnimal ent, EntityPlayer plr) {
		int size = plr.inventory.getSizeInventory();
		for(int i = casterInvIndex; i < size; i++) {
			ItemStack stack = plr.inventory.getStackInSlot(i);
			if(ent.isBreedingItem(stack)) {
				casterInvIndex = i;
				return stack;
			}
		}
		
		for(int i = 0; i < size; i++) {
			ItemStack stack = plr.inventory.getStackInSlot(i);
			if(ent.isBreedingItem(stack)) {
				casterInvIndex = i;
				return stack;
			}
		}
		
		return null;
	}
	
	
}
