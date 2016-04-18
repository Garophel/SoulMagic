package celestibytes.soulmagic.init;

import celestibytes.soulmagic.item.ItemWand;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {
	
	public static Item itemWand;
	
	public static void init() {
		itemWand = new ItemWand().setRegistryName("wand");
		GameRegistry.register(itemWand);
	}
}
