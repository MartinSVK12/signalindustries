package sunsetsatellite.signalindustries;

import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.material.ToolMaterial;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import sunsetsatellite.signalindustries.covers.*;
import sunsetsatellite.signalindustries.items.*;
import sunsetsatellite.signalindustries.items.applications.ItemPortableWorkbench;
import sunsetsatellite.signalindustries.items.applications.ItemRaziel;
import sunsetsatellite.signalindustries.items.applications.ItemSmartWatch;
import sunsetsatellite.signalindustries.items.applications.ItemTrigger;
import sunsetsatellite.signalindustries.items.attachments.*;
import sunsetsatellite.signalindustries.items.containers.*;
import sunsetsatellite.signalindustries.items.covers.ItemCover;
import sunsetsatellite.signalindustries.items.models.*;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.ItemBuilder;

import static sunsetsatellite.catalyst.Catalyst.listOf;
import static sunsetsatellite.signalindustries.SignalIndustries.*;

public class SIItems extends DataInitializer {
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
    public static Item evilCatalyst;
    public static Item infernalEye;
    public static Item dimensionalShard;
    public static Item warpOrb;
    public static ItemArmorTiered signalumPrototypeHarness;
    public static ItemArmorTiered signalumPrototypeHarnessGoggles;
    public static Item basicSignalumDrill;
    public static Item reinforcedSignalumDrill;
    public static Item fuelCell;
    public static Item nullTrigger;
    public static Item romChipProjectile;
    public static Item romChipBoost;
    public static Item romChipShield;
    public static Item energyCatalyst;
    public static Item signalumSaber;
    public static Item pulsar;
    public static ItemSignalumPowerSuit signalumPowerSuitHelmet;
    public static ItemSignalumPowerSuit signalumPowerSuitChestplate;
    public static ItemSignalumPowerSuit signalumPowerSuitLeggings;
    public static ItemSignalumPowerSuit signalumPowerSuitBoots;
    public static Item pulsarAttachment;
    public static Item extendedEnergyPack;
    public static Item crystalWings;
    public static Item annihilationCrown;
    public static Item basicBackpack;
    public static Item reinforcedBackpack;
    public static Item nightVisionLens;
    public static Item movementBoosters;
    public static ItemPortableWorkbench portableWorkbench;
    public static ItemSmartWatch smartWatch;
    public static Item abilityModule;
    public static Item awakenedAbilityModule;
    public static Item crystalChip;
    public static Item pureCrystalChip;
    public static Item basicEnergyCore;
    public static Item reinforcedEnergyCore;
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
    public static Item configurationTablet;
    public static Item blankAbilityModule;
    public static Item abilityContainerCasing;
    public static Item blankChip;
    public static Item positionMemoryChip;
    public static Item condensedMilkCan;
    public static Item bucketCaramel;
    public static Item caramelPlate;
    public static Item krowka;

    public static ItemCover blankCover;
    public static ItemCover redstoneCover;
    public static ItemCover voidCover;
    public static ItemCover conveyorCover;
    public static ItemCover pumpCover;
    public static ItemCover switchCover;
    public static ItemCover dilithiumLensCover;

    public static Item raziel;

    public void init() {
        if (initialized) return;
        LOGGER.info("Initializing items...");
        rawSignalumCrystal = simpleItem("rawSignalumCrystal", "signalindustries:item/rawsignalumcrystal");
        signalumCrystal = simpleItem("signalumCrystal", "signalindustries:item/signalumcrystal");
        awakenedSignalumCrystal = simpleItem("awakenedSignalumCrystal", "signalindustries:item/awakenedsignalumcrystal").setMaxStackSize(1);
        awakenedSignalumFragment = simpleItem("awakenedSignalumFragment", "signalindustries:item/awakenedsignalumfragment");
        coalDust = simpleItem("coalDust", "signalindustries:item/coaldust");
        ironDust = simpleItem("ironDust", "signalindustries:item/iron_dust");
        goldDust = simpleItem("goldDust", "signalindustries:item/gold_dust");
        netherCoalDust = simpleItem("netherCoalDust", "signalindustries:item/nethercoaldust");
        tinyNetherCoalDust = simpleItem("tinyNetherCoalDust", "signalindustries:item/tiny_nether_coal_dust");
        emptySignalumCrystalDust = simpleItem("emptySignalumCrystalDust", "signalumCrystalDust", "signalindustries:item/emptysignalumdust");
        saturatedSignalumCrystalDust = simpleItem("saturatedSignalumCrystalDust", "signalindustries:item/saturatedsignalumdust");
        awakenedSignalumCrystalDust = simpleItem("awakenedSignalumCrystalDust", "signalindustries:item/awakened_signalum_dust");
        ironPlateHammer = simpleItem("ironPlateHammer", "signalindustries:item/platehammer").setMaxStackSize(1);
        cobblestonePlate = simpleItem("cobblestonePlate", "signalindustries:item/cobblestoneplate");
        stonePlate = simpleItem("stonePlate", "signalindustries:item/stoneplate");
        crystalAlloyPlate = simpleItem("crystalAlloyPlate", "signalindustries:item/crystalalloyplate");
        steelPlate = simpleItem("steelPlate", "signalindustries:item/steelplate");
        reinforcedCrystalAlloyPlate = simpleItem("reinforcedCrystalAlloyPlate", "signalindustries:item/reinforcedcrystalalloyplate");
        saturatedSignalumAlloyPlate = simpleItem("saturatedSignalumAlloyPlate", "signalindustries:item/saturatedsignalumalloyplate");
        dilithiumPlate = simpleItem("dilithiumPlate", "signalindustries:item/dilithiumplate");
        voidAlloyPlate = simpleItem("voidAlloyPlate", "signalindustries:item/void_alloy_plate");
        awakenedAlloyPlate = simpleItem("awakenedAlloyPlate", "signalindustries:item/awakened_alloy_plate");
        crystalAlloyIngot = simpleItem("crystalAlloyIngot", "signalindustries:item/crystalalloy");
        reinforcedCrystalAlloyIngot = simpleItem("reinforcedCrystalAlloyIngot", "signalindustries:item/reinforcedcrystalalloy");
        saturatedSignalumAlloyIngot = simpleItem("saturatedSignalumAlloyIngot", "signalindustries:item/saturatedsignalumalloy");
        voidAlloyIngot = simpleItem("voidAlloyIngot", "signalindustries:item/void_alloy");
        awakenedAlloyIngot = simpleItem("awakenedAlloyIngot", "signalindustries:item/awakened_alloy");
        diamondCuttingGear = simpleItem("diamondCuttingGear", "signalindustries:item/diamondcuttinggear");
        signalumCuttingGear = simpleItem("signalumCuttingGear", "signalindustries:item/signalumcuttinggear");
        realityString = simpleItem("realityString", "signalindustries:item/stringofreality");
        dilithiumShard = simpleItem("dilithiumShard", "signalindustries:item/dilithiumshard");
        monsterShard = simpleItem("monsterShard", "signalindustries:item/monstershard");
        infernalFragment = simpleItem("infernalFragment", "signalindustries:item/infernalfragment");
        evilCatalyst = simpleItem("evilCatalyst", "signalindustries:item/evilcatalyst").setMaxStackSize(4);
        infernalEye = simpleItem("infernalEye", "signalindustries:item/infernaleye").setMaxStackSize(4);
        dimensionalShard = simpleItem("dimensionalShard", "signalindustries:item/dimensionalshard");
        crystalChip = simpleItem("crystalChip", "signalindustries:item/crystal_chip");
        pureCrystalChip = simpleItem("pureCrystalChip", "signalindustries:item/pure_crystal_chip");
        basicEnergyCore = simpleItem("basicEnergyCore", "signalindustries:item/basic_energy_core");
        reinforcedEnergyCore = simpleItem("reinforcedEnergyCore", "signalindustries:item/reinforced_energy_core");
        basicDrillBit = simpleItem("basicDrillBit", "signalindustries:item/basic_drill_bit");
        reinforcedDrillBit = simpleItem("reinforcedDrillBit", "signalindustries:item/reinforced_drill_bit");
        basicDrillCasing = simpleItem("basicDrillCasing", "signalindustries:item/basic_drill_casing");
        reinforcedDrillCasing = simpleItem("reinforcedDrillCasing", "signalindustries:item/reinforced_drill_casing");
        pulsarShell = simpleItem("pulsarShell", "signalindustries:item/pulsar_shell");
        pulsarInnerCore = simpleItem("pulsarInnerCore", "signalindustries:item/pulsar_inner_core");
        pulsarOuterCore = simpleItem("pulsarOuterCore", "signalindustries:item/pulsar_outer_core");
        itemManipulationCircuit = simpleItem("itemManipulationCircuit", "signalindustries:item/item_manipulation_circuit");
        fluidManipulationCircuit = simpleItem("fluidManipulationCircuit", "signalindustries:item/fluid_manipulation_circuit");
        dilithiumControlCore = simpleItem("dilithiumControlCore", "signalindustries:item/dilithium_control_core");
        warpManipulatorCircuit = simpleItem("warpManipulatorCircuit", "signalindustries:item/warp_manipulator_circuit");
        dilithiumChip = simpleItem("dilithiumChip", "signalindustries:item/dilithium_chip");
        dimensionalChip = simpleItem("dimensionalChip", "signalindustries:item/dimensional_chip");
        attachmentPoint = simpleItem("attachmentPoint", "signalindustries:item/attachment_point");
        blankAbilityModule = simpleItem("blankAbilityModule", "signalindustries:item/blank_module");
        abilityContainerCasing = simpleItem("abilityContainerCasing", "signalindustries:item/abilitycontainercasing");
        blankChip = simpleItem("blankChip", "romChip.blank", "signalindustries:item/blank_chip");
        condensedMilkCan = simpleItem("condensedMilkCan","condensedMilkCan","signalindustries:item/condensed_milk_can").setMaxStackSize(1);
        bucketCaramel = simpleItem("bucketCaramel","bucketCaramel","signalindustries:item/bucket_caramel").setMaxStackSize(1).setContainerItem(Item.bucket);
        caramelPlate = simpleItem("caramelPlate","caramelPlate","signalindustries:item/caramel_plate");
        signalumAlloyMesh = simpleItem("signalumAlloyMesh","signalumAlloyMesh","signalindustries:item/signalum_alloy_mesh");
        crystalWingPart = simpleItem("crystalWingPart","crystalWingPart","signalindustries:item/crystal_wing_part").setMaxStackSize(6);
        raziel = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/raziel")
                .setStackSize(1)
                .build(new ItemRaziel("raziel", config.getInt("ItemIDs.raziel"), Tier.BASIC));
        krowka = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/krowka")
                .setStackSize(8)
                .setItemModel((item)->new ItemModelStandard(item, MOD_ID))
                .build(new ItemFood("krowka",config.getInt("ItemIDs.krowka"),1,1,false,8));

        signalumCrystalEmpty = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/signalumcrystalempty")
                .setStackSize(1)
                .setItemModel((item) -> new ItemModelStandard(item, MOD_ID))
                .build(new ItemSignalumCrystal("signalumCrystalEmpty", config.getInt("ItemIDs.signalumCrystalEmpty"),false));
        signalumCrystalBattery = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/signalumcrystalbattery")
                .setStackSize(1)
                .setItemModel((item) -> new ItemModelStandard(item, MOD_ID))
                .build(new ItemSignalumCrystal("signalumCrystal.battery", config.getInt("ItemIDs.signalumCrystalBattery"),false));
        infiniteSignalumCrystal = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/infinitesignalumcrystal")
                .setStackSize(1)
                .setItemModel((item) -> new ItemModelStandard(item, MOD_ID))
                .build(new ItemSignalumCrystal("infiniteSignalumCrystal", config.getInt("ItemIDs.infiniteSignalumCrystal"),true));
        volatileSignalumCrystal = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/volatile_signalum_crystal")
                .setStackSize(4)
                .setItemModel((item) -> new ItemModelStandard(item, MOD_ID))
                .build(new ItemSignalumCrystalVolatile("volatileSignalumCrystal", config.getInt("ItemIDs.volatileSignalumCrystal")));
        warpOrb = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/warporb")
                .setStackSize(1)
                .setItemModel((item) -> new ItemModelStandard(item, MOD_ID))
                .build(new ItemWarpOrb("warpOrb", config.getInt("ItemIDs.warpOrb")));
        signalumPrototypeHarness = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/harness")
                .build(new ItemSignalumPrototypeHarness("basic.prototypeHarness", config.getInt("ItemIDs.signalumPrototypeHarness"), armorPrototypeHarness, 1, Tier.BASIC));
        signalumPrototypeHarnessGoggles = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/goggles")
                .build(new ItemSignalumPrototypeHarness("basic.prototypeHarnessGoggles", config.getInt("ItemIDs.signalumPrototypeHarnessGoggles"), armorPrototypeHarness, 0, Tier.BASIC));
        basicSignalumDrill = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/signalum_drill")
                .build(new ItemSignalumDrill("basic.signalumDrill", config.getInt("ItemIDs.basicSignalumDrill"), Tier.BASIC, toolMaterialBasic));
        reinforcedSignalumDrill = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/signalum_drill_reinforced")
                .build(new ItemSignalumDrill("reinforced.signalumDrill", config.getInt("ItemIDs.reinforcedSignalumDrill"), Tier.REINFORCED, toolMaterialReinforced));
        fuelCell = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/fuelcellempty")
                .setStackSize(1)
                .setItemModel((item) -> new ItemModelFuelCell(item, MOD_ID))
                .build(new ItemFuelCell("fuelCell", config.getInt("ItemIDs.fuelCell")));
        nullTrigger = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/trigger")
                .setStackSize(1)
                .setItemModel((item) -> new ItemModelTrigger(item, MOD_ID))
                .build(new ItemTrigger("trigger.null", config.getInt("ItemIDs.nullTrigger")));
        romChipProjectile = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/chip1")
                .build(new ItemRomChip("romChip.projectile", config.getInt("ItemIDs.romChipProjectile")));
        romChipBoost = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/chip2")
                .build(new ItemRomChip("romChip.boost", config.getInt("ItemIDs.romChipBoost")));
        romChipShield = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/chip3")
                .build(new ItemRomChip("romChip.shield", config.getInt("ItemIDs.romChipShield")));
        energyCatalyst = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/energycatalyst")
                .build(new Item("energyCatalyst", config.getInt("ItemIDs.energyCatalyst")));
        signalumSaber = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/signalumsaberunpowered")
                .setItemModel((item) -> new ItemModelSaber(item, MOD_ID))
                .build(new ItemSignalumSaber("reinforced.signalumSaber", config.getInt("ItemIDs.signalumSaber"), Tier.REINFORCED, ToolMaterial.stone));
        pulsar = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/pulsaractive")
                .setStackSize(1)
                .setItemModel((item) -> new ItemModelPulsar(item, MOD_ID))
                .build(new ItemPulsar("reinforced.pulsar", config.getInt("ItemIDs.pulsar"), Tier.REINFORCED));
        signalumPowerSuitHelmet = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/signalumpowersuit_helmet")
                .build(new ItemSignalumPowerSuit("reinforced.signalumpowersuit.helmet", config.getInt("ItemIDs.signalumPowerSuitHelmet"), armorSignalumPowerSuit, 0, Tier.REINFORCED));
        signalumPowerSuitChestplate = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/signalumpowersuit_chestplate")
                .build(new ItemSignalumPowerSuit("reinforced.signalumpowersuit.chestplate", config.getInt("ItemIDs.signalumPowerSuitChestplate"), armorSignalumPowerSuit, 1, Tier.REINFORCED));
        signalumPowerSuitLeggings = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/signalumpowersuit_leggings")
                .build(new ItemSignalumPowerSuit("reinforced.signalumpowersuit.leggings", config.getInt("ItemIDs.signalumPowerSuitLeggings"), armorSignalumPowerSuit, 2, Tier.REINFORCED));
        signalumPowerSuitBoots = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/signalumpowersuit_boots")
                .build(new ItemSignalumPowerSuit("reinforced.signalumpowersuit.boots", config.getInt("ItemIDs.signalumPowerSuitBoots"), armorSignalumPowerSuit, 3, Tier.REINFORCED));
        pulsarAttachment = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/pulsar_attachment")
                .build(new ItemPulsarAttachment("reinforced.attachment.pulsar", config.getInt("ItemIDs.pulsarAttachment"), listOf(AttachmentPoint.ARM_FRONT), Tier.REINFORCED));
        extendedEnergyPack = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/extended_energy_pack")
                .build(new ItemTieredAttachment("reinforced.attachment.extendedEnergyPack", config.getInt("ItemIDs.extendedEnergyPack"), listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED));
        annihilationCrown = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/crown")
                .build(new ItemCrownAttachment("reinforced.attachment.crown", config.getInt("ItemIDs.annihilationCrown"), listOf(AttachmentPoint.HEAD_TOP), Tier.REINFORCED));
        crystalWings = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/wings")
                .build(new ItemWingsAttachment("reinforced.attachment.wings", config.getInt("ItemIDs.crystalWings"), listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED));
        basicBackpack = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/basic_backpack")
                .build(new ItemBackpackAttachment("basic.attachment.backpack", config.getInt("ItemIDs.basicBackpack"), listOf(AttachmentPoint.CORE_BACK), Tier.BASIC));
        reinforcedBackpack = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/reinforced_backpack")
                .build(new ItemBackpackAttachment("reinforced.attachment.backpack", config.getInt("ItemIDs.reinforcedBackpack"), listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED));
        nightVisionLens = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/night_vision_goggles")
                .build(new ItemNVGAttachment("reinforced.attachment.nightVisionLens", config.getInt("ItemIDs.nightVisionLens"), listOf(AttachmentPoint.HEAD_LENS), Tier.REINFORCED));
        movementBoosters = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/movement_boosters")
                .build(new ItemMovementBoostersAttachment("reinforced.attachment.movementBoosters", config.getInt("ItemIDs.movementBoosters"), listOf(AttachmentPoint.BOOT_BACK), Tier.REINFORCED));
        portableWorkbench = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/portable_workbench")
                .setStackSize(1)
                .build(new ItemPortableWorkbench("basic.portableWorkbench", config.getInt("ItemIDs.portableWorkbench"), Tier.BASIC));
        smartWatch = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/smartwatch")
                .setStackSize(1)
                .build(new ItemSmartWatch("basic.smartWatch", config.getInt("ItemIDs.smartWatch"), Tier.BASIC));
        abilityModule = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/abilitymodule")
                .build(new ItemAbilityModule("abilityModule", config.getInt("ItemIDs.abilityModule"), Tier.REINFORCED));
        awakenedAbilityModule = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/awakenedmodule")
                .build(new ItemAbilityModule("awakenedAbilityModule", config.getInt("ItemIDs.awakenedAbilityModule"), Tier.AWAKENED));
        meteorTracker = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/meteor_tracker_uncalibrated")
                .setItemModel((item -> new ItemModelMeteorTracker(item,MOD_ID)))
                .build(new ItemMeteorTracker("meteorTracker", config.getInt("ItemIDs.meteorTracker")));
        configurationTablet = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/configuration_tablet_rotation")
                .setItemModel((item)->new ItemModelConfigurationTablet(item,MOD_ID))
                .build(new ItemConfigurationTablet("configurationTablet.rotation", config.getInt("ItemIDs.configurationTablet")).setMaxStackSize(1));
        positionMemoryChip = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/position_chip")
                .build(new ItemPositionChip("romChip.position", config.getInt("ItemIDs.positionMemoryChip")));

        blankCover = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/blank_cover")
                .build(new ItemCover("cover.blank",config.getInt("ItemIDs.blankCover"), BlankCover::new));

        redstoneCover = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/redstone_cover")
                .build(new ItemCover("cover.redstone",config.getInt("ItemIDs.redstoneCover"), RedstoneCover::new));

        voidCover = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/void_cover")
                .build(new ItemCover("cover.void",config.getInt("ItemIDs.voidCover"), VoidCover::new));

        conveyorCover = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/conveyor_cover")
                .build(new ItemCover("cover.item", config.getInt("ItemIDs.conveyorCover"), ConveyorCover::new));

        pumpCover = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/pump_cover")
                .build(new ItemCover("cover.fluid", config.getInt("ItemIDs.pumpCover"), PumpCover::new));

        switchCover = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/switch_cover")
                .build(new ItemCover("cover.switch", config.getInt("ItemIDs.switchCover"), SwitchCover::new));

        dilithiumLensCover = new ItemBuilder(MOD_ID)
                .setIcon("signalindustries:item/dilithium_lens")
                .build(new ItemCover("cover.dilithiumLens", config.getInt("ItemIDs.dilithiumLensCover"), DilithiumLensCover::new));

        ironPlateHammer.setContainerItem(ironPlateHammer);

        setInitialized(true);
    }

    public static Item simpleItem(String name, String texture) {
        return new ItemBuilder(MOD_ID).setIcon(texture).build(new Item(name, config.getInt("ItemIDs." + name)));
    }

    public static Item simpleItem(String name, String lang, String texture) {
        return new ItemBuilder(MOD_ID).setIcon(texture).build(new Item(lang, config.getInt("ItemIDs." + name)));
    }
}