package sunsetsatellite.signalindustries.tiles;


import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TileEntityIgnitor extends TileEntityFluidItemContainer implements IMultiblockPart {
    public boolean isActivated = false;
    private final TickTimer timer = new TickTimer(this, this::work, 20, true);
    public TileEntity connectedTo;

    public TileEntityIgnitor() {
        itemContents = new ItemStack[0];
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        Arrays.fill(fluidCapacity, 1000);
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        for (Direction dir : Direction.values()) {
            fluidConnections.put(dir, Connection.INPUT);
            activeFluidSlots.put(dir, 0);
        }
        transferSpeed = 10;
    }

    @Override
    public void tick() {
        super.tick();
        timer.tick();
        Random random = new Random();
        spreadFluids(Direction.Y_POS);
        worldObj.markBlocksDirty(x, y, z, x, y, z);
        extractFluids();
        if (getFluidInSlot(0) != null && getFluidInSlot(0).amount <= 0) fluidContents[0] = null;
        if (isActivated && getFluidInSlot(0) != null && getFluidInSlot(0).amount >= 5) {
            if (random.nextFloat() < 0.25) {
                float xd = random.nextFloat() / 10 - 0.05f;
                float yd = random.nextFloat() / 10 - 0.05f;
                float zd = random.nextFloat() / 10 - 0.05f;
                /*SignalIndustries.spawnParticle(new EntityFlameFX(worldObj,x+0.5,y+0.5,z,xd,yd,zd, EntityFlameFX.Type.ORANGE));
                SignalIndustries.spawnParticle(new EntityFlameFX(worldObj,x+0.5,y+0.5,z+1,xd,yd,zd, EntityFlameFX.Type.ORANGE));
                SignalIndustries.spawnParticle(new EntityFlameFX(worldObj,x,y+0.5,z+0.5,xd,yd,zd, EntityFlameFX.Type.ORANGE));
                SignalIndustries.spawnParticle(new EntityFlameFX(worldObj,x+1,y+0.5,z+0.5,xd,yd,zd, EntityFlameFX.Type.ORANGE));*/
            }
        }
    }

    public void work() {
        if (isActivated && getFluidInSlot(0) != null && getFluidInSlot(0).amount >= 5) {
            getFluidInSlot(0).amount -= 5;
        }
    }

    public void spreadFluids(Direction dir) {
        for (Direction direction : Direction.values()) {
            fluidConnections.put(dir, Connection.BOTH);
        }
        if (getFluidInSlot(0) != null) {
            this.give(dir);
        }
        for (Direction direction : Direction.values()) {
            fluidConnections.put(dir, Connection.INPUT);
        }
    }

    public boolean isBurning() {
        return fluidContents[0] != null && fluidContents[0].fluid == SIFluids.ENERGY && fluidContents[0].amount > 0 && isActivated;
    }

    public boolean isEmpty() {
        return fluidContents[0] == null || fluidContents[0].fluid == SIFluids.ENERGY && fluidContents[0].amount == 0;
    }

    public boolean isReady() {
        return fluidContents[0] != null && fluidContents[0].fluid == SIFluids.ENERGY && fluidContents[0].amount >= fluidCapacity[0];
    }

    @Override
    public boolean isConnected() {
        return connectedTo != null;
    }

    @Override
    public TileEntity getConnectedTileEntity() {
        return connectedTo;
    }

    @Override
    public boolean connect(TileEntity tileEntity) {
        connectedTo = tileEntity;
        return true;
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.ignitor";
    }
}
