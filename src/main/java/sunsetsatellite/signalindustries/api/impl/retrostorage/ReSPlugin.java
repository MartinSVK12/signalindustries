package sunsetsatellite.signalindustries.api.impl.retrostorage;

import org.slf4j.Logger;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.signalindustries.SIBlocks;

public class ReSPlugin {
    public void initializePlugin(Logger logger) {
        RetroStorage.DISALLOWED_FLUIDS.add((SIBlocks.energyFlowing));
    }
}
