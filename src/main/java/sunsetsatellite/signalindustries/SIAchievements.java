package sunsetsatellite.signalindustries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.achievements.data.AchievementPages;
import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.Items;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import sunsetsatellite.signalindustries.util.AchievementPageSI;

import static sunsetsatellite.signalindustries.SignalIndustries.*;

public class SIAchievements extends DataInitializer {

	public static Achievement HELP;
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
	public static Achievement MEGA_TROMMEL;

    public static Achievement AWAKENED;
    public static Achievement ENDGAME;

    @Override
    public void init() {
        if (initialized) return;
        LOGGER.info("Initializing achievements...");

		INIT = new Achievement(id("init"), langKey("init"), SIItems.rawSignalumCrystal, null).setType(Achievement.TYPE_SPECIAL).registerAchievement();
		HELP = new Achievement(id("help"), langKey("help"), Items.PAPER, null).setType(Achievement.TYPE_SPECIAL).registerAchievement();
		THE_PROTOTYPE = new Achievement(id("the_prototype"), langKey("thePrototype"), SIBlocks.prototypeMachineCore, INIT).setType(Achievement.TYPE_SPECIAL).registerAchievement();
		FROM_WITHIN = new Achievement(id("from_within"), langKey("fromWithin"), SIBlocks.prototypeExtractor, THE_PROTOTYPE).registerAchievement();
		TRANSFER = new Achievement(id("transfer"), langKey("transfer"), SIBlocks.prototypeConduit, THE_PROTOTYPE).registerAchievement();
		BUFFER = new Achievement(id("buffer"), langKey("buffer"), SIBlocks.prototypeEnergyCell, THE_PROTOTYPE).registerAchievement();
		CRUSHER = new Achievement(id("crusher"), langKey("crusher"), SIBlocks.prototypeCrusher, THE_PROTOTYPE).registerAchievement();
		ALLOY_SMELTER = new Achievement(id("alloy_smelter"), langKey("alloySmelter"), SIBlocks.prototypeAlloySmelter, THE_PROTOTYPE).registerAchievement();
		PLATE_FORMER = new Achievement(id("plate_former"), langKey("plateFormer"), SIBlocks.prototypePlateFormer, THE_PROTOTYPE).registerAchievement();
		SHINING = new Achievement(id("shining"), langKey("shining"), SIItems.signalumCrystal, THE_PROTOTYPE).setType(Achievement.TYPE_SPECIAL).registerAchievement();
		BASIC = new Achievement(id("basic"), langKey("basic"), SIBlocks.basicMachineCore, SHINING).setType(Achievement.TYPE_SPECIAL).registerAchievement();
		ROM_CHIP = new Achievement(id("rom_chip"), langKey("romChip"), SIItems.romChipJump, null).registerAchievement();
		COMBINED = new Achievement(id("combined"), langKey("combined"), SIBlocks.basicCrystalChamber, BASIC).registerAchievement();
		MINER = new Achievement(id("miner"), langKey("miner"), SIBlocks.basicAutomaticMiner, BASIC).registerAchievement();
		PUMP = new Achievement(id("pump"), langKey("pump"), SIBlocks.basicPump, BASIC).registerAchievement();
		HARNESS = new Achievement(id("harness"), langKey("harness"), SIItems.signalumPrototypeHarness, BASIC).registerAchievement();
		PROGRAMMER = new Achievement(id("programmer"), langKey("programmer"), SIBlocks.basicProgrammer, ROM_CHIP).registerAchievement();
		TRIGGER = new Achievement(id("trigger"), langKey("trigger"), SIItems.nullTrigger, PROGRAMMER).registerAchievement();
		CHALLENGE = new Achievement(id("challenge"), langKey("challenge"), SIBlocks.basicWrathBeacon, BASIC).registerAchievement();
		VICTORY = new Achievement(id("victory"), langKey("victory"), SIItems.clearKey, BASIC).registerAchievement();
		RELIC = new Achievement(id("relic"), langKey("relic"), SIBlocks.glowingObsidian, BASIC).registerAchievement();
		KNIGHTS_ALLOY = new Achievement(id("knights_alloy"), langKey("knightAlloy"), SIItems.reinforcedCrystalAlloyIngot, RELIC).registerAchievement();
		REINFORCED = new Achievement(id("reinforced"), langKey("reinforced"), SIBlocks.reinforcedMachineCore, KNIGHTS_ALLOY).setType(Achievement.TYPE_SPECIAL).registerAchievement();
		VICTORY_REINFORCED = new Achievement(id("victory_reinforced"), langKey("victory.reinforced"), SIBlocks.reinforcedWrathBeacon, REINFORCED).registerAchievement();
		BLADE = new Achievement(id("blade"), langKey("blade"), SIItems.signalumSaber, REINFORCED).registerAchievement();
		PULSE = new Achievement(id("pulse"), langKey("pulse"), SIItems.pulsar, REINFORCED).registerAchievement();
		POWER_SUIT = new Achievement(id("power_suit"), langKey("powerSuit"), SIItems.signalumPowerSuitChestplate, REINFORCED).registerAchievement();
		DILITHIUM = new Achievement(id("dilithium"), langKey("dilithium"), SIItems.dilithiumShard, REINFORCED).registerAchievement();
		DIMENSIONAL = new Achievement(id("dimensional"), langKey("dimensional"), SIItems.dimensionalShard, REINFORCED).registerAchievement();
		WARP_ORB = new Achievement(id("warp_orb"), langKey("warpOrb"), SIItems.warpOrb, DIMENSIONAL).registerAchievement();
		ANCHOR = new Achievement(id("anchor"), langKey("anchor"), SIBlocks.dimensionalAnchor, WARP_ORB).registerAchievement();
		TELEPORT_SUCCESS = new Achievement(id("teleport_success"), langKey("teleport.success"), Blocks.GRASS_RETRO, ANCHOR).registerAchievement();
		TELEPORT_FAIL = new Achievement(id("teleport_fail"), langKey("teleport.fail"), SIBlocks.realityFabric, WARP_ORB).registerAchievement();
		ETERNITY = new Achievement(id("eternity"), langKey("eternity"), SIBlocks.rootedFabric, TELEPORT_FAIL).registerAchievement();
		FALSE_ETERNITY = new Achievement(id("false_eternity"), langKey("falseEternity"), SIBlocks.dimensionalShardOre, TELEPORT_FAIL).registerAchievement();
		BOOST = new Achievement(id("boost"), langKey("boost"), SIBlocks.dilithiumBooster, DILITHIUM).registerAchievement();
		WINGS = new Achievement(id("wings"), langKey("wings"), SIItems.crystalWings, POWER_SUIT).registerAchievement();
		HORIZONS = new Achievement(id("horizons"), langKey("horizons"), SIBlocks.reinforcedEnergyConnector, REINFORCED).registerAchievement();
		REACTOR = new Achievement(id("reactor"), langKey("reactor"), SIBlocks.signalumReactorCore, HORIZONS).registerAchievement();
		RISING_ABOVE = new Achievement(id("rising_above"), langKey("risingAbove"), SIItems.awakenedSignalumFragment, REACTOR).registerAchievement();
		BLOOD_MOON = new Achievement(id("blood_moon"), langKey("bloodMoon"), SIItems.monsterShard, null).registerAchievement();
		ECLIPSE = new Achievement(id("eclipse"), langKey("eclipse"), SIItems.infernalFragment, null).registerAchievement();
		STARFALL = new Achievement(id("starfall"), langKey("starfall"), Blocks.LAMP_ACTIVE, null).registerAchievement();
//DIVINE_KNOWLEDGE = new Achievement(id("divine_knowledge"), langKey("divineKnowledge"), SIItems.raziel, null).registerAchievement();
		WAKING1 = new Achievement(id("waking1"), langKey("waking1"), SIBlocks.wakingCrusher, RISING_ABOVE).registerAchievement();
		WAKING2 = new Achievement(id("waking2"), langKey("waking2"), SIBlocks.wakingPlateFormer, RISING_ABOVE).registerAchievement();
		WAKING3 = new Achievement(id("waking3"), langKey("waking3"), SIBlocks.wakingInfuser, RISING_ABOVE).registerAchievement();
		WAKING4 = new Achievement(id("waking4"), langKey("waking4"), SIBlocks.wakingAlloySmelter, RISING_ABOVE).registerAchievement();
		AWAKENED = new Achievement(id("awakened"), langKey("awakened"), SIItems.awakenedSignalumCrystal, RISING_ABOVE).setType(Achievement.TYPE_SPECIAL).registerAchievement();
		ENDGAME = new Achievement(id("endgame"), langKey("endgame"), SIBlocks.awakenedMachineCore, AWAKENED).setType(Achievement.TYPE_SPECIAL).registerAchievement();
		GATE = new Achievement(id("gate"), langKey("gate"), SIBlocks.warpGate, ENDGAME).registerAchievement();
		MEGA_TROMMEL = new Achievement(id("mega_trommel"), langKey("megaTrommel"), SIBlocks.awakenedTrommel, ENDGAME).registerAchievement();

		setInitialized(true);
    }

	@Environment(EnvType.CLIENT)
	public void initClient(){
		AchievementPageSI SIGNAL_INDUSTRIES = new AchievementPageSI();

		SIGNAL_INDUSTRIES.addAchievement(INIT, 0, -10);
		SIGNAL_INDUSTRIES.addAchievement(HELP, 0, -12);
		SIGNAL_INDUSTRIES.addAchievement(THE_PROTOTYPE, 2, -10);
		SIGNAL_INDUSTRIES.addAchievement(FROM_WITHIN, 3, -11);
		SIGNAL_INDUSTRIES.addAchievement(TRANSFER, 4, -9);
		SIGNAL_INDUSTRIES.addAchievement(BUFFER, 5, -11);
		SIGNAL_INDUSTRIES.addAchievement(CRUSHER, 6, -9);
		SIGNAL_INDUSTRIES.addAchievement(ALLOY_SMELTER, 7, -11);
		SIGNAL_INDUSTRIES.addAchievement(PLATE_FORMER, 8, -9);
		SIGNAL_INDUSTRIES.addAchievement(SHINING, 9, -10);
		SIGNAL_INDUSTRIES.addAchievement(BASIC, 2, -8);
		SIGNAL_INDUSTRIES.addAchievement(ROM_CHIP, -2, -2);
		SIGNAL_INDUSTRIES.addAchievement(COMBINED, 4, -7);
		SIGNAL_INDUSTRIES.addAchievement(MINER, 6, -7);
		SIGNAL_INDUSTRIES.addAchievement(PUMP, 8, -7);
		SIGNAL_INDUSTRIES.addAchievement(HARNESS, 3, -5);
		SIGNAL_INDUSTRIES.addAchievement(PROGRAMMER, 5, -5);
		SIGNAL_INDUSTRIES.addAchievement(TRIGGER, 7, -5);
		SIGNAL_INDUSTRIES.addAchievement(CHALLENGE, 11, -5);
		SIGNAL_INDUSTRIES.addAchievement(VICTORY, 13, -5);
		SIGNAL_INDUSTRIES.addAchievement(RELIC, 9, -6);
		SIGNAL_INDUSTRIES.addAchievement(KNIGHTS_ALLOY, 9, -4);
		SIGNAL_INDUSTRIES.addAchievement(REINFORCED, 9, -2);
		SIGNAL_INDUSTRIES.addAchievement(VICTORY_REINFORCED, 11, -3);
		SIGNAL_INDUSTRIES.addAchievement(BLADE, 7, -3);
		SIGNAL_INDUSTRIES.addAchievement(PULSE, 5, -3);
		SIGNAL_INDUSTRIES.addAchievement(POWER_SUIT, 3, -3);
		SIGNAL_INDUSTRIES.addAchievement(DILITHIUM, 7, -1);
		SIGNAL_INDUSTRIES.addAchievement(DIMENSIONAL, 11, -1);
		SIGNAL_INDUSTRIES.addAchievement(WARP_ORB, 13, -1);
		SIGNAL_INDUSTRIES.addAchievement(ANCHOR, 15, -1);
		SIGNAL_INDUSTRIES.addAchievement(TELEPORT_SUCCESS, 17, -2);
		SIGNAL_INDUSTRIES.addAchievement(TELEPORT_FAIL, 17, 0);
		SIGNAL_INDUSTRIES.addAchievement(ETERNITY, 19, 0);
		SIGNAL_INDUSTRIES.addAchievement(FALSE_ETERNITY, 17, 2);
		SIGNAL_INDUSTRIES.addAchievement(BOOST, 5, -1);
		SIGNAL_INDUSTRIES.addAchievement(WINGS, 1, -3);
		SIGNAL_INDUSTRIES.addAchievement(HORIZONS, 9, 0);
		SIGNAL_INDUSTRIES.addAchievement(REACTOR, 9, 2);
		SIGNAL_INDUSTRIES.addAchievement(RISING_ABOVE, 9, 4);
		SIGNAL_INDUSTRIES.addAchievement(BLOOD_MOON, -2, -8);
		SIGNAL_INDUSTRIES.addAchievement(ECLIPSE, -2, -6);
		SIGNAL_INDUSTRIES.addAchievement(STARFALL, -2, -4);
//SIGNAL_INDUSTRIES.addAchievement(DIVINE_KNOWLEDGE, -2, 0);
		SIGNAL_INDUSTRIES.addAchievement(WAKING1, 6, 6);
		SIGNAL_INDUSTRIES.addAchievement(WAKING2, 8, 6);
		SIGNAL_INDUSTRIES.addAchievement(WAKING3, 10, 6);
		SIGNAL_INDUSTRIES.addAchievement(WAKING4, 12, 6);
		SIGNAL_INDUSTRIES.addAchievement(AWAKENED, 9, 8);
		SIGNAL_INDUSTRIES.addAchievement(ENDGAME, 9, 10);
		SIGNAL_INDUSTRIES.addAchievement(GATE, 10, 12);
		SIGNAL_INDUSTRIES.addAchievement(MEGA_TROMMEL, 8, 12);

		AchievementPages.register(SIGNAL_INDUSTRIES);
	}
}
