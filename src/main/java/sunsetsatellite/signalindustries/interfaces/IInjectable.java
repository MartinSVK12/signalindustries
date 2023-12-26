package sunsetsatellite.signalindustries.interfaces;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;

public interface IInjectable {
    void fill(FluidStack fluidStack, ItemStack stack, TileEntityFluidContainer tile, int maxAmount);

    boolean canFill(ItemStack stack);
}
