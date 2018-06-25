package net.insane96mcp.smasher.init;

import java.util.ArrayList;

import net.insane96mcp.smasher.Smasher;
import net.insane96mcp.smasher.block.BlockSmasher;
import net.insane96mcp.smasher.lib.Names;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;

public class ModBlocks {
	
	public static BlockSmasher smasher;
	
	public static ArrayList<Block> BLOCKS = new ArrayList<Block>();
	
	public static void Init() {
		smasher = new BlockSmasher();
		smasher.setRegistryName(new ResourceLocation(Smasher.MOD_ID, Names.SMASHER));
		smasher.setCreativeTab(ModItems.CREATIVE_TAB_SMASHER);
		smasher.setHardness(7f);
		smasher.setResistance(10f);
		smasher.setHarvestLevel("pickaxe", 1);
		BLOCKS.add(smasher);
	}
	
	public static void PostInit() {
		
	}
}
