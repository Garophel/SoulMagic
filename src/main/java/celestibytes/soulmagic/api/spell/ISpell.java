package celestibytes.soulmagic.api.spell;

import net.minecraft.util.math.Vec3d;

public interface ISpell {
	/** Origin of the spell effect.
	 *  Must not be null, use Vec3d.ZERO instead of null
	 *  if unsure. Usually the position of the caster or
	 *  spell entity. */
	public Vec3d getPosition();
}
