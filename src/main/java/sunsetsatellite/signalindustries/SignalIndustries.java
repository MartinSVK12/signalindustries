package sunsetsatellite.signalindustries;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.fx.EntityFX;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.client.render.entity.LivingRenderer;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.SnowballRenderer;
import net.minecraft.client.render.model.ModelZombie;
import net.minecraft.client.sound.block.BlockSounds;
import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.type.WorldTypes;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.core.util.NBTEditCommand;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.RenderMultiblock;
import sunsetsatellite.catalyst.multiblocks.Structure;
import sunsetsatellite.catalyst.multiblocks.StructureCommand;
import sunsetsatellite.signalindustries.abilities.powersuit.*;
import sunsetsatellite.signalindustries.blocks.*;
import sunsetsatellite.signalindustries.blocks.states.EEPROMProgrammerStateInterpreter;
import sunsetsatellite.signalindustries.dim.WorldTypeEternity;
import sunsetsatellite.signalindustries.entities.EntityCrystal;
import sunsetsatellite.signalindustries.entities.EntityEnergyOrb;
import sunsetsatellite.signalindustries.entities.mob.EntityInfernal;
import sunsetsatellite.signalindustries.gui.*;
import sunsetsatellite.signalindustries.interfaces.mixins.IEntityPlayerMP;
import sunsetsatellite.signalindustries.interfaces.mixins.IKeybinds;
import sunsetsatellite.signalindustries.inventories.*;
import sunsetsatellite.signalindustries.inventories.base.TileEntityWithName;
import sunsetsatellite.signalindustries.inventories.item.InventoryHarness;
import sunsetsatellite.signalindustries.inventories.item.InventoryPulsar;
import sunsetsatellite.signalindustries.inventories.machines.*;
import sunsetsatellite.signalindustries.items.*;
import sunsetsatellite.signalindustries.items.abilities.ItemWithAbility;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.items.attachments.ItemWingsAttachment;
import sunsetsatellite.signalindustries.items.containers.ItemFuelCell;
import sunsetsatellite.signalindustries.items.containers.ItemSignalumCrystal;
import sunsetsatellite.signalindustries.items.containers.ItemSignalumDrill;
import sunsetsatellite.signalindustries.items.containers.ItemSignalumSaber;
import sunsetsatellite.signalindustries.items.attachments.ItemPulsarAttachment;
import sunsetsatellite.signalindustries.items.attachments.ItemTieredAttachment;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;
import sunsetsatellite.signalindustries.powersuit.GuiPowerSuit;
import sunsetsatellite.signalindustries.render.*;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.BlockTexture;
import sunsetsatellite.signalindustries.util.Mode;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.signalindustries.weather.WeatherBloodMoon;
import sunsetsatellite.signalindustries.weather.WeatherEclipse;
import sunsetsatellite.signalindustries.weather.WeatherSolarApocalypse;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.*;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.achievements.AchievementPage;
import turniplabs.halplibe.util.toml.Toml;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.model.block.BlockModelDragonFly;
import useless.dragonfly.model.entity.BenchEntityModel;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class SignalIndustries implements ModInitializer, GameStartEntrypoint {

    private static int availableBlockId = 1200;
    private static int availableItemId = 17100;

    public static final String MOD_ID = "signalindustries";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final TomlConfigHandler config;
    public static List<ChunkCoordinates> meteorLocations = new ArrayList<>();

    static {
        Toml configToml = new Toml("Signal Industries configuration file.");
        configToml.addCategory("BlockIDs");
        configToml.addCategory("ItemIDs");
        configToml.addCategory("EntityIDs");
        configToml.addCategory("Other");
        configToml.addEntry("Other.eternityDimId",3);
        configToml.addEntry("Other.GuiId",10);
        configToml.addEntry("Other.machinePacketId",113);
        configToml.addEntry("EntityIDs.infernalId",50);

        List<Field> blockFields = Arrays.stream(SignalIndustries.class.getDeclaredFields()).filter((F)->Block.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
        for (Field blockField : blockFields) {
            configToml.addEntry("BlockIDs."+blockField.getName(),availableBlockId++);
        }
        List<Field> itemFields = Arrays.stream(SignalIndustries.class.getDeclaredFields()).filter((F)->Item.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
        for (Field itemField : itemFields) {
            configToml.addEntry("ItemIDs."+itemField.getName(),availableItemId++);
        }

        config = new TomlConfigHandler(MOD_ID,configToml);

        try {
            Class.forName("net.minecraft.core.block.Block");
            Class.forName("net.minecraft.core.item.Item");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_center.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_bottom.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_bottom_left.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_bottom_right.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_top.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_top_left.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_top_right.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_top_bottom.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_left_right.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_no_right.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_no_left.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_no_bottom.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_no_top.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_left.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_right.png");
    }
    //public static final Config config = new Config(MOD_ID, mapOf(new String[]{"PacketOpenMachineGUI_ID","PacketPipeItemSpawn_ID","GuiID"},new String[]{"113","114","9"}), new Class[]{SignalIndustries.class});

    public static HashMap<String, ArrayList<Class<?>>> nameToGuiMap = new HashMap<>();
    public static final Block signalumOre = new BlockBuilder(MOD_ID)
            .setTextures("signalum_ore.png")
            .setLuminance(1)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(3)
            .setResistance(25)
            .build(new BlockOreSignalum("signalumOre",config.getInt("BlockIDs.signalumOre")).withTags(BlockTags.MINEABLE_BY_PICKAXE));

    public static final Block dilithiumOre = new BlockBuilder(MOD_ID)
            .setTextures("dilithium_ore.png")
            .setLuminance(1)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(75)
            .setResistance(100)
            .build(new BlockOreDilithium("dilithiumOre",config.getInt("BlockIDs.dilithiumOre")).withTags(BlockTags.MINEABLE_BY_PICKAXE));

    public static final Block dimensionalShardOre = new BlockBuilder(MOD_ID)
            .setTextures("dimensional_shard_ore.png")
            .setLuminance(1)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(200)
            .setResistance(50000)
            .build(new BlockOreDimensionalShard("dimensionalShardOre",config.getInt("BlockIDs.dimensionalShardOre")).withTags(BlockTags.MINEABLE_BY_PICKAXE));


    public static final Block dilithiumBlock = new BlockBuilder(MOD_ID)
            .setTextures("dilithium_block.png")
            .setLuminance(1)
            .setBlockSound(BlockSounds.GLASS)
            .setHardness(20)
            .setResistance(1000)
            .build(new Block("dilithiumBlock",config.getInt("BlockIDs.dilithiumBlock"),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
    public static final Block emptyCrystalBlock = new BlockBuilder(MOD_ID)
            .setTextures("empty_crystal_block.png")
            .setLuminance(1)
            .setBlockSound(BlockSounds.GLASS)
            .setHardness(12)
            .setResistance(1000)
            .build(new Block("emptyCrystalBlock",config.getInt("BlockIDs.emptyCrystalBlock"),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
    public static final Block rawCrystalBlock = new BlockBuilder(MOD_ID)
            .setTextures("saturated_crystal_block.png")
            .setLuminance(1).setBlockSound(BlockSounds.GLASS)
            .setHardness(24).setResistance(50000)
            .build(new Block("rawCrystalBlock",config.getInt("BlockIDs.rawCrystalBlock"),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
    public static final Block awakenedSignalumCrystalBlock = new BlockBuilder(MOD_ID)
            .setTextures("awakened_crystal_block.png")
            .setLuminance(1)
            .setBlockSound(BlockSounds.GLASS)
            .setHardness(50)
            .setResistance(1000000)
            .build(new Block("awakenedSignalumCrystalBlock",config.getInt("BlockIDs.awakenedSignalumCrystalBlock"),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));

    public static final Block dilithiumCrystalBlock = new BlockBuilder(MOD_ID)
            .setTextures("dilithium_crystal_block.png")
            .setLuminance(1)
            .setBlockSound(BlockSounds.GLASS)
            .setHardness(20)
            .setResistance(1000)
            .build(new BlockDilithiumCrystal("dilithiumCrystalBlock",config.getInt("BlockIDs.dilithiumCrystalBlock"),Material.glass,false)).withTags(BlockTags.MINEABLE_BY_PICKAXE);

    public static final Block prototypeMachineCore = new BlockBuilder(MOD_ID)
            .setTextures("machine_prototype.png")
            .setBlockSound(BlockSounds.STONE)
            .setLuminance(0)
            .setHardness(1)
            .setResistance(3)
            .build(new BlockTiered("prototype.machine",config.getInt("BlockIDs.prototypeMachineCore"), Tier.PROTOTYPE,Material.stone));

    public static final Block basicMachineCore = new BlockBuilder(MOD_ID)
            .setTextures("machine_basic.png")
            .setBlockSound(BlockSounds.METAL)
            .setLuminance(0)
            .setHardness(3)
            .setResistance(8)
            .build(new BlockTiered("basic.machine",config.getInt("BlockIDs.basicMachineCore"), Tier.BASIC,Material.metal));

    public static final Block reinforcedMachineCore = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("machine_reinforced.png")
            .setLuminance(0)
            .setHardness(4)
            .setResistance(15)
            .build(new BlockTiered("reinforced.machine",config.getInt("BlockIDs.reinforcedMachineCore"), Tier.REINFORCED,Material.metal));

    public static final Block awakenedMachineCore = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("machine_awakened.png")
            .setLuminance(1)
            .setHardness(1)
            .setResistance(50)
            .build(new BlockTiered("awakened.machine",config.getInt("BlockIDs.awakenedMachineCore"), Tier.AWAKENED,Material.metal));


    public static final Block reinforcedCasing = new BlockBuilder(MOD_ID)
            .setTextures("reinforced_casing.png")
            .setLuminance(0)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(10)
            .setResistance(2000)
            .build(new Block("reinforced.casing",config.getInt("BlockIDs.reinforcedCasing"),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
    public static final Block reinforcedGlass = new BlockBuilder(MOD_ID)
            .setTextures("reinforced_glass.png")
            .setLuminance(0)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(4)
            .setResistance(2000)
            .build(new BlockConnectedTextureCursed("reinforced.glass",config.getInt("BlockIDs.reinforcedGlass"),Material.metal,"reinforced_glass").withTags(BlockTags.MINEABLE_BY_PICKAXE));

    public static final Block prototypeConduit = new BlockBuilder(MOD_ID)
            .setTextures("conduit_prototype.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockConduit("prototype.conduit",config.getInt("BlockIDs.prototypeConduit"),Tier.PROTOTYPE,Material.glass));

    public static final Block basicConduit = new BlockBuilder(MOD_ID)
            .setTextures("conduit_basic.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockConduit("basic.conduit",config.getInt("BlockIDs.basicConduit"),Tier.BASIC,Material.glass));

    public static final Block reinforcedConduit = new BlockBuilder(MOD_ID)
            .setTextures("conduit_reinforced.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockConduit("reinforced.conduit",config.getInt("BlockIDs.reinforcedConduit"),Tier.REINFORCED,Material.glass));

    public static final Block awakenedConduit = new BlockBuilder(MOD_ID)
            .setTextures("conduit_awakened.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockConduit("awakened.conduit",config.getInt("BlockIDs.awakenedConduit"),Tier.AWAKENED,Material.glass));


    public static final Block prototypeFluidConduit = new BlockBuilder(MOD_ID)
            .setTextures("fluid_pipe_prototype.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockFluidConduit("prototype.conduit.fluid",config.getInt("BlockIDs.prototypeFluidConduit"),Tier.PROTOTYPE,Material.glass));

    public static final Block basicFluidConduit = new BlockBuilder(MOD_ID)
            .setTextures("fluid_pipe_basic.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockFluidConduit("basic.conduit.fluid",config.getInt("BlockIDs.basicFluidConduit"),Tier.BASIC,Material.glass));

    public static final Block reinforcedFluidConduit = new BlockBuilder(MOD_ID)
            .setTextures("fluid_pipe_reinforced.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockFluidConduit("reinforced.conduit.fluid",config.getInt("BlockIDs.reinforcedFluidConduit"),Tier.REINFORCED,Material.glass));


    public static final Block infiniteEnergyCell = new BlockBuilder(MOD_ID)
            .setTextures("cell_prototype.png")
            .setLuminance(1)
            .setHardness(-1)
            .setResistance(1000000)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockEnergyCell("infinite.energyCell",config.getInt("BlockIDs.infiniteEnergyCell"),Tier.INFINITE,Material.glass));

    public static final Block prototypeEnergyCell = new BlockBuilder(MOD_ID)
            .setTextures("cell_prototype.png")
            .setLuminance(1)
            .setHardness(1)
            .setResistance(5)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockEnergyCell("prototype.energyCell",config.getInt("BlockIDs.prototypeEnergyCell"),Tier.PROTOTYPE,Material.glass));

    public static final Block basicEnergyCell = new BlockBuilder(MOD_ID)
            .setTextures("cell_basic.png")
            .setLuminance(1)
            .setHardness(1)
            .setResistance(5)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockEnergyCell("basic.energyCell",config.getInt("BlockIDs.basicEnergyCell"),Tier.BASIC,Material.glass));


    public static final Block prototypeFluidTank = new BlockBuilder(MOD_ID)
            .setTextures("fluid_tank_prototype.png")
            .setLuminance(0)
            .setHardness(1)
            .setResistance(5)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockSIFluidTank("prototype.fluidTank",config.getInt("BlockIDs.prototypeFluidTank"),Tier.PROTOTYPE,Material.glass));

    public static final Block basicFluidTank = new BlockBuilder(MOD_ID)
            .setTextures("fluid_tank_basic.png")
            .setLuminance(0)
            .setHardness(1)
            .setResistance(5)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockSIFluidTank("basic.fluidTank",config.getInt("BlockIDs.basicFluidTank"),Tier.BASIC,Material.glass));


    /*public static final Block recipeMaker = new BlockBuilder(MOD_ID)
            .setTextures("prototype_connection.png")
            .setLuminance(0)
            .setHardness(1)
            .setResistance(5)
            .setBlockSound(BlockSounds.STONE)
            .build(new BlockRecipeMaker("recipeMaker",config.getInt("BlockIDs.recipeMaker"),Material.stone));*/


    public static final Block prototypeExtractor = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("prototype_blank.png")
            .setSideTextures("extractor_prototype_side_empty.png")
            .build(new BlockExtractor("prototype.extractor",config.getInt("BlockIDs.prototypeExtractor"),Tier.PROTOTYPE,Material.stone));

    public static final Block basicExtractor = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setSideTextures("extractor_basic_side_empty.png")
            .build(new BlockExtractor("basic.extractor",config.getInt("BlockIDs.basicExtractor"),Tier.BASIC,Material.metal));

    public static final Block prototypeCrusher = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("prototype_blank.png")
            .setTopTexture("crusher_prototype_top_inactive.png")
            .setNorthTexture("crusher_prototype_side.png")
            .build(new BlockCrusher("prototype.crusher",config.getInt("BlockIDs.prototypeCrusher"), Tier.PROTOTYPE,Material.stone));

    public static final Block basicCrusher = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setTopTexture("crusher_basic_top_inactive.png")
            .setNorthTexture("crusher_basic_side.png")
            .build(new BlockCrusher("basic.crusher",config.getInt("BlockIDs.basicCrusher"), Tier.BASIC,Material.metal));

    public static final Block prototypeAlloySmelter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("prototype_blank.png")
            .setNorthTexture("alloy_smelter_prototype_inactive.png")
            .build(new BlockAlloySmelter("prototype.alloySmelter",config.getInt("BlockIDs.prototypeAlloySmelter"), Tier.PROTOTYPE,Material.stone));

    public static final Block basicAlloySmelter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setNorthTexture("alloy_smelter_basic_inactive.png")
            .build(new BlockAlloySmelter("basic.alloySmelter",config.getInt("BlockIDs.basicAlloySmelter"), Tier.BASIC,Material.metal));

    public static final Block prototypePlateFormer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("prototype_blank.png")
            .setNorthTexture("plate_former_prototype_inactive.png")
            .build(new BlockPlateFormer("prototype.plateFormer",config.getInt("BlockIDs.prototypePlateFormer"), Tier.PROTOTYPE,Material.stone));

    public static final Block prototypeCrystalCutter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("prototype_blank.png")
            .setNorthTexture("crystal_cutter_prototype_inactive.png")
            .build(new BlockCrystalCutter("prototype.crystalCutter",config.getInt("BlockIDs.prototypeCrystalCutter"), Tier.PROTOTYPE,Material.stone));

    public static final Block basicCrystalCutter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("basic_blank.png")
            .setNorthTexture("crystal_cutter_basic_inactive.png")
            .build(new BlockCrystalCutter("basic.crystalCutter",config.getInt("BlockIDs.basicCrystalCutter"), Tier.BASIC,Material.stone));

    public static final Block basicCrystalChamber = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setNorthTexture("basic_crystal_chamber_side_inactive.png")
            .build(new BlockCrystalChamber("basic.crystalChamber",config.getInt("BlockIDs.basicCrystalChamber"), Tier.BASIC,Material.stone));

    public static final Block basicInfuser = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setSideTextures("infuser_basic_side_inactive.png")
            .build(new BlockInfuser("basic.infuser",config.getInt("BlockIDs.basicInfuser"), Tier.BASIC,Material.metal));

    public static final Block basicWrathBeacon = new BlockBuilder(MOD_ID)
            .setHardness(2)
            .setResistance(500)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setSideTextures("wrath_beacon.png")
            .build(new BlockWrathBeacon("basic.wrathBeacon",config.getInt("BlockIDs.basicWrathBeacon"), Tier.BASIC,Material.metal));
    //public static final Block reinforcedWrathBeacon = new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockWrathBeacon("",config.getInt("BlockIDs.reinforcedWrathBeacon"),Tiers.REINFORCED,Material.metal),"reinforced.wrathBeacon","reinforced_blank.png","reinforced_wrath_beacon_active.png",BlockSounds.METAL,25f,500f,1);
    //public static final Block awakenedWrathBeacon = new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockWrathBeacon("",config.getInt("BlockIDs.awakenedWrathBeacon"),Tiers.AWAKENED,Material.metal),"awakened.wrathBeacon","reinforced_blank.png","awakened_wrath_beacon_active.png",BlockSounds.METAL,25f,500f,1);
    public static final int[][] wrathBeaconTex = new int[][]{TextureHelper.getOrCreateBlockTexture(MOD_ID,"wrath_beacon.png"),TextureHelper.getOrCreateBlockTexture(MOD_ID,"wrath_beacon_active.png")};

    public static final Block dimensionalAnchor = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("reinforced_blank.png")
            .setTopTexture("dimensional_anchor_top_inactive.png")
            .setSideTextures("dimensional_anchor_inactive.png")
            .build(new BlockDimensionalAnchor("reinforced.dimensionalAnchor",config.getInt("BlockIDs.dimensionalAnchor"), Tier.REINFORCED,Material.metal));

    public static final Block dilithiumStabilizer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("reinforced_blank.png")
            .setSideTextures("dilithium_stabilizer_side_inactive.png")
            .setNorthTexture("dilithium_top_inactive.png")
            .build(new BlockDilithiumStabilizer("reinforced.dilithiumStabilizer",config.getInt("BlockIDs.dilithiumStabilizer"), Tier.REINFORCED,Material.metal));

    public static final Block dilithiumBooster = new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockDilithiumBooster("reinforced.dilithiumBooster",config.getInt("BlockIDs.dilithiumBooster"), Tier.REINFORCED,Material.metal),"reinforced_blank.png","reinforced_blank.png","dilithium_top_inactive.png","dilithium_booster_side_inactive.png","dilithium_booster_side_inactive.png","dilithium_booster_side_inactive.png",BlockSounds.METAL,5f,20f,1);
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("reinforced_blank.png")
            .setSideTextures("dilithium_booster_side_inactive.png")
            .setNorthTexture("dilithium_top_inactive.png")
            .build(new BlockDilithiumBooster("reinforced.dilithiumBooster",config.getInt("BlockIDs.dilithiumBooster"), Tier.REINFORCED,Material.metal));

    public static final Block prototypePump = new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockPump("prototype.pump",config.getInt("BlockIDs.prototypePump"), Tier.PROTOTYPE,Material.stone),"prototype_pump_top_empty.png","prototype_blank.png","prototype_pump_side_empty.png","prototype_pump_side_empty.png","prototype_pump_side_empty.png","prototype_pump_side_empty.png",BlockSounds.STONE,2,3,0);
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("prototype_blank.png")
            .setTopTexture("prototype_pump_top_empty.png")
            .setSideTextures("prototype_pump_side_empty.png")
            .build(new BlockPump("prototype.pump",config.getInt("BlockIDs.prototypePump"), Tier.PROTOTYPE,Material.stone));

    /*public static final Block prototypeBlockBreaker = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("prototype_blank.png")
            .setTopBottomTexture("prototype_block_breaker_side_2.png")
            .setSideTextures("prototype_block_breaker_side.png")
            .setNorthTexture("prototype_block_breaker.png")
            .build(new BlockBreaker("prototype.blockBreaker",config.getInt("BlockIDs.prototypeBlockBreaker"),Tier.PROTOTYPE,Material.stone));*/

    public static final int[][] breakerTex = new int[][]{TextureHelper.getOrCreateBlockTexture(MOD_ID,"prototype_block_breaker.png"),TextureHelper.getOrCreateBlockTexture(MOD_ID,"prototype_block_breaker_active.png"),TextureHelper.getOrCreateBlockTexture(MOD_ID,"prototype_block_breaker_side.png"),TextureHelper.getOrCreateBlockTexture(MOD_ID,"prototype_block_breaker_side_active.png"),TextureHelper.getOrCreateBlockTexture(MOD_ID,"prototype_block_breaker_side_2.png"),TextureHelper.getOrCreateBlockTexture(MOD_ID,"prototype_block_breaker_side_2_active.png"),TextureHelper.getOrCreateBlockTexture(MOD_ID,"inserteroutput.png")};
    public static final Block basicAutomaticMiner = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setTextures("basic_blank.png")
            .setNorthTexture("basic_automatic_miner.png")
            .build(new BlockAutoMiner("basic.automaticMiner",config.getInt("BlockIDs.basicAutomaticMiner"),Tier.BASIC,Material.metal));
    public static final Block externalIo = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setTextures("external_io_blank.png")
            .build(new BlockExternalIO("basic.externalIO",config.getInt("BlockIDs.externalIo"),Tier.BASIC,Material.metal));

    public static final Block reinforcedCentrifuge = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(1)
            .setTextures("reinforced_blank.png")
            .setNorthTexture("reinforced_centrifuge_front_inactive.png")
            .setTopTexture("reinforced_centrifuge_empty.png")
            .build(new BlockCentrifuge("reinforced.centrifuge",config.getInt("BlockIDs.reinforcedCentrifuge"),Tier.REINFORCED,Material.metal));

    public static final Block reinforcedIgnitor = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(1)
            .setSideTextures("reinforced_ignitor_inactive.png")
            .setTopTexture("reinforced_ignitor_top_inactive.png")
            .setBottomTexture("reinforced_ignitor_bottom_inactive.png")
            .build(new BlockIgnitor("reinforced.ignitor",config.getInt("BlockIDs.reinforcedIgnitor"),Tier.REINFORCED,Material.metal));

    public static final Block signalumReactorCore = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(1)
            .setTextures("reinforced_blank.png")
            .setSideTextures("signalum_reactor_side_inactive.png")
            .setNorthTexture("signalum_reactor_front_inactive.png")
            .build(new BlockSignalumReactorCore("reinforced.signalumReactorCore",config.getInt("BlockIDs.signalumReactorCore"),Tier.REINFORCED,Material.metal));

    public static final Block reinforcedEnergyConnector = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(1)
            .setTextures("reinforced_energy_connector.png")
            .build(new BlockEnergyConnector("reinforced.energyConnector",config.getInt("BlockIDs.reinforcedEnergyConnector"),Tier.REINFORCED,Material.metal));

    public static final Block reinforcedFluidInputHatch = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("reinforced_fluid_input_hatch.png")
            .build(new BlockFluidInputHatch("reinforced.fluidInputHatch",config.getInt("BlockIDs.reinforcedFluidInputHatch"),Tier.REINFORCED,Material.metal));

    public static final Block reinforcedFluidOutputHatch = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("reinforced_fluid_output_hatch.png")
            .build(new BlockFluidOutputHatch("reinforced.fluidOutputHatch",config.getInt("BlockIDs.reinforcedFluidOutputHatch"),Tier.REINFORCED,Material.metal));

    public static final Block reinforcedItemInputBus = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("reinforced_input_bus.png")
            .build(new BlockInputBus("reinforced.itemInputBus",config.getInt("BlockIDs.reinforcedItemInputBus"),Tier.REINFORCED,Material.metal));

    public static final Block reinforcedItemOutputBus = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("reinforced_output_bus.png")
            .build(new BlockOutputBus("reinforced.itemOutputBus",config.getInt("BlockIDs.reinforcedItemOutputBus"),Tier.REINFORCED,Material.metal));

    public static final Block basicEnergyInjector = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID,"basic_energy_injector.json")))
            .setTextures("basic_energy_injector_bottom.png")
            .build(new BlockEnergyInjector("basic.energyInjector",config.getInt("BlockIDs.basicEnergyInjector"),Tier.BASIC,Material.metal));

    public static final Block basicSignalumDynamo = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setBlockModel(new BlockModelDragonFly(ModelHelper.getOrCreateBlockModel(MOD_ID,"signalum_dynamo.json")))
            .setTextures(1,0)
            .build(new BlockSignalumDynamo("basic.dynamo",config.getInt("BlockIDs.basicSignalumDynamo"),Tier.BASIC,Material.metal));

    public static final Block basicProgrammer = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setBlockModel(
                    new BlockModelDragonFly(
                            ModelHelper.getOrCreateBlockModel(MOD_ID,"eeprom_programmer.json"),
                            ModelHelper.getOrCreateBlockState(MOD_ID,"eeprom_programmer.json"),
                            new EEPROMProgrammerStateInterpreter(),
                            true
                    )
            )
            .setTextures(1,0)
            .build(new BlockProgrammer("basic.programmer",config.getInt("BlockIDs.basicProgrammer"),Tier.BASIC,Material.metal));

    //this has to be after any other block
    public static final int[] energyTex = TextureHelper.getOrCreateBlockTexture(MOD_ID,"signalum_energy_transparent.png");
    public static final int[] burntSignalumTex = TextureHelper.getOrCreateBlockTexture(MOD_ID,"burnt_signalum.png");//registerFluidTexture(MOD_ID,"signalum_energy.png",0,4);
    public static final Block energyFlowing = new BlockBuilder(MOD_ID)
            .setTextures("signalum_energy_transparent.png")
            .build(new BlockFluidFlowing("signalumEnergy",config.getInt("BlockIDs.energyFlowing"),Material.water).withTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES));
    //BlockHelper.createBlock(MOD_ID,new BlockFluidFlowing("signalumEnergy",config.getInt("BlockIDs.energyFlowing"),Material.water),"signalum_energy_transparent.png",BlockSounds.DEFAULT,1.0f,1.0f,0).withTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES);
    public static final Block energyStill = new BlockBuilder(MOD_ID)
            .setTextures("signalum_energy_transparent.png")
            .build(new BlockFluidFlowing("signalumEnergy",config.getInt("BlockIDs.energyStill"),Material.water).withTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES));
    //BlockHelper.createBlock(MOD_ID,new BlockFluidStill("signalumEnergy",config.getInt("BlockIDs.energyStill"),Material.water),"signalum_energy_transparent.png",BlockSounds.DEFAULT,1.0f,1.0f,0).withTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES);

    public static final Block burntSignalumFlowing = new BlockBuilder(MOD_ID)
            .setTextures("burnt_signalum.png")
            .build(new BlockFluidFlowing("burntSignalum",config.getInt("BlockIDs.burntSignalumFlowing"),Material.water).withTexCoords(burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1]).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES));
    public static final Block burntSignalumStill = new BlockBuilder(MOD_ID)
            .setTextures("burnt_signalum.png")
            .build(new BlockFluidStill("burntSignalum",config.getInt("BlockIDs.burntSignalumStill"),Material.water).withTexCoords(burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1],burntSignalumTex[0],burntSignalumTex[1]).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES));

    public static final Item signalumCrystalEmpty = ItemHelper.createItem(MOD_ID,new ItemSignalumCrystal("signalumCrystalEmpty",config.getInt("ItemIDs.signalumCrystalEmpty")),"signalumcrystalempty.png").setMaxStackSize(1);
    public static final Item signalumCrystal = ItemHelper.createItem(MOD_ID,new ItemSignalumCrystal("signalumCrystal",config.getInt("ItemIDs.signalumCrystal")),"signalumcrystal.png").setMaxStackSize(1);
    public static final Item rawSignalumCrystal = ItemHelper.createItem(MOD_ID,new Item("rawSignalumCrystal",config.getInt("ItemIDs.rawSignalumCrystal")),"rawsignalumcrystal.png");

    public static final Item awakenedSignalumCrystal = ItemHelper.createItem(MOD_ID, new Item("awakenedSignalumCrystal",config.getInt("ItemIDs.awakenedSignalumCrystal")),"awakenedsignalumcrystal.png").setMaxStackSize(1);
    public static final Item awakenedSignalumFragment = ItemHelper.createItem(MOD_ID, new Item("awakenedSignalumFragment",config.getInt("ItemIDs.awakenedSignalumFragment")),"awakenedsignalumfragment.png");

    public static final Item coalDust = ItemHelper.createItem(MOD_ID,new Item("coalDust",config.getInt("ItemIDs.coalDust")),"coaldust.png");
    public static final Item netherCoalDust = ItemHelper.createItem(MOD_ID,new Item("netherCoalDust",config.getInt("ItemIDs.netherCoalDust")),"nethercoaldust.png");
    public static final Item emptySignalumCrystalDust = ItemHelper.createItem(MOD_ID,new Item("signalumCrystalDust",config.getInt("ItemIDs.emptySignalumCrystalDust")),"emptysignalumdust.png");
    public static final Item saturatedSignalumCrystalDust = ItemHelper.createItem(MOD_ID,new Item("saturatedSignalumCrystalDust",config.getInt("ItemIDs.saturatedSignalumCrystalDust")),"saturatedsignalumdust.png");

    public static final Item ironPlateHammer = ItemHelper.createItem(MOD_ID,new Item("ironPlateHammer",config.getInt("ItemIDs.ironPlateHammer")),"platehammer.png").setMaxStackSize(1);

    public static final Item cobblestonePlate = ItemHelper.createItem(MOD_ID,new Item("cobblestonePlate",config.getInt("ItemIDs.cobblestonePlate")),"cobblestoneplate.png");
    public static final Item stonePlate = ItemHelper.createItem(MOD_ID,new Item("stonePlate",config.getInt("ItemIDs.stonePlate")),"stoneplate.png");
    public static final Item crystalAlloyPlate = ItemHelper.createItem(MOD_ID,new Item("crystalAlloyPlate",config.getInt("ItemIDs.crystalAlloyPlate")),"crystalalloyplate.png");
    public static final Item steelPlate = ItemHelper.createItem(MOD_ID,new Item("steelPlate",config.getInt("ItemIDs.steelPlate")),"steelplate.png");
    public static final Item reinforcedCrystalAlloyPlate = ItemHelper.createItem(MOD_ID,new Item("reinforcedCrystalAlloyPlate",config.getInt("ItemIDs.reinforcedCrystalAlloyPlate")),"reinforcedcrystalalloyplate.png");
    public static final Item saturatedSignalumAlloyPlate = ItemHelper.createItem(MOD_ID,new Item("saturatedSignalumAlloyPlate",config.getInt("ItemIDs.saturatedSignalumAlloyPlate")),"saturatedsignalumalloyplate.png");
    public static final Item dilithiumPlate = ItemHelper.createItem(MOD_ID,new Item("dilithiumPlate",config.getInt("ItemIDs.dilithiumPlate")),"dilithiumplate.png");


    public static final Item crystalAlloyIngot = ItemHelper.createItem(MOD_ID,new Item("crystalAlloyIngot",config.getInt("ItemIDs.crystalAlloyIngot")),"crystalalloy.png");
    public static final Item reinforcedCrystalAlloyIngot = ItemHelper.createItem(MOD_ID,new Item("reinforcedCrystalAlloyIngot",config.getInt("ItemIDs.reinforcedCrystalAlloyIngot")),"reinforcedcrystalalloy.png");
    public static final Item saturatedSignalumAlloyIngot = ItemHelper.createItem(MOD_ID,new Item("saturatedSignalumAlloyIngot",config.getInt("ItemIDs.saturatedSignalumAlloyIngot")),"saturatedsignalumalloy.png");

    public static final Item diamondCuttingGear = ItemHelper.createItem(MOD_ID,new Item("diamondCuttingGear",config.getInt("ItemIDs.diamondCuttingGear")),"diamondcuttinggear.png");

    public static final Block portalEternity = new BlockBuilder(MOD_ID)
            .setTextures("reality_fabric.png")
            .setBlockSound(BlockSounds.GLASS)
            .setLuminance(1)
            .build(new BlockPortal("eternityPortal",config.getInt("BlockIDs.portalEternity"),config.getInt("Other.eternityDimId"),Block.bedrock.id,Block.fire.id));

    //BlockHelper.createBlock(MOD_ID,new BlockPortal("eternityPortal",config.getInt("BlockIDs.portalEternity"),3,Block.bedrock.id,Block.fire.id),"reality_fabric.png",BlockSounds.GLASS,1.0f,1.0f,1);
    public static final Block realityFabric = new BlockBuilder(MOD_ID)
            .setTextures("reality_fabric.png")
            .setBlockSound(BlockSounds.STONE)
            .setHardness(150)
            .setResistance(50000)
            .setLuminance(0)
            .build(new BlockUndroppable("realityFabric",config.getInt("BlockIDs.realityFabric"),Material.stone));
    //BlockHelper.createBlock(MOD_ID,new BlockUndroppable("realityFabric",config.getInt("BlockIDs.realityFabric"),Material.dirt),"reality_fabric.png",BlockSounds.STONE,150f,50000f,0);
    public static final Block rootedFabric = new BlockBuilder(MOD_ID)
            .setTextures("rooted_fabric.png")
            .setBlockSound(BlockSounds.STONE)
            .setHardness(50)
            .setResistance(50000)
            .setLuminance(0)
            .build(new Block("rootedFabric",config.getInt("BlockIDs.rootedFabric"),Material.stone));

    public static final int[][] railTex = new int[][]{TextureHelper.getOrCreateBlockTexture(MOD_ID,"dilithium_rail_unpowered.png"),TextureHelper.getOrCreateBlockTexture(MOD_ID,"dilithium_rail.png")};

    public static final Block dilithiumRail = new BlockBuilder(MOD_ID)
            .setLuminance(0)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(50)
            .setTextures("dilithium_rail_unpowered.png")
            .build(new BlockDilithiumRail("dilithiumRail",config.getInt("BlockIDs.dilithiumRail"),true));


    public static final Item monsterShard = simpleItem("monsterShard","monstershard.png");
    public static final Item infernalFragment = simpleItem("infernalFragment","infernalfragment.png");
    public static final Item evilCatalyst = simpleItem("evilCatalyst","evilcatalyst.png").setMaxStackSize(4);
    public static final Item infernalEye = simpleItem("infernalEye","infernaleye.png").setMaxStackSize(4);
    public static final Item dimensionalShard = simpleItem("dimensionalShard","dimensionalshard.png");
    public static final Item warpOrb = ItemHelper.createItem(MOD_ID,new ItemWarpOrb("warpOrb",config.getInt("ItemIDs.warpOrb")),"warporb.png").setMaxStackSize(1);
    public static final Item realityString = simpleItem("realityString","stringofreality.png");
    public static final Item dilithiumShard = simpleItem("dilithiumShard","dilithiumshard.png");

    public static final Block eternalTreeLog = new BlockBuilder(MOD_ID)
            .setHardness(75f)
            .setResistance(50000)
            .setLuminance(15)
            .setTopBottomTexture("eternal_tree_log_top.png")
            .setSideTextures("eternal_tree_log.png")
            .setBlockSound(BlockSounds.WOOD)
            .build(new BlockEternalTreeLog("eternalTreeLog",config.getInt("BlockIDs.eternalTreeLog"),Material.wood));//new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockEternalTreeLog(key("eternalTreeLog",config.getInt("BlockIDs.eternalTreeLog"),Material.wood),"eternal_tree_log_top.png","eternal_tree_log.png",BlockSounds.WOOD, 75f,50000f,1);

    public static final Block glowingObsidian = new BlockBuilder(MOD_ID)
            .setTextures("glowing_obsidian.png")
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(1200)
            .setLuminance(1)
            .build(new Block("glowingObsidian",config.getInt("BlockIDs.glowingObsidian"),Material.stone));
        //BlockHelper.createBlock(MOD_ID,new Block(key("glowingObsidian",config.getInt("BlockIDs.glowingObsidian"),Material.stone),"glowing_obsidian.png",BlockSounds.STONE, 50f,1200f,1.0f/2.0f);

    public static final ArmorMaterial armorPrototypeHarness = ArmorHelper.createArmorMaterial(SignalIndustries.MOD_ID,"signalumprototypeharness",1200,10,10,10,10);
    public static final ArmorMaterial armorSignalumPowerSuit = ArmorHelper.createArmorMaterial(SignalIndustries.MOD_ID,"signalumpowersuit",9999,50,50,50,50);

    public static final ItemArmorTiered signalumPrototypeHarness = (ItemArmorTiered) ItemHelper.createItem(MOD_ID,new ItemSignalumPrototypeHarness("basic.prototypeHarness",config.getInt("ItemIDs.signalumPrototypeHarness"),armorPrototypeHarness,1, Tier.BASIC),"harness.png");
    public static final ItemArmorTiered signalumPrototypeHarnessGoggles = (ItemArmorTiered) ItemHelper.createItem(MOD_ID,new ItemSignalumPrototypeHarness("basic.prototypeHarnessGoggles",config.getInt("ItemIDs.signalumPrototypeHarnessGoggles"),armorPrototypeHarness,0, Tier.BASIC),"goggles.png");

    public static final ToolMaterial toolMaterialBasic = new ToolMaterial().setDurability(9999).setMiningLevel(3).setEfficiency(25,50);
    public static final ToolMaterial toolMaterialReinforced = new ToolMaterial().setDurability(9999).setMiningLevel(4).setEfficiency(45,80);
    public static final ToolMaterial toolMaterialAwakened = new ToolMaterial().setDurability(9999).setMiningLevel(5).setEfficiency(60,100);

    public static final Item basicSignalumDrill = ItemHelper.createItem(MOD_ID,new ItemSignalumDrill("basic.signalumDrill",config.getInt("ItemIDs.basicSignalumDrill"),Tier.BASIC,toolMaterialBasic),"signalum_drill.png");
    public static final Item reinforcedSignalumDrill = ItemHelper.createItem(MOD_ID,new ItemSignalumDrill("reinforced.signalumDrill",config.getInt("ItemIDs.reinforcedSignalumDrill"),Tier.REINFORCED,toolMaterialReinforced),"signalum_drill_reinforced.png");

    public static final Item fuelCell = ItemHelper.createItem(MOD_ID,new ItemFuelCell("fuelCell",config.getInt("ItemIDs.fuelCell")),"fuelcellempty.png").setMaxStackSize(1);
    public static final int[][] fuelCellTex = new int[][]{TextureHelper.getOrCreateItemTexture(MOD_ID,"fuelcellempty.png"),TextureHelper.getOrCreateItemTexture(MOD_ID,"fuelcellfilled.png"),TextureHelper.getOrCreateItemTexture(MOD_ID,"fuelcelldepleted.png")};


    public static final Item nullTrigger = ItemHelper.createItem(MOD_ID,new ItemTrigger("trigger.null",config.getInt("ItemIDs.nullTrigger")),"trigger.png").setMaxStackSize(1);

    public static final Item romChipProjectile = ItemHelper.createItem(MOD_ID,new ItemRomChip("romChip.projectile",config.getInt("ItemIDs.romChipProjectile")),"chip1.png");
    public static final Item romChipBoost = ItemHelper.createItem(MOD_ID,new ItemRomChip("romChip.boost",config.getInt("ItemIDs.romChipBoost")),"chip2.png");

    public static final Item energyCatalyst = ItemHelper.createItem(MOD_ID,new Item("energyCatalyst",config.getInt("ItemIDs.energyCatalyst")),"energycatalyst.png");

    public static final Item signalumSaber = ItemHelper.createItem(MOD_ID, new ItemSignalumSaber("reinforced.signalumSaber",config.getInt("ItemIDs.signalumSaber"), Tier.REINFORCED, ToolMaterial.stone), "signalumsaberunpowered.png");
    public static final int[][] saberTex = new int[][]{TextureHelper.getOrCreateItemTexture(MOD_ID,"signalumsaberunpowered.png"),TextureHelper.getOrCreateItemTexture(MOD_ID,"signalumsaber.png")};

    public static final Item pulsar = ItemHelper.createItem(MOD_ID,new ItemPulsar("reinforced.pulsar",config.getInt("ItemIDs.pulsar"), Tier.REINFORCED),"pulsaractive.png").setMaxStackSize(1);
    public static final int[][] pulsarTex = new int[][]{TextureHelper.getOrCreateItemTexture(MOD_ID,"pulsarinactive.png"),TextureHelper.getOrCreateItemTexture(MOD_ID,"pulsaractive.png"),TextureHelper.getOrCreateItemTexture(MOD_ID,"pulsarcharged.png"),TextureHelper.getOrCreateItemTexture(MOD_ID,"pulsarwarpactive.png"),TextureHelper.getOrCreateItemTexture(MOD_ID,"pulsarwarpcharged.png")};

    public static final ItemSignalumPowerSuit signalumPowerSuitHelmet = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.helmet",config.getInt("ItemIDs.signalumPowerSuitHelmet"),armorSignalumPowerSuit,0,Tier.REINFORCED),"signalumpowersuit_helmet.png");
    public static final ItemSignalumPowerSuit signalumPowerSuitChestplate = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.chestplate",config.getInt("ItemIDs.signalumPowerSuitChestplate"),armorSignalumPowerSuit,1,Tier.REINFORCED),"signalumpowersuit_chestplate.png");
    public static final ItemSignalumPowerSuit signalumPowerSuitLeggings = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.leggings",config.getInt("ItemIDs.signalumPowerSuitLeggings"),armorSignalumPowerSuit,2,Tier.REINFORCED),"signalumpowersuit_leggings.png");
    public static final ItemSignalumPowerSuit signalumPowerSuitBoots = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.boots",config.getInt("ItemIDs.signalumPowerSuitBoots"),armorSignalumPowerSuit,3,Tier.REINFORCED),"signalumpowersuit_boots.png");

    //public static final Item testingAttachment = ItemHelper.createItem(MOD_ID,new ItemAttachment(config.getInt("ItemIDs.testingAttachment"), listOf(AttachmentPoint.ANY)),"attachment.testingAttachment","energyorb.png");
    public static final Item pulsarAttachment = ItemHelper.createItem(MOD_ID,new ItemPulsarAttachment("reinforced.attachment.pulsar",config.getInt("ItemIDs.pulsarAttachment"), listOf(AttachmentPoint.ARM_FRONT), Tier.REINFORCED),"pulsar_attachment.png").setMaxStackSize(1);
    public static final Item extendedEnergyPack = ItemHelper.createItem(MOD_ID,new ItemTieredAttachment("reinforced.attachment.extendedEnergyPack",config.getInt("ItemIDs.extendedEnergyPack"), listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED),"extended_energy_pack.png").setMaxStackSize(1);
        public static final Item crystalWings = ItemHelper.createItem(MOD_ID,new ItemWingsAttachment("reinforced.attachment.wings",config.getInt("ItemIDs.crystalWings"), listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED),"wings.png").setMaxStackSize(1);

    public static final SuitBaseAbility testAbility = new TestingAbility();
    public static final SuitBaseEffectAbility testEffectAbility = new TestingEffectAbility();
    //public static final SuitBaseEffectAbility clockworkAbility = new ClockworkAbility();
    public static final SuitBaseAbility boostAbility = new BoostAbility();
    public static final SuitBaseAbility projectileAbility = new ProjectileAbility();

    //public static final Item testingAbilityContainer = ItemHelper.createItem(MOD_ID,new ItemWithAbility(config.getInt("ItemIDs.testingAbilityContainer"),testEffectAbility),"testingAbilityItem","testingability.png");
    //public static final Item clockworkAbilityContainer = ItemHelper.createItem(MOD_ID,new ItemWithAbility(config.getInt("ItemIDs.clockworkAbilityContainer"),clockworkAbility),"clockworkAbilityContainer","ability12.png");
    public static final Item boostAbilityContainer = ItemHelper.createItem(MOD_ID,new ItemWithAbility("boostAbilityContainer",config.getInt("ItemIDs.boostAbilityContainer"),boostAbility),"ability2.png");
    public static final Item projectileAbilityContainer = ItemHelper.createItem(MOD_ID,new ItemWithAbility("projectileAbilityContainer",config.getInt("ItemIDs.projectileAbilityContainer"),projectileAbility),"ability1.png");

    public static final Item abilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("abilityModule",config.getInt("ItemIDs.abilityModule"),Mode.NORMAL),"abilitymodule.png");
    /*public static final Item normalAbilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("",config.getInt("ItemIDs.normalAbilityModule"),Mode.NORMAL),"normalAbilityModule","normalmodule.png");
    public static final Item attackAbilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("",config.getInt("ItemIDs.attackAbilityModule"),Mode.ATTACK),"attackAbilityModule","attackmodule.png");
    public static final Item defenseAbilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("",config.getInt("ItemIDs.defenseAbilityModule"),Mode.DEFENSE),"defenseAbilityModule","defensemodule.png");
    public static final Item pursuitAbilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("",config.getInt("ItemIDs.pursuitAbilityModule"),Mode.PURSUIT),"pursuitAbilityModule","pursuitmodule.png");*/
    public static final Item awakenedAbilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("awakenedAbilityModule",config.getInt("ItemIDs.awakenedAbilityModule"),Mode.AWAKENED),"awakenedmodule.png");

    //public static final Item crystalChip = ItemHelper.createItem(MOD_ID,new Item(config.getInt("ItemIDs.crystalChip")),"crystalChip","crystal_chip.png");
    public static final Item crystalChip = simpleItem("crystalChip","crystal_chip.png");
    public static final Item pureCrystalChip = simpleItem("pureCrystalChip","pure_crystal_chip.png");
    public static final Item basicEnergyCore = simpleItem("basicEnergyCore","basic_energy_core.png");
    public static final Item reinforcedEnergyCore = simpleItem("reinforcedEnergyCore","reinforced_energy_core.png");
    //public static final Item awakenedEnergyCore = simpleItem("awakenedEnergyCore","awakened_energy_core.png");
    public static final Item basicDrillBit = simpleItem("basicDrillBit","basic_drill_bit.png");
    public static final Item reinforcedDrillBit = simpleItem("reinforcedDrillBit","reinforced_drill_bit.png");
    public static final Item basicDrillCasing = simpleItem("basicDrillCasing","basic_drill_casing.png");
    public static final Item reinforcedDrillCasing = simpleItem("reinforcedDrillCasing","reinforced_drill_casing.png");
    public static final Item pulsarShell = simpleItem("pulsarShell","pulsar_shell.png");
    public static final Item pulsarInnerCore = simpleItem("pulsarInnerCore","pulsar_inner_core.png");
    public static final Item pulsarOuterCore = simpleItem("pulsarOuterCore","pulsar_outer_core.png");
    public static final Item itemManipulationCircuit = simpleItem("itemManipulationCircuit","item_manipulation_circuit.png");
    public static final Item fluidManipulationCircuit = simpleItem("fluidManipulationCircuit","fluid_manipulation_circuit.png");
    public static final Item dilithiumControlCore = simpleItem("dilithiumControlCore","dilithium_control_core.png");
    public static final Item warpManipulatorCircuit = simpleItem("warpManipulatorCircuit","warp_manipulator_circuit.png");
    public static final Item dilithiumChip = simpleItem("dilithiumChip","dilithium_chip.png");
    public static final Item dimensionalChip = simpleItem("dimensionalChip","dimensional_chip.png");
    public static final Item attachmentPoint = simpleItem("attachmentPoint","attachment_point.png");
    public static final Item meteorTracker = ItemHelper.createItem(MOD_ID,new ItemMeteorTracker("meteorTracker",config.getInt("ItemIDs.meteorTracker")),"meteor_tracker_uncalibrated.png");
    public static final Item blankAbilityModule = simpleItem("blankAbilityModule","blank_module.png");
    public static final Item abilityContainerCasing = simpleItem("abilityContainerCasing","abilitycontainercasing.png");
    public static final Item blankChip = simpleItem("blankChip","romChip.blank","blank_chip.png");


    public static final int[] energyOrbTex = TextureHelper.getOrCreateItemTexture(MOD_ID,"energyorb.png");

    public static final Weather weatherBloodMoon = new WeatherBloodMoon(10).setLanguageKey("bloodMoon");
    public static final Weather weatherEclipse = new WeatherEclipse(11).setLanguageKey("solarEclipse");
    public static final Weather weatherSolarApocalypse = new WeatherSolarApocalypse(12).setLanguageKey("solarApocalypse");

    public static final AchievementPage ACHIEVEMENTS = new SignalIndustriesAchievementPage();

    public static final Biome biomeEternity = Biomes.register("signalindustries:eternity",new Biome().setFillerBlock(realityFabric.id).setTopBlock(realityFabric.id).setColor(0x808080));
    public static final WorldType eternityWorld = WorldTypes.register("signalindustries:eternity",new WorldTypeEternity(key("eternity")));
    public static final Dimension dimEternity = new Dimension(key("eternity"),Dimension.overworld,1,portalEternity.id).setDefaultWorldType(eternityWorld);

    public static final Multiblock dimAnchorMultiblock = new Multiblock(MOD_ID,new Class[]{SignalIndustries.class},"dimensionalAnchor","dimensionalAnchor",false);
    public static final Multiblock testMultiblock = new Multiblock(MOD_ID,new Class[]{SignalIndustries.class},"test","test",false);
    public static final Multiblock signalumReactor = new Multiblock(MOD_ID,new Class[]{SignalIndustries.class},"signalumReactor","signalumReactor",false);

    public static Map<String, BlockTexture> textures = new HashMap<>();

    @Override
    public void onInitialize() {
        LOGGER.info("Signal Industries initialized.");
    }

    static {
        textures.put(Tier.PROTOTYPE.name()+".extractor.active",new BlockTexture(MOD_ID).setTopAndBottom("prototype_blank.png").setSides("extractor_prototype_side_active.png"));
        textures.put(Tier.BASIC.name()+".extractor.active",new BlockTexture(MOD_ID).setTopAndBottom("basic_blank.png").setSides("extractor_basic_side_active.png"));

        textures.put(Tier.PROTOTYPE.name()+".crusher.active",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setTopTexture("crusher_prototype_top_active.png").setNorthTexture("crusher_prototype_side.png"));
        textures.put(Tier.BASIC.name()+".crusher.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setTopTexture("crusher_basic_top_active.png").setNorthTexture("crusher_basic_side.png"));

        textures.put(Tier.PROTOTYPE.name()+".alloySmelter.active",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setNorthTexture("alloy_smelter_prototype_active.png"));
        textures.put(Tier.BASIC.name()+".alloySmelter.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setNorthTexture("alloy_smelter_basic_active.png"));

        textures.put(Tier.PROTOTYPE.name()+".plateFormer.active",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setNorthTexture("plate_former_prototype_active.png"));

        textures.put(Tier.PROTOTYPE.name()+".crystalCutter.active",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setNorthTexture("crystal_cutter_prototype_active.png"));
        textures.put(Tier.BASIC.name()+".crystalCutter.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setNorthTexture("crystal_cutter_basic_active.png"));

        textures.put(Tier.BASIC.name()+".crystalChamber.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setNorthTexture("basic_crystal_chamber_side_active.png"));

        textures.put(Tier.BASIC.name()+".infuser.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setSides("infuser_basic_side_active.png"));

        textures.put(Tier.BASIC.name()+".wrathBeacon.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setSides("wrath_beacon_active.png"));

        textures.put(Tier.PROTOTYPE.name()+".pump.active",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setSides("prototype_pump_side.png").setTopTexture("prototype_pump_top.png"));

        textures.put("dimensionalAnchor.active",new BlockTexture(MOD_ID).setSides("dimensional_anchor.png").setBottomTexture("dimensional_anchor_bottom.png").setTopTexture("dimensional_anchor_top.png"));

        textures.put("dilithiumStabilizer.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setSides("dilithium_stabilizer_side_active.png").setNorthTexture("dilithium_top_active.png"));
        textures.put("dilithiumStabilizer.vertical",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setSides("dilithium_stabilizer_side_inactive.png").setTopTexture("dilithium_top_inactive.png"));
        textures.put("dilithiumStabilizer.vertical.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setSides("dilithium_stabilizer_side_active.png").setTopTexture("dilithium_top_active.png"));

        textures.put("dilithiumBooster.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setSides("dilithium_booster_side_active.png").setNorthTexture("dilithium_top_active.png"));

        textures.put(Tier.BASIC.name()+".externalIo",new BlockTexture(MOD_ID).setAll("external_io_blank.png").setTopTexture("external_io_input.png").setBottomTexture("external_io_output.png").setNorthTexture("external_io_both.png"));

        textures.put(Tier.REINFORCED.name()+".centrifuge.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setTopTexture("reinforced_centrifuge_closed.png").setNorthTexture("reinforced_centrifuge_front_active.png"));
        textures.put(Tier.REINFORCED.name()+".centrifuge.loaded",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setTopTexture("reinforced_centrifuge_loaded.png").setNorthTexture("reinforced_centrifuge_front_inactive.png"));

        textures.put(Tier.REINFORCED.name()+".ignitor.inverted",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_inactive_inverted.png").setTopTexture("reinforced_ignitor_bottom_inactive.png").setBottomTexture("reinforced_ignitor_top_inactive.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.active",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_active.png").setTopTexture("reinforced_ignitor_top_active.png").setBottomTexture("reinforced_ignitor_bottom_active.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.inverted.active",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_active_inverted.png").setTopTexture("reinforced_ignitor_bottom_active.png").setBottomTexture("reinforced_ignitor_top_active.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.ready",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_ready.png").setTopTexture("reinforced_ignitor_top_ready.png").setBottomTexture("reinforced_ignitor_bottom_ready.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.inverted.ready",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_ready_inverted.png").setTopTexture("reinforced_ignitor_bottom_ready.png").setBottomTexture("reinforced_ignitor_top_ready.png"));

        Dimension.registerDimension(config.getInt("Other.eternityDimId"),dimEternity);
    }

    public SignalIndustries(){
        //RecipeFIleLoader.load("/assets/signalindustries/recipes/recipes.txt",mapOf(new String[]{"SignalIndustries"},new String[]{"sunsetsatellite.signalindustries.SignalIndustries"}));
        BlockModelDispatcher.getInstance().addDispatch(dilithiumRail,new BlockModelRenderBlocks(9));
        BlockModelDispatcher.getInstance().addDispatch(energyStill,new BlockModelRenderBlocks(4));
        BlockModelDispatcher.getInstance().addDispatch(energyFlowing,new BlockModelRenderBlocks(4));
        BlockModelDispatcher.getInstance().addDispatch(burntSignalumFlowing,new BlockModelRenderBlocks(4));
        BlockModelDispatcher.getInstance().addDispatch(burntSignalumStill,new BlockModelRenderBlocks(4));
        //PacketAccessor.callAddIdClassMapping(config.getInt("Other.machinePacketId"),true,false, PacketOpenMachineGUI.class);

        List<Field> fields = new ArrayList<>(Arrays.asList(SignalIndustries.class.getDeclaredFields()));
        fields.removeIf((F)->F.getType() != Block.class);

        for (Field field : fields) {
            try {
                Block block = (Block) field.get(null);
                ItemToolPickaxe.miningLevels.put(block,3);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        ItemToolPickaxe.miningLevels.put(prototypeMachineCore,3);
        ItemToolPickaxe.miningLevels.put(basicMachineCore,3);
        ItemToolPickaxe.miningLevels.put(reinforcedMachineCore,3);
        ItemToolPickaxe.miningLevels.put(awakenedMachineCore,3);
        ItemToolPickaxe.miningLevels.put(signalumOre,3);
        ItemToolPickaxe.miningLevels.put(dilithiumBlock,4);
        ItemToolPickaxe.miningLevels.put(dilithiumCrystalBlock,4);
        ItemToolPickaxe.miningLevels.put(emptyCrystalBlock,3);
        ItemToolPickaxe.miningLevels.put(rawCrystalBlock,3);
        ItemToolPickaxe.miningLevels.put(awakenedSignalumCrystalBlock,4);
        ItemToolPickaxe.miningLevels.put(rootedFabric,4);
        ItemToolPickaxe.miningLevels.put(dimensionalShardOre,4);
        ItemToolPickaxe.miningLevels.put(dilithiumOre,4);
        ItemToolPickaxe.miningLevels.put(realityFabric,5);
        ItemToolPickaxe.miningLevels.put(reinforcedGlass,3);
        ItemToolPickaxe.miningLevels.put(reinforcedCasing,3);



        ironPlateHammer.setContainerItem(ironPlateHammer);

        CommandHelper.createCommand(new NBTEditCommand());
        CommandHelper.createCommand(new StructureCommand("structure","struct"));
        EntityHelper.Core.createSpecialTileEntity(TileEntityConduit.class, new RenderFluidInConduit(),"Conduit");
        EntityHelper.Core.createSpecialTileEntity(TileEntityFluidConduit.class, new RenderFluidInConduit(),"Fluid Conduit");
        EntityHelper.Core.createEntity(EntityCrystal.class,47,"signalumCrystal");
        EntityHelper.Client.assignEntityRenderer(EntityCrystal.class,new SnowballRenderer(signalumCrystal.getIconFromDamage(0)));
        EntityHelper.Core.createEntity(EntityEnergyOrb.class,49,"energyOrb");
        EntityHelper.Client.assignEntityRenderer(EntityEnergyOrb.class,new SnowballRenderer(Block.texCoordToIndex(energyOrbTex[0],energyOrbTex[1])));

        EntityHelper.Core.createSpecialTileEntity(TileEntityEnergyCell.class,new RenderFluidInBlock(),"Energy Cell");
        addToNameGuiMap("Energy Cell", GuiEnergyCell.class, TileEntityEnergyCell.class);

        EntityHelper.Core.createSpecialTileEntity(TileEntitySIFluidTank.class,new RenderFluidInBlock(),"SI Fluid Tank");
        addToNameGuiMap("SI Fluid Tank", GuiSIFluidTank.class, TileEntitySIFluidTank.class);

        EntityHelper.Core.createTileEntity(TileEntityExtractor.class,"Extractor");
        addToNameGuiMap("Extractor", GuiExtractor.class, TileEntityExtractor.class);

        EntityHelper.Core.createTileEntity(TileEntityCrusher.class,"Crusher");
        addToNameGuiMap("Crusher", GuiCrusher.class, TileEntityCrusher.class);

        EntityHelper.Core.createTileEntity(TileEntityAlloySmelter.class,"Alloy Smelter");
        addToNameGuiMap("Alloy Smelter", GuiAlloySmelter.class, TileEntityAlloySmelter.class);

        EntityHelper.Core.createTileEntity(TileEntityPlateFormer.class,"Plate Former");
        addToNameGuiMap("Plate Former", GuiPlateFormer.class, TileEntityPlateFormer.class);

        EntityHelper.Core.createTileEntity(TileEntityCrystalCutter.class,"Crystal Cutter");
        addToNameGuiMap("Crystal Cutter", GuiCrystalCutter.class, TileEntityCrystalCutter.class);

        EntityHelper.Core.createTileEntity(TileEntityInfuser.class,"Infuser");
        addToNameGuiMap("Infuser", GuiInfuser.class, TileEntityInfuser.class);

        EntityHelper.Core.createTileEntity(TileEntityBooster.class,"Dilithium Booster");
        addToNameGuiMap("Dilithium Booster", GuiBooster.class, TileEntityBooster.class);

        EntityHelper.Core.createTileEntity(TileEntityStabilizer.class,"Dilithium Stabilizer");
        addToNameGuiMap("Dilithium Stabilizer", GuiStabilizer.class, TileEntityStabilizer.class);

        EntityHelper.Core.createTileEntity(TileEntityCrystalChamber.class,"Crystal Chamber");
        addToNameGuiMap("Crystal Chamber", GuiCrystalChamber.class, TileEntityCrystalChamber.class);

        EntityHelper.Core.createTileEntity(TileEntityPump.class,"Pump");
        addToNameGuiMap("Pump", GuiPump.class, TileEntityCrystalChamber.class);

        EntityHelper.Core.createSpecialTileEntity(TileEntityDimensionalAnchor.class,new RenderMultiblock(),"Dimensional Anchor");
        addToNameGuiMap("Dimensional Anchor", GuiDimAnchor.class, TileEntityDimensionalAnchor.class);

        EntityHelper.Core.createSpecialTileEntity(TileEntityAutoMiner.class, new RenderAutoMiner(),"Automatic Miner");
        addToNameGuiMap("Automatic Miner", GuiAutoMiner.class, TileEntityAutoMiner.class);

        EntityHelper.Core.createTileEntity(TileEntityExternalIO.class,"External I/O");
        addToNameGuiMap("External I/O", GuiExternalIO.class, TileEntityExternalIO.class);

        EntityHelper.Core.createTileEntity(TileEntityCentrifuge.class,"Separation Centrifuge");
        addToNameGuiMap("Separation Centrifuge", GuiCentrifuge.class, TileEntityCentrifuge.class);

        EntityHelper.Core.createSpecialTileEntity(TileEntitySignalumReactor.class,new RenderSignalumReactor(),"Signalum Reactor");
        addToNameGuiMap("Signalum Reactor", GuiSignalumReactor.class, TileEntitySignalumReactor.class);

        EntityHelper.Core.createTileEntity(TileEntityEnergyConnector.class,"Energy Connector");
        addToNameGuiMap("Energy Connector", GuiEnergyConnector.class, TileEntityEnergyConnector.class);

        EntityHelper.Core.createTileEntity(TileEntityItemBus.class,"Item Bus");
        addToNameGuiMap("Item Bus", GuiItemBus.class, TileEntityItemBus.class);

        EntityHelper.Core.createTileEntity(TileEntityFluidHatch.class,"Fluid Hatch");
        addToNameGuiMap("Fluid Hatch", GuiItemBus.class, TileEntityFluidHatch.class);

        EntityHelper.Core.createTileEntity(TileEntityIgnitor.class,"Signalum Ignitor");

        EntityHelper.Core.createSpecialTileEntity(TileEntityEnergyInjector.class,new RenderEnergyInjector(),"Energy Injector");
        addToNameGuiMap("Energy Injector",GuiEnergyInjector.class,TileEntityEnergyInjector.class);

        EntityHelper.Core.createTileEntity(TileEntitySignalumDynamo.class,"Signalum Dynamo");
        addToNameGuiMap("Signalum Dynamo", GuiSignalumDynamo.class,TileEntitySignalumDynamo.class);

        EntityHelper.Core.createTileEntity(TileEntityProgrammer.class,"EEPROM Programmer");
        addToNameGuiMap("EEPROM Programmer", GuiProgrammer.class,TileEntityProgrammer.class);

        addToNameGuiMap("The Pulsar", GuiPulsar.class, InventoryPulsar.class);
        addToNameGuiMap("Signalum Prototype Harness", GuiHarness.class, InventoryHarness.class);

        EntityHelper.Core.createTileEntity(TileEntityRecipeMaker.class,"Recipe Maker");
        EntityHelper.Core.createTileEntity(TileEntityWrathBeacon.class,"Wrath Beacon");
        EntityHelper.Core.createTileEntity(TileEntityBlockBreaker.class,"Block Breaker");

        Multiblock.multiblocks.put("dimensionalAnchor",dimAnchorMultiblock);
        Multiblock.multiblocks.put("test",testMultiblock);
        Multiblock.multiblocks.put("signalumReactor",signalumReactor);
        SignalIndustries.LOGGER.info(String.format("Loaded %d multiblocks..",Multiblock.multiblocks.size()));
        SignalIndustries.LOGGER.info(String.format("Loaded %d internal structures.", Structure.internalStructures.size()));

        EntityHelper.Core.createEntity(EntityInfernal.class,config.getInt("EntityIDs.infernalId"),"Infernal");
        EntityHelper.Client.assignEntityRenderer(EntityInfernal.class,new MobRenderer<EntityInfernal>(new ModelZombie(),0.5F));
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

    public static Item simpleItem(String name, String texture){
        return ItemHelper.createItem(MOD_ID,new Item(name,config.getInt("ItemIDs."+name)),texture);
    }

    public static Item simpleItem(String name, String lang, String texture){
        return ItemHelper.createItem(MOD_ID,new Item(lang,config.getInt("ItemIDs."+name)),texture);
    }


    public static void displayGui(EntityPlayer entityplayer, GuiScreen guiScreen, Container container, IInventory tile, int x, int y, int z) {
        if(entityplayer instanceof EntityPlayerMP) {
            ((IEntityPlayerMP)entityplayer).displayGuiScreen_si(guiScreen,container,tile,x,y,z);
        } else {
            Minecraft.getMinecraft(Minecraft.class).displayGuiScreen(guiScreen);
        }
    }

    public static void displayGui(EntityPlayer entityplayer, GuiScreen guiScreen, TileEntityWithName tile, int x, int y, int z) {
        if(entityplayer instanceof EntityPlayerMP) {
            ((IEntityPlayerMP)entityplayer).displayGuiScreen_si(guiScreen,tile,x,y,z);
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

    public static void addToNameGuiMap(String name, Class<? extends Gui> guiClass, Class<?> tileEntityClass){
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

    public static String key(String key){
        return HalpLibe.addModId(MOD_ID,key);
    }

    public static void usePortal(int dim) {
        Minecraft mc = Minecraft.getMinecraft(Minecraft.class);
        Dimension lastDim = Dimension.getDimensionList().get(mc.thePlayer.dimension);
        Dimension newDim = Dimension.getDimensionList().get(dim);
        System.out.println("Switching to dimension \"" + newDim.getTranslatedName() + "\"!!");
        mc.thePlayer.dimension = dim;
        mc.theWorld.setEntityDead(mc.thePlayer);
        mc.thePlayer.removed = false;
        double d = mc.thePlayer.x;
        double d1 = mc.thePlayer.z;
        double newY = mc.thePlayer.y;
        d *= Dimension.getCoordScale(lastDim, newDim);
        d1 *= Dimension.getCoordScale(lastDim, newDim);
        mc.thePlayer.moveTo(d, newY, d1, mc.thePlayer.yRot, mc.thePlayer.xRot);
        if (mc.thePlayer.isAlive()) {
            mc.theWorld.updateEntityWithOptionalForce(mc.thePlayer, false);
        }

        World world;
        world = new World(mc.theWorld, newDim);
        if (newDim == lastDim.homeDim) {
            mc.changeWorld(world, "Leaving " + lastDim.getTranslatedName(), mc.thePlayer);
        } else {
            mc.changeWorld(world, "Entering " + newDim.getTranslatedName(), mc.thePlayer);
        }

        mc.thePlayer.world = mc.theWorld;
        if (mc.thePlayer.isAlive()) {
            mc.thePlayer.moveTo(d, newY, d1, mc.thePlayer.yRot, mc.thePlayer.xRot);
            mc.theWorld.updateEntityWithOptionalForce(mc.thePlayer, false);
        }
    }

    @Override
    public void beforeGameStart() {

    }

    @Override
    public void afterGameStart() {
        AchievementHelper.addPage(ACHIEVEMENTS);
        OptionsCategory category = new OptionsCategory("gui.options.page.controls.category.signalindustries");
        category
                .withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyOpenSuit()))
                .withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyActivateAbility()))
                .withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeySwitchMode()))
                .withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyActivateHeadTopAttachment()))
                .withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyActivateArmBackLAttachment()))
                .withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyActivateArmBackRAttachment()))
                .withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyActivateArmFrontLAttachment()))
                .withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyActivateArmFrontRAttachment()))
                .withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyActivateArmSideLAttachment()))
                .withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyActivateArmSideRAttachment()))
                .withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyActivateCoreBackAttachment()))
                .withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyActivateLegSideLAttachment()))
                .withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyActivateLegSideRAttachment()))
                .withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyActivateBootBackLAttachment()))
                .withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyActivateBootBackRAttachment()));
        OptionsPages.CONTROLS
                .withComponent(category);
    }
}
