package celestibytes.soulmagic.proxy;

import celestibytes.soulmagic.init.ModItems;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent e) {
		ModItems.init();
	}
}
