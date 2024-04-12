package sunsetsatellite.signalindustries.inventories;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.interfaces.mixins.INBTCompound;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTiered;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityStorageContainer extends TileEntityTiered {
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
        nbttagcompound.putBoolean("locked", locked);
        if(contents != null){
            CompoundTag contentsTag = new CompoundTag();
            contents.writeToNBT(contentsTag);
            nbttagcompound.put("Contents", contentsTag);
        }
    }

    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        infinite = nbttagcompound.getBoolean("Infinite");
        unlimited = nbttagcompound.getBoolean("Unlimited");
        locked = nbttagcompound.getBoolean("locked");
        if(nbttagcompound.containsKey("Contents")){
            contents = ItemStack.readItemStackFromNbt(nbttagcompound.getCompound("Contents"));
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
}
