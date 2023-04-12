package sunsetsatellite.signalindustries.recipes;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.HashMap;

public class CrusherRecipes extends MachineRecipesBase<Integer,ItemStack> {
    public static final CrusherRecipes instance = new CrusherRecipes();

    protected CrusherRecipes() {
        addRecipe(SignalIndustries.signalumCrystalEmpty.itemID,new ItemStack(SignalIndustries.emptySignalumCrystalDust,2));
        addRecipe(SignalIndustries.rawSignalumCrystal.itemID,new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,1));
        addRecipe(SignalIndustries.signalumCrystal.itemID,new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,2));
        addRecipe(Item.coal.itemID,new ItemStack(SignalIndustries.coalDust,1));
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