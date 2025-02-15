package sunsetsatellite.signalindustries;

import sunsetsatellite.catalyst.core.util.DataInitializer;
import turniplabs.halplibe.util.BlockInitEntrypoint;

import static sunsetsatellite.signalindustries.SignalIndustries.LOGGER;

public class SIBlocks extends DataInitializer implements BlockInitEntrypoint {
    @Override
    public void init() {
        if (initialized) return;
        LOGGER.info("Initializing blocks...");
        setInitialized(true);
    }

    @Override
    public void afterBlockInit() {
        init();
    }
}
