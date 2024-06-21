package sunsetsatellite.signalindustries;

import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.Structure;
import sunsetsatellite.catalyst.core.util.DataInitializer;

import static sunsetsatellite.signalindustries.SignalIndustries.*;

public class SIMultiblocks extends DataInitializer {
    public static Multiblock dimAnchorMultiblock;
    public static Multiblock wrathTree;
    public static Multiblock signalumReactor;
    public static Multiblock extractionManifold;
    public static Multiblock inductionSmelterBasic;

    public void init(){
        if(initialized) return;
        LOGGER.info("Initializing multiblocks...");
        dimAnchorMultiblock = new Multiblock(MOD_ID, new Class[]{SIBlocks.class}, "dimensionalAnchor", "dimensionalAnchor", false);
        wrathTree = new Multiblock(MOD_ID, new Class[]{SIBlocks.class}, "wrathTree", "reinforcedWrathBeacon", false);
        signalumReactor = new Multiblock(MOD_ID, new Class[]{SIBlocks.class}, "signalumReactor", "signalumReactor", false);
        extractionManifold = new Multiblock(MOD_ID, new Class[]{SIBlocks.class}, "reinforcedExtractor", "reinforcedExtractor", false);
        inductionSmelterBasic = new Multiblock(MOD_ID, new Class[]{SIBlocks.class}, "basicInductionSmelter", "basicInductionSmelter", false);
        Multiblock.multiblocks.put("dimensionalAnchor", dimAnchorMultiblock);
        Multiblock.multiblocks.put("wrathTree", wrathTree);
        Multiblock.multiblocks.put("signalumReactor", signalumReactor);
        Multiblock.multiblocks.put("extractionManifold", extractionManifold);
        Multiblock.multiblocks.put("basicInductionSmelter", inductionSmelterBasic);
        LOGGER.info(String.format("Loaded %d multiblocks..",Multiblock.multiblocks.size()));
        LOGGER.info(String.format("Loaded %d internal structures.", Structure.internalStructures.size()));
        setInitialized(true);
    }

}