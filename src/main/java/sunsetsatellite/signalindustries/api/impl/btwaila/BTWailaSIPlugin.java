package sunsetsatellite.signalindustries.api.impl.btwaila;

import org.slf4j.Logger;
import sunsetsatellite.signalindustries.api.impl.btwaila.tooltips.EnergyTooltip;
import toufoumaster.btwaila.BTWailaCustomTootltipPlugin;

public class BTWailaSIPlugin implements BTWailaCustomTootltipPlugin {
    @Override
    public void initializePlugin(Logger logger) {
        logger.info("Loading tooltips for: BTWailaSIPlugin from signalindustries..");
        (new EnergyTooltip()).addTooltip();
    }
}
