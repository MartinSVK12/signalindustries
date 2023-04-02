package sunsetsatellite.signalindustries.recipes;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AlloySmelterRecipes extends MachineRecipesBase<Integer[],ItemStack> {
    public static final AlloySmelterRecipes instance = new AlloySmelterRecipes();

    protected AlloySmelterRecipes() {
        addRecipe(new Integer[]{Item.ingotSteel.itemID, SignalIndustries.emptySignalumCrystalDust.itemID},new ItemStack(SignalIndustries.crystalAlloyIngot,1));
    }

    public void addRecipe(Integer[] ids, ItemStack stack) {
        this.recipeList.put(ids, stack);
    }

    public ItemStack getResult(Integer[] ids) {
        ItemStack stack = null;
        for(Map.Entry<Integer[],ItemStack> recipe : recipeList.entrySet()){
            if(Arrays.equals(recipe.getKey(),ids)){
                stack = recipe.getValue();
            }
        }
        return stack == null ? null : stack.copy();
    }

    public HashMap<Integer[], ItemStack> getRecipeList() {
        return this.recipeList;
    }
}