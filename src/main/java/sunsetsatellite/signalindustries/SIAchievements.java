package sunsetsatellite.signalindustries;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.gui.achievements.data.AchievementPage;
import net.minecraft.client.gui.achievements.data.AchievementPages;
import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import sunsetsatellite.signalindustries.api.impl.vintagequesting.VintageQuestingSIPlugin;
import sunsetsatellite.signalindustries.util.AchievementPageSI;

import static sunsetsatellite.signalindustries.SignalIndustries.*;

public class SIAchievements extends DataInitializer {

    public static AchievementPage SIGNAL_INDUSTRIES = new AchievementPageSI();

    public static Achievement INIT;
    public static Achievement THE_PROTOTYPE;
    public static Achievement FROM_WITHIN;
    public static Achievement TRANSFER;
    public static Achievement BUFFER;
    public static Achievement CRUSHER;
    public static Achievement ALLOY_SMELTER;
    public static Achievement PLATE_FORMER;
    public static Achievement SHINING;
    public static Achievement BASIC;
    public static Achievement ROM_CHIP;
    public static Achievement COMBINED;
    public static Achievement MINER;
    public static Achievement PUMP;
    public static Achievement HARNESS;
    public static Achievement PROGRAMMER;
    public static Achievement TRIGGER;
    public static Achievement CHALLENGE;
    public static Achievement VICTORY;
    public static Achievement RELIC;
    public static Achievement KNIGHTS_ALLOY;
    public static Achievement REINFORCED;
    public static Achievement VICTORY_REINFORCED;
    public static Achievement BLADE;
    public static Achievement PULSE;
    public static Achievement POWER_SUIT;
    public static Achievement DILITHIUM;
    public static Achievement DIMENSIONAL;
    public static Achievement WARP_ORB;
    public static Achievement ANCHOR;
    public static Achievement TELEPORT_SUCCESS;
    public static Achievement TELEPORT_FAIL;
    public static Achievement ETERNITY;
    public static Achievement FALSE_ETERNITY;
    public static Achievement BOOST;
    public static Achievement WINGS;
    public static Achievement HORIZONS;
    public static Achievement REACTOR;
    public static Achievement RISING_ABOVE;
    public static Achievement BLOOD_MOON;
    public static Achievement ECLIPSE;
    public static Achievement STARFALL;
    //public static Achievement DIVINE_KNOWLEDGE;
    public static Achievement WAKING1;
    public static Achievement WAKING2;
    public static Achievement WAKING3;
    public static Achievement WAKING4;
    public static Achievement GATE;

    public static Achievement AWAKENED;
    public static Achievement ENDGAME;

    @Override
    public void init() {
        if(initialized) return;
        LOGGER.info("Initializing achievements...");

        INIT = new Achievement(id("init"), langKey("init"), SIItems.rawSignalumCrystal, null).setType(Achievement.TYPE_SPECIAL);
        THE_PROTOTYPE = new Achievement(id("the_prototype"), langKey("thePrototype"), SIBlocks.prototypeMachineCore, INIT).setType(Achievement.TYPE_SPECIAL);
        FROM_WITHIN = new Achievement(id("from_within"), langKey("fromWithin"), SIBlocks.prototypeExtractor, THE_PROTOTYPE);
        TRANSFER = new Achievement(id("transfer"), langKey("transfer"), SIBlocks.prototypeConduit, THE_PROTOTYPE);
        BUFFER = new Achievement(id("buffer"), langKey("buffer"), SIBlocks.prototypeEnergyCell, THE_PROTOTYPE);
        CRUSHER = new Achievement(id("crusher"), langKey("crusher"), SIBlocks.prototypeCrusher, THE_PROTOTYPE);
        ALLOY_SMELTER = new Achievement(id("alloy_smelter"), langKey("alloySmelter"), SIBlocks.prototypeAlloySmelter, THE_PROTOTYPE);
        PLATE_FORMER = new Achievement(id("plate_former"), langKey("plateFormer"), SIBlocks.prototypePlateFormer, THE_PROTOTYPE);
        SHINING = new Achievement(id("shining"), langKey("shining"), SIItems.signalumCrystal, THE_PROTOTYPE).setType(Achievement.TYPE_SPECIAL);
        BASIC = new Achievement(id("basic"), langKey("basic"), SIBlocks.basicMachineCore, SHINING).setType(Achievement.TYPE_SPECIAL);
        ROM_CHIP = new Achievement(id("rom_chip"), langKey("romChip"), SIItems.romChipBoost, null);
        COMBINED = new Achievement(id("combined"), langKey("combined"), SIBlocks.basicCrystalChamber, BASIC);
        MINER = new Achievement(id("miner"), langKey("miner"), SIBlocks.basicAutomaticMiner, BASIC);
        PUMP = new Achievement(id("pump"), langKey("pump"), SIBlocks.basicPump, BASIC);
        HARNESS = new Achievement(id("harness"), langKey("harness"), SIItems.signalumPrototypeHarness, BASIC);
        PROGRAMMER = new Achievement(id("programmer"), langKey("programmer"), SIBlocks.basicProgrammer, ROM_CHIP);
        TRIGGER = new Achievement(id("trigger"), langKey("trigger"), SIItems.nullTrigger, PROGRAMMER);
        CHALLENGE = new Achievement(id("challenge"), langKey("challenge"), SIBlocks.basicWrathBeacon, BASIC);
        VICTORY = new Achievement(id("victory"), langKey("victory"), SIItems.clearKey, BASIC);
        RELIC = new Achievement(id("relic"), langKey("relic"), SIBlocks.glowingObsidian, BASIC);
        KNIGHTS_ALLOY = new Achievement(id("knights_alloy"), langKey("knightAlloy"), SIItems.reinforcedCrystalAlloyIngot, RELIC);
        REINFORCED = new Achievement(id("reinforced"), langKey("reinforced"), SIBlocks.reinforcedMachineCore, KNIGHTS_ALLOY).setType(Achievement.TYPE_SPECIAL);
        VICTORY_REINFORCED = new Achievement(id("victory_reinforced"), langKey("victory.reinforced"), SIBlocks.reinforcedWrathBeacon, REINFORCED);
        BLADE = new Achievement(id("blade"), langKey("blade"), SIItems.signalumSaber, REINFORCED);
        PULSE = new Achievement(id("pulse"), langKey("pulse"), SIItems.pulsar, REINFORCED);
        POWER_SUIT = new Achievement(id("power_suit"), langKey("powerSuit"), SIItems.signalumPowerSuitChestplate, REINFORCED);
        DILITHIUM = new Achievement(id("dilithium"), langKey("dilithium"), SIItems.dilithiumShard, REINFORCED);
        DIMENSIONAL = new Achievement(id("dimensional"), langKey("dimensional"), SIItems.dimensionalShard, REINFORCED);
        WARP_ORB = new Achievement(id("warp_orb"), langKey("warpOrb"), SIItems.warpOrb, DIMENSIONAL);
        ANCHOR = new Achievement(id("anchor"), langKey("anchor"), SIBlocks.dimensionalAnchor, WARP_ORB);
        TELEPORT_SUCCESS = new Achievement(id("teleport_success"), langKey("teleport.success"), Blocks.GRASS_RETRO, ANCHOR);
        TELEPORT_FAIL = new Achievement(id("teleport_fail"), langKey("teleport.fail"), SIBlocks.realityFabric, ANCHOR);
        ETERNITY = new Achievement(id("eternity"), langKey("eternity"), SIBlocks.rootedFabric, TELEPORT_FAIL);
        FALSE_ETERNITY = new Achievement(id("false_eternity"), langKey("falseEternity"), SIBlocks.dimensionalShardOre, TELEPORT_FAIL);
        BOOST = new Achievement(id("boost"), langKey("boost"), SIBlocks.dilithiumBooster, DILITHIUM);
        WINGS = new Achievement(id("wings"), langKey("wings"), SIItems.crystalWings, POWER_SUIT);
        HORIZONS = new Achievement(id("horizons"), langKey("horizons"), SIBlocks.reinforcedEnergyConnector, REINFORCED);
        REACTOR = new Achievement(id("reactor"), langKey("reactor"), SIBlocks.signalumReactorCore, HORIZONS);
        RISING_ABOVE = new Achievement(id("rising_above"), langKey("risingAbove"), SIItems.awakenedSignalumFragment, REACTOR);
        BLOOD_MOON = new Achievement(id("blood_moon"), langKey("bloodMoon"), SIItems.monsterShard, null);
        ECLIPSE = new Achievement(id("eclipse"), langKey("eclipse"), SIItems.infernalFragment, null);
        STARFALL = new Achievement(id("starfall"), langKey("starfall"), Blocks.LAMP_ACTIVE, null);
        //DIVINE_KNOWLEDGE = new Achievement(id("divine_knowledge"), langKey("divineKnowledge"), SIItems.raziel, null);
        WAKING1 = new Achievement(id("waking1"), langKey("waking1"), SIBlocks.wakingCrusher, RISING_ABOVE);
        WAKING2 = new Achievement(id("waking2"), langKey("waking2"), SIBlocks.wakingPlateFormer, RISING_ABOVE);
        WAKING3 = new Achievement(id("waking3"), langKey("waking3"), SIBlocks.wakingInfuser, RISING_ABOVE);
        WAKING4 = new Achievement(id("waking4"), langKey("waking4"), SIBlocks.wakingAlloySmelter, RISING_ABOVE);
        AWAKENED = new Achievement(id("awakened"), langKey("awakened"), SIItems.awakenedSignalumCrystal, RISING_ABOVE).setType(Achievement.TYPE_SPECIAL);
        ENDGAME = new Achievement(id("endgame"), langKey("endgame"), SIBlocks.awakenedMachineCore, AWAKENED).setType(Achievement.TYPE_SPECIAL);
        GATE = new Achievement(id("gate"), langKey("gate"), SIBlocks.warpGate, ENDGAME);

        SIGNAL_INDUSTRIES.addAchievement(INIT, 0, -0);
        SIGNAL_INDUSTRIES.addAchievement(THE_PROTOTYPE, 2, -0);
        SIGNAL_INDUSTRIES.addAchievement(FROM_WITHIN, 3, -1);
        SIGNAL_INDUSTRIES.addAchievement(TRANSFER, 4, 1);
        SIGNAL_INDUSTRIES.addAchievement(BUFFER, 5, -1);
        SIGNAL_INDUSTRIES.addAchievement(CRUSHER, 6, 1);
        SIGNAL_INDUSTRIES.addAchievement(ALLOY_SMELTER, 7, -1);
        SIGNAL_INDUSTRIES.addAchievement(PLATE_FORMER, 8, 1);
        SIGNAL_INDUSTRIES.addAchievement(SHINING, 9, -0);
        SIGNAL_INDUSTRIES.addAchievement(BASIC, 2, 2);
        SIGNAL_INDUSTRIES.addAchievement(ROM_CHIP, -2, 6);
        SIGNAL_INDUSTRIES.addAchievement(COMBINED, 4, 3);
        SIGNAL_INDUSTRIES.addAchievement(MINER, 6, 3);
        SIGNAL_INDUSTRIES.addAchievement(PUMP, 8, 3);
        SIGNAL_INDUSTRIES.addAchievement(HARNESS, 3, 5);
        SIGNAL_INDUSTRIES.addAchievement(PROGRAMMER, 5, 5);
        SIGNAL_INDUSTRIES.addAchievement(TRIGGER, 7, 5);
        SIGNAL_INDUSTRIES.addAchievement(CHALLENGE, 11, 5);
        SIGNAL_INDUSTRIES.addAchievement(VICTORY, 13, 5);
        SIGNAL_INDUSTRIES.addAchievement(RELIC, 9, 4);
        SIGNAL_INDUSTRIES.addAchievement(KNIGHTS_ALLOY, 9, 6);
        SIGNAL_INDUSTRIES.addAchievement(REINFORCED, 9, 8);
        SIGNAL_INDUSTRIES.addAchievement(VICTORY_REINFORCED, 11, 7);
        SIGNAL_INDUSTRIES.addAchievement(BLADE, 7, 7);
        SIGNAL_INDUSTRIES.addAchievement(PULSE, 5, 7);
        SIGNAL_INDUSTRIES.addAchievement(POWER_SUIT, 3, 7);
        SIGNAL_INDUSTRIES.addAchievement(DILITHIUM, 7, 9);
        SIGNAL_INDUSTRIES.addAchievement(DIMENSIONAL, 11, 9);
        SIGNAL_INDUSTRIES.addAchievement(WARP_ORB, 13, 9);
        SIGNAL_INDUSTRIES.addAchievement(ANCHOR, 15, 9);
        SIGNAL_INDUSTRIES.addAchievement(TELEPORT_SUCCESS, 17, 8);
        SIGNAL_INDUSTRIES.addAchievement(TELEPORT_FAIL, 17, 10);
        SIGNAL_INDUSTRIES.addAchievement(ETERNITY, 19, 10);
        SIGNAL_INDUSTRIES.addAchievement(FALSE_ETERNITY, 17, 12);
        SIGNAL_INDUSTRIES.addAchievement(BOOST, 5, 9);
        SIGNAL_INDUSTRIES.addAchievement(WINGS, 1, 7);
        SIGNAL_INDUSTRIES.addAchievement(HORIZONS, 9, 10);
        SIGNAL_INDUSTRIES.addAchievement(REACTOR, 9, 12);
        SIGNAL_INDUSTRIES.addAchievement(RISING_ABOVE, 9, 14);
        SIGNAL_INDUSTRIES.addAchievement(BLOOD_MOON, -2, 2);
        SIGNAL_INDUSTRIES.addAchievement(ECLIPSE, -2, 4);
        SIGNAL_INDUSTRIES.addAchievement(STARFALL, -2, 8);
        //SIGNAL_INDUSTRIES.addAchievement(DIVINE_KNOWLEDGE, -2, 10);
        SIGNAL_INDUSTRIES.addAchievement(WAKING1, 6, 16);
        SIGNAL_INDUSTRIES.addAchievement(WAKING2, 8, 16);
        SIGNAL_INDUSTRIES.addAchievement(WAKING3, 10, 16);
        SIGNAL_INDUSTRIES.addAchievement(WAKING4, 12, 16);
        SIGNAL_INDUSTRIES.addAchievement(AWAKENED, 9, 18);
        SIGNAL_INDUSTRIES.addAchievement(ENDGAME, 9, 20);
        SIGNAL_INDUSTRIES.addAchievement(GATE, 10, 21);

        AchievementPages.register(SIGNAL_INDUSTRIES);

        if (FabricLoaderImpl.INSTANCE.isModLoaded("vintagequesting")) {
            if(SIConfig.config.getBoolean("Other.enableQuests")){
                new VintageQuestingSIPlugin().initializePlugin();
            }
        }

        setInitialized(true);
    }
}
