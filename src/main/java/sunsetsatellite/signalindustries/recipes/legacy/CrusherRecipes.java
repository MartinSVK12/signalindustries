package sunsetsatellite.signalindustries.recipes.legacy;


import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SIItems;

import java.util.HashMap;
@Deprecated
public class CrusherRecipes extends MachineRecipesBase<Integer, ItemStack> {
    public static final CrusherRecipes instance = new CrusherRecipes();

    protected CrusherRecipes() {
        addRecipe(SIItems.signalumCrystalEmpty.id,new ItemStack(SIItems.emptySignalumCrystalDust,2));
        addRecipe(SIItems.rawSignalumCrystal.id,new ItemStack(SIItems.saturatedSignalumCrystalDust,1));
        addRecipe(SIItems.signalumCrystal.id,new ItemStack(SIItems.saturatedSignalumCrystalDust,2));
        addRecipe(Item.coal.id,new ItemStack(SIItems.coalDust,1));
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