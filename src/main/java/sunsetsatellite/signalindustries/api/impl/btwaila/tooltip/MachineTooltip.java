package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.inventories.machines.*;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.util.ProgressBarOptions;
import toufoumaster.btwaila.util.TextureOptions;

public class MachineTooltip extends SIBaseTooltip<TileEntityTieredMachineBase> {
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
        addClass(TileEntityAssembler.class);
        addClass(TileEntityInductionSmelter.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityTieredMachineBase tile, AdvancedInfoComponent c) {
        ProgressBarOptions options = new ProgressBarOptions()
                .setForegroundOptions(new TextureOptions(0x00FF00, TextureRegistry.getTexture("minecraft:block/sand")))
                .setBackgroundOptions(new TextureOptions(0,TextureRegistry.getTexture("signalindustries:block/reality_fabric")))
                .setText("Progress: ");
        c.drawProgressBarTextureWithText(tile.progressTicks,tile.progressMaxTicks,options,0);
        drawFluids(tile,c);
        c.drawInventory(tile, 0);
    }

}
