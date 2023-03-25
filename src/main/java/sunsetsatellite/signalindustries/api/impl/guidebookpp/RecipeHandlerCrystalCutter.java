package sunsetsatellite.signalindustries.api.impl.guidebookpp;

import net.minecraft.src.*;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.guidebookpp.IRecipeHandlerBase;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.CrystalCutterRecipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RecipeHandlerCrystalCutter
    implements IRecipeHandlerBase {
    public ContainerGuidebookRecipeBase getContainer(Object o) {
        RecipeCrystalCutter recipe = (RecipeCrystalCutter) o;
        return new ContainerGuidebookCrystalCutterRecipe(new ItemStack(SignalIndustries.prototypeCrystalCutter),recipe.itemInputs,recipe.fluidInputs,recipe.itemOutputs,recipe.fluidOutputs);
    }


    public int getRecipeAmount() {
        return getRecipes().size();
    }

    public ArrayList<?> getRecipes() {
        HashMap<ArrayList<Object>, ItemStack> rawRecipes = new HashMap<>(CrystalCutterRecipes.getInstance().getRecipeList());
        ArrayList<RecipeCrystalCutter> recipes = new ArrayList<>();
        rawRecipes.forEach((I,O)->{
            ArrayList<ItemStack> list = new ArrayList<>();
            ArrayList<FluidStack> fluidList = new ArrayList<>();
            for (Object obj : I) {
                if(obj instanceof ItemStack){
                    list.add((ItemStack) obj);
                } else if (obj instanceof FluidStack) {
                    fluidList.add((FluidStack) obj);
                }

            }
            ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
            recipes.add(new RecipeCrystalCutter(list,fluidList,singletonlist2, null));
        });
        return recipes;
    }

    public ArrayList<?> getRecipesFiltered(ItemStack filter, boolean usage) {
        HashMap<ArrayList<Object>,ItemStack> rawRecipes = new HashMap<>(CrystalCutterRecipes.getInstance().getRecipeList());
        ArrayList<RecipeCrystalCutter> recipes = new ArrayList<>();
        rawRecipes.forEach((I,O)->{
            if(usage){
                for(Object obj : I){
                    if((obj instanceof ItemStack && ((ItemStack) obj).isItemEqual(filter)) || ( filter.itemID < 16384 && (obj instanceof FluidStack && ((FluidStack) obj).isFluidEqual(new FluidStack((BlockFluid) Block.blocksList[filter.itemID], filter.stackSize))))) {
                        ArrayList<ItemStack> list = new ArrayList<>();
                        ArrayList<FluidStack> fluidList = new ArrayList<>();
                        if (obj instanceof ItemStack) {
                            list.add((ItemStack) obj);
                        } else {
                            fluidList.add((FluidStack) obj);
                        }
                        ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
                        recipes.add(new RecipeCrystalCutter(list, fluidList, singletonlist2, null));
                        break;
                    }
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
        HashMap<ArrayList<Object>,ItemStack> rawRecipes = new HashMap<>(CrystalCutterRecipes.getInstance().getRecipeList());
        ArrayList<RecipeCrystalCutter> recipes = new ArrayList<>();
        rawRecipes.forEach((I,O)->{
            ArrayList<ItemStack> list = new ArrayList<>();
            ArrayList<FluidStack> fluidList = new ArrayList<>();
            for (Object obj : I) {
                if(obj instanceof ItemStack){
                    list.add((ItemStack) obj);
                } else if (obj instanceof FluidStack) {
                    fluidList.add((FluidStack) obj);
                }

            }
            ArrayList<ItemStack> singletonlist2 = new ArrayList<>(Collections.singleton(O));
            recipes.add(new RecipeCrystalCutter(list,fluidList,singletonlist2, null));
        });
        recipes.removeIf((R)->!getNameOfRecipeOutput(R).contains(name.toLowerCase()));
        return recipes;
    }

    @Override
    public String getNameOfRecipeOutput(Object recipe){
        StringTranslate trans = StringTranslate.getInstance();
        return trans.translateKey(((RecipeCrystalCutter)recipe).itemOutputs.get(0).getItemName()+".name").toLowerCase();
    }

    @Override
    public String getHandlerName() {
        return "crystal cutter";
    }
}
