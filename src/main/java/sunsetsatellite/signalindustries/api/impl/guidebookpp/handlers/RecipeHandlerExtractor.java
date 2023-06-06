package sunsetsatellite.signalindustries.api.impl.guidebookpp.handlers;

import net.minecraft.src.*;
import sunsetsatellite.fluidapi.FluidAPI;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.util.MachineRecipes;
import sunsetsatellite.guidebookpp.GuidebookPlusPlus;
import sunsetsatellite.guidebookpp.IRecipeHandlerBase;
import sunsetsatellite.guidebookpp.RecipeGroup;
import sunsetsatellite.guidebookpp.RecipeRegistry;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.guidebookpp.containers.ContainerGuidebookExtractorRecipe;
import sunsetsatellite.fluidapi.gbookpp.RecipeFluid;
import sunsetsatellite.signalindustries.recipes.ExtractorRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class RecipeHandlerExtractor
    implements IRecipeHandlerBase {
    public ContainerGuidebookRecipeBase getContainer(Object o) {
        RecipeFluid recipe = (RecipeFluid) o;
        return new ContainerGuidebookExtractorRecipe(new ItemStack(SignalIndustries.prototypeExtractor),recipe);
    }

    @Override
    public void addRecipes() {
        GuidebookPlusPlus.LOGGER.info("Adding recipes for: " + this.getClass().getSimpleName());
        ArrayList<RecipeFluid> recipes = new ArrayList<>();
        ExtractorRecipes.instance.getRecipeList().forEach((I, O)->{
            recipes.add(new RecipeFluid(
                    new ArrayList<>(Arrays.asList(new ItemStack(I,1,0))),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(Arrays.asList(O)),1,0)
            );
        });
        RecipeGroup group = new RecipeGroup(SignalIndustries.MOD_ID, SignalIndustries.prototypeExtractor,this,recipes);
        RecipeRegistry.groups.add(group);
    }

}
