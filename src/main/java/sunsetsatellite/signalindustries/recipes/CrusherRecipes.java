package sunsetsatellite.signalindustries.recipes;


import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.HashMap;

public class CrusherRecipes extends MachineRecipesBase<Integer, ItemStack> {
    public static final CrusherRecipes instance = new CrusherRecipes();

    protected CrusherRecipes() {
        addRecipe(SignalIndustries.signalumCrystalEmpty.id,new ItemStack(SignalIndustries.emptySignalumCrystalDust,2));
        addRecipe(SignalIndustries.rawSignalumCrystal.id,new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,1));
        addRecipe(SignalIndustries.signalumCrystal.id,new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,2));
        addRecipe(Item.coal.id,new ItemStack(SignalIndustries.coalDust,1));
    }

    public void addRecipe(Integer id, ItemStack stack) {
        this.recipeList.put(id, stack);
    }

    public ItemStack getResult(Integer id) {
        ItemStack stack = this.recipeList.get(id);
        return stack == null ? null : stack.copy();
    }

    public HashMap<Integer, ItemStack> getRecipeList() {
        return this.recipeList;
    }
}