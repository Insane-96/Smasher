package net.insane96mcp.smasher.tileentity;

import javax.annotation.Nullable;

import net.insane96mcp.smasher.Smasher;
import net.insane96mcp.smasher.init.ModBlocks;
import net.insane96mcp.smasher.init.ModItems;
import net.insane96mcp.smasher.lib.Names;
import net.insane96mcp.smasher.lib.Properties;
import net.insane96mcp.smasher.utils.SmasherRecipesHelper;
import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.reflect.internal.Trees.This;

public class TileEntitySmasher extends TileEntityLockable implements ITickable, ISidedInventory {

	private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {2};
    private static final int[] SLOTS_SIDES = new int[] {1};
    private NonNullList<ItemStack> smasherItemStacks = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
    private int burnTime;
    private int currentItemBurnTime;
    private int upgradelessCurrentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private boolean isPoweredByFire;
    private String customName;
	
	public TileEntitySmasher() { }

	@Override
	public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.smasherItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.smasherItemStacks);
        this.burnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");
        this.currentItemBurnTime = compound.getInteger("CurrentItemBurnTime");
        this.upgradelessCurrentItemBurnTime = compound.getInteger("UpgradelessCurrentItemBurnTime");

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
        compound.setInteger("BurnTime", (short)this.burnTime);
        compound.setInteger("CookTime", (short)this.cookTime);
        compound.setInteger("CookTimeTotal", (short)this.totalCookTime);
        compound.setInteger("CurrentItemBurnTime", this.currentItemBurnTime);
        compound.setInteger("UpgradelessCurrentItemBurnTime", this.upgradelessCurrentItemBurnTime);
        ItemStackHelper.saveAllItems(compound, this.smasherItemStacks);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        return compound;
	}

	@Override
	public void update() {
		boolean isBurning = this.isBurning();
        boolean markDirty = false;
        
        ItemStack upgradeSlotStack = this.getStackInSlot(3);
        //boolean hasSpeedUpgrade = upgradeSlotStack.getItem().equals(ModItems.speedUpgrade);
        
        //System.out.println(this.totalCookTime + " " + this.getCookTime(this.smasherItemStacks.get(0)));
        if (this.totalCookTime != this.getCookTime(this.smasherItemStacks.get(0))) {
        	
        	float cookTimeRatio = (float)this.cookTime / (float)this.totalCookTime;

            this.totalCookTime = this.getCookTime(this.smasherItemStacks.get(0));
            this.cookTime = (int) (this.totalCookTime * cookTimeRatio);
            this.markDirty();
        }
        
        int gotBurnTime = this.upgradelessCurrentItemBurnTime;
        for (int i = 0; i < upgradeSlotStack.getCount(); i++) {
        	gotBurnTime *= 1f - (Properties.Upgrade.Speed.fuelConsumption / 100f);
        }
        if (gotBurnTime != this.currentItemBurnTime) {
        	float burnTimeRatio = (float)this.burnTime / (float)this.currentItemBurnTime;

            this.currentItemBurnTime = gotBurnTime;
            this.burnTime = (int) (this.currentItemBurnTime * burnTimeRatio);
            this.markDirty();
        }

        if (this.isBurning())
        {
            --this.burnTime;
        }

        if (!this.world.isRemote)
        {
            ItemStack itemstack = this.smasherItemStacks.get(1);

            if (this.isBurning() || !itemstack.isEmpty() && !((ItemStack)this.smasherItemStacks.get(0)).isEmpty())
            {
                if (!this.isBurning() && this.canSmash())
                {
                    this.burnTime = TileEntityFurnace.getItemBurnTime(itemstack);
                    this.currentItemBurnTime = this.burnTime;
                    this.upgradelessCurrentItemBurnTime = this.burnTime;

                    if (this.isBurning())
                    {
                        markDirty = true;

                        if (!itemstack.isEmpty())
                        {
                            Item item = itemstack.getItem();
                            itemstack.shrink(1);

                            if (itemstack.isEmpty())
                            {
                                ItemStack item1 = item.getContainerItem(itemstack);
                                this.smasherItemStacks.set(1, item1);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmash())
                {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime)
                    {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(this.smasherItemStacks.get(0));
                        this.smashItem();
                        markDirty = true;
                    }
                }
                else
                {
                    this.cookTime = 0;
                }
            }

            if (isBurning != this.isBurning())
            {
                markDirty = true;
                ModBlocks.smasher.setState(this.isBurning(), this.world, this.pos);
            }
        }

        if (markDirty)
        {
            this.markDirty();
        }
	}
	
	private boolean canSmash()
    {
        if (((ItemStack)this.smasherItemStacks.get(0)).isEmpty())
        {
            return false;
        }
        else
        {
            ItemStack smashingResult = SmasherRecipesHelper.instance().getSmashingResult(this.smasherItemStacks.get(0));
            
            if (smashingResult.isEmpty())
            {
                return false;
            }
            else
            {
                ItemStack outputStack = this.smasherItemStacks.get(2);

                if (outputStack.isEmpty())
                {
                    return true;
                }
                else if (!outputStack.isItemEqual(smashingResult))
                {
                    return false;
                }
                else if (outputStack.getCount() + smashingResult.getCount() <= this.getInventoryStackLimit() && outputStack.getCount() + smashingResult.getCount() <= outputStack.getMaxStackSize())
                {
                    return true;
                }
                else
                {
                    return outputStack.getCount() + smashingResult.getCount() <= smashingResult.getMaxStackSize();
                }
            }
        }
    }
	
	public void smashItem()
    {
        if (this.canSmash())
        {
            ItemStack itemstack = this.smasherItemStacks.get(0);
            ItemStack itemstack1 = SmasherRecipesHelper.instance().getSmashingResult(itemstack);
            ItemStack itemstack2 = this.smasherItemStacks.get(2);

            if (itemstack2.isEmpty())
            {
                this.smasherItemStacks.set(2, itemstack1.copy());
            }
            else if (itemstack2.getItem() == itemstack1.getItem())
            {
                itemstack2.grow(itemstack1.getCount());
            }

            if (itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && itemstack.getMetadata() == 1 && !((ItemStack)this.smasherItemStacks.get(1)).isEmpty() && ((ItemStack)this.smasherItemStacks.get(1)).getItem() == Items.BUCKET)
            {
                this.smasherItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
        }
    }
	
	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		sendUpdates();
	}

	private void sendUpdates() {
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, getState(), getState(), 3);
		world.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
		markDirty();
	}
	
	private IBlockState getState() {
		return world.getBlockState(pos);
	}

	@Override
	public int getSizeInventory() {
		return this.smasherItemStacks.size();
	}

	@Override
	public boolean isEmpty() {

        for (ItemStack itemstack : this.smasherItemStacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.smasherItemStacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.smasherItemStacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.smasherItemStacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = this.smasherItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.smasherItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            this.totalCookTime = this.getCookTime(stack);
            this.cookTime = 0;
            this.markDirty();
        }        
	}

	private int getCookTime(ItemStack stack) {
		int cookTime = SmasherRecipesHelper.instance().getSmashingTime(stack);//Properties.General.deafultCookTime;
		ItemStack upgradeSlotStack = this.smasherItemStacks.get(3);
		if (upgradeSlotStack.getItem().equals(ModItems.speedUpgrade)) {
			for (int i = 0; i < upgradeSlotStack.getCount(); i++) {
				cookTime *= 1f - (Properties.Upgrade.Speed.speedBonus / 100f);
			}
		}
		return Math.max(1, cookTime);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
	}

	@Override
	public void openInventory(EntityPlayer player) {
		
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 2)
        {
            return false;
        }
        else if (index != 1)
        {
            return true;
        }
        else
        {
            ItemStack itemstack = this.smasherItemStacks.get(1);
            return TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && itemstack.getItem() != Items.BUCKET;
        }
	}

	@Override
	public int getField(int id) {
        switch (id)
        {
            case 0:
                return this.burnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            case 4:
            	return this.upgradelessCurrentItemBurnTime;
            default:
                return 0;
        }
	}

	@Override
	public void setField(int id, int value) {
        switch (id)
        {
            case 0:
                this.burnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
                break;
            case 4:
            	this.upgradelessCurrentItemBurnTime = value;
        }
	}

	@Override
	public int getFieldCount() {
		return 5;
	}

	@Override
	public void clear() {
		this.smasherItemStacks.clear();
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.smasher"; 
	}

	@Override
	public boolean hasCustomName() {
		return this.customName != null && !this.customName.isEmpty();
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerSmasher(playerInventory, this);
	}

	@Override
	public String getGuiID() {
        return Smasher.RESOURCE_PREFIX + Names.SMASHER;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.DOWN)
        {
            return SLOTS_BOTTOM;
        }
        else
        {
            return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (direction == EnumFacing.DOWN && index == 1)
        {
            Item item = stack.getItem();

            if (item != Items.WATER_BUCKET && item != Items.BUCKET)
            {
                return false;
            }
        }
        
        return false;
	}

	public boolean isBurning() {
		return this.burnTime > 0;
	}
	
	public boolean isSmashing() {
		return cookTime > 0 && isBurning();
	}

    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }
}