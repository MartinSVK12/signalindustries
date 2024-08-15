package sunsetsatellite.signalindustries.api.impl.catalyst;


import org.slf4j.Logger;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.fluids.registry.FluidContainerRegistryEntry;
import sunsetsatellite.catalyst.fluids.util.FluidType;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Collections;

import static sunsetsatellite.catalyst.Catalyst.listOf;

public class SignalIndustriesFluidPlugin {
    public void initializePlugin(Logger logger) {
        logger.info("Loading fluids from signalindustries..");
        CatalystFluids.TYPES.register(new FluidType("signalindustries:energy",listOf(SIBlocks.energyFlowing,SIBlocks.energyStill)));
        CatalystFluids.TYPES.register(new FluidType("signalindustries:burntSignalum",listOf(SIBlocks.burntSignalumFlowing,SIBlocks.burntSignalumStill)));
        logger.info("Loading fluid containers from signalindustries..");
        FluidContainerRegistryEntry entry = new FluidContainerRegistryEntry(SignalIndustries.MOD_ID, SIItems.signalumCrystalBattery, SIItems.signalumCrystalBattery, Collections.singletonList(SIBlocks.energyFlowing));
        CatalystFluids.CONTAINERS.register(SignalIndustries.key("signalumCrystal"),entry);
        entry = new FluidContainerRegistryEntry(SignalIndustries.MOD_ID, SIItems.signalumSaber, SIItems.signalumSaber,  Collections.singletonList(SIBlocks.energyFlowing));
        CatalystFluids.CONTAINERS.register(SignalIndustries.key("signalumSaber"),entry);
        entry = new FluidContainerRegistryEntry(SignalIndustries.MOD_ID, SIItems.basicSignalumDrill, SIItems.basicSignalumDrill,  Collections.singletonList(SIBlocks.energyFlowing));
        CatalystFluids.CONTAINERS.register(SignalIndustries.key("basicSignalumDrill"),entry);
        entry = new FluidContainerRegistryEntry(SignalIndustries.MOD_ID, SIItems.reinforcedSignalumDrill, SIItems.reinforcedSignalumDrill,  Collections.singletonList(SIBlocks.energyFlowing));
        CatalystFluids.CONTAINERS.register(SignalIndustries.key("reinforcedSignalumDrill"),entry);
        entry = new FluidContainerRegistryEntry(SignalIndustries.MOD_ID, SIItems.fuelCell, SIItems.fuelCell,  Collections.singletonList(SIBlocks.energyFlowing));
        CatalystFluids.CONTAINERS.register(SignalIndustries.key("fuelCellFuel"),entry);
        entry = new FluidContainerRegistryEntry(SignalIndustries.MOD_ID, SIItems.fuelCell, SIItems.fuelCell,  Collections.singletonList(SIBlocks.burntSignalumFlowing));
        CatalystFluids.CONTAINERS.register(SignalIndustries.key("fuelCellDepleted"),entry);
        entry = new FluidContainerRegistryEntry(SignalIndustries.MOD_ID, SIItems.infiniteSignalumCrystal, SIItems.infiniteSignalumCrystal,  Collections.singletonList(SIBlocks.energyFlowing));
        CatalystFluids.CONTAINERS.register(SignalIndustries.key("infiniteCrystal"),entry);
    }
}
