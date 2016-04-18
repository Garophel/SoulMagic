package celestibytes.soulmagic.item;

import celestibytes.soulmagic.SoulMagic;
import celestibytes.soulmagic.api.spell.IContinuousSpell;
import celestibytes.soulmagic.api.spell.IInstantSpell;
import celestibytes.soulmagic.init.ModItems;
import celestibytes.soulmagic.init.ModSpells;
import celestibytes.soulmagic.spell.SpellHandlerCapability;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWand extends Item {
	
	public ItemWand() {
		setCreativeTab(CreativeTabs.tabTools);
		setMaxStackSize(1);
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return getWandType(stack) == WandType.CONTINUOUS ? EnumAction.BLOCK : super.getItemUseAction(stack);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String ret = "item." + SoulMagic.MODID + ":wand.";
		int meta = stack.getMetadata();
		switch(meta) {
		case 0:
			ret += "instant";
			break;
		case 1:
			ret += "projectile";
			break;
		case 2:
			ret += "lingering";
			break;
		case 3:
			ret += "continuous";
			break;
		default:
			ret += "unknown";
		}
		
		return ret;
	}
	
	public static WandType getWandType(ItemStack stack) {
		if(stack != null && stack.getItem() == ModItems.itemWand) {
			switch(stack.getMetadata()) {
			case 0: return WandType.INSTANT;
			case 1: return WandType.PROJECTILE;
			case 2: return WandType.LINGERING;
			case 3: return WandType.CONTINUOUS;
			}
		}
		
		return WandType.UNKNOWN;
	}
	
	public static String getSpellId(ItemStack stack) {
		return stack.hasTagCompound() ? stack.getTagCompound().getString("spellid") : "";
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if(!world.isRemote) {
			if(getWandType(stack) == WandType.INSTANT) {
				Class<? extends IInstantSpell> spell = ModSpells.getInstantSpell(getSpellId(stack));
				if(spell != null) SpellHandlerCapability.castInstantSpell(world, player, spell);
			} else if(getWandType(stack) == WandType.CONTINUOUS) {
				player.setActiveHand(hand);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
		}
		
		return super.onItemRightClick(stack, world, player, hand);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer plr, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!world.isRemote && getWandType(stack) == WandType.INSTANT) {
			Class<? extends IInstantSpell> spell = ModSpells.getInstantSpell(getSpellId(stack));
			if(spell != null) SpellHandlerCapability.castInstantSpell(pos, facing, world, plr, spell);
		}
		
		return super.onItemUse(stack, plr, world, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
		if(!world.isRemote && getWandType(stack) == WandType.CONTINUOUS) {
			SpellHandlerCapability.stopContinuousSpell(entityLiving);
		}
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		if(!player.worldObj.isRemote && getWandType(stack) == WandType.CONTINUOUS) {
			if(count == getMaxItemUseDuration(stack)) {
				Class<? extends IContinuousSpell> clazz = ModSpells.getContinuousSpell(getSpellId(stack));
				if(clazz != null) SpellHandlerCapability.castContinuousSpell(player.worldObj, player, clazz);
			} else {
				SpellHandlerCapability.updateContinuousSpell(player.worldObj, player);
			}
		}
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return getWandType(stack) == WandType.CONTINUOUS ? 800000 : 0;
	}
	
	public static enum WandType {
		INSTANT("instant"),
		PROJECTILE("projectile"),
		LINGERING("lingering"),
		CONTINUOUS("continuous"),
		UNKNOWN("unknown");
		
		public final String unlocalizedName;
		
		private WandType(String unlocalizedName) {
			this.unlocalizedName = unlocalizedName;
		}
		
		@Override
		public String toString() {
			return unlocalizedName;
		}
	}
}
