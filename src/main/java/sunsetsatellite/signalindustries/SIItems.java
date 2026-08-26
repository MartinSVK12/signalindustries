package sunsetsatellite.signalindustries;

import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.Items;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import sunsetsatellite.signalindustries.abilities.powersuit.ScanSuitAbility;
import sunsetsatellite.signalindustries.covers.*;
import sunsetsatellite.signalindustries.items.*;
import sunsetsatellite.signalindustries.items.applications.ItemPortableWorkbench;
import sunsetsatellite.signalindustries.items.applications.ItemSmartWatch;
import sunsetsatellite.signalindustries.items.applications.ItemTrigger;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithAbility;
import sunsetsatellite.signalindustries.items.attachments.*;
import sunsetsatellite.signalindustries.items.base.ItemArmorTiered;
import sunsetsatellite.signalindustries.items.covers.ItemCover;
import sunsetsatellite.signalindustries.items.tools.*;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryCategory;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static sunsetsatellite.catalyst.Catalyst.listOf;
import static sunsetsatellite.signalindustries.SIConfig.item;
import static sunsetsatellite.signalindustries.SignalIndustries.*;
import static sunsetsatellite.signalindustries.SignalIndustries.MOD_ID;

public class SIItems extends DataInitializer {

	public static HashMap<Item, String> itemTextures = new HashMap<>();

	public static Item signalumCrystalEmpty;
	public static Item signalumCrystal;
	public static Item signalumCrystalBattery;
	public static Item infiniteSignalumCrystal;
	public static Item volatileSignalumCrystal;
	public static Item rawSignalumCrystal;
	public static Item awakenedSignalumCrystal;
	public static Item awakenedSignalumFragment;
	public static Item coalDust;
	public static Item ironDust;
	public static Item goldDust;
	public static Item netherCoalDust;
	public static Item tinyNetherCoalDust;
	public static Item emptySignalumCrystalDust;
	public static Item saturatedSignalumCrystalDust;
	public static Item awakenedSignalumCrystalDust;
	public static Item ironPlateHammer;
	public static Item cobblestonePlate;
	public static Item stonePlate;
	public static Item crystalAlloyPlate;
	public static Item steelPlate;
	public static Item reinforcedCrystalAlloyPlate;
	public static Item saturatedSignalumAlloyPlate;
	public static Item dilithiumPlate;
	public static Item voidAlloyPlate;
	public static Item awakenedAlloyPlate;
	public static Item crystalAlloyIngot;
	public static Item reinforcedCrystalAlloyIngot;
	public static Item saturatedSignalumAlloyIngot;
	public static Item signalumAlloyMesh;
	public static Item voidAlloyIngot;
	public static Item awakenedAlloyIngot;
	public static Item diamondCuttingGear;
	public static Item signalumCuttingGear;
	public static Item realityString;
	public static Item dilithiumShard;
	public static Item monsterShard;
	public static Item infernalFragment;
	public static Item evilEye;
	public static Item infernalEye;
	public static Item dimensionalShard;
	public static Item dimensionalFragment;
	public static Item warpOrb;
	public static ItemArmorTiered signalumPrototypeHarness;
	public static ItemArmorTiered signalumPrototypeHarnessGoggles;
	public static Item basicSignalumDrill;
	public static Item reinforcedSignalumDrill;
	public static Item fuelCell;
	public static Item nullTrigger;
	public static Item clearKey;
	public static Item saturatedKey;
	public static Item signalumSaber;
	public static Item pulsar;
	public static ItemSignalumPowerSuit signalumPowerSuitHelmet;
	public static ItemSignalumPowerSuit signalumPowerSuitChestplate;
	public static ItemSignalumPowerSuit signalumPowerSuitLeggings;
	public static ItemSignalumPowerSuit signalumPowerSuitBoots;
	public static Item crystalChip;
	public static Item pureCrystalChip;
	public static Item awakenedCrystalChip;
	public static Item basicEnergyCore;
	public static Item reinforcedEnergyCore;
	public static Item awakenedEnergyCore;
	public static Item basicDrillBit;
	public static Item reinforcedDrillBit;
	public static Item basicDrillCasing;
	public static Item reinforcedDrillCasing;
	public static Item pulsarShell;
	public static Item pulsarInnerCore;
	public static Item pulsarOuterCore;
	public static Item itemManipulationCircuit;
	public static Item fluidManipulationCircuit;
	public static Item dilithiumControlCore;
	public static Item warpManipulatorCircuit;
	public static Item dilithiumChip;
	public static Item dimensionalChip;
	public static Item attachmentPoint;
	public static Item crystalWingPart;
	public static Item meteorTracker;
	public static Item reinforcedMeteorTracker;
	public static Item configurationTablet;
	public static Item blankAbilityModule;
	public static Item abilityContainerCasing;
	public static Item blankChip;
	public static Item positionMemoryChip;
	public static Item precisionControlChip;
	public static Item unlimitedChip;
	public static Item condensedMilkCan;
	public static Item bucketCaramel;
	public static Item caramelPlate;
	public static Item krowka;
	public static Item blueprint;
	public static Item goldprint;
	public static Item heatingCoil;
	public static Item coolingCoil;

	public static ItemAttachment pulsarAttachment;
	public static ItemAttachment extendedEnergyPack;
	public static ItemWingsAttachment crystalWings;
	public static ItemAttachment annihilationCrown;
	public static ItemAttachment basicBackpack;
	public static ItemAttachment reinforcedBackpack;
	public static ItemAttachment nightVisionLens;
	public static ItemAttachment movementBoosters;
	public static ItemPortableWorkbench portableWorkbench;
	public static ItemSmartWatch smartWatch;
	public static ItemAttachment abilityModule;
	public static ItemAttachment awakenedAbilityModule;

	public static Item romChipProjectile;
	public static Item romChipJump;
	public static Item romChipShield;
	public static Item romChipScan;

	public static Item scanAbilityContainer;

	public static ItemCover blankCover;
	public static ItemCover redstoneCover;
	public static ItemCover voidCover;
	public static ItemCover conveyorCover;
	public static ItemCover pumpCover;
	public static ItemCover switchCover;
	public static ItemCover dilithiumLensCover;

	public static Item raziel;

	public static ItemSuitColorizer suitColorizerWhite;
	public static ItemSuitColorizer suitColorizerBlue;
	public static ItemSuitColorizer suitColorizerPurple;
	public static ItemSuitColorizer suitColorizerTransparent;
	public static ItemSuitColorizer suitColorizerInverted;

	public static Item dimensionMaker;

	public static Map<ItemRomChip, ItemWithAbility> chipsToAbilityMap = new HashMap<>();

	@Override
	public void init() {
		if (initialized) return;
		LOGGER.info("Initializing items...");

		rawSignalumCrystal = simpleItem("rawSignalumCrystal", "raw_signalite_crystal", "rawSignalumCrystal", "raw_signalum_crystal");
		signalumCrystal = simpleItem("signalumCrystal", "signalite_crystal", "signalumCrystal", "signalum_crystal");
		signalumCrystalEmpty = simpleItem("signalumCrystalEmpty", "signalite_crystal_empty", "signalumCrystalEmpty", "signalum_crystal_empty");
		awakenedSignalumCrystal = simpleItem("awakenedSignalumCrystal", "awakened_signalite_crystal", "awakenedSignalumCrystal", "awakened_signalum_crystal");
		awakenedSignalumFragment = simpleItem("awakenedSignalumFragment", "awakened_signalite_fragment", "awakenedSignalumFragment", "awakened_signalum_fragment");
		coalDust = simpleItem("coalDust", "coal_dust", "coalDust", "coal_dust");
		ironDust = simpleItem("ironDust", "iron_dust", "ironDust", "iron_dust");
		goldDust = simpleItem("goldDust", "gold_dust", "goldDust", "gold_dust");
		netherCoalDust = simpleItem("netherCoalDust", "nether_coal_dust", "netherCoalDust", "nethercoaldust");
		tinyNetherCoalDust = simpleItem("tinyNetherCoalDust", "tiny_nether_coal_dust", "tinyNetherCoalDust", "tiny_nether_coal_dust");
		emptySignalumCrystalDust = simpleItem("emptySignalumCrystalDust", "empty_signalite_crystal_dust", "signalumCrystalDust", "empty_signalum_dust");
		saturatedSignalumCrystalDust = simpleItem("saturatedSignalumCrystalDust", "saturated_signalite_crystal_dust", "saturatedSignalumCrystalDust", "saturated_signalum_dust");
		awakenedSignalumCrystalDust = simpleItem("awakenedSignalumCrystalDust", "awakened_signalite_crystal_dust", "awakenedSignalumCrystalDust", "awakened_signalum_dust");
		ironPlateHammer = simpleItem("ironPlateHammer", "iron_plate_hammer", "ironPlateHammer", "plate_hammer").setMaxStackSize(1);
		ironPlateHammer.setContainerItem(ironPlateHammer);
		cobblestonePlate = simpleItem("cobblestonePlate", "cobblestone_plate", "cobblestonePlate", "cobblestone_plate");
		stonePlate = simpleItem("stonePlate", "stone_plate", "stonePlate", "stone_plate");
		crystalAlloyPlate = simpleItem("crystalAlloyPlate", "crystal_alloy_plate", "crystalAlloyPlate", "crystal_alloy_plate");
		steelPlate = simpleItem("steelPlate", "steel_plate", "steelPlate", "steel_plate");
		reinforcedCrystalAlloyPlate = simpleItem("reinforcedCrystalAlloyPlate", "reinforced_crystal_alloy_plate", "reinforcedCrystalAlloyPlate", "reinforced_crystal_alloy_plate");
		saturatedSignalumAlloyPlate = simpleItem("saturatedSignalumAlloyPlate", "saturated_signalite_alloy_plate", "saturatedSignalumAlloyPlate", "saturated_signalum_alloy_plate");
		dilithiumPlate = simpleItem("dilithiumPlate", "dilithium_plate", "dilithiumPlate", "dilithium_plate");
		voidAlloyPlate = simpleItem("voidAlloyPlate", "void_alloy_plate", "voidAlloyPlate", "void_alloy_plate");
		awakenedAlloyPlate = simpleItem("awakenedAlloyPlate", "awakened_alloy_plate", "awakenedAlloyPlate", "awakened_alloy_plate");
		crystalAlloyIngot = simpleItem("crystalAlloyIngot", "crystal_alloy_ingot", "crystalAlloyIngot", "crystal_alloy");
		reinforcedCrystalAlloyIngot = simpleItem("reinforcedCrystalAlloyIngot", "reinforced_crystal_alloy_ingot", "reinforcedCrystalAlloyIngot", "reinforced_crystal_alloy");
		saturatedSignalumAlloyIngot = simpleItem("saturatedSignalumAlloyIngot", "saturated_signalite_alloy_ingot", "saturatedSignalumAlloyIngot", "saturated_signalum_alloy");
		voidAlloyIngot = simpleItem("voidAlloyIngot", "void_alloy_ingot", "voidAlloyIngot", "void_alloy");
		awakenedAlloyIngot = simpleItem("awakenedAlloyIngot", "awakened_alloy_ingot", "awakenedAlloyIngot", "awakened_alloy");
		diamondCuttingGear = simpleItem("diamondCuttingGear", "diamond_cutting_gear", "diamondCuttingGear", "diamond_cutting_gear");
		signalumCuttingGear = simpleItem("signalumCuttingGear", "signalite_cutting_gear", "signalumCuttingGear", "signalum_cutting_gear");
		realityString = simpleItem("realityString", "string_of_reality", "realityString", "string_of_reality");
		dilithiumShard = simpleItem("dilithiumShard", "dilithium_shard", "dilithiumShard", "dilithium_shard");
		monsterShard = simpleItem("monsterShard", "monster_shard", "monsterShard", "monster_shard");
		infernalFragment = simpleItem("infernalFragment", "infernal_fragment", "infernalFragment", "infernal_fragment");
		evilEye = simpleItem("evilEye", "evil_eye", "evilEye", "evileye").setMaxStackSize(4);
		infernalEye = simpleItem("infernalEye", "infernal_eye", "infernalEye", "infernaleye").setMaxStackSize(4);
		dimensionalShard = simpleItem("dimensionalShard", "dimensional_shard", "dimensionalShard", "dimensional_shard");
		crystalChip = simpleItem("crystalChip", "crystal_chip", "crystalChip", "crystal_chip");
		pureCrystalChip = simpleItem("pureCrystalChip", "pure_crystal_chip", "pureCrystalChip", "pure_crystal_chip");
		awakenedCrystalChip = simpleItem("awakenedCrystalChip", "awakened_crystal_chip", "awakenedCrystalChip", "awakened_crystal_chip");
		basicEnergyCore = simpleItem("basicEnergyCore", "basic_energy_core", "basicEnergyCore", "basic_energy_core");
		reinforcedEnergyCore = simpleItem("reinforcedEnergyCore", "reinforced_energy_core", "reinforcedEnergyCore", "reinforced_energy_core");
		awakenedEnergyCore = simpleItem("awakenedEnergyCore", "awakened_energy_core", "awakenedEnergyCore", "awakened_energy_core");
		basicDrillBit = simpleItem("basicDrillBit", "basic_drill_bit", "basicDrillBit", "basic_drill_bit");
		reinforcedDrillBit = simpleItem("reinforcedDrillBit", "reinforced_drill_bit", "reinforcedDrillBit", "reinforced_drill_bit");
		basicDrillCasing = simpleItem("basicDrillCasing", "basic_drill_casing", "basicDrillCasing", "basic_drill_casing");
		reinforcedDrillCasing = simpleItem("reinforcedDrillCasing", "reinforced_drill_casing", "reinforcedDrillCasing", "reinforced_drill_casing");
		pulsarShell = simpleItem("pulsarShell", "pulsar_shell", "pulsarShell", "pulsar_shell");
		pulsarInnerCore = simpleItem("pulsarInnerCore", "pulsar_inner_core", "pulsarInnerCore", "pulsar_inner_core");
		pulsarOuterCore = simpleItem("pulsarOuterCore", "pulsar_outer_core", "pulsarOuterCore", "pulsar_outer_core");
		itemManipulationCircuit = simpleItem("itemManipulationCircuit", "item_manipulation_circuit", "itemManipulationCircuit", "item_manipulation_circuit");
		fluidManipulationCircuit = simpleItem("fluidManipulationCircuit", "fluid_manipulation_circuit", "fluidManipulationCircuit", "fluid_manipulation_circuit");
		dilithiumControlCore = simpleItem("dilithiumControlCore", "dilithium_control_core", "dilithiumControlCore", "dilithium_control_core");
		warpManipulatorCircuit = simpleItem("warpManipulatorCircuit", "warp_manipulator_circuit", "warpManipulatorCircuit", "warp_manipulator_circuit");
		dilithiumChip = simpleItem("dilithiumChip", "dilithium_chip", "dilithiumChip", "dilithium_chip");
		dimensionalChip = simpleItem("dimensionalChip", "dimensional_chip", "dimensionalChip", "dimensional_chip");
		attachmentPoint = simpleItem("attachmentPoint", "attachment_point", "attachmentPoint", "attachment_point");
		blankAbilityModule = simpleItem("blankAbilityModule", "blank_ability_module", "blankAbilityModule", "blank_module");
		abilityContainerCasing = simpleItem("abilityContainerCasing", "ability_container_casing", "abilityContainerCasing", "ability_container_casing");
		blankChip = simpleItem("blankChip", "blank_chip", "romChip.blank", "blank_chip");
		condensedMilkCan = simpleItem("condensedMilkCan", "condensed_milk_can", "condensedMilkCan", "condensed_milk_can").setMaxStackSize(1);
		bucketCaramel = simpleItem("bucketCaramel", "bucket_caramel", "bucketCaramel", "bucket_caramel").setMaxStackSize(1).setContainerItem(Items.BUCKET_IRON);
		caramelPlate = simpleItem("caramelPlate", "caramel_plate", "caramelPlate", "caramel_plate");
		signalumAlloyMesh = simpleItem("signalumAlloyMesh", "signalum_alloy_mesh", "signalumAlloyMesh", "signalum_alloy_mesh");
		crystalWingPart = simpleItem("crystalWingPart", "crystal_wing_part", "crystalWingPart", "crystal_wing_part").setMaxStackSize(6);
		precisionControlChip = simpleItem("precisionControlChip", "precision_control_chip", "romChip.precision", "precision_control_chip");
		clearKey = simpleItem("clearKey", "clear_key", "clearKey", "clear_key");
		saturatedKey = simpleItem("saturatedKey", "saturated_key", "saturatedKey", "saturated_key");
		unlimitedChip = simpleItem("unlimitedChip", "unlimited_chip", "romChip.unlimited", "unlimited_chip");
		heatingCoil = simpleItem("heatingCoil", "heating_coil", "heatingCoil", "heating_coil");
		coolingCoil = simpleItem("coolingCoil", "cooling_coil", "coolingCoil", "cooling_coil");
		//ashenTreeSapling = simpleItem("ashenTreeSapling", "ashen_tree_sapling","sapling.ashen","ashen_tree_sapling");

		raziel = simpleItem("raziel", "raziel", "raziel", "raziel");

		signalumCrystalBattery = customItem(() ->
				new ItemSignalumCrystal(
					"signalumCrystal.battery",
					key("item/signalite_crystal_battery"),
					item("signalumCrystalBattery"),
					false
				),
			"signalum_crystal_battery")
			.setMaxStackSize(1);

		infiniteSignalumCrystal = customItem(() ->
				new ItemSignalumCrystal(
					"infiniteSignalumCrystal",
					key("item/infinite_signalite_crystal"),
					item("infiniteSignalumCrystal"),
					true
				),
			"infinite_signalum_crystal")
			.setMaxStackSize(1);

		dimensionalFragment = customItem(() ->
				new ItemDimFragment(
					"dimensionalFragment",
					key("item/dimensional_fragment"),
					item("dimensionalFragment")
				),
			"dimensional_fragment");

		portableWorkbench = (ItemPortableWorkbench) customItem(() -> new ItemPortableWorkbench(
				"basic.portableWorkbench",
				key("item/portable_workbench"),
				item("portableWorkbench"),
				Tier.BASIC),
			"portable_workbench")
			.setMaxStackSize(1);

		smartWatch = (ItemSmartWatch) customItem(() -> new ItemSmartWatch(
				"basic.smartWatch",
				key("item/smart_watch"),
				item("smartWatch"),
				Tier.BASIC),
			"smartwatch")
			.setMaxStackSize(1);

		basicSignalumDrill = customItem(() -> new ItemSignalumDrill(
				"basic.signalumDrill",
				key("item/basic_signalite_drill"),
				item("basicSignalumDrill"), toolMaterialBasic, Tier.BASIC),
			"signalum_drill"
		).setMaxStackSize(1);

		reinforcedSignalumDrill = customItem(() -> new ItemSignalumDrill(
				"reinforced.signalumDrill",
				key("item/reinforced_signalite_drill"),
				item("reinforcedSignalumDrill"), toolMaterialReinforced, Tier.REINFORCED),
			"signalum_drill_reinforced"
		).setMaxStackSize(1);

		signalumSaber = customItem(() -> new ItemSignalumSaber(
				"reinforced.signalumSaber",
				key("item/reinforced_signalite_saber"),
				item("signalumSaber"), toolMaterialReinforced, Tier.REINFORCED),
			"signalum_saber_unpowered"
		).setMaxStackSize(1);

		configurationTablet = customItem(() -> new ItemConfigurationTablet(
				"configurationTablet.rotation",
				key("item/configuration_tablet"),
				item("configurationTablet")),
			"configuration_tablet_rotation"
		).setMaxStackSize(1);

		volatileSignalumCrystal = customItem(
			() -> new ItemVolatileSignalumCrystal("volatileSignalumCrystal", key("volatile_signalite_crystal"), item("volatileSignalumCrystal")),
			"volatile_signalum_crystal"
		).setMaxStackSize(4);

		blankCover = (ItemCover) customItem(() -> new ItemCover("cover.blank", key("item/blank_cover"), item("blankCover"), BlankCover::new), "blank_cover");
		dilithiumLensCover = (ItemCover) customItem(() -> new ItemCover("cover.dilithiumLens", key("item/dilithium_lens_cover"), item("dilithiumLensCover"), DilithiumLensCover::new), "dilithium_lens");
		conveyorCover = (ItemCover) customItem(() -> new ItemCover("cover.item", key("item/conveyor_cover"), item("conveyorCover"), ConveyorCover::new), "conveyor_cover");
		pumpCover = (ItemCover) customItem(() -> new ItemCover("cover.fluid", key("item/pump_cover"), item("pumpCover"), PumpCover::new), "pump_cover");
		switchCover = (ItemCover) customItem(() -> new ItemCover("cover.switch", key("item/switch_cover"), item("switchCover"), SwitchCover::new), "switch_cover");
		redstoneCover = (ItemCover) customItem(() -> new ItemCover("cover.redstone", key("item/redstone_cover"), item("redstoneCover"), RedstoneCover::new), "redstone_cover");
		voidCover = (ItemCover) customItem(() -> new ItemCover("cover.void", key("item/void_cover"), item("voidCover"), VoidCover::new), "void_cover");

		pulsar = customItem(() -> new ItemPulsar("reinforced.pulsar", key("item/pulsar"), item("pulsar"), Tier.REINFORCED), "pulsar_inactive").setMaxStackSize(1);

		meteorTracker = customItem(() -> new ItemMeteorTracker("meteorTracker", key("item/meteor_tracker"), item("meteorTracker")), "meteor_tracker_uncalibrated").setMaxStackSize(1);
		reinforcedMeteorTracker = customItem(() -> new ItemReinforcedMeteorTracker("reinforced.meteorTracker", key("item/reinforced_meteor_tracker"), item("reinforcedMeteorTracker")), "reinforced_meteor_tracker_uncalibrated").setMaxStackSize(1);

		warpOrb = customItem(() -> new ItemWarpOrb("warpOrb", key("item/warp_orb"), item("warpOrb")), "warp_orb").setMaxStackSize(1);

		fuelCell = customItem(() -> new ItemFuelCell("fuelCell", key("item/fuel_cell"), item("fuelCell")), "fuelcellempty").setMaxStackSize(1);

		romChipProjectile = customItem(() -> new ItemRomChip("romChip.projectile", key("item/rom_chip_projectile"), item("romChipProjectile")), "chip1").setMaxStackSize(1);
		romChipJump = customItem(() -> new ItemRomChip("romChip.jump", key("item/rom_chip_jump"), item("romChipJump")), "chip2").setMaxStackSize(1);
		romChipShield = customItem(() -> new ItemRomChip("romChip.shield", key("item/rom_chip_shield"), item("romChipShield")), "chip3").setMaxStackSize(1);
		romChipScan = customItem(() -> new ItemRomChip("romChip.scan", key("item/rom_chip_scab"), item("romChipScan")), "chip4").setMaxStackSize(1);

		positionMemoryChip = customItem(() -> new ItemPositionChip("romChip.position", key("item/position_chip"), item("positionMemoryChip")), "position_chip").setMaxStackSize(1);

		nullTrigger = customItem(() -> new ItemTrigger("trigger.null", key("item/trigger"), item("nullTrigger")), "trigger").setMaxStackSize(1);

		signalumPrototypeHarness = (ItemArmorTiered) customItem(() -> new ItemSignalumPowerHarness("basic.prototypeHarness", key("item/harness"), item("signalumPrototypeHarness"), armorPrototypeHarness, HumanArmorShape.CHEST, Tier.BASIC), "harness").setMaxStackSize(1);
		signalumPrototypeHarnessGoggles = (ItemArmorTiered) customItem(() -> new ItemSignalumPowerHarness("basic.prototypeHarnessGoggles", key("item/harness_goggles"), item("signalumPrototypeHarnessGoggles"), armorPrototypeHarness, HumanArmorShape.HEAD, Tier.BASIC), "goggles").setMaxStackSize(1);

		signalumPowerSuitHelmet = (ItemSignalumPowerSuit) customItem(() -> new ItemSignalumPowerSuit("reinforced.powerSuit.helmet", key("item/power_suit_helmet"), item("signalumPowerSuitHelmet"), armorSignalumPowerSuit, HumanArmorShape.HEAD, Tier.REINFORCED), "signalumpowersuit_helmet").setMaxStackSize(1);
		signalumPowerSuitChestplate = (ItemSignalumPowerSuit) customItem(() -> new ItemSignalumPowerSuit("reinforced.powerSuit.chestplate", key("item/power_suit_chestplate"), item("signalumPowerSuitChestplate"), armorSignalumPowerSuit, HumanArmorShape.CHEST, Tier.REINFORCED), "signalumpowersuit_chestplate").setMaxStackSize(1);
		signalumPowerSuitLeggings = (ItemSignalumPowerSuit) customItem(() -> new ItemSignalumPowerSuit("reinforced.powerSuit.leggings", key("item/power_suit_leggings"), item("signalumPowerSuitLeggings"), armorSignalumPowerSuit, HumanArmorShape.LEGS, Tier.REINFORCED), "signalumpowersuit_leggings").setMaxStackSize(1);
		signalumPowerSuitBoots = (ItemSignalumPowerSuit) customItem(() -> new ItemSignalumPowerSuit("reinforced.powerSuit.boots", key("item/power_suit_boots"), item("signalumPowerSuitBoots"), armorSignalumPowerSuit, HumanArmorShape.BOOTS, Tier.REINFORCED), "signalumpowersuit_boots").setMaxStackSize(1);

		scanAbilityContainer = customItem(() -> new ItemWithAbility("ability.scan", key("item/scan_ability_container"), item("scanAbilityContainer"), new ScanSuitAbility()), "ability4").setMaxStackSize(1);

		crystalWings = (ItemWingsAttachment) customItem(() -> new ItemWingsAttachment("reinforced.attachment.wings", key("item/crystal_wings"), item("crystalWings"), Catalyst.listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED), "wings").setMaxStackSize(1);
		extendedEnergyPack = (ItemAttachment) customItem(() -> new ItemExtendedEnergyPackAttachment("reinforced.attachment.extendedEnergyPack", key("item/extended_energy_pack"), item("extendedEnergyPack"), Catalyst.listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED), "extended_energy_pack").setMaxStackSize(1);
		nightVisionLens = (ItemAttachment) customItem(() -> new ItemNVGAttachment("reinforced.attachment.nightVisionLens", key("item/night_vision_lens"), item("nightVisionLens"), Catalyst.listOf(AttachmentPoint.HEAD_LENS), Tier.REINFORCED), "night_vision_goggles").setMaxStackSize(1);
		pulsarAttachment = (ItemAttachment) customItem(() -> new ItemPulsarAttachment("reinforced.attachment.pulsar", key("item/pulsar_attachment"), item("pulsarAttachment"), Catalyst.listOf(AttachmentPoint.ARM_FRONT), Tier.REINFORCED), "pulsar_attachment").setMaxStackSize(1);
		movementBoosters = (ItemAttachment) customItem(() -> new ItemMovementBoostersAttachment("reinforced.attachment.movementBoosters", key("item/movement_boosters"), item("movementBoosters"), Catalyst.listOf(AttachmentPoint.BOOT_BACK), Tier.REINFORCED), "movement_boosters").setMaxStackSize(2);
		basicBackpack = (ItemAttachment) customItem(() -> new ItemBackpackAttachment("basic.attachment.backpack", key("item/basic_backpack"), item("basicBackpack"), Catalyst.listOf(AttachmentPoint.CORE_BACK), Tier.BASIC), "basic_backpack").setMaxStackSize(1);
		reinforcedBackpack = (ItemAttachment) customItem(() -> new ItemBackpackAttachment("reinforced.attachment.backpack", key("item/reinforced_backpack"), item("reinforcedBackpack"), Catalyst.listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED), "reinforced_backpack").setMaxStackSize(1);

		abilityModule = (ItemAttachment) customItem(() -> new ItemAbilityModule("abilityModule", key("item/ability_module"), item("abilityModule"), Catalyst.listOf(AttachmentPoint.CORE_MODULE), Tier.REINFORCED), "ability_module").setMaxStackSize(1);
		awakenedAbilityModule = (ItemAttachment) customItem(() -> new ItemAbilityModule("awakenedAbilityModule", key("item/awakened_ability_module"), item("awakenedAbilityModule"), Catalyst.listOf(AttachmentPoint.CORE_MODULE), Tier.AWAKENED), "awakened_ability_module").setMaxStackSize(1);

		suitColorizerWhite = (ItemSuitColorizer) customItem(() -> new ItemSuitColorizer(
			"reinforced.attachment.colorizer.white",
			key("item/suit_colorizer_white"),
			item("suitColorizerWhite"),
			listOf(AttachmentPoint.COLORIZER), Tier.REINFORCED,
			"/assets/signalindustries/textures/armor/power_suit_white"), "colorizer_white").setMaxStackSize(1);

		suitColorizerBlue = (ItemSuitColorizer) customItem(() -> new ItemSuitColorizer(
			"reinforced.attachment.colorizer.blue",
			key("item/suit_colorizer_blue"),
			item("suitColorizerBlue"),
			listOf(AttachmentPoint.COLORIZER), Tier.REINFORCED,
			"/assets/signalindustries/textures/armor/power_suit_blue"), "colorizer_blue").setMaxStackSize(1);

		suitColorizerPurple = (ItemSuitColorizer) customItem(() -> new ItemSuitColorizer(
			"reinforced.attachment.colorizer.purple",
			key("item/suit_colorizer_purple"),
			item("suitColorizerPurple"),
			listOf(AttachmentPoint.COLORIZER), Tier.REINFORCED,
			"/assets/signalindustries/textures/armor/power_suit_purple"), "colorizer_purple").setMaxStackSize(1);

		suitColorizerTransparent = (ItemSuitColorizer) customItem(() -> new ItemSuitColorizer(
			"reinforced.attachment.colorizer.transparent",
			key("item/suit_colorizer_transparent"),
			item("suitColorizerTransparent"),
			listOf(AttachmentPoint.COLORIZER), Tier.REINFORCED,
			"/assets/signalindustries/textures/armor/power_suit_transparent"), "colorizer_transparent").setMaxStackSize(1);

		suitColorizerInverted = (ItemSuitColorizer) customItem(() -> new ItemSuitColorizer(
			"reinforced.attachment.colorizer.inverted",
			key("item/suit_colorizer_inverted"),
			item("suitColorizerInverted"),
			listOf(AttachmentPoint.COLORIZER), Tier.REINFORCED,
			"/assets/signalindustries/textures/armor/power_suit_inverted"), "colorizer_inverted").setMaxStackSize(1);

		blueprint = customItem(() -> new ItemBlueprint("blueprint", key("item/blueprint"), item("blueprint")), "blueprint").setMaxStackSize(1);
		goldprint = customItem(() -> new ItemGoldprint("goldprint", key("item/goldprint"), item("goldprint")), "goldprint").setMaxStackSize(1);

		krowka = customItem(() -> new ItemFood(
				"krowka",
				key("item/krowka"),
				item("krowka"),
				1,
				1,
				false, 8),
			"krowka"
		);

		dimensionMaker = customItem(() -> new ItemDimensionMaker("dimensionMaker", key("item/dimension_maker"), item("dimensionMaker")), "dimension_maker").setMaxStackSize(1);

		chipsToAbilityMap.put((ItemRomChip) romChipScan, (ItemWithAbility) scanAbilityContainer);

		List<Field> itemFields = Arrays.stream(SIItems.class.getDeclaredFields()).filter((F) -> F.getType() == Item.class).toList();

		int unknownAmount = 0;
		int itemAmount = itemFields.size();
		for (Field field : itemFields) {
			try {
				Item item = (Item) field.get(null);
				if (item == null) {
					unknownAmount++;
					field.set(null, simpleItem(field.getName(), field.getName(), field.getName(), "unknown"));
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		//LOGGER.info("Item progress: {}/{} ({}% complete)",itemAmount-unknownAmount,itemAmount,((float)(itemAmount-unknownAmount)/itemAmount)*100);

		setInitialized(true);
	}

	public Item simpleItem(String configId, String name, String lang, String texture) {
		Item item = new Item(lang, key("item/" + name), item(configId));
		itemTextures.put(item, texture);
		//LOGGER.info("Registering item '" + item.namespaceID.toString() + "' with texture 'signalindustries:item/" + texture + "'.");
		return new ItemBuilder(MOD_ID).setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS))
			.build(item);
	}

	public Item customItem(Supplier<Item> itemSupplier, String texture) {
		Item item = itemSupplier.get();
		itemTextures.put(item, texture);
		//LOGGER.info("Registering item '" + item.namespaceID.toString() + "' with texture 'signalindustries:item/" + texture + "'.");
		return new ItemBuilder(MOD_ID).setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS))
			.build(item);
	}

	public void afterItemInit() {
		init();
		new SIMultiblocks().init();
	}
}
