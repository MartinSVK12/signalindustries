package sunsetsatellite.signalindustries.recipes;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AlloySmelterRecipes {
    private static final AlloySmelterRecipes instance = new AlloySmelterRecipes();
    private final HashMap<Item[],ItemStack> recipeList = new HashMap<>();

    public static AlloySmelterRecipes getInstance() {
        return instance;
    }

    private AlloySmelterRecipes() {
        addRecipe(new Item[]{Item.ingotSteel, SignalIndustries.emptySignalumCrystalDust},new ItemStack(SignalIndustries.crystalAlloyIngot,1));
    }

    public void addRecipe(Item[] ids, ItemStack stack) {
        this.recipeList.put(ids, stack);
    }

    public ItemStack getResult(Item[] ids) {
        ItemStack stack = null;
        for(Map.Entry<Item[],ItemStack> recipe : recipeList.entrySet()){
            if(Arrays.equals(recipe.getKey(),ids)){
                stack = recipe.getValue();
            }
        }
        return stack == null ? null : stack.copy();
    }

    public HashMap<Item[], ItemStack> getRecipeList() {
        return this.recipeList;
    }
}