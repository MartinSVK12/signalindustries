package sunsetsatellite.signalindustries.covers;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IFluidIO;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.api.IFluidTransfer;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IAcceptsCovers;
import sunsetsatellite.signalindustries.items.covers.ItemCover;

public class PumpCover extends CoverBase {

    protected final IconCoordinate texture = TextureRegistry.getTexture("signalindustries:block/pump_cover");

    @Override
    public void openConfiguration(EntityPlayer player) {

    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        super.writeToNbt(tag);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        super.readFromNbt(tag);
    }

    @Override
    public void tick() {
        if(machine instanceof IFluidIO && machine instanceof IFluidTransfer && machine instanceof IFluidInventory && machine instanceof TileEntity){
            TileEntity machineTile = (TileEntity) machine;
            IFluidIO fluidIO = (IFluidIO) machine;
            IFluidTransfer fluidTransfer = (IFluidTransfer) machine;
            IFluidInventory fluidInv = (IFluidInventory) machine;
            Connection con = fluidIO.getFluidIOForSide(dir);
            int activeSlot = fluidIO.getActiveFluidSlotForSide(dir);
            if(activeSlot == -1 || con == Connection.NONE) return;
            TileEntity tile = dir.getTileEntity(machineTile.worldObj, machineTile);
            if(tile instanceof IFluidIO && tile instanceof IFluidTransfer && tile instanceof IFluidInventory){
                IFluidIO otherIO = (IFluidIO) tile;
                IFluidTransfer otherTransfer = (IFluidTransfer) tile;
                IFluidInventory otherFluidInv = (IFluidInventory) tile;
                Connection otherCon = otherIO.getFluidIOForSide(dir.getOpposite());
                int otherActiveSlot = otherIO.getActiveFluidSlotForSide(dir.getOpposite());
                if(otherCon == Connection.NONE || otherActiveSlot == -1) return;
                if(con == Connection.INPUT && otherCon == Connection.OUTPUT){
                    if(otherFluidInv.getFluidInSlot(otherActiveSlot) != null){
                        fluidTransfer.take(otherFluidInv.getFluidInSlot(otherActiveSlot),dir);
                    }
                } else if (con == Connection.OUTPUT && otherCon == Connection.INPUT) {
                    if(fluidInv.getFluidInSlot(activeSlot) != null){
                        fluidTransfer.give(dir);
                    }
                }

            }
        }
    }

    @Override
    public IconCoordinate getTexture() {
        return texture;
    }

    @Override
    public ItemCover getItem() {
        return SIItems.pumpCover;
    }

    @Override
    public void onInstalled(Direction dir, IAcceptsCovers machine, EntityPlayer player) {
        player.sendMessage("Cover installed!");
        super.onInstalled(dir, machine, player);
    }

    @Override
    public void onRemoved(EntityPlayer player) {
        player.sendMessage("Cover removed!");
        super.onRemoved(player);
    }
}
