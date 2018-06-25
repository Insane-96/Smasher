package net.insane96mcp.smasher.proxies;

import net.insane96mcp.smasher.Smasher;
import net.insane96mcp.smasher.init.ModBlocks;
import net.insane96mcp.smasher.init.ModItems;
import net.insane96mcp.smasher.lib.Config;
import net.insane96mcp.smasher.lib.Properties;
import net.insane96mcp.smasher.tileentity.GuiHandler;
import net.insane96mcp.smasher.tileentity.TileEntitySmasher;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
	public void PreInit(FMLPreInitializationEvent event) {
		Config.config = new Configuration(event.getSuggestedConfigurationFile());
		Config.SyncConfig();
		Properties.Init();

		ModBlocks.Init();
		ModItems.Init();
	}
	
	public void Init(FMLInitializationEvent event) {
		GameRegistry.registerTileEntity(TileEntitySmasher.class, "Smasher");
		NetworkRegistry.INSTANCE.registerGuiHandler(Smasher.instance, new GuiHandler());
		
		Properties.PostInit();
		
		ModItems.PostInit();
	}
	
	public void PostInit(FMLPostInitializationEvent event) {
		Config.SaveConfig();
	}
}
