package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidPipe;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityFluidConduit;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;

import java.util.Map;

public class FluidConduitTooltip extends SIBaseTooltip<TileEntityFluidConduit> {
    @Override
    public void initTooltip() {
        addClass(TileEntityFluidConduit.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityFluidConduit conduit, AdvancedInfoComponent c) {
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
