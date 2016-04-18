package celestibytes.soulmagic.api.spell;

import celestibytes.soulmagic.api.spell.ext.IInstantSpellExt;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IInstantSpell extends ISpell {
	
	/** Called once when the spell is first casted regardless of what it is casted on. */
	public void onCast(World world, EntityLivingBase caster, ISpellHandler magic, IInstantSpellExt ext);
	
	/** Called once if the spell is casted on nothing. */
	public void onCastNothing(World world, EntityLivingBase caster, ISpellHandler magic, IInstantSpellExt ext);
	
	/** Called once if the spell is casted on a EntityLivingBase(a player or a mob). */
	public void onCastLiving(EntityLivingBase target, World world, EntityLivingBase caster, ISpellHandler magic, IInstantSpellExt ext);
	
	/** Called once if the spell is casted on a block. */
	public void onCastBlock(BlockPos pos, EnumFacing side, World world, EntityLivingBase caster, ISpellHandler magic, IInstantSpellExt ext);
}
