package sunsetsatellite.signalindustries;

import b100.utils.ReflectUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.material.ArmorMaterial;
import net.minecraft.src.material.ToolMaterial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.mixin.accessors.PacketAccessor;
import sunsetsatellite.fluidapi.render.RenderFluidInBlock;
import sunsetsatellite.signalindustries.api.impl.itempipes.blocks.BlockFilter;
import sunsetsatellite.signalindustries.api.impl.itempipes.blocks.BlockInserter;
import sunsetsatellite.signalindustries.api.impl.itempipes.blocks.BlockItemPipe;
import sunsetsatellite.signalindustries.api.impl.itempipes.gui.GuiFilter;
import sunsetsatellite.signalindustries.api.impl.itempipes.misc.EntityPipeItem;
import sunsetsatellite.signalindustries.api.impl.itempipes.misc.RenderPipeItem;
import sunsetsatellite.signalindustries.api.impl.itempipes.tiles.TileEntityFilter;
import sunsetsatellite.signalindustries.api.impl.itempipes.tiles.TileEntityInserter;
import sunsetsatellite.signalindustries.api.impl.itempipes.tiles.TileEntityItemPipe;
import sunsetsatellite.signalindustries.blocks.*;
import sunsetsatellite.signalindustries.dim.*;
import sunsetsatellite.signalindustries.entities.EntityCrystal;
import sunsetsatellite.signalindustries.entities.EntityEnergyOrb;
import sunsetsatellite.signalindustries.gui.*;
import sunsetsatellite.signalindustries.interfaces.mixins.IEntityPlayerMP;
import sunsetsatellite.signalindustries.items.*;
import sunsetsatellite.signalindustries.mixin.accessors.BiomeGenBaseAccessor;
import sunsetsatellite.signalindustries.mixin.accessors.WorldTypeAccessor;
import sunsetsatellite.signalindustries.mp.packets.PacketOpenMachineGUI;
import sunsetsatellite.signalindustries.mp.packets.PacketPipeItemSpawn;
import sunsetsatellite.signalindustries.tiles.*;
import sunsetsatellite.signalindustries.util.*;
import sunsetsatellite.signalindustries.weather.WeatherBloodMoon;
import sunsetsatellite.signalindustries.weather.WeatherEclipse;
import sunsetsatellite.signalindustries.weather.WeatherSolarApocalypse;
import sunsetsatellite.sunsetutils.util.NBTEditCommand;
import turniplabs.halplibe.helper.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;


public class SignalIndustries implements ModInitializer {

    private static int availableBlockId = 1200;
    private static int availableItemId = 600;

    public static final String MOD_ID = "signalindustries";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static HashMap<String, ArrayList<Class<?>>> nameToGuiMap = new HashMap<>();
    public static final Block signalumOre = BlockHelper.createBlock(MOD_ID,new BlockOreSignalum(Config.getFromConfig("signalumOre",availableBlockId++)),"signalumOre","signalumore.png",Block.soundStoneFootstep,3.0f,25.0f,1);
    public static final Block dilithiumOre = BlockHelper.createBlock(MOD_ID,new BlockOreDilithium(Config.getFromConfig("dilithiumOre",availableBlockId++)),"dilithiumOre","dilithiumore.png",Block.soundStoneFootstep,10.0f,25.0f,1);

    public static final Block prototypeMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(Config.getFromConfig("prototypeMachineCore",availableBlockId++), Tiers.PROTOTYPE,Material.rock),"prototype.machine","machineprototype.png",Block.soundStoneFootstep,2.0f,3.0f,0);
    public static final Block basicMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(Config.getFromConfig("basicMachineCore",availableBlockId++),Tiers.BASIC,Material.rock),"basic.machine","machinebasic.png",Block.soundStoneFootstep,3.0f,8.0f,1.0f/2.0f);
    public static final Block reinforcedMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(Config.getFromConfig("reinforcedMachineCore",availableBlockId++),Tiers.REINFORCED,Material.rock),"reinforced.machine","machinereinforced.png",Block.soundStoneFootstep,4.0f,15.0f,1.0f/1.50f);
    public static final Block awakenedMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(Config.getFromConfig("awakenedMachineCore",availableBlockId++),Tiers.AWAKENED,Material.rock),"awakened.machine","machineawakened.png",Block.soundStoneFootstep,5.0f,50.0f,1);

    public static final Block prototypeConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(Config.getFromConfig("prototypeConduit",availableBlockId++),Tiers.PROTOTYPE,Material.glass),"prototype.conduit","conduitprototype.png",Block.soundGlassFootstep,1.0f,1.0f,0);
    public static final Block basicConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(Config.getFromConfig("basicConduit",availableBlockId++),Tiers.BASIC,Material.glass),"basic.conduit","conduitbasic.png",Block.soundGlassFootstep,1.0f,1.0f,0);
    public static final Block reinforcedConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(Config.getFromConfig("reinforcedConduit",availableBlockId++),Tiers.REINFORCED,Material.glass),"reinforced.conduit","conduitreinforced.png",Block.soundGlassFootstep,1.0f,1.0f,0);
    public static final Block awakenedConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(Config.getFromConfig("awakenedConduit",availableBlockId++),Tiers.AWAKENED,Material.glass),"awakened.conduit","conduitawakened.png",Block.soundGlassFootstep,1.0f,1.0f,0);

    public static final Block prototypeFluidConduit = BlockHelper.createBlock(MOD_ID,new BlockFluidConduit(Config.getFromConfig("prototypeFluidConduit",availableBlockId++),Tiers.PROTOTYPE,Material.glass),"prototype.conduit.fluid","fluidpipeprototype.png",Block.soundGlassFootstep,1.0f,1.0f,0);
    public static final Block basicFluidConduit = BlockHelper.createBlock(MOD_ID,new BlockFluidConduit(Config.getFromConfig("basicFluidConduit",availableBlockId++),Tiers.BASIC,Material.glass),"basic.conduit.fluid","fluidpipebasic.png",Block.soundGlassFootstep,1.0f,1.0f,0);
    public static final Block reinforcedFluidConduit = BlockHelper.createBlock(MOD_ID,new BlockFluidConduit(Config.getFromConfig("reinforcedFluidConduit",availableBlockId++),Tiers.REINFORCED,Material.glass),"reinforced.conduit.fluid","fluidpipereinforced.png",Block.soundGlassFootstep,1.0f,1.0f,0);

    public static final Block infiniteEnergyCell = BlockHelper.createBlock(MOD_ID,new BlockEnergyCell(Config.getFromConfig("infiniteEnergyCell",availableBlockId++),Tiers.INFINITE,Material.glass),"infinite.energyCell","cellprototype.png",Block.soundGlassFootstep,-1.0f,1000000.0f,0);
    public static final Block prototypeEnergyCell = BlockHelper.createBlock(MOD_ID,new BlockEnergyCell(Config.getFromConfig("prototypeEnergyCell",availableBlockId++),Tiers.PROTOTYPE,Material.glass),"prototype.energyCell","cellprototype.png",Block.soundGlassFootstep,2.0f,5.0f,0);
    public static final Block basicEnergyCell = BlockHelper.createBlock(MOD_ID,new BlockEnergyCell(Config.getFromConfig("basicEnergyCell",availableBlockId++),Tiers.BASIC,Material.glass),"basic.energyCell","cellbasic.png",Block.soundGlassFootstep,2.0f,5.0f,0);

    public static final Block prototypeFluidTank = BlockHelper.createBlock(MOD_ID,new BlockSIFluidTank(Config.getFromConfig("prototypeFluidTank",availableBlockId++),Tiers.PROTOTYPE,Material.glass),"prototype.fluidTank","fluidtankprototype.png",Block.soundGlassFootstep,2.0f,5.0f,0);

    public static final Block recipeMaker = BlockHelper.createBlock(MOD_ID,new BlockRecipeMaker(Config.getFromConfig("recipeMaker",availableBlockId++),Material.rock),"recipeMaker","prototypeconnection.png",Block.soundStoneFootstep,2.0f,5.0f,0);


    public static final Block prototypeExtractor = BlockHelper.createBlock(MOD_ID,new BlockExtractor(Config.getFromConfig("prototypeExtractor",availableBlockId++),Tiers.PROTOTYPE,Material.rock),"prototype.extractor","prototypeblank.png","extractorprototypesideempty.png",Block.soundStoneFootstep,2,3,0);
    public static final Block basicExtractor = BlockHelper.createBlock(MOD_ID,new BlockExtractor(Config.getFromConfig("basicExtractor",availableBlockId++),Tiers.BASIC,Material.rock),"basic.extractor","basicblank.png","extractorbasicsideempty.png",Block.soundStoneFootstep,2,3,0);
    public static final int[][] extractorTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"extractorprototypesideempty.png"),TextureHelper.registerBlockTexture(MOD_ID,"extractorprototypesideinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"extractorprototypesideactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"extractorbasicsideempty.png"),TextureHelper.registerBlockTexture(MOD_ID,"extractorbasicsideinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"extractorbasicsideactive.png")};

    public static final Block prototypeCrusher = BlockHelper.createBlock(MOD_ID,new BlockCrusher(Config.getFromConfig("prototypeCrusher",availableBlockId++),Tiers.PROTOTYPE,Material.rock),"prototype.crusher","crusherprototypetopinactive.png","prototypeblank.png","crusherprototypeside.png","prototypeblank.png","prototypeblank.png","prototypeblank.png",Block.soundStoneFootstep,2,3,0);
    public static final Block basicCrusher = BlockHelper.createBlock(MOD_ID,new BlockCrusher(Config.getFromConfig("basicCrusher",availableBlockId++),Tiers.BASIC,Material.iron),"basic.crusher","crusherbasictopinactive.png","basicblank.png","crusherbasicside.png","basicblank.png","basicblank.png","basicblank.png",Block.soundMetalFootstep,2,3,0);
    public static final int[][] crusherTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"crusherprototypetopinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"crusherprototypetopactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"crusherbasictopinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"crusherbasictopactive.png")};

    public static final Block prototypeAlloySmelter = BlockHelper.createBlock(MOD_ID,new BlockAlloySmelter(Config.getFromConfig("prototypeAlloySmelter",availableBlockId++),Tiers.PROTOTYPE,Material.rock),"prototype.alloySmelter","prototypeblank.png","prototypeblank.png","alloysmelterprototypeinactive.png","prototypeblank.png","prototypeblank.png","prototypeblank.png",Block.soundStoneFootstep,2,3,0);
    public static final Block basicAlloySmelter = BlockHelper.createBlock(MOD_ID,new BlockAlloySmelter(Config.getFromConfig("basicAlloySmelter",availableBlockId++),Tiers.BASIC,Material.iron),"basic.alloySmelter","basicblank.png","basicblank.png","alloysmelterbasicinactive.png","basicblank.png","basicblank.png","basicblank.png",Block.soundStoneFootstep,2,3,0);
    public static final int[][] alloySmelterTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"alloysmelterprototypeinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"alloysmelterprototypeactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"alloysmelterbasicinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"alloysmelterbasicactive.png")};

    public static final Block prototypePlateFormer = BlockHelper.createBlock(MOD_ID,new BlockPlateFormer(Config.getFromConfig("prototypePlateFormer",availableBlockId++),Tiers.PROTOTYPE,Material.rock),"prototype.plateFormer","prototypeblank.png","prototypeblank.png","plateformerprototypeinactive.png","prototypeblank.png","prototypeblank.png","prototypeblank.png",Block.soundStoneFootstep,2,3,0);
    public static final int[][] plateFormerTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"plateformerprototypeinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"plateformerprototypeactive.png")};

    public static final Block prototypeCrystalCutter = BlockHelper.createBlock(MOD_ID,new BlockCrystalCutter(Config.getFromConfig("prototypeCrystalCutter",availableBlockId++),Tiers.PROTOTYPE,Material.rock),"prototype.crystalCutter","prototypeblank.png","prototypeblank.png","crystalcutterprototypeinactive.png","prototypeblank.png","prototypeblank.png","prototypeblank.png",Block.soundStoneFootstep,2,3,0);
    public static final int[][] crystalCutterTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"crystalcutterprototypeinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"crystalcutterprototypeactive.png")};

    public static final Block basicCrystalChamber = BlockHelper.createBlock(MOD_ID,new BlockCrystalChamber(Config.getFromConfig("basicCrystalChamber",availableBlockId++),Tiers.BASIC,Material.rock),"basic.crystalChamber","basicblank.png","basicblank.png","basiccrystalchambersideinactive.png","basicblank.png","basicblank.png","basicblank.png",Block.soundStoneFootstep,2,3,0);
    public static final int[][] crystalChamberTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"basiccrystalchambersideinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"basiccrystalchambersideactive.png")};


    public static final Block basicInfuser = BlockHelper.createBlock(MOD_ID,new BlockInfuser(Config.getFromConfig("basicInfuser",availableBlockId++),Tiers.BASIC,Material.iron),"basic.infuser","basicblank.png","infuserbasicsideinactive.png",Block.soundMetalFootstep,2,3,0);
    public static final int[][] infuserTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"infuserbasicsideinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"infuserbasicsideactive.png")};

    public static final Block basicWrathBeacon = BlockHelper.createBlock(MOD_ID,new BlockWrathBeacon(Config.getFromConfig("basicWrathBeacon",availableBlockId++),Tiers.BASIC,Material.iron),"basic.wrathBeacon","basicblank.png","wrathbeacon.png",Block.soundMetalFootstep,10f,500f,1);
    //public static final Block reinforcedWrathBeacon = BlockHelper.createBlock(MOD_ID,new BlockWrathBeacon(Config.getFromConfig("reinforcedWrathBeacon",availableBlockId++),Tiers.REINFORCED,Material.iron),"reinforced.wrathBeacon","reinforcedblank.png","reinforcedwrathbeaconactive.png",Block.soundMetalFootstep,25f,500f,1);
    //public static final Block awakenedWrathBeacon = BlockHelper.createBlock(MOD_ID,new BlockWrathBeacon(Config.getFromConfig("awakenedWrathBeacon",availableBlockId++),Tiers.AWAKENED,Material.iron),"awakened.wrathBeacon","reinforcedblank.png","awakenedwrathbeaconactive.png",Block.soundMetalFootstep,25f,500f,1);
    public static final int[][] wrathBeaconTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"wrathbeacon.png"),TextureHelper.registerBlockTexture(MOD_ID,"wrathbeaconactive.png")};

    public static final Block dimensionalAnchor = BlockHelper.createBlock(MOD_ID,new BlockDimensionalAnchor(Config.getFromConfig("dimensionalAnchor",availableBlockId++),Tiers.REINFORCED,Material.iron),"reinforced.dimensionalAnchor","dimensionanchortopinactive.png","reinforcedblank.png","dimensionalanchorinactive.png",Block.soundMetalFootstep,5f,20f,1);
    public static final int[][] anchorTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"dimensionalanchorinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"dimensionanchortopinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"reinforcedblank.png"),TextureHelper.registerBlockTexture(MOD_ID,"dimensionalanchor.png"),TextureHelper.registerBlockTexture(MOD_ID,"dimensionanchortop.png"),TextureHelper.registerBlockTexture(MOD_ID,"dimensionalanchorbottom.png")};

    public static final Block dilithiumStabilizer = BlockHelper.createBlock(MOD_ID,new BlockDilithiumStabilizer(Config.getFromConfig("dilithiumStabilizer",availableBlockId++),Tiers.REINFORCED,Material.iron),"reinforced.dilithiumStabilizer","reinforcedblank.png","reinforcedblank.png","dilithiumtopinactive.png","dilithiumstabilizersideinactive.png","dilithiumstabilizersideinactive.png","dilithiumstabilizersideinactive.png",Block.soundMetalFootstep,5f,20f,1);
    public static final int[][] dilithStabilizerTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"dilithiumtopinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"dilithiumtopactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"dilithiumstabilizersideinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"dilithiumstabilizersideactive.png")};

    public static final Block dilithiumBooster = BlockHelper.createBlock(MOD_ID,new BlockDilithiumBooster(Config.getFromConfig("dilithiumBooster",availableBlockId++),Tiers.REINFORCED,Material.iron),"reinforced.dilithiumBooster","reinforcedblank.png","reinforcedblank.png","dilithiumtopinactive.png","dilithiumboostersideinactive.png","dilithiumboostersideinactive.png","dilithiumboostersideinactive.png",Block.soundMetalFootstep,5f,20f,1);
    public static final int[][] dilithBoosterTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"dilithiumtopinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"dilithiumtopactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"dilithiumboostersideinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"dilithiumboostersideactive.png")};

    public static final Block prototypePump = BlockHelper.createBlock(MOD_ID,new BlockPump(Config.getFromConfig("prototypePump",availableBlockId++),Tiers.PROTOTYPE,Material.rock),"prototype.pump","prototypepumptop.png","prototypeblank.png","prototypepumpside.png","prototypepumpside.png","prototypepumpside.png","prototypepumpside.png",Block.soundStoneFootstep,2,3,0);
    public static final int[][] pumpTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"prototypepumpsideempty.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototypepumpside.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototypepumptopempty.png"),TextureHelper.registerBlockTexture(MOD_ID,"prototypepumptop.png")};


    //this has to be after any other block
    public static final int[] energyTex = TextureHelper.registerBlockTexture(MOD_ID,"signalumenergy.png"); //registerFluidTexture(MOD_ID,"signalumenergy.png",0,4);
    public static final Block energyFlowing = BlockHelper.createBlock(MOD_ID,new BlockFluidFlowing(Config.getFromConfig("signalumEnergy",availableBlockId++),Material.water),"signalumEnergy","signalumenergy.png",Block.soundPowderFootstep,1.0f,1.0f,0).setNotInCreativeMenu().setPlaceOverwrites().setTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]);
    public static final Block energyStill = BlockHelper.createBlock(MOD_ID,new BlockFluidStill(Config.getFromConfig("signalumEnergy",availableBlockId++),Material.water),"signalumEnergy","signalumenergy.png",Block.soundPowderFootstep,1.0f,1.0f,0).setNotInCreativeMenu().setPlaceOverwrites().setPlaceOverwrites().setTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]);


    public static final Item signalumCrystalEmpty = ItemHelper.createItem(MOD_ID,new ItemSignalumCrystal(Config.getFromConfig("signalumCrystalEmpty",availableItemId++)),"signalumCrystalEmpty","signalumcrystalempty.png").setMaxStackSize(1);
    public static final Item signalumCrystal = ItemHelper.createItem(MOD_ID,new ItemSignalumCrystal(Config.getFromConfig("signalumCrystal",availableItemId++)),"signalumCrystal","signalumcrystal.png").setMaxStackSize(1);
    public static final Item rawSignalumCrystal = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("rawSignalumCrystal",availableItemId++)),"rawSignalumCrystal","rawsignalumcrystal.png");

    public static final Item awakenedSignalumCrystal = ItemHelper.createItem(MOD_ID, new Item(Config.getFromConfig("awakenedSignalumCrystal",availableItemId++)),"awakenedSignalumCrystal","awakenedsignalumcrystal.png").setMaxStackSize(1);
    public static final Item awakenedSignalumFragment = ItemHelper.createItem(MOD_ID, new Item(Config.getFromConfig("awakenedSignalumFragment",availableItemId++)),"awakenedSignalumFragment","awakenedsignalumfragment.png");

    public static final Item coalDust = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("coalDust",availableItemId++)),"coalDust","coaldust.png");
    public static final Item netherCoalDust = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("netherCoalDust",availableItemId++)),"netherCoalDust","nethercoaldust.png");
    public static final Item emptySignalumCrystalDust = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("emptySignalumCrystalDust",availableItemId++)),"signalumCrystalDust","emptysignalumdust.png");
    public static final Item saturatedSignalumCrystalDust = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("saturatedSignalumCrystalDust",availableItemId++)),"saturatedSignalumCrystalDust","saturatedsignalumdust.png");

    public static final Item ironPlateHammer = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("ironPlateHammer",availableItemId++)),"ironPlateHammer","platehammer.png");

    public static final Item cobblestonePlate = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("cobblestonePlate",availableItemId++)),"cobblestonePlate","cobblestoneplate.png");
    public static final Item stonePlate = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("stonePlate",availableItemId++)),"stonePlate","stoneplate.png");
    public static final Item crystalAlloyPlate = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("crystalAlloyPlate",availableItemId++)),"crystalAlloyPlate","crystalalloyplate.png");
    public static final Item steelPlate = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("steelPlate",availableItemId++)),"steelPlate","steelplate.png");
    public static final Item reinforcedCrystalAlloyPlate = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("reinforcedCrystalAlloyPlate",availableItemId++)),"reinforcedCrystalAlloyPlate","reinforcedcrystalalloyplate.png");
    public static final Item saturatedSignalumAlloyPlate = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("saturatedSignalumAlloyPlate",availableItemId++)),"saturatedSignalumAlloyPlate","saturatedsignalumalloyplate.png");
    public static final Item dilithiumPlate = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("dilithiumPlate",availableItemId++)),"dilithiumPlate","dilithiumplate.png");


    public static final Item crystalAlloyIngot = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("crystalAlloyIngot",availableItemId++)),"crystalAlloyIngot","crystalalloy.png");
    public static final Item reinforcedCrystalAlloyIngot = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("reinforcedCrystalAlloyIngot",availableItemId++)),"reinforcedCrystalAlloyIngot","reinforcedcrystalalloy.png");
    public static final Item saturatedSignalumAlloyIngot = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("saturatedSignalumAlloyIngot",availableItemId++)),"saturatedSignalumAlloyIngot","saturatedsignalumalloy.png");

    public static final Item diamondCuttingGear = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("diamondCuttingGear",availableItemId++)),"diamondCuttingGear","diamondcuttinggear.png");

    public static final Block portalEternity = BlockHelper.createBlock(MOD_ID,new BlockPortal(availableBlockId++,3,Block.bedrock.blockID,Block.fire.blockID),"eternityPortal","realityfabric.png",Block.soundGlassFootstep,1.0f,1.0f,1);
    public static final Block realityFabric = BlockHelper.createBlock(MOD_ID,new BlockUndroppable(Config.getFromConfig("realityFabric",availableBlockId++),Material.ground),"realityFabric","realityfabric.png",Block.soundStoneFootstep,150f,50000f,0);
    public static final Block rootedFabric = BlockHelper.createBlock(MOD_ID,new Block(Config.getFromConfig("rootedFabric",availableBlockId++),Material.ground),"rootedFabric","rootedfabric.png",Block.soundStoneFootstep,50f,50000f,0);
    public static final Block dimensionalShardOre = BlockHelper.createBlock(MOD_ID,new BlockOreDimensionalShard(Config.getFromConfig("dimensionalShardOre",availableBlockId++)),"dimensionalShardOre","dimensionalshardore.png",Block.soundStoneFootstep,100f,50000f,1);

    public static final Item monsterShard = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("monsterShard",availableItemId++)),"monsterShard","monstershard.png");
    public static final Item evilCatalyst = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("evilCatalyst",availableItemId++)),"evilCatalyst","evilcatalyst.png").setMaxStackSize(4);
    public static final Item dimensionalShard = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("dimensionalShard",availableItemId++)),"dimensionalShard","dimensionalshard.png");
    public static final Item warpOrb = ItemHelper.createItem(MOD_ID,new ItemWarpOrb(Config.getFromConfig("warpOrb",availableItemId++)),"warpOrb","warporb.png").setMaxStackSize(1);
    public static final Item realityString = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("realityString",availableItemId++)),"realityString","stringofreality.png");
    public static final Item dilithiumShard = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("dilithiumShard",availableItemId++)),"dilithiumShard","dilithiumshard.png");

    public static final Block eternalTreeLog = BlockHelper.createBlock(MOD_ID,new BlockEternalTreeLog(Config.getFromConfig("eternalTreeLog",availableBlockId++),Material.wood),"eternalTreeLog","eternaltreelogtop.png","eternaltreelog.png",Block.soundWoodFootstep, 75f,50000f,1);

    public static final Block glowingObsidian = BlockHelper.createBlock(MOD_ID,new Block(Config.getFromConfig("glowingObsidian",availableBlockId++),Material.rock),"glowingObsidian","glowingobsidian.png",Block.soundStoneFootstep, 50f,1200f,1.0f/2.0f);

    public static final ArmorMaterial armorPrototypeHarness = ArmorHelper.createArmorMaterial("signalumprototypeharness",1200,10,10,10,10);

    public static final ItemArmorTiered signalumPrototypeHarness = (ItemArmorTiered) ItemHelper.createItem(MOD_ID,new ItemSignalumPrototypeHarness(Config.getFromConfig("prototypeHarness",700),armorPrototypeHarness,1,Tiers.BASIC),"basic.prototypeHarness","harness.png");
    public static final ItemArmorTiered signalumPrototypeHarnessGoggles = (ItemArmorTiered) ItemHelper.createItem(MOD_ID,new ItemSignalumPrototypeHarness(Config.getFromConfig("prototypeHarnessGoggles",701),armorPrototypeHarness,0,Tiers.BASIC),"basic.prototypeHarnessGoggles","goggles.png");

    public static final Item nullTrigger = ItemHelper.createItem(MOD_ID,new ItemTrigger(Config.getFromConfig("triggerNull",availableItemId++)),"triggerNull","trigger.png").setMaxStackSize(1);

    public static final Item romChipProjectile = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("romChipProjectile",availableItemId++)),"romChipProjectile","chip1.png");
    public static final Item romChipBoost = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("romChipBoost",availableItemId++)),"romChipBoost","chip2.png");

    public static final Item energyCatalyst = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("energyCatalyst",availableItemId++)),"energyCatalyst","energycatalyst.png");

    public static final Item signalumSaber = ItemHelper.createItem(MOD_ID, new ItemSignalumSaber(Config.getFromConfig("signalumSaber",availableItemId++),Tiers.REINFORCED, ToolMaterial.stone), "signalumSaber", "signalumsaberunpowered.png");
    public static final int[][] saberTex = new int[][]{TextureHelper.registerItemTexture(MOD_ID,"signalumsaberunpowered.png"),TextureHelper.registerItemTexture(MOD_ID,"signalumsaber.png")};

    public static final Item pulsar = ItemHelper.createItem(MOD_ID,new ItemPulsar(Config.getFromConfig("pulsar",availableItemId++),Tiers.REINFORCED),"pulsar","pulsaractive.png").setMaxStackSize(1);
    public static final int[][] pulsarTex = new int[][]{TextureHelper.registerItemTexture(MOD_ID,"pulsarinactive.png"),TextureHelper.registerItemTexture(MOD_ID,"pulsaractive.png"),TextureHelper.registerItemTexture(MOD_ID,"pulsarcharged.png"),TextureHelper.registerItemTexture(MOD_ID,"pulsarwarpactive.png"),TextureHelper.registerItemTexture(MOD_ID,"pulsarwarpcharged.png")};

    public static final Block itemPipe = BlockHelper.createBlock(MOD_ID,new BlockItemPipe(Config.getFromConfig("itemPipe",availableBlockId++),Material.glass),"itemPipe","itempipe.png",Block.soundGlassFootstep,1.0f,1.0f,0);
    public static final Block itemPipeOpaque = BlockHelper.createBlock(MOD_ID,new BlockItemPipe(Config.getFromConfig("itemPipeOpaque",availableBlockId++),Material.glass),"itemPipeOpaque","itempipeopaque.png",Block.soundGlassFootstep,1.0f,1.0f,0);
    public static final Block inserter = BlockHelper.createBlock(MOD_ID,new BlockInserter(Config.getFromConfig("inserter",availableBlockId++),Material.rock),"inserter","prototypeblank.png","prototypeblank.png","inserterinput.png","prototypeblank.png","inserteroutput.png","prototypeblank.png",Block.soundStoneFootstep,1.0f,1.0f,0);
    public static final Block filter = BlockHelper.createBlock(MOD_ID,new BlockFilter(Config.getFromConfig("filter",availableBlockId++),Material.rock),"filter","filterred.png","filtergreen.png","filterblue.png","filtercyan.png","filtermagenta.png","filteryellow.png",Block.soundStoneFootstep,1.0f,1.0f,0);

    public static final int[][] railTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"dilithiumrailunpowered.png"),TextureHelper.registerBlockTexture(MOD_ID,"dilithiumrail.png")};
    public static final Block dilithiumRail = BlockHelper.createBlock(MOD_ID,new BlockDilithiumRail(Config.getFromConfig("dilithiumRail",availableBlockId++),true),"dilithiumRail","dilithiumrailunpowered.png",Block.soundMetalFootstep,1,50f,0);

    public static final int[] energyOrbTex = TextureHelper.registerItemTexture(MOD_ID,"energyorb.png");

    public static Weather weatherBloodMoon = new WeatherBloodMoon(7).setLanguageKey("bloodMoon");
    public static Weather weatherEclipse = new WeatherEclipse(8).setLanguageKey("solarEclipse");
    public static Weather weatherSolarApocalypse = new WeatherSolarApocalypse(9).setLanguageKey("solarApocalypse");
    public static BiomeGenBase biomeEternity = createBiome(16, BiomeGenEternity.class);

    public static Dimension dimEternity;
    public static WorldType eternityWorld;

    @Override
    public void onInitialize() {
        LOGGER.info("Signal Industries initialized.");
    }

    public SignalIndustries(){
        PacketAccessor.callAddIdClassMapping(Config.getFromConfig("PacketOpenMachineGUI_ID",113),true,false, PacketOpenMachineGUI.class);
        PacketAccessor.callAddIdClassMapping(Config.getFromConfig("PacketPipeItemSpawn_ID",114),true,false, PacketPipeItemSpawn.class);
        //PacketAccessor.callAddIdClassMapping(Config.getFromConfig("PacketPipeItemPos_ID",115),true,false, PacketPipeItemPos.class);


        eternityWorld = createWorldType(14,"eternity").setLanguageKey("worldType.eternity").setDefaultWeather(Weather.weatherClear).setWorldProvider(new WorldProviderEternity());
        dimEternity = DimensionHelper.createDimension(3,"eternity",Dimension.overworld,1.0f,portalEternity,eternityWorld,0,256);

        CommandHelper.createCommand(new NBTEditCommand());
        CommandHelper.createCommand(new StructureCommand("structure","struct"));
        EntityHelper.createSpecialTileEntity(TileEntityConduit.class, new RenderFluidInConduit(),"Conduit");
        EntityHelper.createSpecialTileEntity(TileEntityFluidConduit.class, new RenderFluidInConduit(),"Fluid Conduit");
        EntityHelper.createEntity(EntityCrystal.class,new RenderSnowball(signalumCrystal.getIconFromDamage(0)),47,"signalumCrystal");
        EntityHelper.createEntity(EntityEnergyOrb.class,new RenderSnowball(Block.texCoordToIndex(energyOrbTex[0],energyOrbTex[1])),49,"energyOrb");
        EntityHelper.createEntity(EntityPipeItem.class,new RenderPipeItem(),48,"pipeItem");

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

        EntityHelper.createTileEntity(TileEntityInfuser.class,"Crystal Cutter");
        addToNameGuiMap("Infuser", GuiInfuser.class, TileEntityInfuser.class);

        EntityHelper.createTileEntity(TileEntityBooster.class,"Dilithium Booster");
        addToNameGuiMap("Dilithium Booster", GuiBooster.class, TileEntityBooster.class);

        EntityHelper.createTileEntity(TileEntityCrystalChamber.class,"Crystal Chamber");
        addToNameGuiMap("Crystal Chamber", GuiCrystalChamber.class, TileEntityCrystalChamber.class);

        EntityHelper.createTileEntity(TileEntityPump.class,"Pump");
        addToNameGuiMap("Pump", GuiPump.class, TileEntityCrystalChamber.class);

        EntityHelper.createSpecialTileEntity(TileEntityDimensionalAnchor.class,new RenderMultiblock(),"Dimensional Anchor");
        //TODO: Add nameGui mapping

        EntityHelper.createTileEntity(TileEntityFilter.class,"Filter");
        addToNameGuiMap("Filter", GuiFilter.class, TileEntityFilter.class);

        addToNameGuiMap("The Pulsar", GuiPulsar.class, InventoryPulsar.class);
        addToNameGuiMap("Signalum Prototype Harness", GuiHarness.class, InventoryHarness.class);

        EntityHelper.createTileEntity(TileEntityRecipeMaker.class,"Recipe Maker");
        EntityHelper.createTileEntity(TileEntityItemPipe.class,"Item Pipe");
        EntityHelper.createTileEntity(TileEntityInserter.class,"Inserter");
        EntityHelper.createTileEntity(TileEntityWrathBeacon.class,"Wrath Beacon");

        //crafting recipes in RecipeHandlerCraftingSI

        Config.init();
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
            Minecraft.getMinecraft().displayGuiScreen(guiScreen);
        }
    }

    public static void displayGui(EntityPlayer entityplayer, GuiScreen guiScreen, Container container, IInventory tile, ItemStack stack) {
        if(entityplayer instanceof EntityPlayerMP) {
            ((IEntityPlayerMP)entityplayer).displayItemGuiScreen_si(guiScreen,container,tile,stack);
        } else {
            Minecraft.getMinecraft().displayGuiScreen(guiScreen);
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
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().renderViewEntity == null || Minecraft.getMinecraft().effectRenderer == null)
            return;
        double d6 = Minecraft.getMinecraft().renderViewEntity.posX - particle.posX;
        double d7 = Minecraft.getMinecraft().renderViewEntity.posY - particle.posY;
        double d8 = Minecraft.getMinecraft().renderViewEntity.posZ - particle.posZ;
        double d9 = 16.0D;
        if (d6 * d6 + d7 * d7 + d8 * d8 > d9 * d9)
            return;
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    public static WorldType createWorldType(int id, String name){
        WorldType[] extendedList = Arrays.copyOf(WorldType.worldTypes, WorldType.worldTypes.length + 1);
        WorldTypeAccessor.setWorldTypes(extendedList);

        return new WorldType(id,name);

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
    public static BiomeGenBase createBiome(int id, Class<? extends BiomeGenBase> clazz){
        BiomeGenBase[] extendedList = Arrays.copyOf(BiomeGenBase.biomeList, BiomeGenBase.biomeList.length + 1);
        BiomeGenBaseAccessor.setBiomeList(extendedList);
        try {
            return clazz.getDeclaredConstructor(int.class).newInstance(id);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void usePortal(int dim) {
        Dimension lastDim = Dimension.dimensionList[Minecraft.getMinecraft().thePlayer.dimension];
        Dimension newDim = Dimension.dimensionList[dim];
        System.out.println("Switching to dimension \"" + newDim.getName() + "\"!!");
        Minecraft.getMinecraft().thePlayer.dimension = dim;
        Minecraft.getMinecraft().theWorld.setEntityDead(Minecraft.getMinecraft().thePlayer);
        Minecraft.getMinecraft().thePlayer.isDead = false;
        double d = Minecraft.getMinecraft().thePlayer.posX;
        double d1 = Minecraft.getMinecraft().thePlayer.posZ;
        double newY = Minecraft.getMinecraft().thePlayer.posY;
        d *= Dimension.getCoordScale(lastDim, newDim);
        d1 *= Dimension.getCoordScale(lastDim, newDim);
        Minecraft.getMinecraft().thePlayer.setLocationAndAngles(d, newY, d1, Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.rotationPitch);
        if (Minecraft.getMinecraft().thePlayer.isEntityAlive())
            Minecraft.getMinecraft().theWorld.updateEntityWithOptionalForce(Minecraft.getMinecraft().thePlayer, false);
        World world = null;
        world = new World(Minecraft.getMinecraft().theWorld, newDim);
        if (newDim == lastDim.homeDim) {
            Minecraft.getMinecraft().changeWorld(world, "Leaving " + lastDim.getName(), Minecraft.getMinecraft().thePlayer);
        } else {
            Minecraft.getMinecraft().changeWorld(world, "Entering " + newDim.getName(), Minecraft.getMinecraft().thePlayer);
        }
        Minecraft.getMinecraft().thePlayer.worldObj = Minecraft.getMinecraft().theWorld;
        if (Minecraft.getMinecraft().thePlayer.isEntityAlive()) {
            Minecraft.getMinecraft().thePlayer.setLocationAndAngles(d, newY, d1, Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.rotationPitch);
            Minecraft.getMinecraft().theWorld.updateEntityWithOptionalForce(Minecraft.getMinecraft().thePlayer, false);
        }
    }

}
