package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import sunsetsatellite.signalindustries.tiles.TileEntitySignalumDynamo;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.tiles.machines.*;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityDimensionalAnchor;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityGreenhouse;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityInductionSmelter;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityLaserDrill;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingAlloySmelter;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingCrusher;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingInfuser;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingPlateFormer;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.util.ProgressBarOptions;
import toufoumaster.btwaila.util.TextureOptions;

import java.util.Objects;

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
        addClass(TileEntityCollector.class);
        addClass(TileEntityStoneworks.class);
        addClass(TileEntityEnergyInjector.class);
        addClass(TileEntityWakingInfuser.class);
        addClass(TileEntityWakingCrusher.class);
        addClass(TileEntityWakingAlloySmelter.class);
        addClass(TileEntityWakingPlateFormer.class);
        addClass(TileEntityLaserDrill.class);
        addClass(TileEntityGreenhouse.class);
        addClass(TileEntityBonsaiPot.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityTieredMachineBase tile, AdvancedInfoComponent c) {
        long coversCount = tile.getCovers().values().stream().filter(Objects::nonNull).count();
        if(coversCount > 0){
            c.drawStringWithShadow(coversCount+" "+(coversCount > 1 ? "covers" : "cover")+" installed.",0);
        }
        ProgressBarOptions options = new ProgressBarOptions()
                .setForegroundOptions(new TextureOptions(0x00FF00, TextureRegistry.getTexture("minecraft:block/sand")))
                .setBackgroundOptions(new TextureOptions(0,TextureRegistry.getTexture("signalindustries:block/reality_fabric")))
                .setText("Progress: ");
        c.drawProgressBarTextureWithText(tile.progressTicks,tile.progressMaxTicks,options,0);
        drawFluids(tile,c,true);
        c.drawInventory(tile, 0);
    }

}
