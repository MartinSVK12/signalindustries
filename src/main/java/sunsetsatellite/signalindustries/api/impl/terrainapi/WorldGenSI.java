package sunsetsatellite.signalindustries.api.impl.terrainapi;

import net.minecraft.core.block.Block;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.worldgen.WorldFeatureGeode;
import sunsetsatellite.signalindustries.worldgen.WorldFeatureMeteor;
import sunsetsatellite.signalindustries.worldgen.WorldFeatureObelisk;
import useless.terrainapi.generation.overworld.OverworldFunctions;
import useless.terrainapi.initialization.BaseInitialization;
import useless.terrainapi.initialization.worldtypes.OverworldInitialization;

public class WorldGenSI extends BaseInitialization {
    @Override
    protected void initValues() {

    }

    @Override
    protected void initStructure() {

    }

    @Override
    protected void initOre() {

    }

    @Override
    protected void initRandom() {
        OverworldInitialization.randomFeatures.addFeatureSurface(new WorldFeatureMeteor(Block.oreIronBasalt.id,0,25),256);
        OverworldInitialization.randomFeatures.addFeatureSurface(new WorldFeatureMeteor(SIBlocks.signalumOre.id,0,15),512);
        OverworldInitialization.randomFeatures.addFeatureSurface(new WorldFeatureMeteor(SIBlocks.dilithiumOre.id,0,3),1024);
        OverworldInitialization.randomFeatures.addFeatureSurface(new WorldFeatureObelisk(),2048);
        OverworldInitialization.randomFeatures.addFeature(
                (x) -> new WorldFeatureGeode(SIBlocks.signalumOre.id,0,20,4),
                null,
                OverworldFunctions::getStandardOreBiomesDensity,
                new Object[]{1, null},
                10,
                0.10f,
                0.25f
        );
    }

    @Override
    protected void initBiome() {

    }
}
