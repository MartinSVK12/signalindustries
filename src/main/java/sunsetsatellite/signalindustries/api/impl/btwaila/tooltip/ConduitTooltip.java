package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityConduit;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;

public class ConduitTooltip extends SIBaseTooltip<TileEntityConduit> {
    @Override
    public void initTooltip() {
        addClass(TileEntityConduit.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityConduit conduit, AdvancedInfoComponent c) {
		/*for (Map.Entry<TileEntityFluidPipe.Orientation, TileEntityFluidPipe.Section> entry : conduit.sections.entrySet()) {
		    TileEntityFluidPipe.Orientation o = entry.getKey();
			TileEntityFluidPipe.Section s = entry.getValue();
			Connection con;
			if(o == TileEntityFluidPipe.Orientation.CENTER){
				con = Connection.BOTH;
			}
			else {
				con = conduit.internalConnections.get(o.dir);
			}

			c.drawStringWithShadow(o+": "+ TextFormatting.RED +s.amount + TextFormatting.WHITE + " | " + TextFormatting.YELLOW + s.counter + TextFormatting.WHITE + " | " + con,0);
		}*/
		int sidesConnected = 0;
		for (Direction dir : Direction.values()) {
			if(conduit.isPipeConnected(dir)){
				sidesConnected++;
			}
		}
		FluidStack stack = null;
		if(conduit.fluid != null){
			stack = new FluidStack(conduit.fluid, conduit.getFluidAmount());
		}
        drawFluid(stack, conduit.getCapacity() * (sidesConnected + 1), c, false);
    }
}
