package sunsetsatellite.signalindustries.api.impl.guidebookpp.handlers;


import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.guidebookpp.GuidebookPlusPlus;
import sunsetsatellite.guidebookpp.IRecipeHandlerBase;
import sunsetsatellite.guidebookpp.RecipeGroup;
import sunsetsatellite.guidebookpp.RecipeRegistry;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.guidebookpp.containers.ContainerGuidebookInfuserRecipe;
import sunsetsatellite.fluidapi.gbookpp.RecipeFluid;
import sunsetsatellite.signalindustries.recipes.ExtractorRecipes;
import sunsetsatellite.signalindustries.recipes.InfuserRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class RecipeHandlerInfuser
    implements IRecipeHandlerBase {
    public ContainerGuidebookRecipeBase getContainer(Object o) {
        RecipeFluid recipe = (RecipeFluid) o;
        return new ContainerGuidebookInfuserRecipe(new ItemStack(SignalIndustries.basicInfuser),recipe);
    }

    @Override
    public void addRecipes() {
        GuidebookPlusPlus.LOGGER.info("Adding recipes for: " + this.getClass().getSimpleName());
        ArrayList<RecipeFluid> recipes = new ArrayList<>();
        InfuserRecipes.instance.getRecipeList().forEach((I, O)->{
            ArrayList<ItemStack> itemInputList = new ArrayList<>();
            ArrayList<FluidStack> fluidInputList = new ArrayList<>();
            for (Object obj : I) {
                if(obj instanceof ItemStack){
                    itemInputList.add((ItemStack) obj);
                } else if (obj instanceof FluidStack) {
                    fluidInputList.add((FluidStack) obj);
                }

            }
            ArrayList<ItemStack> itemOutputList = new ArrayList<>(Collections.singleton(O));
            recipes.add(new RecipeFluid(
                itemInputList,fluidInputList,itemOutputList,new ArrayList<>(),1,0
            ));
        });
        RecipeGroup group = new RecipeGroup(SignalIndustries.MOD_ID, SignalIndustries.basicInfuser,this,recipes);
        RecipeRegistry.groups.add(group);
    }

}
