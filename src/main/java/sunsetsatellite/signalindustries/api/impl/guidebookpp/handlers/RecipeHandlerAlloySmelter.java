package sunsetsatellite.signalindustries.api.impl.guidebookpp.handlers;

import net.minecraft.src.ContainerGuidebookRecipeBase;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.guidebookpp.GuidebookPlusPlus;
import sunsetsatellite.guidebookpp.IRecipeHandlerBase;
import sunsetsatellite.guidebookpp.RecipeGroup;
import sunsetsatellite.guidebookpp.RecipeRegistry;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.guidebookpp.containers.ContainerGuidebookAlloySmelterRecipe;
import sunsetsatellite.fluidapi.gbookpp.RecipeFluid;
import sunsetsatellite.signalindustries.recipes.AlloySmelterRecipes;
import sunsetsatellite.signalindustries.recipes.InfuserRecipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RecipeHandlerAlloySmelter
    implements IRecipeHandlerBase {
    public ContainerGuidebookRecipeBase getContainer(Object o) {
        RecipeFluid recipe = (RecipeFluid) o;
        return new ContainerGuidebookAlloySmelterRecipe(new ItemStack(SignalIndustries.prototypeAlloySmelter),recipe);
    }

    @Override
    public void addRecipes() {
        GuidebookPlusPlus.LOGGER.info("Adding recipes for: " + this.getClass().getSimpleName());
        ArrayList<RecipeFluid> recipes = new ArrayList<>();
        AlloySmelterRecipes.instance.getRecipeList().forEach((I, O)->{
            ArrayList<ItemStack> itemInputList = new ArrayList<>();
            for (Integer integer : I) {
                itemInputList.add(new ItemStack(integer,1,0));
            }
            ArrayList<ItemStack> itemOutputList = new ArrayList<>(Collections.singleton(O));
            recipes.add(new RecipeFluid(
                    itemInputList,new ArrayList<>(),itemOutputList,new ArrayList<>(),1,0
            ));
        });
        RecipeGroup group = new RecipeGroup(SignalIndustries.MOD_ID, SignalIndustries.prototypeAlloySmelter,this,recipes);
        RecipeRegistry.groups.add(group);
    }

}
