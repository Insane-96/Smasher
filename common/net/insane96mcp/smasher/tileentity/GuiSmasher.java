package net.insane96mcp.smasher.tileentity;

import net.insane96mcp.smasher.Smasher;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSmasher extends GuiContainer
{
    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Smasher.MOD_ID, "textures/gui/container/smasher.png");
    /** The player inventory bound to this GUI. */
    private final InventoryPlayer playerInventory;
    private final IInventory tileSmasher;

    public GuiSmasher(InventoryPlayer playerInv, IInventory furnaceInv)
    {
        super(new ContainerSmasher(playerInv, furnaceInv));
        this.playerInventory = playerInv;
        this.tileSmasher = furnaceInv;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.tileSmasher.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        if (TileEntitySmasher.isBurning(this.tileSmasher))
        {
            int k = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(i + 9, j + 41 - k, 176, 12 - k, 14, k + 1);
        }

        int l = this.getCookProgressScaled(22);
        this.drawTexturedModalRect(i + 70, j + 32, 176, 14, l + 1, 22);
    }

    private int getCookProgressScaled(int pixels)
    {
        int i = this.tileSmasher.getField(2);
        int j = this.tileSmasher.getField(3);
        //return 11;
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    private int getBurnLeftScaled(int pixels)
    {
        int i = this.tileSmasher.getField(1);

        if (i == 0)
        {
            i = 200;
        }

        return this.tileSmasher.getField(0) * pixels / i;
    }
}
