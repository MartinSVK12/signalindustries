package sunsetsatellite.signalindustries.api.impl.guidebookpp.handlers;

import net.minecraft.src.ContainerGuidebookRecipeBase;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;
import sunsetsatellite.guidebookpp.IRecipeHandlerBase;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.guidebookpp.containers.ContainerGuidebookCrusherRecipe;
import sunsetsatellite.signalindustries.api.impl.guidebookpp.recipes.RecipeCrusher;
import sunsetsatellite.signalindustries.recipes.CrusherRecipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RecipeHandlerCrusher
    implements IRecipeHandlerBase {
    public ContainerGuidebookRecipeBase getContainer(Object o) {
        RecipeCrusher recipe = (RecipeCrusher) o;
        return new ContainerGuidebookCrusherRecipe(new ItemStack(SignalIndustries.prototypeCrusher),recipe.itemInputs,recipe.fluidInputs,recipe.itemOutputs,recipe.fluidOutputs);
    }


    public int getRecipeAmount() {
        return getRecipes().size();
    }

    public ArrayList<?> getRecipes() {
        HashMap<Integer, ItemStack> rawRecipes = new HashMap<>(CrusherRecipes.instance.getRecipeList());
        ArrayList<RecipeCrusher> recipes = new ArrayList<>();
        rawRecipes.forEach((I,O)->{
            ArrayList<ItemStack> singletonList = new ArrayList<>(Collections.singleton(new ItemStack(I, 1, 0)));
            ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
            recipes.add(new RecipeCrusher(singletonList,null,singletonlist2, null));
        });
        return recipes;
    }

    public ArrayList<?> getRecipesFiltered(ItemStack filter, boolean usage) {
        HashMap<Integer,ItemStack> rawRecipes = new HashMap<>(CrusherRecipes.instance.getRecipeList());
        ArrayList<RecipeCrusher> recipes = new ArrayList<>();
        rawRecipes.forEach((I,O)->{

            if(usage){
                if(new ItemStack(I,1,0).isItemEqual(filter)){
                    ArrayList<ItemStack> singletonList = new ArrayList<>(Collections.singleton(new ItemStack(I, 1, 0)));
                    ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
                    recipes.add(new RecipeCrusher(singletonList,null,singletonlist2, null));
                }
            } else {
                if(O.isItemEqual(filter)){
                    ArrayList<ItemStack> singletonList = new ArrayList<>(Collections.singleton(new ItemStack(I, 1, 0)));
                    ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
                    recipes.add(new RecipeCrusher(singletonList,null,singletonlist2, null));
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
        HashMap<Integer,ItemStack> rawRecipes = new HashMap<>(CrusherRecipes.instance.getRecipeList());
        ArrayList<RecipeCrusher> recipes = new ArrayList<>();
        rawRecipes.forEach((I,O)->{
            ArrayList<ItemStack> singletonList = new ArrayList<>(Collections.singleton(new ItemStack(I, 1, 0)));
            ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
            recipes.add(new RecipeCrusher(singletonList,null,singletonlist2, null));
        });
        recipes.removeIf((R)->!getNameOfRecipeOutput(R).contains(name.toLowerCase()));
        return recipes;
    }

    @Override
    public String getNameOfRecipeOutput(Object recipe){
        StringTranslate trans = StringTranslate.getInstance();
        return trans.translateKey(((RecipeCrusher)recipe).itemOutputs.get(0).getItemName()+".name").toLowerCase();
    }

    @Override
    public String getHandlerName() {
        return "signalum crusher";
    }
}
