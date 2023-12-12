package sunsetsatellite.signalindustries.api.impl.guidebookpp.handlers;


import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.ContainerGuidebookRecipeBase;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.gbookpp.RecipeFluid;
import sunsetsatellite.guidebookpp.GuidebookPlusPlus;
import sunsetsatellite.guidebookpp.IRecipeHandlerBase;
import sunsetsatellite.guidebookpp.RecipeGroup;
import sunsetsatellite.guidebookpp.RecipeRegistry;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.guidebookpp.containers.ContainerGuidebookCrystalCutterRecipe;
import sunsetsatellite.signalindustries.recipes.BasicCrystalCutterRecipes;

import java.util.ArrayList;
import java.util.Collections;

public class RecipeHandlerBasicCrystalCutter
    implements IRecipeHandlerBase {
    public ContainerGuidebookRecipeBase getContainer(Object o) {
        RecipeFluid recipe = (RecipeFluid) o;
        return new ContainerGuidebookCrystalCutterRecipe(new ItemStack(SignalIndustries.basicCrystalCutter),recipe);
    }

    @Override
    public void addRecipes() {
        GuidebookPlusPlus.LOGGER.info("Adding recipes for: " + this.getClass().getSimpleName());
        ArrayList<RecipeFluid> recipes = new ArrayList<>();
        BasicCrystalCutterRecipes.getInstance().getRecipeList().forEach((I, O)->{
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
                    itemInputList,fluidInputList,itemOutputList,new ArrayList<>(),1, (Integer) I.get(2)
            ));
        });
        RecipeGroup group = new RecipeGroup(SignalIndustries.MOD_ID, SignalIndustries.prototypeCrystalCutter,this,recipes);
        RecipeRegistry.groups.add(group);
    }

}