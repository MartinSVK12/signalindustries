package sunsetsatellite.signalindustries;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.material.ArmorMaterial;
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
import sunsetsatellite.signalindustries.dim.WeatherBloodMoon;
import sunsetsatellite.signalindustries.dim.WeatherEclipse;
import sunsetsatellite.signalindustries.dim.WeatherSolarApocalypse;
import sunsetsatellite.signalindustries.entities.EntityCrystal;
import sunsetsatellite.signalindustries.gui.*;
import sunsetsatellite.signalindustries.interfaces.mixins.IEntityPlayerMP;
import sunsetsatellite.signalindustries.items.*;
import sunsetsatellite.signalindustries.mp.packets.PacketOpenMachineGUI;
import sunsetsatellite.signalindustries.mp.packets.PacketPipeItemSpawn;
import sunsetsatellite.signalindustries.tiles.*;
import sunsetsatellite.signalindustries.util.Config;
import sunsetsatellite.sunsetutils.util.NBTEditCommand;
import sunsetsatellite.signalindustries.util.RenderFluidInConduit;
import sunsetsatellite.signalindustries.util.Tiers;
import turniplabs.halplibe.helper.*;

import java.util.ArrayList;
import java.util.HashMap;


public class SignalIndustries implements ModInitializer {

    private static int availableBlockId = 1200;
    private static int availableItemId = 600;

    public static final String MOD_ID = "signalindustries";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static HashMap<String, ArrayList<Class<?>>> nameToGuiMap = new HashMap<>();
    public static final Block signalumOre = BlockHelper.createBlock(MOD_ID,new BlockOreSignalum(Config.getFromConfig("signalumOre",availableBlockId++)),"signalumOre","signalumore.png",Block.soundStoneFootstep,1.0f,15.0f,1);

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

    public static final Block basicInfuser = BlockHelper.createBlock(MOD_ID,new BlockInfuser(Config.getFromConfig("basicInfuser",availableBlockId++),Tiers.BASIC,Material.iron),"basic.infuser","basicblank.png","infuserbasicsideinactive.png",Block.soundMetalFootstep,2,3,0);
    public static final int[][] infuserTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"infuserbasicsideinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"infuserbasicsideactive.png")};

    public static final Block basicWrathBeacon = BlockHelper.createBlock(MOD_ID,new BlockTiered(Config.getFromConfig("basicWrathBeacon",availableBlockId++),Tiers.BASIC,Material.iron),"basic.wrathBeacon","basicblank.png","wrathbeaconactive.png",Block.soundMetalFootstep,25f,500f,1);
    public static final Block reinforcedWrathBeacon = BlockHelper.createBlock(MOD_ID,new BlockTiered(Config.getFromConfig("reinforcedWrathBeacon",availableBlockId++),Tiers.REINFORCED,Material.iron),"reinforced.wrathBeacon","reinforcedblank.png","reinforcedwrathbeaconactive.png",Block.soundMetalFootstep,25f,500f,1);
    public static final Block awakenedWrathBeacon = BlockHelper.createBlock(MOD_ID,new BlockTiered(Config.getFromConfig("awakenedWrathBeacon",availableBlockId++),Tiers.AWAKENED,Material.iron),"awakened.wrathBeacon","reinforcedblank.png","awakenedwrathbeaconactive.png",Block.soundMetalFootstep,25f,500f,1);

    //this has to be after any other block
    public static final int[] energyTex = TextureHelper.registerBlockTexture(MOD_ID,"signalumenergy.png"); //registerFluidTexture(MOD_ID,"signalumenergy.png",0,4);
    public static final Block energyFlowing = BlockHelper.createBlock(MOD_ID,new BlockFluidFlowing(Config.getFromConfig("signalumEnergy",availableBlockId++),Material.water),"signalumEnergy","signalumenergy.png",Block.soundPowderFootstep,1.0f,1.0f,0).setNotInCreativeMenu().setPlaceOverwrites().setTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]);
    public static final Block energyStill = BlockHelper.createBlock(MOD_ID,new BlockFluidStill(Config.getFromConfig("signalumEnergy",availableBlockId++),Material.water),"signalumEnergy","signalumenergy.png",Block.soundPowderFootstep,1.0f,1.0f,0).setNotInCreativeMenu().setPlaceOverwrites().setPlaceOverwrites().setTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]);


    public static final Item signalumCrystalEmpty = ItemHelper.createItem(MOD_ID,new ItemSignalumCrystal(Config.getFromConfig("signalumCrystalEmpty",availableItemId++)),"signalumCrystalEmpty","signalumcrystalempty.png").setMaxStackSize(1);
    public static final Item signalumCrystal = ItemHelper.createItem(MOD_ID,new ItemSignalumCrystal(Config.getFromConfig("signalumCrystal",availableItemId++)),"signalumCrystal","signalumcrystal.png").setMaxStackSize(1);
    public static final Item rawSignalumCrystal = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("rawSignalumCrystal",availableItemId++)),"rawSignalumCrystal","rawsignalumcrystal.png");

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

    public static final Item crystalAlloyIngot = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("crystalAlloyIngot",availableItemId++)),"crystalAlloyIngot","crystalalloy.png");
    public static final Item reinforcedCrystalAlloyIngot = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("reinforcedCrystalAlloyIngot",availableItemId++)),"reinforcedCrystalAlloyIngot","reinforcedcrystalalloy.png");
    public static final Item saturatedSignalumAlloyIngot = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("saturatedSignalumAlloyIngot",availableItemId++)),"saturatedSignalumAlloyIngot","saturatedsignalumalloy.png");

    public static final Item diamondCuttingGear = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("diamondCuttingGear",availableItemId++)),"diamondCuttingGear","diamondcuttinggear.png");

    public static final Block portalEternity = BlockHelper.createBlock(MOD_ID,new BlockPortal(availableBlockId++,3,Block.bedrock.blockID,Block.fire.blockID),"eternityPortal","realityfabric.png",Block.soundGlassFootstep,1.0f,1.0f,1);
    public static final Block realityFabric = BlockHelper.createBlock(MOD_ID,new Block(Config.getFromConfig("realityFabric",availableBlockId++),Material.ground),"realityFabric","realityfabric.png",Block.soundStoneFootstep,50f,50000f,0);
    public static final Block rootedFabric = BlockHelper.createBlock(MOD_ID,new Block(Config.getFromConfig("rootedFabric",availableBlockId++),Material.ground),"rootedFabric","rootedfabric.png",Block.soundStoneFootstep,50f,50000f,0);
    public static final Block dimensionalShardOre = BlockHelper.createBlock(MOD_ID,new BlockOreDimensionalShard(Config.getFromConfig("dimensionalShardOre",availableBlockId++)),"dimensionalShardOre","dimensionalshardore.png",Block.soundStoneFootstep,100f,50000f,1);

    public static final Item dimensionalShard = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("dimensionalShard",availableItemId++)),"dimensionalShard","dimensionalshard.png");
    public static final Item warpOrb = ItemHelper.createItem(MOD_ID,new ItemWarpOrb(Config.getFromConfig("warpOrb",availableItemId++)),"warpOrb","warporb.png").setMaxStackSize(1);


    public static final Block eternalTreeLog = BlockHelper.createBlock(MOD_ID,new BlockEternalTreeLog(Config.getFromConfig("eternalTreeLog",availableBlockId++),Material.wood),"eternalTreeLog","eternaltreelogtop.png","eternaltreelog.png",Block.soundWoodFootstep, 75f,50000f,1);

    public static final Block glowingObsidian = BlockHelper.createBlock(MOD_ID,new Block(Config.getFromConfig("glowingObsidian",availableBlockId++),Material.rock),"glowingObsidian","glowingobsidian.png",Block.soundStoneFootstep, 50f,1200f,1.0f/2.0f);

    public static final ArmorMaterial armorPrototypeHarness = ArmorHelper.createArmorMaterial("signalumprototypeharness",1200,10,10,10,10);

    public static final ItemArmorTiered signalumPrototypeHarness = (ItemArmorTiered) ItemHelper.createItem(MOD_ID,new ItemSignalumPrototypeHarness(Config.getFromConfig("prototypeHarness",700),armorPrototypeHarness,1,Tiers.BASIC),"basic.prototypeHarness","harness.png");
    public static final ItemArmorTiered signalumPrototypeHarnessGoggles = (ItemArmorTiered) ItemHelper.createItem(MOD_ID,new ItemSignalumPrototypeHarness(Config.getFromConfig("prototypeHarnessGoggles",701),armorPrototypeHarness,0,Tiers.BASIC),"basic.prototypeHarnessGoggles","goggles.png");

    public static final Item nullTrigger = ItemHelper.createItem(MOD_ID,new ItemTrigger(Config.getFromConfig("nullTrigger",availableItemId++)),"nullTrigger","trigger.png").setMaxStackSize(1);

    public static final Item pulsar = ItemHelper.createItem(MOD_ID,new ItemPulsar(Config.getFromConfig("pulsar",availableItemId++),Tiers.REINFORCED),"pulsar","pulsaractive.png").setMaxStackSize(1);
    public static final int[][] pulsarTex = new int[][]{TextureHelper.registerItemTexture(MOD_ID,"pulsarinactive.png"),TextureHelper.registerItemTexture(MOD_ID,"pulsaractive.png"),TextureHelper.registerItemTexture(MOD_ID,"pulsarcharged.png"),TextureHelper.registerItemTexture(MOD_ID,"pulsarwarpactive.png"),TextureHelper.registerItemTexture(MOD_ID,"pulsarwarpcharged.png")};

    public static final Block itemPipe = BlockHelper.createBlock(MOD_ID,new BlockItemPipe(Config.getFromConfig("itemPipe",availableBlockId++),Material.glass),"itemPipe","itempipe.png",Block.soundGlassFootstep,1.0f,1.0f,0);
    public static final Block itemPipeOpaque = BlockHelper.createBlock(MOD_ID,new BlockItemPipe(Config.getFromConfig("itemPipeOpaque",availableBlockId++),Material.glass),"itemPipeOpaque","itempipeopaque.png",Block.soundGlassFootstep,1.0f,1.0f,0);
    public static final Block inserter = BlockHelper.createBlock(MOD_ID,new BlockInserter(Config.getFromConfig("inserter",availableBlockId++),Material.rock),"inserter","prototypeblank.png","prototypeblank.png","inserterinput.png","prototypeblank.png","inserteroutput.png","prototypeblank.png",Block.soundStoneFootstep,1.0f,1.0f,0);
    public static final Block filter = BlockHelper.createBlock(MOD_ID,new BlockFilter(Config.getFromConfig("filter",availableBlockId++),Material.rock),"filter","filterred.png","filtergreen.png","filterblue.png","filtercyan.png","filtermagenta.png","filteryellow.png",Block.soundStoneFootstep,1.0f,1.0f,0);


    public static Weather weatherBloodMoon = new WeatherBloodMoon(7).setLanguageKey("bloodMoon");
    public static Weather weatherEclipse = new WeatherEclipse(8).setLanguageKey("solarEclipse");
    public static Weather weatherSolarApocalypse = new WeatherSolarApocalypse(9).setLanguageKey("solarApocalypse");
    public static BiomeGenBase biomeEternity; //= createBiome(16, BiomeGenEternity.class);

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


        /*eternityWorld = createWorldType(14,"eternity").setLanguageKey("worldType.eternity").setDefaultWeather(Weather.weatherClear).setWorldProvider(new WorldProviderEternity());
        dimEternity = DimensionHelper.createDimension(3,"eternity",Dimension.overworld,1.0f,portalEternity,eternityWorld,0,256);*/

        CommandHelper.createCommand(new NBTEditCommand());
        EntityHelper.createSpecialTileEntity(TileEntityConduit.class, new RenderFluidInConduit(),"Conduit");
        EntityHelper.createSpecialTileEntity(TileEntityFluidConduit.class, new RenderFluidInConduit(),"Fluid Conduit");
        EntityHelper.createEntity(EntityCrystal.class,new RenderSnowball(signalumCrystal.getIconFromDamage(0)),47,"signalumCrystal");
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

        EntityHelper.createTileEntity(TileEntityFilter.class,"Filter");
        addToNameGuiMap("Filter", GuiFilter.class, TileEntityFilter.class);

        addToNameGuiMap("The Pulsar", GuiPulsar.class, InventoryPulsar.class);

        EntityHelper.createTileEntity(TileEntityRecipeMaker.class,"Recipe Maker");
        EntityHelper.createTileEntity(TileEntityItemPipe.class,"Item Pipe");
        EntityHelper.createTileEntity(TileEntityInserter.class,"Inserter");

        //auto-generated recipe code
        RecipeHelper.Crafting.createRecipe(SignalIndustries.ironPlateHammer, 1, new Object[]{"012","345","678",'1',new ItemStack(Item.ingotIron,1,0),'4',new ItemStack(Item.stick,1,0),'5',new ItemStack(Item.ingotIron,1,0),'6',new ItemStack(Item.stick,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.diamondCuttingGear, 1, new Object[]{"012","345","678",'1',new ItemStack(Item.diamond,1,0),'3',new ItemStack(Item.diamond,1,0),'5',new ItemStack(Item.diamond,1,0),'7',new ItemStack(Item.diamond,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.prototypeMachineCore, 1, new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'1',new ItemStack(SignalIndustries.stonePlate,1,0),'2',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'3',new ItemStack(SignalIndustries.stonePlate,1,0),'4',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'5',new ItemStack(SignalIndustries.stonePlate,1,0),'6',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'7',new ItemStack(SignalIndustries.stonePlate,1,0),'8',new ItemStack(SignalIndustries.cobblestonePlate,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.cobblestonePlate, 1, new Object[]{"012","345","678",'4',new ItemStack(SignalIndustries.ironPlateHammer,1,0),'7',new ItemStack(Block.cobbleStone,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.stonePlate, 1, new Object[]{"012","345","678",'4',new ItemStack(SignalIndustries.ironPlateHammer,1,0),'7',new ItemStack(Block.stone,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.prototypeConduit, 4, new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.stonePlate,1,0),'1',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'2',new ItemStack(SignalIndustries.stonePlate,1,0),'3',new ItemStack(Block.glass,1,0),'4',new ItemStack(Block.glass,1,0),'5',new ItemStack(Block.glass,1,0),'6',new ItemStack(SignalIndustries.stonePlate,1,0),'7',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'8',new ItemStack(SignalIndustries.stonePlate,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.prototypeFluidConduit, 4, new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'1',new ItemStack(SignalIndustries.stonePlate,1,0),'2',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'3',new ItemStack(Block.glass,1,0),'4',new ItemStack(Block.glass,1,0),'5',new ItemStack(Block.glass,1,0),'6',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'7',new ItemStack(SignalIndustries.stonePlate,1,0),'8',new ItemStack(SignalIndustries.cobblestonePlate,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.prototypeFluidTank, 1, new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'1',new ItemStack(SignalIndustries.stonePlate,1,0),'2',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'3',new ItemStack(SignalIndustries.stonePlate,1,0),'4',new ItemStack(Block.glass,1,0),'5',new ItemStack(SignalIndustries.stonePlate,1,0),'6',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'7',new ItemStack(SignalIndustries.stonePlate,1,0),'8',new ItemStack(SignalIndustries.cobblestonePlate,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.prototypeExtractor, 1, new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'1',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'2',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'3',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'4',new ItemStack(SignalIndustries.prototypeMachineCore,1,0),'5',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'6',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'7',new ItemStack(Block.furnaceStoneIdle,1,0),'8',new ItemStack(SignalIndustries.cobblestonePlate,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.prototypeEnergyCell, 1, new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'1',new ItemStack(Block.glass,1,0),'2',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'3',new ItemStack(Block.glass,1,0),'4',new ItemStack(SignalIndustries.prototypeMachineCore,1,0),'5',new ItemStack(Block.glass,1,0),'6',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'7',new ItemStack(Block.glass,1,0),'8',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.prototypeCrusher, 1, new Object[]{"012","345","678",'0',new ItemStack(Item.flint,1,0),'1',new ItemStack(Item.flint,1,0),'2',new ItemStack(Item.flint,1,0),'3',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'4',new ItemStack(SignalIndustries.prototypeMachineCore,1,0),'5',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'7',new ItemStack(SignalIndustries.cobblestonePlate,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.prototypeAlloySmelter, 1, new Object[]{"012","345","678",'1',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'3',new ItemStack(Block.furnaceBlastIdle,1,0),'4',new ItemStack(SignalIndustries.prototypeMachineCore,1,0),'5',new ItemStack(Block.furnaceBlastIdle,1,0),'7',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.prototypePlateFormer, 1, new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'1',new ItemStack(SignalIndustries.ironPlateHammer,1,0),'2',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'3',new ItemStack(SignalIndustries.ironPlateHammer,1,0),'4',new ItemStack(SignalIndustries.prototypeMachineCore,1,0),'5',new ItemStack(SignalIndustries.ironPlateHammer,1,0),'6',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'7',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'8',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.prototypeCrystalCutter, 1, new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'1',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'2',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'3',new ItemStack(SignalIndustries.diamondCuttingGear,1,0),'4',new ItemStack(SignalIndustries.prototypeMachineCore,1,0),'5',new ItemStack(SignalIndustries.diamondCuttingGear,1,0),'6',new ItemStack(SignalIndustries.cobblestonePlate,1,0),'7',new ItemStack(SignalIndustries.rawSignalumCrystal,1,0),'8',new ItemStack(SignalIndustries.cobblestonePlate,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.basicMachineCore, 1, new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.steelPlate,1,0),'1',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'2',new ItemStack(SignalIndustries.steelPlate,1,0),'3',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'4',new ItemStack(SignalIndustries.signalumCrystal,1,0),'5',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'6',new ItemStack(SignalIndustries.steelPlate,1,0),'7',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'8',new ItemStack(SignalIndustries.steelPlate,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.basicConduit, 4, new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'1',new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,1,0),'2',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'3',new ItemStack(Block.glass,1,0),'4',new ItemStack(SignalIndustries.prototypeConduit,1,0),'5',new ItemStack(Block.glass,1,0),'6',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'7',new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,1,0),'8',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0)});
        RecipeHelper.Crafting.createRecipe(SignalIndustries.basicFluidConduit, 4, new Object[]{"012","345","678",'0',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'1',new ItemStack(SignalIndustries.steelPlate,1,0),'2',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'3',new ItemStack(Block.glass,1,0),'4',new ItemStack(SignalIndustries.prototypeFluidConduit,1,0),'5',new ItemStack(Block.glass,1,0),'6',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0),'7',new ItemStack(SignalIndustries.steelPlate,1,0),'8',new ItemStack(SignalIndustries.crystalAlloyPlate,1,0)});
        //auto-generated recipe code



        Config.init();
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

    /*public static WorldType createWorldType(int id, String name){
        WorldType[] extendedList = (WorldType[]) Arrays.copyOf(WorldType.worldTypes, WorldType.worldTypes.length + 1);
        WorldTypeAccessor.setWorldTypes(extendedList);

        return new WorldType(id,name);

    }

    public static BiomeGenBase createBiome(int id, Class<? extends BiomeGenBase> clazz){
        BiomeGenBase[] extendedList = (BiomeGenBase[]) Arrays.copyOf(BiomeGenBase.biomeList, BiomeGenBase.biomeList.length + 1);
        BiomeGenBaseAccessor.setBiomeList(extendedList);
        return new BiomeGenEternity(id);
        //return new WorldType(id,name);
    }*/

}
