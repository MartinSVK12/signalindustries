package sunsetsatellite.signalindustries;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.Structure;
import sunsetsatellite.catalyst.multiblocks.StructureBuilder;
import sunsetsatellite.signalindustries.util.SIMultiblock;
import sunsetsatellite.signalindustries.util.Tier;

import static sunsetsatellite.signalindustries.SignalIndustries.LOGGER;
import static sunsetsatellite.signalindustries.SignalIndustries.MOD_ID;

public class SIMultiblocks extends DataInitializer {

    public static SIMultiblock dimAnchorMultiblock;
    public static SIMultiblock wrathTree;
    public static SIMultiblock signalumReactor;
    public static SIMultiblock extractionManifold;
    public static SIMultiblock inductionSmelterBasic;
    public static SIMultiblock warpGate;
    public static SIMultiblock wakingCrusher;
    public static SIMultiblock wakingAlloySmelter;
    public static SIMultiblock wakingPlateFormer;
    public static SIMultiblock wakingInfuser;
    public static SIMultiblock laserDrill;

    @Override
    public void init() {
        if(initialized) return;
        LOGGER.info("Initializing multiblocks...");

        wrathTree = new SIMultiblock(MOD_ID, new Class[]{SIBlocks.class}, "wrathTree", "reinforcedWrathBeacon", false, Tier.REINFORCED);
        // Example:
        /*CompoundTag multiblockData = new StructureBuilder('M', SIBlocks.basicInductionSmelter, 5)
                .addLayer("BBO",
                          "BBE",
                          "BBI")
                .addLayer("CCC",
                          "C C",
                          "CCC")
                .addLayer("BBB",
                          "BBM",
                          "BBB")
                .mapSymbol('B',new ItemStack(SIBlocks.basicCasing,1,-1))
                .mapSymbol('C',new ItemStack(SIBlocks.signalumAlloyCoil,1,1))
                .mapSymbol('I',new ItemStack(SIBlocks.basicItemInputBus,1,-1))
                .mapSymbol('O',new ItemStack(SIBlocks.basicItemOutputBus,1,-1))
                .mapSymbol('E',new ItemStack(SIBlocks.basicEnergyConnector,1,-1))
                .build();*/
        inductionSmelterBasic = new SIMultiblock(MOD_ID, new Class[]{SIBlocks.class}, "basicInductionSmelter", "basicInductionSmelter", false, Tier.BASIC);
        wakingPlateFormer = new SIMultiblock(MOD_ID, new Class[]{SIBlocks.class}, "wakingPlateFormer", "wakingPlateFormer", false, Tier.REINFORCED);
        wakingAlloySmelter = new SIMultiblock(MOD_ID, new Class[]{SIBlocks.class}, "wakingAlloySmelter", "wakingAlloySmelter", false, Tier.REINFORCED);
        wakingCrusher = new SIMultiblock(MOD_ID, new Class[]{SIBlocks.class}, "wakingCrusher", "wakingCrusher", false, Tier.REINFORCED);
        wakingInfuser = new SIMultiblock(MOD_ID, new Class[]{SIBlocks.class}, "wakingInfuser", "wakingInfuser", false, Tier.REINFORCED);
        dimAnchorMultiblock = new SIMultiblock(MOD_ID, new Class[]{SIBlocks.class}, "dimensionalAnchor", "dimensionalAnchor", false, Tier.REINFORCED);
        signalumReactor = new SIMultiblock(MOD_ID, new Class[]{SIBlocks.class}, "signalumReactor", "signalumReactor", false, Tier.REINFORCED);
        extractionManifold = new SIMultiblock(MOD_ID, new Class[]{SIBlocks.class}, "extractionManifold", "reinforcedExtractor", false, Tier.REINFORCED);
        warpGate = new SIMultiblock(MOD_ID, new Class[]{SIBlocks.class}, "warpGate", "warpGate", false, Tier.AWAKENED);

        CompoundTag laserDrillData = new StructureBuilder('M', SIBlocks.reinforcedLaserDrill, 2)
                .addLayer(
                        "  11111  ",
                        " 1222221 ",
                        "122   221",
                        "12     2O",
                        "E2     2M",
                        "12     2I",
                        "122   221",
                        " 1222221 ",
                        "  11111  "
                )
                .addLayer(
                        "    F    ",
                        "    F    ",
                        "         ",
                        "         ",
                        "FF  S  FF",
                        "         ",
                        "         ",
                        "    F    ",
                        "    F    "
                )
                .addLayer(
                        "         ",
                        "    F    ",
                        "         ",
                        "         ",
                        " F  S  F ",
                        "         ",
                        "         ",
                        "    F    ",
                        "         "
                )
                .addLayer(
                        "         ",
                        "    F    ",
                        "         ",
                        "    1    ",
                        " F 1F1 F ",
                        "    1    ",
                        "         ",
                        "    F    ",
                        "         "
                )
                .addLayer(
                        "         ",
                        "    F    ",
                        "         ",
                        "   1G1   ",
                        " F GFG F ",
                        "   1G1   ",
                        "         ",
                        "    F    ",
                        "         "
                )
                .addLayer(
                        "         ",
                        "    F    ",
                        "   1F1   ",
                        "  1 F 1   ",
                        " FFFFFFF ",
                        "  1 F 1   ",
                        "   1F1   ",
                        "    F    ",
                        "         "
                )
                .addLayer(
                        "         ",
                        "         ",
                        "         ",
                        "   1G1   ",
                        "   GFG   ",
                        "   1G1   ",
                        "         ",
                        "         ",
                        "         "
                )
                .addLayer(
                        "         ",
                        "         ",
                        "         ",
                        "   1G1   ",
                        "   GFG   ",
                        "   1G1   ",
                        "         ",
                        "         ",
                        "         "
                )
                .addLayer(
                        "         ",
                        "         ",
                        "         ",
                        "    1    ",
                        "   1F1   ",
                        "    1    ",
                        "         ",
                        "         ",
                        "         "
                )
                .addLayer(
                        "         ",
                        "         ",
                        "         ",
                        "    1    ",
                        "   1F1   ",
                        "    1    ",
                        "         ",
                        "         ",
                        "         "
                )
                .addLayer(
                        "         ",
                        "         ",
                        "         ",
                        "         ",
                        "    F    ",
                        "         ",
                        "         ",
                        "         ",
                        "         "
                )
                .addLayer(
                        "         ",
                        "         ",
                        "         ",
                        "         ",
                        "    F    ",
                        "         ",
                        "         ",
                        "         ",
                        "         "
                )
                .mapSymbol('1', new ItemStack(SIBlocks.reinforcedCasing, 1, -1))
                .mapSymbol('2', new ItemStack(SIBlocks.reinforcedCasing2, 1, -1))
                .mapSymbol('F', new ItemStack(SIBlocks.reinforcedFrame, 1, -1))
                .mapSymbol('G', new ItemStack(SIBlocks.reinforcedGrate,1, -1))
                .mapSymbol('S', new ItemStack(SIBlocks.rawCrystalBlock,1,-1))
                .mapSymbol('I', new ItemStack(SIBlocks.reinforcedFluidInputHatch, 1, -1))
                .mapSymbol('O', new ItemStack(SIBlocks.reinforcedItemOutputBus,1, -1))
                .mapSymbol('E', new ItemStack(SIBlocks.reinforcedEnergyConnector,1, -1))
                .build();

        laserDrill = new SIMultiblock(MOD_ID, new Class[]{SIBlocks.class}, "laserDrill", laserDrillData, false, Tier.REINFORCED);

        Multiblock.multiblocks.put("dimensionalAnchor", dimAnchorMultiblock);
        Multiblock.multiblocks.put("wrathTree", wrathTree);
        Multiblock.multiblocks.put("signalumReactor", signalumReactor);
        Multiblock.multiblocks.put("extractionManifold", extractionManifold);
        Multiblock.multiblocks.put("basicInductionSmelter", inductionSmelterBasic);
        Multiblock.multiblocks.put("wakingPlateFormer", wakingPlateFormer);
        Multiblock.multiblocks.put("wakingAlloySmelter", wakingAlloySmelter);
        Multiblock.multiblocks.put("wakingCrusher", wakingCrusher);
        Multiblock.multiblocks.put("wakingInfuser", wakingInfuser);
        Multiblock.multiblocks.put("warpGate", warpGate);
        Multiblock.multiblocks.put("laserDrill", laserDrill);

        LOGGER.info("Loaded {} multiblocks..", Multiblock.multiblocks.size());
        LOGGER.info("Loaded {} internal structures.", Structure.internalStructures.size());
        setInitialized(true);
    }
}
