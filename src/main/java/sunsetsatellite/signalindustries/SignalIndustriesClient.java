package sunsetsatellite.signalindustries;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.particle.ParticleDispatcher;
import net.minecraft.client.gui.guidebook.mobs.MobInfoRegistry;
import net.minecraft.client.gui.options.components.BooleanOptionComponent;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.render.worldtype.WorldTypeFXDispatcher;
import net.minecraft.client.world.WorldClient;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.world.Dimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.MpGuiEntryClient;
import sunsetsatellite.signalindustries.dim.WorldTypeFXEternity;
import sunsetsatellite.signalindustries.entities.MobInfernal;
import sunsetsatellite.signalindustries.entities.ParticleShockwave;
import sunsetsatellite.signalindustries.interfaces.mixins.IKeybinds;
import sunsetsatellite.signalindustries.invs.InventoryAbilityModule;
import sunsetsatellite.signalindustries.invs.InventoryBackpack;
import sunsetsatellite.signalindustries.invs.InventoryHarness;
import sunsetsatellite.signalindustries.invs.InventoryPulsar;
import sunsetsatellite.signalindustries.menus.*;
import sunsetsatellite.signalindustries.powersuit.InventoryPowerSuit;
import sunsetsatellite.signalindustries.powersuit.MenuPowerSuit;
import sunsetsatellite.signalindustries.powersuit.ScreenPowerSuit;
import sunsetsatellite.signalindustries.screens.*;
import sunsetsatellite.signalindustries.tiles.*;
import sunsetsatellite.signalindustries.tiles.base.TileEntityCoverable;
import sunsetsatellite.signalindustries.tiles.machines.*;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.*;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingAlloySmelter;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingCrusher;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingInfuser;
import turniplabs.halplibe.util.ClientStartEntrypoint;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.HashMap;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

@Environment(EnvType.CLIENT)
public class SignalIndustriesClient implements ClientModInitializer, ClientStartEntrypoint {

    public static final String MOD_ID = "signalindustries|client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final HashMap<String,KeyBinding> attachmentKeybinds = new HashMap<>();

    @Override
    public void onInitializeClient() {

        Catalyst.GUIS.register(key("gui/extractor"),new MpGuiEntryClient(TileEntityExtractor.class, ScreenExtractor.class, MenuExtractor.class));
        Catalyst.GUIS.register(key("gui/collector"),new MpGuiEntryClient(TileEntityCollector.class, ScreenCollector.class, MenuCollector.class));
        Catalyst.GUIS.register(key("gui/fluid_tank"),new MpGuiEntryClient(TileEntitySIFluidTank.class, ScreenSIFluidTank.class, MenuSIFluidTank.class));
        Catalyst.GUIS.register(key("gui/energy_cell"),new MpGuiEntryClient(TileEntityEnergyCell.class, ScreenEnergyCell.class, MenuSIFluidTank.class));
        Catalyst.GUIS.register(key("gui/crusher"),new MpGuiEntryClient(TileEntityCrusher.class, ScreenCrusher.class, MenuCrusher.class));
        Catalyst.GUIS.register(key("gui/alloy_smelter"),new MpGuiEntryClient(TileEntityAlloySmelter.class, ScreenAlloySmelter.class, MenuAlloySmelter.class));
        Catalyst.GUIS.register(key("gui/plate_former"),new MpGuiEntryClient(TileEntityPlateFormer.class, ScreenPlateFormer.class, MenuPlateFormer.class));
        Catalyst.GUIS.register(key("gui/crystal_chamber"),new MpGuiEntryClient(TileEntityCrystalChamber.class, ScreenCrystalChamber.class, MenuCrystalChamber.class));
        Catalyst.GUIS.register(key("gui/crystal_cutter"),new MpGuiEntryClient(TileEntityCrystalCutter.class, ScreenCrystalCutter.class, MenuCrystalCutter.class));
        Catalyst.GUIS.register(key("gui/infuser"),new MpGuiEntryClient(TileEntityInfuser.class, ScreenInfuser.class, MenuInfuser.class));
        Catalyst.GUIS.register(key("gui/stoneworks"),new MpGuiEntryClient(TileEntityStoneworks.class, ScreenStoneworks.class, MenuStoneworks.class));
        Catalyst.GUIS.register(key("gui/pump"),new MpGuiEntryClient(TileEntityPump.class, ScreenPump.class, MenuPump.class));
        Catalyst.GUIS.register(key("gui/assembler"),new MpGuiEntryClient(TileEntityAssembler.class, ScreenAssembler.class, MenuAssembler.class));
        Catalyst.GUIS.register(key("gui/auto_miner"),new MpGuiEntryClient(TileEntityAutoMiner.class, ScreenAutoMiner.class, MenuAutoMiner.class));
        Catalyst.GUIS.register(key("gui/multi_conduit"),new MpGuiEntryClient(TileEntityMultiConduit.class, ScreenMultiConduitConfig.class, null));
        Catalyst.GUIS.register(key("gui/restrict_item_conduit"),new MpGuiEntryClient(TileEntityItemConduit.class, ScreenRestrictPipeConfig.class, null));
        Catalyst.GUIS.register(key("gui/sensor_item_conduit"),new MpGuiEntryClient(TileEntityItemConduit.class, ScreenSensorPipeConfig.class, MenuSensorPipe.class));
        Catalyst.GUIS.register(key("gui/dynamo"),new MpGuiEntryClient(TileEntitySignalumDynamo.class, ScreenSignalumDynamo.class, MenuSignalumDynamo.class));
        Catalyst.GUIS.register(key("gui/booster"),new MpGuiEntryClient(TileEntityBooster.class, ScreenBooster.class, MenuBooster.class));
        Catalyst.GUIS.register(key("gui/stabilizer"),new MpGuiEntryClient(TileEntityStabilizer.class, ScreenStabilizer.class, MenuStabilizer.class));
        Catalyst.GUIS.register(key("gui/external_io"),new MpGuiEntryClient(TileEntityExternalIO.class, ScreenExternalIO.class, MenuExternalIO.class));
        Catalyst.GUIS.register(key("gui/filter"),new MpGuiEntryClient(TileEntityFilter.class, ScreenFilter.class, MenuFilter.class));
        Catalyst.GUIS.register(key("gui/pulsar"),new MpGuiEntryClient(InventoryPulsar.class, ScreenPulsar.class, MenuPulsar.class));
        Catalyst.GUIS.register(key("gui/pulsar_attch"),new MpGuiEntryClient(InventoryPulsar.class, ScreenPulsarAttachment.class, MenuPulsarAttachment.class));
        Catalyst.GUIS.register(key("gui/pulsar_block"),new MpGuiEntryClient(TileEntityPulsar.class, ScreenPulsarBlock.class, MenuPulsarBlock.class));
        Catalyst.GUIS.register(key("gui/backpack"),new MpGuiEntryClient(InventoryBackpack.class, ScreenBackpack.class, MenuBackpack.class));
        Catalyst.GUIS.register(key("gui/harness"),new MpGuiEntryClient(InventoryHarness.class, ScreenHarness.class, MenuHarness.class));
        Catalyst.GUIS.register(key("gui/power_suit"),new MpGuiEntryClient(InventoryPowerSuit.class, ScreenPowerSuit.class, MenuPowerSuit.class));
        Catalyst.GUIS.register(key("gui/ability_module"),new MpGuiEntryClient(InventoryAbilityModule.class, ScreenAbilityModule.class, MenuAbilityModule.class));
        Catalyst.GUIS.register(key("gui/energy_connector"),new MpGuiEntryClient(TileEntityEnergyConnector.class, ScreenEnergyConnector.class, MenuEnergyConnector.class));
        Catalyst.GUIS.register(key("gui/fluid_hatch"),new MpGuiEntryClient(TileEntityFluidHatch.class, ScreenFluidHatch.class, MenuFluidHatch.class));
        Catalyst.GUIS.register(key("gui/item_bus"),new MpGuiEntryClient(TileEntityItemBus.class, ScreenItemBus.class, MenuItemBus.class));
        Catalyst.GUIS.register(key("gui/induction_smelter"),new MpGuiEntryClient(TileEntityInductionSmelter.class, ScreenMultiblock.class, MenuMultiblock.class));
        Catalyst.GUIS.register(key("gui/waking_alloy_smelter"),new MpGuiEntryClient(TileEntityWakingAlloySmelter.class, ScreenMultiblock.class, MenuMultiblock.class));
        Catalyst.GUIS.register(key("gui/waking_crusher"),new MpGuiEntryClient(TileEntityWakingCrusher.class, ScreenMultiblock.class, MenuMultiblock.class));
        Catalyst.GUIS.register(key("gui/waking_plate_former"),new MpGuiEntryClient(TileEntityPlateFormer.class, ScreenMultiblock.class, MenuMultiblock.class));
        Catalyst.GUIS.register(key("gui/waking_infuser"),new MpGuiEntryClient(TileEntityWakingInfuser.class, ScreenMultiblock.class, MenuMultiblock.class));
        Catalyst.GUIS.register(key("gui/centrifuge"),new MpGuiEntryClient(TileEntityCentrifuge.class, ScreenCentrifuge.class, MenuCentrifuge.class));
        Catalyst.GUIS.register(key("gui/reactor"),new MpGuiEntryClient(TileEntitySignalumReactor.class, ScreenSignalumReactor.class, MenuSignalumReactor.class));
        Catalyst.GUIS.register(key("gui/dim_anchor"),new MpGuiEntryClient(TileEntityDimensionalAnchor.class, ScreenDimAnchor.class, MenuDimAnchor.class));
        Catalyst.GUIS.register(key("gui/r_extractor"),new MpGuiEntryClient(TileEntityReinforcedExtractor.class, ScreenReinforcedExtractor.class, MenuReinforcedExtractor.class));
        Catalyst.GUIS.register(key("gui/builder"),new MpGuiEntryClient(TileEntityBuilder.class, ScreenBuilder.class, MenuBuilder.class));
        Catalyst.GUIS.register(key("gui/injector"),new MpGuiEntryClient(TileEntityEnergyInjector.class, ScreenInjector.class, MenuInjector.class));
        Catalyst.GUIS.register(key("gui/programmer"),new MpGuiEntryClient(TileEntityProgrammer.class, ScreenProgrammer.class, MenuProgrammer.class));
        Catalyst.GUIS.register(key("gui/warp_gate"),new MpGuiEntryClient(TileEntityWarpGate.class, ScreenWarpGate.class, MenuWarpGate.class));
        Catalyst.GUIS.register(key("gui/laser_drill"),new MpGuiEntryClient(TileEntityLaserDrill.class, ScreenMultiblock.class, MenuMultiblock.class));

        Catalyst.GUIS.register(key("gui/switch_cover"),new MpGuiEntryClient(TileEntityCoverable.class, ScreenSwitchCoverConfig.class, MenuCover.class));
        Catalyst.GUIS.register(key("gui/void_cover"),new MpGuiEntryClient(TileEntityCoverable.class, ScreenVoidCoverConfig.class, MenuCover.class));
        Catalyst.GUIS.register(key("gui/redstone_cover"),new MpGuiEntryClient(TileEntityCoverable.class, ScreenRedstoneCoverConfig.class, MenuCover.class));

        try {
            TextureRegistry.initializeAllFiles(SignalIndustries.MOD_ID, TextureRegistry.blockAtlas, true);
            TextureRegistry.initializeAllFiles(SignalIndustries.MOD_ID, TextureRegistry.itemAtlas, true);
            TextureRegistry.initializeAllFiles(SignalIndustries.MOD_ID, TextureRegistry.artAtlas, true);
            TextureRegistry.initializeAllFiles(SignalIndustries.MOD_ID, TextureRegistry.particleAtlas, true);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("SI Client initialized.");
    }

    @Override
    public void beforeClientStart() {
        LOGGER.info("Beginning client pre-init.");
    }

    @Override
    public void afterClientStart() {
        LOGGER.info("Beginning client post-init.");
        new SIAchievements().init();

        WorldTypeFXDispatcher.getInstance().addDispatch(new WorldTypeFXEternity(SIWorldTypes.ETERNITY_WORLD)
                .setHasAurora(false).setHasGround(true).setHasClouds(false).setHasSky(false));

        ParticleDispatcher.getInstance().addDispatch("signalindustries.shockwave", ParticleShockwave::new);

        MobInfoRegistry.register(
                MobInfernal.class,
                "guidebook.section.mob.infernal.name",
                "guidebook.section.mob.infernal.desc",
                40,
                1000,
                new MobInfoRegistry.MobDrop[]{
                        new MobInfoRegistry.MobDrop(new ItemStack(SIItems.infernalFragment),1,0,2)
                }
        );

        Method[] methods = IKeybinds.class.getDeclaredMethods();
        for (Method method : methods) {
            try {
                if(method.getName().contains("Attachment")){
                    KeyBinding keyBinding = (KeyBinding) method.invoke(Minecraft.getMinecraft().gameSettings);
                    attachmentKeybinds.put(method.getName(), keyBinding);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        IKeybinds gameSettings = (IKeybinds) Minecraft.getMinecraft().gameSettings;

        OptionsPage optionsPage = new OptionsPage("gui.options.page.signalindustries", SIItems.signalumCrystal.getDefaultStack());
        optionsPage.withComponent(new BooleanOptionComponent(gameSettings.signalindustries$isSuitBackgroundShown()));
        OptionsPages.register(optionsPage);

        OptionsCategory category = new OptionsCategory("gui.options.page.controls.category.signalindustries");
        category
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeyOpenSuit()))
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeyActivateAbility()))
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeySwitchMode()))
                //.withComponent(new KeyBindingComponent(((IKeybinds) Minecraft.getMinecraft().gameSettings).signalindustries$getKeyShowIndex()))
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeyActivateHeadTopAttachment()))
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeyActivateHeadLensAttachment()))
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeyActivateArmBackLAttachment()))
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeyActivateArmBackRAttachment()))
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeyActivateArmFrontLAttachment()))
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeyActivateArmFrontRAttachment()))
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeyActivateArmSideLAttachment()))
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeyActivateArmSideRAttachment()))
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeyActivateCoreBackAttachment()))
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeyActivateLegSideLAttachment()))
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeyActivateLegSideRAttachment()))
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeyActivateBootBackLAttachment()))
                .withComponent(new KeyBindingComponent(gameSettings.signalIndustries$getKeyActivateBootBackRAttachment()));
        OptionsPages.CONTROLS
                .withComponent(category);
    }

    public static void movePlayerToDimension(Player player, int dimension) {
        Minecraft mc = Minecraft.getMinecraft();
        Dimension lastDim = Dimension.getDimensionList().get(player.dimension);
        Dimension newDim = Dimension.getDimensionList().get(dimension);
        System.out.println("Switching to dimension \"" + newDim.getTranslatedName() + "\"!!");
        player.dimension = dimension;
        mc.currentWorld.setEntityDead(player);
        mc.thePlayer.removed = false;
        double x = player.x;
        double y = player.y + 64;
        double z = player.z;
        player.moveTo(x *= Dimension.getCoordScale(lastDim, newDim), y, z *= Dimension.getCoordScale(lastDim, newDim), player.yRot, player.xRot);
        if (player.isAlive()) {
            mc.currentWorld.updateEntityWithOptionalForce(player, false);
        }
        WorldClient world = new WorldClient(mc.currentWorld, newDim);
        if (newDim == lastDim.homeDim) {
            mc.changeWorld(world, "Leaving " + lastDim.getTranslatedName(), player);
        } else {
            mc.changeWorld(world, "Entering " + newDim.getTranslatedName(), player);
        }
        player.world = mc.currentWorld;
        if (player.isAlive()) {
            player.moveTo(x, y, z, player.yRot, player.xRot);
            mc.currentWorld.updateEntityWithOptionalForce(player, false);
        }
    }
}
