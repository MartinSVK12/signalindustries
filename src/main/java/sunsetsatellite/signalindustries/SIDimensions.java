package sunsetsatellite.signalindustries;

import net.minecraft.core.world.Dimension;
import sunsetsatellite.catalyst.core.util.DataInitializer;

import static sunsetsatellite.signalindustries.SIConfig.config;
import static sunsetsatellite.signalindustries.SignalIndustries.LOGGER;
import static sunsetsatellite.signalindustries.SignalIndustries.langKey;

public class SIDimensions extends DataInitializer {

    public static Dimension ETERNITY;

    @Override
    public void init() {
        if (initialized) return;
        LOGGER.info("Initializing dimensions...");

        ETERNITY = new Dimension(langKey("eternity"), Dimension.OVERWORLD, 1, null, SIWorldTypes.ETERNITY_WORLD);
        Dimension.registerDimension(config.getInt("Other.eternityDimId"), ETERNITY);

        setInitialized(true);
    }
}
