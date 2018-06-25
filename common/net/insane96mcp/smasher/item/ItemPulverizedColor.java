package net.insane96mcp.smasher.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class ItemPulverizedColor implements IItemColor {

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		if (stack.getItem() instanceof ItemPulverized) {
			ItemPulverized pulverized = (ItemPulverized) stack.getItem();
			return pulverized.color;
		}
		return 0;
	}
}
