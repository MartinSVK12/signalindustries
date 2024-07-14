package sunsetsatellite.signalindustries.inventories;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.catalyst.fluids.util.FluidStack;

import java.util.ArrayList;
import java.util.Map;

public class TileEntityVoidContainer extends TileEntityFluidItemContainer {

    public TileEntityVoidContainer(){
        for (Direction dir : Direction.values()) {
            fluidContents = new FluidStack[1];
            fluidCapacity = new int[]{Integer.MAX_VALUE};
            itemContents = new ItemStack[1];
            itemConnections.put(dir, Connection.INPUT);
            fluidConnections.put(dir, Connection.INPUT);
            activeItemSlots.put(dir,0);
            activeFluidSlots.put(dir,0);
            acceptedFluids.clear();
            for (FluidStack ignored : fluidContents) {
                acceptedFluids.add(new ArrayList<>(CatalystFluids.CONTAINERS.getAllFluids()));
            }
        }
    }

    public void extractFluids(){
        for (Map.Entry<Direction, Connection> e : fluidConnections.entrySet()) {
            Direction dir = e.getKey();
            Connection connection = e.getValue();
            TileEntity tile = dir.getTileEntity(worldObj,this);
            if (tile instanceof TileEntityFluidPipe) {
                moveFluids(dir, (TileEntityFluidPipe) tile);
                ((TileEntityFluidPipe) tile).rememberTicks = 100;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        extractFluids();
        worldObj.markBlockDirty(x,y,z);
        fluidContents[0] = null;
        itemContents[0] = null;
    }
}
