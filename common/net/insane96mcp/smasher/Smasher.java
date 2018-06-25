package net.insane96mcp.smasher;

import org.apache.logging.log4j.Logger;

import net.insane96mcp.smasher.proxies.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Smasher.MOD_ID, name = Smasher.MOD_NAME, version = Smasher.VERSION, acceptedMinecraftVersions = Smasher.MINECRAFT_VERSIONS)
public class Smasher {
	
	public static final String MOD_ID = "smasher";
	public static final String MOD_NAME = "Smasher";
	public static final String VERSION = "1.0.0";
	public static final String RESOURCE_PREFIX = MOD_ID.toLowerCase() + ":";
	public static final String MINECRAFT_VERSIONS = "[1.12,1.12.2]";
	
	@Instance(MOD_ID)
	public static Smasher instance;
	
	@SidedProxy(clientSide = "net.insane96mcp.smasher.proxies.ClientProxy", serverSide = "net.insane96mcp.smasher.proxies.ServerProxy")
	public static CommonProxy proxy;
	
	public static Logger logger;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		
		proxy.PreInit(event);
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event) {
		proxy.Init(event);
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent event) {
		proxy.PostInit(event);
	}
}
