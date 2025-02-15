package sunsetsatellite.signalindustries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ModInitializer;
import turniplabs.halplibe.util.GameStartEntrypoint;

public class SignalIndustries implements ModInitializer, GameStartEntrypoint {

    public static final String MOD_ID = "signalindustries";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        //noinspection InstantiationOfUtilityClass
        new SIConfig();
        LOGGER.info("Signal Industries initialized. Shine!");
    }

    @Override
    public void beforeGameStart() {
        LOGGER.info("Beginning core pre-init.");
    }

    @Override
    public void afterGameStart() {
        LOGGER.info("Beginning core post-init.");
    }
}
