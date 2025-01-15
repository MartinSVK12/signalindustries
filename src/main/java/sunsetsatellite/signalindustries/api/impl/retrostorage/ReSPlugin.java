package sunsetsatellite.signalindustries.api.impl.retrostorage;

import org.slf4j.Logger;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.signalindustries.api.impl.catalyst.fluid.SIFluids;

public class ReSPlugin {
    public void initializePlugin(Logger logger) {
        RetroStorage.DISALLOWED_FLUIDS.add(SIFluids.SIGNALUM_ENERGY);
    }
}
