package sunsetsatellite.signalindustries;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.net.entity.NetEntityHandler;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import net.minecraft.core.world.save.LevelStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Signal;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.Structure;
import sunsetsatellite.signalindustries.entities.*;
import sunsetsatellite.signalindustries.items.ItemBlueprint;
import sunsetsatellite.signalindustries.mp.entity.entry.NetEntryEnergyOrb;
import sunsetsatellite.signalindustries.mp.entity.entry.NetEntryFallingMeteor;
import sunsetsatellite.signalindustries.mp.entity.entry.NetEntrySunbeam;
import sunsetsatellite.signalindustries.mp.entity.entry.NetEntryVolatileCrystal;
import sunsetsatellite.signalindustries.mp.message.*;
import sunsetsatellite.signalindustries.tiles.*;
import sunsetsatellite.signalindustries.tiles.machines.*;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.*;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingAlloySmelter;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingCrusher;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingInfuser;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingPlateFormer;
import sunsetsatellite.signalindustries.util.CustomStructure;
import sunsetsatellite.signalindustries.util.MeteorLocation;
import turniplabs.halplibe.helper.ArmorHelper;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.util.GameStartEntrypoint;

import java.util.*;
import java.util.stream.Collectors;

import static sunsetsatellite.signalindustries.SIConfig.config;

//TODO: reimplement achievement getting
//TODO: reimplement covers
public class SignalIndustries implements ModInitializer, GameStartEntrypoint {

    public static final String MOD_ID = "signalindustries";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static List<MeteorLocation> meteorLocations = new ArrayList<>();
    public static List<ChunkCoordinates> chunkLoaders = new ArrayList<>();
    public static Set<BlockInstance> uvLamps = new HashSet<>();
    public static HashMap<String, CustomStructure> customStructures = new HashMap<>();
    public static boolean bloodMoonsDisabled = false;

    public static final ArmorMaterial armorPrototypeHarness = ArmorHelper.createArmorMaterial(SignalIndustries.MOD_ID, "harness", 1200, 10, 10, 10, 10);
    public static final ArmorMaterial armorSignalumPowerSuit = ArmorHelper.createArmorMaterial(SignalIndustries.MOD_ID, "power_suit", 9999, 50, 50, 50, 50);

    public static final ToolMaterial toolMaterialBasic = new ToolMaterial().setDurability(9999).setMiningLevel(3).setEfficiency(25, 50);
    public static final ToolMaterial toolMaterialReinforced = new ToolMaterial().setDurability(9999).setMiningLevel(config.getInt("Other.dilithiumMiningLevel")).setEfficiency(45, 80);
    public static final ToolMaterial toolMaterialAwakened = new ToolMaterial().setDurability(9999).setMiningLevel(config.getInt("Other.awakenedMiningLevel")).setEfficiency(60, 100);

    public static final Tag<Block<?>> SIGNALUM_CONDUITS_CONNECT = Tag.of("signalum_conduits_connect");
    public static final Tag<Block<?>> FLUID_CONDUITS_CONNECT = Tag.of("fluid_conduits_connect");
    public static final Tag<Block<?>> ITEM_CONDUITS_CONNECT = Tag.of("item_conduits_connect");
    public static final Tag<Block<?>> CASING = Tag.of("casing");
    public static final Tag<Block<?>> REPLACEABLE_CASING = Tag.of("replaceable_casing");
    public static final Tag<Block<?>> PROTOTYPE_CASING = Tag.of("prototype_casing");
    public static final Tag<Block<?>> BASIC_CASING = Tag.of("basic_casing");
    public static final Tag<Block<?>> REINFORCED_CASING = Tag.of("reinforced_casing");
    public static final Tag<Block<?>> AWAKENED_CASING = Tag.of("awakened_casing");
    public static final Tag<Block<?>> ORE_BLOCK = Tag.of("ore_block");

    public static final Map<Block<?>, Integer> ORE_BLOCK_COUNT = new HashMap<>();

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Override
    public void onInitialize() {
        LOGGER.info("Loading SI config...");
        new SIConfig();
        new SIArt().init();

        EntityHelper.createTileEntity(TileEntityExtractor.class, id("extractor"));
        EntityHelper.createTileEntity(TileEntityCollector.class, id("collector"));
        EntityHelper.createTileEntity(TileEntitySIFluidTank.class, id("fluid_tank"));
        EntityHelper.createTileEntity(TileEntityEnergyCell.class, id("energy_cell"));
        EntityHelper.createTileEntity(TileEntityCrusher.class, id("crusher"));
        EntityHelper.createTileEntity(TileEntityAlloySmelter.class, id("alloy_smelter"));
        EntityHelper.createTileEntity(TileEntityPlateFormer.class, id("plate_former"));
        EntityHelper.createTileEntity(TileEntityCrystalChamber.class, id("crystal_chamber"));
        EntityHelper.createTileEntity(TileEntityCrystalCutter.class, id("crystal_cutter"));
        EntityHelper.createTileEntity(TileEntityInfuser.class, id("infuser"));
        EntityHelper.createTileEntity(TileEntityStoneworks.class, id("stoneworks"));
        EntityHelper.createTileEntity(TileEntityPump.class, id("pump"));
        EntityHelper.createTileEntity(TileEntityAssembler.class, id("assembler"));
        EntityHelper.createTileEntity(TileEntityAutoMiner.class, id("auto_miner"));
        EntityHelper.createTileEntity(TileEntitySignalumDynamo.class, id("dynamo"));
        EntityHelper.createTileEntity(TileEntityEnergyInjector.class, id("injector"));
        EntityHelper.createTileEntity(TileEntityBooster.class, id("booster"));
        EntityHelper.createTileEntity(TileEntityStabilizer.class, id("stabilizer"));
        EntityHelper.createTileEntity(TileEntityExternalIO.class, id("external_io"));
        EntityHelper.createTileEntity(TileEntityEnergyConnector.class, id("energy_connector"));
        EntityHelper.createTileEntity(TileEntityItemBus.class, id("item_bus"));
        EntityHelper.createTileEntity(TileEntityFluidHatch.class, id("fluid_hatch"));
        EntityHelper.createTileEntity(TileEntityInductionSmelter.class, id("induction_smelter"));
        EntityHelper.createTileEntity(TileEntityWakingAlloySmelter.class, id("waking_alloy_smelter"));
        EntityHelper.createTileEntity(TileEntityWakingCrusher.class, id("waking_crusher"));
        EntityHelper.createTileEntity(TileEntityWakingPlateFormer.class, id("waking_plate_former"));
        EntityHelper.createTileEntity(TileEntityWakingInfuser.class, id("waking_infuser"));
        EntityHelper.createTileEntity(TileEntityCentrifuge.class, id("centrifuge"));
        EntityHelper.createTileEntity(TileEntityDimensionalAnchor.class, id("dimensional_anchor"));
        EntityHelper.createTileEntity(TileEntityReinforcedExtractor.class, id("reinforced_extractor"));
        EntityHelper.createTileEntity(TileEntitySignalumReactor.class, id("reactor"));
        EntityHelper.createTileEntity(TileEntityIgnitor.class, id("ignitor"));
        EntityHelper.createTileEntity(TileEntityFluidConduit.class, id("fluid_conduit"));
        EntityHelper.createTileEntity(TileEntityConduit.class, id("conduit"));
        EntityHelper.createTileEntity(TileEntityCatalystConduit.class, id("catalyst_conduit"));
        EntityHelper.createTileEntity(TileEntityItemConduit.class, id("item_conduit"));
        EntityHelper.createTileEntity(TileEntityFilter.class, id("filter"));
        EntityHelper.createTileEntity(TileEntityInserter.class, id("inserter"));
        EntityHelper.createTileEntity(TileEntityStorageContainer.class, id("storage_container"));
        EntityHelper.createTileEntity(TileEntityVoidContainer.class, id("void_container"));
        EntityHelper.createTileEntity(TileEntityBuilder.class, id("builder"));
        EntityHelper.createTileEntity(TileEntityChunkloader.class, id("chunkloader"));
        EntityHelper.createTileEntity(TileEntityWarpGate.class, id("warp_gate"));
        EntityHelper.createTileEntity(TileEntityMultiConduit.class, id("multi_conduit"));
        EntityHelper.createTileEntity(TileEntityProgrammer.class, id("programmer"));
        EntityHelper.createTileEntity(TileEntityReinforcedWrathBeacon.class, id("reinforced_wrath_beacon"));
        EntityHelper.createTileEntity(TileEntityWrathBeacon.class, id("wrath_beacon"));
        EntityHelper.createTileEntity(TileEntityUVLamp.class, id("uv_lamp"));
        EntityHelper.createTileEntity(TileEntityPulsar.class, id("pulsar"));
        EntityHelper.createTileEntity(TileEntityBonsaiPot.class, id("bonsai"));
        EntityHelper.createTileEntity(TileEntityLaserDrill.class, id("laser_drill"));
        EntityHelper.createTileEntity(TileEntityGreenhouse.class, id("greenhouse"));
        EntityHelper.createTileEntity(TileEntityEncapsulator.class, id("encapsulator"));
        EntityHelper.createTileEntity(TileEntityPedestal.class, id("pedestal"));
        EntityHelper.createTileEntity(TileEntitySITrommel.class, id("trommel"));
        EntityHelper.createTileEntity(TileEntityRedstoneClock.class, id("redstone_clock"));

        EntityHelper.createEntity(ProjectileCrystal.class, id("volatile_crystal"), "entity.signalindustries.volatileCrystal");
        EntityHelper.createEntity(ProjectileFallingMeteor.class, id("falling_meteor"), "entity.signalindustries.fallingMeteor");
        EntityHelper.createEntity(ProjectileEnergyOrb.class, id("energy_orb"), "entity.signalindustries.energyOrb");
        EntityHelper.createEntity(ProjectileSunbeam.class, id("sunbeam"), "entity.signalindustries.sunbeam");
        EntityHelper.createEntity(MobInfernal.class, id("infernal"), "entity.signalindustries.infernal");
        EntityHelper.createEntity(EntityRealityTear.class, id("reality_tear"), "entity.signalindustries.realityTear");
        EntityHelper.createEntity(EntityShockwave.class, id("shockwave"), "entity.signalindustries.shockwave");

        NetEntityHandler.registerNetworkEntry(new NetEntryVolatileCrystal(), config.getInt("EntityIDs.volatileCrystalId"));
        NetEntityHandler.registerNetworkEntry(new NetEntryFallingMeteor(), config.getInt("EntityIDs.fallingMeteorId"));
        NetEntityHandler.registerNetworkEntry(new NetEntryEnergyOrb(), config.getInt("EntityIDs.energyOrbId"));
        NetEntityHandler.registerNetworkEntry(new NetEntrySunbeam(), config.getInt("EntityIDs.sunbeamId"));

        NetworkHandler.registerNetworkMessage(NetworkMessageRecipeIdChange::new);
        NetworkHandler.registerNetworkMessage(NetworkMessageIOChange::new);
        NetworkHandler.registerNetworkMessage(NetworkMessageBuilderConfig::new);
        NetworkHandler.registerNetworkMessage(NetworkMessageReactorStart::new);
        NetworkHandler.registerNetworkMessage(NetworkMessageDrillModeChange::new);
        NetworkHandler.registerNetworkMessage(NetworkMessageOpenSuit::new);
        NetworkHandler.registerNetworkMessage(NetworkMessagePowerSuitAction::new);
        NetworkHandler.registerNetworkMessage(NetworkMessagePowerSuitSync::new);
        NetworkHandler.registerNetworkMessage(NetworkMessageExternalIOLinkBreak::new);
        NetworkHandler.registerNetworkMessage(NetworkMessageFilterConfig::new);
        NetworkHandler.registerNetworkMessage(NetworkMessageAutoMinerStart::new);
        NetworkHandler.registerNetworkMessage(NetworkMessageMeteorLocationSync::new);
        NetworkHandler.registerNetworkMessage(NetworkMessageSensorPipeSetFilter::new);
        NetworkHandler.registerNetworkMessage(NetworkMessagePowerSuitRemoteSync::new);
        NetworkHandler.registerNetworkMessage(NetworkMessageRedstoneCoverSetFilter::new);

        BlockTags.TAG_LIST.add(SIGNALUM_CONDUITS_CONNECT);
        BlockTags.TAG_LIST.add(FLUID_CONDUITS_CONNECT);
        BlockTags.TAG_LIST.add(ITEM_CONDUITS_CONNECT);
        BlockTags.TAG_LIST.add(CASING);
        BlockTags.TAG_LIST.add(REPLACEABLE_CASING);
        BlockTags.TAG_LIST.add(PROTOTYPE_CASING);
        BlockTags.TAG_LIST.add(BASIC_CASING);
        BlockTags.TAG_LIST.add(REINFORCED_CASING);
        BlockTags.TAG_LIST.add(AWAKENED_CASING);
        BlockTags.TAG_LIST.add(ORE_BLOCK);

        Catalyst.WORLD_LOAD_SIGNAL.connect(new LoadListener());

        LOGGER.info("Signal Industries is loading... Shine!");
    }

    @Override
    public void beforeGameStart() {
        LOGGER.info("Beginning core pre-init.");
    }

    @Override
    public void afterGameStart() {
        LOGGER.info("Beginning core post-init.");

        //here to load after every other mods recipes
        SIRecipes.loadSpecial();
    }

    public static void addMeteorLocation(MeteorLocation location) {
        meteorLocations.add(location);
        if (EnvironmentHelper.isServerEnvironment()) {
            NetworkHandler.sendToAllPlayers(new NetworkMessageMeteorLocationSync(location));
        }
    }

    public static int getEnergyBurnTime(FluidStack stack) {
        if (stack == null) {
            return 0;
        } else {
            return stack.isFluidEqual(new FluidStack(SIFluids.ENERGY)) ? 200 : 0;
        }
    }

    public static NamespaceID id(String id) {
        return NamespaceID.getPermanent(MOD_ID, id);
    }

    public static String key(String key) {
        return MOD_ID + ":" + key;
    }

    public static String langKey(String key) {
        return MOD_ID + "." + key;
    }

    public static boolean hasItems(List<RecipeSymbol> symbols, List<ItemStack> available) {
        symbols.removeIf(Objects::isNull);
        List<ItemStack> copy = available.stream().map(ItemStack::copy).collect(Collectors.toList());
        int s = 0;
        int sReq = (int) symbols.stream().filter(Objects::nonNull).count();
        label:
        for (RecipeSymbol symbol : symbols) {
            for (ItemStack stack : copy) {
                if (symbol.matches(stack)) {
                    if (stack == null || stack.stackSize <= 0) continue;
                    stack.stackSize--;
                    s++;
                    continue label;
                }
            }
        }
        return s == sReq;
    }

    public static Structure getStructureFromBlueprint(ItemStack blueprint, World world) {
        if (blueprint != null && blueprint.getItem() instanceof ItemBlueprint) {
            String key = blueprint.getData().getStringOrDefault("multiblock", "");
            String customKey = blueprint.getData().getStringOrDefault("structure", "");
            if (!key.isEmpty()) {
                return Multiblock.multiblocks.get(key.replace("multiblock.signalindustries.", ""));
            } else if (!customKey.isEmpty()) {
                if (SignalIndustries.customStructures.containsKey(customKey)) {
                    return SignalIndustries.customStructures.get(customKey);
                } else {
                    CustomStructure structure = new CustomStructure(customKey, world, false, false);
                    SignalIndustries.customStructures.put(customKey, structure);
                    return structure;
                }
            }
            return null;
        }
        return null;
    }

    public static class LoadListener implements Signal.Listener<LevelStorage> {
        @Override
        public void signalEmitted(Signal<LevelStorage> signal, LevelStorage levelStorage) {
            customStructures.clear();
        }
    }
}
