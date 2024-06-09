package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.Side;
import org.w3c.dom.Text;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityBooster;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityStabilizer;
import sunsetsatellite.signalindustries.util.Tier;
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
                .setBackgroundOptions(new TextureOptions(0xFFFFFF, TextureRegistry.getTexture("signalindustries:block/reality_fabric")))
                .setText("Fuel: ");
        if(tile.tier == Tier.BASIC){
            options.setForegroundOptions(new TextureOptions(0xFFFFFF, TextureRegistry.getTexture("minecraft:block/block_redstone")));
        } else {
            options.setForegroundOptions(new TextureOptions(0xFFFFFF, TextureRegistry.getTexture("signalindustries:block/dilithium_crystal_block")));
        }
        c.drawProgressBarTextureWithText(tile.progressTicks,tile.progressMaxTicks,options,0);
        drawFluids(tile,c,false);
        c.drawInventory(tile, 0);
    }

}
