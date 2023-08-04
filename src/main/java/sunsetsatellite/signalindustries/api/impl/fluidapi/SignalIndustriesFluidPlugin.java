package sunsetsatellite.signalindustries.api.impl.fluidapi;



import org.slf4j.Logger;
import sunsetsatellite.fluidapi.FluidAPIPlugin;
import sunsetsatellite.fluidapi.FluidRegistry;
import sunsetsatellite.fluidapi.FluidRegistryEntry;
import sunsetsatellite.signalindustries.SignalIndustries;

public class SignalIndustriesFluidPlugin implements FluidAPIPlugin {
    /*@Override
    public void initializePlugin(FluidRegistry registry, Logger logger) {
        logger.info("Loading fluids from signalindustries..");
        registry.addFluid((BlockFluid) SignalIndustries.energyFlowing,SignalIndustries.signalumCrystal, SignalIndustries.signalumCrystalEmpty);
    }*/

    @Override
    public void initializePlugin(Logger logger) {
        logger.info("Loading fluids from signalindustries..");
        FluidRegistryEntry entry = new FluidRegistryEntry(SignalIndustries.MOD_ID,SignalIndustries.signalumCrystal,SignalIndustries.signalumCrystalEmpty, (BlockFluid) SignalIndustries.energyFlowing);
        FluidRegistry.addToRegistry("signalumEnergy",entry);
        entry = new FluidRegistryEntry(SignalIndustries.MOD_ID,SignalIndustries.signalumSaber,SignalIndustries.signalumSaber, (BlockFluid) SignalIndustries.energyFlowing);
        FluidRegistry.addToRegistry("signalumSaber",entry);
    }
}
