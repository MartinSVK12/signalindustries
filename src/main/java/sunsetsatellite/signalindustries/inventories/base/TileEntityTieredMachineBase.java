package sunsetsatellite.signalindustries.inventories.base;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import sunsetsatellite.catalyst.core.util.*;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.covers.DilithiumLensCover;
import sunsetsatellite.signalindustries.covers.SwitchCover;
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
    public TickTimer IOPreviewTimer = new TickTimer(this,this::disableIOPreview,20,false);
    public boolean disabled = false;


    @Override
    public void disableIOPreview() {
        preview = IOPreview.NONE;
    }

    @Override
    public void setTemporaryIOPreview(IOPreview preview, int ticks) {
        IOPreviewTimer.value = ticks;
        IOPreviewTimer.max = ticks;
        IOPreviewTimer.unpause();
        this.preview = preview;
    }

    @Override
    public boolean isBurning(){
        return fuelBurnTicks > 0;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void tick() {
        super.tick();
        IOPreviewTimer.tick();
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
                    Direction side = Direction.getDirectionFromSide(meta);
                    if (side.getOpposite() == dir) {
                        if(((TileEntityBooster) tile).tier == Tier.BASIC){
                            speedMultiplier = 1.5f;
                            //yield = 1.05f;
                            if(((TileEntityBooster) tile).hasCover(side, DilithiumLensCover.class)){
                                speedMultiplier = 1.75f;
                                //yield = 1.15f;
                            }
                        } else if(((TileEntityBooster) tile).tier == Tier.REINFORCED) {
                            speedMultiplier = 2;
                            //yield = 1.25f;
                            if(((TileEntityBooster) tile).hasCover(side, DilithiumLensCover.class)){
                                speedMultiplier = 2.5f;
                                //yield = 1.35f;
                            }
                        } else if (((TileEntityBooster) tile).tier == Tier.AWAKENED) {
                            speedMultiplier = 3;
                            //yield = 2f;
                            if(((TileEntityBooster) tile).hasCover(side, DilithiumLensCover.class)){
                                speedMultiplier = 4f;
                                //yield = 2.1f;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
        tag.putShort("BurnTime", (short)this.fuelBurnTicks);
        tag.putShort("ProcessTime", (short)this.progressTicks);
        tag.putShort("MaxBurnTime", (short)this.fuelMaxBurnTicks);
        tag.putInt("MaxProcessTime",this.progressMaxTicks);
        tag.putBoolean("Disabled",disabled);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        fuelBurnTicks = tag.getShort("BurnTime");
        progressTicks = tag.getShort("ProcessTime");
        progressMaxTicks = tag.getInteger("MaxProcessTime");
        fuelMaxBurnTicks = tag.getShort("MaxBurnTime");
        disabled = tag.getBoolean("Disabled");
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
    public void setFluidIOForSide(Direction dir, Connection con) {
        fluidConnections.put(dir,con);
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
    public void setItemIOForSide(Direction dir, Connection con) {
        itemConnections.put(dir,con);
    }

    @Override
    public IOPreview getPreview() {
        return preview;
    }

    @Override
    public void setPreview(IOPreview preview) {
        this.preview = preview;
    }

    public void onPoweredBlockChange(boolean powered){
        if(hasCoverAnywhere(SwitchCover.class)){
            disabled = powered;
        }
    }

}
