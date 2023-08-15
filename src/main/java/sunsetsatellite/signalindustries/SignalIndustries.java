package sunsetsatellite.signalindustries;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.SnowballRenderer;
import net.minecraft.client.render.model.ModelZombie;
import net.minecraft.client.sound.block.BlockSounds;
import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.fx.EntityFX;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.mixin.accessors.PacketAccessor;
import sunsetsatellite.fluidapi.render.RenderFluidInBlock;
import sunsetsatellite.signalindustries.abilities.powersuit.*;
import sunsetsatellite.signalindustries.blocks.*;
import sunsetsatellite.signalindustries.entities.EntityCrystal;
import sunsetsatellite.signalindustries.entities.EntityEnergyOrb;
import sunsetsatellite.signalindustries.entities.mob.EntityInfernal;
import sunsetsatellite.signalindustries.gui.*;
import sunsetsatellite.signalindustries.interfaces.mixins.IEntityPlayerMP;
import sunsetsatellite.signalindustries.inventories.*;
import sunsetsatellite.signalindustries.items.*;
import sunsetsatellite.signalindustries.items.abilities.ItemWithAbility;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.mp.packets.PacketOpenMachineGUI;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Mode;
import sunsetsatellite.signalindustries.util.RenderFluidInConduit;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.signalindustries.weather.WeatherBloodMoon;
import sunsetsatellite.signalindustries.weather.WeatherEclipse;
import sunsetsatellite.signalindustries.weather.WeatherSolarApocalypse;
import sunsetsatellite.sunsetutils.util.Config;
import sunsetsatellite.sunsetutils.util.NBTEditCommand;
import sunsetsatellite.sunsetutils.util.multiblocks.Multiblock;
import sunsetsatellite.sunsetutils.util.multiblocks.RenderMultiblock;
import sunsetsatellite.sunsetutils.util.multiblocks.Structure;
import sunsetsatellite.sunsetutils.util.multiblocks.StructureCommand;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.*;

import java.util.*;

public class SignalIndustries implements ModInitializer {

    private static int availableBlockId = 1200;
    private static int availableItemId = 17100;

    public static final String MOD_ID = "signalindustries";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Config config = new Config(MOD_ID, mapOf(new String[]{"PacketOpenMachineGUI_ID","PacketPipeItemSpawn_ID","GuiID"},new String[]{"113","114","9"}), new Class[]{SignalIndustries.class});

    public static HashMap<String, ArrayList<Class<?>>> nameToGuiMap = new HashMap<>();
    public static final Block signalumOre = BlockHelper.createBlock(MOD_ID,new BlockOreSignalum(key("signalumOre"),config.getFromConfig("signalumOre",availableBlockId++)),"signalumore.png",BlockSounds.STONE,3.0f,25.0f,1);
    public static final Block dilithiumOre = BlockHelper.createBlock(MOD_ID,new BlockOreDilithium(key("dilithiumOre"),config.getFromConfig("dilithiumOre",availableBlockId++)),"dilithiumore.png",BlockSounds.STONE,10.0f,25.0f,1);
    public static final Block dimensionalShardOre = BlockHelper.createBlock(MOD_ID,new BlockOreDimensionalShard(key("dimensionalShardOre"),config.getFromConfig("dimensionalShardOre",availableBlockId++)),"dimensionalshardore.png",BlockSounds.STONE,100f,50000f,1);

    public static final Block prototypeMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(key("prototype.machine"),config.getFromConfig("prototypeMachineCore",availableBlockId++), Tier.PROTOTYPE,Material.stone),"machineprototype.png",BlockSounds.STONE,2.0f,3.0f,0);
    public static final Block basicMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(key("basic.machine"),config.getFromConfig("basicMachineCore",availableBlockId++), Tier.BASIC,Material.stone),"machinebasic.png",BlockSounds.STONE,3.0f,8.0f,1.0f/2.0f);
    public static final Block reinforcedMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(key("reinforced.machine"),config.getFromConfig("reinforcedMachineCore",availableBlockId++), Tier.REINFORCED,Material.stone),"machinereinforced.png",BlockSounds.STONE,4.0f,15.0f,1.0f/1.50f);
    public static final Block awakenedMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(key("awakened.machine"),config.getFromConfig("awakenedMachineCore",availableBlockId++), Tier.AWAKENED,Material.stone),"machineawakened.png",BlockSounds.STONE,5.0f,50.0f,1);

    public static final Block prototypeConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(key("prototype.conduit"),config.getFromConfig("prototypeConduit",availableBlockId++), Tier.PROTOTYPE, Material.glass),"conduitprototype.png",BlockSounds.GLASS,1.0f,1.0f,0);
    public static final Block basicConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(key("basic.conduit"),config.getFromConfig("basicConduit",availableBlockId++), Tier.BASIC,Material.glass),"conduitbasic.png",BlockSounds.GLASS,1.0f,1.0f,0);
    public static final Block reinforcedConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(key("reinforced.conduit"),config.getFromConfig("reinforcedConduit",availableBlockId++), Tier.REINFORCED,Material.glass),"conduitreinforced.png",BlockSounds.GLASS,1.0f,1.0f,0);
    public static final Block awakenedConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(key("awakened.conduit"),config.getFromConfig("awakenedConduit",availableBlockId++), Tier.AWAKENED,Material.glass),"conduitawakened.png",BlockSounds.GLASS,1.0f,1.0f,0);

    public static final Block prototypeFluidConduit = BlockHelper.createBlock(MOD_ID,new BlockFluidConduit(key("prototype.conduit.fluid"),config.getFromConfig("prototypeFluidConduit",availableBlockId++), Tier.PROTOTYPE,Material.glass),"fluidpipeprototype.png",BlockSounds.GLASS,1.0f,1.0f,0);
    public static final Block basicFluidConduit = BlockHelper.createBlock(MOD_ID,new BlockFluidConduit(key("basic.conduit.fluid"),config.getFromConfig("basicFluidConduit",availableBlockId++), Tier.BASIC,Material.glass),"fluidpipebasic.png",BlockSounds.GLASS,1.0f,1.0f,0);
    public static final Block reinforcedFluidConduit = BlockHelper.createBlock(MOD_ID,new BlockFluidConduit(key("reinforced.conduit.fluid"),config.getFromConfig("reinforcedFluidConduit",availableBlockId++), Tier.REINFORCED,Material.glass),"fluidpipereinforced.png",BlockSounds.GLASS,1.0f,1.0f,0);

    public static final Block infiniteEnergyCell = BlockHelper.createBlock(MOD_ID,new BlockEnergyCell(key("infinite.energyCell"),config.getFromConfig("infiniteEnergyCell",availableBlockId++), Tier.INFINITE,Material.glass),"cellprototype.png",BlockSounds.GLASS,-1.0f,1000000.0f,0);
    public static final Block prototypeEnergyCell = BlockHelper.createBlock(MOD_ID,new BlockEnergyCell(key("prototype.energyCell"),config.getFromConfig("prototypeEnergyCell",availableBlockId++), Tier.PROTOTYPE,Material.glass),"cellprototype.png",BlockSounds.GLASS,2.0f,5.0f,0);
    public static final Block basicEnergyCell = BlockHelper.createBlock(MOD_ID,new BlockEnergyCell(key("basic.energyCell"),config.getFromConfig("basicEnergyCell",availableBlockId++), Tier.BASIC,Material.glass),"cellbasic.png",BlockSounds.GLASS,2.0f,5.0f,0);

    public static final Block prototypeFluidTank = BlockHelper.createBlock(MOD_ID,new BlockSIFluidTank(key("prototype.fluidTank"),config.getFromConfig("prototypeFluidTank",availableBlockId++), Tier.PROTOTYPE,Material.glass),"fluidtankprototype.png",BlockSounds.GLASS,2.0f,5.0f,0);

    public static final Block recipeMaker = BlockHelper.createBlock(MOD_ID,new BlockRecipeMaker(key("recipeMaker"),config.getFromConfig("recipeMaker",availableBlockId++),Material.stone),"prototypeconnection.png",BlockSounds.STONE,2.0f,5.0f,0);


    public static final Block prototypeExtractor = BlockHelper.createBlock(MOD_ID,new BlockExtractor(key("prototype.extractor"),config.getFromConfig("prototypeExtractor",availableBlockId++), Tier.PROTOTYPE,Material.stone),"prototypeblank.png","extractorprototypesideempty.png",BlockSounds.STONE,2,3,0);
    public static final Block basicExtractor = BlockHelper.createBlock(MOD_ID,new BlockExtractor(key("basic.extractor"),config.getFromConfig("basicExtractor",availableBlockId++), Tier.BASIC,Material.stone),"basicblank.png","extractorbasicsideempty.png",BlockSounds.STONE,2,3,0);
    public static final int[][] extractorTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"extractorprototypesideempty.png"),TextureHelper.registerBlockTexture(MOD_ID,"extractorprototypesideinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"extractorprototypesideactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"extractorbasicsideempty.png"),TextureHelper.registerBlockTexture(MOD_ID,"extractorbasicsideinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"extractorbasicsideactive.png")};

    public static final Block prototypeCrusher = BlockHelper.createBlock(MOD_ID,new BlockCrusher(key("prototype.crusher"),config.getFromConfig("prototypeCrusher",availableBlockId++), Tier.PROTOTYPE,Material.stone),"crusherprototypetopinactive.png","prototypeblank.png","crusherprototypeside.png","prototypeblank.png","prototypeblank.png","prototypeblank.png",BlockSounds.STONE,2,3,0);
    public static final Block basicCrusher = BlockHelper.createBlock(MOD_ID,new BlockCrusher(key("basic.crusher"),config.getFromConfig("basicCrusher",availableBlockId++), Tier.BASIC,Material.metal),"crusherbasictopinactive.png","basicblank.png","crusherbasicside.png","basicblank.png","basicblank.png","basicblank.png",BlockSounds.METAL,2,3,0);
    public static final int[][] crusherTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"crusherprototypetopinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"crusherprototypetopactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"crusherbasictopinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"crusherbasictopactive.png")};

    public static final Block prototypeAlloySmelter = BlockHelper.createBlock(MOD_ID,new BlockAlloySmelter(key("prototype.alloySmelter"),config.getFromConfig("prototypeAlloySmelter",availableBlockId++), Tier.PROTOTYPE,Material.stone),"prototypeblank.png","prototypeblank.png","alloysmelterprototypeinactive.png","prototypeblank.png","prototypeblank.png","prototypeblank.png",BlockSounds.STONE,2,3,0);
    public static final Block basicAlloySmelter = BlockHelper.createBlock(MOD_ID,new BlockAlloySmelter(key("basic.alloySmelter"),config.getFromConfig("basicAlloySmelter",availableBlockId++), Tier.BASIC,Material.metal),"basicblank.png","basicblank.png","alloysmelterbasicinactive.png","basicblank.png","basicblank.png","basicblank.png",BlockSounds.STONE,2,3,0);
    public static final int[][] alloySmelterTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"alloysmelterprototypeinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"alloysmelterprototypeactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"alloysmelterbasicinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"alloysmelterbasicactive.png")};

    public static final Block prototypePlateFormer = BlockHelper.createBlock(MOD_ID,new BlockPlateFormer(key("prototype.plateFormer"),config.getFromConfig("prototypePlateFormer",availableBlockId++), Tier.PROTOTYPE,Material.stone),"prototypeblank.png","prototypeblank.png","plateformerprototypeinactive.png","prototypeblank.png","prototypeblank.png","prototypeblank.png",BlockSounds.STONE,2,3,0);
    public static final int[][] plateFormerTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"plateformerprototypeinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"plateformerprototypeactive.png")};

    public static final Block prototypeCrystalCutter = BlockHelper.createBlock(MOD_ID,new BlockCrystalCutter(key("prototype.crystalCutter"),config.getFromConfig("prototypeCrystalCutter",availableBlockId++), Tier.PROTOTYPE,Material.stone),"prototypeblank.png","prototypeblank.png","crystalcutterprototypeinactive.png","prototypeblank.png","prototypeblank.png","prototypeblank.png",BlockSounds.STONE,2,3,0);
    public static final int[][] crystalCutterTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"crystalcutterprototypeinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"crystalcutterprototypeactive.png")};

    public static final Block basicCrystalChamber = BlockHelper.createBlock(MOD_ID,new BlockCrystalChamber(key("basic.crystalChamber"),config.getFromConfig("basicCrystalChamber",availableBlockId++), Tier.BASIC,Material.stone),"basicblank.png","basicblank.png","basiccrystalchambersideinactive.png","basicblank.png","basicblank.png","basicblank.png",BlockSounds.STONE,2,3,0);
    public static final int[][] crystalChamberTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"basiccrystalchambersideinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"basiccrystalchambersideactive.png")};


    public static final Block basicInfuser = BlockHelper.createBlock(MOD_ID,new BlockInfuser(key("basic.infuser"),config.getFromConfig("basic.infuser",availableBlockId++), Tier.BASIC,Material.metal),"basicblank.png","infuserbasicsideinactive.png",BlockSounds.METAL,2,3,0);
    public static final int[][] infuserTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"infuserbasicsideinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"infuserbasicsideactive.png")};

    public static final Block basicWrathBeacon = BlockHelper.createBlock(MOD_ID,new BlockWrathBeacon(key("basic.wrathBeacon"),config.getFromConfig("basicWrathBeacon",availableBlockId++), Tier.BASIC,Material.metal),"basicblank.png","wrathbeacon.png",BlockSounds.METAL,10f,500f,1);
    //public static final Block reinforcedWrathBeacon = BlockHelper.createBlock(MOD_ID,new BlockWrathBeacon(""),config.getFromConfig("reinforcedWrathBeacon",availableBlockId++),Tiers.REINFORCED,Material.metal),"reinforced.wrathBeacon","reinforcedblank.png","reinforcedwrathbeaconactive.png",BlockSounds.METAL,25f,500f,1);
    //public static final Block awakenedWrathBeacon = BlockHelper.createBlock(MOD_ID,new BlockWrathBeacon(""),config.getFromConfig("awakenedWrathBeacon",availableBlockId++),Tiers.AWAKENED,Material.metal),"awakened.wrathBeacon","reinforcedblank.png","awakenedwrathbeaconactive.png",BlockSounds.METAL,25f,500f,1);
    public static final int[][] wrathBeaconTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"wrathbeacon.png"),TextureHelper.registerBlockTexture(MOD_ID,"wrathbeaconactive.png")};

    public static final Block dimensionalAnchor = BlockHelper.createBlock(MOD_ID,new BlockDimensionalAnchor(key("reinforced.dimensionalAnchor"),config.getFromConfig("dimensionalAnchor",availableBlockId++), Tier.REINFORCED,Material.metal),"dimensionanchortopinactive.png","reinforcedblank.png","dimensionalanchorinactive.png",BlockSounds.METAL,5f,20f,1);
    public static final int[][] anchorTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"dimensionalanchorinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"dimensionanchortopinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"reinforcedblank.png"),TextureHelper.registerBlockTexture(MOD_ID,"dimensionalanchor.png"),TextureHelper.registerBlockTexture(MOD_ID,"dimensionanchortop.png"),TextureHelper.registerBlockTexture(MOD_ID,"dimensionalanchorbottom.png")};

    public static final Block dilithiumStabilizer = BlockHelper.createBlock(MOD_ID,new BlockDilithiumStabilizer(key("reinforced.dilithiumStabilizer"),config.getFromConfig("dilithiumStabilizer",availableBlockId++), Tier.REINFORCED,Material.metal),"reinforcedblank.png","reinforcedblank.png","dilithiumtopinactive.png","dilithiumstabilizersideinactive.png","dilithiumstabilizersideinactive.png","dilithiumstabilizersideinactive.png",BlockSounds.METAL,5f,20f,1);
    public static final int[][] dilithStabilizerTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"dilithiumtopinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"dilithiumtopactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"dilithiumstabilizersideinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"dilithiumstabilizersideactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"reinforcedblank.png")};

    public static final Block dilithiumBooster = BlockHelper.createBlock(MOD_ID,new BlockDilithiumBooster(key("reinforced.dilithiumBooster"),config.getFromConfig("dilithiumBooster",availableBlockId++), Tier.REINFORCED,Material.metal),"reinforcedblank.png","reinforcedblank.png","dilithiumtopinactive.png","dilithiumboostersideinactive.png","dilithiumboostersideinactive.png","dilithiumboostersideinactive.png",BlockSounds.METAL,5f,20f,1);
    public static final int[][] dilithBoosterTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"dilithiumtopinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"dilithiumtopactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"dilithiumboostersideinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"dilithiumboostersideactive.png")};

    public static final Block prototypePump = BlockHelper.createBlock(MOD_ID,new BlockPump(key("prototype.pump"),config.getFromConfig("prototypePump",availableBlockId++), Tier.PROTOTYPE,Material.stone),"prototypepumptop.png","prototypeblank.png","prototypepumpside.png","prototypepumpside.png","prototypepumpside.png","prototypepumpside.png",BlockSounds.STONE,2,3,0);
    public static final int[][] pumpTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"prototypepumpsideempty.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototypepumpside.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototypepumptopempty.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototypepumptop.png")};

    public static final Block prototypeBlockBreaker = BlockHelper.createBlock(MOD_ID, new BlockBreaker(key("prototype.blockBreaker"),config.getFromConfig("prototypeBlockBreaker",availableBlockId++),Tier.PROTOTYPE,Material.stone), "prototypeblockbreakerside2.png", "prototypeblockbreakerside2.png", "prototypeblockbreaker.png", "prototypeblockbreakerside.png", "prototypeblank.png", "prototypeblockbreakerside.png", BlockSounds.STONE, 2f,3f,0f);
    public static final int[][] breakerTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"prototypeblockbreaker.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototypeblockbreakeractive.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototypeblockbreakerside.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototypeblockbreakersideactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototypeblockbreakerside2.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototypeblockbreakerside2active.png"),TextureHelper.registerBlockTexture(MOD_ID,"inserteroutput.png")};

    //this has to be after any other block
    public static final int[] energyTex = TextureHelper.registerBlockTexture(MOD_ID,"signalumenergy.png"); //registerFluidTexture(MOD_ID,"signalumenergy.png",0,4);
    public static final Block energyFlowing = BlockHelper.createBlock(MOD_ID,new BlockFluidFlowing(key("signalumEnergy"),config.getFromConfig("signalumEnergy",availableBlockId++),Material.water),"signalumenergy.png",BlockSounds.DEFAULT,1.0f,1.0f,0).withTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES);
    public static final Block energyStill = BlockHelper.createBlock(MOD_ID,new BlockFluidStill(key("signalumEnergy"),config.getFromConfig("signalumEnergy",availableBlockId++),Material.water),"signalumenergy.png",BlockSounds.DEFAULT,1.0f,1.0f,0).withTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES);


    public static final Item signalumCrystalEmpty = ItemHelper.createItem(MOD_ID,new ItemSignalumCrystal(config.getFromConfig("signalumCrystalEmpty",availableItemId++)),"signalumCrystalEmpty","signalumcrystalempty.png").setMaxStackSize(1);
    public static final Item signalumCrystal = ItemHelper.createItem(MOD_ID,new ItemSignalumCrystal(config.getFromConfig("signalumCrystal",availableItemId++)),"signalumCrystal","signalumcrystal.png").setMaxStackSize(1);
    public static final Item rawSignalumCrystal = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("rawSignalumCrystal",availableItemId++)),"rawSignalumCrystal","rawsignalumcrystal.png");

    public static final Item awakenedSignalumCrystal = ItemHelper.createItem(MOD_ID, new Item(config.getFromConfig("awakenedSignalumCrystal",availableItemId++)),"awakenedSignalumCrystal","awakenedsignalumcrystal.png").setMaxStackSize(1);
    public static final Item awakenedSignalumFragment = ItemHelper.createItem(MOD_ID, new Item(config.getFromConfig("awakenedSignalumFragment",availableItemId++)),"awakenedSignalumFragment","awakenedsignalumfragment.png");

    public static final Item coalDust = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("coalDust",availableItemId++)),"coalDust","coaldust.png");
    public static final Item netherCoalDust = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("netherCoalDust",availableItemId++)),"netherCoalDust","nethercoaldust.png");
    public static final Item emptySignalumCrystalDust = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("emptySignalumCrystalDust",availableItemId++)),"signalumCrystalDust","emptysignalumdust.png");
    public static final Item saturatedSignalumCrystalDust = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("saturatedSignalumCrystalDust",availableItemId++)),"saturatedSignalumCrystalDust","saturatedsignalumdust.png");

    public static final Item ironPlateHammer = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("ironPlateHammer",availableItemId++)),"ironPlateHammer","platehammer.png");

    public static final Item cobblestonePlate = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("cobblestonePlate",availableItemId++)),"cobblestonePlate","cobblestoneplate.png");
    public static final Item stonePlate = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("stonePlate",availableItemId++)),"stonePlate","stoneplate.png");
    public static final Item crystalAlloyPlate = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("crystalAlloyPlate",availableItemId++)),"crystalAlloyPlate","crystalalloyplate.png");
    public static final Item steelPlate = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("steelPlate",availableItemId++)),"steelPlate","steelplate.png");
    public static final Item reinforcedCrystalAlloyPlate = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("reinforcedCrystalAlloyPlate",availableItemId++)),"reinforcedCrystalAlloyPlate","reinforcedcrystalalloyplate.png");
    public static final Item saturatedSignalumAlloyPlate = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("saturatedSignalumAlloyPlate",availableItemId++)),"saturatedSignalumAlloyPlate","saturatedsignalumalloyplate.png");
    public static final Item dilithiumPlate = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("dilithiumPlate",availableItemId++)),"dilithiumPlate","dilithiumplate.png");


    public static final Item crystalAlloyIngot = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("crystalAlloyIngot",availableItemId++)),"crystalAlloyIngot","crystalalloy.png");
    public static final Item reinforcedCrystalAlloyIngot = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("reinforcedCrystalAlloyIngot",availableItemId++)),"reinforcedCrystalAlloyIngot","reinforcedcrystalalloy.png");
    public static final Item saturatedSignalumAlloyIngot = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("saturatedSignalumAlloyIngot",availableItemId++)),"saturatedSignalumAlloyIngot","saturatedsignalumalloy.png");

    public static final Item diamondCuttingGear = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("diamondCuttingGear",availableItemId++)),"diamondCuttingGear","diamondcuttinggear.png");

    public static final Block portalEternity = BlockHelper.createBlock(MOD_ID,new BlockPortal(key("eternityPortal"),availableBlockId++,3,Block.bedrock.id,Block.fire.id),"realityfabric.png",BlockSounds.GLASS,1.0f,1.0f,1);
    public static final Block realityFabric = BlockHelper.createBlock(MOD_ID,new BlockUndroppable(key("realityFabric"),config.getFromConfig("realityFabric",availableBlockId++),Material.dirt),"realityfabric.png",BlockSounds.STONE,150f,50000f,0);
    public static final Block rootedFabric = BlockHelper.createBlock(MOD_ID,new Block(key("rootedFabric"),config.getFromConfig("rootedFabric",availableBlockId++),Material.dirt),"rootedfabric.png",BlockSounds.STONE,50f,50000f,0);

    public static final Item monsterShard = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("monsterShard",availableItemId++)),"monsterShard","monstershard.png");
    public static final Item infernalFragment = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("infernalFragment",availableItemId++)),"infernalFragment","infernalfragment.png");
    public static final Item evilCatalyst = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("evilCatalyst",availableItemId++)),"evilCatalyst","evilcatalyst.png").setMaxStackSize(4);
    public static final Item dimensionalShard = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("dimensionalShard",availableItemId++)),"dimensionalShard","dimensionalshard.png");
    public static final Item warpOrb = ItemHelper.createItem(MOD_ID,new ItemWarpOrb(config.getFromConfig("warpOrb",availableItemId++)),"warpOrb","warporb.png").setMaxStackSize(1);
    public static final Item realityString = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("realityString",availableItemId++)),"realityString","stringofreality.png");
    public static final Item dilithiumShard = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("dilithiumShard",availableItemId++)),"dilithiumShard","dilithiumshard.png");

    public static final Block eternalTreeLog = BlockHelper.createBlock(MOD_ID,new BlockEternalTreeLog(key("eternalTreeLog"),config.getFromConfig("eternalTreeLog",availableBlockId++),Material.wood),"eternaltreelogtop.png","eternaltreelog.png",BlockSounds.WOOD, 75f,50000f,1);

    public static final Block glowingObsidian = BlockHelper.createBlock(MOD_ID,new Block(key("glowingObsidian"),config.getFromConfig("glowingObsidian",availableBlockId++),Material.stone),"glowingobsidian.png",BlockSounds.STONE, 50f,1200f,1.0f/2.0f);

    public static final ArmorMaterial armorPrototypeHarness = ArmorHelper.createArmorMaterial("signalumprototypeharness",1200,10,10,10,10);
    public static final ArmorMaterial armorSignalumPowerSuit = ArmorHelper.createArmorMaterial("signalumpowersuit",9999,50,50,50,50);

    public static final ItemArmorTiered signalumPrototypeHarness = (ItemArmorTiered) ItemHelper.createItem(MOD_ID,new ItemSignalumPrototypeHarness("basic.prototypeHarness",config.getFromConfig("prototypeHarness",700),armorPrototypeHarness,1, Tier.BASIC),"basic.prototypeHarness","harness.png");
    public static final ItemArmorTiered signalumPrototypeHarnessGoggles = (ItemArmorTiered) ItemHelper.createItem(MOD_ID,new ItemSignalumPrototypeHarness("basic.prototypeHarnessGoggles",config.getFromConfig("prototypeHarnessGoggles",701),armorPrototypeHarness,0, Tier.BASIC),"basic.prototypeHarnessGoggles","goggles.png");

    public static final Item nullTrigger = ItemHelper.createItem(MOD_ID,new ItemTrigger(config.getFromConfig("triggerNull",availableItemId++)),"triggerNull","trigger.png").setMaxStackSize(1);

    public static final Item romChipProjectile = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("romChipProjectile",availableItemId++)),"romChipProjectile","chip1.png");
    public static final Item romChipBoost = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("romChipBoost",availableItemId++)),"romChipBoost","chip2.png");

    public static final Item energyCatalyst = ItemHelper.createItem(MOD_ID,new Item(config.getFromConfig("energyCatalyst",availableItemId++)),"energyCatalyst","energycatalyst.png");

    public static final Item signalumSaber = ItemHelper.createItem(MOD_ID, new ItemSignalumSaber("signalumSaber",config.getFromConfig("signalumSaber",availableItemId++), Tier.REINFORCED, ToolMaterial.stone), "signalumSaber", "signalumsaberunpowered.png");
    public static final int[][] saberTex = new int[][]{TextureHelper.registerItemTexture(MOD_ID,"signalumsaberunpowered.png"),TextureHelper.registerItemTexture(MOD_ID,"signalumsaber.png")};

    public static final Item pulsar = ItemHelper.createItem(MOD_ID,new ItemPulsar(config.getFromConfig("pulsar",availableItemId++), Tier.REINFORCED),"pulsar","pulsaractive.png").setMaxStackSize(1);
    public static final int[][] pulsarTex = new int[][]{TextureHelper.registerItemTexture(MOD_ID,"pulsarinactive.png"),TextureHelper.registerItemTexture(MOD_ID,"pulsaractive.png"),TextureHelper.registerItemTexture(MOD_ID,"pulsarcharged.png"),TextureHelper.registerItemTexture(MOD_ID,"pulsarwarpactive.png"),TextureHelper.registerItemTexture(MOD_ID,"pulsarwarpcharged.png")};

    public static final ItemSignalumPowerSuit signalumPowerSuitHelmet = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.helmet",config.getFromConfig("signalumPowerSuitHelmet",availableItemId++),armorSignalumPowerSuit,0,Tier.REINFORCED),"reinforced.signalumpowersuit.helmet","signalumpowersuit_helmet.png");
    public static final ItemSignalumPowerSuit signalumPowerSuitChestplate = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.chestplate",config.getFromConfig("signalumPowerSuitChestplate",availableItemId++),armorSignalumPowerSuit,1,Tier.REINFORCED),"reinforced.signalumpowersuit.chestplate","signalumpowersuit_chestplate.png");
    public static final ItemSignalumPowerSuit signalumPowerSuitLeggings = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.leggings",config.getFromConfig("signalumPowerSuitChestplate",availableItemId++),armorSignalumPowerSuit,2,Tier.REINFORCED),"reinforced.signalumpowersuit.leggings","signalumpowersuit_leggings.png");
    public static final ItemSignalumPowerSuit signalumPowerSuitBoots = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.boots",config.getFromConfig("signalumPowerSuitChestplate",availableItemId++),armorSignalumPowerSuit,3,Tier.REINFORCED),"reinforced.signalumpowersuit.boots","signalumpowersuit_boots.png");

    public static final Item testingAttachment = ItemHelper.createItem(MOD_ID,new ItemAttachment(config.getFromConfig("testingAttachment",availableItemId++), listOf(AttachmentPoint.ANY)),"attachment.testingAttachment","energyorb.png");

    public static SuitBaseAbility testAbility = new TestingAbility();
    public static SuitBaseEffectAbility testEffectAbility = new TestingEffectAbility();
    public static SuitBaseEffectAbility clockworkAbility = new ClockworkAbility();

    public static final Item testingAbility = ItemHelper.createItem(MOD_ID,new ItemWithAbility(config.getFromConfig("testingAbilityItem",availableItemId++),testEffectAbility),"testingAbilityItem","testingability.png");
    public static final Item clockworkAbilityContainer = ItemHelper.createItem(MOD_ID,new ItemWithAbility(config.getFromConfig("clockworkAbilityContainer",availableItemId++),clockworkAbility),"clockworkAbilityContainer","clockworkability.png");


    public static final Item abilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule(config.getFromConfig("abilityModule",availableItemId++),Mode.NORMAL),"abilityModule","abilitymodule.png");
    /*public static final Item normalAbilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("",config.getFromConfig("normalAbilityModule",availableItemId++),Mode.NORMAL),"normalAbilityModule","normalmodule.png");
    public static final Item attackAbilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("",config.getFromConfig("attackAbilityModule",availableItemId++),Mode.ATTACK),"attackAbilityModule","attackmodule.png");
    public static final Item defenseAbilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("",config.getFromConfig("defenseAbilityModule",availableItemId++),Mode.DEFENSE),"defenseAbilityModule","defensemodule.png");
    public static final Item pursuitAbilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("",config.getFromConfig("pursuitAbilityModule",availableItemId++),Mode.PURSUIT),"pursuitAbilityModule","pursuitmodule.png");*/
    public static final Item awakenedAbilityModuel = ItemHelper.createItem(MOD_ID,new ItemAbilityModule(config.getFromConfig("awakenedAbilityModuel",availableItemId++),Mode.AWAKENED),"awakenedAbilityModule","awakenedmodule.png");

    /*public static final Block itemPipe = BlockHelper.createBlock(MOD_ID,new BlockItemPipe("",config.getFromConfig("itemPipe",availableBlockId++),Material.glass),"itemPipe","itempipe.png",BlockSounds.GLASS,1.0f,1.0f,0);
    public static final Block itemPipeOpaque = BlockHelper.createBlock(MOD_ID,new BlockItemPipe("",config.getFromConfig("itemPipeOpaque",availableBlockId++),Material.glass),"itemPipeOpaque","itempipeopaque.png",BlockSounds.GLASS,1.0f,1.0f,0);
    public static final Block inserter = BlockHelper.createBlock(MOD_ID,new BlockInserter("",config.getFromConfig("inserter",availableBlockId++),Material.stone),"inserter","prototypeblank.png","prototypeblank.png","inserterinput.png","prototypeblank.png","inserteroutput.png","prototypeblank.png",BlockSounds.STONE,1.0f,1.0f,0);
    public static final Block filter = BlockHelper.createBlock(MOD_ID,new BlockFilter("",config.getFromConfig("filter",availableBlockId++),Material.stone),"filter","filterred.png","filtergreen.png","filterblue.png","filtercyan.png","filtermagenta.png","filteryellow.png",BlockSounds.STONE,1.0f,1.0f,0);*/

    public static final int[][] railTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"dilithiumrailunpowered.png"),TextureHelper.registerBlockTexture(MOD_ID,"dilithiumrail.png")};
    public static final Block dilithiumRail = BlockHelper.createBlock(MOD_ID,new BlockDilithiumRail(key("dilithiumRail"),config.getFromConfig("dilithiumRail",availableBlockId++),true),"dilithiumrailunpowered.png", BlockSounds.METAL,1,50f,0);

    public static final int[] energyOrbTex = TextureHelper.registerItemTexture(MOD_ID,"energyorb.png");

    public static Weather weatherBloodMoon = new WeatherBloodMoon(10).setLanguageKey("bloodMoon");
    public static Weather weatherEclipse = new WeatherEclipse(11).setLanguageKey("solarEclipse");
    public static Weather weatherSolarApocalypse = new WeatherSolarApocalypse(12).setLanguageKey("solarApocalypse");
    //public static BiomeGenBase biomeEternity = createBiome(16, BiomeGenEternity.class);

    public static Dimension dimEternity;
    public static WorldType eternityWorld;

    public static Multiblock dimAnchorMultiblock = new Multiblock(MOD_ID,new Class[]{SignalIndustries.class},"dimensionalAnchor","dimensionalAnchor",false);

    @Override
    public void onInitialize() {
        LOGGER.info("Signal Industries initialized.");
    }

    public SignalIndustries(){
        //RecipeFIleLoader.load("/assets/signalindustries/recipes/recipes.txt",mapOf(new String[]{"SignalIndustries"},new String[]{"sunsetsatellite.signalindustries.SignalIndustries"}));
        BlockModelDispatcher.getInstance().addDispatch(dilithiumRail,new BlockModelRenderBlocks(9));
        PacketAccessor.callAddIdClassMapping(config.getFromConfig("PacketOpenMachineGUI_ID",113),true,false, PacketOpenMachineGUI.class);
        //PacketAccessor.callAddIdClassMapping(config.getFromConfig("PacketPipeItemSpawn_ID",114),true,false, PacketPipeItemSpawn.class);
        //PacketAccessor.callAddIdClassMapping(config.getFromConfig("PacketPipeItemPos_ID",115),true,false, PacketPipeItemPos.class);

        ItemToolPickaxe.miningLevels.put(signalumOre,3);
        ItemToolPickaxe.miningLevels.put(rootedFabric,4);
        ItemToolPickaxe.miningLevels.put(dimensionalShardOre,4);
        ItemToolPickaxe.miningLevels.put(dilithiumOre,4);


        //eternityWorld = createWorldType(14,"eternity").setLanguageKey("worldType.eternity").setDefaultWeather(Weather.weatherClear).setWorldProvider(new WorldProviderEternity());
        //dimEternity = DimensionHelper.createDimension(3,"eternity", Dimension.overworld,1.0f,portalEternity,eternityWorld,0,256);

        CommandHelper.createCommand(new NBTEditCommand());
        CommandHelper.createCommand(new StructureCommand("structure","struct"));
        EntityHelper.createSpecialTileEntity(TileEntityConduit.class, new RenderFluidInConduit(),"Conduit");
        EntityHelper.createSpecialTileEntity(TileEntityFluidConduit.class, new RenderFluidInConduit(),"Fluid Conduit");
        EntityHelper.createEntity(EntityCrystal.class,new SnowballRenderer(signalumCrystal.getIconFromDamage(0)),47,"signalumCrystal");
        EntityHelper.createEntity(EntityEnergyOrb.class,new SnowballRenderer(Block.texCoordToIndex(energyOrbTex[0],energyOrbTex[1])),49,"energyOrb");
        //EntityHelper.createEntity(EntityPipeItem.class,new RenderPipeItem(),48,"pipeItem");

        EntityHelper.createSpecialTileEntity(TileEntityEnergyCell.class,new RenderFluidInBlock(),"Energy Cell");
        addToNameGuiMap("Energy Cell", GuiEnergyCell.class, TileEntityEnergyCell.class);

        EntityHelper.createSpecialTileEntity(TileEntitySIFluidTank.class,new RenderFluidInBlock(),"SI Fluid Tank");
        addToNameGuiMap("SI Fluid Tank", GuiSIFluidTank.class, TileEntitySIFluidTank.class);

        EntityHelper.createTileEntity(TileEntityExtractor.class,"Extractor");
        addToNameGuiMap("Extractor", GuiExtractor.class, TileEntityExtractor.class);

        EntityHelper.createTileEntity(TileEntityCrusher.class,"Crusher");
        addToNameGuiMap("Crusher", GuiCrusher.class, TileEntityCrusher.class);

        EntityHelper.createTileEntity(TileEntityAlloySmelter.class,"Alloy Smelter");
        addToNameGuiMap("Alloy Smelter", GuiAlloySmelter.class, TileEntityAlloySmelter.class);

        EntityHelper.createTileEntity(TileEntityPlateFormer.class,"Plate Former");
        addToNameGuiMap("Plate Former", GuiPlateFormer.class, TileEntityPlateFormer.class);

        EntityHelper.createTileEntity(TileEntityCrystalCutter.class,"Crystal Cutter");
        addToNameGuiMap("Crystal Cutter", GuiCrystalCutter.class, TileEntityCrystalCutter.class);

        EntityHelper.createTileEntity(TileEntityInfuser.class,"Infuser");
        addToNameGuiMap("Infuser", GuiInfuser.class, TileEntityInfuser.class);

        EntityHelper.createTileEntity(TileEntityBooster.class,"Dilithium Booster");
        addToNameGuiMap("Dilithium Booster", GuiBooster.class, TileEntityBooster.class);

        EntityHelper.createTileEntity(TileEntityStabilizer.class,"Dilithium Stabilizer");
        addToNameGuiMap("Dilithium Stabilizer", GuiStabilizer.class, TileEntityStabilizer.class);

        EntityHelper.createTileEntity(TileEntityCrystalChamber.class,"Crystal Chamber");
        addToNameGuiMap("Crystal Chamber", GuiCrystalChamber.class, TileEntityCrystalChamber.class);

        EntityHelper.createTileEntity(TileEntityPump.class,"Pump");
        addToNameGuiMap("Pump", GuiPump.class, TileEntityCrystalChamber.class);

        EntityHelper.createSpecialTileEntity(TileEntityDimensionalAnchor.class,new RenderMultiblock(),"Dimensional Anchor");
        addToNameGuiMap("Dimensional Anchor", GuiDimAnchor.class, TileEntityDimensionalAnchor.class);

        //EntityHelper.createTileEntity(TileEntityFilter.class,"Filter");
        //addToNameGuiMap("Filter", GuiFilter.class, TileEntityFilter.class);

        addToNameGuiMap("The Pulsar", GuiPulsar.class, InventoryPulsar.class);
        addToNameGuiMap("Signalum Prototype Harness", GuiHarness.class, InventoryHarness.class);

        EntityHelper.createTileEntity(TileEntityRecipeMaker.class,"Recipe Maker");
        //EntityHelper.createTileEntity(TileEntityItemPipe.class,"Item Pipe");
        //EntityHelper.createTileEntity(TileEntityInserter.class,"Inserter");
        EntityHelper.createTileEntity(TileEntityWrathBeacon.class,"Wrath Beacon");
        EntityHelper.createTileEntity(TileEntityBlockBreaker.class,"Block Breaker");

        Multiblock.multiblocks.put("dimensionalAnchor",dimAnchorMultiblock);
        SignalIndustries.LOGGER.info(String.format("Loaded %d multiblocks..",Multiblock.multiblocks.size()));
        SignalIndustries.LOGGER.info(String.format("Loaded %d internal structures.", Structure.internalStructures.size()));

        EntityHelper.createEntity(EntityInfernal.class,new MobRenderer<EntityInfernal>(new ModelZombie(), 0.5F),config.getFromConfig("infernalId",50),"Infernal");

        //crafting recipes in RecipeHandlerCraftingSI

    }

    public static <K,V> Map<K,V> mapOf(K[] keys, V[] values){
        if(keys.length != values.length){
            throw new IllegalArgumentException("Arrays differ in size!");
        }
        HashMap<K,V> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i],values[i]);
        }
        return map;
    }

    @SafeVarargs
    public static <T> List<T> listOf(T... values){
        return new ArrayList<>(Arrays.asList(values));
    }

    public static void debug(String string, Object... args){
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        String trace = element.getFileName() + ":" + element.getLineNumber();
        LOGGER.info("["+trace+"] "+String.format(string,args));
    }

    public static void error(String string, Object... args){
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        String trace = element.getFileName() + ":" + element.getLineNumber();
        LOGGER.error("["+trace+"] "+String.format(string,args));
    }


    public static void displayGui(EntityPlayer entityplayer, GuiScreen guiScreen, Container container, IInventory tile, int x, int y, int z) {
        if(entityplayer instanceof EntityPlayerMP) {
            ((IEntityPlayerMP)entityplayer).displayGuiScreen_si(guiScreen,container,tile,x,y,z);
        } else {
            Minecraft.getMinecraft(Minecraft.class).displayGuiScreen(guiScreen);
        }
    }

    public static void displayGui(EntityPlayer entityplayer, GuiScreen guiScreen, Container container, IInventory tile, ItemStack stack) {
        if(entityplayer instanceof EntityPlayerMP) {
            ((IEntityPlayerMP)entityplayer).displayItemGuiScreen_si(guiScreen,container,tile,stack);
        } else {
            Minecraft.getMinecraft(Minecraft.class).displayGuiScreen(guiScreen);
        }
    }

    public static void addToNameGuiMap(String name, Class<? extends Gui> guiClass, Class<? extends IInventory> tileEntityClass){
        ArrayList<Class<?>> list = new ArrayList<>();
        list.add(guiClass);
        list.add(tileEntityClass);
        nameToGuiMap.put(name,list);
    }

    public static int getEnergyBurnTime(FluidStack stack) {
        if(stack == null) {
            return 0;
        } else {
            return stack.isFluidEqual(new FluidStack((BlockFluid) SignalIndustries.energyFlowing,1)) ? 100 : 0;
        }
    }

    public static void spawnParticle(EntityFX particle){
        if (Minecraft.getMinecraft(Minecraft.class) == null || Minecraft.getMinecraft(Minecraft.class).thePlayer == null || Minecraft.getMinecraft(Minecraft.class).effectRenderer == null)
            return;
        double d6 = Minecraft.getMinecraft(Minecraft.class).thePlayer.x - particle.x;
        double d7 = Minecraft.getMinecraft(Minecraft.class).thePlayer.y - particle.y;
        double d8 = Minecraft.getMinecraft(Minecraft.class).thePlayer.z - particle.z;
        double d9 = 16.0D;
        if (d6 * d6 + d7 * d7 + d8 * d8 > d9 * d9)
            return;
        Minecraft.getMinecraft(Minecraft.class).effectRenderer.addEffect(particle);
    }

    public static WorldType createWorldType(int id, String name){
        /*WorldType[] extendedList = Arrays.copyOf(WorldType.worldTypes, WorldType.worldTypes.length + 1);
        WorldTypeAccessor.setWorldTypes(extendedList);

        return new WorldType(id,name);*/
        return null;

    }

    /*public static BiomeGenBase createBiome(int id, Class<? extends BiomeGenPublic> clazz){
        BiomeGenBase[] extendedList = Arrays.copyOf(BiomeGenBase.biomeList, BiomeGenBase.biomeList.length + 1);
        Field biomeListField = ReflectUtils.getField(clazz,"biomeList");
        try {
            //lord forgive me for what im about to do
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe) unsafeField.get(null);
            Object staticFieldBase = unsafe.staticFieldBase(biomeListField);
            long staticFieldOffset = unsafe.staticFieldOffset(biomeListField);
            unsafe.putObject(staticFieldBase, staticFieldOffset, extendedList);
            return clazz.getConstructor(int.class).newInstance(id);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }*/
    public static Object/*BiomeGenBase*/ createBiome(int id, Class<? extends Object/*BiomeGenBase*/> clazz){
        /*BiomeGenBase[] extendedList = Arrays.copyOf(BiomeGenBase.biomeList, BiomeGenBase.biomeList.length + 1);
        BiomeGenBaseAccessor.setBiomeList(extendedList);
        try {
            return clazz.getDeclaredConstructor(int.class).newInstance(id);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }*/
        return null;
    }

    public static String key(String key){
        return HalpLibe.addModId(MOD_ID,key);
    }

    public static void usePortal(int dim) {
        /*Dimension lastDim = Dimension.dimensionList[Minecraft.getMinecraft(Minecraft.class).thePlayer.dimension];
        Dimension newDim = Dimension.dimensionList[dim];
        System.out.println("Switching to dimension \"" + newDim.getName() + "\"!!");
        Minecraft.getMinecraft(Minecraft.class).thePlayer.dimension = dim;
        Minecraft.getMinecraft(Minecraft.class).theWorld.setEntityDead(Minecraft.getMinecraft(Minecraft.class).thePlayer);
        Minecraft.getMinecraft(Minecraft.class).thePlayer.isDead = false;
        double d = Minecraft.getMinecraft(Minecraft.class).thePlayer.posX;
        double d1 = Minecraft.getMinecraft(Minecraft.class).thePlayer.posZ;
        double newY = Minecraft.getMinecraft(Minecraft.class).thePlayer.posY;
        d *= Dimension.getCoordScale(lastDim, newDim);
        d1 *= Dimension.getCoordScale(lastDim, newDim);
        Minecraft.getMinecraft(Minecraft.class).thePlayer.setLocationAndAngles(d, newY, d1, Minecraft.getMinecraft(Minecraft.class).thePlayer.rotationYaw, Minecraft.getMinecraft(Minecraft.class).thePlayer.rotationPitch);
        if (Minecraft.getMinecraft(Minecraft.class).thePlayer.isEntityAlive())
            Minecraft.getMinecraft(Minecraft.class).theWorld.updateEntityWithOptionalForce(Minecraft.getMinecraft(Minecraft.class).thePlayer, false);
        World world = null;
        world = new World(Minecraft.getMinecraft(Minecraft.class).theWorld, newDim);
        if (newDim == lastDim.homeDim) {
            Minecraft.getMinecraft(Minecraft.class).changeWorld(world, "Leaving " + lastDim.getName(), Minecraft.getMinecraft(Minecraft.class).thePlayer);
        } else {
            Minecraft.getMinecraft(Minecraft.class).changeWorld(world, "Entering " + newDim.getName(), Minecraft.getMinecraft(Minecraft.class).thePlayer);
        }
        Minecraft.getMinecraft(Minecraft.class).thePlayer.world = Minecraft.getMinecraft(Minecraft.class).theWorld;
        if (Minecraft.getMinecraft(Minecraft.class).thePlayer.isEntityAlive()) {
            Minecraft.getMinecraft(Minecraft.class).thePlayer.setLocationAndAngles(d, newY, d1, Minecraft.getMinecraft(Minecraft.class).thePlayer.rotationYaw, Minecraft.getMinecraft(Minecraft.class).thePlayer.rotationPitch);
            Minecraft.getMinecraft(Minecraft.class).theWorld.updateEntityWithOptionalForce(Minecraft.getMinecraft(Minecraft.class).thePlayer, false);
        }*/
    }

}
