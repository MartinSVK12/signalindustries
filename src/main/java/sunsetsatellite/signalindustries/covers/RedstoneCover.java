package sunsetsatellite.signalindustries.covers;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.items.covers.ItemCover;
import sunsetsatellite.signalindustries.tiles.base.TileEntityWithName;

public class RedstoneCover extends CoverBase {

    protected final String on = "signalindustries:block/redstone_cover_on";
    protected final String off = "signalindustries:block/redstone_cover_off";

    public boolean sensorActive;
    public int sensorMode = 0;
    public int sensorAmount = 0;
    public int sensorSlot = 0;
    public boolean sensorUseMeta = true;
    public boolean sensorUseData = false;
    public ItemStack sensorStack = null;

    @Override
    public void openConfiguration(Player player, Direction dir) {
        if (machine instanceof Container && machine instanceof TileEntity) {
            TileEntity tile = (TileEntity) machine;
            Catalyst.displayGui(player, tile, SignalIndustries.key("gui/redstone_cover"), Catalyst.compoundOf(new String[]{"side"}, dir.ordinal()));
        } else if (machine instanceof TileEntityWithName) {
            TileEntityWithName tile = (TileEntityWithName) machine;
            Catalyst.displayGui(player, tile, SignalIndustries.key("gui/redstone_cover"), Catalyst.compoundOf(new String[]{"side"}, dir.ordinal()));
        }
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        super.readFromNbt(tag);
        sensorActive = tag.getBoolean("IsActive");
        sensorMode = tag.getInteger("SensorMode");
        sensorSlot = tag.getInteger("SensorSlot");
        sensorAmount = tag.getInteger("CheckAmount");
        sensorUseMeta = tag.getBoolean("UseMeta");
        sensorUseData = tag.getBoolean("UseData");
        if (tag.containsKey("SensorStack")) {
            sensorStack = ItemStack.readItemStackFromNbt(tag.getCompound("SensorStack"));
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        super.writeToNbt(tag);
        tag.putBoolean("IsActive", sensorActive);
        tag.putInt("CheckAmount", sensorAmount);
        tag.putInt("SensorMode", sensorMode);
        tag.putInt("SensorSlot", sensorSlot);
        tag.putBoolean("UseMeta", sensorUseMeta);
        tag.putBoolean("UseData", sensorUseData);
        if (sensorStack != null) {
            CompoundTag itemNbt = new CompoundTag();
            sensorStack.writeToNBT(itemNbt);
            tag.putCompound("SensorStack", itemNbt);
        }
    }

    @Override
    public void tick() {
        ItemStack stack = null;
        boolean previousState = sensorActive;
        sensorActive = false;
        if (sensorSlot >= 0 && machine instanceof Container && machine instanceof TileEntity) {
            TileEntity tile = (TileEntity) machine;
            stack = ((Container) machine).getItem(sensorSlot);
            if (stack != null && sensorStack != null) {
                if (stack.itemID == sensorStack.itemID) {
                    sensorActive = checkIfValidForSensor(stack);
                }
            }
            if (tile.worldObj != null && previousState != sensorActive)
                tile.worldObj.notifyBlocksOfNeighborChange(tile.x, tile.y, tile.z, sensorActive ? 15 : 0);
        } else if (machine instanceof Container && machine instanceof TileEntity) {
            TileEntity tile = (TileEntity) machine;
            for (int i = 0; i < ((Container) machine).getContainerSize(); i++) {
                stack = ((Container) machine).getItem(i);
                if (stack != null && sensorStack != null) {
                    if (stack.itemID == sensorStack.itemID) {
                        sensorActive = checkIfValidForSensor(stack);
                        if (sensorActive) break;
                    }
                }
            }
            if (tile.worldObj != null && previousState != sensorActive)
                tile.worldObj.notifyBlocksOfNeighborChange(tile.x, tile.y, tile.z, sensorActive ? 15 : 0);
        }

    }

    private boolean checkIfValidForSensor(ItemStack stack) {
        boolean yes = false;
        switch (sensorMode) {
            case 0:
                yes = stack.stackSize == sensorAmount;
                break;
            case 1:
                yes = stack.stackSize != sensorAmount;
                break;
            case 2:
                yes = stack.stackSize > sensorAmount;
                break;
            case 3:
                yes = stack.stackSize < sensorAmount;
                break;
            case 4:
                yes = stack.stackSize >= sensorAmount;
                break;
            case 5:
                yes = stack.stackSize <= sensorAmount;
                break;
        }
        if (sensorUseMeta && stack.getMetadata() != sensorStack.getMetadata()) {
            yes = false;
        }
        if (sensorUseData && !stack.getData().equals(sensorStack.getData())) {
            yes = false;
        }
        return yes;
    }

    @Override
    public String getTexture() {
        return sensorActive ? on : off;
    }

    @Override
    public ItemCover getItem() {
        return SIItems.redstoneCover;
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        if (machine instanceof Container && machine instanceof TileEntity) {
            sensorActive = false;
            TileEntity tile = (TileEntity) machine;
            if (tile.worldObj != null) tile.worldObj.notifyBlocksOfNeighborChange(tile.x, tile.y, tile.z, 0);
        }
    }

    @Override
    public void buttonClicked(int id, int button, int channel) {
        switch (id) {
            case 2:
                if (sensorAmount > 0) sensorAmount--;
                break;
            case 1:
                sensorAmount++;
                break;
            case 3:
                sensorUseMeta = !sensorUseMeta;
                break;
            case 4:
                sensorUseData = !sensorUseData;
                break;
            case 0:
                sensorMode++;
                if (sensorMode == 6) sensorMode = 0;
                break;
            case 5:
                if (machine instanceof Container && sensorSlot < ((Container) machine).getContainerSize() - 1) {
                    sensorSlot++;
                }
                break;
            case 6:
                if (sensorSlot >= 0) sensorSlot--;
                break;
        }
    }
}
