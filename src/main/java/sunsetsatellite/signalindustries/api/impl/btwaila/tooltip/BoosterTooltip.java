package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityBooster;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityStabilizer;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;
import toufoumaster.btwaila.tooltips.Tooltip;
import toufoumaster.btwaila.util.ColorOptions;
import toufoumaster.btwaila.util.ProgressBarOptions;
import toufoumaster.btwaila.util.TextureOptions;

public class BoosterTooltip extends SIBaseTooltip<TileEntityBooster> {
    @Override
    public void initTooltip() {
        addClass(TileEntityBooster.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityBooster tile, AdvancedInfoComponent c) {
        ProgressBarOptions options = new ProgressBarOptions()
                .setForegroundOptions(new TextureOptions(0xFFFFFF,SignalIndustries.dilithiumCrystalBlock.atlasIndices[0]))
                .setBackgroundOptions(new TextureOptions(0xFFFFFF,SignalIndustries.realityFabric.atlasIndices[0]))
                .setText("Fuel: ");
        c.drawProgressBarTextureWithText(tile.progressTicks,tile.progressMaxTicks,options,0);
        drawFluids(tile,c);
        c.drawInventory(tile, 0);
    }

}
