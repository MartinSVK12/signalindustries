package sunsetsatellite.signalindustries;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.material.ArmorMaterial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.render.RenderFluidInBlock;
import sunsetsatellite.fluidapi.template.gui.GuiFluidTank;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidTank;
import sunsetsatellite.signalindustries.blocks.*;
import sunsetsatellite.signalindustries.entities.EntityCrystal;
import sunsetsatellite.signalindustries.gui.*;
import sunsetsatellite.signalindustries.interfaces.mixins.IEntityPlayerMP;
import sunsetsatellite.signalindustries.items.ItemArmorTiered;
import sunsetsatellite.signalindustries.items.ItemSignalumPrototypeHarness;
import sunsetsatellite.signalindustries.tiles.*;
import sunsetsatellite.signalindustries.util.Config;
import sunsetsatellite.signalindustries.items.ItemSignalumCrystal;
import sunsetsatellite.signalindustries.util.NBTEditCommand;
import sunsetsatellite.signalindustries.util.RenderFluidInConduit;
import turniplabs.halplibe.helper.*;

import java.util.ArrayList;
import java.util.HashMap;


public class SignalIndustries implements ModInitializer {
    public static final String MOD_ID = "signalindustries";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static HashMap<String, ArrayList<Class<?>>> nameToGuiMap = new HashMap<>();
    public static final Block signalumOre = BlockHelper.createBlock(MOD_ID,new BlockOreSignalum(Config.getFromConfig("signalumOre",1202)),"signalumOre","signalumore.png",Block.soundStoneFootstep,1.0f,15.0f,1);

    public static final Block prototypeMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(Config.getFromConfig("prototypeMachineCore",1207),Tiers.PROTOTYPE,Material.rock),"prototype.machine","machineprototype.png",Block.soundStoneFootstep,2.0f,3.0f,0);
    public static final Block basicMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(Config.getFromConfig("basicMachineCore",1208),Tiers.BASIC,Material.rock),"basic.machine","machinebasic.png",Block.soundStoneFootstep,3.0f,8.0f,1.0f/2.0f);
    public static final Block reinforcedMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(Config.getFromConfig("reinforcedMachineCore",1209),Tiers.REINFORCED,Material.rock),"reinforced.machine","machinereinforced.png",Block.soundStoneFootstep,4.0f,15.0f,1.0f/1.50f);
    public static final Block awakenedMachineCore = BlockHelper.createBlock(MOD_ID,new BlockTiered(Config.getFromConfig("awakenedMachineCore",1210),Tiers.AWAKENED,Material.rock),"awakened.machine","machineawakened.png",Block.soundStoneFootstep,5.0f,50.0f,1);

    public static final Block prototypeConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(Config.getFromConfig("prototypeConduit",1203),Tiers.PROTOTYPE,Material.glass),"prototype.conduit","conduitprototype.png",Block.soundGlassFootstep,1.0f,1.0f,0);
    public static final Block basicConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(Config.getFromConfig("basicConduit",1204),Tiers.BASIC,Material.glass),"basic.conduit","conduitbasic.png",Block.soundGlassFootstep,1.0f,1.0f,0);
    public static final Block reinforcedConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(Config.getFromConfig("reinforcedConduit",1205),Tiers.REINFORCED,Material.glass),"reinforced.conduit","conduitreinforced.png",Block.soundGlassFootstep,1.0f,1.0f,0);
    public static final Block awakenedConduit = BlockHelper.createBlock(MOD_ID,new BlockConduit(Config.getFromConfig("awakenedConduit",1206),Tiers.AWAKENED,Material.glass),"awakened.conduit","conduitawakened.png",Block.soundGlassFootstep,1.0f,1.0f,0);

    public static final Block prototypeFluidConduit = BlockHelper.createBlock(MOD_ID,new BlockFluidConduit(Config.getFromConfig("prototypeFluidConduit",1212),Tiers.PROTOTYPE,Material.glass),"prototype.conduit.fluid","fluidpipeprototype.png",Block.soundGlassFootstep,1.0f,1.0f,0);
    public static final Block basicFluidConduit = BlockHelper.createBlock(MOD_ID,new BlockFluidConduit(Config.getFromConfig("basicFluidConduit",1213),Tiers.BASIC,Material.glass),"basic.conduit.fluid","fluidpipebasic.png",Block.soundGlassFootstep,1.0f,1.0f,0);
    public static final Block reinforcedFluidConduit = BlockHelper.createBlock(MOD_ID,new BlockFluidConduit(Config.getFromConfig("reinforcedFluidConduit",1214),Tiers.REINFORCED,Material.glass),"reinforced.conduit.fluid","fluidpipereinforced.png",Block.soundGlassFootstep,1.0f,1.0f,0);

    public static final Block prototypeEnergyCell = BlockHelper.createBlock(MOD_ID,new BlockEnergyCell(Config.getFromConfig("prototypeEnergyCell",1211),Tiers.PROTOTYPE,Material.glass),"prototype.energyCell","cellprototype.png",Block.soundGlassFootstep,2.0f,5.0f,0);

    public static final Block prototypeFluidTank = BlockHelper.createBlock(MOD_ID,new BlockSIFluidTank(Config.getFromConfig("prototypeFluidTank",1215),Tiers.PROTOTYPE,Material.glass),"prototype.fluidTank","fluidtankprototype.png",Block.soundGlassFootstep,2.0f,5.0f,0);

    public static final Block prototypeExtractor = BlockHelper.createBlock(MOD_ID,new BlockExtractor(Config.getFromConfig("prototypeExtractor",1216),Tiers.PROTOTYPE,Material.rock),"prototype.extractor","prototypeblank.png","extractorprototypesideempty.png",Block.soundStoneFootstep,2,3,0);
    public static final int[][] extractorTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"extractorprototypesideempty.png"),TextureHelper.registerBlockTexture(MOD_ID,"extractorprototypesideinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"extractorprototypesideactive.png")};

    public static final Block prototypeCrusher = BlockHelper.createBlock(MOD_ID,new BlockCrusher(Config.getFromConfig("prototypeCrusher",1217),Tiers.PROTOTYPE,Material.rock),"prototype.crusher","crusherprototypetopinactive.png","prototypeblank.png","crusherprototypeside.png","prototypeblank.png","prototypeblank.png","prototypeblank.png",Block.soundStoneFootstep,2,3,0);
    public static final int[][] crusherTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"crusherprototypetopinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"crusherprototypetopactive.png")};

    public static final Block prototypeAlloySmelter = BlockHelper.createBlock(MOD_ID,new BlockAlloySmelter(Config.getFromConfig("prototypeAlloySmelter",1218),Tiers.PROTOTYPE,Material.rock),"prototype.alloySmelter","prototypeblank.png","prototypeblank.png","alloysmelterprototypeinactive.png","prototypeblank.png","prototypeblank.png","prototypeblank.png",Block.soundStoneFootstep,2,3,0);
    public static final int[][] alloySmelterTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"alloysmelterprototypeinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"alloysmelterprototypeactive.png")};

    public static final Block prototypePlateFormer = BlockHelper.createBlock(MOD_ID,new BlockPlateFormer(Config.getFromConfig("prototypePlateFormer",1219),Tiers.PROTOTYPE,Material.rock),"prototype.plateFormer","prototypeblank.png","prototypeblank.png","plateformerprototypeinactive.png","prototypeblank.png","prototypeblank.png","prototypeblank.png",Block.soundStoneFootstep,2,3,0);
    public static final int[][] plateFormerTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"plateformerprototypeinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"plateformerprototypeactive.png")};

    public static final Block prototypeCrystalCutter = BlockHelper.createBlock(MOD_ID,new BlockCrystalCutter(Config.getFromConfig("prototypeCrystalCutter",1220),Tiers.PROTOTYPE,Material.rock),"prototype.crystalCutter","prototypeblank.png","prototypeblank.png","crystalcutterprototypeinactive.png","prototypeblank.png","prototypeblank.png","prototypeblank.png",Block.soundStoneFootstep,2,3,0);
    public static final int[][] crystalCutterTex = new int[][]{TextureHelper.registerBlockTexture(MOD_ID,"crystalcutterprototypeinactive.png"),TextureHelper.registerBlockTexture(MOD_ID,"crystalcutterprototypeactive.png")};


    //this has to be after any other block
    public static final int[] energyTex = TextureHelper.registerBlockTexture(MOD_ID,"signalumenergy.png"); //registerFluidTexture(MOD_ID,"signalumenergy.png",0,4);
    public static final Block energyFlowing = BlockHelper.createBlock(MOD_ID,new BlockFluidFlowing(Config.getFromConfig("signalumEnergy",1200),Material.water),"signalumEnergy","signalumenergy.png",Block.soundPowderFootstep,1.0f,1.0f,0).setNotInCreativeMenu().setPlaceOverwrites().setTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]);
    public static final Block energyStill = BlockHelper.createBlock(MOD_ID,new BlockFluidStill(Config.getFromConfig("signalumEnergy",1200)+1,Material.water),"signalumEnergy","signalumenergy.png",Block.soundPowderFootstep,1.0f,1.0f,0).setNotInCreativeMenu().setPlaceOverwrites().setPlaceOverwrites().setTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]);


    public static final Item signalumCrystalEmpty = ItemHelper.createItem(MOD_ID,new ItemSignalumCrystal(Config.getFromConfig("signalumCrystalEmpty",601)),"signalumCrystalEmpty","signalumcrystalempty.png").setMaxStackSize(1);
    public static final Item signalumCrystal = ItemHelper.createItem(MOD_ID,new ItemSignalumCrystal(Config.getFromConfig("signalumCrystal",600)),"signalumCrystal","signalumcrystal.png").setMaxStackSize(1);
    public static final Item rawSignalumCrystal = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("rawSignalumCrystal",602)),"rawSignalumCrystal","rawsignalumcrystal.png");

    public static final Item coalDust = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("coalDust",603)),"coalDust","coaldust.png");
    public static final Item emptySignalumCrystalDust = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("emptySignalumCrystalDust",604)),"signalumCrystalDust","emptysignalumdust.png");
    public static final Item saturatedSignalumCrystalDust = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("saturatedSignalumCrystalDust",605)),"saturatedSignalumCrystalDust","saturatedsignalumdust.png");

    public static final Item ironPlateHammer = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("ironPlateHammer",606)),"ironPlateHammer","platehammer.png");

    public static final Item cobblestonePlate = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("cobblestonePlate",607)),"cobblestonePlate","cobblestoneplate.png");
    public static final Item stonePlate = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("stonePlate",608)),"stonePlate","stoneplate.png");
    public static final Item crystalAlloyPlate = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("crystalAlloyPlate",610)),"crystalAlloyPlate","crystalalloyplate.png");
    public static final Item steelPlate = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("steelPlate",611)),"steelPlate","steelplate.png");

    public static final Item crystalAlloyIngot = ItemHelper.createItem(MOD_ID,new Item(Config.getFromConfig("crystalAlloyIngot",609)),"crystalAlloyIngot","crystalalloy.png");

    public static final ArmorMaterial armorPrototypeHarness = ArmorHelper.createArmorMaterial("signalumprototypeharness",1200,10,10,10,10);

    public static final ItemArmorTiered signalumPrototypeHarness = (ItemArmorTiered) ItemHelper.createItem(MOD_ID,new ItemSignalumPrototypeHarness(Config.getFromConfig("prototypeHarness",700),armorPrototypeHarness,1,Tiers.BASIC),"basic.prototypeHarness","harness.png");
    public static final ItemArmorTiered signalumPrototypeHarnessGoggles = (ItemArmorTiered) ItemHelper.createItem(MOD_ID,new ItemSignalumPrototypeHarness(Config.getFromConfig("prototypeHarnessGoggles",701),armorPrototypeHarness,0,Tiers.BASIC),"basic.prototypeHarnessGoggles","goggles.png");

    @Override
    public void onInitialize() {
        LOGGER.info("Signal Industries initialized.");
    }

    public SignalIndustries(){
        CommandHelper.createCommand(new NBTEditCommand());
        EntityHelper.createSpecialTileEntity(TileEntityConduit.class, new RenderFluidInConduit(),"Conduit");
        EntityHelper.createSpecialTileEntity(TileEntityFluidConduit.class, new RenderFluidInConduit(),"Fluid Conduit");
        EntityHelper.createEntity(EntityCrystal.class,new RenderSnowball(signalumCrystal.getIconFromDamage(0)),47,"signalumCrystal");

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

        Config.init();
    }


    public static void displayGui(EntityPlayer entityplayer, GuiScreen guiScreen, Container container, IInventory tile) {
        if(entityplayer instanceof EntityPlayerMP) {
            ((IEntityPlayerMP)entityplayer).displayGuiScreen_si(guiScreen,container,tile);
        } else {
            Minecraft.getMinecraft().displayGuiScreen(guiScreen);
        }
    }

    public static void addToNameGuiMap(String name, Class<? extends Gui> guiClass, Class<? extends TileEntity> tileEntityClass){
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
}
