package sunsetsatellite.signalindustries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.fx.EntityFX;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.SnowballRenderer;
import net.minecraft.client.render.model.ModelZombie;
import net.minecraft.core.Global;
import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.sound.BlockSounds;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.type.WorldTypes;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.NBTEditCommand;
import sunsetsatellite.catalyst.fluids.mp.packets.PacketSetFluidSlot;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.RenderMultiblock;
import sunsetsatellite.catalyst.multiblocks.Structure;
import sunsetsatellite.catalyst.multiblocks.StructureCommand;
import sunsetsatellite.signalindustries.abilities.powersuit.*;
import sunsetsatellite.signalindustries.blocks.*;
import sunsetsatellite.signalindustries.blocks.base.BlockConnectedTextureCursed;
import sunsetsatellite.signalindustries.blocks.base.BlockTiered;
import sunsetsatellite.signalindustries.blocks.base.BlockUndroppable;
import sunsetsatellite.signalindustries.blocks.machines.*;
import sunsetsatellite.signalindustries.blocks.states.ConduitStateInterpreter;
import sunsetsatellite.signalindustries.blocks.states.EEPROMProgrammerStateInterpreter;
import sunsetsatellite.signalindustries.blocks.states.ItemConduitStateInterpreter;
import sunsetsatellite.signalindustries.commands.RecipeReloadCommand;
import sunsetsatellite.signalindustries.dim.WorldTypeEternity;
import sunsetsatellite.signalindustries.entities.EntityCrystal;
import sunsetsatellite.signalindustries.entities.EntityEnergyOrb;
import sunsetsatellite.signalindustries.entities.EntityFallingMeteor;
import sunsetsatellite.signalindustries.entities.EntitySunbeam;
import sunsetsatellite.signalindustries.entities.mob.EntityInfernal;
import sunsetsatellite.signalindustries.gui.*;
import sunsetsatellite.signalindustries.interfaces.mixins.IEntityPlayerMP;
import sunsetsatellite.signalindustries.interfaces.mixins.IKeybinds;
import sunsetsatellite.signalindustries.inventories.*;
import sunsetsatellite.signalindustries.inventories.base.TileEntityWithName;
import sunsetsatellite.signalindustries.inventories.item.InventoryBackpack;
import sunsetsatellite.signalindustries.inventories.item.InventoryHarness;
import sunsetsatellite.signalindustries.inventories.item.InventoryPulsar;
import sunsetsatellite.signalindustries.inventories.machines.*;
import sunsetsatellite.signalindustries.items.*;
import sunsetsatellite.signalindustries.items.applications.ItemWithAbility;
import sunsetsatellite.signalindustries.items.attachments.*;
import sunsetsatellite.signalindustries.items.containers.*;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;
import sunsetsatellite.signalindustries.mp.packets.PacketOpenMachineGUI;
import sunsetsatellite.signalindustries.render.*;
import sunsetsatellite.signalindustries.util.*;
import sunsetsatellite.signalindustries.weather.WeatherBloodMoon;
import sunsetsatellite.signalindustries.weather.WeatherEclipse;
import sunsetsatellite.signalindustries.weather.WeatherMeteorShower;
import sunsetsatellite.signalindustries.weather.WeatherSolarApocalypse;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.*;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.achievements.AchievementPage;
import turniplabs.halplibe.util.toml.Toml;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.model.block.BlockModelDragonFly;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SignalIndustries implements ModInitializer, GameStartEntrypoint, ClientStartEntrypoint {

    private static final int blockIdStart = 1200;
    private static final int itemIdStart = 17100;

    public static final String MOD_ID = "signalindustries";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final TomlConfigHandler config;
    public static List<ChunkCoordinates> meteorLocations = new ArrayList<>();
    public static Set<BlockInstance> uvLamps = new HashSet<>();

    static {
        List<Field> blockFields = Arrays.stream(SignalIndustries.class.getDeclaredFields()).filter((F)->Block.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
        List<Field> itemFields = Arrays.stream(SignalIndustries.class.getDeclaredFields()).filter((F)->Item.class.isAssignableFrom(F.getType())).collect(Collectors.toList());

        Toml defaultConfig = new Toml("Signal Industries configuration file.");
        defaultConfig.addCategory("BlockIDs");
        defaultConfig.addCategory("ItemIDs");
        defaultConfig.addCategory("EntityIDs");
        defaultConfig.addCategory("Other");
        defaultConfig.addEntry("Other.eternityDimId",3);
        defaultConfig.addEntry("Other.GuiId",10);
        defaultConfig.addEntry("Other.machinePacketId",113);
        defaultConfig.addEntry("EntityIDs.infernalId",100);

        int blockId = blockIdStart;
        int itemId = itemIdStart;
        for (Field blockField : blockFields) {
            defaultConfig.addEntry("BlockIDs."+blockField.getName(),blockId++);
        }
        for (Field itemField : itemFields) {
            defaultConfig.addEntry("ItemIDs."+itemField.getName(),itemId++);
        }


        config = new TomlConfigHandler(MOD_ID,new Toml("Signal Industries configuration file."));

        File configFile = config.getConfigFile();

        if(config.getConfigFile().exists()){
            config.loadConfig();
            config.setDefaults(config.getRawParsed());
            Toml rawConfig = config.getRawParsed();
            int maxBlocks = ((Toml)rawConfig.get(".BlockIDs")).getOrderedKeys().size();
            int maxItems = ((Toml)rawConfig.get(".ItemIDs")).getOrderedKeys().size();
            int newNextBlockId = blockIdStart + maxBlocks;
            int newNextItemId = itemIdStart + maxItems;
            boolean changed = false;

            for (Field F : blockFields) {
                if (!rawConfig.contains("BlockIDs." + F.getName())) {
                    rawConfig.addEntry("BlockIDs." + F.getName(), newNextBlockId++);
                    changed = true;
                }
            }
            for (Field F : itemFields) {
                if (!rawConfig.contains("ItemIDs." + F.getName())) {
                    rawConfig.addEntry("ItemIDs." + F.getName(), newNextItemId++);
                    changed = true;
                }
            }
            if(changed){
                config.setDefaults(rawConfig);
                config.writeConfig();
                config.loadConfig();
            }
        } else {
            config.setDefaults(defaultConfig);
            try {
                //noinspection ResultOfMethodCallIgnored
                configFile.getParentFile().mkdirs();
                //noinspection ResultOfMethodCallIgnored
                configFile.createNewFile();
                config.writeConfig();
                config.loadConfig();
            } catch (IOException e) {
                throw new RuntimeException("Failed to generate config!",e);
            }
        }

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

        TextureHelper.getOrCreateBlockTexture(MOD_ID,"signalum_ore_overlay.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"glowing_obsidian_overlay.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"input_overlay.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"both_io_overlay.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"output_overlay.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"dilithium_rail_overlay.png");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"uv_lamp_overlay.png");

        TextureHelper.getOrCreateParticleTexture(MOD_ID,"meteor_tail.png");
    }
    //public static final Config config = new Config(MOD_ID, mapOf(new String[]{"PacketOpenMachineGUI_ID","PacketPipeItemSpawn_ID","GuiID"},new String[]{"113","114","9"}), new Class[]{SignalIndustries.class});

    public static HashMap<String, ArrayList<Class<?>>> nameToGuiMap = new HashMap<>();

    public static final Tag<Block> ENERGY_CONDUITS_CONNECT = Tag.of("energy_conduits_connect");
    public static final Tag<Block> FLUID_CONDUITS_CONNECT = Tag.of("fluid_conduits_connect");
    public static final Tag<Block> ITEM_CONDUITS_CONNECT = Tag.of("item_conduits_connect");

    public static final Block signalumOre = new BlockBuilder(MOD_ID)
            .setTextures("signalum_ore.png")
            .setLuminance(3)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(3)
            .setResistance(25)
            .build(new BlockOreSignalum("signalumOre",config.getInt("BlockIDs.signalumOre")).withTags(BlockTags.MINEABLE_BY_PICKAXE).withOverbright());

    public static final Block dilithiumOre = new BlockBuilder(MOD_ID)
            .setTextures("dilithium_ore.png")
            .setLuminance(3)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(75)
            .setResistance(100)
            .build(new BlockOreDilithium("dilithiumOre",config.getInt("BlockIDs.dilithiumOre")).withTags(BlockTags.MINEABLE_BY_PICKAXE));

    public static final Block dimensionalShardOre = new BlockBuilder(MOD_ID)
            .setTextures("dimensional_shard_ore.png")
            .setLuminance(3)
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
            .setLuminance(15).setBlockSound(BlockSounds.GLASS)
            .setHardness(24).setResistance(50000)
            .build(new Block("rawCrystalBlock",config.getInt("BlockIDs.rawCrystalBlock"),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
    public static final Block awakenedSignalumCrystalBlock = new BlockBuilder(MOD_ID)
            .setTextures("awakened_crystal_block.png")
            .setLuminance(15)
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

    public static final Block basicCasing = new BlockBuilder(MOD_ID)
            .setTextures("basic_casing.png")
            .setLuminance(0)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(10)
            .setResistance(2000)
            .build(new Block("basic.casing",config.getInt("BlockIDs.basicCasing"),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
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
            .setBlockModel(
                    new BlockModelDragonFly(
                            ModelHelper.getOrCreateBlockModel(MOD_ID,"conduit/energy/prototype/conduit_all.json"),
                            ModelHelper.getOrCreateBlockState(MOD_ID,"prototype_conduit.json"),
                            new ConduitStateInterpreter(),
                            true
                    )
            )
            .build(new BlockConduit("prototype.conduit",config.getInt("BlockIDs.prototypeConduit"),Tier.PROTOTYPE,Material.glass));

    public static final Block basicConduit = new BlockBuilder(MOD_ID)
            .setTextures("conduit_basic.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    new BlockModelDragonFly(
                        ModelHelper.getOrCreateBlockModel(MOD_ID,"conduit/energy/basic/conduit_all.json"),
                        ModelHelper.getOrCreateBlockState(MOD_ID,"basic_conduit.json"),
                        new ConduitStateInterpreter(),
                        true
                    )
            )
            .build(new BlockConduit("basic.conduit",config.getInt("BlockIDs.basicConduit"),Tier.BASIC,Material.glass));

    public static final Block reinforcedConduit = new BlockBuilder(MOD_ID)
            .setTextures("conduit_reinforced.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    new BlockModelDragonFly(
                            ModelHelper.getOrCreateBlockModel(MOD_ID,"conduit/energy/reinforced/conduit_all.json"),
                            ModelHelper.getOrCreateBlockState(MOD_ID,"reinforced_conduit.json"),
                            new ConduitStateInterpreter(),
                            true
                    )
            )
            .build(new BlockConduit("reinforced.conduit",config.getInt("BlockIDs.reinforcedConduit"),Tier.REINFORCED,Material.glass));

    public static final Block awakenedConduit = new BlockBuilder(MOD_ID)
            .setTextures("conduit_awakened.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    new BlockModelDragonFly(
                            ModelHelper.getOrCreateBlockModel(MOD_ID,"conduit/energy/awakened/conduit_all.json"),
                            ModelHelper.getOrCreateBlockState(MOD_ID,"awakened_conduit.json"),
                            new ConduitStateInterpreter(),
                            true
                    )
            )
            .build(new BlockConduit("awakened.conduit",config.getInt("BlockIDs.awakenedConduit"),Tier.AWAKENED,Material.glass));


    public static final Block prototypeFluidConduit = new BlockBuilder(MOD_ID)
            .setTextures("fluid_pipe_prototype.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    new BlockModelDragonFly(
                            ModelHelper.getOrCreateBlockModel(MOD_ID,"conduit/fluid/prototype/conduit_all.json"),
                            ModelHelper.getOrCreateBlockState(MOD_ID,"prototype_fluid_conduit.json"),
                            new ConduitStateInterpreter(),
                            true
                    )
            )
            .build(new BlockFluidConduit("prototype.conduit.fluid",config.getInt("BlockIDs.prototypeFluidConduit"),Tier.PROTOTYPE,Material.glass));

    public static final Block basicFluidConduit = new BlockBuilder(MOD_ID)
            .setTextures("fluid_pipe_basic.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    new BlockModelDragonFly(
                            ModelHelper.getOrCreateBlockModel(MOD_ID,"conduit/fluid/basic/conduit_all.json"),
                            ModelHelper.getOrCreateBlockState(MOD_ID,"basic_fluid_conduit.json"),
                            new ConduitStateInterpreter(),
                            true
                    )
            )
            .build(new BlockFluidConduit("basic.conduit.fluid",config.getInt("BlockIDs.basicFluidConduit"),Tier.BASIC,Material.glass));

    public static final Block reinforcedFluidConduit = new BlockBuilder(MOD_ID)
            .setTextures("fluid_pipe_reinforced.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    new BlockModelDragonFly(
                            ModelHelper.getOrCreateBlockModel(MOD_ID,"conduit/fluid/reinforced/conduit_all.json"),
                            ModelHelper.getOrCreateBlockState(MOD_ID,"reinforced_fluid_conduit.json"),
                            new ConduitStateInterpreter(),
                            true
                    )
            )
            .build(new BlockFluidConduit("reinforced.conduit.fluid",config.getInt("BlockIDs.reinforcedFluidConduit"),Tier.REINFORCED,Material.glass));

    public static final Block prototypeItemConduit = new BlockBuilder(MOD_ID)
            .setTextures("item_conduit_prototype.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    new BlockModelDragonFly(
                            ModelHelper.getOrCreateBlockModel(MOD_ID,"conduit/item/prototype/conduit_all.json"),
                            ModelHelper.getOrCreateBlockState(MOD_ID,"prototype_item_conduit.json"),
                            new ItemConduitStateInterpreter(),
                            true
                    )
            )
            .build(new BlockItemConduit("prototype.conduit.item",config.getInt("BlockIDs.prototypeItemConduit"),Tier.PROTOTYPE,Material.glass, PipeType.NORMAL));

    public static final Block basicItemConduit = new BlockBuilder(MOD_ID)
            .setTextures("item_conduit_basic.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    new BlockModelDragonFly(
                            ModelHelper.getOrCreateBlockModel(MOD_ID,"conduit/item/basic/conduit_all.json"),
                            ModelHelper.getOrCreateBlockState(MOD_ID,"basic_item_conduit.json"),
                            new ItemConduitStateInterpreter(),
                            true
                    )
            )
            .build(new BlockItemConduit("basic.conduit.item",config.getInt("BlockIDs.basicItemConduit"),Tier.BASIC,Material.glass, PipeType.NORMAL));

    public static final Block basicRestrictItemConduit = new BlockBuilder(MOD_ID)
            .setTextures("item_conduit_basic.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    new BlockModelDragonFly(
                            ModelHelper.getOrCreateBlockModel(MOD_ID,"conduit/item/basic/restrict/conduit_all.json"),
                            ModelHelper.getOrCreateBlockState(MOD_ID,"basic_item_conduit_restrict.json"),
                            new ItemConduitStateInterpreter(),
                            true
                    )
            )
            .build(new BlockItemConduit("basic.conduit.item.restrict",config.getInt("BlockIDs.basicRestrictItemConduit"),Tier.BASIC,Material.glass, PipeType.RESTRICT));

    public static final Block basicSensorItemConduit = new BlockBuilder(MOD_ID)
            .setTextures("item_conduit_basic_sensor_off.png")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    new BlockModelDragonFly(
                            ModelHelper.getOrCreateBlockModel(MOD_ID,"conduit/item/basic/sensor/off/conduit_all.json"),
                            ModelHelper.getOrCreateBlockState(MOD_ID,"basic_item_conduit_sensor.json"),
                            new ItemConduitStateInterpreter(),
                            true
                    )
            )
            .build(new BlockItemConduit("basic.conduit.item.sensor",config.getInt("BlockIDs.basicSensorItemConduit"),Tier.BASIC,Material.glass, PipeType.SENSOR));

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

    public static final Block reinforcedExtractor = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("reinforced_blank.png")
            .setSideTextures("extractor_reinforced_side_empty.png")
            .build(new BlockExtractor("reinforced.extractor",config.getInt("BlockIDs.reinforcedExtractor"),Tier.REINFORCED,Material.metal));

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

    public static final Block reinforcedCrusher = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("reinforced_blank.png")
            .setTopTexture("crusher_reinforced_top_inactive.png")
            .setNorthTexture("crusher_reinforced_side.png")
            .build(new BlockCrusher("reinforced.crusher",config.getInt("BlockIDs.reinforcedCrusher"), Tier.REINFORCED, Material.metal));

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

    public static final Block basicInductionSmelter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setNorthTexture("basic_induction_smelter_front_inactive.png")
            .setTopTexture("basic_induction_smelter_top_inactive.png")
            .build(new BlockInductionSmelter("basic.inductionSmelter",config.getInt("BlockIDs.basicInductionSmelter"), Tier.BASIC,Material.metal));

    public static final Block prototypePlateFormer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("prototype_blank.png")
            .setNorthTexture("plate_former_prototype_inactive.png")
            .build(new BlockPlateFormer("prototype.plateFormer",config.getInt("BlockIDs.prototypePlateFormer"), Tier.PROTOTYPE,Material.stone));

    public static final Block basicPlateFormer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setNorthTexture("plate_former_basic_inactive.png")
            .build(new BlockPlateFormer("basic.plateFormer",config.getInt("BlockIDs.basicPlateFormer"), Tier.BASIC,Material.metal));

    public static final Block reinforcedPlateFormer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("reinforced_blank.png")
            .setNorthTexture("plate_former_reinforced_inactive.png")
            .build(new BlockPlateFormer("reinforced.plateFormer",config.getInt("BlockIDs.reinforcedPlateFormer"), Tier.REINFORCED,Material.metal));

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
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setNorthTexture("crystal_cutter_basic_inactive.png")
            .build(new BlockCrystalCutter("basic.crystalCutter",config.getInt("BlockIDs.basicCrystalCutter"), Tier.BASIC,Material.metal));

    public static final Block reinforcedCrystalCutter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("reinforced_blank.png")
            .setNorthTexture("crystal_cutter_reinforced_inactive.png")
            .build(new BlockCrystalCutter("reinforced.crystalCutter",config.getInt("BlockIDs.reinforcedCrystalCutter"), Tier.REINFORCED, Material.stone));

    public static final Block basicCrystalChamber = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setNorthTexture("basic_crystal_chamber_side_inactive.png")
            .build(new BlockCrystalChamber("basic.crystalChamber",config.getInt("BlockIDs.basicCrystalChamber"), Tier.BASIC,Material.metal));

    public static final Block reinforcedCrystalChamber = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("reinforced_blank.png")
            .setNorthTexture("reinforced_crystal_chamber_side_inactive.png")
            .build(new BlockCrystalChamber("reinforced.crystalChamber",config.getInt("BlockIDs.reinforcedCrystalChamber"), Tier.REINFORCED,Material.metal));

    public static final Block basicInfuser = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setSideTextures("infuser_basic_side_inactive.png")
            .build(new BlockInfuser("basic.infuser",config.getInt("BlockIDs.basicInfuser"), Tier.BASIC,Material.metal));

    public static final Block basicAssembler = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_assembler_side.png")
            .setNorthTexture("basic_assembler_front.png")
            .build(new BlockAssembler("basic.assembler",config.getInt("BlockIDs.basicAssembler"), Tier.BASIC, Material.metal));

    public static final Block prototypeStorageContainer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("prototype_blank.png")
            .setNorthTexture("container_prototype_front.png")
            .build(new BlockStorageContainer("prototype.storageContainer",config.getInt("BlockIDs.prototypeStorageContainer"), Tier.PROTOTYPE, Material.stone));

    public static final Block infiniteStorageContainer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("prototype_blank.png")
            .setNorthTexture("container_prototype_front.png")
            .build(new BlockStorageContainer("infinite.storageContainer",config.getInt("BlockIDs.infiniteStorageContainer"), Tier.INFINITE, Material.stone));

    public static final Block basicStorageContainer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setNorthTexture("container_basic_front.png")
            .build(new BlockStorageContainer("basic.storageContainer",config.getInt("BlockIDs.basicStorageContainer"), Tier.BASIC, Material.metal));

    public static final Block reinforcedStorageContainer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("reinforced_blank.png")
            .setNorthTexture("container_reinforced_front.png")
            .build(new BlockStorageContainer("reinforced.storageContainer",config.getInt("BlockIDs.reinforcedStorageContainer"), Tier.REINFORCED, Material.metal));

    public static final Block basicWrathBeacon = new BlockBuilder(MOD_ID)
            .setHardness(2)
            .setResistance(500)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setSideTextures("wrath_beacon.png")
            .build(new BlockWrathBeacon("basic.wrathBeacon",config.getInt("BlockIDs.basicWrathBeacon"), Tier.BASIC,Material.metal));

    public static final Block reinforcedWrathBeacon = new BlockBuilder(MOD_ID)
            .setHardness(2)
            .setResistance(500)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("reinforced_blank.png")
            .setSideTextures("reinforced_wrath_beacon.png")
            .build(new BlockWrathBeacon("reinforced.wrathBeacon",config.getInt("BlockIDs.reinforcedWrathBeacon"), Tier.REINFORCED,Material.metal));

    //public static final Block awakenedWrathBeacon = new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockWrathBeacon("",config.getInt("BlockIDs.awakenedWrathBeacon"),Tiers.AWAKENED,Material.metal),"awakened.wrathBeacon","reinforced_blank.png","awakened_wrath_beacon_active.png",BlockSounds.METAL,25f,500f,1);

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

    public static final Block redstoneBooster = new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockDilithiumBooster("reinforced.dilithiumBooster",config.getInt("BlockIDs.dilithiumBooster"), Tier.REINFORCED,Material.metal),"reinforced_blank.png","reinforced_blank.png","dilithium_top_inactive.png","dilithium_booster_side_inactive.png","dilithium_booster_side_inactive.png","dilithium_booster_side_inactive.png",BlockSounds.METAL,5f,20f,1);
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setSideTextures("basic_booster_side_inactive.png")
            .setNorthTexture("basic_booster_top_inactive.png")
            .build(new BlockDilithiumBooster("basic.booster",config.getInt("BlockIDs.redstoneBooster"), Tier.BASIC,Material.metal));

    public static final Block dilithiumBooster = new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockDilithiumBooster("reinforced.dilithiumBooster",config.getInt("BlockIDs.dilithiumBooster"), Tier.REINFORCED,Material.metal),"reinforced_blank.png","reinforced_blank.png","dilithium_top_inactive.png","dilithium_booster_side_inactive.png","dilithium_booster_side_inactive.png","dilithium_booster_side_inactive.png",BlockSounds.METAL,5f,20f,1);
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("reinforced_blank.png")
            .setSideTextures("dilithium_booster_side_inactive.png")
            .setNorthTexture("dilithium_top_inactive.png")
            .build(new BlockDilithiumBooster("reinforced.booster",config.getInt("BlockIDs.dilithiumBooster"), Tier.REINFORCED,Material.metal));

    //TODO: W.I.P.
    /*public static final Block awakenedBooster = new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockDilithiumBooster("reinforced.dilithiumBooster",config.getInt("BlockIDs.dilithiumBooster"), Tier.REINFORCED,Material.metal),"reinforced_blank.png","reinforced_blank.png","dilithium_top_inactive.png","dilithium_booster_side_inactive.png","dilithium_booster_side_inactive.png","dilithium_booster_side_inactive.png",BlockSounds.METAL,5f,20f,1);
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("awakened_blank.png")
            .setSideTextures("awakened_booster_side_inactive.png")
            .setNorthTexture("awakened_booster_top_inactive.png")
            .build(new BlockDilithiumBooster("awakened.booster",config.getInt("BlockIDs.awakenedBooster"), Tier.AWAKENED,Material.metal));*/

    public static final Block prototypePump = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("prototype_blank.png")
            .setTopTexture("prototype_pump_top_empty.png")
            .setSideTextures("prototype_pump_side_empty.png")
            .build(new BlockPump("prototype.pump",config.getInt("BlockIDs.prototypePump"), Tier.PROTOTYPE,Material.stone));

    public static final Block basicPump = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setTopTexture("basic_pump_top_empty.png")
            .setSideTextures("basic_pump_side_empty.png")
            .build(new BlockPump("basic.pump",config.getInt("BlockIDs.basicPump"), Tier.BASIC,Material.metal));

    public static final Block prototypeInserter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("prototype_blank.png")
            .setSouthTexture("inserter_output.png")
            .setNorthTexture("inserter_input.png")
            .build(new BlockInserter("prototype.inserter",config.getInt("BlockIDs.prototypeInserter"),Tier.PROTOTYPE,Material.stone));

    public static final Block basicInserter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("basic_blank.png")
            .setSouthTexture("basic_inserter_output.png")
            .setNorthTexture("basic_inserter_input.png")
            .build(new BlockInserter("basic.inserter",config.getInt("BlockIDs.basicInserter"),Tier.BASIC,Material.metal));

    public static final Block prototypeFilter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTopTexture("filter_red.png")
            .setNorthTexture("filter_green.png")
            .setEastTexture("filter_blue.png")
            .setWestTexture("filter_yellow.png")
            .setSouthTexture("filter_magenta.png")
            .setBottomTexture("filter_cyan.png")
            .build(new BlockFilter("prototype.filter",config.getInt("BlockIDs.prototypeFilter"),Tier.PROTOTYPE,Material.stone));

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

    public static final Block reinforcedExternalIo = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setTextures("reinforced_external_io_blank.png")
            .build(new BlockExternalIO("reinforced.externalIO",config.getInt("BlockIDs.reinforcedExternalIo"),Tier.REINFORCED,Material.metal));

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

    public static final Block basicEnergyConnector = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(1)
            .setTextures("basic_energy_connector.png")
            .build(new BlockEnergyConnector("basic.energyConnector",config.getInt("BlockIDs.basicEnergyConnector"),Tier.BASIC,Material.metal));

    public static final Block basicFluidInputHatch = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("basic_fluid_input_hatch.png")
            .build(new BlockFluidInputHatch("basic.fluidInputHatch",config.getInt("BlockIDs.basicFluidInputHatch"),Tier.BASIC,Material.metal));

    public static final Block basicFluidOutputHatch = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("basic_fluid_output_hatch.png")
            .build(new BlockFluidOutputHatch("basic.fluidOutputHatch",config.getInt("BlockIDs.basicFluidOutputHatch"),Tier.BASIC,Material.metal));

    public static final Block basicItemInputBus = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("basic_input_bus.png")
            .build(new BlockInputBus("basic.itemInputBus",config.getInt("BlockIDs.basicItemInputBus"),Tier.BASIC,Material.metal));

    public static final Block basicItemOutputBus = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("basic_output_bus.png")
            .build(new BlockOutputBus("basic.itemOutputBus",config.getInt("BlockIDs.basicItemOutputBus"),Tier.BASIC,Material.metal));
    
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

    public static final Block cobblestoneBricks = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(3)
            .setLuminance(0)
            .setTextures("cobblestone_bricks.png")
            .build(new Block("prototype.bricks",config.getInt("BlockIDs.cobblestoneBricks"),Material.stone));

    public static final Block crystalAlloyBricks = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setLuminance(0)
            .setTextures("crystal_alloy_bricks.png")
            .build(new Block("basic.bricks",config.getInt("BlockIDs.crystalAlloyBricks"),Material.metal));

    public static final Block reinforcedCrystalAlloyBricks = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setLuminance(0)
            .setTextures("reinforced_alloy_bricks.png")
            .build(new Block("reinforced.bricks",config.getInt("BlockIDs.reinforcedCrystalAlloyBricks"),Material.metal));

    public static final Block signalumAlloyCoil = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setLuminance(0)
            .setNorthTexture("signalum_alloy_coil.png")
            .setSouthTexture("signalum_alloy_coil.png")
            .setEastTexture("signalum_alloy_coil_2.png")
            .setWestTexture("signalum_alloy_coil_2.png")
            .setTopBottomTexture("signalum_alloy_coil_top.png")
            .build(new Block("signalumAlloyCoil",config.getInt("BlockIDs.signalumAlloyCoil"),Material.metal));

    public static final Block dilithiumCoil = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setLuminance(0)
            .setSideTextures("dilithium_coil.png")
            .setTopBottomTexture("dilithium_coil_top.png")
            .build(new Block("dilithiumCoil",config.getInt("BlockIDs.dilithiumCoil"),Material.metal));

    public static final Block awakenedAlloyCoil = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setLuminance(0)
            .setNorthTexture("awakened_alloy_coil.png")
            .setSouthTexture("awakened_alloy_coil.png")
            .setEastTexture("awakened_alloy_coil_2.png")
            .setWestTexture("awakened_alloy_coil_2.png")
            .setTopBottomTexture("awakened_alloy_coil_top.png")
            .build(new Block("awakenedAlloyCoil",config.getInt("BlockIDs.awakenedAlloyCoil"),Material.metal));

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
    public static final Item volatileSignalumCrystal = ItemHelper.createItem(MOD_ID,new ItemSignalumCrystalVolatile("volatileSignalumCrystal",config.getInt("ItemIDs.volatileSignalumCrystal")),"volatile_signalum_crystal.png").setMaxStackSize(4);
    public static final Item rawSignalumCrystal = ItemHelper.createItem(MOD_ID,new Item("rawSignalumCrystal",config.getInt("ItemIDs.rawSignalumCrystal")),"rawsignalumcrystal.png");

    public static final Item awakenedSignalumCrystal = ItemHelper.createItem(MOD_ID, new Item("awakenedSignalumCrystal",config.getInt("ItemIDs.awakenedSignalumCrystal")),"awakenedsignalumcrystal.png").setMaxStackSize(1);
    public static final Item awakenedSignalumFragment = ItemHelper.createItem(MOD_ID, new Item("awakenedSignalumFragment",config.getInt("ItemIDs.awakenedSignalumFragment")),"awakenedsignalumfragment.png");

    public static final Item coalDust = ItemHelper.createItem(MOD_ID,new Item("coalDust",config.getInt("ItemIDs.coalDust")),"coaldust.png");
    public static final Item netherCoalDust = ItemHelper.createItem(MOD_ID,new Item("netherCoalDust",config.getInt("ItemIDs.netherCoalDust")),"nethercoaldust.png");
    public static final Item tinyNetherCoalDust = ItemHelper.createItem(MOD_ID,new Item("tinyNetherCoalDust",config.getInt("ItemIDs.tinyNetherCoalDust")),"tiny_nether_coal_dust.png");
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
    public static final Item voidAlloyPlate = ItemHelper.createItem(MOD_ID,new Item("voidAlloyPlate",config.getInt("ItemIDs.voidAlloyPlate")),"void_alloy_plate.png");
    public static final Item awakenedAlloyPlate = ItemHelper.createItem(MOD_ID,new Item("awakenedAlloyPlate",config.getInt("ItemIDs.awakenedAlloyPlate")),"awakened_alloy_plate.png");

    public static final Item crystalAlloyIngot = ItemHelper.createItem(MOD_ID,new Item("crystalAlloyIngot",config.getInt("ItemIDs.crystalAlloyIngot")),"crystalalloy.png");
    public static final Item reinforcedCrystalAlloyIngot = ItemHelper.createItem(MOD_ID,new Item("reinforcedCrystalAlloyIngot",config.getInt("ItemIDs.reinforcedCrystalAlloyIngot")),"reinforcedcrystalalloy.png");
    public static final Item saturatedSignalumAlloyIngot = ItemHelper.createItem(MOD_ID,new Item("saturatedSignalumAlloyIngot",config.getInt("ItemIDs.saturatedSignalumAlloyIngot")),"saturatedsignalumalloy.png");
    public static final Item voidAlloyIngot = ItemHelper.createItem(MOD_ID,new Item("voidAlloyIngot",config.getInt("ItemIDs.voidAlloyIngot")),"void_alloy.png");
    public static final Item awakenedAlloyIngot = ItemHelper.createItem(MOD_ID,new Item("awakenedAlloyIngot",config.getInt("ItemIDs.awakenedAlloyIngot")),"awakened_alloy.png");

    public static final Item diamondCuttingGear = ItemHelper.createItem(MOD_ID,new Item("diamondCuttingGear",config.getInt("ItemIDs.diamondCuttingGear")),"diamondcuttinggear.png");
    public static final Item signalumCuttingGear = ItemHelper.createItem(MOD_ID,new Item("signalumCuttingGear",config.getInt("ItemIDs.signalumCuttingGear")),"signalumcuttinggear.png");

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
            .build(new Block("rootedFabric",config.getInt("BlockIDs.rootedFabric"),Material.stone).withTags(BlockTags.MINEABLE_BY_PICKAXE));

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
            .setLuminance(12)
            .setTopBottomTexture("eternal_tree_log_top.png")
            .setSideTextures("eternal_tree_log.png")
            .setBlockSound(BlockSounds.WOOD)
            .build(new BlockEternalTreeLog("eternalTreeLog",config.getInt("BlockIDs.eternalTreeLog"),Material.wood));//new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockEternalTreeLog(key("eternalTreeLog",config.getInt("BlockIDs.eternalTreeLog"),Material.wood),"eternal_tree_log_top.png","eternal_tree_log.png",BlockSounds.WOOD, 75f,50000f,1);

    public static final Block fueledEternalTreeLog = new BlockBuilder(MOD_ID)
            .setUnbreakable()
            .setResistance(18000000)
            .setLuminance(15)
            .setTopBottomTexture("fueled_eternal_tree_log_top.png")
            .setSideTextures("fueled_eternal_tree_log.png")
            .setBlockSound(BlockSounds.WOOD)
            .build(new BlockEternalTreeLog("fueledEternalTreeLog",config.getInt("BlockIDs.fueledEternalTreeLog"),Material.wood));

    public static final Block glowingObsidian = new BlockBuilder(MOD_ID)
            .setTextures("glowing_obsidian.png")
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(1200)
            .setLuminance(10)
            .build(new BlockGlowingObsidian("glowingObsidian",config.getInt("BlockIDs.glowingObsidian"),Material.stone));
        //BlockHelper.createBlock(MOD_ID,new Block(key("glowingObsidian",config.getInt("BlockIDs.glowingObsidian"),Material.stone),"glowing_obsidian.png",BlockSounds.STONE, 50f,1200f,1.0f/2.0f);

    public static final Block uvLamp = new BlockBuilder(MOD_ID)
            .setTextures("uv_lamp_inactive.png")
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .build(new BlockUVLamp("uvLamp",config.getInt("BlockIDs.uvLamp"),Material.metal));

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

    //TODO: WIP
    //public static final Item sunriseDawn = ItemHelper.createItem(MOD_ID,new ItemSunriseDawn("awakened.sunriseDawn",config.getInt("ItemIDs.sunriseDawn"), ToolMaterial.steel, Tier.AWAKENED),"sunrise_dawn.png").setMaxStackSize(1);
    
    public static final ItemSignalumPowerSuit signalumPowerSuitHelmet = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.helmet",config.getInt("ItemIDs.signalumPowerSuitHelmet"),armorSignalumPowerSuit,0,Tier.REINFORCED),"signalumpowersuit_helmet.png");
    public static final ItemSignalumPowerSuit signalumPowerSuitChestplate = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.chestplate",config.getInt("ItemIDs.signalumPowerSuitChestplate"),armorSignalumPowerSuit,1,Tier.REINFORCED),"signalumpowersuit_chestplate.png");
    public static final ItemSignalumPowerSuit signalumPowerSuitLeggings = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.leggings",config.getInt("ItemIDs.signalumPowerSuitLeggings"),armorSignalumPowerSuit,2,Tier.REINFORCED),"signalumpowersuit_leggings.png");
    public static final ItemSignalumPowerSuit signalumPowerSuitBoots = (ItemSignalumPowerSuit) ItemHelper.createItem(MOD_ID,new ItemSignalumPowerSuit("reinforced.signalumpowersuit.boots",config.getInt("ItemIDs.signalumPowerSuitBoots"),armorSignalumPowerSuit,3,Tier.REINFORCED),"signalumpowersuit_boots.png");

    //public static final Item testingAttachment = ItemHelper.createItem(MOD_ID,new ItemAttachment(config.getInt("ItemIDs.testingAttachment"), listOf(AttachmentPoint.ANY)),"attachment.testingAttachment","energyorb.png");
    //implicit max stack size of 1 (defined in ItemAttachment)
    public static final Item pulsarAttachment = ItemHelper.createItem(MOD_ID,new ItemPulsarAttachment("reinforced.attachment.pulsar",config.getInt("ItemIDs.pulsarAttachment"), listOf(AttachmentPoint.ARM_FRONT), Tier.REINFORCED),"pulsar_attachment.png");
    public static final Item extendedEnergyPack = ItemHelper.createItem(MOD_ID,new ItemTieredAttachment("reinforced.attachment.extendedEnergyPack",config.getInt("ItemIDs.extendedEnergyPack"), listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED),"extended_energy_pack.png");
    public static final Item crystalWings = ItemHelper.createItem(MOD_ID,new ItemWingsAttachment("reinforced.attachment.wings",config.getInt("ItemIDs.crystalWings"), listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED),"wings.png");
    public static final Item basicBackpack = ItemHelper.createItem(MOD_ID,new ItemBackpackAttachment("basic.attachment.backpack",config.getInt("ItemIDs.basicBackpack"), listOf(AttachmentPoint.CORE_BACK), Tier.BASIC),"basic_backpack.png");
    public static final Item reinforcedBackpack = ItemHelper.createItem(MOD_ID,new ItemBackpackAttachment("reinforced.attachment.backpack",config.getInt("ItemIDs.reinforcedBackpack"), listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED),"reinforced_backpack.png");
    public static final Item nightVisionLens = ItemHelper.createItem(MOD_ID,new ItemNVGAttachment("reinforced.attachment.nightVisionLens",config.getInt("ItemIDs.nightVisionLens"), listOf(AttachmentPoint.HEAD_TOP), Tier.REINFORCED),"night_vision_goggles.png");
    public static final Item movementBoosters = ItemHelper.createItem(MOD_ID,new ItemMovementBoostersAttachment("reinforced.attachment.movementBoosters",config.getInt("ItemIDs.movementBoosters"), listOf(AttachmentPoint.BOOT_BACK), Tier.REINFORCED),"movement_boosters.png");

    public static final ItemPortableWorkbench portableWorkbench = (ItemPortableWorkbench) ItemHelper.createItem(MOD_ID,new ItemPortableWorkbench("basic.portableWorkbench",config.getInt("ItemIDs.portableWorkbench"),Tier.BASIC),"portable_workbench.png");
    public static final ItemSmartWatch smartWatch = (ItemSmartWatch) ItemHelper.createItem(MOD_ID,new ItemSmartWatch("basic.smartWatch",config.getInt("ItemIDs.smartWatch"),Tier.BASIC),"smartwatch.png");


    public static final SuitBaseAbility testAbility = new TestingAbility();
    public static final SuitBaseEffectAbility testEffectAbility = new TestingEffectAbility();
    //public static final SuitBaseEffectAbility clockworkAbility = new ClockworkAbility();
    public static final SuitBaseAbility boostAbility = new BoostAbility();
    public static final SuitBaseAbility projectileAbility = new ProjectileAbility();

    //public static final Item testingAbilityContainer = ItemHelper.createItem(MOD_ID,new ItemWithAbility(config.getInt("ItemIDs.testingAbilityContainer"),testEffectAbility),"testingAbilityItem","testingability.png");
    //public static final Item clockworkAbilityContainer = ItemHelper.createItem(MOD_ID,new ItemWithAbility(config.getInt("ItemIDs.clockworkAbilityContainer"),clockworkAbility),"clockworkAbilityContainer","ability12.png");
    public static final Item boostAbilityContainer = ItemHelper.createItem(MOD_ID,new ItemWithAbility("boostAbilityContainer",config.getInt("ItemIDs.boostAbilityContainer"),boostAbility),"ability2.png").setMaxStackSize(1);
    public static final Item projectileAbilityContainer = ItemHelper.createItem(MOD_ID,new ItemWithAbility("projectileAbilityContainer",config.getInt("ItemIDs.projectileAbilityContainer"),projectileAbility),"ability1.png").setMaxStackSize(1);

    public static final Item abilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("abilityModule",config.getInt("ItemIDs.abilityModule"),Tier.REINFORCED),"abilitymodule.png");
    public static final Item awakenedAbilityModule = ItemHelper.createItem(MOD_ID,new ItemAbilityModule("awakenedAbilityModule",config.getInt("ItemIDs.awakenedAbilityModule"),Tier.AWAKENED),"awakenedmodule.png");

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
    public static final Item positionMemoryChip = ItemHelper.createItem(MOD_ID,new ItemPositionChip("romChip.position",config.getInt("ItemIDs.positionMemoryChip")),"position_chip.png");


    public static final int[] energyOrbTex = TextureHelper.getOrCreateItemTexture(MOD_ID,"energyorb.png");

    public static final Weather weatherBloodMoon = new WeatherBloodMoon(10).setLanguageKey("bloodMoon");
    public static final Weather weatherEclipse = new WeatherEclipse(11).setLanguageKey("solarEclipse");
    public static final Weather weatherSolarApocalypse = new WeatherSolarApocalypse(12).setLanguageKey("solarApocalypse");
    public static final Weather weatherMeteorShower = new WeatherMeteorShower(13).setLanguageKey("meteorShower");


    public static final AchievementPage ACHIEVEMENTS = new SignalIndustriesAchievementPage();

    public static final Biome biomeEternity = Biomes.register("signalindustries:eternity",new Biome("eternity").setFillerBlock(realityFabric.id).setTopBlock(realityFabric.id).setColor(0x808080));
    public static final WorldType eternityWorld = WorldTypes.register("signalindustries:eternity",new WorldTypeEternity(key("eternity")));
    public static Dimension dimEternity;

    public static final Multiblock dimAnchorMultiblock = new Multiblock(MOD_ID,new Class[]{SignalIndustries.class},"dimensionalAnchor","dimensionalAnchor",false);
    public static final Multiblock wrathTree = new Multiblock(MOD_ID,new Class[]{SignalIndustries.class},"wrathTree","reinforcedWrathBeacon",false);
    public static final Multiblock signalumReactor = new Multiblock(MOD_ID,new Class[]{SignalIndustries.class},"signalumReactor","signalumReactor",false);
    public static final Multiblock extractionManifold = new Multiblock(MOD_ID,new Class[]{SignalIndustries.class},"reinforcedExtractor","reinforcedExtractor",false);
    public static final Multiblock inductionSmelterBasic = new Multiblock(MOD_ID,new Class[]{SignalIndustries.class},"basicInductionSmelter","basicInductionSmelter",false);

    public static Map<String, BlockTexture> textures = new HashMap<>();

    @Override
    public void onInitialize() {
        LOGGER.info("Signal Industries initialized.");
    }

    static {
        textures.put(Tier.PROTOTYPE.name()+".extractor.active",new BlockTexture(MOD_ID).setTopAndBottom("prototype_blank.png").setSides("extractor_prototype_side_active.png"));
        textures.put(Tier.BASIC.name()+".extractor.active",new BlockTexture(MOD_ID).setTopAndBottom("basic_blank.png").setSides("extractor_basic_side_active.png"));
        textures.put(Tier.REINFORCED.name()+".extractor.active",new BlockTexture(MOD_ID).setTopAndBottom("reinforced_blank.png").setSides("extractor_reinforced_side_active.png"));
        textures.put(Tier.PROTOTYPE.name()+".extractor.active.overlay",new BlockTexture(MOD_ID).setSides("extractor_overlay.png"));
        textures.put(Tier.BASIC.name()+".extractor.active.overlay",new BlockTexture(MOD_ID).setSides("extractor_overlay.png"));
        textures.put(Tier.REINFORCED.name()+".extractor.active.overlay",new BlockTexture(MOD_ID).setSides("extractor_overlay.png"));

        textures.put(Tier.PROTOTYPE.name()+".crusher.active",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setTopTexture("crusher_prototype_top_active.png").setNorthTexture("crusher_prototype_side.png"));
        textures.put(Tier.BASIC.name()+".crusher.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setTopTexture("crusher_basic_top_active.png").setNorthTexture("crusher_basic_side.png"));
        textures.put(Tier.REINFORCED.name()+".crusher.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setTopTexture("crusher_reinforced_top_active.png").setNorthTexture("crusher_reinforced_side.png"));
        textures.put(Tier.PROTOTYPE.name()+".crusher.active.overlay",new BlockTexture(MOD_ID).setTopTexture("crusher_overlay.png"));
        textures.put(Tier.BASIC.name()+".crusher.active.overlay",new BlockTexture(MOD_ID).setTopTexture("crusher_overlay.png"));
        textures.put(Tier.REINFORCED.name()+".crusher.active.overlay",new BlockTexture(MOD_ID).setTopTexture("crusher_overlay.png"));

        textures.put(Tier.PROTOTYPE.name()+".alloySmelter.active",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setNorthTexture("alloy_smelter_prototype_active.png"));
        textures.put(Tier.BASIC.name()+".alloySmelter.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setNorthTexture("alloy_smelter_basic_active.png"));
        textures.put(Tier.PROTOTYPE.name()+".alloySmelter.active.overlay",new BlockTexture(MOD_ID).setNorthTexture("alloy_smelter_overlay.png"));
        textures.put(Tier.BASIC.name()+".alloySmelter.active.overlay",new BlockTexture(MOD_ID).setNorthTexture("alloy_smelter_overlay.png"));

        textures.put(Tier.BASIC.name()+".inductionSmelter.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setNorthTexture("basic_induction_smelter_front_active.png").setTopTexture("basic_induction_smelter_top_active.png"));
        textures.put(Tier.BASIC.name()+".inductionSmelter.active.overlay",new BlockTexture(MOD_ID).setNorthTexture("induction_smelter_front_overlay.png").setTopTexture("induction_smelter_top_overlay.png"));

        textures.put(Tier.PROTOTYPE.name()+".plateFormer.active",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setNorthTexture("plate_former_prototype_active.png"));
        textures.put(Tier.BASIC.name()+".plateFormer.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setNorthTexture("plate_former_basic_active.png"));
        textures.put(Tier.REINFORCED.name()+".plateFormer.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setNorthTexture("plate_former_reinforced_active.png"));
        textures.put(Tier.PROTOTYPE.name()+".plateFormer.active.overlay",new BlockTexture(MOD_ID).setNorthTexture("plate_former_overlay.png"));
        textures.put(Tier.BASIC.name()+".plateFormer.active.overlay",new BlockTexture(MOD_ID).setNorthTexture("plate_former_overlay.png"));
        textures.put(Tier.REINFORCED.name()+".plateFormer.active.overlay",new BlockTexture(MOD_ID).setNorthTexture("reinforced_plate_former_overlay.png"));

        textures.put(Tier.PROTOTYPE.name()+".crystalCutter.active",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setNorthTexture("crystal_cutter_prototype_active.png"));
        textures.put(Tier.BASIC.name()+".crystalCutter.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setNorthTexture("crystal_cutter_basic_active.png"));
        textures.put(Tier.REINFORCED.name()+".crystalCutter.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setNorthTexture("crystal_cutter_reinforced_active.png"));

        textures.put(Tier.PROTOTYPE.name()+".crystalCutter.active.overlay",new BlockTexture(MOD_ID).setNorthTexture("cutter_overlay.png"));
        textures.put(Tier.BASIC.name()+".crystalCutter.active.overlay",new BlockTexture(MOD_ID).setNorthTexture("cutter_overlay.png"));
        textures.put(Tier.REINFORCED.name()+".crystalCutter.active.overlay",new BlockTexture(MOD_ID).setNorthTexture("reinforced_cutter_overlay.png"));

        textures.put(Tier.BASIC.name()+".crystalChamber.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setNorthTexture("basic_crystal_chamber_side_active.png"));
        textures.put(Tier.BASIC.name()+".crystalChamber.active.overlay",new BlockTexture(MOD_ID).setNorthTexture("chamber_overlay.png"));

        textures.put(Tier.REINFORCED.name()+".crystalChamber.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setNorthTexture("reinforced_crystal_chamber_side_active.png"));
        textures.put(Tier.REINFORCED.name()+".crystalChamber.active.overlay",new BlockTexture(MOD_ID).setNorthTexture("reinforced_chamber_overlay.png"));

        textures.put(Tier.BASIC.name()+".automaticMiner.active.overlay",new BlockTexture(MOD_ID).setNorthTexture("auto_miner_overlay.png"));

        textures.put(Tier.BASIC.name()+".infuser.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setSides("infuser_basic_side_active.png"));
        textures.put(Tier.BASIC.name()+".infuser.active.overlay",new BlockTexture(MOD_ID).setSides("infuser_overlay.png"));

        textures.put(Tier.BASIC.name()+".wrathBeacon.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setSides("wrath_beacon_active.png"));
        textures.put(Tier.REINFORCED.name()+".wrathBeacon.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setSides("reinforced_wrath_beacon_active.png"));

        textures.put(Tier.PROTOTYPE.name()+".pump.active",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setSides("prototype_pump_side.png").setTopTexture("prototype_pump_top.png"));
        textures.put(Tier.BASIC.name()+".pump.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setSides("basic_pump_side_empty.png").setTopTexture("basic_pump_top_empty.png"));

        textures.put(Tier.REINFORCED.name()+".signalumReactorCore.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setSides("signalum_reactor_side_active.png").setNorthTexture("signalum_reactor_front_active.png"));
        textures.put(Tier.REINFORCED.name()+".signalumReactorCore.active.overlay",new BlockTexture(MOD_ID).setSides("reactor_side_overlay.png").setNorthTexture("reactor_overlay.png"));

        textures.put("dimensionalAnchor.active",new BlockTexture(MOD_ID).setSides("dimensional_anchor.png").setBottomTexture("dimensional_anchor_bottom.png").setTopTexture("dimensional_anchor_top.png"));
        textures.put("dimensionalAnchor.active.overlay",new BlockTexture(MOD_ID).setSides("anchor_overlay.png").setBottomTexture("anchor_blank_overlay.png").setTopTexture("anchor_top_overlay.png"));

        textures.put("dilithiumStabilizer.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setSides("dilithium_stabilizer_side_active.png").setNorthTexture("dilithium_top_active.png"));
        textures.put("dilithiumStabilizer.active.overlay",new BlockTexture(MOD_ID).setSides("stabilizer_overlay.png").setNorthTexture("dilithium_machine_overlay.png"));
        textures.put("dilithiumStabilizer.vertical",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setSides("dilithium_stabilizer_side_inactive.png").setTopTexture("dilithium_top_inactive.png"));
        textures.put("dilithiumStabilizer.vertical.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setSides("dilithium_stabilizer_side_active.png").setTopTexture("dilithium_top_active.png"));
        textures.put("dilithiumStabilizer.vertical.active.overlay",new BlockTexture(MOD_ID).setSides("stabilizer_overlay.png").setTopTexture("dilithium_machine_overlay.png"));

        textures.put(Tier.BASIC.name()+"booster.active",new BlockTexture(MOD_ID).setAll("basic_blank.png").setSides("basic_booster_side_active.png").setNorthTexture("basic_booster_top_active.png"));
        textures.put(Tier.BASIC.name()+"booster.active.overlay",new BlockTexture(MOD_ID).setSides("basic_booster_overlay.png").setNorthTexture("basic_booster_overlay_top.png"));
        textures.put(Tier.REINFORCED.name()+"booster.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setSides("dilithium_booster_side_active.png").setNorthTexture("dilithium_top_active.png"));
        textures.put(Tier.REINFORCED.name()+"booster.active.overlay",new BlockTexture(MOD_ID).setSides("booster_overlay.png").setNorthTexture("dilithium_machine_overlay.png"));
        textures.put(Tier.AWAKENED.name()+"booster.active",new BlockTexture(MOD_ID).setAll("awakened_blank.png").setSides("awakened_booster_side_active.png").setNorthTexture("awakened_booster_top_active.png"));
        textures.put(Tier.AWAKENED.name()+"booster.active.overlay",new BlockTexture(MOD_ID).setSides("awakened_booster_overlay.png").setNorthTexture("dilithium_machine_overlay.png"));

        textures.put("uvLamp.active",new BlockTexture(MOD_ID).setAll("uv_lamp.png"));

        textures.put(Tier.PROTOTYPE.name()+".inserter.vertical",new BlockTexture(MOD_ID).setAll("prototype_blank.png").setBottomTexture("inserter_output.png").setTopTexture("inserter_input.png"));
        textures.put(Tier.BASIC.name()+".inserter.vertical",new BlockTexture(MOD_ID).setAll("basic_blank.png").setBottomTexture("basic_inserter_output.png").setTopTexture("basic_inserter_input.png"));

        textures.put(Tier.BASIC.name()+".externalIo",new BlockTexture(MOD_ID).setAll("external_io_blank.png").setTopTexture("external_io_input.png").setBottomTexture("external_io_output.png").setNorthTexture("external_io_both.png"));
        textures.put(Tier.REINFORCED.name()+".externalIo",new BlockTexture(MOD_ID).setAll("reinforced_external_io_blank.png").setTopTexture("reinforced_external_io_input.png").setBottomTexture("reinforced_external_io_output.png").setNorthTexture("reinforced_external_io_both.png"));

        textures.put(Tier.BASIC.name()+".assembler.active",new BlockTexture(MOD_ID).setAll("basic_assembler_side_active.png").setNorthTexture("basic_assembler_front_active.png"));
        textures.put(Tier.BASIC.name()+".assembler.active.overlay",new BlockTexture(MOD_ID).setAll("assembler_overlay_side.png").setNorthTexture("assembler_overlay_front.png"));

        textures.put(Tier.REINFORCED.name()+".centrifuge.active",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setTopTexture("reinforced_centrifuge_closed.png").setNorthTexture("reinforced_centrifuge_front_active.png"));
        textures.put(Tier.REINFORCED.name()+".centrifuge.active.overlay",new BlockTexture(MOD_ID).setNorthTexture("centrifuge_overlay.png"));
        textures.put(Tier.REINFORCED.name()+".centrifuge.loaded",new BlockTexture(MOD_ID).setAll("reinforced_blank.png").setTopTexture("reinforced_centrifuge_loaded.png").setNorthTexture("reinforced_centrifuge_front_inactive.png"));

        textures.put(Tier.REINFORCED.name()+".ignitor.inverted",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_inactive_inverted.png").setTopTexture("reinforced_ignitor_bottom_inactive.png").setBottomTexture("reinforced_ignitor_top_inactive.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.active",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_active.png").setTopTexture("reinforced_ignitor_top_active.png").setBottomTexture("reinforced_ignitor_bottom_active.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.active.overlay",new BlockTexture(MOD_ID).setSides("ignitor_1_overlay.png").setTopTexture("ignitor_7_overlay.png").setBottomTexture("ignitor_3_overlay.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.inverted.active",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_active_inverted.png").setTopTexture("reinforced_ignitor_bottom_active.png").setBottomTexture("reinforced_ignitor_top_active.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.inverted.active.overlay",new BlockTexture(MOD_ID).setSides("ignitor_2_overlay.png").setTopTexture("ignitor_3_overlay.png").setBottomTexture("ignitor_7_overlay.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.ready",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_ready.png").setTopTexture("reinforced_ignitor_top_ready.png").setBottomTexture("reinforced_ignitor_bottom_ready.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.ready.overlay",new BlockTexture(MOD_ID).setSides("ignitor_5_overlay.png").setTopTexture("ignitor_8_overlay.png").setBottomTexture("ignitor_4_overlay.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.inverted.ready",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_ready_inverted.png").setTopTexture("reinforced_ignitor_bottom_ready.png").setBottomTexture("reinforced_ignitor_top_ready.png"));
        textures.put(Tier.REINFORCED.name()+".ignitor.inverted.ready.overlay",new BlockTexture(MOD_ID).setSides("ignitor_6_overlay.png").setTopTexture("ignitor_4_overlay.png").setBottomTexture("ignitor_8_overlay.png"));

        NetworkHelper.register(PacketOpenMachineGUI.class, true, true);
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
        ItemToolPickaxe.miningLevels.put(basicCasing,3);

        ironPlateHammer.setContainerItem(ironPlateHammer);

        CommandHelper.Core.createCommand(new NBTEditCommand());
        CommandHelper.Core.createCommand(new RecipeReloadCommand("recipes"));
        CommandHelper.Core.createCommand(new StructureCommand("structure","struct"));

        EntityHelper.Core.createEntity(EntityCrystal.class,347,"signalumCrystal");
        EntityHelper.Core.createEntity(EntityEnergyOrb.class,349,"energyOrb");
        EntityHelper.Core.createEntity(EntitySunbeam.class,349,"sunBeam");
        EntityHelper.Core.createEntity(EntityFallingMeteor.class,350,"fallingMeteor");


        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT){
            EntityHelper.Client.assignEntityRenderer(EntityCrystal.class,new SnowballRenderer(volatileSignalumCrystal.getIconFromDamage(0)));
            EntityHelper.Client.assignEntityRenderer(EntityEnergyOrb.class,new SnowballRenderer(Block.texCoordToIndex(energyOrbTex[0],energyOrbTex[1])));
            EntityHelper.Client.assignEntityRenderer(EntitySunbeam.class,new SunbeamRenderer());
            EntityHelper.Client.assignEntityRenderer(EntityFallingMeteor.class,new FallingMeteorRenderer());
        }

        addEntities();
        //crafting recipes in RecipeHandlerCraftingSI

    }

    //idea got *too smart* recently and now considers anything after accessor stubs "unreachable" due to the throw statement in them that will never be triggered
    @SuppressWarnings("UnreachableCode")
    private void addEntities(){
        boolean isClientSide = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
        EntityHelper.Core.createTileEntity(TileEntityConduit.class,"Conduit");
        if (isClientSide) EntityHelper.Client.assignTileEntityRenderer(TileEntityConduit.class, new RenderFluidInConduit());

        EntityHelper.Core.createTileEntity(TileEntityFluidConduit.class,"Fluid Conduit");
        if (isClientSide) EntityHelper.Client.assignTileEntityRenderer(TileEntityFluidConduit.class, new RenderFluidInConduit());

        EntityHelper.Core.createTileEntity(TileEntityItemConduit.class,"Item Conduit");
        if (isClientSide) EntityHelper.Client.assignTileEntityRenderer(TileEntityItemConduit.class, new RenderItemsInConduit());

        EntityHelper.Core.createTileEntity(TileEntityInserter.class, "Inserter");

        EntityHelper.Core.createTileEntity(TileEntityEnergyCell.class,"Energy Cell");
        if (isClientSide) EntityHelper.Client.assignTileEntityRenderer(TileEntityEnergyCell.class,new RenderFluidInBlock());
        addToNameGuiMap("Energy Cell", GuiEnergyCell.class, TileEntityEnergyCell.class);

        EntityHelper.Core.createTileEntity(TileEntitySIFluidTank.class,"SI Fluid Tank");
        if (isClientSide) EntityHelper.Client.assignTileEntityRenderer(TileEntitySIFluidTank.class,new RenderFluidInBlock());
        addToNameGuiMap("SI Fluid Tank", GuiSIFluidTank.class, TileEntitySIFluidTank.class);

        EntityHelper.Core.createTileEntity(TileEntityExtractor.class,"Extractor");
        addToNameGuiMap("Extractor", GuiExtractor.class, TileEntityExtractor.class);

        EntityHelper.Core.createTileEntity(TileEntityReinforcedExtractor.class, "Extraction Manifold");
        if (isClientSide) EntityHelper.Client.assignTileEntityRenderer(TileEntityReinforcedExtractor.class, new RenderMultiblock());
        addToNameGuiMap("Extraction Manifold", GuiReinforcedExtractor.class, TileEntityReinforcedExtractor.class);

        EntityHelper.Core.createTileEntity(TileEntityInductionSmelter.class, "Induction Smelter");
        if (isClientSide) EntityHelper.Client.assignTileEntityRenderer(TileEntityInductionSmelter.class, new RenderMultiblock());

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

        EntityHelper.Core.createTileEntity(TileEntityAssembler.class,"SI Assembler");
        if (isClientSide) EntityHelper.Client.assignTileEntityRenderer(TileEntityAssembler.class, new RenderAssemblerItemSprite3D());
        addToNameGuiMap("SI Assembler", GuiAssembler.class, TileEntityAssembler.class);

        EntityHelper.Core.createTileEntity(TileEntityStorageContainer.class,"Storage Container");
        if (isClientSide) EntityHelper.Client.assignTileEntityRenderer(TileEntityStorageContainer.class, new RenderStorageContainer());

        EntityHelper.Core.createTileEntity(TileEntityDimensionalAnchor.class,"Dimensional Anchor");
        if (isClientSide) EntityHelper.Client.assignTileEntityRenderer(TileEntityDimensionalAnchor.class,new RenderMultiblock());
        addToNameGuiMap("Dimensional Anchor", GuiDimAnchor.class, TileEntityDimensionalAnchor.class);

        EntityHelper.Core.createTileEntity(TileEntityAutoMiner.class,"Automatic Miner");
        if (isClientSide) EntityHelper.Client.assignTileEntityRenderer(TileEntityAutoMiner.class, new RenderAutoMiner());
        addToNameGuiMap("Automatic Miner", GuiAutoMiner.class, TileEntityAutoMiner.class);

        EntityHelper.Core.createTileEntity(TileEntityExternalIO.class,"External I/O");
        addToNameGuiMap("External I/O", GuiExternalIO.class, TileEntityExternalIO.class);

        EntityHelper.Core.createTileEntity(TileEntityCentrifuge.class,"Separation Centrifuge");
        addToNameGuiMap("Separation Centrifuge", GuiCentrifuge.class, TileEntityCentrifuge.class);

        EntityHelper.Core.createTileEntity(TileEntitySignalumReactor.class,"Signalum Reactor");
        if (isClientSide) EntityHelper.Client.assignTileEntityRenderer(TileEntitySignalumReactor.class,new RenderSignalumReactor());
        addToNameGuiMap("Signalum Reactor", GuiSignalumReactor.class, TileEntitySignalumReactor.class);

        EntityHelper.Core.createTileEntity(TileEntityEnergyConnector.class,"Energy Connector");
        addToNameGuiMap("Energy Connector", GuiEnergyConnector.class, TileEntityEnergyConnector.class);

        EntityHelper.Core.createTileEntity(TileEntityItemBus.class,"Item Bus");
        addToNameGuiMap("Item Bus", GuiItemBus.class, TileEntityItemBus.class);

        EntityHelper.Core.createTileEntity(TileEntityFluidHatch.class,"Fluid Hatch");
        addToNameGuiMap("Fluid Hatch", GuiItemBus.class, TileEntityFluidHatch.class);

        EntityHelper.Core.createTileEntity(TileEntityIgnitor.class,"Signalum Ignitor");

        EntityHelper.Core.createTileEntity(TileEntityEnergyInjector.class,"Energy Injector");
        if (isClientSide) EntityHelper.Client.assignTileEntityRenderer(TileEntityEnergyInjector.class,new RenderEnergyInjector());
        addToNameGuiMap("Energy Injector",GuiEnergyInjector.class,TileEntityEnergyInjector.class);

        EntityHelper.Core.createTileEntity(TileEntitySignalumDynamo.class,"Signalum Dynamo");
        addToNameGuiMap("Signalum Dynamo", GuiSignalumDynamo.class,TileEntitySignalumDynamo.class);

        EntityHelper.Core.createTileEntity(TileEntityProgrammer.class,"EEPROM Programmer");
        addToNameGuiMap("EEPROM Programmer", GuiProgrammer.class,TileEntityProgrammer.class);

        EntityHelper.Core.createTileEntity(TileEntityFilter.class,"Filter");
        addToNameGuiMap("Filter", GuiFilter.class, TileEntityFilter.class);

        EntityHelper.Core.createTileEntity(TileEntityUVLamp.class,"Ultraviolet Lamp");

        addToNameGuiMap("The Pulsar", GuiPulsar.class, InventoryPulsar.class);
        addToNameGuiMap("Signalum Prototype Harness", GuiHarness.class, InventoryHarness.class);
        addToNameGuiMap("Backpack", GuiBackpack.class, InventoryBackpack.class);

        EntityHelper.Core.createTileEntity(TileEntityRecipeMaker.class,"Recipe Maker");
        EntityHelper.Core.createTileEntity(TileEntityWrathBeacon.class,"Wrath Beacon");
        EntityHelper.Core.createTileEntity(TileEntityReinforcedWrathBeacon.class,"Reinforced Wrath Beacon");
        if (isClientSide) EntityHelper.Client.assignTileEntityRenderer(TileEntityReinforcedWrathBeacon.class,new RenderReinforcedWrathBeacon());
        EntityHelper.Core.createTileEntity(TileEntityBlockBreaker.class,"Block Breaker");

        Multiblock.multiblocks.put("dimensionalAnchor",dimAnchorMultiblock);
        Multiblock.multiblocks.put("wrathTree",wrathTree);
        Multiblock.multiblocks.put("signalumReactor",signalumReactor);
        Multiblock.multiblocks.put("extractionManifold",extractionManifold);
        Multiblock.multiblocks.put("basicInductionSmelter",inductionSmelterBasic);
        SignalIndustries.LOGGER.info(String.format("Loaded %d multiblocks..",Multiblock.multiblocks.size()));
        SignalIndustries.LOGGER.info(String.format("Loaded %d internal structures.", Structure.internalStructures.size()));

        EntityHelper.Core.createEntity(EntityInfernal.class,config.getInt("EntityIDs.infernalId"),"Infernal");
        if (isClientSide) EntityHelper.Client.assignEntityRenderer(EntityInfernal.class,new MobRenderer<EntityInfernal>(new ModelZombie(),0.5F));
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

    public static void displayGui(EntityPlayer entityplayer, Supplier<GuiScreen> screenSupplier, Container container, IInventory tile, int x, int y, int z) {
        if(entityplayer instanceof EntityPlayerMP) {
            ((IEntityPlayerMP)entityplayer).displayGuiScreen_si(screenSupplier,container,tile,x,y,z);
        } else {
            Minecraft.getMinecraft(Minecraft.class).displayGuiScreen(screenSupplier.get());
        }
    }

    public static void displayGui(EntityPlayer entityplayer, Supplier<GuiScreen> screenSupplier, TileEntityWithName tile, int x, int y, int z) {
        if(entityplayer instanceof EntityPlayerMP) {
            ((IEntityPlayerMP)entityplayer).displayGuiScreen_si(screenSupplier,tile,x,y,z);
        } else {
            Minecraft.getMinecraft(Minecraft.class).displayGuiScreen(screenSupplier.get());
        }
    }

    public static void displayGui(EntityPlayer entityplayer, Supplier<GuiScreen> screenSupplier, Container container, IInventory tile, ItemStack stack) {
        if(entityplayer instanceof EntityPlayerMP) {
            ((IEntityPlayerMP)entityplayer).displayItemGuiScreen_si(screenSupplier,container,tile,stack);
        } else {
            Minecraft.getMinecraft(Minecraft.class).displayGuiScreen(screenSupplier.get());
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

    public static void spawnParticle(EntityFX particle, double renderDistance){
        if (Minecraft.getMinecraft(Minecraft.class) == null || Minecraft.getMinecraft(Minecraft.class).thePlayer == null || Minecraft.getMinecraft(Minecraft.class).effectRenderer == null)
            return;
        double d6 = Minecraft.getMinecraft(Minecraft.class).thePlayer.x - particle.x;
        double d7 = Minecraft.getMinecraft(Minecraft.class).thePlayer.y - particle.y;
        double d8 = Minecraft.getMinecraft(Minecraft.class).thePlayer.z - particle.z;
        if (d6 * d6 + d7 * d7 + d8 * d8 > renderDistance * renderDistance)
            return;
        Minecraft.getMinecraft(Minecraft.class).effectRenderer.addEffect(particle);
    }

    public static String key(String key){
        return HalpLibe.addModId(MOD_ID,key);
    }

    public static <T> boolean listContains(List<T> list, T o, BiFunction<T,T,Boolean> equals){
        for (T obj : list) {
            if(equals.apply(o,obj)){
                return true;
            }
        }
        return false;
    }

    public static ArrayList<ItemStack> condenseList(List<ItemStack> list){
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for (ItemStack stack : list) {
            if(stack != null){
                Optional<ItemStack> existing = stacks.stream().filter((S) -> S.itemID == stack.itemID).findAny();
                if (existing.isPresent()) {
                    existing.get().stackSize += stack.stackSize;
                } else {
                    stacks.add(stack.copy());
                }
            }
        }
        return stacks;
    }

    public static boolean hasItems(List<RecipeSymbol> symbols, List<ItemStack> available){
        symbols.removeIf(Objects::isNull);
        List<ItemStack> copy = available.stream().map(ItemStack::copy).collect(Collectors.toList());
        int s = 0;
        int sReq = (int) symbols.stream().filter(Objects::nonNull).count();
        label:
        for (RecipeSymbol symbol : symbols) {
            for (ItemStack stack : copy) {
                if(symbol.matches(stack)){
                    if(stack == null || stack.stackSize <= 0) continue;
                    stack.stackSize--;
                    s++;
                    continue label;
                }
            }
        }
        return s == sReq;
    }

    public static String[] splitStringIntoLines(FontRenderer fr, String string) {
        String[] words = string.split(" ");
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            if (fr.getStringWidth(line + " " + word) > 142) {
                lines.add(line.toString());
                line = new StringBuilder();
            }
            if (word.contains("\n")) {
                String safeWord = word.replace("\r", "");
                String[] wordParts = safeWord.split("\n");
                for (int i = 0; i < wordParts.length; i++) {
                    if (i > 0) {
                        lines.add(line.toString());
                        line = new StringBuilder();
                    }
                    line.append(wordParts[i]).append(" ");
                }
            } else {
                line.append(word).append(" ");
            }
        }
        lines.add(line.toString());
        return lines.toArray(new String[0]);
    }

    public static String[] splitStringIntoLines(FontRenderer fr, String string, int maxLength) {
        String[] words = string.split(" ");
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            if (fr.getStringWidth(line + " " + word) > maxLength) {
                lines.add(line.toString());
                line = new StringBuilder();
            }
            if (word.contains("\n")) {
                String safeWord = word.replace("\r", "");
                String[] wordParts = safeWord.split("\n");
                for (int i = 0; i < wordParts.length; i++) {
                    if (i > 0) {
                        lines.add(line.toString());
                        line = new StringBuilder();
                    }
                    line.append(wordParts[i]).append(" ");
                }
            } else {
                line.append(word).append(" ");
            }
        }
        lines.add(line.toString());
        return lines.toArray(new String[0]);
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

    }

    @Override
    public void beforeClientStart() {

    }

    @Override
    public void afterClientStart() {
        BlockDataExporter.export(this.getClass());
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
