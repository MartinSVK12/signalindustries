package sunsetsatellite.signalindustries;

import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.biome.SurfaceProperties;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import sunsetsatellite.signalindustries.dim.BiomeEternity;

import static sunsetsatellite.signalindustries.SignalIndustries.LOGGER;

public class SIBiomes extends DataInitializer {

    public static Biome biomeEternity;

    @Override
    public void init() {
        if (initialized) return;
        LOGGER.info("Initializing biomes...");

        biomeEternity = Biomes.register("signalindustries:eternity", new BiomeEternity());
        setInitialized(true);
    }
}
