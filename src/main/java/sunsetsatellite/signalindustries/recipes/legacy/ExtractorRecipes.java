package sunsetsatellite.signalindustries.recipes.legacy;


import net.minecraft.core.block.BlockFluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;

import java.util.HashMap;
@Deprecated
public class ExtractorRecipes extends MachineRecipesBase<Integer, FluidStack> {
    public static final ExtractorRecipes instance = new ExtractorRecipes();

    protected ExtractorRecipes() {
        this.addRecipe(SIItems.rawSignalumCrystal.id,new FluidStack((BlockFluid) SIBlocks.energyFlowing,160));
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
