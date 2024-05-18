package sunsetsatellite.signalindustries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.fx.EntityFX;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.SnowballRenderer;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.model.ModelZombie;
import net.minecraft.client.render.stitcher.AtlasStitcher;
import net.minecraft.client.render.stitcher.TextureRegistry;
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
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.sound.BlockSounds;
import net.minecraft.core.util.collection.Pair;
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
import org.useless.dragonfly.helper.ModelHelper;
import org.useless.dragonfly.model.block.BlockModelDragonFly;
import org.useless.dragonfly.model.block.DFBlockModelBuilder;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.NBTEditCommand;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.RenderMultiblock;
import sunsetsatellite.catalyst.multiblocks.Structure;
import sunsetsatellite.catalyst.multiblocks.StructureCommand;
import sunsetsatellite.signalindustries.abilities.powersuit.*;
import sunsetsatellite.signalindustries.api.impl.retrostorage.ReSPlugin;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
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

    public static AtlasStitcher blockAtlas;
    public static AtlasStitcher itemAtlas;
    public static AtlasStitcher particleAtlas;

    static {

        List<Field> blockFields = Arrays.stream(SignalIndustries.class.getDeclaredFields()).filter((F) -> Block.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
        List<Field> itemFields = Arrays.stream(SignalIndustries.class.getDeclaredFields()).filter((F) -> Item.class.isAssignableFrom(F.getType())).collect(Collectors.toList());

        Toml defaultConfig = new Toml("Signal Industries configuration file.");
        defaultConfig.addCategory("BlockIDs");
        defaultConfig.addCategory("ItemIDs");
        defaultConfig.addCategory("EntityIDs");
        defaultConfig.addCategory("Other");
        defaultConfig.addEntry("Other.eternityDimId", 3);
        defaultConfig.addEntry("Other.GuiId", 10);
        defaultConfig.addEntry("Other.machinePacketId", 113);
        defaultConfig.addEntry("EntityIDs.infernalId", 100);

        int blockId = blockIdStart;
        int itemId = itemIdStart;
        for (Field blockField : blockFields) {
            defaultConfig.addEntry("BlockIDs." + blockField.getName(), blockId++);
        }
        for (Field itemField : itemFields) {
            defaultConfig.addEntry("ItemIDs." + itemField.getName(), itemId++);
        }


        config = new TomlConfigHandler(MOD_ID, new Toml("Signal Industries configuration file."));

        File configFile = config.getConfigFile();

        if (config.getConfigFile().exists()) {
            config.loadConfig();
            config.setDefaults(config.getRawParsed());
            Toml rawConfig = config.getRawParsed();
            int maxBlocks = ((Toml) rawConfig.get(".BlockIDs")).getOrderedKeys().size();
            int maxItems = ((Toml) rawConfig.get(".ItemIDs")).getOrderedKeys().size();
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
            if (changed) {
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
                throw new RuntimeException("Failed to generate config!", e);
            }
        }

        /*try {
            Class.forName("net.minecraft.block.Block");
            Class.forName("net.minecraft.item.Item");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }*/


        /*TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_center");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_bottom");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_bottom_left");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_bottom_right");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_top");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_top_left");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_top_right");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_top_bottom");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_left_right");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_no_right");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_no_left");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_no_bottom");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_no_top");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_left");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"reinforced_glass_right");

        TextureHelper.getOrCreateBlockTexture(MOD_ID,"signalum_ore_overlay");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"glowing_obsidian_overlay");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"input_overlay");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"both_io_overlay");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"output_overlay");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"dilithium_rail_overlay");
        TextureHelper.getOrCreateBlockTexture(MOD_ID,"uv_lamp_overlay");

        TextureHelper.getOrCreateParticleTexture(MOD_ID,"meteor_tail");*/
    }
    //public static final Config config = new Config(MOD_ID, mapOf(new String[]{"PacketOpenMachineGUI_ID","PacketPipeItemSpawn_ID","GuiID"},new String[]{"113","114","9"}), new Class[]{SignalIndustries.class});

    public static HashMap<String, ArrayList<Class<?>>> nameToGuiMap = new HashMap<>();

    public static final Tag<Block> ENERGY_CONDUITS_CONNECT = Tag.of("energy_conduits_connect");
    public static final Tag<Block> FLUID_CONDUITS_CONNECT = Tag.of("fluid_conduits_connect");
    public static final Tag<Block> ITEM_CONDUITS_CONNECT = Tag.of("item_conduits_connect");

    public static final Block signalumOre = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/signalum_ore")
            .setLuminance(3)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(3)
            .setResistance(25)
            .build(new BlockOreSignalum("signalumOre",config.getInt("BlockIDs.signalumOre")).withTags(BlockTags.MINEABLE_BY_PICKAXE).withOverbright());

    public static final Block dilithiumOre = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/dilithium_ore")
            .setLuminance(3)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(75)
            .setResistance(100)
            .build(new BlockOreDilithium("dilithiumOre",config.getInt("BlockIDs.dilithiumOre")).withTags(BlockTags.MINEABLE_BY_PICKAXE));

    public static final Block dimensionalShardOre = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/dimensional_shard_ore")
            .setLuminance(3)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(200)
            .setResistance(50000)
            .build(new BlockOreDimensionalShard("dimensionalShardOre",config.getInt("BlockIDs.dimensionalShardOre")).withTags(BlockTags.MINEABLE_BY_PICKAXE));


    public static final Block dilithiumBlock = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/dilithium_block")
            .setLuminance(1)
            .setBlockSound(BlockSounds.GLASS)
            .setHardness(20)
            .setResistance(1000)
            .build(new Block("dilithiumBlock",config.getInt("BlockIDs.dilithiumBlock"), Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
    public static final Block emptyCrystalBlock = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/empty_crystal_block")
            .setLuminance(1)
            .setBlockSound(BlockSounds.GLASS)
            .setHardness(12)
            .setResistance(1000)
            .build(new Block("emptyCrystalBlock",config.getInt("BlockIDs.emptyCrystalBlock"),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
    public static final Block rawCrystalBlock = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/saturated_crystal_block")
            .setLuminance(15).setBlockSound(BlockSounds.GLASS)
            .setHardness(24).setResistance(50000)
            .build(new Block("rawCrystalBlock",config.getInt("BlockIDs.rawCrystalBlock"),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
    public static final Block awakenedSignalumCrystalBlock = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/awakened_crystal_block")
            .setLuminance(15)
            .setBlockSound(BlockSounds.GLASS)
            .setHardness(50)
            .setResistance(1000000)
            .build(new Block("awakenedSignalumCrystalBlock",config.getInt("BlockIDs.awakenedSignalumCrystalBlock"),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));

    public static final Block dilithiumCrystalBlock = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/dilithium_crystal_block")
            .setLuminance(1)
            .setBlockSound(BlockSounds.GLASS)
            .setHardness(20)
            .setResistance(1000)
            .build(new BlockDilithiumCrystal("dilithiumCrystalBlock",config.getInt("BlockIDs.dilithiumCrystalBlock"),Material.glass,false)).withTags(BlockTags.MINEABLE_BY_PICKAXE);

    public static final Block prototypeMachineCore = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/machine_prototype")
            .setBlockSound(BlockSounds.STONE)
            .setLuminance(0)
            .setHardness(1)
            .setResistance(3)
            .build(new BlockTiered("prototype.machine",config.getInt("BlockIDs.prototypeMachineCore"), Tier.PROTOTYPE,Material.stone));

    public static final Block basicMachineCore = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/machine_basic")
            .setBlockSound(BlockSounds.METAL)
            .setLuminance(0)
            .setHardness(3)
            .setResistance(8)
            .build(new BlockTiered("basic.machine",config.getInt("BlockIDs.basicMachineCore"), Tier.BASIC,Material.metal));

    public static final Block reinforcedMachineCore = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/machine_reinforced")
            .setLuminance(0)
            .setHardness(4)
            .setResistance(15)
            .build(new BlockTiered("reinforced.machine",config.getInt("BlockIDs.reinforcedMachineCore"), Tier.REINFORCED,Material.metal));

    public static final Block awakenedMachineCore = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/machine_awakened")
            .setLuminance(1)
            .setHardness(1)
            .setResistance(50)
            .build(new BlockTiered("awakened.machine",config.getInt("BlockIDs.awakenedMachineCore"), Tier.AWAKENED,Material.metal));

    public static final Block basicCasing = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/basic_casing")
            .setLuminance(0)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(10)
            .setResistance(2000)
            .build(new Block("basic.casing",config.getInt("BlockIDs.basicCasing"),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
    public static final Block reinforcedCasing = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/reinforced_casing")
            .setLuminance(0)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(10)
            .setResistance(2000)
            .build(new Block("reinforced.casing",config.getInt("BlockIDs.reinforcedCasing"),Material.metal).withTags(BlockTags.MINEABLE_BY_PICKAXE));
    public static final Block reinforcedGlass = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/reinforced_glass")
            .setLuminance(0)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(4)
            .setResistance(2000)
            .build(new BlockConnectedTextureCursed("reinforced.glass",config.getInt("BlockIDs.reinforcedGlass"),Material.metal,"reinforced_glass").withTags(BlockTags.MINEABLE_BY_PICKAXE));

    public static final Block prototypeConduit = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/conduit_prototype")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    block ->
                        new DFBlockModelBuilder(MOD_ID)
                                .setBlockModel("conduit/energy/prototype/conduit_all.json")
                                .setBlockState("prototype_conduit.json")
                                .setMetaStateInterpreter(new ConduitStateInterpreter())
                                .build(block)
            )
            .build(new BlockConduit("prototype.conduit",config.getInt("BlockIDs.prototypeConduit"),Tier.PROTOTYPE,Material.glass));

    public static final Block basicConduit = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/conduit_basic")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    block ->
                            new DFBlockModelBuilder(MOD_ID)
                                    .setBlockModel("conduit/energy/basic/conduit_all.json")
                                    .setBlockState("basic_conduit.json")
                                    .setMetaStateInterpreter(new ConduitStateInterpreter())
                                    .build(block)
            )
            .build(new BlockConduit("basic.conduit",config.getInt("BlockIDs.basicConduit"),Tier.BASIC,Material.glass));

    public static final Block reinforcedConduit = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/conduit_reinforced")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    block ->
                            new DFBlockModelBuilder(MOD_ID)
                                    .setBlockModel("conduit/energy/reinforced/conduit_all.json")
                                    .setBlockState("reinforced_conduit.json")
                                    .setMetaStateInterpreter(new ConduitStateInterpreter())
                                    .build(block)
            )
            .build(new BlockConduit("reinforced.conduit",config.getInt("BlockIDs.reinforcedConduit"),Tier.REINFORCED,Material.glass));

    public static final Block awakenedConduit = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/conduit_awakened")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    block ->
                            new DFBlockModelBuilder(MOD_ID)
                                    .setBlockModel("conduit/energy/awakened/conduit_all.json")
                                    .setBlockState("awakened_conduit.json")
                                    .setMetaStateInterpreter(new ConduitStateInterpreter())
                                    .build(block)
            )
            .build(new BlockConduit("awakened.conduit",config.getInt("BlockIDs.awakenedConduit"),Tier.AWAKENED,Material.glass));


    public static final Block prototypeFluidConduit = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/fluid_pipe_prototype")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    block ->
                            new DFBlockModelBuilder(MOD_ID)
                                    .setBlockModel("conduit/fluid/prototype/conduit_all.json")
                                    .setBlockState("prototype_fluid_conduit.json")
                                    .setMetaStateInterpreter(new ConduitStateInterpreter())
                                    .build(block)
            )
            .build(new BlockFluidConduit("prototype.conduit.fluid",config.getInt("BlockIDs.prototypeFluidConduit"),Tier.PROTOTYPE,Material.glass));

    public static final Block basicFluidConduit = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/fluid_pipe_basic")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    block ->
                            new DFBlockModelBuilder(MOD_ID)
                                    .setBlockModel("conduit/fluid/basic/conduit_all.json")
                                    .setBlockState("basic_fluid_conduit.json")
                                    .setMetaStateInterpreter(new ConduitStateInterpreter())
                                    .build(block)
            )
            .build(new BlockFluidConduit("basic.conduit.fluid",config.getInt("BlockIDs.basicFluidConduit"),Tier.BASIC,Material.glass));

    public static final Block reinforcedFluidConduit = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/fluid_pipe_reinforced")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    block ->
                            new DFBlockModelBuilder(MOD_ID)
                                    .setBlockModel("conduit/fluid/reinforced/conduit_all.json")
                                    .setBlockState("reinforced_fluid_conduit.json")
                                    .setMetaStateInterpreter(new ConduitStateInterpreter())
                                    .build(block)
            )
            .build(new BlockFluidConduit("reinforced.conduit.fluid",config.getInt("BlockIDs.reinforcedFluidConduit"),Tier.REINFORCED,Material.glass));

    public static final Block prototypeItemConduit = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/item_conduit_prototype")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    block ->
                            new DFBlockModelBuilder(MOD_ID)
                                    .setBlockModel("conduit/item/prototype/conduit_all.json")
                                    .setBlockState("prototype_item_conduit.json")
                                    .setMetaStateInterpreter(new ItemConduitStateInterpreter())
                                    .build(block)
            )
            .build(new BlockItemConduit("prototype.conduit.item",config.getInt("BlockIDs.prototypeItemConduit"),Tier.PROTOTYPE,Material.glass, PipeType.NORMAL));

    public static final Block basicItemConduit = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/item_conduit_basic")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    block ->
                            new DFBlockModelBuilder(MOD_ID)
                                    .setBlockModel("conduit/item/basic/conduit_all.json")
                                    .setBlockState("basic_item_conduit.json")
                                    .setMetaStateInterpreter(new ItemConduitStateInterpreter())
                                    .build(block)
            )
            .build(new BlockItemConduit("basic.conduit.item",config.getInt("BlockIDs.basicItemConduit"),Tier.BASIC,Material.glass, PipeType.NORMAL));

    public static final Block basicRestrictItemConduit = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/item_conduit_basic")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    block ->
                            new DFBlockModelBuilder(MOD_ID)
                                    .setBlockModel("conduit/item/basic/restrict/conduit_all.json")
                                    .setBlockState("basic_item_conduit_restrict.json")
                                    .setMetaStateInterpreter(new ItemConduitStateInterpreter())
                                    .build(block)
            )
            .build(new BlockItemConduit("basic.conduit.item.restrict",config.getInt("BlockIDs.basicRestrictItemConduit"),Tier.BASIC,Material.glass, PipeType.RESTRICT));

    public static final Block basicSensorItemConduit = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/item_conduit_basic_sensor_off")
            .setLuminance(0)
            .setResistance(1)
            .setHardness(1)
            .setBlockSound(BlockSounds.GLASS)
            .setBlockModel(
                    block ->
                            new DFBlockModelBuilder(MOD_ID)
                                    .setBlockModel("conduit/item/basic/sensor/off/conduit_all.json")
                                    .setBlockState("basic_item_conduit_sensor.json")
                                    .setMetaStateInterpreter(new ItemConduitStateInterpreter())
                                    .build(block)
            )
            .build(new BlockItemConduit("basic.conduit.item.sensor",config.getInt("BlockIDs.basicSensorItemConduit"),Tier.BASIC,Material.glass, PipeType.SENSOR));

    public static final Block infiniteEnergyCell = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/cell_prototype")
            .setLuminance(1)
            .setHardness(-1)
            .setResistance(1000000)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockEnergyCell("infinite.energyCell",config.getInt("BlockIDs.infiniteEnergyCell"),Tier.INFINITE,Material.glass));

    public static final Block prototypeEnergyCell = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/cell_prototype")
            .setLuminance(1)
            .setHardness(1)
            .setResistance(5)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockEnergyCell("prototype.energyCell",config.getInt("BlockIDs.prototypeEnergyCell"),Tier.PROTOTYPE,Material.glass));

    public static final Block basicEnergyCell = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/cell_basic")
            .setLuminance(1)
            .setHardness(1)
            .setResistance(5)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockEnergyCell("basic.energyCell",config.getInt("BlockIDs.basicEnergyCell"),Tier.BASIC,Material.glass));


    public static final Block prototypeFluidTank = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/fluid_tank_prototype")
            .setLuminance(0)
            .setHardness(1)
            .setResistance(5)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockSIFluidTank("prototype.fluidTank",config.getInt("BlockIDs.prototypeFluidTank"),Tier.PROTOTYPE,Material.glass));

    public static final Block infiniteFluidTank = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/fluid_tank_prototype")
            .setLuminance(0)
            .setHardness(1)
            .setResistance(5)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockSIFluidTank("infinite.fluidTank",config.getInt("BlockIDs.infiniteFluidTank"),Tier.INFINITE,Material.glass));

    public static final Block basicFluidTank = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/fluid_tank_basic")
            .setLuminance(0)
            .setHardness(1)
            .setResistance(5)
            .setBlockSound(BlockSounds.GLASS)
            .build(new BlockSIFluidTank("basic.fluidTank",config.getInt("BlockIDs.basicFluidTank"),Tier.BASIC,Material.glass));


    /*public static final Block recipeMaker = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/prototype_connection")
            .setLuminance(0)
            .setHardness(1)
            .setResistance(5)
            .setBlockSound(BlockSounds.STONE)
            .build(new BlockRecipeMaker("recipeMaker",config.getInt("BlockIDs.recipeMaker"),Material.stone));*/


    public static final Block prototypeExtractor = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("signalindustries:block/prototype_blank")
            .setSideTextures("signalindustries:block/extractor_prototype_side_empty")
            .build(new BlockExtractor("prototype.extractor",config.getInt("BlockIDs.prototypeExtractor"),Tier.PROTOTYPE,Material.stone));

    public static final Block basicExtractor = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/basic_blank")
            .setSideTextures("signalindustries:block/extractor_basic_side_empty")
            .build(new BlockExtractor("basic.extractor",config.getInt("BlockIDs.basicExtractor"),Tier.BASIC,Material.metal));

    public static final Block reinforcedExtractor = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/reinforced_blank")
            .setSideTextures("signalindustries:block/extractor_reinforced_side_empty")
            .build(new BlockExtractor("reinforced.extractor",config.getInt("BlockIDs.reinforcedExtractor"),Tier.REINFORCED,Material.metal));

    public static final Block prototypeCrusher = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("signalindustries:block/prototype_blank")
            .setTopTexture("signalindustries:block/crusher_prototype_top_inactive")
            .setSouthTexture("signalindustries:block/crusher_prototype_side")
            .build(new BlockCrusher("prototype.crusher",config.getInt("BlockIDs.prototypeCrusher"), Tier.PROTOTYPE,Material.stone));

    public static final Block basicCrusher = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/basic_blank")
            .setTopTexture("signalindustries:block/crusher_basic_top_inactive")
            .setSouthTexture("signalindustries:block/crusher_basic_side")
            .build(new BlockCrusher("basic.crusher",config.getInt("BlockIDs.basicCrusher"), Tier.BASIC,Material.metal));

    public static final Block reinforcedCrusher = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/reinforced_blank")
            .setTopTexture("signalindustries:block/crusher_reinforced_top_inactive")
            .setSouthTexture("signalindustries:block/crusher_reinforced_side")
            .build(new BlockCrusher("reinforced.crusher",config.getInt("BlockIDs.reinforcedCrusher"), Tier.REINFORCED, Material.metal));

    public static final Block prototypeAlloySmelter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("signalindustries:block/prototype_blank")
            .setSouthTexture("signalindustries:block/alloy_smelter_prototype_inactive")
            .build(new BlockAlloySmelter("prototype.alloySmelter",config.getInt("BlockIDs.prototypeAlloySmelter"), Tier.PROTOTYPE,Material.stone));

    public static final Block basicAlloySmelter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/basic_blank")
            .setSouthTexture("signalindustries:block/alloy_smelter_basic_inactive")
            .build(new BlockAlloySmelter("basic.alloySmelter",config.getInt("BlockIDs.basicAlloySmelter"), Tier.BASIC,Material.metal));

    public static final Block basicInductionSmelter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/basic_blank")
            .setSouthTexture("signalindustries:block/basic_induction_smelter_front_inactive")
            .setTopTexture("signalindustries:block/basic_induction_smelter_top_inactive")
            .build(new BlockInductionSmelter("basic.inductionSmelter",config.getInt("BlockIDs.basicInductionSmelter"), Tier.BASIC,Material.metal));

    public static final Block prototypePlateFormer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("signalindustries:block/prototype_blank")
            .setSouthTexture("signalindustries:block/plate_former_prototype_inactive")
            .build(new BlockPlateFormer("prototype.plateFormer",config.getInt("BlockIDs.prototypePlateFormer"), Tier.PROTOTYPE,Material.stone));

    public static final Block basicPlateFormer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/basic_blank")
            .setSouthTexture("signalindustries:block/plate_former_basic_inactive")
            .build(new BlockPlateFormer("basic.plateFormer",config.getInt("BlockIDs.basicPlateFormer"), Tier.BASIC,Material.metal));

    public static final Block reinforcedPlateFormer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/reinforced_blank")
            .setSouthTexture("signalindustries:block/plate_former_reinforced_inactive")
            .build(new BlockPlateFormer("reinforced.plateFormer",config.getInt("BlockIDs.reinforcedPlateFormer"), Tier.REINFORCED,Material.metal));

    public static final Block prototypeCrystalCutter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("signalindustries:block/prototype_blank")
            .setSouthTexture("signalindustries:block/crystal_cutter_prototype_inactive")
            .build(new BlockCrystalCutter("prototype.crystalCutter",config.getInt("BlockIDs.prototypeCrystalCutter"), Tier.PROTOTYPE,Material.stone));

    public static final Block basicCrystalCutter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/basic_blank")
            .setSouthTexture("signalindustries:block/crystal_cutter_basic_inactive")
            .build(new BlockCrystalCutter("basic.crystalCutter",config.getInt("BlockIDs.basicCrystalCutter"), Tier.BASIC,Material.metal));

    public static final Block reinforcedCrystalCutter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/reinforced_blank")
            .setSouthTexture("signalindustries:block/crystal_cutter_reinforced_inactive")
            .build(new BlockCrystalCutter("reinforced.crystalCutter",config.getInt("BlockIDs.reinforcedCrystalCutter"), Tier.REINFORCED, Material.stone));

    public static final Block basicCrystalChamber = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/basic_blank")
            .setSouthTexture("signalindustries:block/basic_crystal_chamber_side_inactive")
            .build(new BlockCrystalChamber("basic.crystalChamber",config.getInt("BlockIDs.basicCrystalChamber"), Tier.BASIC,Material.metal));

    public static final Block reinforcedCrystalChamber = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/reinforced_blank")
            .setSouthTexture("signalindustries:block/reinforced_crystal_chamber_side_inactive")
            .build(new BlockCrystalChamber("reinforced.crystalChamber",config.getInt("BlockIDs.reinforcedCrystalChamber"), Tier.REINFORCED,Material.metal));

    public static final Block basicInfuser = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/basic_blank")
            .setSideTextures("signalindustries:block/infuser_basic_side_inactive")
            .build(new BlockInfuser("basic.infuser",config.getInt("BlockIDs.basicInfuser"), Tier.BASIC,Material.metal));

    public static final Block basicAssembler = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/basic_assembler_side")
            .setSouthTexture("signalindustries:block/basic_assembler_front")
            .build(new BlockAssembler("basic.assembler",config.getInt("BlockIDs.basicAssembler"), Tier.BASIC, Material.metal));

    public static final Block prototypeStorageContainer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("signalindustries:block/prototype_blank")
            .setSouthTexture("signalindustries:block/container_prototype_front")
            .build(new BlockStorageContainer("prototype.storageContainer",config.getInt("BlockIDs.prototypeStorageContainer"), Tier.PROTOTYPE, Material.stone));

    public static final Block infiniteStorageContainer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("signalindustries:block/prototype_blank")
            .setSouthTexture("signalindustries:block/container_prototype_front")
            .build(new BlockStorageContainer("infinite.storageContainer",config.getInt("BlockIDs.infiniteStorageContainer"), Tier.INFINITE, Material.stone));

    public static final Block basicStorageContainer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/basic_blank")
            .setSouthTexture("signalindustries:block/container_basic_front")
            .build(new BlockStorageContainer("basic.storageContainer",config.getInt("BlockIDs.basicStorageContainer"), Tier.BASIC, Material.metal));

    public static final Block reinforcedStorageContainer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/reinforced_blank")
            .setSouthTexture("signalindustries:block/container_reinforced_front")
            .build(new BlockStorageContainer("reinforced.storageContainer",config.getInt("BlockIDs.reinforcedStorageContainer"), Tier.REINFORCED, Material.metal));

    public static final Block basicWrathBeacon = new BlockBuilder(MOD_ID)
            .setHardness(2)
            .setResistance(500)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/basic_blank")
            .setSideTextures("signalindustries:block/wrath_beacon")
            .build(new BlockWrathBeacon("basic.wrathBeacon",config.getInt("BlockIDs.basicWrathBeacon"), Tier.BASIC,Material.metal));

    public static final Block reinforcedWrathBeacon = new BlockBuilder(MOD_ID)
            .setHardness(2)
            .setResistance(500)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/reinforced_blank")
            .setSideTextures("signalindustries:block/reinforced_wrath_beacon")
            .build(new BlockWrathBeacon("reinforced.wrathBeacon",config.getInt("BlockIDs.reinforcedWrathBeacon"), Tier.REINFORCED,Material.metal));

    //public static final Block awakenedWrathBeacon = new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockWrathBeacon("",config.getInt("BlockIDs.awakenedWrathBeacon"),Tiers.AWAKENED,Material.metal),"awakened.wrathBeacon","reinforced_blank","awakened_wrath_beacon_active",BlockSounds.METAL,25f,500f,1);

    public static final Block dimensionalAnchor = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/reinforced_blank")
            .setTopTexture("signalindustries:block/dimensional_anchor_top_inactive")
            .setSideTextures("signalindustries:block/dimensional_anchor_inactive")
            .build(new BlockDimensionalAnchor("reinforced.dimensionalAnchor",config.getInt("BlockIDs.dimensionalAnchor"), Tier.REINFORCED,Material.metal));

    public static final Block dilithiumStabilizer = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/reinforced_blank")
            .setSideTextures("signalindustries:block/dilithium_stabilizer_side_inactive")
            .setSouthTexture("signalindustries:block/dilithium_top_inactive")
            .build(new BlockDilithiumStabilizer("reinforced.dilithiumStabilizer",config.getInt("BlockIDs.dilithiumStabilizer"), Tier.REINFORCED,Material.metal));

    public static final Block redstoneBooster = new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockDilithiumBooster("reinforced.dilithiumBooster",config.getInt("BlockIDs.dilithiumBooster"), Tier.REINFORCED,Material.metal),"reinforced_blank","reinforced_blank","dilithium_top_inactive","dilithium_booster_side_inactive","dilithium_booster_side_inactive","dilithium_booster_side_inactive",BlockSounds.METAL,5f,20f,1);
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/basic_blank")
            .setSideTextures("signalindustries:block/basic_booster_side_inactive")
            .setSouthTexture("signalindustries:block/basic_booster_top_inactive")
            .build(new BlockDilithiumBooster("basic.booster",config.getInt("BlockIDs.redstoneBooster"), Tier.BASIC,Material.metal));

    public static final Block dilithiumBooster = new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockDilithiumBooster("reinforced.dilithiumBooster",config.getInt("BlockIDs.dilithiumBooster"), Tier.REINFORCED,Material.metal),"reinforced_blank","reinforced_blank","dilithium_top_inactive","dilithium_booster_side_inactive","dilithium_booster_side_inactive","dilithium_booster_side_inactive",BlockSounds.METAL,5f,20f,1);
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/reinforced_blank")
            .setSideTextures("signalindustries:block/dilithium_booster_side_inactive")
            .setSouthTexture("signalindustries:block/dilithium_top_inactive")
            .build(new BlockDilithiumBooster("reinforced.booster",config.getInt("BlockIDs.dilithiumBooster"), Tier.REINFORCED,Material.metal));

    //TODO: W.I.P.
    public static final Block awakenedBooster = new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockDilithiumBooster("reinforced.dilithiumBooster",config.getInt("BlockIDs.dilithiumBooster"), Tier.REINFORCED,Material.metal),"reinforced_blank","reinforced_blank","dilithium_top_inactive","dilithium_booster_side_inactive","dilithium_booster_side_inactive","dilithium_booster_side_inactive",BlockSounds.METAL,5f,20f,1);
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/awakened_blank")
            .setSideTextures("signalindustries:block/awakened_booster_side_inactive")
            .setSouthTexture("signalindustries:block/awakened_booster_top_inactive")
            .build(new BlockDilithiumBooster("awakened.booster",config.getInt("BlockIDs.awakenedBooster"), Tier.AWAKENED,Material.metal));

    public static final Block prototypePump = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("signalindustries:block/prototype_blank")
            .setTopTexture("signalindustries:block/prototype_pump_top_empty")
            .setSideTextures("signalindustries:block/prototype_pump_side_empty")
            .build(new BlockPump("prototype.pump",config.getInt("BlockIDs.prototypePump"), Tier.PROTOTYPE,Material.stone));

    public static final Block basicPump = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/basic_blank")
            .setTopTexture("signalindustries:block/basic_pump_top_empty")
            .setSideTextures("signalindustries:block/basic_pump_side_empty")
            .build(new BlockPump("basic.pump",config.getInt("BlockIDs.basicPump"), Tier.BASIC,Material.metal));

    public static final Block prototypeInserter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTextures("signalindustries:block/prototype_blank")
            .setNorthTexture("signalindustries:block/inserter_output")
            .setSouthTexture("signalindustries:block/inserter_input")
            .build(new BlockInserter("prototype.inserter",config.getInt("BlockIDs.prototypeInserter"),Tier.PROTOTYPE,Material.stone));

    public static final Block basicInserter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.METAL)
            .setTextures("signalindustries:block/basic_blank")
            .setNorthTexture("signalindustries:block/basic_inserter_output")
            .setSouthTexture("signalindustries:block/basic_inserter_input")
            .build(new BlockInserter("basic.inserter",config.getInt("BlockIDs.basicInserter"),Tier.BASIC,Material.metal));

    public static final Block prototypeFilter = new BlockBuilder(MOD_ID)
            .setHardness(1)
            .setResistance(3)
            .setBlockSound(BlockSounds.STONE)
            .setTopTexture("signalindustries:block/filter_red")
            .setSouthTexture("signalindustries:block/filter_green")
            .setEastTexture("signalindustries:block/filter_blue")
            .setWestTexture("signalindustries:block/filter_yellow")
            .setNorthTexture("signalindustries:block/filter_magenta")
            .setBottomTexture("signalindustries:block/filter_cyan")
            .build(new BlockFilter("prototype.filter",config.getInt("BlockIDs.prototypeFilter"),Tier.PROTOTYPE,Material.stone));

    public static final Block basicAutomaticMiner = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setTextures("signalindustries:block/basic_blank")
            .setSouthTexture("signalindustries:block/basic_automatic_miner")
            .build(new BlockAutoMiner("basic.automaticMiner",config.getInt("BlockIDs.basicAutomaticMiner"),Tier.BASIC,Material.metal));

    public static final Block externalIo = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setTextures("signalindustries:block/external_io_blank")
            .build(new BlockExternalIO("basic.externalIO",config.getInt("BlockIDs.externalIo"),Tier.BASIC,Material.metal));

    public static final Block reinforcedExternalIo = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setTextures("signalindustries:block/reinforced_external_io_blank")
            .build(new BlockExternalIO("reinforced.externalIO",config.getInt("BlockIDs.reinforcedExternalIo"),Tier.REINFORCED,Material.metal));

    public static final Block reinforcedCentrifuge = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(1)
            .setTextures("signalindustries:block/reinforced_blank")
            .setSouthTexture("signalindustries:block/reinforced_centrifuge_front_inactive")
            .setTopTexture("signalindustries:block/reinforced_centrifuge_empty")
            .build(new BlockCentrifuge("reinforced.centrifuge",config.getInt("BlockIDs.reinforcedCentrifuge"),Tier.REINFORCED,Material.metal));

    public static final Block reinforcedIgnitor = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(1)
            .setSideTextures("signalindustries:block/reinforced_ignitor_inactive")
            .setTopTexture("signalindustries:block/reinforced_ignitor_top_inactive")
            .setBottomTexture("signalindustries:block/reinforced_ignitor_bottom_inactive")
            .build(new BlockIgnitor("reinforced.ignitor",config.getInt("BlockIDs.reinforcedIgnitor"),Tier.REINFORCED,Material.metal));

    public static final Block signalumReactorCore = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(1)
            .setTextures("signalindustries:block/reinforced_blank")
            .setSideTextures("signalindustries:block/signalum_reactor_side_inactive")
            .setSouthTexture("signalindustries:block/signalum_reactor_front_inactive")
            .build(new BlockSignalumReactorCore("reinforced.signalumReactorCore",config.getInt("BlockIDs.signalumReactorCore"),Tier.REINFORCED,Material.metal));

    public static final Block reinforcedEnergyConnector = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(1)
            .setTextures("signalindustries:block/reinforced_energy_connector")
            .build(new BlockEnergyConnector("reinforced.energyConnector",config.getInt("BlockIDs.reinforcedEnergyConnector"),Tier.REINFORCED,Material.metal));

    public static final Block basicEnergyConnector = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(1)
            .setTextures("signalindustries:block/basic_energy_connector")
            .build(new BlockEnergyConnector("basic.energyConnector",config.getInt("BlockIDs.basicEnergyConnector"),Tier.BASIC,Material.metal));

    public static final Block basicFluidInputHatch = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("signalindustries:block/basic_fluid_input_hatch")
            .build(new BlockFluidInputHatch("basic.fluidInputHatch",config.getInt("BlockIDs.basicFluidInputHatch"),Tier.BASIC,Material.metal));

    public static final Block basicFluidOutputHatch = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("signalindustries:block/basic_fluid_output_hatch")
            .build(new BlockFluidOutputHatch("basic.fluidOutputHatch",config.getInt("BlockIDs.basicFluidOutputHatch"),Tier.BASIC,Material.metal));

    public static final Block basicItemInputBus = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("signalindustries:block/basic_input_bus")
            .build(new BlockInputBus("basic.itemInputBus",config.getInt("BlockIDs.basicItemInputBus"),Tier.BASIC,Material.metal));

    public static final Block basicItemOutputBus = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("signalindustries:block/basic_output_bus")
            .build(new BlockOutputBus("basic.itemOutputBus",config.getInt("BlockIDs.basicItemOutputBus"),Tier.BASIC,Material.metal));
    
    public static final Block reinforcedFluidInputHatch = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("signalindustries:block/reinforced_fluid_input_hatch")
            .build(new BlockFluidInputHatch("reinforced.fluidInputHatch",config.getInt("BlockIDs.reinforcedFluidInputHatch"),Tier.REINFORCED,Material.metal));

    public static final Block reinforcedFluidOutputHatch = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("signalindustries:block/reinforced_fluid_output_hatch")
            .build(new BlockFluidOutputHatch("reinforced.fluidOutputHatch",config.getInt("BlockIDs.reinforcedFluidOutputHatch"),Tier.REINFORCED,Material.metal));

    public static final Block reinforcedItemInputBus = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("signalindustries:block/reinforced_input_bus")
            .build(new BlockInputBus("reinforced.itemInputBus",config.getInt("BlockIDs.reinforcedItemInputBus"),Tier.REINFORCED,Material.metal));

    public static final Block reinforcedItemOutputBus = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setTextures("signalindustries:block/reinforced_output_bus")
            .build(new BlockOutputBus("reinforced.itemOutputBus",config.getInt("BlockIDs.reinforcedItemOutputBus"),Tier.REINFORCED,Material.metal));

    public static final Block basicEnergyInjector = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setBlockModel(block ->
                    new DFBlockModelBuilder(MOD_ID)
                            .setBlockModel("basic_energy_injector.json")
                            .build(block))
            .build(new BlockEnergyInjector("basic.energyInjector",config.getInt("BlockIDs.basicEnergyInjector"),Tier.BASIC,Material.metal));

    public static final Block basicSignalumDynamo = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setBlockModel(block ->
                    new DFBlockModelBuilder(MOD_ID)
                            .setBlockModel("signalum_dynamo.json")
                            .build(block))
            .build(new BlockSignalumDynamo("basic.dynamo",config.getInt("BlockIDs.basicSignalumDynamo"),Tier.BASIC,Material.metal));

    public static final Block basicProgrammer = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(20)
            .setLuminance(0)
            .setBlockModel(
                    block ->
                            new DFBlockModelBuilder(MOD_ID)
                                    .setBlockModel("eeprom_programmer.json")
                                    .setBlockState("eeprom_programmer.json")
                                    .setMetaStateInterpreter(new EEPROMProgrammerStateInterpreter())
                                    .build(block)
            )
            .build(new BlockProgrammer("basic.programmer",config.getInt("BlockIDs.basicProgrammer"),Tier.BASIC,Material.metal));

    public static final Block cobblestoneBricks = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.STONE)
            .setHardness(1)
            .setResistance(3)
            .setLuminance(0)
            .setTextures("signalindustries:block/cobblestone_bricks")
            .build(new Block("prototype.bricks",config.getInt("BlockIDs.cobblestoneBricks"),Material.stone));

    public static final Block crystalAlloyBricks = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setLuminance(0)
            .setTextures("signalindustries:block/crystal_alloy_bricks")
            .build(new Block("basic.bricks",config.getInt("BlockIDs.crystalAlloyBricks"),Material.metal));

    public static final Block reinforcedCrystalAlloyBricks = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setLuminance(0)
            .setTextures("signalindustries:block/reinforced_alloy_bricks")
            .build(new Block("reinforced.bricks",config.getInt("BlockIDs.reinforcedCrystalAlloyBricks"),Material.metal));

    public static final Block signalumAlloyCoil = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setLuminance(0)
            .setSouthTexture("signalindustries:block/signalum_alloy_coil")
            .setNorthTexture("signalindustries:block/signalum_alloy_coil")
            .setEastTexture("signalindustries:block/signalum_alloy_coil_2")
            .setWestTexture("signalindustries:block/signalum_alloy_coil_2")
            .setTopBottomTextures("signalindustries:block/signalum_alloy_coil_top")
            .build(new Block("signalumAlloyCoil",config.getInt("BlockIDs.signalumAlloyCoil"),Material.metal));

    public static final Block dilithiumCoil = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setLuminance(0)
            .setSideTextures("signalindustries:block/dilithium_coil")
            .setTopBottomTextures("signalindustries:block/dilithium_coil_top")
            .build(new Block("dilithiumCoil",config.getInt("BlockIDs.dilithiumCoil"),Material.metal));

    public static final Block awakenedAlloyCoil = new BlockBuilder(MOD_ID)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .setLuminance(0)
            .setSouthTexture("signalindustries:block/awakened_alloy_coil")
            .setNorthTexture("signalindustries:block/awakened_alloy_coil")
            .setEastTexture("signalindustries:block/awakened_alloy_coil_2")
            .setWestTexture("signalindustries:block/awakened_alloy_coil_2")
            .setTopBottomTextures("signalindustries:block/awakened_alloy_coil_top")
            .build(new Block("awakenedAlloyCoil",config.getInt("BlockIDs.awakenedAlloyCoil"),Material.metal));

    //this has to be after any other block
    //public static final int[] energyTex = TextureHelper.getOrCreateBlockTexture(MOD_ID,"signalum_energy_transparent");
    //public static final int[] burntSignalumTex = TextureHelper.getOrCreateBlockTexture(MOD_ID,"burnt_signalum");//registerFluidTexture(MOD_ID,"signalum_energy",0,4);
    public static final BlockFluid energyFlowing = (BlockFluid) new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/signalum_energy_transparent")
            .build(new BlockFluidFlowing("signalumEnergy",config.getInt("BlockIDs.energyFlowing"),Material.water).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES));
    //BlockHelper.createBlock(MOD_ID,new BlockFluidFlowing("signalumEnergy",config.getInt("BlockIDs.energyFlowing"),Material.water),"signalum_energy_transparent",BlockSounds.DEFAULT,1.0f,1.0f,0).withTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES);
    public static final BlockFluid energyStill = (BlockFluid) new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/signalum_energy_transparent")
            .build(new BlockFluidFlowing("signalumEnergy",config.getInt("BlockIDs.energyStill"),Material.water).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES));
    //BlockHelper.createBlock(MOD_ID,new BlockFluidStill("signalumEnergy",config.getInt("BlockIDs.energyStill"),Material.water),"signalum_energy_transparent",BlockSounds.DEFAULT,1.0f,1.0f,0).withTexCoords(energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1],energyTex[0],energyTex[1]).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES);

    public static final BlockFluid burntSignalumFlowing = (BlockFluid) new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/burnt_signalum")
            .build(new BlockFluidFlowing("burntSignalum",config.getInt("BlockIDs.burntSignalumFlowing"),Material.water).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES));
    public static final BlockFluid burntSignalumStill = (BlockFluid) new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/burnt_signalum")
            .build(new BlockFluidStill("burntSignalum",config.getInt("BlockIDs.burntSignalumStill"),Material.water).withTags(BlockTags.NOT_IN_CREATIVE_MENU,BlockTags.PLACE_OVERWRITES));

    public static final Item signalumCrystalEmpty = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/signalumcrystalempty")
            .setStackSize(1)
            .setItemModel((item)->new ItemModelStandard(item,MOD_ID))
            .build(new ItemSignalumCrystal("signalumCrystalEmpty",config.getInt("ItemIDs.signalumCrystalEmpty")));
    public static final Item signalumCrystal = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/signalumcrystal")
            .setStackSize(1)
            .setItemModel((item)->new ItemModelStandard(item,MOD_ID))
            .build(new ItemSignalumCrystal("signalumCrystal",config.getInt("ItemIDs.signalumCrystal")));
    public static final Item volatileSignalumCrystal = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/volatile_signalum_crystal")
            .setStackSize(4)
            .setItemModel((item)->new ItemModelStandard(item,MOD_ID))
            .build(new ItemSignalumCrystalVolatile("volatileSignalumCrystal",config.getInt("ItemIDs.volatileSignalumCrystal")));
    public static final Item rawSignalumCrystal = simpleItem("rawSignalumCrystal","signalindustries:item/rawsignalumcrystal");

    public static final Item awakenedSignalumCrystal = simpleItem("awakenedSignalumCrystal","signalindustries:item/awakenedsignalumcrystal").setMaxStackSize(1);
    public static final Item awakenedSignalumFragment = simpleItem("awakenedSignalumFragment","signalindustries:item/awakenedsignalumfragment");

    public static final Item coalDust = simpleItem("coalDust", "signalindustries:item/coaldust");
    public static final Item netherCoalDust = simpleItem("netherCoalDust","signalindustries:item/nethercoaldust");
    public static final Item tinyNetherCoalDust = simpleItem("tinyNetherCoalDust","signalindustries:item/tiny_nether_coal_dust");
    public static final Item emptySignalumCrystalDust = simpleItem("emptySignalumCrystalDust","signalumCrystalDust","signalindustries:item/emptysignalumdust");
    public static final Item saturatedSignalumCrystalDust = simpleItem("saturatedSignalumCrystalDust","signalindustries:item/saturatedsignalumdust");
    public static final Item ironPlateHammer = simpleItem("ironPlateHammer","signalindustries:item/platehammer").setMaxStackSize(1);
    public static final Item cobblestonePlate = simpleItem("cobblestonePlate","signalindustries:item/cobblestoneplate");
    public static final Item stonePlate = simpleItem("stonePlate","signalindustries:item/stoneplate");
    public static final Item crystalAlloyPlate = simpleItem("crystalAlloyPlate","signalindustries:item/crystalalloyplate");
    public static final Item steelPlate = simpleItem("steelPlate","signalindustries:item/steelplate");
    public static final Item reinforcedCrystalAlloyPlate = simpleItem("reinforcedCrystalAlloyPlate","signalindustries:item/reinforcedcrystalalloyplate");
    public static final Item saturatedSignalumAlloyPlate = simpleItem("saturatedSignalumAlloyPlate","signalindustries:item/saturatedsignalumalloyplate");
    public static final Item dilithiumPlate = simpleItem("dilithiumPlate", "signalindustries:item/dilithiumplate");
    public static final Item voidAlloyPlate = simpleItem("voidAlloyPlate", "signalindustries:item/void_alloy_plate");
    public static final Item awakenedAlloyPlate = simpleItem("awakenedAlloyPlate", "signalindustries:item/awakened_alloy_plate");
    public static final Item crystalAlloyIngot = simpleItem("crystalAlloyIngot", "signalindustries:item/crystalalloy");
    public static final Item reinforcedCrystalAlloyIngot = simpleItem("reinforcedCrystalAlloyIngot", "signalindustries:item/reinforcedcrystalalloy");
    public static final Item saturatedSignalumAlloyIngot = simpleItem("saturatedSignalumAlloyIngot", "signalindustries:item/saturatedsignalumalloy");
    public static final Item voidAlloyIngot = simpleItem("voidAlloyIngot", "signalindustries:item/void_alloy");
    public static final Item awakenedAlloyIngot = simpleItem("awakenedAlloyIngot", "signalindustries:item/awakened_alloy");
    public static final Item diamondCuttingGear = simpleItem("diamondCuttingGear", "signalindustries:item/diamondcuttinggear");
    public static final Item signalumCuttingGear = simpleItem("signalumCuttingGear", "signalindustries:item/signalumcuttinggear");

    public static final Block portalEternity = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/reality_fabric")
            .setBlockSound(BlockSounds.GLASS)
            .setLuminance(1)
            .build(new BlockPortal("eternityPortal",config.getInt("BlockIDs.portalEternity"),config.getInt("Other.eternityDimId"),Block.bedrock.id,Block.fire.id));

    //BlockHelper.createBlock(MOD_ID,new BlockPortal("eternityPortal",config.getInt("BlockIDs.portalEternity"),3,Block.bedrock.id,Block.fire.id),"reality_fabric",BlockSounds.GLASS,1.0f,1.0f,1);
    public static final Block realityFabric = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/reality_fabric")
            .setBlockSound(BlockSounds.STONE)
            .setHardness(150)
            .setResistance(50000)
            .setLuminance(0)
            .build(new BlockUndroppable("realityFabric",config.getInt("BlockIDs.realityFabric"),Material.stone));
    //BlockHelper.createBlock(MOD_ID,new BlockUndroppable("realityFabric",config.getInt("BlockIDs.realityFabric"),Material.dirt),"reality_fabric",BlockSounds.STONE,150f,50000f,0);
    public static final Block rootedFabric = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/rooted_fabric")
            .setBlockSound(BlockSounds.STONE)
            .setHardness(50)
            .setResistance(50000)
            .setLuminance(0)
            .build(new Block("rootedFabric",config.getInt("BlockIDs.rootedFabric"),Material.stone).withTags(BlockTags.MINEABLE_BY_PICKAXE));

    //public static final int[][] railTex = new int[][]{TextureHelper.getOrCreateBlockTexture(MOD_ID,"dilithium_rail_unpowered"),TextureHelper.getOrCreateBlockTexture(MOD_ID,"dilithium_rail")};

    public static final Block dilithiumRail = new BlockBuilder(MOD_ID)
            .setLuminance(0)
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(50)
            .setTextures("signalindustries:block/dilithium_rail_unpowered")
            .build(new BlockDilithiumRail("dilithiumRail",config.getInt("BlockIDs.dilithiumRail"),true));


    public static final Item realityString = simpleItem("realityString","signalindustries:item/stringofreality");
    public static final Item dilithiumShard = simpleItem("dilithiumShard","signalindustries:item/dilithiumshard");
    public static final Item monsterShard = simpleItem("monsterShard","signalindustries:item/monstershard");
    public static final Item infernalFragment = simpleItem("infernalFragment","signalindustries:item/infernalfragment");
    public static final Item evilCatalyst = simpleItem("evilCatalyst","signalindustries:item/evilcatalyst").setMaxStackSize(4);
    public static final Item infernalEye = simpleItem("infernalEye","signalindustries:item/infernaleye").setMaxStackSize(4);
    public static final Item dimensionalShard = simpleItem("dimensionalShard","signalindustries:item/dimensionalshard");
    public static final Item warpOrb = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/warporb")
            .setStackSize(1)
            .setItemModel((item)->new ItemModelStandard(item,MOD_ID))
            .build(new ItemWarpOrb("warpOrb",config.getInt("ItemIDs.warpOrb")));

    public static final Block eternalTreeLog = new BlockBuilder(MOD_ID)
            .setHardness(75f)
            .setResistance(50000)
            .setLuminance(12)
            .setTopBottomTextures("signalindustries:block/eternal_tree_log_top")
            .setSideTextures("signalindustries:block/eternal_tree_log")
            .setBlockSound(BlockSounds.WOOD)
            .build(new BlockEternalTreeLog("eternalTreeLog",config.getInt("BlockIDs.eternalTreeLog"),Material.wood));//new BlockBuilder(MOD_ID) //BlockHelper.createBlock(MOD_ID,new BlockEternalTreeLog(key("eternalTreeLog",config.getInt("BlockIDs.eternalTreeLog"),Material.wood),"eternal_tree_log_top","eternal_tree_log",BlockSounds.WOOD, 75f,50000f,1);

    public static final Block fueledEternalTreeLog = new BlockBuilder(MOD_ID)
            .setUnbreakable()
            .setResistance(18000000)
            .setLuminance(15)
            .setTopBottomTextures("signalindustries:block/fueled_eternal_tree_log_top")
            .setSideTextures("signalindustries:block/fueled_eternal_tree_log")
            .setBlockSound(BlockSounds.WOOD)
            .build(new BlockEternalTreeLog("fueledEternalTreeLog",config.getInt("BlockIDs.fueledEternalTreeLog"),Material.wood));

    public static final Block glowingObsidian = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/glowing_obsidian")
            .setBlockSound(BlockSounds.STONE)
            .setHardness(2)
            .setResistance(1200)
            .setLuminance(10)
            .build(new BlockGlowingObsidian("glowingObsidian",config.getInt("BlockIDs.glowingObsidian"),Material.stone));
        //BlockHelper.createBlock(MOD_ID,new Block(key("glowingObsidian",config.getInt("BlockIDs.glowingObsidian"),Material.stone),"glowing_obsidian",BlockSounds.STONE, 50f,1200f,1.0f/2.0f);

    public static final Block uvLamp = new BlockBuilder(MOD_ID)
            .setTextures("signalindustries:block/uv_lamp_inactive")
            .setBlockSound(BlockSounds.METAL)
            .setHardness(1)
            .setResistance(3)
            .build(new BlockUVLamp("uvLamp",config.getInt("BlockIDs.uvLamp"),Material.metal));

    public static final ArmorMaterial armorPrototypeHarness = ArmorHelper.createArmorMaterial(SignalIndustries.MOD_ID,"signalumprototypeharness",1200,10,10,10,10);
    public static final ArmorMaterial armorSignalumPowerSuit = ArmorHelper.createArmorMaterial(SignalIndustries.MOD_ID,"signalumpowersuit",9999,50,50,50,50);

    public static final ItemArmorTiered signalumPrototypeHarness = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/harness")
            .build(new ItemSignalumPrototypeHarness("basic.prototypeHarness",config.getInt("ItemIDs.signalumPrototypeHarness"),armorPrototypeHarness,1, Tier.BASIC));

    public static final ItemArmorTiered signalumPrototypeHarnessGoggles = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/goggles")
            .build(new ItemSignalumPrototypeHarness("basic.prototypeHarnessGoggles",config.getInt("ItemIDs.signalumPrototypeHarnessGoggles"),armorPrototypeHarness,0, Tier.BASIC));

    public static final ToolMaterial toolMaterialBasic = new ToolMaterial().setDurability(9999).setMiningLevel(3).setEfficiency(25,50);
    public static final ToolMaterial toolMaterialReinforced = new ToolMaterial().setDurability(9999).setMiningLevel(4).setEfficiency(45,80);
    public static final ToolMaterial toolMaterialAwakened = new ToolMaterial().setDurability(9999).setMiningLevel(5).setEfficiency(60,100);

    public static final Item basicSignalumDrill = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/signalum_drill")
            .build(new ItemSignalumDrill("basic.signalumDrill",config.getInt("ItemIDs.basicSignalumDrill"),Tier.BASIC,toolMaterialBasic));

    public static final Item reinforcedSignalumDrill = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/signalum_drill_reinforced")
            .build(new ItemSignalumDrill("reinforced.signalumDrill", config.getInt("ItemIDs.reinforcedSignalumDrill"), Tier.REINFORCED, toolMaterialReinforced));

    public static final Item fuelCell = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/fuelcellempty")
            .setStackSize(1)
            .build(new ItemFuelCell("fuelCell",config.getInt("ItemIDs.fuelCell")));

    public static final Item nullTrigger = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/trigger")
            .setStackSize(1)
            .build(new ItemTrigger("trigger.null",config.getInt("ItemIDs.nullTrigger")));

    public static final Item romChipProjectile = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/chip1")
            .build(new ItemRomChip("romChip.projectile",config.getInt("ItemIDs.romChipProjectile")));

    public static final Item romChipBoost = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/chip2")
            .build(new ItemRomChip("romChip.boost",config.getInt("ItemIDs.romChipBoost")));

    public static final Item energyCatalyst = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/energycatalyst")
            .build(new Item("energyCatalyst",config.getInt("ItemIDs.energyCatalyst")));

    public static final Item signalumSaber = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/signalumsaberunpowered")
            .build(new ItemSignalumSaber("reinforced.signalumSaber",config.getInt("ItemIDs.signalumSaber"), Tier.REINFORCED, ToolMaterial.stone));

    public static final Item pulsar = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/pulsaractive")
            .setStackSize(1)
            .build(new ItemPulsar("reinforced.pulsar",config.getInt("ItemIDs.pulsar"), Tier.REINFORCED));

    //TODO: WIP
    //public static final Item sunriseDawn = ItemHelper.createItem(MOD_ID,new ItemSunriseDawn("awakened.sunriseDawn",config.getInt("ItemIDs.sunriseDawn"), ToolMaterial.steel, Tier.AWAKENED),"sunrise_dawn").setMaxStackSize(1);

    public static final ItemSignalumPowerSuit signalumPowerSuitHelmet = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/signalumpowersuit_helmet")
            .build(new ItemSignalumPowerSuit("reinforced.signalumpowersuit.helmet",config.getInt("ItemIDs.signalumPowerSuitHelmet"),armorSignalumPowerSuit,0,Tier.REINFORCED));

    public static final ItemSignalumPowerSuit signalumPowerSuitChestplate = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/signalumpowersuit_chestplate")
            .build(new ItemSignalumPowerSuit("reinforced.signalumpowersuit.chestplate",config.getInt("ItemIDs.signalumPowerSuitChestplate"),armorSignalumPowerSuit,1,Tier.REINFORCED));

    public static final ItemSignalumPowerSuit signalumPowerSuitLeggings = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/signalumpowersuit_leggings")
            .build(new ItemSignalumPowerSuit("reinforced.signalumpowersuit.leggings",config.getInt("ItemIDs.signalumPowerSuitLeggings"),armorSignalumPowerSuit,2,Tier.REINFORCED));

    public static final ItemSignalumPowerSuit signalumPowerSuitBoots = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/signalumpowersuit_boots")
            .build(new ItemSignalumPowerSuit("reinforced.signalumpowersuit.boots",config.getInt("ItemIDs.signalumPowerSuitBoots"),armorSignalumPowerSuit,3,Tier.REINFORCED));

    //public static final Item testingAttachment = ItemHelper.createItem(MOD_ID,new ItemAttachment(config.getInt("ItemIDs.testingAttachment"), listOf(AttachmentPoint.ANY)),"attachment.testingAttachment","energyorb");
    //implicit max stack size of 1 (defined in ItemAttachment)
    public static final Item pulsarAttachment = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/pulsar_attachment")
            .build(new ItemPulsarAttachment("reinforced.attachment.pulsar",config.getInt("ItemIDs.pulsarAttachment"), listOf(AttachmentPoint.ARM_FRONT), Tier.REINFORCED));

    public static final Item extendedEnergyPack = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/extended_energy_pack")
            .build(new ItemTieredAttachment("reinforced.attachment.extendedEnergyPack",config.getInt("ItemIDs.extendedEnergyPack"), listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED));

    public static final Item crystalWings = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/wings")
            .build(new ItemWingsAttachment("reinforced.attachment.wings",config.getInt("ItemIDs.crystalWings"), listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED));

    public static final Item basicBackpack = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/basic_backpack")
            .build(new ItemBackpackAttachment("basic.attachment.backpack",config.getInt("ItemIDs.basicBackpack"), listOf(AttachmentPoint.CORE_BACK), Tier.BASIC));

    public static final Item reinforcedBackpack = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/reinforced_backpack")
            .build(new ItemBackpackAttachment("reinforced.attachment.backpack",config.getInt("ItemIDs.reinforcedBackpack"), listOf(AttachmentPoint.CORE_BACK), Tier.REINFORCED));

    public static final Item nightVisionLens = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/night_vision_goggles")
            .build(new ItemNVGAttachment("reinforced.attachment.nightVisionLens",config.getInt("ItemIDs.nightVisionLens"), listOf(AttachmentPoint.HEAD_TOP), Tier.REINFORCED));

    public static final Item movementBoosters = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/movement_boosters")
            .build(new ItemMovementBoostersAttachment("reinforced.attachment.movementBoosters",config.getInt("ItemIDs.movementBoosters"), listOf(AttachmentPoint.BOOT_BACK), Tier.REINFORCED));

    public static final ItemPortableWorkbench portableWorkbench = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/portable_workbench")
            .setStackSize(1)
            .build(new ItemPortableWorkbench("basic.portableWorkbench",config.getInt("ItemIDs.portableWorkbench"),Tier.BASIC));

    public static final ItemSmartWatch smartWatch = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/smartwatch")
            .setStackSize(1)
            .build(new ItemSmartWatch("basic.smartWatch",config.getInt("ItemIDs.smartWatch"),Tier.BASIC));

    public static final SuitBaseAbility testAbility = new TestingAbility();
    public static final SuitBaseEffectAbility testEffectAbility = new TestingEffectAbility();
    //public static final SuitBaseEffectAbility clockworkAbility = new ClockworkAbility();
    public static final SuitBaseAbility boostAbility = new BoostAbility();
    public static final SuitBaseAbility projectileAbility = new ProjectileAbility();

    //public static final Item testingAbilityContainer = ItemHelper.createItem(MOD_ID,new ItemWithAbility(config.getInt("ItemIDs.testingAbilityContainer"),testEffectAbility),"testingAbilityItem","testingability");
    //public static final Item clockworkAbilityContainer = ItemHelper.createItem(MOD_ID,new ItemWithAbility(config.getInt("ItemIDs.clockworkAbilityContainer"),clockworkAbility),"clockworkAbilityContainer","ability12");
    public static final Item boostAbilityContainer = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/ability2")
            .setStackSize(1)
            .build(new ItemWithAbility("boostAbilityContainer",config.getInt("ItemIDs.boostAbilityContainer"),boostAbility));

    public static final Item projectileAbilityContainer = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/ability1")
            .setStackSize(1)
            .build(new ItemWithAbility("projectileAbilityContainer",config.getInt("ItemIDs.projectileAbilityContainer"),projectileAbility));

    public static final Item abilityModule = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/abilitymodule")
            .build(new ItemAbilityModule("abilityModule",config.getInt("ItemIDs.abilityModule"),Tier.REINFORCED));

    public static final Item awakenedAbilityModule = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/awakenedmodule")
            .build(new ItemAbilityModule("awakenedAbilityModule",config.getInt("ItemIDs.awakenedAbilityModule"),Tier.AWAKENED));

    public static final Item crystalChip = simpleItem("crystalChip","signalindustries:item/crystal_chip");
    public static final Item pureCrystalChip = simpleItem("pureCrystalChip","signalindustries:item/pure_crystal_chip");
    public static final Item basicEnergyCore = simpleItem("basicEnergyCore","signalindustries:item/basic_energy_core");
    public static final Item reinforcedEnergyCore = simpleItem("reinforcedEnergyCore","signalindustries:item/reinforced_energy_core");
    public static final Item basicDrillBit = simpleItem("basicDrillBit","signalindustries:item/basic_drill_bit");
    public static final Item reinforcedDrillBit = simpleItem("reinforcedDrillBit","signalindustries:item/reinforced_drill_bit");
    public static final Item basicDrillCasing = simpleItem("basicDrillCasing","signalindustries:item/basic_drill_casing");
    public static final Item reinforcedDrillCasing = simpleItem("reinforcedDrillCasing","signalindustries:item/reinforced_drill_casing");
    public static final Item pulsarShell = simpleItem("pulsarShell","signalindustries:item/pulsar_shell");
    public static final Item pulsarInnerCore = simpleItem("pulsarInnerCore","signalindustries:item/pulsar_inner_core");
    public static final Item pulsarOuterCore = simpleItem("pulsarOuterCore","signalindustries:item/pulsar_outer_core");
    public static final Item itemManipulationCircuit = simpleItem("itemManipulationCircuit","signalindustries:item/item_manipulation_circuit");
    public static final Item fluidManipulationCircuit = simpleItem("fluidManipulationCircuit","signalindustries:item/fluid_manipulation_circuit");
    public static final Item dilithiumControlCore = simpleItem("dilithiumControlCore","signalindustries:item/dilithium_control_core");
    public static final Item warpManipulatorCircuit = simpleItem("warpManipulatorCircuit","signalindustries:item/warp_manipulator_circuit");
    public static final Item dilithiumChip = simpleItem("dilithiumChip","signalindustries:item/dilithium_chip");
    public static final Item dimensionalChip = simpleItem("dimensionalChip","signalindustries:item/dimensional_chip");
    public static final Item attachmentPoint = simpleItem("attachmentPoint","signalindustries:item/attachment_point");
    public static final Item meteorTracker = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/meteor_tracker_uncalibrated")
            .build(new ItemMeteorTracker("meteorTracker",config.getInt("ItemIDs.meteorTracker")));
    public static final Item blankAbilityModule = simpleItem("blankAbilityModule","signalindustries:item/blank_module");
    public static final Item abilityContainerCasing = simpleItem("abilityContainerCasing","signalindustries:item/abilitycontainercasing");
    public static final Item blankChip = simpleItem("blankChip","romChip.blank","signalindustries:item/blank_chip");
    public static final Item positionMemoryChip = new ItemBuilder(MOD_ID)
            .setIcon("signalindustries:item/position_chip")
            .build(new ItemPositionChip("romChip.position",config.getInt("ItemIDs.positionMemoryChip")));

    //public static final int[] energyOrbTex = TextureHelper.getOrCreateItemTexture(MOD_ID,"energyorb");

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
        /*textures.put(Tier.PROTOTYPE.name()+".extractor.active",new BlockTexture(MOD_ID).setTopAndBottom("prototype_blank").setSides("extractor_prototype_side_active"));
        textures.put(Tier.BASIC.name()+".extractor.active",new BlockTexture(MOD_ID).setTopAndBottom("basic_blank").setSides("extractor_basic_side_active"));
        textures.put(Tier.REINFORCED.name()+".extractor.active",new BlockTexture(MOD_ID).setTopAndBottom("reinforced_blank").setSides("extractor_reinforced_side_active"));
        textures.put(Tier.PROTOTYPE.name()+".extractor.active.overlay",new BlockTexture(MOD_ID).setSides("extractor_overlay"));
        textures.put(Tier.BASIC.name()+".extractor.active.overlay",new BlockTexture(MOD_ID).setSides("extractor_overlay"));
        textures.put(Tier.REINFORCED.name()+".extractor.active.overlay",new BlockTexture(MOD_ID).setSides("extractor_overlay"));

        textures.put(Tier.PROTOTYPE.name()+".crusher.active",new BlockTexture(MOD_ID).setAll("prototype_blank").setTopTexture("signalindustries:block/crusher_prototype_top_active").setSouthTexture("signalindustries:block/crusher_prototype_side"));
        textures.put(Tier.BASIC.name()+".crusher.active",new BlockTexture(MOD_ID).setAll("basic_blank").setTopTexture("signalindustries:block/crusher_basic_top_active").setSouthTexture("signalindustries:block/crusher_basic_side"));
        textures.put(Tier.REINFORCED.name()+".crusher.active",new BlockTexture(MOD_ID).setAll("reinforced_blank").setTopTexture("signalindustries:block/crusher_reinforced_top_active").setSouthTexture("signalindustries:block/crusher_reinforced_side"));
        textures.put(Tier.PROTOTYPE.name()+".crusher.active.overlay",new BlockTexture(MOD_ID).setTopTexture("signalindustries:block/crusher_overlay"));
        textures.put(Tier.BASIC.name()+".crusher.active.overlay",new BlockTexture(MOD_ID).setTopTexture("signalindustries:block/crusher_overlay"));
        textures.put(Tier.REINFORCED.name()+".crusher.active.overlay",new BlockTexture(MOD_ID).setTopTexture("signalindustries:block/crusher_overlay"));

        textures.put(Tier.PROTOTYPE.name()+".alloySmelter.active",new BlockTexture(MOD_ID).setAll("prototype_blank").setSouthTexture("signalindustries:block/alloy_smelter_prototype_active"));
        textures.put(Tier.BASIC.name()+".alloySmelter.active",new BlockTexture(MOD_ID).setAll("basic_blank").setSouthTexture("signalindustries:block/alloy_smelter_basic_active"));
        textures.put(Tier.PROTOTYPE.name()+".alloySmelter.active.overlay",new BlockTexture(MOD_ID).setSouthTexture("signalindustries:block/alloy_smelter_overlay"));
        textures.put(Tier.BASIC.name()+".alloySmelter.active.overlay",new BlockTexture(MOD_ID).setSouthTexture("signalindustries:block/alloy_smelter_overlay"));

        textures.put(Tier.BASIC.name()+".inductionSmelter.active",new BlockTexture(MOD_ID).setAll("basic_blank").setSouthTexture("signalindustries:block/basic_induction_smelter_front_active").setTopTexture("signalindustries:block/basic_induction_smelter_top_active"));
        textures.put(Tier.BASIC.name()+".inductionSmelter.active.overlay",new BlockTexture(MOD_ID).setSouthTexture("signalindustries:block/induction_smelter_front_overlay").setTopTexture("signalindustries:block/induction_smelter_top_overlay"));

        textures.put(Tier.PROTOTYPE.name()+".plateFormer.active",new BlockTexture(MOD_ID).setAll("prototype_blank").setSouthTexture("signalindustries:block/plate_former_prototype_active"));
        textures.put(Tier.BASIC.name()+".plateFormer.active",new BlockTexture(MOD_ID).setAll("basic_blank").setSouthTexture("signalindustries:block/plate_former_basic_active"));
        textures.put(Tier.REINFORCED.name()+".plateFormer.active",new BlockTexture(MOD_ID).setAll("reinforced_blank").setSouthTexture("signalindustries:block/plate_former_reinforced_active"));
        textures.put(Tier.PROTOTYPE.name()+".plateFormer.active.overlay",new BlockTexture(MOD_ID).setSouthTexture("signalindustries:block/plate_former_overlay"));
        textures.put(Tier.BASIC.name()+".plateFormer.active.overlay",new BlockTexture(MOD_ID).setSouthTexture("signalindustries:block/plate_former_overlay"));
        textures.put(Tier.REINFORCED.name()+".plateFormer.active.overlay",new BlockTexture(MOD_ID).setSouthTexture("signalindustries:block/reinforced_plate_former_overlay"));

        textures.put(Tier.PROTOTYPE.name()+".crystalCutter.active",new BlockTexture(MOD_ID).setAll("prototype_blank").setSouthTexture("signalindustries:block/crystal_cutter_prototype_active"));
        textures.put(Tier.BASIC.name()+".crystalCutter.active",new BlockTexture(MOD_ID).setAll("basic_blank").setSouthTexture("signalindustries:block/crystal_cutter_basic_active"));
        textures.put(Tier.REINFORCED.name()+".crystalCutter.active",new BlockTexture(MOD_ID).setAll("reinforced_blank").setSouthTexture("signalindustries:block/crystal_cutter_reinforced_active"));

        textures.put(Tier.PROTOTYPE.name()+".crystalCutter.active.overlay",new BlockTexture(MOD_ID).setSouthTexture("signalindustries:block/cutter_overlay"));
        textures.put(Tier.BASIC.name()+".crystalCutter.active.overlay",new BlockTexture(MOD_ID).setSouthTexture("signalindustries:block/cutter_overlay"));
        textures.put(Tier.REINFORCED.name()+".crystalCutter.active.overlay",new BlockTexture(MOD_ID).setSouthTexture("signalindustries:block/reinforced_cutter_overlay"));

        textures.put(Tier.BASIC.name()+".crystalChamber.active",new BlockTexture(MOD_ID).setAll("basic_blank").setSouthTexture("signalindustries:block/basic_crystal_chamber_side_active"));
        textures.put(Tier.BASIC.name()+".crystalChamber.active.overlay",new BlockTexture(MOD_ID).setSouthTexture("signalindustries:block/chamber_overlay"));

        textures.put(Tier.REINFORCED.name()+".crystalChamber.active",new BlockTexture(MOD_ID).setAll("reinforced_blank").setSouthTexture("signalindustries:block/reinforced_crystal_chamber_side_active"));
        textures.put(Tier.REINFORCED.name()+".crystalChamber.active.overlay",new BlockTexture(MOD_ID).setSouthTexture("signalindustries:block/reinforced_chamber_overlay"));

        textures.put(Tier.BASIC.name()+".automaticMiner.active.overlay",new BlockTexture(MOD_ID).setSouthTexture("signalindustries:block/auto_miner_overlay"));

        textures.put(Tier.BASIC.name()+".infuser.active",new BlockTexture(MOD_ID).setAll("basic_blank").setSides("infuser_basic_side_active"));
        textures.put(Tier.BASIC.name()+".infuser.active.overlay",new BlockTexture(MOD_ID).setSides("infuser_overlay"));

        textures.put(Tier.BASIC.name()+".wrathBeacon.active",new BlockTexture(MOD_ID).setAll("basic_blank").setSides("wrath_beacon_active"));
        textures.put(Tier.REINFORCED.name()+".wrathBeacon.active",new BlockTexture(MOD_ID).setAll("reinforced_blank").setSides("reinforced_wrath_beacon_active"));

        textures.put(Tier.PROTOTYPE.name()+".pump.active",new BlockTexture(MOD_ID).setAll("prototype_blank").setSides("prototype_pump_side").setTopTexture("signalindustries:block/prototype_pump_top"));
        textures.put(Tier.BASIC.name()+".pump.active",new BlockTexture(MOD_ID).setAll("basic_blank").setSides("basic_pump_side_empty").setTopTexture("signalindustries:block/basic_pump_top_empty"));

        textures.put(Tier.REINFORCED.name()+".signalumReactorCore.active",new BlockTexture(MOD_ID).setAll("reinforced_blank").setSides("signalum_reactor_side_active").setSouthTexture("signalindustries:block/signalum_reactor_front_active"));
        textures.put(Tier.REINFORCED.name()+".signalumReactorCore.active.overlay",new BlockTexture(MOD_ID).setSides("reactor_side_overlay").setSouthTexture("signalindustries:block/reactor_overlay"));

        textures.put("dimensionalAnchor.active",new BlockTexture(MOD_ID).setSides("dimensional_anchor").setBottomTexture("dimensional_anchor_bottom").setTopTexture("dimensional_anchor_top"));
        textures.put("dimensionalAnchor.active.overlay",new BlockTexture(MOD_ID).setSides("anchor_overlay").setBottomTexture("anchor_blank_overlay").setTopTexture("anchor_top_overlay"));

        textures.put("dilithiumStabilizer.active",new BlockTexture(MOD_ID).setAll("reinforced_blank").setSides("dilithium_stabilizer_side_active").setSouthTexture("dilithium_top_active"));
        textures.put("dilithiumStabilizer.active.overlay",new BlockTexture(MOD_ID).setSides("stabilizer_overlay").setSouthTexture("dilithium_machine_overlay"));
        textures.put("dilithiumStabilizer.vertical",new BlockTexture(MOD_ID).setAll("reinforced_blank").setSides("dilithium_stabilizer_side_inactive").setTopTexture("dilithium_top_inactive"));
        textures.put("dilithiumStabilizer.vertical.active",new BlockTexture(MOD_ID).setAll("reinforced_blank").setSides("dilithium_stabilizer_side_active").setTopTexture("dilithium_top_active"));
        textures.put("dilithiumStabilizer.vertical.active.overlay",new BlockTexture(MOD_ID).setSides("stabilizer_overlay").setTopTexture("dilithium_machine_overlay"));

        textures.put(Tier.BASIC.name()+"booster.active",new BlockTexture(MOD_ID).setAll("basic_blank").setSides("basic_booster_side_active").setSouthTexture("basic_booster_top_active"));
        textures.put(Tier.BASIC.name()+"booster.active.overlay",new BlockTexture(MOD_ID).setSides("basic_booster_overlay").setSouthTexture("basic_booster_overlay_top"));
        textures.put(Tier.REINFORCED.name()+"booster.active",new BlockTexture(MOD_ID).setAll("reinforced_blank").setSides("dilithium_booster_side_active").setSouthTexture("dilithium_top_active"));
        textures.put(Tier.REINFORCED.name()+"booster.active.overlay",new BlockTexture(MOD_ID).setSides("booster_overlay").setSouthTexture("dilithium_machine_overlay"));
        textures.put(Tier.AWAKENED.name()+"booster.active",new BlockTexture(MOD_ID).setAll("awakened_blank").setSides("awakened_booster_side_active").setSouthTexture("awakened_booster_top_active"));
        textures.put(Tier.AWAKENED.name()+"booster.active.overlay",new BlockTexture(MOD_ID).setSides("awakened_booster_overlay").setSouthTexture("dilithium_machine_overlay"));

        textures.put("uvLamp.active",new BlockTexture(MOD_ID).setAll("uv_lamp"));

        textures.put(Tier.PROTOTYPE.name()+".inserter.vertical",new BlockTexture(MOD_ID).setAll("prototype_blank").setBottomTexture("inserter_output").setTopTexture("inserter_input"));
        textures.put(Tier.BASIC.name()+".inserter.vertical",new BlockTexture(MOD_ID).setAll("basic_blank").setBottomTexture("basic_inserter_output").setTopTexture("basic_inserter_input"));

        textures.put(Tier.BASIC.name()+".externalIo",new BlockTexture(MOD_ID).setAll("external_io_blank").setTopTexture("external_io_input").setBottomTexture("external_io_output").setSouthTexture("external_io_both"));
        textures.put(Tier.REINFORCED.name()+".externalIo",new BlockTexture(MOD_ID).setAll("reinforced_external_io_blank").setTopTexture("reinforced_external_io_input").setBottomTexture("reinforced_external_io_output").setSouthTexture("reinforced_external_io_both"));

        textures.put(Tier.BASIC.name()+".assembler.active",new BlockTexture(MOD_ID).setAll("basic_assembler_side_active").setSouthTexture("basic_assembler_front_active"));
        textures.put(Tier.BASIC.name()+".assembler.active.overlay",new BlockTexture(MOD_ID).setAll("assembler_overlay_side").setSouthTexture("assembler_overlay_front"));

        textures.put(Tier.REINFORCED.name()+".centrifuge.active",new BlockTexture(MOD_ID).setAll("reinforced_blank").setTopTexture("reinforced_centrifuge_closed").setSouthTexture("reinforced_centrifuge_front_active"));
        textures.put(Tier.REINFORCED.name()+".centrifuge.active.overlay",new BlockTexture(MOD_ID).setSouthTexture("centrifuge_overlay"));
        textures.put(Tier.REINFORCED.name()+".centrifuge.loaded",new BlockTexture(MOD_ID).setAll("reinforced_blank").setTopTexture("reinforced_centrifuge_loaded").setSouthTexture("reinforced_centrifuge_front_inactive"));

        textures.put(Tier.REINFORCED.name()+".ignitor.inverted",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_inactive_inverted").setTopTexture("reinforced_ignitor_bottom_inactive").setBottomTexture("reinforced_ignitor_top_inactive"));
        textures.put(Tier.REINFORCED.name()+".ignitor.active",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_active").setTopTexture("reinforced_ignitor_top_active").setBottomTexture("reinforced_ignitor_bottom_active"));
        textures.put(Tier.REINFORCED.name()+".ignitor.active.overlay",new BlockTexture(MOD_ID).setSides("ignitor_1_overlay").setTopTexture("ignitor_7_overlay").setBottomTexture("ignitor_3_overlay"));
        textures.put(Tier.REINFORCED.name()+".ignitor.inverted.active",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_active_inverted").setTopTexture("reinforced_ignitor_bottom_active").setBottomTexture("reinforced_ignitor_top_active"));
        textures.put(Tier.REINFORCED.name()+".ignitor.inverted.active.overlay",new BlockTexture(MOD_ID).setSides("ignitor_2_overlay").setTopTexture("ignitor_3_overlay").setBottomTexture("ignitor_7_overlay"));
        textures.put(Tier.REINFORCED.name()+".ignitor.ready",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_ready").setTopTexture("reinforced_ignitor_top_ready").setBottomTexture("reinforced_ignitor_bottom_ready"));
        textures.put(Tier.REINFORCED.name()+".ignitor.ready.overlay",new BlockTexture(MOD_ID).setSides("ignitor_5_overlay").setTopTexture("ignitor_8_overlay").setBottomTexture("ignitor_4_overlay"));
        textures.put(Tier.REINFORCED.name()+".ignitor.inverted.ready",new BlockTexture(MOD_ID).setSides("reinforced_ignitor_ready_inverted").setTopTexture("reinforced_ignitor_bottom_ready").setBottomTexture("reinforced_ignitor_top_ready"));
        textures.put(Tier.REINFORCED.name()+".ignitor.inverted.ready.overlay",new BlockTexture(MOD_ID).setSides("ignitor_6_overlay").setTopTexture("ignitor_4_overlay").setBottomTexture("ignitor_8_overlay"));*/

        NetworkHelper.register(PacketOpenMachineGUI.class, true, true);
    }

    public SignalIndustries(){

        //RecipeFIleLoader.load("/assets/signalindustries/recipes/recipes.txt",mapOf(new String[]{"SignalIndustries"},new String[]{"sunsetsatellite.signalindustries.SignalIndustries"}));
        /*BlockModelDispatcher.getInstance().addDispatch(dilithiumRail,new BlockModelRenderBlocks(9));
        BlockModelDispatcher.getInstance().addDispatch(energyStill,new BlockModelRenderBlocks(4));
        BlockModelDispatcher.getInstance().addDispatch(energyFlowing,new BlockModelRenderBlocks(4));
        BlockModelDispatcher.getInstance().addDispatch(burntSignalumFlowing,new BlockModelRenderBlocks(4));
        BlockModelDispatcher.getInstance().addDispatch(burntSignalumStill,new BlockModelRenderBlocks(4));*/
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

        CommandHelper.createCommand(new NBTEditCommand());
        CommandHelper.createCommand(new RecipeReloadCommand("recipes"));
        CommandHelper.createCommand(new StructureCommand("structure","struct"));

        EntityHelper.createEntity(EntityCrystal.class,347,"signalumCrystal",() -> new SnowballRenderer(volatileSignalumCrystal));
        EntityHelper.createEntity(EntityEnergyOrb.class,349,"energyOrb", () -> new SnowballRenderer(TextureRegistry.getTexture("signalindustries:item/energyorb")));
        EntityHelper.createEntity(EntitySunbeam.class,349,"sunBeam", SunbeamRenderer::new);
        EntityHelper.createEntity(EntityFallingMeteor.class,350,"fallingMeteor", FallingMeteorRenderer::new);

        if (FabricLoaderImpl.INSTANCE.isModLoaded("retrostorage")) {
            new ReSPlugin().initializePlugin(LOGGER);
        }

        addEntities();
        //crafting recipes in RecipeHandlerCraftingSI

    }

    //idea got *too smart* recently and now considers anything after accessor stubs "unreachable" due to the throw statement in them that will never be triggered
    @SuppressWarnings("UnreachableCode")
    private void addEntities(){
        EntityHelper.createSpecialTileEntity(TileEntityConduit.class, "Conduit", RenderFluidInConduit::new);

        EntityHelper.createSpecialTileEntity(TileEntityFluidConduit.class,"Fluid Conduit", RenderFluidInConduit::new);

        EntityHelper.createSpecialTileEntity(TileEntityItemConduit.class, "Item Conduit", RenderItemsInConduit::new);
        EntityHelper.createSpecialTileEntity(TileEntityEnergyCell.class, "Energy Cell", RenderFluidInBlock::new);
        EntityHelper.createSpecialTileEntity(TileEntitySIFluidTank.class, "SI Fluid Tank", RenderFluidInBlock::new);
        EntityHelper.createSpecialTileEntity(TileEntityReinforcedExtractor.class, "Extraction Manifold", RenderMultiblock::new);
        EntityHelper.createSpecialTileEntity(TileEntityAssembler.class, "SI Assembler", RenderAssemblerItemSprite3D::new);
        EntityHelper.createSpecialTileEntity(TileEntityDimensionalAnchor.class, "Dimensional Anchor", RenderMultiblock::new);
        EntityHelper.createSpecialTileEntity(TileEntityStorageContainer.class, "Storage Container", RenderStorageContainer::new);
        EntityHelper.createSpecialTileEntity(TileEntityAutoMiner.class, "Automatic Miner", RenderAutoMiner::new);
        EntityHelper.createSpecialTileEntity(TileEntitySignalumReactor.class, "Signalum Reactor", RenderSignalumReactor::new);
        EntityHelper.createSpecialTileEntity(TileEntityEnergyInjector.class, "Energy Injector", RenderEnergyInjector::new);
        EntityHelper.createSpecialTileEntity(TileEntityReinforcedWrathBeacon.class, "Reinforced Wrath Beacon", RenderReinforcedWrathBeacon::new);


        EntityHelper.createTileEntity(TileEntityInserter.class, "Inserter");
        EntityHelper.createTileEntity(TileEntityExtractor.class,"Extractor");
        EntityHelper.createTileEntity(TileEntityCrusher.class,"Crusher");
        EntityHelper.createTileEntity(TileEntityAlloySmelter.class,"Alloy Smelter");
        EntityHelper.createTileEntity(TileEntityPlateFormer.class,"Plate Former");
        EntityHelper.createTileEntity(TileEntityCrystalCutter.class,"Crystal Cutter");
        EntityHelper.createTileEntity(TileEntityInfuser.class,"Infuser");
        EntityHelper.createTileEntity(TileEntityBooster.class,"Dilithium Booster");
        EntityHelper.createTileEntity(TileEntityStabilizer.class,"Dilithium Stabilizer");
        EntityHelper.createTileEntity(TileEntityCrystalChamber.class,"Crystal Chamber");
        EntityHelper.createTileEntity(TileEntityPump.class,"Pump");
        EntityHelper.createTileEntity(TileEntityExternalIO.class,"External I/O");
        EntityHelper.createTileEntity(TileEntityCentrifuge.class,"Separation Centrifuge");
        EntityHelper.createTileEntity(TileEntityEnergyConnector.class,"Energy Connector");
        EntityHelper.createTileEntity(TileEntityItemBus.class,"Item Bus");
        EntityHelper.createTileEntity(TileEntityFluidHatch.class,"Fluid Hatch");
        EntityHelper.createTileEntity(TileEntityIgnitor.class,"Signalum Ignitor");
        EntityHelper.createTileEntity(TileEntitySignalumDynamo.class,"Signalum Dynamo");
        EntityHelper.createTileEntity(TileEntityProgrammer.class,"EEPROM Programmer");
        EntityHelper.createTileEntity(TileEntityFilter.class,"Filter");
        EntityHelper.createTileEntity(TileEntityUVLamp.class,"Ultraviolet Lamp");

        addToNameGuiMap("Energy Cell", GuiEnergyCell.class, TileEntityEnergyCell.class);
        addToNameGuiMap("SI Fluid Tank", GuiSIFluidTank.class, TileEntitySIFluidTank.class);
        addToNameGuiMap("Extractor", GuiExtractor.class, TileEntityExtractor.class);
        addToNameGuiMap("Extraction Manifold", GuiReinforcedExtractor.class, TileEntityReinforcedExtractor.class);
        addToNameGuiMap("Crusher", GuiCrusher.class, TileEntityCrusher.class);
        addToNameGuiMap("Alloy Smelter", GuiAlloySmelter.class, TileEntityAlloySmelter.class);
        addToNameGuiMap("Plate Former", GuiPlateFormer.class, TileEntityPlateFormer.class);
        addToNameGuiMap("Crystal Cutter", GuiCrystalCutter.class, TileEntityCrystalCutter.class);
        addToNameGuiMap("Infuser", GuiInfuser.class, TileEntityInfuser.class);
        addToNameGuiMap("Dilithium Booster", GuiBooster.class, TileEntityBooster.class);
        addToNameGuiMap("Dilithium Stabilizer", GuiStabilizer.class, TileEntityStabilizer.class);
        addToNameGuiMap("Crystal Chamber", GuiCrystalChamber.class, TileEntityCrystalChamber.class);
        addToNameGuiMap("Pump", GuiPump.class, TileEntityCrystalChamber.class);
        addToNameGuiMap("SI Assembler", GuiAssembler.class, TileEntityAssembler.class);
        addToNameGuiMap("Dimensional Anchor", GuiDimAnchor.class, TileEntityDimensionalAnchor.class);
        addToNameGuiMap("Automatic Miner", GuiAutoMiner.class, TileEntityAutoMiner.class);
        addToNameGuiMap("External I/O", GuiExternalIO.class, TileEntityExternalIO.class);
        addToNameGuiMap("Separation Centrifuge", GuiCentrifuge.class, TileEntityCentrifuge.class);
        addToNameGuiMap("Signalum Reactor", GuiSignalumReactor.class, TileEntitySignalumReactor.class);
        addToNameGuiMap("Energy Connector", GuiEnergyConnector.class, TileEntityEnergyConnector.class);
        addToNameGuiMap("Item Bus", GuiItemBus.class, TileEntityItemBus.class);
        addToNameGuiMap("Fluid Hatch", GuiItemBus.class, TileEntityFluidHatch.class);
        addToNameGuiMap("Energy Injector",GuiEnergyInjector.class,TileEntityEnergyInjector.class);
        addToNameGuiMap("Signalum Dynamo", GuiSignalumDynamo.class,TileEntitySignalumDynamo.class);
        addToNameGuiMap("EEPROM Programmer", GuiProgrammer.class,TileEntityProgrammer.class);
        addToNameGuiMap("Filter", GuiFilter.class, TileEntityFilter.class);

        addToNameGuiMap("The Pulsar", GuiPulsar.class, InventoryPulsar.class);
        addToNameGuiMap("Signalum Prototype Harness", GuiHarness.class, InventoryHarness.class);
        addToNameGuiMap("Backpack", GuiBackpack.class, InventoryBackpack.class);

        EntityHelper.createTileEntity(TileEntityRecipeMaker.class,"Recipe Maker");
        EntityHelper.createTileEntity(TileEntityWrathBeacon.class,"Wrath Beacon");
        EntityHelper.createTileEntity(TileEntityBlockBreaker.class,"Block Breaker");

        Multiblock.multiblocks.put("dimensionalAnchor",dimAnchorMultiblock);
        Multiblock.multiblocks.put("wrathTree",wrathTree);
        Multiblock.multiblocks.put("signalumReactor",signalumReactor);
        Multiblock.multiblocks.put("extractionManifold",extractionManifold);
        Multiblock.multiblocks.put("basicInductionSmelter",inductionSmelterBasic);
        SignalIndustries.LOGGER.info(String.format("Loaded %d multiblocks..",Multiblock.multiblocks.size()));
        SignalIndustries.LOGGER.info(String.format("Loaded %d internal structures.", Structure.internalStructures.size()));

        EntityHelper.createEntity(EntityInfernal.class,config.getInt("EntityIDs.infernalId"),"Infernal", () -> new MobRenderer<EntityInfernal>(new ModelZombie(),0.5F));
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

    public static <T,U> List<Pair<T,U>> zip(List<T> first, List<U> second){
        List<Pair<T,U>> list = new ArrayList<>();
        List<?> shortest = first.size() < second.size() ? first : second;
        for (int i = 0; i < shortest.size(); i++) {
            list.add(Pair.of(first.get(i),second.get(i)));
        }
        return list;
    }

    public static Item simpleItem(String name, String texture){
        return new ItemBuilder(MOD_ID).setIcon(texture).build(new Item(name, config.getInt("ItemIDs." + name)));
    }

    public static Item simpleItem(String name, String lang, String texture){
        return new ItemBuilder(MOD_ID).setIcon(texture).build(new Item(lang, config.getInt("ItemIDs." + name)));
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
            return stack.isFluidEqual(new FluidStack(SignalIndustries.energyFlowing,1)) ? 100 : 0;
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
        particleAtlas = TextureRegistry.register("particle", new AtlasStitcher("particle", false, null));
        itemAtlas = TextureRegistry.register("item", new AtlasStitcher("item", true, "/assets/minecraft/textures/item/texture_missing.png"));
        blockAtlas = TextureRegistry.register("block", new AtlasStitcher("block", true, "/assets/minecraft/textures/block/texture_missing.png"));

        try {
            TextureRegistry.initializeAllFiles(MOD_ID, blockAtlas);
            TextureRegistry.initializeAllFiles(MOD_ID, itemAtlas);
            TextureRegistry.initializeAllFiles(MOD_ID, particleAtlas);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
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
