package net.insane96mcp.smasher.init;

import java.util.ArrayList;

import net.insane96mcp.smasher.Smasher;
import net.insane96mcp.smasher.item.ItemPulverized;
import net.insane96mcp.smasher.item.ItemUpgrade;
import net.insane96mcp.smasher.lib.Names;
import net.insane96mcp.smasher.lib.Properties;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {
	
	public static final CreativeTabs CREATIVE_TAB_SMASHER = (new CreativeTabs(Smasher.MOD_NAME) {
		
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModBlocks.smasher);
		}
	});

	public static ItemUpgrade speedUpgrade;
	//public static ItemUpgrade experienceUpgrade;

	public static ItemPulverized pulverizedIron;
	public static ItemPulverized pulverizedGold;
	
	//public static ArrayList<ItemPulverized> pulverizedItems = new ArrayList<ItemPulverized>();
	
	public static ArrayList<Item> ITEMS = new ArrayList<Item>();
	
	public static void Init() {
		speedUpgrade = new ItemUpgrade(new ResourceLocation(Smasher.MOD_ID, Names.SPEED_UPGRADE), ModItems.CREATIVE_TAB_SMASHER, ItemUpgrade.UpgradeType.SPEED);
		speedUpgrade.setMaxStackSize(Properties.Upgrade.Speed.maxUpgrades);
		ITEMS.add(speedUpgrade);

		pulverizedIron = new ItemPulverized(new ResourceLocation(Smasher.MOD_ID, Names.PULVERIZED_IRON), ModItems.CREATIVE_TAB_SMASHER, "iron", 0x373944);
		ITEMS.add(pulverizedIron);
		//pulverizedItems.add(pulverizedIron);
		
		pulverizedGold = new ItemPulverized(new ResourceLocation(Smasher.MOD_ID, Names.PULVERIZED_GOLD), ModItems.CREATIVE_TAB_SMASHER, "gold", 0x9b990a);
		ITEMS.add(pulverizedGold);
		//pulverizedItems.add(pulverizedGold);
		/*experienceUpgrade = new ItemUpgrade(new ResourceLocation(Smasher.MOD_ID, Names.EXPERIENCE_UPGRADE), ModItems.CREATIVE_TAB_SMASHER, ItemUpgrade.UpgradeType.EXPERIENCE);
		experienceUpgrade.setMaxStackSize(1);
		ITEMS.add(experienceUpgrade);*/
	}
	
	public static void PostInit() {
		GameRegistry.addSmelting(pulverizedIron, new ItemStack(Items.IRON_INGOT), 0.35f);
		GameRegistry.addSmelting(pulverizedGold, new ItemStack(Items.GOLD_INGOT), 0.5f);
	}
}
