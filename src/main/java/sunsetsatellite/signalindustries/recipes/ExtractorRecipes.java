package sunsetsatellite.signalindustries.recipes;


import net.minecraft.core.block.BlockFluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.HashMap;

public class ExtractorRecipes extends MachineRecipesBase<Integer, FluidStack> {
    public static final ExtractorRecipes instance = new ExtractorRecipes();

    protected ExtractorRecipes() {
        this.addRecipe(SignalIndustries.rawSignalumCrystal.id,new FluidStack((BlockFluid) SignalIndustries.energyFlowing,160));
    }

    public HashMap<Integer,FluidStack> getRecipeList() {
        return this.recipeList;
    }

    @Override
    public void addRecipe(Integer input, FluidStack output) {
        this.recipeList.put(input, output);
    }

    @Override
    public FluidStack getResult(Integer input) {
        FluidStack stack = this.recipeList.get(input);
        return stack == null ? null : stack.copy();
    }

}
