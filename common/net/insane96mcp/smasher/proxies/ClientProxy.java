package net.insane96mcp.smasher.proxies;

import net.insane96mcp.smasher.init.ModItems;
import net.insane96mcp.smasher.item.ItemPulverized;
import net.insane96mcp.smasher.item.ItemPulverizedColor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy{

	@Override
	public void PreInit(FMLPreInitializationEvent event) {
		super.PreInit(event);
	}

	@Override
	public void Init(FMLInitializationEvent event) {
		super.Init(event);
	}

	@Override
	public void PostInit(FMLPostInitializationEvent event) {
		//Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemPulverizedColor(), ModItems.pulverizedItems.toArray(new ItemPulverized[ModItems.pulverizedItems.size()]));
		super.PostInit(event);
	}
	
}
