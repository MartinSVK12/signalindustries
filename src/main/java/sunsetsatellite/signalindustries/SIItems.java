package sunsetsatellite.signalindustries;

import net.minecraft.core.item.Item;
import sunsetsatellite.catalyst.core.util.DataInitializer;
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
	public static /*ItemAttachment*/ Item extendedEnergyPack;
	public static /*ItemWingsAttachment*/ Item crystalWings;
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

	public static /*ItemCover*/ Item blankCover;
	public static /*ItemCover*/ Item redstoneCover;
	public static /*ItemCover*/ Item voidCover;
	public static /*ItemCover*/ Item conveyorCover;
	public static /*ItemCover*/ Item pumpCover;
	public static /*ItemCover*/ Item switchCover;
	public static /*ItemCover*/ Item dilithiumLensCover;

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
