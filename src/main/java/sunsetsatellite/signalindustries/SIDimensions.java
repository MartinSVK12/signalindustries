package sunsetsatellite.signalindustries;

import net.minecraft.core.world.Dimension;
import sunsetsatellite.signalindustries.util.DataInitializer;

import static sunsetsatellite.signalindustries.SignalIndustries.*;

public class SIDimensions extends DataInitializer {
    public static Dimension dimEternity;

    public void init(){
        if(initialized) return;
        LOGGER.info("Initializing dimensions...");
        dimEternity = new Dimension(key("eternity"),Dimension.overworld,1, SIBlocks.portalEternity.id).setDefaultWorldType(SIWorldTypes.eternityWorld);
        Dimension.registerDimension(config.getInt("Other.eternityDimId"), SIDimensions.dimEternity);
        setInitialized(true);
    }
}