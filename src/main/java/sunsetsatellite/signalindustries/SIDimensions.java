package sunsetsatellite.signalindustries;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.core.Global;
import net.minecraft.core.world.Dimension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.EntityTracker;
import sunsetsatellite.signalindustries.util.DataInitializer;

import static sunsetsatellite.signalindustries.SignalIndustries.*;

public class SIDimensions extends DataInitializer {
    public static Dimension dimEternity;

    public void init(){
        if(initialized) return;
        LOGGER.info("Initializing dimensions...");
        dimEternity = new Dimension(langKey("eternity"),Dimension.overworld,1, SIBlocks.portalEternity.id).setDefaultWorldType(SIWorldTypes.eternityWorld);
        Dimension.registerDimension(config.getInt("Other.eternityDimId"), SIDimensions.dimEternity);
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER){
            ((MinecraftServer) Global.accessor).entityTracker = new EntityTracker[Dimension.getDimensionList().size()];
        }
        setInitialized(true);
    }
}