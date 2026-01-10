package sunsetsatellite.signalindustries;

import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.type.WorldTypes;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import sunsetsatellite.signalindustries.dim.WorldTypeEternity;

import static sunsetsatellite.signalindustries.SignalIndustries.LOGGER;

public class SIWorldTypes extends DataInitializer {

    public static WorldType ETERNITY_WORLD;

    @Override
    public void init() {
        if (initialized) return;
        LOGGER.info("Initializing world types...");
        ETERNITY_WORLD = WorldTypes.register("signalindustries:eternity", new WorldTypeEternity("signalindustries.eternity"));

        setInitialized(true);
    }
}
