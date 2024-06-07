package sunsetsatellite.signalindustries;

import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.type.WorldTypes;
import sunsetsatellite.signalindustries.dim.WorldTypeEternity;
import sunsetsatellite.signalindustries.util.DataInitializer;

import static sunsetsatellite.signalindustries.SignalIndustries.*;

public class SIWorldTypes extends DataInitializer {
    public static WorldType eternityWorld;

    public void init(){
        if(initialized) return;
        LOGGER.info("Initializing world types...");
        eternityWorld = WorldTypes.register("signalindustries:eternity", new WorldTypeEternity(key("eternity")));
        setInitialized(true);
    }

}