package sunsetsatellite.signalindustries.api.impl.guidebookpp;

import net.minecraft.src.ContainerGuidebookRecipeBase;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;
import sunsetsatellite.guidebookpp.IRecipeHandlerBase;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.PlateFormerRecipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RecipeHandlerPlateFormer
    implements IRecipeHandlerBase {
    public ContainerGuidebookRecipeBase getContainer(Object o) {
        RecipePlateFormer recipe = (RecipePlateFormer) o;
        return new ContainerGuidebookPlateFormerRecipe(new ItemStack(SignalIndustries.prototypePlateFormer),recipe.itemInputs,recipe.fluidInputs,recipe.itemOutputs,recipe.fluidOutputs);
    }


    public int getRecipeAmount() {
        return getRecipes().size();
    }

    public ArrayList<?> getRecipes() {
        HashMap<Integer, ItemStack> rawRecipes = new HashMap<>(PlateFormerRecipes.getInstance().getRecipeList());
        ArrayList<RecipePlateFormer> recipes = new ArrayList<>();
        rawRecipes.forEach((I,O)->{
            ArrayList<ItemStack> singletonList = new ArrayList<>(Collections.singleton(new ItemStack(I, 1, 0)));
            ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
            recipes.add(new RecipePlateFormer(singletonList,null,singletonlist2, null));
        });
        return recipes;
    }

    public ArrayList<?> getRecipesFiltered(ItemStack filter, boolean usage) {
        HashMap<Integer,ItemStack> rawRecipes = new HashMap<>(PlateFormerRecipes.getInstance().getRecipeList());
        ArrayList<RecipePlateFormer> recipes = new ArrayList<>();
        rawRecipes.forEach((I,O)->{
            if(usage){
                if(new ItemStack(I,1,0).isItemEqual(filter)){
                    ArrayList<ItemStack> singletonList = new ArrayList<>(Collections.singleton(new ItemStack(I, 1, 0)));
                    ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
                    recipes.add(new RecipePlateFormer(singletonList,null,singletonlist2, null));
                }
            } else {
                if(O.isItemEqual(filter)){
                    ArrayList<ItemStack> singletonList = new ArrayList<>(Collections.singleton(new ItemStack(I, 1, 0)));
                    ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
                    recipes.add(new RecipePlateFormer(singletonList,null,singletonlist2, null));
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
        HashMap<Integer,ItemStack> rawRecipes = new HashMap<>(PlateFormerRecipes.getInstance().getRecipeList());
        ArrayList<RecipePlateFormer> recipes = new ArrayList<>();
        rawRecipes.forEach((I,O)->{
            ArrayList<ItemStack> singletonList = new ArrayList<>(Collections.singleton(new ItemStack(I, 1, 0)));
            ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
            recipes.add(new RecipePlateFormer(singletonList,null,singletonlist2, null));
        });
        recipes.removeIf((R)->!getNameOfRecipeOutput(R).contains(name.toLowerCase()));
        return recipes;
    }

    @Override
    public String getNameOfRecipeOutput(Object recipe){
        StringTranslate trans = StringTranslate.getInstance();
        return trans.translateKey(((RecipePlateFormer)recipe).itemOutputs.get(0).getItemName()+".name").toLowerCase();
    }

    @Override
    public String getHandlerName() {
        return "plate former";
    }
}
