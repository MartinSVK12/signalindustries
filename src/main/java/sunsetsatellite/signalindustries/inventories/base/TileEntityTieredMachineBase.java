package sunsetsatellite.signalindustries.inventories.base;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IFluidIO;
import sunsetsatellite.catalyst.core.util.IItemIO;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityBooster;
import sunsetsatellite.signalindustries.util.IOPreview;
import sunsetsatellite.signalindustries.util.Tier;


public class TileEntityTieredMachineBase extends TileEntityTieredContainer implements IFluidIO, IItemIO, IHasIOPreview, IActiveForm {
    public int fuelBurnTicks = 0;
    public int fuelMaxBurnTicks = 0;
    public int progressTicks = 0;
    public int progressMaxTicks = 200;
    public float speedMultiplier = 1;
    public float yield = 1;
    public IOPreview preview = IOPreview.NONE;

    @Override
    public boolean isBurning(){
        return fuelBurnTicks > 0;
    }

    @Override
    public void tick() {
        super.tick();
        BlockContainerTiered block = (BlockContainerTiered) getBlockType();
        if(block != null){
            applyModifiers();
        }
    }

    public void applyModifiers(){
        speedMultiplier = 1;
        yield = 1;
        for(Direction dir : Direction.values()) {
            TileEntity tile = dir.getTileEntity(worldObj, this);
            if (tile instanceof TileEntityBooster && this instanceof IBoostable) {
                if (((TileEntityBooster) tile).isBurning()) {
                    int meta = tile.getMovedData();
                    if (Direction.getDirectionFromSide(meta).getOpposite() == dir) {
                        if(((TileEntityBooster) tile).tier == Tier.BASIC){
                            speedMultiplier = 1.5f;
                            yield = 1.05f;
                        } else if(((TileEntityBooster) tile).tier == Tier.REINFORCED) {
                            speedMultiplier = 2;
                            yield = 1.25f;
                        } else if (((TileEntityBooster) tile).tier == Tier.AWAKENED) {
                            speedMultiplier = 3;
                            yield = 2f;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag nBTTagCompound1) {
        super.writeToNBT(nBTTagCompound1);
        nBTTagCompound1.putShort("BurnTime", (short)this.fuelBurnTicks);
        nBTTagCompound1.putShort("ProcessTime", (short)this.progressTicks);
        nBTTagCompound1.putShort("MaxBurnTime", (short)this.fuelMaxBurnTicks);
        nBTTagCompound1.putInt("MaxProcessTime",this.progressMaxTicks);
    }

    @Override
    public void readFromNBT(CompoundTag nBTTagCompound1) {
        super.readFromNBT(nBTTagCompound1);
        fuelBurnTicks = nBTTagCompound1.getShort("BurnTime");
        progressTicks = nBTTagCompound1.getShort("ProcessTime");
        progressMaxTicks = nBTTagCompound1.getInteger("MaxProcessTime");
        fuelMaxBurnTicks = nBTTagCompound1.getShort("MaxBurnTime");
    }

    public int getProgressScaled(int paramInt) {
        return this.progressTicks * paramInt / progressMaxTicks;
    }

    public int getBurnTimeRemainingScaled(int paramInt) {
        if(this.fuelMaxBurnTicks == 0) {
            this.fuelMaxBurnTicks = 200;
        }
        return this.fuelBurnTicks * paramInt / this.fuelMaxBurnTicks;
    }

    @Override
    public int getActiveFluidSlotForSide(Direction dir) {
        return activeFluidSlots.get(dir);
    }

    @Override
    public Connection getFluidIOForSide(Direction dir) {
        return fluidConnections.get(dir);
    }

    @Override
    public int getActiveItemSlotForSide(Direction dir) {
        if(activeItemSlots.get(dir) == -1){
            if(itemConnections.get(dir) == Connection.INPUT){
                for (int i = 0; i < itemContents.length; i++) {
                    ItemStack content = itemContents[i];
                    if (content == null) {
                        return i;
                    }
                }
            } else if(itemConnections.get(dir) == Connection.OUTPUT) {
                for (int i = 0; i < itemContents.length; i++) {
                    ItemStack content = itemContents[i];
                    if (content != null) {
                        return i;
                    }
                }
            }
            return 0;
        } else {
            return activeItemSlots.get(dir);
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        return new Packet140TileEntityData(this);
    }

    @Override
    public int getActiveItemSlotForSide(Direction dir, ItemStack stack) {
        if(activeItemSlots.get(dir) == -1){
            if(itemConnections.get(dir) == Connection.INPUT){
                for (int i = 0; i < itemContents.length; i++) {
                    ItemStack content = itemContents[i];
                    if (content == null || (content.isItemEqual(stack) && content.stackSize+stack.stackSize <= content.getMaxStackSize())) {
                        return i;
                    }
                }
            } else if(itemConnections.get(dir) == Connection.OUTPUT) {
                for (int i = 0; i < itemContents.length; i++) {
                    ItemStack content = itemContents[i];
                    if (content != null) {
                        return i;
                    }
                }
            }
            return 0;
        } else {
            return activeItemSlots.get(dir);
        }
    }

    @Override
    public Connection getItemIOForSide(Direction dir) {
        return itemConnections.get(dir);
    }

    @Override
    public IOPreview getPreview() {
        return preview;
    }

    @Override
    public void setPreview(IOPreview preview) {
        this.preview = preview;
    }
}
