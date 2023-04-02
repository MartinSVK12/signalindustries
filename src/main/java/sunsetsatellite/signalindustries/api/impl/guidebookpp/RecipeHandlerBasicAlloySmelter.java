package sunsetsatellite.signalindustries.api.impl.guidebookpp;

import net.minecraft.src.ContainerGuidebookRecipeBase;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;
import sunsetsatellite.guidebookpp.IRecipeHandlerBase;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.BasicAlloySmelterRecipes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RecipeHandlerBasicAlloySmelter
    implements IRecipeHandlerBase {
    public ContainerGuidebookRecipeBase getContainer(Object o) {
        RecipeBasicAlloySmelter recipe = (RecipeBasicAlloySmelter) o;
        return new ContainerGuidebookAlloySmelterRecipe(new ItemStack(SignalIndustries.basicAlloySmelter),recipe.itemInputs,recipe.fluidInputs,recipe.itemOutputs,recipe.fluidOutputs);
    }


    public int getRecipeAmount() {
        return getRecipes().size();
    }

    public ArrayList<?> getRecipes() {
        HashMap<Integer[], ItemStack> rawRecipes = new HashMap<>(BasicAlloySmelterRecipes.instance.getRecipeList());
        ArrayList<RecipeBasicAlloySmelter> recipes = new ArrayList<>();
        rawRecipes.forEach((I,O)->{
            ArrayList<ItemStack> list = new ArrayList<>();
            for (Integer item : I) {
                list.add(new ItemStack(item,1,0));
            }
            ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
            recipes.add(new RecipeBasicAlloySmelter(list,null,singletonlist2, null));
        });
        return recipes;
    }

    public ArrayList<?> getRecipesFiltered(ItemStack filter, boolean usage) {
        HashMap<Integer[],ItemStack> rawRecipes = new HashMap<>(BasicAlloySmelterRecipes.instance.getRecipeList());
        ArrayList<RecipeBasicAlloySmelter> recipes = new ArrayList<>();
        rawRecipes.forEach((I,O)->{
            if(usage){
                if(new ItemStack(I[0],1,0).isItemEqual(filter) || new ItemStack(I[1],1,0).isItemEqual(filter)){
                    ArrayList<ItemStack> list = new ArrayList<>();
                    for (Integer item : I) {
                        list.add(new ItemStack(item,1,0));
                    }
                    ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
                    recipes.add(new RecipeBasicAlloySmelter(list,null,singletonlist2, null));
                }
            } else {
                if(O.isItemEqual(filter)){
                    ArrayList<ItemStack> list = new ArrayList<>();
                    for (Integer item : I) {
                        list.add(new ItemStack(item,1,0));
                    }
                    ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
                    recipes.add(new RecipeBasicAlloySmelter(list,null,singletonlist2, null));
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
        HashMap<Integer[],ItemStack> rawRecipes = new HashMap<>(BasicAlloySmelterRecipes.instance.getRecipeList());
        ArrayList<RecipeBasicAlloySmelter> recipes = new ArrayList<>();
        rawRecipes.forEach((I,O)->{
            ArrayList<ItemStack> list = new ArrayList<>();
            for (Integer item : I) {
                list.add(new ItemStack(item,1,0));
            }
            ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
            recipes.add(new RecipeBasicAlloySmelter(list,null,singletonlist2, null));
        });
        recipes.removeIf((R)->!getNameOfRecipeOutput(R).contains(name.toLowerCase()));
        return recipes;
    }

    @Override
    public String getNameOfRecipeOutput(Object recipe){
        StringTranslate trans = StringTranslate.getInstance();
        return trans.translateKey(((RecipeBasicAlloySmelter)recipe).itemOutputs.get(0).getItemName()+".name").toLowerCase();
    }

    @Override
    public String getHandlerName() {
        return "basic alloy smelter";
    }
}
