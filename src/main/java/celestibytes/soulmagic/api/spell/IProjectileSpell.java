package celestibytes.soulmagic.api.spell;

import celestibytes.soulmagic.api.spell.ext.IProjectileSpellExt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public interface IProjectileSpell extends ISpell, INBTSerializable<NBTTagCompound> {
	
	/** Called once when the spell is first casted. */
	public void onCast(World world, Entity spell, EntityLivingBase caster, ISpellHandler magic, IProjectileSpellExt ext);
	
	/** Called each tick while the spell is flying and haven't hit anything. */
	public void onFly(World world, Entity spell, EntityLivingBase caster, ISpellHandler magic, IProjectileSpellExt ext);
	
	/** Called when the spell hits a block. */
	public void onHitBlock(BlockPos hit, EnumFacing side, World world, Entity spell, EntityLivingBase caster, ISpellHandler magic, IProjectileSpellExt ext);
	
	/** Called when the spell hits a EntityLivingBase(player or mob). */
	public void onHitLiving(EntityLivingBase hit, World world, Entity spell, EntityLivingBase caster, ISpellHandler magic, IProjectileSpellExt ext);
}
