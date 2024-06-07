package sunsetsatellite.signalindustries.recipes.legacy;


import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SIItems;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class AlloySmelterRecipes extends MachineRecipesBase<Integer[], ItemStack> {
    public static final AlloySmelterRecipes instance = new AlloySmelterRecipes();

    protected AlloySmelterRecipes() {
        addRecipe(new Integer[]{Item.ingotSteel.id, SIItems.emptySignalumCrystalDust.id},new ItemStack(SIItems.crystalAlloyIngot,1));
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