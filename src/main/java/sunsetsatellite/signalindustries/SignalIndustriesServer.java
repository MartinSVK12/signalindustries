package sunsetsatellite.signalindustries;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.MpGuiEntry;
import sunsetsatellite.catalyst.core.util.mp.MpGuiEntryClient;
import sunsetsatellite.signalindustries.invs.InventoryAbilityModule;
import sunsetsatellite.signalindustries.invs.InventoryBackpack;
import sunsetsatellite.signalindustries.invs.InventoryHarness;
import sunsetsatellite.signalindustries.invs.InventoryPulsar;
import sunsetsatellite.signalindustries.menus.*;
import sunsetsatellite.signalindustries.powersuit.InventoryPowerSuit;
import sunsetsatellite.signalindustries.powersuit.MenuPowerSuit;
import sunsetsatellite.signalindustries.screens.*;
import sunsetsatellite.signalindustries.tiles.*;
import sunsetsatellite.signalindustries.tiles.base.TileEntityCoverable;
import sunsetsatellite.signalindustries.tiles.machines.*;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.*;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingAlloySmelter;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingCrusher;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingInfuser;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

@Environment(EnvType.SERVER)
public class SignalIndustriesServer implements DedicatedServerModInitializer {

    public static final String MOD_ID = "signalindustries|server";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeServer() {

        Catalyst.GUIS.register(key("gui/extractor"),new MpGuiEntry(TileEntityExtractor.class, MenuExtractor.class));
        Catalyst.GUIS.register(key("gui/collector"),new MpGuiEntry(TileEntityCollector.class, MenuCollector.class));
        Catalyst.GUIS.register(key("gui/fluid_tank"),new MpGuiEntry(TileEntitySIFluidTank.class, MenuSIFluidTank.class));
        Catalyst.GUIS.register(key("gui/energy_cell"),new MpGuiEntry(TileEntityEnergyCell.class, MenuSIFluidTank.class));
        Catalyst.GUIS.register(key("gui/crusher"),new MpGuiEntry(TileEntityCrusher.class, MenuCrusher.class));
        Catalyst.GUIS.register(key("gui/alloy_smelter"),new MpGuiEntry(TileEntityAlloySmelter.class, MenuAlloySmelter.class));
        Catalyst.GUIS.register(key("gui/plate_former"),new MpGuiEntry(TileEntityPlateFormer.class, MenuPlateFormer.class));
        Catalyst.GUIS.register(key("gui/crystal_chamber"),new MpGuiEntry(TileEntityCrystalChamber.class, MenuCrystalChamber.class));
        Catalyst.GUIS.register(key("gui/crystal_cutter"),new MpGuiEntry(TileEntityCrystalCutter.class, MenuCrystalCutter.class));
        Catalyst.GUIS.register(key("gui/pump"),new MpGuiEntry(TileEntityPump.class, MenuPump.class));
        Catalyst.GUIS.register(key("gui/stoneworks"),new MpGuiEntry(TileEntityStoneworks.class, MenuStoneworks.class));
        Catalyst.GUIS.register(key("gui/assembler"),new MpGuiEntry(TileEntityAssembler.class, MenuAssembler.class));
        Catalyst.GUIS.register(key("gui/auto_miner"),new MpGuiEntry(TileEntityAutoMiner.class, MenuAutoMiner.class));
        Catalyst.GUIS.register(key("gui/sensor_item_conduit"),new MpGuiEntry(TileEntityItemConduit.class, MenuSensorPipe.class));
        Catalyst.GUIS.register(key("gui/dynamo"),new MpGuiEntry(TileEntitySignalumDynamo.class, MenuSignalumDynamo.class));
        Catalyst.GUIS.register(key("gui/infuser"),new MpGuiEntry(TileEntityInfuser.class, MenuInfuser.class));
        Catalyst.GUIS.register(key("gui/booster"),new MpGuiEntry(TileEntityBooster.class, MenuBooster.class));
        Catalyst.GUIS.register(key("gui/stabilizer"),new MpGuiEntry(TileEntityStabilizer.class, MenuStabilizer.class));
        Catalyst.GUIS.register(key("gui/external_io"),new MpGuiEntry(TileEntityExternalIO.class, MenuExternalIO.class));
        Catalyst.GUIS.register(key("gui/filter"),new MpGuiEntry(TileEntityFilter.class, MenuFilter.class));
        Catalyst.GUIS.register(key("gui/pulsar"),new MpGuiEntry(InventoryPulsar.class, MenuPulsar.class));
        Catalyst.GUIS.register(key("gui/pulsar_attch"),new MpGuiEntry(InventoryPulsar.class, MenuPulsarAttachment.class));
        Catalyst.GUIS.register(key("gui/pulsar_block"),new MpGuiEntry(TileEntityPulsar.class, MenuPulsarBlock.class));
        Catalyst.GUIS.register(key("gui/backpack"),new MpGuiEntry(InventoryBackpack.class, MenuBackpack.class));
        Catalyst.GUIS.register(key("gui/harness"),new MpGuiEntry(InventoryHarness.class, MenuHarness.class));
        Catalyst.GUIS.register(key("gui/power_suit"),new MpGuiEntry(InventoryPowerSuit.class, MenuPowerSuit.class));
        Catalyst.GUIS.register(key("gui/ability_module"),new MpGuiEntry(InventoryAbilityModule.class, MenuAbilityModule.class));
        Catalyst.GUIS.register(key("gui/energy_connector"),new MpGuiEntry(TileEntityEnergyConnector.class, MenuEnergyConnector.class));
        Catalyst.GUIS.register(key("gui/fluid_hatch"),new MpGuiEntry(TileEntityFluidHatch.class, MenuFluidHatch.class));
        Catalyst.GUIS.register(key("gui/item_bus"),new MpGuiEntry(TileEntityItemBus.class, MenuItemBus.class));
        Catalyst.GUIS.register(key("gui/induction_smelter"),new MpGuiEntry(TileEntityInductionSmelter.class, MenuMultiblock.class));
        Catalyst.GUIS.register(key("gui/waking_alloy_smelter"),new MpGuiEntry(TileEntityWakingAlloySmelter.class, MenuMultiblock.class));
        Catalyst.GUIS.register(key("gui/waking_crusher"),new MpGuiEntry(TileEntityWakingCrusher.class, MenuMultiblock.class));
        Catalyst.GUIS.register(key("gui/waking_plate_former"),new MpGuiEntry(TileEntityPlateFormer.class, MenuMultiblock.class));
        Catalyst.GUIS.register(key("gui/waking_infuser"),new MpGuiEntry(TileEntityWakingInfuser.class, MenuMultiblock.class));
        Catalyst.GUIS.register(key("gui/centrifuge"),new MpGuiEntry(TileEntityCentrifuge.class, MenuCentrifuge.class));
        Catalyst.GUIS.register(key("gui/reactor"),new MpGuiEntry(TileEntitySignalumReactor.class, MenuSignalumReactor.class));
        Catalyst.GUIS.register(key("gui/dim_anchor"),new MpGuiEntry(TileEntityDimensionalAnchor.class, MenuDimAnchor.class));
        Catalyst.GUIS.register(key("gui/r_extractor"),new MpGuiEntry(TileEntityReinforcedExtractor.class, MenuReinforcedExtractor.class));
        Catalyst.GUIS.register(key("gui/builder"),new MpGuiEntry(TileEntityBuilder.class, MenuBuilder.class));
        Catalyst.GUIS.register(key("gui/programmer"),new MpGuiEntry(TileEntityProgrammer.class, MenuProgrammer.class));
        Catalyst.GUIS.register(key("gui/injector"),new MpGuiEntry(TileEntityEnergyInjector.class, MenuInjector.class));
        Catalyst.GUIS.register(key("gui/warp_gate"),new MpGuiEntry(TileEntityWarpGate.class, MenuWarpGate.class));
        Catalyst.GUIS.register(key("gui/laser_drill"),new MpGuiEntry(TileEntityLaserDrill.class, MenuMultiblock.class));
        Catalyst.GUIS.register(key("gui/greenhouse"),new MpGuiEntry(TileEntityGreenhouse.class, MenuMultiblock.class));

        Catalyst.GUIS.register(key("gui/switch_cover"),new MpGuiEntry(TileEntityCoverable.class, MenuCover.class));
        Catalyst.GUIS.register(key("gui/void_cover"),new MpGuiEntry(TileEntityCoverable.class, MenuCover.class));
        Catalyst.GUIS.register(key("gui/redstone_cover"),new MpGuiEntry(TileEntityCoverable.class, MenuCover.class));

        LOGGER.info("SI Server initialized.");
    }
}
