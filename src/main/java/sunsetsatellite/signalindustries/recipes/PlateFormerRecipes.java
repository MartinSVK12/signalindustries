package sunsetsatellite.signalindustries.recipes;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.HashMap;
import java.util.Map;

public class PlateFormerRecipes {
    private static final PlateFormerRecipes instance = new PlateFormerRecipes();
    private final HashMap<Integer,ItemStack> recipeList = new HashMap<>();

    public static PlateFormerRecipes getInstance() {
        return instance;
    }

    private PlateFormerRecipes() {
        addRecipe(Block.stone.blockID,new ItemStack(SignalIndustries.stonePlate,2));
        addRecipe(Block.cobbleStone.blockID,new ItemStack(SignalIndustries.cobblestonePlate,2));
        addRecipe(Item.ingotSteel.itemID,new ItemStack(SignalIndustries.steelPlate,1));
        addRecipe(SignalIndustries.crystalAlloyIngot.itemID,new ItemStack(SignalIndustries.crystalAlloyPlate,1));
        /*addRecipe(SignalIndustries.signalumCrystalEmpty.itemID,new ItemStack(SignalIndustries.emptySignalumCrystalDust,2));
        addRecipe(SignalIndustries.rawSignalumCrystal.itemID,new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,1));
        addRecipe(SignalIndustries.signalumCrystal.itemID,new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,2));
        addRecipe(Item.coal.itemID,new ItemStack(SignalIndustries.coalDust,1));*/
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