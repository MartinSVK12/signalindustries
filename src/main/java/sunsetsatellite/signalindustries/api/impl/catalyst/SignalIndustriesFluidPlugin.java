package sunsetsatellite.signalindustries.api.impl.catalyst;


import org.slf4j.Logger;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.fluids.registry.FluidRegistryEntry;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Collections;

public class SignalIndustriesFluidPlugin {
    public void initializePlugin(Logger logger) {
        logger.info("Loading fluids from signalindustries..");
        FluidRegistryEntry entry = new FluidRegistryEntry(SignalIndustries.MOD_ID, SIItems.signalumCrystalBattery, SIItems.signalumCrystalEmpty, Collections.singletonList(SIBlocks.energyFlowing));
        CatalystFluids.FLUIDS.register(SignalIndustries.key("signalumEnergy"),entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID, SIItems.signalumSaber, SIItems.signalumSaber,  Collections.singletonList(SIBlocks.energyFlowing));
        CatalystFluids.FLUIDS.register(SignalIndustries.key("signalumSaber"),entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID, SIItems.basicSignalumDrill, SIItems.basicSignalumDrill,  Collections.singletonList(SIBlocks.energyFlowing));
        CatalystFluids.FLUIDS.register(SignalIndustries.key("basicSignalumDrill"),entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID, SIItems.reinforcedSignalumDrill, SIItems.reinforcedSignalumDrill,  Collections.singletonList(SIBlocks.energyFlowing));
        CatalystFluids.FLUIDS.register(SignalIndustries.key("reinforcedSignalumDrill"),entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID, SIItems.fuelCell, SIItems.fuelCell,  Collections.singletonList(SIBlocks.energyFlowing));
        CatalystFluids.FLUIDS.register(SignalIndustries.key("fuelCellFuel"),entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID, SIItems.fuelCell, SIItems.fuelCell,  Collections.singletonList(SIBlocks.burntSignalumFlowing));
        CatalystFluids.FLUIDS.register(SignalIndustries.key("fuelCellDepleted"),entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID, SIItems.infiniteSignalumCrystal, SIItems.infiniteSignalumCrystal,  Collections.singletonList(SIBlocks.energyFlowing));
        CatalystFluids.FLUIDS.register(SignalIndustries.key("infiniteCrystal"),entry);
    }
}
