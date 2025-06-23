package sunsetsatellite.signalindustries;

import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import sunsetsatellite.catalyst.fluids.util.Fluid;

import static sunsetsatellite.signalindustries.SignalIndustries.LOGGER;

public class SIFluids extends DataInitializer {

    public static Fluid ENERGY;
    public static Fluid BURNT_ENERGY;

    @Override
    public void init() {
        if (initialized) return;
        LOGGER.info("Initializing fluids...");

        ENERGY = new Fluid(SignalIndustries.id("fluid/energy"), "fluid.signalindustries.energy", Catalyst.listOf(SIBlocks.energyFlowing, SIBlocks.energyStill));
        BURNT_ENERGY = new Fluid(SignalIndustries.id("fluid/burnt_energy"), "fluid.signalindustries.burntEnergy", Catalyst.listOf(SIBlocks.burntSignalumFlowing, SIBlocks.burntSignalumStill));

        setInitialized(true);
    }
}
