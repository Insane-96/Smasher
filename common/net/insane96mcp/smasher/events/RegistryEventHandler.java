package net.insane96mcp.smasher.events;

import net.insane96mcp.smasher.Smasher;
import net.insane96mcp.smasher.init.ModBlocks;
import net.insane96mcp.smasher.init.ModItems;
import net.insane96mcp.smasher.init.ModSounds;
import net.insane96mcp.smasher.item.ItemPulverized;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Smasher.MOD_ID)
public class RegistryEventHandler {

	//1.12 Register Items and Blocks
	@SubscribeEvent
	public static void RegisterBlocks(RegistryEvent.Register<Block> event) {
		for (Block block : ModBlocks.BLOCKS)
			event.getRegistry().register(block);
	}
	
	@SubscribeEvent
	public static void RegisterItems(RegistryEvent.Register<Item> event) {
		for (Item item : ModItems.ITEMS)
			event.getRegistry().register(item);

		for (Block block : ModBlocks.BLOCKS)
			event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}
	
	@SubscribeEvent
	public static void RegisterSounds(RegistryEvent.Register<SoundEvent> event) {
		for (SoundEvent soundEvent : ModSounds.SOUNDS) {
			event.getRegistry().register(soundEvent);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void RegisterModels(ModelRegistryEvent event) {
		for (Item item : ModItems.ITEMS) {
			/*if (item instanceof ItemPulverized) {
				ItemPulverized pulverized = (ItemPulverized) item;
				ModelLoader.setCustomModelResourceLocation(pulverized, 0, new ModelResourceLocation(pulverized.getRegistryName() + pulverized.ingotName, "inventory"));
			}
			else {*/
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
			//}
		}
		for (Block block : ModBlocks.BLOCKS) {
			Item item = Item.getItemFromBlock(block);
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
		}
	}
}
