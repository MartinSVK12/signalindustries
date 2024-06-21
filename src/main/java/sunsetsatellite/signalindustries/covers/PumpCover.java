package sunsetsatellite.signalindustries.covers;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
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
        if(machine instanceof TileEntityFluidContainer){
            TileEntityFluidContainer fluidContainer = (TileEntityFluidContainer) machine;
            Connection con = fluidContainer.getConnection(dir);
            int activeSlot = fluidContainer.getActiveFluidSlot(dir);
            if(activeSlot == -1 || con == Connection.NONE) return;
            TileEntity tile = dir.getTileEntity(fluidContainer.worldObj, fluidContainer);
            if(tile instanceof TileEntityFluidContainer){
                TileEntityFluidContainer other = (TileEntityFluidContainer) tile;
                Connection otherCon = other.getConnection(dir.getOpposite());
                int otherActiveSlot = other.getActiveFluidSlot(dir.getOpposite());
                if(otherCon == Connection.NONE || otherActiveSlot == -1) return;
                if(con == Connection.INPUT && otherCon == Connection.OUTPUT){
                    if(other.getFluidInSlot(otherActiveSlot) != null){
                        fluidContainer.take(other.getFluidInSlot(otherActiveSlot),dir);
                    }
                } else if (con == Connection.OUTPUT && otherCon == Connection.INPUT) {
                    if(fluidContainer.getFluidInSlot(activeSlot) != null){
                        fluidContainer.give(dir);
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
