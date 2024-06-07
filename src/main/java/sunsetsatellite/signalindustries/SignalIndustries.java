package sunsetsatellite.signalindustries;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.fx.EntityFX;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.SnowballRenderer;
import net.minecraft.client.render.model.ModelZombie;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.*;
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
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.NBTEditCommand;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.multiblocks.RenderMultiblock;
import sunsetsatellite.catalyst.multiblocks.StructureCommand;
import sunsetsatellite.signalindustries.abilities.powersuit.*;
import sunsetsatellite.signalindustries.api.impl.retrostorage.ReSPlugin;
import sunsetsatellite.signalindustries.commands.RecipeReloadCommand;
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
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;
import sunsetsatellite.signalindustries.mp.packets.PacketOpenMachineGUI;
import sunsetsatellite.signalindustries.render.*;
import sunsetsatellite.signalindustries.util.*;
import turniplabs.halplibe.helper.*;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;
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

    static {

        List<Field> blockFields = Arrays.stream(SIBlocks.class.getDeclaredFields()).filter((F) -> Block.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
        List<Field> itemFields = Arrays.stream(SIItems.class.getDeclaredFields()).filter((F) -> Item.class.isAssignableFrom(F.getType())).collect(Collectors.toList());

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

    public static HashMap<String, ArrayList<Class<?>>> nameToGuiMap = new HashMap<>();

    public static final Tag<Block> ENERGY_CONDUITS_CONNECT = Tag.of("energy_conduits_connect");
    public static final Tag<Block> FLUID_CONDUITS_CONNECT = Tag.of("fluid_conduits_connect");
    public static final Tag<Block> ITEM_CONDUITS_CONNECT = Tag.of("item_conduits_connect");

    public static final ArmorMaterial armorPrototypeHarness = ArmorHelper.createArmorMaterial(SignalIndustries.MOD_ID,"signalumprototypeharness",1200,10,10,10,10);
    public static final ArmorMaterial armorSignalumPowerSuit = ArmorHelper.createArmorMaterial(SignalIndustries.MOD_ID,"signalumpowersuit",9999,50,50,50,50);

    public static final ToolMaterial toolMaterialBasic = new ToolMaterial().setDurability(9999).setMiningLevel(3).setEfficiency(25,50);
    public static final ToolMaterial toolMaterialReinforced = new ToolMaterial().setDurability(9999).setMiningLevel(4).setEfficiency(45,80);
    public static final ToolMaterial toolMaterialAwakened = new ToolMaterial().setDurability(9999).setMiningLevel(5).setEfficiency(60,100);

    //TODO: WIP
    //public static final Item sunriseDawn = ItemHelper.createItem(MOD_ID,new ItemSunriseDawn("awakened.sunriseDawn",config.getInt("ItemIDs.sunriseDawn"), ToolMaterial.steel, Tier.AWAKENED),"sunrise_dawn").setMaxStackSize(1);

    public static final SuitBaseAbility testAbility = new TestingAbility();
    public static final SuitBaseEffectAbility testEffectAbility = new TestingEffectAbility();
    //public static final SuitBaseEffectAbility clockworkAbility = new ClockworkAbility();
    public static final SuitBaseAbility boostAbility = new BoostAbility();
    public static final SuitBaseAbility projectileAbility = new ProjectileAbility();

    public static final SignalIndustriesAchievementPage ACHIEVEMENTS = new SignalIndustriesAchievementPage();

    @Override
    public void onInitialize() {
        new SIBlocks().init();
        new SIItems().init();
        new SIAchievements().init();
        new SIWeather().init();
        new SIBiomes().init();
        new SIWorldTypes().init();
        new SIDimensions().init();
        new SIMultiblocks().init();

        List<Field> fields = new ArrayList<>(Arrays.asList(SIBlocks.class.getDeclaredFields()));
        fields.removeIf((F)->F.getType() != Block.class);

        for (Field field : fields) {
            try {
                Block block = (Block) field.get(null);
                ItemToolPickaxe.miningLevels.put(block,3);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        ItemToolPickaxe.miningLevels.put(SIBlocks.prototypeMachineCore,3);
        ItemToolPickaxe.miningLevels.put(SIBlocks.basicMachineCore,3);
        ItemToolPickaxe.miningLevels.put(SIBlocks.reinforcedMachineCore,3);
        ItemToolPickaxe.miningLevels.put(SIBlocks.awakenedMachineCore,3);
        ItemToolPickaxe.miningLevels.put(SIBlocks.signalumOre,3);
        ItemToolPickaxe.miningLevels.put(SIBlocks.dilithiumBlock,4);
        ItemToolPickaxe.miningLevels.put(SIBlocks.dilithiumCrystalBlock,4);
        ItemToolPickaxe.miningLevels.put(SIBlocks.emptyCrystalBlock,3);
        ItemToolPickaxe.miningLevels.put(SIBlocks.rawCrystalBlock,3);
        ItemToolPickaxe.miningLevels.put(SIBlocks.awakenedSignalumCrystalBlock,4);
        ItemToolPickaxe.miningLevels.put(SIBlocks.rootedFabric,4);
        ItemToolPickaxe.miningLevels.put(SIBlocks.dimensionalShardOre,4);
        ItemToolPickaxe.miningLevels.put(SIBlocks.dilithiumOre,4);
        ItemToolPickaxe.miningLevels.put(SIBlocks.realityFabric,5);
        ItemToolPickaxe.miningLevels.put(SIBlocks.reinforcedGlass,3);
        ItemToolPickaxe.miningLevels.put(SIBlocks.reinforcedCasing,3);
        ItemToolPickaxe.miningLevels.put(SIBlocks.basicCasing,3);

        CommandHelper.createCommand(new NBTEditCommand());
        CommandHelper.createCommand(new RecipeReloadCommand("recipes"));
        CommandHelper.createCommand(new StructureCommand("structure","struct"));

        if (FabricLoaderImpl.INSTANCE.isModLoaded("retrostorage")) {
            new ReSPlugin().initializePlugin(LOGGER);
        }

        addEntities();
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

    //idea got *too smart* recently and now considers anything after accessor stubs "unreachable" due to the throw statement in them that will never be triggered
    @SuppressWarnings("UnreachableCode")
    private void addEntities(){
        EntityHelper.createEntity(EntityCrystal.class,347,"signalumCrystal",() -> new SnowballRenderer(SIItems.volatileSignalumCrystal));
        EntityHelper.createEntity(EntityEnergyOrb.class,349,"energyOrb", () -> new SnowballRenderer(TextureRegistry.getTexture("signalindustries:item/energyorb")));
        EntityHelper.createEntity(EntitySunbeam.class,349,"sunBeam", SunbeamRenderer::new);
        EntityHelper.createEntity(EntityFallingMeteor.class,350,"fallingMeteor", FallingMeteorRenderer::new);

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

    public static <T,V> T[] arrayFill(T[] array,V value){
        Arrays.fill(array,value);
        return array;
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
            return stack.isFluidEqual(new FluidStack(SIBlocks.energyFlowing,1)) ? 100 : 0;
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
        return MOD_ID+":"+key;
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
        try {
            TextureRegistry.initializeAllFiles(MOD_ID, TextureRegistry.blockAtlas);
            TextureRegistry.initializeAllFiles(MOD_ID, TextureRegistry.itemAtlas);
            TextureRegistry.initializeAllFiles(MOD_ID, TextureRegistry.particleAtlas);
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
