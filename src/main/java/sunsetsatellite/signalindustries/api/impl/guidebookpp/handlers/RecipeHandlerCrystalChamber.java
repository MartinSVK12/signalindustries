package sunsetsatellite.signalindustries.api.impl.guidebookpp.handlers;

import net.minecraft.src.ContainerGuidebookRecipeBase;
import net.minecraft.src.ItemStack;
import sunsetsatellite.fluidapi.gbookpp.RecipeFluid;
import sunsetsatellite.guidebookpp.GuidebookPlusPlus;
import sunsetsatellite.guidebookpp.IRecipeHandlerBase;
import sunsetsatellite.guidebookpp.RecipeGroup;
import sunsetsatellite.guidebookpp.RecipeRegistry;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.guidebookpp.containers.ContainerGuidebookAlloySmelterRecipe;
import sunsetsatellite.signalindustries.api.impl.guidebookpp.containers.ContainerGuidebookCrystalChamberRecipe;
import sunsetsatellite.signalindustries.recipes.AlloySmelterRecipes;
import sunsetsatellite.signalindustries.recipes.CrystalChamberRecipes;

import java.util.ArrayList;
import java.util.Collections;

public class RecipeHandlerCrystalChamber
    implements IRecipeHandlerBase {
    public ContainerGuidebookRecipeBase getContainer(Object o) {
        RecipeFluid recipe = (RecipeFluid) o;
        return new ContainerGuidebookCrystalChamberRecipe(new ItemStack(SignalIndustries.basicCrystalChamber),recipe);
    }

    @Override
    public void addRecipes() {
        GuidebookPlusPlus.LOGGER.info("Adding recipes for: " + this.getClass().getSimpleName());
        ArrayList<RecipeFluid> recipes = new ArrayList<>();
        CrystalChamberRecipes.instance.getRecipeList().forEach((I, O)->{
            ArrayList<ItemStack> itemInputList = new ArrayList<>();
            for (Integer integer : I) {
                itemInputList.add(new ItemStack(integer,1,0));
            }
            ArrayList<ItemStack> itemOutputList = new ArrayList<>(Collections.singleton(O));
            recipes.add(new RecipeFluid(
                    itemInputList,new ArrayList<>(),itemOutputList,new ArrayList<>(),1,0
            ));
        });
        RecipeGroup group = new RecipeGroup(SignalIndustries.MOD_ID, SignalIndustries.basicCrystalChamber,this,recipes);
        RecipeRegistry.groups.add(group);
    }

}
