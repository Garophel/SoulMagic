package celestibytes.soulmagic.proxy;

import celestibytes.soulmagic.SoulMagic;
import celestibytes.soulmagic.init.ModItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		ModelLoader.setCustomModelResourceLocation(ModItems.itemWand, 0, new ModelResourceLocation(SoulMagic.MODID + ":wand", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemWand, 1, new ModelResourceLocation(SoulMagic.MODID + ":wand", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemWand, 2, new ModelResourceLocation(SoulMagic.MODID + ":wand", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.itemWand, 3, new ModelResourceLocation(SoulMagic.MODID + ":wand", "inventory"));
	}
}
