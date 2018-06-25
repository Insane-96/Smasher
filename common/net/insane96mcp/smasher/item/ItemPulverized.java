package net.insane96mcp.smasher.item;

import net.insane96mcp.smasher.item.ItemUpgrade.UpgradeType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ItemPulverized extends Item {
	
	public String ingotName;
	public int color;

	public ItemPulverized(ResourceLocation name, CreativeTabs tab, String ingotName, int color) {
		setRegistryName(name);
		setCreativeTab(tab);
		setUnlocalizedName(name.toString());
		
		this.ingotName = ingotName;
		this.color = color;
	}
}
