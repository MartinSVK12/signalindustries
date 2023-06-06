package sunsetsatellite.signalindustries.recipes;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CrystalChamberRecipes extends MachineRecipesBase<Integer[],ItemStack> {
    public static final CrystalChamberRecipes instance = new CrystalChamberRecipes();

    protected CrystalChamberRecipes() {
        addRecipe(new Integer[]{SignalIndustries.signalumCrystal.itemID, SignalIndustries.signalumCrystal.itemID},new ItemStack(SignalIndustries.signalumCrystal,1));
        addRecipe(new Integer[]{SignalIndustries.signalumCrystalEmpty.itemID, SignalIndustries.signalumCrystalEmpty.itemID},new ItemStack(SignalIndustries.signalumCrystalEmpty,1));
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