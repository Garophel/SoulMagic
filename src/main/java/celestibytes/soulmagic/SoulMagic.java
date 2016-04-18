package celestibytes.soulmagic;

import celestibytes.soulmagic.init.ModItems;
import celestibytes.soulmagic.init.ModSpells;
import celestibytes.soulmagic.proxy.CommonProxy;
import celestibytes.soulmagic.spell.EntityUpdateHandler;
import celestibytes.soulmagic.spell.SpellHandlerCapability;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=SoulMagic.MODID, name=SoulMagic.MOD_NAME, version=SoulMagic.VERSION)
public class SoulMagic {
	public static final String MODID = "soulmagic";
	public static final String MOD_NAME = "Soul Magic";
	public static final String VERSION = "1.0";
	
	public static final String PROXY_CLIENT = "celestibytes.soulmagic.proxy.ClientProxy";
	public static final String PROXY_COMMON = "celestibytes.soulmagic.proxy.CommonProxy";
	
	@SidedProxy(clientSide=PROXY_CLIENT, serverSide=PROXY_COMMON)
	public static CommonProxy proxy;
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		ModSpells.init();
		SpellHandlerCapability.register();
		MinecraftForge.EVENT_BUS.register(new EntityUpdateHandler());
	}
}
