package sunsetsatellite.signalindustries.recipes;

import net.minecraft.src.Block;
import net.minecraft.src.BlockFluid;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.util.MachineRecipes;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.HashMap;

public class ExtractorRecipes {
    private static final ExtractorRecipes instance = new ExtractorRecipes();
    private final HashMap<Integer, FluidStack> recipeList = new HashMap<>();

    public static ExtractorRecipes getInstance() {
        return instance;
    }

    private ExtractorRecipes() {
        this.addRecipe(SignalIndustries.rawSignalumCrystal.itemID,new FluidStack((BlockFluid) SignalIndustries.energyFlowing,150));
    }

    public void addRecipe(int id, FluidStack stack) {
        this.recipeList.put(id, stack);
    }

    public FluidStack getResult(int id) {
        FluidStack stack = ((FluidStack)this.recipeList.get(id));
        return stack == null ? null : stack.copy();
    }

    public HashMap<Integer,FluidStack> getRecipeList() {
        return this.recipeList;
    }
}
