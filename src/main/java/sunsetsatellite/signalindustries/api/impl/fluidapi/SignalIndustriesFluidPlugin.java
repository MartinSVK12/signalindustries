package sunsetsatellite.signalindustries.api.impl.fluidapi;


import net.minecraft.core.block.BlockFluid;
import org.slf4j.Logger;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.fluids.registry.FluidRegistryEntry;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Collections;

public class SignalIndustriesFluidPlugin {
    public void initializePlugin(Logger logger) {
        logger.info("Loading fluids from signalindustries..");
        FluidRegistryEntry entry = new FluidRegistryEntry(SignalIndustries.MOD_ID,SignalIndustries.signalumCrystal,SignalIndustries.signalumCrystalEmpty, Collections.singletonList((BlockFluid) SignalIndustries.energyFlowing));
        CatalystFluids.FLUIDS.register(SignalIndustries.key("signalumEnergy"),entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID,SignalIndustries.signalumSaber,SignalIndustries.signalumSaber,  Collections.singletonList((BlockFluid) SignalIndustries.energyFlowing));
        CatalystFluids.FLUIDS.register(SignalIndustries.key("signalumSaber"),entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID,SignalIndustries.basicSignalumDrill,SignalIndustries.basicSignalumDrill,  Collections.singletonList((BlockFluid) SignalIndustries.energyFlowing));
        CatalystFluids.FLUIDS.register(SignalIndustries.key("basicSignalumDrill"),entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID,SignalIndustries.reinforcedSignalumDrill,SignalIndustries.reinforcedSignalumDrill,  Collections.singletonList((BlockFluid) SignalIndustries.energyFlowing));
        CatalystFluids.FLUIDS.register(SignalIndustries.key("reinforcedSignalumDrill"),entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID,SignalIndustries.fuelCell,SignalIndustries.fuelCell,  Collections.singletonList((BlockFluid) SignalIndustries.energyFlowing));
        CatalystFluids.FLUIDS.register(SignalIndustries.key("fuelCellFuel"),entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID,SignalIndustries.fuelCell,SignalIndustries.fuelCell,  Collections.singletonList((BlockFluid) SignalIndustries.burntSignalumFlowing));
        CatalystFluids.FLUIDS.register(SignalIndustries.key("fuelCellDepleted"),entry);
    }
}
