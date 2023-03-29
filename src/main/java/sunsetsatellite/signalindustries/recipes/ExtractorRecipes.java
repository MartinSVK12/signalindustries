package sunsetsatellite.signalindustries.recipes;

import net.minecraft.src.Block;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.ItemStack;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.util.MachineRecipes;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.HashMap;

public class ExtractorRecipes extends MachineRecipesBase<Integer,FluidStack> {
    public static final ExtractorRecipes instance = new ExtractorRecipes();

    protected ExtractorRecipes() {
        this.addRecipe(SignalIndustries.rawSignalumCrystal.itemID,new FluidStack((BlockFluid) SignalIndustries.energyFlowing,160));
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
