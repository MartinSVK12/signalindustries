package sunsetsatellite.signalindustries.api.impl.fluidapi;

import net.minecraft.src.BlockFluid;
import net.minecraft.src.Item;
import org.slf4j.Logger;
import sunsetsatellite.fluidapi.FluidAPIPlugin;
import sunsetsatellite.fluidapi.FluidRegistry;
import sunsetsatellite.signalindustries.SignalIndustries;

public class SignalIndustriesFluidPlugin implements FluidAPIPlugin {
    @Override
    public void initializePlugin(FluidRegistry registry, Logger logger) {
        logger.info("Loading fluids from signalindustries..");
        registry.addFluid((BlockFluid) SignalIndustries.energyFlowing,SignalIndustries.signalumCrystal, SignalIndustries.signalumCrystalEmpty);
    }
}
