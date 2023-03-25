package sunsetsatellite.signalindustries.api.impl.guidebookpp;

import net.minecraft.src.ContainerGuidebookRecipeBase;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;
import sunsetsatellite.guidebookpp.IRecipeHandlerBase;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.AlloySmelterRecipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RecipeHandlerAlloySmelter
    implements IRecipeHandlerBase {
    public ContainerGuidebookRecipeBase getContainer(Object o) {
        RecipeAlloySmelter recipe = (RecipeAlloySmelter) o;
        return new ContainerGuidebookAlloySmelterRecipe(new ItemStack(SignalIndustries.prototypeAlloySmelter),recipe.itemInputs,recipe.fluidInputs,recipe.itemOutputs,recipe.fluidOutputs);
    }


    public int getRecipeAmount() {
        return getRecipes().size();
    }

    public ArrayList<?> getRecipes() {
        HashMap<Item[], ItemStack> rawRecipes = new HashMap<>(AlloySmelterRecipes.getInstance().getRecipeList());
        ArrayList<RecipeAlloySmelter> recipes = new ArrayList<>();
        rawRecipes.forEach((I,O)->{
            ArrayList<ItemStack> list = new ArrayList<>();
            for (Item item : I) {
                list.add(new ItemStack(item,1,0));
            }
            ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
            recipes.add(new RecipeAlloySmelter(list,null,singletonlist2, null));
        });
        return recipes;
    }

    public ArrayList<?> getRecipesFiltered(ItemStack filter, boolean usage) {
        HashMap<Item[],ItemStack> rawRecipes = new HashMap<>(AlloySmelterRecipes.getInstance().getRecipeList());
        ArrayList<RecipeAlloySmelter> recipes = new ArrayList<>();
        rawRecipes.forEach((I,O)->{
            if(usage){
                if(new ItemStack(I[0],1,0).isItemEqual(filter) || new ItemStack(I[1],1,0).isItemEqual(filter)){
                    ArrayList<ItemStack> list = new ArrayList<>();
                    for (Item item : I) {
                        list.add(new ItemStack(item,1,0));
                    }
                    ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
                    recipes.add(new RecipeAlloySmelter(list,null,singletonlist2, null));
                }
            } else {
                if(O.isItemEqual(filter)){
                    ArrayList<ItemStack> list = new ArrayList<>();
                    for (Item item : I) {
                        list.add(new ItemStack(item,1,0));
                    }
                    ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
                    recipes.add(new RecipeAlloySmelter(list,null,singletonlist2, null));
                }
            }
        });
        return recipes;
    }

    @Override
    public ArrayList<?> getRecipesFiltered(String name) {
        if(name.equals("")){
            return getRecipes();
        }
        HashMap<Item[],ItemStack> rawRecipes = new HashMap<>(AlloySmelterRecipes.getInstance().getRecipeList());
        ArrayList<RecipeAlloySmelter> recipes = new ArrayList<>();
        rawRecipes.forEach((I,O)->{
            ArrayList<ItemStack> list = new ArrayList<>();
            for (Item item : I) {
                list.add(new ItemStack(item,1,0));
            }
            ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
            recipes.add(new RecipeAlloySmelter(list,null,singletonlist2, null));
        });
        recipes.removeIf((R)->!getNameOfRecipeOutput(R).contains(name.toLowerCase()));
        return recipes;
    }

    @Override
    public String getNameOfRecipeOutput(Object recipe){
        StringTranslate trans = StringTranslate.getInstance();
        return trans.translateKey(((RecipeAlloySmelter)recipe).itemOutputs.get(0).getItemName()+".name").toLowerCase();
    }

    @Override
    public String getHandlerName() {
        return "alloy smelter";
    }
}
