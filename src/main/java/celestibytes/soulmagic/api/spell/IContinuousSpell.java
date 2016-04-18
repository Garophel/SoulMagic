package celestibytes.soulmagic.api.spell;

import celestibytes.soulmagic.api.spell.ext.IContinuousSpellExt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/** A continuously casted spell until the cast(=use item) button is released. */
public interface IContinuousSpell extends ISpell {
	
	/** Called once when the spell is first casted. */
	public void onCast(World world, Entity spell, EntityLivingBase caster, ISpellHandler magic, IContinuousSpellExt ext);
	
	/** Called each tick when the spell is active. */
	public void onUpdate(World world, Entity spell, EntityLivingBase caster, ISpellHandler magic, IContinuousSpellExt ext);
}
