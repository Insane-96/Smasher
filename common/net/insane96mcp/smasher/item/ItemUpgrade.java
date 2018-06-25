package net.insane96mcp.smasher.item;

import java.util.List;

import net.insane96mcp.smasher.lib.Properties;
import net.insane96mcp.smasher.lib.Translatable;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemUpgrade extends Item {
	
	public UpgradeType type;
	
	public ItemUpgrade(ResourceLocation name, CreativeTabs tab, UpgradeType type) {
		setRegistryName(name);
		setCreativeTab(tab);
		setUnlocalizedName(name.toString());
		this.type = type;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (!Properties.Upgrade.enableUpgrades)
			tooltip.add(I18n.format(Translatable.Upgrade.upgradesDisabled));
		else {
			switch (type) {
			case SPEED:
				tooltip.add(I18n.format(Translatable.Upgrade.speedUpgradeIncrease, Properties.Upgrade.Speed.speedBonus));
				tooltip.add(I18n.format(Translatable.Upgrade.speedUpgradeFuel, Properties.Upgrade.Speed.fuelConsumption));
				break;

			default:
				break;
			}
			tooltip.add(" ");
			tooltip.add(I18n.format(Translatable.Upgrade.maxUpgradeCount, this.maxStackSize));
		}
	}
	
	public enum UpgradeType {
		SPEED,
		EXPERIENCE
	}
}
