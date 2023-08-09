package sunsetsatellite.signalindustries.api.impl.guidebookpp.handlers;


import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.ContainerGuidebookRecipeBase;
import sunsetsatellite.fluidapi.gbookpp.RecipeFluid;
import sunsetsatellite.guidebookpp.GuidebookPlusPlus;
import sunsetsatellite.guidebookpp.IRecipeHandlerBase;
import sunsetsatellite.guidebookpp.RecipeGroup;
import sunsetsatellite.guidebookpp.RecipeRegistry;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.guidebookpp.containers.ContainerGuidebookAlloySmelterRecipe;
import sunsetsatellite.signalindustries.recipes.BasicAlloySmelterRecipes;

import java.util.ArrayList;
import java.util.Collections;

public class RecipeHandlerBasicAlloySmelter
    implements IRecipeHandlerBase {
    public ContainerGuidebookRecipeBase getContainer(Object o) {
        RecipeFluid recipe = (RecipeFluid) o;
        return new ContainerGuidebookAlloySmelterRecipe(new ItemStack(SignalIndustries.basicAlloySmelter),recipe);
    }

    @Override
    public void addRecipes() {
        GuidebookPlusPlus.LOGGER.info("Adding recipes for: " + this.getClass().getSimpleName());
        ArrayList<RecipeFluid> recipes = new ArrayList<>();
        BasicAlloySmelterRecipes.instance.getRecipeList().forEach((I, O)->{
            ArrayList<ItemStack> itemInputList = new ArrayList<>();
            for (Integer integer : I) {
                itemInputList.add(new ItemStack(integer,1,0));
            }
            ArrayList<ItemStack> itemOutputList = new ArrayList<>(Collections.singleton(O));
            recipes.add(new RecipeFluid(
                    itemInputList,new ArrayList<>(),itemOutputList,new ArrayList<>(),1,0
            ));
        });
        RecipeGroup group = new RecipeGroup(SignalIndustries.MOD_ID, SignalIndustries.basicAlloySmelter,this,recipes);
        RecipeRegistry.groups.add(group);
    }


}
