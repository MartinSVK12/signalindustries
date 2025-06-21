package sunsetsatellite.signalindustries.tiles.machines;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredContainer;
import sunsetsatellite.signalindustries.util.IOPreview;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Map;

public class TileEntityEnergyCell extends TileEntityTieredContainer implements IHasIOPreview {

    //only for infinite tier energy cell, if true, the energy cell will act as an infinite source of energy, if false, it will act as a sink destroying any energy it gets.
    //does not do anything for any other tier
    public boolean isInfiniteSource = true;

    public IOPreview preview = IOPreview.NONE;
    public TickTimer IOPreviewTimer = new TickTimer(this,this::disableIOPreview,20,false);

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

    public TileEntityEnergyCell(){
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        itemContents = new ItemStack[0];
        fluidCapacity[0] = 8000;
        transferSpeed = 50;
        fluidConnections.replace(Direction.Y_POS, Connection.INPUT);
        fluidConnections.replace(Direction.Y_NEG, Connection.OUTPUT);
        acceptedFluids.get(0).add(SIFluids.ENERGY);
    }

    @Override
    public void tick() {
        IOPreviewTimer.tick();
        if(tier == Tier.INFINITE){
            for (Map.Entry<Direction, Connection> entry : fluidConnections.entrySet()) {
                if(isInfiniteSource){
                    if(entry.getValue() == Connection.INPUT || entry.getValue() == Connection.BOTH){
                        entry.setValue(Connection.OUTPUT);
                    }
                } else {
                    if(entry.getValue() == Connection.OUTPUT || entry.getValue() == Connection.BOTH){
                        entry.setValue(Connection.INPUT);
                    }
                }
            }
            if(isInfiniteSource){
                fluidCapacity[0] = Integer.MAX_VALUE;
                transferSpeed = Integer.MAX_VALUE;
                fluidContents[0] = new FluidStack(SIFluids.ENERGY, Integer.MAX_VALUE);
            } else {
                fluidCapacity[0] = Integer.MAX_VALUE;
                transferSpeed = Integer.MAX_VALUE;
                if(fluidContents[0] != null){
                    fluidContents[0] = null;
                }
            }
        } else {
            fluidCapacity[0] = (int) Math.pow(2, tier.ordinal()) * 8000;
            transferSpeed = 50 * (tier.ordinal() + 1);
        }
        extractFluids();
        super.tick();
    }
    @Override
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
    }

    @Override
    public IOPreview getPreview() {
        return preview;
    }

    @Override
    public void setPreview(IOPreview preview) {
        this.preview = preview;
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.energyCell";
    }

    @Override
    public boolean canBeCarried(World world, Entity potentialHolder) {
        return true;
    }
}
