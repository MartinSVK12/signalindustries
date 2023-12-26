package sunsetsatellite.signalindustries.recipes.legacy;


import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.HashMap;
@Deprecated
public class PlateFormerRecipes extends MachineRecipesBase<Integer, ItemStack> {
    private static final PlateFormerRecipes instance = new PlateFormerRecipes();

    public static PlateFormerRecipes getInstance() {
        return instance;
    }

    private PlateFormerRecipes() {
        addRecipe(Block.stone.id,new ItemStack(SignalIndustries.stonePlate,2));
        addRecipe(Block.cobbleStone.id,new ItemStack(SignalIndustries.cobblestonePlate,2));
        addRecipe(Item.ingotSteel.id,new ItemStack(SignalIndustries.steelPlate,1));
        addRecipe(SignalIndustries.crystalAlloyIngot.id,new ItemStack(SignalIndustries.crystalAlloyPlate,1));
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