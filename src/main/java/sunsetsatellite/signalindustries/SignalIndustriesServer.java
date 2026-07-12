package sunsetsatellite.signalindustries;

import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.GuiEntry;
import sunsetsatellite.catalyst.core.util.mp.entry.TileGuiEntry;
import sunsetsatellite.signalindustries.gui.menus.*;
import sunsetsatellite.signalindustries.gui.screens.*;
import sunsetsatellite.signalindustries.invs.InventoryAbilityModule;
import sunsetsatellite.signalindustries.invs.InventoryBackpack;
import sunsetsatellite.signalindustries.invs.InventoryHarness;
import sunsetsatellite.signalindustries.invs.InventoryPulsar;
import sunsetsatellite.signalindustries.powersuit.InventoryPowerSuit;
import sunsetsatellite.signalindustries.powersuit.MenuPowerSuit;
import sunsetsatellite.signalindustries.tiles.machines.*;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityDimensionalAnchor;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityInductionSmelter;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingAlloySmelter;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingCrusher;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.waking.TileEntityWakingInfuser;
import sunsetsatellite.signalindustries.tiles.machines.simple.*;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityEnergyConnector;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityFluidHatch;
import sunsetsatellite.signalindustries.tiles.multiblock.TileEntityItemBus;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class SignalIndustriesServer implements DedicatedServerModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("signalindustries|server");

	@Override
	public void onInitializeServer() {
		LOGGER.info("SI Server is being initialized...");
		LOGGER.info("Registering serverside GUI menus...");
		Catalyst.GUIS.register(key("gui/crusher"), new GuiEntry<>(TileEntityCrusher.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/extractor"), new GuiEntry<>(TileEntityExtractor.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/collector"), new GuiEntry<>(TileEntityCollector.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/alloy_smelter"), new GuiEntry<>(TileEntityAlloySmelter.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/plate_former"), new GuiEntry<>(TileEntityPlateFormer.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/crystal_cutter"), new GuiEntry<>(TileEntityCrystalCutter.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/crystal_chamber"), new GuiEntry<>(TileEntityCrystalChamber.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/booster"), new GuiEntry<>(TileEntityBooster.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/infuser"), new GuiEntry<>(TileEntityInfuser.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/stabilizer"), new GuiEntry<>(TileEntityStabilizer.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/item_bus"), new GuiEntry<>(TileEntityItemBus.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/fluid_hatch"), new GuiEntry<>(TileEntityFluidHatch.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/energy_connector"), new GuiEntry<>(TileEntityEnergyConnector.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/energy_cell"), new GuiEntry<>(TileEntityEnergyCell.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/fluid_tank"), new GuiEntry<>(TileEntitySIFluidTank.class, MenuMachine.class));

		Catalyst.GUIS.register(key("gui/anchor"), new GuiEntry<>(TileEntityDimensionalAnchor.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/induction_smelter"), new GuiEntry<>(TileEntityInductionSmelter.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/waking_alloy_smelter"), new GuiEntry<>(TileEntityWakingAlloySmelter.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/waking_crusher"), new GuiEntry<>(TileEntityWakingCrusher.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/waking_plate_former"), new GuiEntry<>(TileEntityPlateFormer.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/waking_infuser"), new GuiEntry<>(TileEntityWakingInfuser.class, MenuMachine.class));
		Catalyst.GUIS.register(key("gui/energy_injector"), new GuiEntry<>(TileEntityEnergyInjector.class, MenuMachine.class));

		Catalyst.GUIS.register(key("gui/harness"), new GuiEntry<>(InventoryHarness.class, MenuHarness.class));
		Catalyst.GUIS.register(key("gui/power_suit"), new GuiEntry<>(InventoryPowerSuit.class, MenuPowerSuit.class));
		Catalyst.GUIS.register(key("gui/backpack"), new GuiEntry<>(InventoryBackpack.class, MenuBackpack.class));
		Catalyst.GUIS.register(key("gui/ability_module"), new GuiEntry<>(InventoryAbilityModule.class, MenuAbilityModule.class));
		Catalyst.GUIS.register(key("gui/pulsar"), new GuiEntry<>(InventoryPulsar.class, MenuPulsar.class));
	}
}
