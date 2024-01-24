package sunsetsatellite.signalindustries.api.impl.terrainapi;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import sunsetsatellite.signalindustries.SignalIndustries;
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
        OverworldInitialization.randomFeatures.addFeatureSurface(new WorldFeatureMeteor(Block.oreIronBasalt.id,0,25),512);
        OverworldInitialization.randomFeatures.addFeatureSurface(new WorldFeatureMeteor(SignalIndustries.signalumOre.id,0,15),1024);
        OverworldInitialization.randomFeatures.addFeatureSurface(new WorldFeatureMeteor(SignalIndustries.dilithiumOre.id,0,3),2048);
        OverworldInitialization.randomFeatures.addFeatureSurface(new WorldFeatureObelisk(),4096);
        OverworldInitialization.randomFeatures.addFeature(
                (x) -> new WorldFeatureGeode(SignalIndustries.signalumOre.id,0,10,3),
                null,
                OverworldFunctions::getStandardBiomesDensity,
                new Object[]{1, null},
                32,
                0.10f,
                0.25f
        );
    }

    @Override
    protected void initBiome() {

    }
}
