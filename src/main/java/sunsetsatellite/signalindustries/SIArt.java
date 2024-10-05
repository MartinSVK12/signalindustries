package sunsetsatellite.signalindustries;

import net.minecraft.core.enums.ArtType;
import sunsetsatellite.catalyst.core.util.DataInitializer;

import static sunsetsatellite.signalindustries.SignalIndustries.LOGGER;

public class SIArt extends DataInitializer {

    public static ArtType eternityBeyondReality;

    public void init() {
        if (initialized) return;
        LOGGER.info("Initializing art...");

        eternityBeyondReality = new ArtType("EternityBeyondReality","Eternity Beyond Reality","theMidford","signalindustries:art/eternity_beyond_reality",32,16);

        setInitialized(true);
    }
}
