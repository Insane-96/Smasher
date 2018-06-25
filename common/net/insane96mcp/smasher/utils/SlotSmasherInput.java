package net.insane96mcp.smasher.utils;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSmasherInput extends Slot{

    public SlotSmasherInput(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition)
    {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(ItemStack stack)
    {
    	if (SmasherRecipesHelper.instance().getSmashingResult(stack).isEmpty())
        {
            return false;
        }
    	return true;
    }
}
