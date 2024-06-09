package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.signalindustries.inventories.*;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;

public class FluidTooltip extends SIBaseTooltip<IFluidInventory> {
    @Override
    public void initTooltip() {
        this.addClass(TileEntitySIFluidTank.class);
        this.addClass(TileEntityEnergyCell.class);

    }

    @Override
    public void drawAdvancedTooltip(IFluidInventory inv, AdvancedInfoComponent c) {
        drawFluids(inv,c,true);
    }
}
