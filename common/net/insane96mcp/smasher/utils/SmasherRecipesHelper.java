package net.insane96mcp.smasher.utils;

import java.util.ArrayList;
import java.util.Map.Entry;

import net.insane96mcp.smasher.Smasher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import scala.collection.generic.BitOperations.Int;

public class SmasherRecipesHelper
{
    private static final SmasherRecipesHelper SMELTING_BASE = new SmasherRecipesHelper();
    private ArrayList<SmasherRecipe> recipes = new ArrayList<SmasherRecipe>();

    /**
     * Returns an instance of FurnaceRecipes.
     */
    public static SmasherRecipesHelper instance()
    {
        return SMELTING_BASE;
    }
   
    private SmasherRecipesHelper() 
    {

    }
    
    public void addRecipeItemStack(ItemStack input, ItemStack output, float experience, int cookTime)
    {
        if (getSmashingResult(input) != ItemStack.EMPTY) { 
        	Smasher.logger.info("Ignored smasher recipe with conflicting input: {} = {}", input.getItem().getRegistryName(), output.getItem().getRegistryName()); 
        	return; 
        }
        this.recipes.add(new SmasherRecipe(input, output, experience, cookTime));
    }
    
    public void addRecipe(String inputId, int inputMeta, String outputId, int outputCount, int outputMeta, float experience, int cookTime)
    {
    	Item inputItem = Item.getByNameOrId(inputId);
    	if (inputItem == null) {
        	Smasher.logger.info("Failed to parse smasher recipe input id: {}", inputId);
        	return;
    	}
    	ItemStack inputStack = new ItemStack(inputItem, 1, inputMeta);
    	
    	Item outputItem = Item.getByNameOrId(outputId);
    	if (outputItem == null) {
        	Smasher.logger.info("Failed to parse smasher recipe output id: {}", outputItem);
        	return;
    	}
    	ItemStack outputStack = new ItemStack(outputItem, outputCount, outputMeta);
    	this.addRecipeItemStack(inputStack, outputStack, experience, cookTime);
    }
    
    public void addRecipe(String inputId, String outputId, int outputCount, float experience, int cookTime)
    {
    	this.addRecipe(inputId, Short.MAX_VALUE, outputId, outputCount, Short.MAX_VALUE, experience, cookTime);
    }
    
    public void addRecipeOreDict(String inputOreDict, int inputMeta, String outputId, int outputCount, int outputMeta, float experience, int cookTime)
    {
    	for (ItemStack inputStack : OreDictionary.getOres(inputOreDict)) {
    		inputStack.setItemDamage(inputMeta);
    		Item outputItem = Item.getByNameOrId(outputId);
        	if (outputItem == null) {
            	Smasher.logger.info("Failed to parse smasher recipe output id: {}", outputId);
            	return;
        	}
    		ItemStack outputStack = new ItemStack(outputItem, outputCount, outputMeta);
    		this.addRecipeItemStack(inputStack, outputStack, experience, cookTime);
    	}
    }
    
    public void addRecipeOreDict(String inputOreDict, String outputId, int outputCount, float experience, int cookTime) {
    	this.addRecipeOreDict(inputOreDict, Short.MAX_VALUE, outputId, outputCount, Short.MAX_VALUE, experience, cookTime);
    }

    public ItemStack getSmashingResult(ItemStack stack)
    {
        for (SmasherRecipe recipe : recipes)
        {
            if (this.compareItemStacks(stack, recipe.input))
            {
                return recipe.output;
            }
        }
        
        return ItemStack.EMPTY;
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
    	return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public ArrayList<SmasherRecipe> getSmashingList()
    {
        return this.recipes;
    }

    public float getSmashingExperience(ItemStack stack)
    {
        for (SmasherRecipe recipe : recipes)
        {
            if (this.compareItemStacks(stack, recipe.output))
            {
                return recipe.experience;
            }
        }

        return 0.0F;
    }

    public int getSmashingTime(ItemStack stack)
    {
        for (SmasherRecipe recipe : recipes)
        {
            if (this.compareItemStacks(stack, recipe.input))
            {
                return recipe.cookTime;
            }
        }

        return 0;
    }
}