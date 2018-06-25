package net.insane96mcp.smasher.utils;

import net.insane96mcp.smasher.item.ItemUpgrade;
import net.insane96mcp.smasher.lib.Properties;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSmasherUpgrade extends Slot{

    public SlotSmasherUpgrade(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition)
    {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem() instanceof ItemUpgrade && Properties.Upgrade.enableUpgrades;	
    }
}
