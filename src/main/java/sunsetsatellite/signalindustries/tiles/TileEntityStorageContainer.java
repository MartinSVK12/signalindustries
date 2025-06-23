package sunsetsatellite.signalindustries.tiles;


import com.mojang.nbt.tags.ByteTag;
import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.UnlimitedItemStack;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTiered;
import sunsetsatellite.signalindustries.util.Tier;

//TODO: make better with item conduits
public class TileEntityStorageContainer extends TileEntityTiered implements Container {
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
        } else {
            nbttagcompound.put("Empty", new ByteTag());
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
            ((UnlimitedItemStack) (Object) contents).enableCustomMaxSize(getTieredCapacity());
            contents.readFromNBT(nbttagcompound.getCompound("Contents"));
            contents.stackSize = nbttagcompound.getCompound("Contents").getInteger("Count");
        } else if(nbttagcompound.containsKey("Empty")){
            contents = null;
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
            if(contents.stackSize <= 0 && !locked){
                contents = null;
            }
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
        capacity = unlimited ? Integer.MAX_VALUE : getTieredCapacity();

        if(contents != null){
            if(infinite){
                contents.stackSize = Integer.MAX_VALUE;
            }
            if(!locked && contents.stackSize <= 0){
                contents = null;
            }
        }
    }

    // Container (for mod compat)
    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot == 0) {
            if(contents == null) return null;
            ((UnlimitedItemStack)(Object) contents).enableCustomMaxSize(getTieredCapacity());
            return contents;
        }
        return null;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (unlimited) return contents.copy();
        if (slot != 0 || contents == null) return null;
        if (contents.stackSize <= amount) {
            ItemStack ret = contents.copy();
            contents.stackSize = 0;
            return ret;
        } else {
            ItemStack ret = contents.splitStack(amount);
            return ret;
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot != 0) return;
        if (locked && stack == null && contents != null) contents.stackSize = 0;
        else contents = stack;
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.storageContainer";
    }

    @Override
    public int getMaxStackSize() {
        return unlimited ? Integer.MAX_VALUE : getTieredCapacity();
    }

    @Override
    public boolean stillValid(Player player) {
        if(worldObj == null || worldObj.getTileEntity(x, y, z) != this)
        {
            return false;
        }
        return player.distanceToSqr((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
    }

    @Override
    public void sortContainer() {

    }

    @Override
    public void dropContents(World world, int x, int y, int z) {
        super.dropContents(world, x, y, z);
        for(int i = 0; i < this.getContainerSize(); i++) {
            ItemStack itemStack = this.getItem(i);
            if(itemStack == null) continue;
            EntityItem item = world.dropItem(x, y, z, itemStack);
            item.xd *= 0.5;
            item.yd *= 0.5;
            item.zd *= 0.5;
            item.pickupDelay = 0;
        }
    }

    public int getTieredCapacity(){
        switch (tier){
            case PROTOTYPE:
                return 4096;
            case BASIC:
                return 16384;
            case REINFORCED:
                return 65535;
            case AWAKENED:
                return 262140;
            case INFINITE:
                return Integer.MAX_VALUE;
        }
        return 4096;
    }

    @Override
    public boolean canBeCarried(World world, Entity potentialHolder) {
        return true;
    }
}
