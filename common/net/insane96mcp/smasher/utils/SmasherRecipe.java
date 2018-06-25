package net.insane96mcp.smasher.utils;

import net.minecraft.item.ItemStack;
import scala.language;

public class SmasherRecipe {
	public ItemStack input;
	public ItemStack output;
	public float experience;
	public int cookTime;
	
	public SmasherRecipe(ItemStack input, ItemStack output, float experience, int cookTime) {
		this.input = input;
		this.output = output;
		this.experience = experience;
		this.cookTime = cookTime;
	}
	
	@Override
	public String toString() {
		return String.format("SmasherRecipe:[input=\"%s:%s\" output=\"%s:%s\" exp=%f]", input.getDisplayName(), input.getMetadata(), output.getDisplayName(), output.getMetadata(), experience);
	}
}
