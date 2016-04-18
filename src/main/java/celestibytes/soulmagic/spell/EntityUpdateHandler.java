package celestibytes.soulmagic.spell;

import celestibytes.soulmagic.api.spell.IMagicManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class EntityUpdateHandler {
	
	@SubscribeEvent
	public void onWorldTickEnd(TickEvent.WorldTickEvent e) {
		if(e.phase == Phase.END && !e.world.isRemote) {
			for(Entity ent : e.world.loadedEntityList) {
				if(ent instanceof EntityLivingBase && !ent.isDead && ent.ticksExisted % 5 == 0) {
					IMagicManager magic = ent.getCapability(SpellHandlerCapability.SPELLHANDLER, null);
					if(magic != null) {
						magic.onUpdate();
					}
				}
			}
		}
	}
	
}
