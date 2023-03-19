package sunsetsatellite.signalindustries.recipes;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.HashMap;
import java.util.Map;

public class CrusherRecipes {
    private static final CrusherRecipes instance = new CrusherRecipes();
    private final HashMap<Integer,ItemStack> recipeList = new HashMap<>();

    public static CrusherRecipes getInstance() {
        return instance;
    }

    private CrusherRecipes() {
        addRecipe(SignalIndustries.signalumCrystalEmpty.itemID,new ItemStack(SignalIndustries.emptySignalumCrystalDust,2));
        addRecipe(SignalIndustries.rawSignalumCrystal.itemID,new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,1));
        addRecipe(SignalIndustries.signalumCrystal.itemID,new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,2));
        addRecipe(Item.coal.itemID,new ItemStack(SignalIndustries.coalDust,1));
    }

    public void addRecipe(int id, ItemStack stack) {
        this.recipeList.put(id, stack);
    }

    public ItemStack getResult(int id) {
        ItemStack stack = ((ItemStack)this.recipeList.get(id));
        return stack == null ? null : stack.copy();
    }

    public Map getRecipeList() {
        return this.recipeList;
    }
}