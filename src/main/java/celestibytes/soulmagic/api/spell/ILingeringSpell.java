package celestibytes.soulmagic.api.spell;

import celestibytes.soulmagic.api.spell.ext.ILingeringSpellExt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

/** A spell that is active until it ends or is stopped. */
public interface ILingeringSpell extends ISpell, INBTSerializable<NBTTagCompound> {
	
	/** Called once when the spell is first casted. */
	public void onCast(World world, Entity spell, EntityLivingBase caster, ISpellHandler magic, ILingeringSpellExt ext);
	
	/** Called each tick while the spell is active. */
	public void onUpdate(World world, Entity spell, EntityLivingBase caster, ISpellHandler magic, ILingeringSpellExt ext);
}
