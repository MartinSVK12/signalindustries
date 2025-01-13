package sunsetsatellite.signalindustries.inventories;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.UnlimitedItemStack;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTiered;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityStorageContainer extends TileEntityTiered implements IInventory {
    public ItemStack contents = null;
    public int capacity = 4096;
    public boolean infinite = false;
    public boolean unlimited = false;
    public boolean locked = false;

    public TileEntityStorageContainer() {

    }

    @Override
    public void writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putBoolean("Infinite", infinite);
        nbttagcompound.putBoolean("Unlimited", unlimited);
        nbttagcompound.putBoolean("Locked", locked);
        if(contents != null){
            CompoundTag contentsTag = new CompoundTag();
            contentsTag.putShort("id", (short)contents.itemID);
            contentsTag.putInt("Count", contents.stackSize);
            contentsTag.putShort("Damage", (short)contents.getMetadata());
            contentsTag.putByte("Expanded", (byte)1);
            contentsTag.putInt("Version", 19134);
            if (!contents.getData().getValue().isEmpty()) {
                contentsTag.putCompound("Data", contents.getData());
            }
            nbttagcompound.put("Contents", contentsTag);
        }
    }

    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        infinite = nbttagcompound.getBoolean("Infinite");
        unlimited = nbttagcompound.getBoolean("Unlimited");
        locked = nbttagcompound.getBoolean("Locked");
        if(nbttagcompound.containsKey("Contents")){
            contents = new ItemStack(1,1,0);
            ((UnlimitedItemStack) (Object) contents).enableCustomMaxSize((int) (4096 * (Math.pow(2, tier.ordinal()))));
            contents.readFromNBT(nbttagcompound.getCompound("Contents"));
            contents.stackSize = nbttagcompound.getCompound("Contents").getInteger("Count");
        }
    }

    public boolean insertStack(ItemStack stack) {
        if(stack == null) return false;
        if(infinite){
            contents = stack.copy();
            if(contents.isItemEqual(stack)){
                stack.stackSize = 0;
            }
            return true;
        }
        if(contents == null){
            if(capacity >= stack.stackSize){
                contents = stack.copy();
                stack.stackSize = 0;
                return true;
            } else {
                contents = stack.copy();
                contents.stackSize = capacity;
                stack.stackSize -= capacity;
                return true;
            }
        } else {
            if(!(contents.isItemEqual(stack))) return false;
            if(!(contents.getData()).equals(stack.getData())) return false;
            if(contents.stackSize >= capacity) return false;
            if(contents.stackSize+stack.stackSize > capacity){
                int remainder = (contents.stackSize+stack.stackSize) - capacity;
                contents.stackSize = capacity;
                stack.stackSize -= remainder;
            } else {
                contents.stackSize += stack.stackSize;
                stack.stackSize = 0;
            }
            return true;
        }
    }

    //extracts maximum amount possible for the stack
    public ItemStack extractStack(){
        if(contents == null) return null;
        if(contents.stackSize <= 0) return null;
        if(contents.stackSize <= contents.getMaxStackSize()){
            ItemStack stack = contents.copy();
            if(!locked){
                contents = null;
            } else {
                contents.stackSize = 0;
            }
            return stack;
        } else {
            ItemStack stack = contents.copy();
            stack.stackSize = contents.getMaxStackSize();
            contents.stackSize -= contents.getMaxStackSize();
            return stack;
        }
    }

    //extracts specific amount capped bu the items max stack size
    public ItemStack extractStack(int amount){
        if(contents == null) return null;
        if(contents.stackSize <= 0) return null;
        amount = Math.min(contents.getMaxStackSize(),amount);
        if(contents.stackSize <= contents.getMaxStackSize() && contents.stackSize <= amount){
            ItemStack stack = contents.copy();
            if(!locked){
                contents = null;
            } else {
                contents.stackSize = 0;
            }
            return stack;
        } else {
            ItemStack stack = contents.copy();
            stack.stackSize = amount;
            contents.stackSize -= amount;
            return stack;
        }
    }

    @Override
    public void tick() {
        super.tick();
        worldObj.markBlocksDirty(x,y,z,x,y,z);
        if(tier == Tier.INFINITE){
            infinite = true;
            unlimited = true;
        }
        capacity = unlimited ? Integer.MAX_VALUE : (int) (4096 * (Math.pow(2, tier.ordinal())));

        if(contents != null){
            if(infinite){
                contents.stackSize = Integer.MAX_VALUE;
            }
            if(!locked && contents.stackSize <= 0){
                contents = null;
            }
        }
    }

    // IInventory (for mod compat)
    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int paramInt) {
        if (paramInt == 0) {
            if(contents == null) return null;
            ((UnlimitedItemStack)(Object) contents).enableCustomMaxSize((int) (4096 * (Math.pow(2, tier.ordinal()))));
            return contents;
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int paramInt1, int paramInt2) {
        if (unlimited) return contents.copy();
        if (paramInt1 != 0 || contents == null) return null;
        if (contents.stackSize <= paramInt2) {
            ItemStack ret = contents.copy();
            contents.stackSize = 0;
            return ret;
        } else {
            ItemStack ret = contents.splitStack(paramInt2);
            return ret;
        }
    }

    @Override
    public void setInventorySlotContents(int paramInt, ItemStack paramItemStack) {
        if (paramInt != 0) return;
        if (locked && paramItemStack == null && contents != null) contents.stackSize = 0;
        else contents = paramItemStack;
    }

    @Override
    public String getInvName() {
        return "SI:StorageContainer";
    }

    @Override
    public int getInventoryStackLimit() {
        return unlimited ? Integer.MAX_VALUE : (int) (4096 * (Math.pow(2, tier.ordinal())));
    }

    @Override
    public void onInventoryChanged() {}

    @Override
    public boolean canInteractWith(EntityPlayer paramEntityPlayer) {
        return false;
    }

    @Override
    public void sortInventory() {}
}
