package sunsetsatellite.signalindustries;

import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.achievement.stat.Stat;
import net.minecraft.core.block.Block;
import sunsetsatellite.signalindustries.util.DataInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;

import static sunsetsatellite.signalindustries.SignalIndustries.*;
import static sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage.*;

public class SIAchievements extends DataInitializer {
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
    public static Achievement DIMENSIONAl;
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

    public void init() {
        if(initialized) return;
        LOGGER.info("Initializing achievements...");
        INIT = new Achievement(nextAchievementID++, key("init"), 0, -offsetY, SIItems.rawSignalumCrystal, null);
        THE_PROTOTYPE = new Achievement(nextAchievementID++, key("thePrototype"), 2, -offsetY, SIBlocks.prototypeMachineCore, INIT);
        FROM_WITHIN = new Achievement(nextAchievementID++, key("fromWithin"), 3, -1 - offsetY, SIBlocks.prototypeExtractor, THE_PROTOTYPE);
        TRANSFER = new Achievement(nextAchievementID++, key("transfer"), 4, 1 - offsetY, SIBlocks.prototypeConduit, THE_PROTOTYPE);
        BUFFER = new Achievement(nextAchievementID++, key("buffer"), 5, -1 - offsetY, SIBlocks.prototypeEnergyCell, THE_PROTOTYPE);
        CRUSHER = new Achievement(nextAchievementID++, key("crusher"), 6, 1 - offsetY, SIBlocks.prototypeCrusher, THE_PROTOTYPE);
        ALLOY_SMELTER = new Achievement(nextAchievementID++, key("alloySmelter"), 7, -1 - offsetY, SIBlocks.prototypeAlloySmelter, THE_PROTOTYPE);
        PLATE_FORMER = new Achievement(nextAchievementID++, key("plateFormer"), 8, 1 - offsetY, SIBlocks.prototypePlateFormer, THE_PROTOTYPE);
        SHINING = new Achievement(nextAchievementID++, key("shining"), 9, -offsetY, SIItems.signalumCrystal, THE_PROTOTYPE);
        BASIC = new Achievement(nextAchievementID++, key("basic"), 2, 2 - offsetY, SIBlocks.basicMachineCore, SHINING);
        ROM_CHIP = new Achievement(nextAchievementID++, key("romChip"), -2, 6 - offsetY, SIItems.romChipBoost, null);
        COMBINED = new Achievement(nextAchievementID++, key("combined"), 4, 3 - offsetY, SIBlocks.basicCrystalChamber, BASIC);
        MINER = new Achievement(nextAchievementID++, key("miner"), 6, 3 - offsetY, SIBlocks.basicAutomaticMiner, BASIC);
        PUMP = new Achievement(nextAchievementID++, key("pump"), 8, 3 - offsetY, SIBlocks.basicPump, BASIC);
        HARNESS = new Achievement(nextAchievementID++, key("harness"), 3, 5 - offsetY, SIItems.signalumPrototypeHarness, BASIC);
        PROGRAMMER = new Achievement(nextAchievementID++, key("programmer"), 5, 5 - offsetY, SIBlocks.basicProgrammer, ROM_CHIP);
        TRIGGER = new Achievement(nextAchievementID++, key("trigger"), 7, 5 - offsetY, SIItems.nullTrigger, PROGRAMMER);
        CHALLENGE = new Achievement(nextAchievementID++, key("challenge"), 11, 5 - offsetY, SIBlocks.basicWrathBeacon, BASIC);
        VICTORY = new Achievement(nextAchievementID++, key("victory"), 13, 5 - offsetY, SIItems.energyCatalyst, BASIC);
        RELIC = new Achievement(nextAchievementID++, key("relic"), 9, 4 - offsetY, SIBlocks.glowingObsidian, BASIC);
        KNIGHTS_ALLOY = new Achievement(nextAchievementID++, key("knightAlloy"), 9, 6 - offsetY, SIItems.reinforcedCrystalAlloyIngot, RELIC);
        REINFORCED = new Achievement(nextAchievementID++, key("reinforced"), 9, 8 - offsetY, SIBlocks.reinforcedMachineCore, KNIGHTS_ALLOY);
        VICTORY_REINFORCED = new Achievement(nextAchievementID++, key("victory.reinforced"), 11, 7 - offsetY, SIBlocks.reinforcedWrathBeacon, REINFORCED);
        BLADE = new Achievement(nextAchievementID++, key("blade"), 7, 7 - offsetY, SIItems.signalumSaber, REINFORCED);
        PULSE = new Achievement(nextAchievementID++, key("pulse"), 5, 7 - offsetY, SIItems.pulsar, REINFORCED);
        POWER_SUIT = new Achievement(nextAchievementID++, key("powerSuit"), 3, 7 - offsetY, SIItems.signalumPowerSuitChestplate, REINFORCED);
        DILITHIUM = new Achievement(nextAchievementID++, key("dilithium"), 7, 9 - offsetY, SIItems.dilithiumShard, REINFORCED);
        DIMENSIONAl = new Achievement(nextAchievementID++, key("dimensional"), 11, 9 - offsetY, SIItems.dimensionalShard, REINFORCED);
        WARP_ORB = new Achievement(nextAchievementID++, key("warpOrb"), 13, 9 - offsetY, SIItems.warpOrb, DIMENSIONAl);
        ANCHOR = new Achievement(nextAchievementID++, key("anchor"), 15, 9 - offsetY, SIBlocks.dimensionalAnchor, WARP_ORB);
        TELEPORT_SUCCESS = new Achievement(nextAchievementID++, key("teleport.success"), 17, 8 - offsetY, Block.grassRetro, ANCHOR);
        TELEPORT_FAIL = new Achievement(nextAchievementID++, key("teleport.fail"), 17, 10 - offsetY, SIBlocks.realityFabric, ANCHOR);
        ETERNITY = new Achievement(nextAchievementID++, key("eternity"), 19, 10 - offsetY, SIBlocks.rootedFabric, TELEPORT_FAIL);
        FALSE_ETERNITY = new Achievement(nextAchievementID++, key("falseEternity"), 17, 12 - offsetY, SIBlocks.dimensionalShardOre, TELEPORT_FAIL);
        BOOST = new Achievement(nextAchievementID++, key("boost"), 5, 9 - offsetY, SIBlocks.dilithiumBooster, DILITHIUM);
        WINGS = new Achievement(nextAchievementID++, key("wings"), 1, 7 - offsetY, SIItems.crystalWings, POWER_SUIT);
        HORIZONS = new Achievement(nextAchievementID++, key("horizons"), 9, 10 - offsetY, SIBlocks.reinforcedEnergyConnector, REINFORCED);
        REACTOR = new Achievement(nextAchievementID++, key("reactor"), 9, 12 - offsetY, SIBlocks.signalumReactorCore, HORIZONS);
        RISING_ABOVE = new Achievement(nextAchievementID++, key("comingSoon"), 9, 14 - offsetY, SIItems.awakenedSignalumFragment, REACTOR);
        BLOOD_MOON = new Achievement(nextAchievementID++, key("bloodMoon"), -2, 2 - offsetY, SIItems.monsterShard, null);
        ECLIPSE = new Achievement(nextAchievementID++, key("eclipse"), -2, 4 - offsetY, SIItems.infernalFragment, null);
        STARFALL = new Achievement(nextAchievementID++, key("starfall"), -2, 8 - offsetY, Block.lampActive, null);

        Field[] achievements = SIAchievements.class.getDeclaredFields();
        Arrays.stream(achievements).filter((F)->F.getType().equals(Achievement.class)).forEach((F)->{
            try {
                ACHIEVEMENTS.achievementList.add((Achievement) ((Stat) F.get(null)).registerStat());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        setInitialized(true);
    }
}