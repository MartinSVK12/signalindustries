package sunsetsatellite.signalindustries;

import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import sunsetsatellite.signalindustries.util.DataInitializer;

import static sunsetsatellite.signalindustries.SignalIndustries.*;

public class SIBiomes extends DataInitializer {
    public static Biome biomeEternity;

    public void init(){
        if(initialized) return;
        LOGGER.info("Initializing biomes...");
        biomeEternity = Biomes.register("signalindustries:eternity", new Biome("eternity").setFillerBlock(SIBlocks.realityFabric.id).setTopBlock(SIBlocks.realityFabric.id).setColor(0x808080));
        setInitialized(true);
    }
}