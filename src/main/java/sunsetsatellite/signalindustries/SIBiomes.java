package sunsetsatellite.signalindustries;

import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.biome.SurfaceProperties;
import sunsetsatellite.catalyst.core.util.DataInitializer;

import static sunsetsatellite.signalindustries.SignalIndustries.LOGGER;

public class SIBiomes extends DataInitializer {

    public static Biome biomeEternity;

    @Override
    public void init() {
        if (initialized) return;
        LOGGER.info("Initializing biomes...");
		SurfaceProperties props = new SurfaceProperties.Builder().withFillerBlock(SIBlocks.realityFabric).withTopBlock(SIBlocks.realityFabric).build();
        biomeEternity = Biomes.register("signalindustries:eternity", new Biome("eternity").withSurfaceProperties(props).withDebugColor(0x808080));
        setInitialized(true);
    }
}
