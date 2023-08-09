package sunsetsatellite.signalindustries.recipes;


import net.minecraft.core.block.BlockFluid;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;

public class BasicExtractorRecipes extends ExtractorRecipes {
    public static final BasicExtractorRecipes instance = new BasicExtractorRecipes();

    protected BasicExtractorRecipes(){
        this.addRecipe(SignalIndustries.rawSignalumCrystal.id,new FluidStack((BlockFluid) SignalIndustries.energyFlowing,240));
    }
}
