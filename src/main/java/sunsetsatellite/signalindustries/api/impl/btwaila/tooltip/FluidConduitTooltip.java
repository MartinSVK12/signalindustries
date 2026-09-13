package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityFluidConduit;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;

public class FluidConduitTooltip extends SIBaseTooltip<TileEntityFluidConduit> {
    @Override
    public void initTooltip() {
        addClass(TileEntityFluidConduit.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityFluidConduit conduit, AdvancedInfoComponent c) {
		FluidStack stack = null;
		if(conduit.fluid != null){
			stack = new FluidStack(conduit.fluid, conduit.getFluidAmount());
		}
		int sidesConnected = 0;
		for (Direction dir : Direction.values()) {
			if(conduit.isPipeConnected(dir)){
				sidesConnected++;
			}
		}
		drawFluid(stack, conduit.getCapacity() * (sidesConnected+1), c, false);
    }
}
