package sunsetsatellite.signalindustries.api.impl.vintagequesting;

import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.collection.Pair;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.gui.QuestChapterPage;
import sunsetsatellite.vintagequesting.quest.Quest;
import sunsetsatellite.vintagequesting.quest.template.QuestTemplate;
import sunsetsatellite.vintagequesting.quest.template.RewardTemplate;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;
import sunsetsatellite.vintagequesting.quest.template.reward.ItemRewardTemplate;
import sunsetsatellite.vintagequesting.quest.template.task.ClickTaskTemplate;
import sunsetsatellite.vintagequesting.quest.template.task.RetrievalTaskTemplate;
import sunsetsatellite.vintagequesting.quest.template.task.VisitDimensionTaskTemplate;
import sunsetsatellite.vintagequesting.util.Logic;

import java.util.ArrayList;
import java.util.List;

import static sunsetsatellite.catalyst.Catalyst.listOf;
import static sunsetsatellite.catalyst.Catalyst.zip;
import static sunsetsatellite.signalindustries.SIBlocks.*;
import static sunsetsatellite.signalindustries.SIItems.*;


public class VintageQuestingSIPlugin {

    public static QuestChapterPage PROTOTYPE_CHAPTER = new PrototypeQuestChapter();
    public static QuestChapterPage BASIC_CHAPTER = new BasicQuestChapter();
    public static QuestChapterPage REINFORCED_CHAPTER = new ReinforcedQuestChapter();
    public static QuestChapterPage AWAKENED_CHAPTER = new AwakenedQuestChapter();

    public void initializePlugin() {

        List<QuestTemplate> prototypeQuests = addPrototypeQuests();
        List<QuestTemplate> basicQuests = addBasicQuests();
        List<QuestTemplate> reinforcedQuests = addReinforcedQuests();
        List<QuestTemplate> awakenedQuests = addAwakenedQuests();

        for (QuestTemplate quest : prototypeQuests) {
            PROTOTYPE_CHAPTER.addQuest(quest);
        }
        for (Quest quest : PROTOTYPE_CHAPTER.getQuests()) {
            quest.setupPrerequisites();
        }

        for (QuestTemplate quest : basicQuests) {
            BASIC_CHAPTER.addQuest(quest);
        }
        for (Quest quest : BASIC_CHAPTER.getQuests()) {
            quest.setupPrerequisites();
        }

        for (QuestTemplate quest : reinforcedQuests) {
            REINFORCED_CHAPTER.addQuest(quest);
        }
        for (Quest quest : REINFORCED_CHAPTER.getQuests()) {
            quest.setupPrerequisites();
        }

        for (QuestTemplate quest : awakenedQuests) {
            AWAKENED_CHAPTER.addQuest(quest);
        }
        for (Quest quest : AWAKENED_CHAPTER.getQuests()) {
            quest.setupPrerequisites();
        }

        VintageQuesting.LOGGER.info("Loaded quests from: signalindustries!");

    }

    public void reload(){
        VintageQuesting.CHAPTERS.unregister(PROTOTYPE_CHAPTER.getId());
        VintageQuesting.CHAPTERS.unregister(BASIC_CHAPTER.getId());
        VintageQuesting.CHAPTERS.unregister(REINFORCED_CHAPTER.getId());

        PROTOTYPE_CHAPTER = new PrototypeQuestChapter();
        BASIC_CHAPTER = new BasicQuestChapter();
        REINFORCED_CHAPTER = new ReinforcedQuestChapter();

        initializePlugin();
    }

    public static List<QuestTemplate> addPrototypeQuests() {
        QuestTemplate welcome = new QuestTemplate("signalindustries:welcome", "quest.signalindustries.welcome", rawSignalumCrystal, Logic.AND, Logic.AND)
                .setTasks(listOf(new ClickTaskTemplate("signalindustries:welcome/click")));

        QuestTemplate genesis = simpleQuest("genesis", signalumOre,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(rawSignalumCrystal, 32)
                        )),
                /*zip(listOf("reward"),
                        listOf(
                                new ItemStack(rawSignalumCrystal,32)
                        )),*/
                listOf(welcome), welcome,
                0,
                1);

        /*QuestTemplate tome = simpleQuest("tome",raziel,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(raziel,1)
                        )),
                listOf(genesis),genesis,
                -1,
                0);*/

        QuestTemplate hammer = simpleQuest("hammer", ironPlateHammer,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(ironPlateHammer, 1)
                        )),
                listOf(welcome), genesis,
                0,
                1);

        QuestTemplate cobblePlates = simpleQuest("cobblePlates", cobblestonePlate,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(cobblestonePlate, 4)
                        )),
                listOf(hammer), hammer,
                -1,
                1);

        QuestTemplate stonePlates = simpleQuest("stonePlates", stonePlate,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(stonePlate, 4)
                        )),
                listOf(hammer), hammer,
                1,
                1);

        QuestTemplate tablet = simpleQuest("tablet", configurationTablet,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(configurationTablet, 1)
                        )),
                listOf(stonePlates, genesis), stonePlates,
                1,
                0);

        QuestTemplate ioConfig = simpleClickQuest("ioConfig", itemManipulationCircuit,
                listOf(tablet), tablet,
                1,
                0);

        QuestTemplate prototypeCore = simpleQuest("prototypeCore", prototypeMachineCore,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeMachineCore, 1)
                        )),
                listOf(genesis, cobblePlates, stonePlates), hammer,
                0,
                2);

        QuestTemplate protoExtract = simpleQuest("prototypeExtractor", prototypeExtractor,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeExtractor, 1)
                        )),
                listOf(prototypeCore), prototypeCore,
                0,
                1);

        QuestTemplate energy = simpleClickQuest("energy", energyFlowing,
                listOf(protoExtract), protoExtract,
                0,
                1);

        QuestTemplate protoPlate = simpleQuest("prototypePlateFormer", prototypePlateFormer,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypePlateFormer, 1)
                        )),
                listOf(prototypeCore), prototypeCore,
                1,
                1);

        QuestTemplate protoSmelter = simpleQuest("prototypeAlloySmelter", prototypeAlloySmelter,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeAlloySmelter, 1)
                        )),
                listOf(prototypeCore), prototypeCore,
                -1,
                1);

        QuestTemplate protoCrusher = simpleQuest("prototypeCrusher", prototypeCrusher,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeCrusher, 1)
                        )),
                listOf(prototypeCore), prototypeCore,
                2,
                1);

        QuestTemplate diamondGear = simpleQuest("diamondGear", diamondCuttingGear,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(diamondCuttingGear, 1)
                        )),
                listOf(), prototypeCore,
                -2 - 1,
                1);

        QuestTemplate protoCutter = simpleQuest("prototypeCrystalCutter", prototypeCrystalCutter,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeCrystalCutter, 1)
                        )),
                listOf(prototypeCore, diamondGear), prototypeCore,
                -2,
                1);

        QuestTemplate energyConduit = simpleQuest("energyConduit", prototypeConduit,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeConduit, 4)
                        )),
                listOf(prototypeCore), prototypeCore,
                4,
                1);

        QuestTemplate fluidConduit = simpleQuest("fluidConduit", prototypeFluidConduit,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeFluidConduit, 4)
                        )),
                listOf(prototypeCore), prototypeCore,
                5,
                1);

        QuestTemplate itemConduit = simpleQuest("itemConduit", prototypeItemConduit,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeItemConduit, 4)
                        )),
                listOf(prototypeCore), prototypeCore,
                6,
                1);

        QuestTemplate energyCell = simpleQuest("energyCell", prototypeEnergyCell,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeEnergyCell, 1)
                        )),
                listOf(energyConduit), energyConduit,
                0,
                1);

        QuestTemplate fluidTank = simpleQuest("fluidTank", prototypeFluidTank,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeFluidTank, 1)
                        )),
                listOf(fluidConduit), fluidConduit,
                0,
                1);

        QuestTemplate pump = simpleQuest("pump", prototypePump,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypePump, 1)
                        )),
                listOf(fluidConduit), fluidConduit,
                0,
                2);

        QuestTemplate inserter = simpleQuest("inserter", prototypeInserter,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeInserter, 1)
                        )),
                listOf(itemConduit), itemConduit,
                0,
                1);

        QuestTemplate storageContainer = simpleQuest("storageContainer", prototypeStorageContainer,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(prototypeStorageContainer, 1)
                        )),
                listOf(prototypeCore), itemConduit,
                1,
                1);

        QuestTemplate crystal = simpleQuest("crystal", signalumCrystal,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(signalumCrystal, 1)
                        )),
                listOf(protoCutter), energy,
                -1,
                1);//.setWidth(1).setHeight(1).setIconSize(2);

        QuestTemplate crystalBattery = simpleQuest("crystalBattery", signalumCrystalBattery,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(signalumCrystalBattery, 1)
                        )),
                listOf(crystal), crystal,
                1,
                1);

        return listOf(
                welcome, genesis, hammer, cobblePlates, stonePlates,
                tablet, ioConfig, prototypeCore, protoPlate, diamondGear, protoCutter,
                protoCrusher, protoExtract, protoSmelter, energy, energyConduit,
                fluidConduit, itemConduit, energyCell, fluidTank, pump, inserter,
                storageContainer, crystal, crystalBattery
        );
    }

    public static List<QuestTemplate> addBasicQuests() {
        QuestTemplate emptyCrystal = new QuestTemplate("signalindustries:emptyCrystal", "quest.signalindustries.emptyCrystal", signalumCrystalEmpty, Logic.AND, Logic.AND)
                .setTasks(listOf(new RetrievalTaskTemplate("signalindustries:emptyCrystal/retrieval", signalumCrystalEmpty.getDefaultStack())))
                .setPreRequisites(listOf(getQuest("prototypeCrystalCutter")));
        QuestTemplate steel = new QuestTemplate("signalindustries:steel", "quest.signalindustries.steel", Items.INGOT_STEEL, Logic.AND, Logic.AND)
                .setTasks(listOf(new RetrievalTaskTemplate("signalindustries:steel/retrieval", Items.INGOT_STEEL.getDefaultStack())))
                .setX(1);
        QuestTemplate crystalDust = simpleQuest("crystalDust", emptySignalumCrystalDust,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(emptySignalumCrystalDust, 1)
                        )),
                listOf(emptyCrystal), emptyCrystal,
                0,
                1);
        QuestTemplate crystalAlloy = simpleQuest("crystalAlloy", crystalAlloyIngot,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(crystalAlloyIngot, 1)
                        )),
                listOf(crystalDust, steel), crystalDust,
                0,
                1);
        QuestTemplate crystalAlloyPlates = simpleQuest("crystalAlloyPlates", crystalAlloyPlate,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(crystalAlloyPlate, 1)
                        )),
                listOf(crystalAlloy), crystalAlloy,
                0,
                1);
        QuestTemplate meteorCompass = simpleQuest("meteorCompass", meteorTracker,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(meteorTracker, 1)
                        )),
                listOf(crystalAlloy, getQuest("crystal")), crystalAlloy,
                -1,
                0);
        QuestTemplate steelPlates = simpleQuest("steelPlates", steelPlate,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(steelPlate, 1)
                        )),
                listOf(steel), crystalAlloyPlates,
                1,
                0);
        QuestTemplate basicCore = simpleQuest("basicCore", basicMachineCore,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(basicMachineCore, 1)
                        )),
                listOf(crystalAlloyPlates, steelPlates, getQuest("crystal")), crystalAlloyPlates,
                -1,
                1);//.setWidth(1).setHeight(1).setIconSize(2);
        QuestTemplate basicExtractor = simpleQuest("basicExtractor", SIBlocks.basicExtractor,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.basicExtractor, 1)
                        )),
                listOf(basicCore), basicCore,
                1,
                1);
        QuestTemplate basicCrusher = simpleQuest("basicCrusher", SIBlocks.basicCrusher,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.basicCrusher, 1)
                        )),
                listOf(basicCore), basicCore,
                1,
                1 + 1);
        QuestTemplate netherCoalDust = simpleQuest("netherCoalDust", SIItems.netherCoalDust,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.netherCoalDust, 1)
                        )),
                listOf(basicCrusher), basicCrusher,
                0,
                1);
        QuestTemplate cheaperSteel = simpleQuest("cheaperSteel", tinyNetherCoalDust,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(tinyNetherCoalDust, 1)
                        )),
                listOf(basicCrusher, netherCoalDust), netherCoalDust,
                0,
                1);
        QuestTemplate basicSmelter = simpleQuest("basicSmelter", basicAlloySmelter,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(basicAlloySmelter, 1)
                        )),
                listOf(basicCore), basicCrusher,
                -1,
                0);
        QuestTemplate basicCutter = simpleQuest("basicCutter", basicCrystalCutter,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(basicCrystalCutter, 1)
                        )),
                listOf(basicCore), basicCrusher,
                1,
                0);
        QuestTemplate basicInfuser = simpleQuest("basicInfuser", SIBlocks.basicInfuser,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.basicInfuser, 1)
                        )),
                listOf(basicCore), basicCutter,
                1,
                0);
        QuestTemplate basicPress = simpleQuest("basicPress", basicPlateFormer,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(basicPlateFormer, 1)
                        )),
                listOf(basicCore), basicInfuser,
                1,
                0);
        QuestTemplate saturatedAlloy = simpleQuest("saturatedAlloy", saturatedSignalumAlloyIngot,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(saturatedSignalumAlloyIngot, 1)
                        )),
                listOf(basicInfuser), basicInfuser,
                0,
                1);
        QuestTemplate saturatedAlloyPlate = simpleQuest("saturatedAlloyPlate", saturatedSignalumAlloyPlate,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(saturatedSignalumAlloyPlate, 1)
                        )),
                listOf(saturatedAlloy, basicPress), basicPress,
                0,
                1);
        QuestTemplate signalumAlloyMesh = simpleQuest("signalumAlloyMesh", SIItems.signalumAlloyMesh,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.signalumAlloyMesh, 1)
                        )),
                listOf(saturatedAlloyPlate, basicCutter), saturatedAlloyPlate,
                0,
                1);
        QuestTemplate basicCollector = simpleQuest("basicCollector", SIBlocks.basicCollector,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.basicCollector, 1)
                        )),
                listOf(basicCore, signalumAlloyMesh), signalumAlloyMesh,
                0,
                1);
        QuestTemplate basicChamber = simpleQuest("basicChamber", basicCrystalChamber,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(basicCrystalChamber, 1)
                        )),
                listOf(basicCore), basicSmelter,
                -1,
                0);
        QuestTemplate bonsaiPot = simpleQuest("basicBonsaiPot", basicBonsai,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(basicBonsai, 1)
                        )),
                listOf(basicCore, crystalAlloyPlates), basicChamber,
                -1,
                0);
        QuestTemplate basicChip = simpleQuest("basicChip", crystalChip,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(crystalChip, 1)
                        )),
                listOf(basicCutter), basicCutter,
                0,
                1);
        QuestTemplate basicEnergyCore = simpleQuest("basicEnergyCore", SIItems.basicEnergyCore,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.basicEnergyCore, 1)
                        )),
                listOf(basicChip, crystalAlloyPlates), basicChip,
                -1,
                1);//.setWidth(1).setHeight(1).setIconSize(2);
        QuestTemplate basicAutoMiner = simpleQuest("basicAutoMiner", basicAutomaticMiner,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(basicAutomaticMiner, 1)
                        )),
                listOf(basicCore, basicEnergyCore), basicEnergyCore,
                1,
                1 + 1);
        QuestTemplate basicAssembler = simpleQuest("basicAssembler", SIBlocks.basicAssembler,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.basicAssembler, 1)
                        )),
                listOf(basicCore, basicEnergyCore), basicAutoMiner,
                -1,
                0);
        QuestTemplate basicInjector = simpleQuest("basicInjector", basicEnergyInjector,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(basicEnergyInjector, 1)
                        )),
                listOf(basicCore, basicEnergyCore), basicAssembler,
                -1,
                0);
        QuestTemplate basicDynamo = simpleQuest("basicDynamo", basicSignalumDynamo,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(basicSignalumDynamo, 1)
                        )),
                listOf(basicCore, basicEnergyCore), basicAutoMiner,
                1,
                0);
        QuestTemplate basicCatalystConduit = simpleQuest("basicCatalystConduit", SIBlocks.basicCatalystConduit,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.basicCatalystConduit, 1)
                        )),
                listOf(basicDynamo), basicDynamo,
                0,
                1);
        QuestTemplate precisionChip = simpleQuest("precisionChip", precisionControlChip,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(precisionControlChip, 1)
                        )),
                listOf(basicAutoMiner), basicAutoMiner,
                0,
                1);
        QuestTemplate basicEnergyCell = simpleQuest("basicEnergyCell", SIBlocks.basicEnergyCell,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.basicEnergyCell, 1)
                        )),
                listOf(basicCore), basicCore,
                -1 - 2,
                1);
        QuestTemplate basicEnergyConduit = simpleQuest("basicEnergyConduit", basicConduit,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(basicConduit, 1)
                        )),
                listOf(basicEnergyCell), basicEnergyCell,
                -1,
                0);
        QuestTemplate basicFluidTank = simpleQuest("basicFluidTank", SIBlocks.basicFluidTank,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.basicFluidTank, 1)
                        )),
                listOf(basicCore), basicEnergyCell,
                0,
                -1);
        QuestTemplate basicFluidConduit = simpleQuest("basicFluidConduit", SIBlocks.basicFluidConduit,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.basicFluidConduit, 1)
                        )),
                listOf(basicFluidTank), basicFluidTank,
                -1,
                0);
        QuestTemplate basicInserter = simpleQuest("basicInserter", SIBlocks.basicInserter,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.basicInserter, 1)
                        )),
                listOf(basicCore), basicFluidTank,
                0,
                -1);
        QuestTemplate basicItemConduit = simpleQuest("basicItemConduit", SIBlocks.basicItemConduit,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.basicItemConduit, 1)
                        )),
                listOf(basicInserter), basicInserter,
                -1,
                0);
        QuestTemplate basicRestrictItemConduit = simpleQuest("basicRestrictItemConduit", SIBlocks.basicRestrictItemConduit,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.basicRestrictItemConduit, 1)
                        )),
                listOf(basicInserter), basicItemConduit,
                0,
                -1);
        QuestTemplate basicSensorItemConduit = simpleQuest("basicSensorItemConduit", SIBlocks.basicSensorItemConduit,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.basicSensorItemConduit, 1)
                        )),
                listOf(basicInserter), basicItemConduit,
                0,
                -2);
        QuestTemplate basicContainer = simpleQuest("basicContainer", basicStorageContainer,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(basicStorageContainer, 1)
                        )),
                listOf(basicCore), basicItemConduit,
                -1,
                0);
        QuestTemplate covers = simpleQuest("covers", blankCover,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(blankCover, 1)
                        )),
                listOf(crystalAlloyPlates, steelPlates), steelPlates,
                2,
                0);
        QuestTemplate conveyorCover = simpleQuest("conveyorCover", SIItems.conveyorCover,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.conveyorCover, 1)
                        )),
                listOf(covers), covers,
                1,
                0);
        QuestTemplate pumpCover = simpleQuest("pumpCover", SIItems.pumpCover,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.pumpCover, 1)
                        )),
                listOf(covers), covers,
                1,
                1);
        QuestTemplate voidCover = simpleQuest("voidCover", SIItems.voidCover,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.voidCover, 1)
                        )),
                listOf(covers), covers,
                1,
                -1);
        QuestTemplate redstoneCover = simpleQuest("redstoneCover", SIItems.redstoneCover,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.redstoneCover, 1)
                        )),
                listOf(covers), covers,
                1,
                -2);
        QuestTemplate switchCover = simpleQuest("switchCover", SIItems.switchCover,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.switchCover, 1)
                        )),
                listOf(covers), covers,
                1,
                2);
        QuestTemplate glowingObsidian = simpleQuest("glowingObsidian", SIBlocks.glowingObsidian,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.glowingObsidian, 1)
                        )),
                listOf(basicInfuser), cheaperSteel,
                -4,
                -1);//.setWidth(1).setHeight(1).setIconSize(2);
        QuestTemplate basicDrill = simpleQuest("basicDrill", basicSignalumDrill,
                zip(listOf("retrieval", "retrieval2", "retrieval3"),
                        listOf(
                                new ItemStack(basicDrillCasing, 1),
                                new ItemStack(basicDrillBit, 1),
                                new ItemStack(basicSignalumDrill, 1)
                        )),
                listOf(steelPlates, crystalAlloyPlates, saturatedAlloyPlate), saturatedAlloyPlate,
                1,
                0);
        QuestTemplate romChips = simpleQuest("romChips", romChipScan,
                zip(listOf("retrieval", "retrieval2", "retrieval3", "retrieval4"),
                        listOf(
                                new ItemStack(romChipBoost, 1),
                                new ItemStack(romChipScan, 1),
                                new ItemStack(romChipProjectile, 1),
                                new ItemStack(romChipShield, 1)
                        )),
                listOf(), basicDrill,
                5,
                0).setTaskLogic(Logic.OR);
        QuestTemplate triggers = simpleQuest("triggers", nullTrigger,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(nullTrigger, 1)
                        )),
                listOf(crystalAlloyPlates, saturatedAlloy, basicEnergyCore, romChips), romChips,
                0,
                1);
        QuestTemplate programmer = simpleQuest("programmer", basicProgrammer,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(basicProgrammer, 1)
                        )),
                listOf(basicCore, steelPlates, saturatedAlloyPlate, basicEnergyCore, romChips, triggers), triggers,
                0,
                1);
        QuestTemplate harness = simpleQuest("powerHarness", signalumPrototypeHarness,
                zip(listOf("retrieval", "retrieval2"),
                        listOf(
                                new ItemStack(signalumPrototypeHarness, 1),
                                new ItemStack(signalumPrototypeHarnessGoggles, 1)
                        )),
                listOf(basicEnergyCore, crystalAlloyPlates, romChips, triggers, programmer), programmer,
                2,
                -1);
        QuestTemplate signaliteAlloyCoil = simpleQuest("signaliteAlloyCoil", signalumAlloyCoil,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(signalumAlloyCoil, 1)
                        )),
                listOf(saturatedAlloyPlate, crystalAlloy), saturatedAlloyPlate,
                2 + 1,
                2);
        QuestTemplate basicInductionSmelter = simpleQuest("basicInductionSmelter", SIBlocks.basicInductionSmelter,
                zip(listOf("retrieval", "retrieval2", "retrieval3", "retrieval4", "retrieval5", "retrieval6"),
                        listOf(
                                new ItemStack(SIBlocks.basicInductionSmelter, 1),
                                new ItemStack(basicCasing, 14),
                                new ItemStack(signalumAlloyCoil, 8),
                                new ItemStack(basicItemInputBus, 1),
                                new ItemStack(basicItemOutputBus, 1),
                                new ItemStack(basicEnergyConnector, 1)
                        )),
                listOf(signaliteAlloyCoil), signaliteAlloyCoil,
                -1,
                1);//.setWidth(1).setHeight(1).setIconSize(2);
        QuestTemplate basicEnergyConnector = simpleQuest("basicEnergyConnector", SIBlocks.basicEnergyConnector,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.basicEnergyConnector, 1)
                        )),
                listOf(basicCore, basicEnergyCore), basicInductionSmelter,
                1,
                1 + 1);
        QuestTemplate basicItemInput = simpleQuest("basicItemInput", basicItemInputBus,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(basicItemInputBus, 1)
                        )),
                listOf(basicCore), basicInductionSmelter,
                0,
                1);
        QuestTemplate basicItemOutput = simpleQuest("basicItemOutput", SIBlocks.basicItemOutputBus,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(basicItemOutputBus, 1)
                        )),
                listOf(basicCore), basicInductionSmelter,
                1 + 1,
                1);
        QuestTemplate basicCasing = simpleQuest("basicCasing", SIBlocks.basicCasing,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.basicCasing, 1)
                        )),
                listOf(crystalAlloyPlates), basicEnergyConnector,
                1,
                0);
        QuestTemplate greenhouse = simpleQuest("greenhouse", basicGreenhouse,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(basicGreenhouse, 1)
                        )),
                listOf(basicCasing, basicEnergyConnector, basicItemInput, basicItemOutput), basicEnergyConnector,
                0,
                2);
        QuestTemplate redstoneBooster = simpleQuest("redstoneBooster", SIBlocks.redstoneBooster,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.redstoneBooster, 1)
                        )),
                listOf(basicCore, basicEnergyCore), basicEnergyCore,
                -1 - 1,
                0);
        return listOf(
                emptyCrystal, steel, crystalDust, crystalAlloy, crystalAlloyPlates, meteorCompass,
                steelPlates, basicCore, basicExtractor, basicCollector, basicCrusher, netherCoalDust,
                cheaperSteel, basicSmelter, basicInfuser, basicCutter, basicPress, saturatedAlloy,
                saturatedAlloyPlate, signalumAlloyMesh, basicChamber, basicAutoMiner, precisionChip,
                basicChip, basicEnergyCore, basicEnergyCell, basicFluidTank, basicInserter,
                basicItemConduit, basicRestrictItemConduit, basicSensorItemConduit, basicContainer,
                basicEnergyConduit, basicFluidConduit, basicAssembler, basicDynamo, basicCatalystConduit,
                basicInjector, bonsaiPot, covers, conveyorCover, pumpCover, redstoneCover, switchCover, voidCover,
                glowingObsidian, basicDrill, signaliteAlloyCoil, basicInductionSmelter, basicEnergyConnector,
                basicItemInput, basicItemOutput, basicCasing, redstoneBooster, romChips, programmer,
                triggers, harness, greenhouse
        );
    }

    public static List<QuestTemplate> addReinforcedQuests() {
        QuestTemplate reinforcedAlloy = new QuestTemplate("signalindustries:reinforcedAlloy", "quest.signalindustries.reinforcedAlloy", reinforcedCrystalAlloyIngot, Logic.AND, Logic.AND)
                .setTasks(listOf(new RetrievalTaskTemplate("signalindustries:reinforcedAlloy/retrieval", reinforcedCrystalAlloyIngot.getDefaultStack())))
                .setPreRequisites(listOf(getQuest("basicSmelter"), getQuest("glowingObsidian")));
        QuestTemplate reinforcedAlloyPlates = new QuestTemplate("signalindustries:reinforcedAlloyPlates", "quest.signalindustries.reinforcedAlloyPlates", reinforcedCrystalAlloyPlate, Logic.AND, Logic.AND)
                .setTasks(listOf(new RetrievalTaskTemplate("signalindustries:reinforcedAlloyPlates/retrieval", reinforcedCrystalAlloyPlate.getDefaultStack())))
                .setPreRequisites(listOf(reinforcedAlloy, getQuest("basicPress")))
                .setY(reinforcedAlloy, 1);
        QuestTemplate signaliteGear = new QuestTemplate("signalindustries:signaliteGear", "quest.signalindustries.signaliteGear", signalumCuttingGear, Logic.AND, Logic.AND)
                .setTasks(listOf(new RetrievalTaskTemplate("signalindustries:signaliteGear/retrieval", signalumCuttingGear.getDefaultStack())))
                .setPreRequisites(listOf(getQuest("crystal")))
                .setY(reinforcedAlloyPlates, 2)
                .setX(reinforcedAlloyPlates, -1);
        QuestTemplate reinforcedCore = new QuestTemplate("signalindustries:reinforcedCore", "quest.signalindustries.reinforcedCore", reinforcedMachineCore, Logic.AND, Logic.AND)
                .setTasks(listOf(new RetrievalTaskTemplate("signalindustries:reinforcedCore/retrieval", reinforcedMachineCore.getDefaultStack())))
                .setPreRequisites(listOf(reinforcedAlloyPlates, getQuest("saturatedAlloy"), getQuest("basicCore")))
                .setY(reinforcedAlloyPlates, 1)
                .setX(reinforcedAlloyPlates, -1);//.setWidth(1).setHeight(1).setIconSize(2);;
        QuestTemplate reinforcedCutter = simpleQuest("reinforcedCutter", reinforcedCrystalCutter,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(reinforcedCrystalCutter, 1)
                        )),
                listOf(reinforcedCore, signaliteGear), reinforcedCore,
                1,
                1 + 1);
        QuestTemplate pureChip = simpleQuest("pureChip", pureCrystalChip,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(pureCrystalChip, 1)
                        )),
                listOf(reinforcedCutter, getQuest("crystal")), reinforcedCutter,
                1,
                0);
        QuestTemplate reinforcedEnergyCore = simpleQuest("reinforcedEnergyCore", SIItems.reinforcedEnergyCore,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.reinforcedEnergyCore, 1)
                        )),
                listOf(pureChip), pureChip,
                1,
                -1);//.setWidth(1).setHeight(1).setIconSize(2);
        QuestTemplate reinforcedDrill = simpleQuest("reinforcedDrill", reinforcedSignalumDrill,
                zip(listOf("retrieval", "retrieval2", "retrieval3"),
                        listOf(
                                new ItemStack(reinforcedSignalumDrill, 1),
                                new ItemStack(reinforcedDrillBit, 1),
                                new ItemStack(reinforcedDrillCasing, 1)
                        )),
                listOf(reinforcedEnergyCore), reinforcedEnergyCore,
                1,
                -1);
        QuestTemplate dilithiumOre = simpleQuest("dilithiumOre", SIBlocks.dilithiumOre,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(dilithiumShard, 1)
                        )),
                listOf(reinforcedDrill), reinforcedDrill,
                1,
                0);
        QuestTemplate reinforcedTracker = simpleQuest("reinforcedTracker", reinforcedMeteorTracker,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(reinforcedMeteorTracker, 1)
                        )),
                listOf(dilithiumOre), dilithiumOre,
                0,
                -1);
        QuestTemplate dilithiumPlate = simpleQuest("dilithiumPlate", SIItems.dilithiumPlate,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.dilithiumPlate, 1)
                        )),
                listOf(dilithiumOre), dilithiumOre,
                1,
                0);
        QuestTemplate blueprint = simpleQuest("blueprint", SIItems.blueprint,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.blueprint, 1)
                        )),
                listOf(), reinforcedEnergyCore,
                0,
                1 + 1);
        QuestTemplate builder = simpleQuest("builder", reinforcedBuilder,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(reinforcedBuilder, 1)
                        )),
                listOf(reinforcedEnergyCore, blueprint), reinforcedEnergyCore,
                1,
                1 + 1);
        QuestTemplate dimensionalOre = simpleQuest("dimensionalOre", dimensionalShardOre,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(dimensionalShard, 1)
                        )),
                listOf(reinforcedDrill), reinforcedDrill,
                1,
                -2);
        QuestTemplate warpOrb = simpleQuest("warpOrb", SIItems.warpOrb,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.warpOrb, 1)
                        )),
                listOf(dimensionalOre), dimensionalOre,
                1,
                0);
        QuestTemplate reinforcedEnergyConnector = simpleQuest("reinforcedEnergyConnector", SIBlocks.reinforcedEnergyConnector,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.reinforcedEnergyConnector, 1)
                        )),
                listOf(reinforcedEnergyCore, reinforcedCore), reinforcedCore,
                -2 + 1,
                1);
        QuestTemplate reinforcedItemInput = simpleQuest("reinforcedItemInput", reinforcedItemInputBus,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(reinforcedItemInputBus, 1)
                        )),
                listOf(reinforcedEnergyCore, reinforcedCore), reinforcedEnergyConnector,
                -1,
                0);
        QuestTemplate reinforcedItemOutput = simpleQuest("reinforcedItemOutput", reinforcedItemOutputBus,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(reinforcedItemOutputBus, 1)
                        )),
                listOf(reinforcedEnergyCore, reinforcedCore), reinforcedEnergyConnector,
                -2,
                0);
        QuestTemplate reinforcedFluidInput = simpleQuest("reinforcedFluidInput", reinforcedFluidInputHatch,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(reinforcedFluidInputHatch, 1)
                        )),
                listOf(reinforcedEnergyCore, reinforcedCore), reinforcedEnergyConnector,
                -1,
                1);
        QuestTemplate reinforcedFluidOutput = simpleQuest("reinforcedFluidOutput", reinforcedFluidOutputHatch,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(reinforcedFluidOutputHatch, 1)
                        )),
                listOf(reinforcedEnergyCore, reinforcedCore), reinforcedEnergyConnector,
                -2,
                1);
        QuestTemplate reinforcedCasing = simpleQuest("reinforcedCasing", SIBlocks.reinforcedCasing,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.reinforcedCasing, 1)
                        )),
                listOf(reinforcedAlloyPlates), reinforcedAlloyPlates,
                -2,
                0);
        QuestTemplate reinforcedCasing2 = simpleQuest("reinforcedCasing2", SIBlocks.reinforcedCasing2,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.reinforcedCasing2, 1)
                        )),
                listOf(reinforcedAlloyPlates), reinforcedCasing,
                0,
                -1);
        QuestTemplate reinforcedGrating = simpleQuest("reinforcedGrating", reinforcedGrate,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.reinforcedGrate, 1)
                        )),
                listOf(reinforcedAlloyPlates, getQuest("signalumAlloyMesh")), reinforcedCasing,
                -1,
                0);
        QuestTemplate glass = simpleQuest("reinforcedGlass", reinforcedGlass,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(reinforcedGlass, 1)
                        )),
                listOf(reinforcedAlloyPlates), reinforcedCasing,
                -1,
                -1);
        QuestTemplate booster = simpleQuest("booster", dilithiumBooster,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(dilithiumBooster, 1)
                        )),
                listOf(dilithiumPlate), dilithiumPlate,
                1,
                0);
        QuestTemplate stabilizer = simpleQuest("stabilizer", dilithiumStabilizer,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(dilithiumStabilizer, 1)
                        )),
                listOf(dilithiumPlate), booster,
                0,
                1);
        QuestTemplate reinforcedWrathBeacon = simpleQuest("reinforcedWrathBeacon", SIBlocks.reinforcedWrathBeacon,
                zip(listOf("retrieval", "retrieval2"),
                        listOf(
                                new ItemStack(SIBlocks.reinforcedWrathBeacon, 1),
                                new ItemStack(eternalTreeLog, 259)
                        )),
                listOf(reinforcedCore), reinforcedCutter,
                0,
                1);
        QuestTemplate saturatedKey = simpleQuest("saturatedKey", SIItems.saturatedKey,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.saturatedKey, 1)
                        )),
                listOf(reinforcedWrathBeacon), reinforcedWrathBeacon,
                0,
                1);
        QuestTemplate eclipse = simpleQuest("eclipse", infernalFragment,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(infernalFragment, 1)
                        )),
                listOf(reinforcedWrathBeacon), reinforcedWrathBeacon,
                -1,
                0);
        QuestTemplate dilithiumChip = simpleQuest("dilithiumChip", SIItems.dilithiumChip,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.dilithiumChip, 1)
                        )),
                listOf(reinforcedCutter), pureChip,
                0,
                -2);
        QuestTemplate dimensionalChip = simpleQuest("dimensionalChip", SIItems.dimensionalChip,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.dimensionalChip, 1)
                        )),
                listOf(reinforcedCutter), pureChip,
                0,
                -2 - 1);
        QuestTemplate anchor = simpleQuest("anchor", dimensionalAnchor,
                zip(listOf("retrieval", "retrieval2", "retrieval3", "retrieval4"),
                        listOf(
                                new ItemStack(dimensionalAnchor, 1),
                                new ItemStack(dilithiumStabilizer, 4),
                                new ItemStack(glowingObsidian, 24),
                                new ItemStack(SIBlocks.reinforcedCasing, 40)
                        )),
                listOf(warpOrb, stabilizer), warpOrb,
                1,
                -1);//.setWidth(1).setHeight(1).setIconSize(2);
        QuestTemplate pulsarBlock = simpleQuest("pulsarBlock", SIBlocks.pulsarBlock,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIBlocks.pulsarBlock, 1)
                        )),
                listOf(reinforcedEnergyCore), reinforcedDrill,
                -1,
                -2 - 1);
        QuestTemplate pulsar = simpleQuest("pulsar", SIItems.pulsar,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.pulsar, 1)
                        )),
                listOf(reinforcedEnergyCore), reinforcedDrill,
                0,
                -2 - 1);
        QuestTemplate powerSuit = simpleQuest("powerSuit", signalumPowerSuitChestplate,
                zip(listOf("retrieval", "retrieval2", "retrieval3", "retrieval4"),
                        listOf(
                                new ItemStack(signalumPowerSuitHelmet, 1),
                                new ItemStack(signalumPowerSuitChestplate, 1),
                                new ItemStack(signalumPowerSuitLeggings, 1),
                                new ItemStack(signalumPowerSuitBoots, 1)
                        )),
                listOf(reinforcedEnergyCore, reinforcedAlloyPlates), builder,
                5,
                0);
        QuestTemplate attachments = simpleQuest("attachments", attachmentPoint,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(attachmentPoint, 1)
                        )),
                listOf(powerSuit), powerSuit,
                1,
                0);
        QuestTemplate pulsarAttachment = simpleQuest("pulsarAttachment", SIItems.pulsarAttachment,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(SIItems.pulsarAttachment, 1)
                        )),
                listOf(pulsar, powerSuit), attachments,
                1,
                0);
        QuestTemplate backpack = simpleQuest("backpack", reinforcedBackpack,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(reinforcedBackpack, 1)
                        )),
                listOf(attachments), attachments,
                1,
                1);
        QuestTemplate nightVision = simpleQuest("nightVision", nightVisionLens,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(nightVisionLens, 1)
                        )),
                listOf(attachments), attachments,
                1,
                -1);
        QuestTemplate wings = simpleQuest("wings", crystalWings,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(crystalWings, 1)
                        )),
                listOf(attachments), attachments,
                1,
                2);
        QuestTemplate energyPack = simpleQuest("energyPack", extendedEnergyPack,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(extendedEnergyPack, 1)
                        )),
                listOf(attachments), attachments,
                1,
                -2);
        QuestTemplate eternity = new QuestTemplate("signalindustries:eternity", "quest.signalindustries.eternity", realityFabric, Logic.AND, Logic.AND)
                .setTasks(listOf(new VisitDimensionTaskTemplate("signalindustries:eternity/visit", SIDimensions.ETERNITY)))
                .setPreRequisites(listOf(pulsar, warpOrb))
                .setY(pulsar, -1).setX(pulsar, -1);
        QuestTemplate fuelCells = simpleQuest("fuelCells", fuelCell,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(fuelCell, 1)
                        )),
                listOf(reinforcedAlloyPlates), saturatedKey,
                0,
                1);
        QuestTemplate ignitor = simpleQuest("ignitor", reinforcedIgnitor,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(reinforcedIgnitor, 1)
                        )),
                listOf(reinforcedCore), fuelCells,
                -2,
                0);
        QuestTemplate reactor = simpleQuest("reactor", signalumReactorCore,
                zip(listOf("retrieval", "retrieval2", "retrieval3", "retrieval4", "retrieval5",
                                "retrieval6", "retrieval7", "retrieval8", "retrieval9", "retrieval10"),
                        listOf(
                                new ItemStack(signalumReactorCore, 1),
                                new ItemStack(signalumAlloyCoil, 6),
                                new ItemStack(SIBlocks.reinforcedCasing, 63),
                                new ItemStack(reinforcedGlass, 73),
                                new ItemStack(dilithiumStabilizer, 4),
                                new ItemStack(reinforcedIgnitor, 5),
                                new ItemStack(reinforcedConduit, 2),
                                new ItemStack(SIBlocks.reinforcedEnergyConnector, 1),
                                new ItemStack(reinforcedItemInputBus, 1),
                                new ItemStack(reinforcedItemOutputBus, 1)
                        )),
                listOf(reinforcedEnergyCore, saturatedKey, reinforcedItemInput, reinforcedItemOutput,
                        reinforcedEnergyConnector, reinforcedCasing, fuelCells, stabilizer, ignitor), saturatedKey,
                -1,
                1);
        QuestTemplate centrifuge = simpleQuest("centrifuge", reinforcedCentrifuge,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(reinforcedCentrifuge, 1)
                        )),
                listOf(reinforcedCore), reactor,
                0,
                1);
        QuestTemplate fragments = simpleQuest("awakenedFragments", awakenedSignalumFragment,
                zip(listOf("retrieval"),
                        listOf(
                                new ItemStack(awakenedSignalumFragment, 1)
                        )),
                listOf(centrifuge), centrifuge,
                0,
                1);

        return listOf(
                reinforcedAlloy, reinforcedAlloyPlates, reinforcedCore, signaliteGear, reinforcedCutter,
                pureChip, reinforcedEnergyCore, reinforcedDrill, dilithiumOre, reinforcedTracker, dilithiumPlate,
                builder, blueprint, dimensionalOre, warpOrb, reinforcedEnergyConnector, reinforcedCasing, glass,
                reinforcedFluidOutput, reinforcedFluidInput, reinforcedItemOutput, reinforcedItemInput,
                reinforcedGrating, reinforcedCasing2, booster, stabilizer, reinforcedWrathBeacon, eclipse,
                dilithiumChip, dimensionalChip, anchor, pulsarBlock, pulsar, powerSuit, attachments,
                backpack, nightVision, wings, energyPack,
                pulsarAttachment, eternity, saturatedKey,
                fuelCells, ignitor, reactor, centrifuge, fragments
        );
    }

    public static List<QuestTemplate> addAwakenedQuests() {
        QuestTemplate awakenedCrystal = new QuestTemplate("signalindustries:awakenedCrystal", "quest.signalindustries.awakenedCrystal", awakenedSignalumCrystal, Logic.AND, Logic.AND)
                .setTasks(listOf(new RetrievalTaskTemplate("signalindustries:awakenedCrystal/retrieval", awakenedSignalumCrystal.getDefaultStack())))
                .setPreRequisites(listOf(getQuest("awakenedFragments")));
        return listOf(
                awakenedCrystal
        );
    }

    public static QuestTemplate simpleQuest(
            String id,
            IItemConvertible icon,
            List<Pair<String, ItemStack>> tasks,
            List<QuestTemplate> preRequisites,
            QuestTemplate offsetQuest,
            int xOffset,
            int yOffset
    ) {
        List<TaskTemplate> retrievalTasks = new ArrayList<>();
        for (Pair<String, ItemStack> task : tasks) {
            retrievalTasks.add(new RetrievalTaskTemplate("signalindustries:" + id + "/" + task.getLeft(), task.getRight()));
        }
        return new QuestTemplate("signalindustries:" + id, "quest.signalindustries." + id, icon, Logic.AND, Logic.AND)
                .setPreRequisites(preRequisites)
                .setTasks(retrievalTasks)
                .setX(offsetQuest, xOffset)
                .setY(offsetQuest, yOffset);
    }

    public static QuestTemplate simpleQuest(
            String id,
            IItemConvertible icon,
            List<Pair<String, ItemStack>> tasks,
            List<Pair<String, ItemStack>> rewards,
            List<QuestTemplate> preRequisites,
            QuestTemplate offsetQuest,
            int xOffset,
            int yOffset
    ) {
        List<TaskTemplate> retrievalTasks = new ArrayList<>();
        for (Pair<String, ItemStack> task : tasks) {
            retrievalTasks.add(new RetrievalTaskTemplate("signalindustries:" + id + "/" + task.getLeft(), task.getRight()));
        }
        List<RewardTemplate> itemRewards = new ArrayList<>();
        for (Pair<String, ItemStack> reward : rewards) {
            itemRewards.add(new ItemRewardTemplate("signalindustries:" + id + "/" + reward.getLeft(), reward.getRight()));
        }
        return new QuestTemplate("signalindustries:" + id, "quest.signalindustries." + id, icon, Logic.AND, Logic.AND)
                .setPreRequisites(preRequisites)
                .setTasks(retrievalTasks)
                .setRewards(itemRewards)
                .setX(offsetQuest, xOffset)
                .setY(offsetQuest, yOffset);
    }

    public static QuestTemplate simpleClickQuest(
            String id,
            IItemConvertible icon,
            List<QuestTemplate> preRequisites,
            QuestTemplate offsetQuest,
            int xOffset,
            int yOffset
    ) {
        return new QuestTemplate("signalindustries:" + id, "quest.signalindustries." + id, icon, Logic.AND, Logic.AND)
                .setPreRequisites(preRequisites)
                .setTasks(listOf(new ClickTaskTemplate("signalindustries:" + id + "/click")))
                .setX(offsetQuest, xOffset)
                .setY(offsetQuest, yOffset);
    }

    public static QuestTemplate getQuest(String id) {
        QuestTemplate item = VintageQuesting.QUESTS.getItem("signalindustries:" + id);
        if (item == null) {
            throw new NullPointerException("Quest " + id + " not found!");
        }
        return item;
    }

}
