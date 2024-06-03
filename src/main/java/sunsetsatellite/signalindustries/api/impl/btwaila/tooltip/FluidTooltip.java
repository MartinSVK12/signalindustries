package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.inventories.*;
import toufoumaster.btwaila.BTWaila;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;
import toufoumaster.btwaila.tooltips.Tooltip;
import toufoumaster.btwaila.util.ColorOptions;
import toufoumaster.btwaila.util.ProgressBarOptions;
import toufoumaster.btwaila.util.TextureOptions;

public class FluidTooltip extends SIBaseTooltip<IFluidInventory> {
    @Override
    public void initTooltip() {
        this.addClass(TileEntitySIFluidTank.class);
        this.addClass(TileEntityEnergyCell.class);
    }

    @Override
    public void drawAdvancedTooltip(IFluidInventory inv, AdvancedInfoComponent c) {
        drawFluids(inv,c);
    }
}
