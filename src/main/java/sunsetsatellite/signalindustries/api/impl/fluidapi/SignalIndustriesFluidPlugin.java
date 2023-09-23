package sunsetsatellite.signalindustries.api.impl.fluidapi;


import net.minecraft.core.block.BlockFluid;
import org.slf4j.Logger;
import sunsetsatellite.fluidapi.FluidAPIPlugin;
import sunsetsatellite.fluidapi.FluidRegistry;
import sunsetsatellite.fluidapi.FluidRegistryEntry;
import sunsetsatellite.signalindustries.SignalIndustries;

public class SignalIndustriesFluidPlugin implements FluidAPIPlugin {

    @Override
    public void initializePlugin(Logger logger) {
        logger.info("Loading fluids from signalindustries..");
        FluidRegistryEntry entry = new FluidRegistryEntry(SignalIndustries.MOD_ID,SignalIndustries.signalumCrystal,SignalIndustries.signalumCrystalEmpty, (BlockFluid) SignalIndustries.energyFlowing);
        FluidRegistry.addToRegistry("signalumEnergy",entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID,SignalIndustries.signalumSaber,SignalIndustries.signalumSaber, (BlockFluid) SignalIndustries.energyFlowing);
        FluidRegistry.addToRegistry("signalumSaber",entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID,SignalIndustries.basicSignalumDrill,SignalIndustries.basicSignalumDrill, (BlockFluid) SignalIndustries.energyFlowing);
        FluidRegistry.addToRegistry("basicSignalumDrill",entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID,SignalIndustries.reinforcedSignalumDrill,SignalIndustries.reinforcedSignalumDrill, (BlockFluid) SignalIndustries.energyFlowing);
        FluidRegistry.addToRegistry("reinforcedSignalumDrill",entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID,SignalIndustries.fuelCell,SignalIndustries.fuelCell, (BlockFluid) SignalIndustries.energyFlowing);
        FluidRegistry.addToRegistry("fuelCellFuel",entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID,SignalIndustries.fuelCell,SignalIndustries.fuelCell, (BlockFluid) SignalIndustries.burntSignalumFlowing);
        FluidRegistry.addToRegistry("fuelCellDepleted",entry);
    }
}
