package sunsetsatellite.signalindustries.recipes;




import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.HashMap;

public class PlateFormerRecipes extends MachineRecipesBase<Integer,ItemStack> {
    private static final PlateFormerRecipes instance = new PlateFormerRecipes();

    public static PlateFormerRecipes getInstance() {
        return instance;
    }

    private PlateFormerRecipes() {
        addRecipe(Block.stone.blockID,new ItemStack(SignalIndustries.stonePlate,2));
        addRecipe(Block.cobbleStone.blockID,new ItemStack(SignalIndustries.cobblestonePlate,2));
        addRecipe(Item.ingotSteel.itemID,new ItemStack(SignalIndustries.steelPlate,1));
        addRecipe(SignalIndustries.crystalAlloyIngot.itemID,new ItemStack(SignalIndustries.crystalAlloyPlate,1));
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