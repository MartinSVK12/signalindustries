package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.core.block.Block;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Color;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachine;
import sunsetsatellite.signalindustries.inventories.machines.*;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;
import toufoumaster.btwaila.util.ColorOptions;
import toufoumaster.btwaila.util.ProgressBarOptions;
import toufoumaster.btwaila.util.TextureOptions;

public class MachineTooltip extends SIBaseTooltip<TileEntityTieredMachine> {
    @Override
    public void initTooltip() {
        addClass(TileEntityExtractor.class);
        addClass(TileEntityCrusher.class);
        addClass(TileEntityPlateFormer.class);
        addClass(TileEntityAlloySmelter.class);
        addClass(TileEntityInfuser.class);
        addClass(TileEntityCrystalChamber.class);
        addClass(TileEntityCrystalCutter.class);
        addClass(TileEntityCentrifuge.class);
        addClass(TileEntityDimensionalAnchor.class);
        addClass(TileEntitySignalumDynamo.class);
        addClass(TileEntityPump.class);
        addClass(TileEntityAutoMiner.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityTieredMachine tile, AdvancedInfoComponent c) {
        ProgressBarOptions options = new ProgressBarOptions()
                .setForegroundOptions(new TextureOptions(0x00FF00,Block.sand.atlasIndices[0]))
                .setBackgroundOptions(new TextureOptions(0,SignalIndustries.realityFabric.atlasIndices[0]))
                .setText("Progress: ");
        c.drawProgressBarTextureWithText(tile.progressTicks,tile.progressMaxTicks,options,0);
        drawFluids(tile,c);
        c.drawInventory(tile, 0);
    }

}
