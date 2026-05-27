package sunsetsatellite.signalindustries;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.Items;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.items.attachments.ItemExtendedEnergyPackAttachment;
import sunsetsatellite.signalindustries.items.attachments.ItemWingsAttachment;
import sunsetsatellite.signalindustries.items.covers.ItemCover;
import sunsetsatellite.signalindustries.items.tools.ItemSignalumCrystal;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryCategory;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;
import turniplabs.halplibe.util.ItemInitEntrypoint;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
	public static Item warpOrb;
	public static /*ItemArmorTiered*/ Item signalumPrototypeHarness;
	public static /*ItemArmorTiered*/ Item signalumPrototypeHarnessGoggles;
	public static Item basicSignalumDrill;
	public static Item reinforcedSignalumDrill;
	public static Item fuelCell;
	public static Item nullTrigger;
	public static Item clearKey;
	public static Item saturatedKey;
	public static Item signalumSaber;
	public static Item pulsar;
	public static /*ItemSignalumPowerSuit*/ Item signalumPowerSuitHelmet;
	public static /*ItemSignalumPowerSuit*/ Item signalumPowerSuitChestplate;
	public static /*ItemSignalumPowerSuit*/ Item signalumPowerSuitLeggings;
	public static /*ItemSignalumPowerSuit*/ Item signalumPowerSuitBoots;
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

	public static /*ItemAttachment*/ Item pulsarAttachment;
	public static ItemAttachment extendedEnergyPack;
	public static ItemWingsAttachment crystalWings;
	public static /*ItemAttachment*/ Item annihilationCrown;
	public static /*ItemAttachment*/ Item basicBackpack;
	public static /*ItemAttachment*/ Item reinforcedBackpack;
	public static /*ItemAttachment*/ Item nightVisionLens;
	public static /*ItemAttachment*/ Item movementBoosters;
	public static /*ItemPortableWorkbench*/ Item portableWorkbench;
	public static /*ItemSmartWatch*/ Item smartWatch;
	public static /*ItemAttachment*/ Item abilityModule;
	public static /*ItemAttachment*/ Item awakenedAbilityModule;

	public static Item romChipProjectile;
	public static Item romChipBoost;
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

	public static /*ItemSuitColorizer*/ Item suitColorizerWhite;
	public static /*ItemSuitColorizer*/ Item suitColorizerBlue;
	public static /*ItemSuitColorizer*/ Item suitColorizerPurple;
	public static /*ItemSuitColorizer*/ Item suitColorizerTransparent;
	public static /*ItemSuitColorizer*/ Item suitColorizerInverted;

	public static Item dimensionMaker;

	@Override
	public void init() {
		if (initialized) return;
		LOGGER.info("Initializing items...");

		rawSignalumCrystal = simpleItem("rawSignalumCrystal", "raw_signalite_crystal", "rawSignalumCrystal", "raw_signalum_crystal");
		signalumCrystal = simpleItem("signalumCrystal", "signalite_crystal", "signalumCrystal", "signalum_crystal");
		signalumCrystalEmpty = simpleItem("signalumCrystalEmpty", "signalite_crystal_empty", "signalumCrystalEmpty", "signalum_crystal_empty");
		awakenedSignalumCrystal = simpleItem("awakenedSignalumCrystal", "awakened_signalite_crystal", "awakenedSignalumCrystal", "awakened_signalum_crystal").setMaxStackSize(1);
		awakenedSignalumFragment = simpleItem("awakenedSignalumFragment", "awakened_signalite_fragment", "awakenedSignalumFragment", "awakened_signalum_fragment");
		coalDust = simpleItem("coalDust", "coal_dust", "coalDust", "coaldust");
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
		reinforcedCrystalAlloyPlate = simpleItem("reinforcedCrystalAlloyPlate", "reinforced_crystal_alloy_plate", "reinforcedCrystalAlloyPlate", "reinforcedcrystalalloyplate");
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

		infiniteSignalumCrystal = customItem(() ->
				new ItemSignalumCrystal(
					"infiniteSignalumCrystal",
					key("item/infinite_signalite_crystal"),
					item("infiniteSignalumCrystal"),
					true
				),
			"infinite_signalum_crystal")
			.setMaxStackSize(1);

		crystalWings = (ItemWingsAttachment) customItem(() -> new ItemWingsAttachment("reinforced.attachment.wings", key("item/crystal_wings"), item("crystalWings"), Catalyst.listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED), "wings").setMaxStackSize(1);
		extendedEnergyPack = (ItemAttachment) customItem(() -> new ItemExtendedEnergyPackAttachment("reinforced.attachment.extendedEnergyPack", key("item/extended_energy_pack"), item("extendedEnergyPack"), Catalyst.listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED), "extended_energy_pack").setMaxStackSize(1);

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

		LOGGER.info("Item progress: {}/{} ({}% complete)",itemAmount-unknownAmount,itemAmount,((float)(itemAmount-unknownAmount)/itemAmount)*100);

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
	}
}
