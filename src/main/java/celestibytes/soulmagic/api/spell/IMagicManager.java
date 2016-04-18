package celestibytes.soulmagic.api.spell;

import celestibytes.soulmagic.api.spell.ext.IContinuousSpellExt;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public interface IMagicManager extends ISpellHandler {
	public void onUpdate();
	
	public void updateContinuous(World world, EntityLivingBase caster, IContinuousSpellExt ext);
	
	public void castContinuous(IContinuousSpell spell, World world, EntityLivingBase caster, IContinuousSpellExt ext);
	
	public void stopContinuous();
}
