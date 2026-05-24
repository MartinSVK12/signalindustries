package sunsetsatellite.signalindustries.tiles;

import net.minecraft.core.item.ItemStack;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;

import java.util.ArrayList;

public class TileEntityVoidContainer extends TileEntityFluidItemContainer {

    public TileEntityVoidContainer() {
        fluidContents = new FluidStack[1];
        itemContents = new ItemStack[1];
        fluidCapacity = new int[]{Integer.MAX_VALUE};
        for (Direction dir : Direction.values()) {
            itemConnections.put(dir, Connection.INPUT);
            fluidConnections.put(dir, Connection.INPUT);
            activeItemSlots.put(dir, 0);
            activeFluidSlots.put(dir, 0);
        }
        acceptedFluids.clear();
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>(Fluid.fluidMap.values()));
        }
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.voidContainer";
    }

	@Override
	public void sort() {

	}

	@Override
    public void tick() {
        super.tick();
        extractFluids();
        if (worldObj != null) {
            worldObj.markBlockDirty(tilePos);
        }
        fluidContents[0] = null;
        itemContents[0] = null;
    }
}
