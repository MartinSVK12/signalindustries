package sunsetsatellite.signalindustries.recipes;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;

import java.util.HashMap;

public class PumpRecipes extends MachineRecipesBase<Integer, FluidStack> {
    public static final PumpRecipes instance = new PumpRecipes();

    protected PumpRecipes() {
        this.addRecipe(Block.fluidWaterStill.id,new FluidStack((BlockFluid) Block.fluidWaterFlowing,1000));
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
