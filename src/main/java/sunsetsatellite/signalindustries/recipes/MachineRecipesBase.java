package sunsetsatellite.signalindustries.recipes;

import net.minecraft.src.ItemStack;
import sunsetsatellite.fluidapi.api.FluidStack;

import java.util.HashMap;

public abstract class MachineRecipesBase<I,O> {

    protected final HashMap<I, O> recipeList = new HashMap<>();

    public abstract HashMap<I,O> getRecipeList();

    public abstract void addRecipe(I input, O output);

    public abstract O getResult(I input);

}
