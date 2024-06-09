package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
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

public class StabilizerTooltip extends SIBaseTooltip<TileEntityStabilizer> {
    @Override
    public void initTooltip() {
        addClass(TileEntityStabilizer.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityStabilizer tile, AdvancedInfoComponent c) {
        ProgressBarOptions options = new ProgressBarOptions()
                .setForegroundOptions(new TextureOptions(0xFFFFFF, TextureRegistry.getTexture("signalindustries:block/dilithium_crystal_block")))
                .setBackgroundOptions(new TextureOptions(0xFFFFFF, TextureRegistry.getTexture("signalindustries:block/reality_fabric")))
                .setText("Fuel: ");
        c.drawProgressBarTextureWithText(tile.progressTicks,tile.progressMaxTicks,options,0);
        drawFluids(tile,c,false);
        c.drawInventory(tile, 0);
    }


}
