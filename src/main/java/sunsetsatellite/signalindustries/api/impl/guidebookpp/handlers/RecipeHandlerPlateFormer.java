package sunsetsatellite.signalindustries.api.impl.guidebookpp.handlers;


import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.ContainerGuidebookRecipeBase;
import sunsetsatellite.fluidapi.gbookpp.RecipeFluid;
import sunsetsatellite.guidebookpp.GuidebookPlusPlus;
import sunsetsatellite.guidebookpp.IRecipeHandlerBase;
import sunsetsatellite.guidebookpp.RecipeGroup;
import sunsetsatellite.guidebookpp.RecipeRegistry;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.guidebookpp.containers.ContainerGuidebookPlateFormerRecipe;
import sunsetsatellite.signalindustries.recipes.PlateFormerRecipes;

import java.util.ArrayList;
import java.util.Collections;

public class RecipeHandlerPlateFormer
    implements IRecipeHandlerBase {
    public ContainerGuidebookRecipeBase getContainer(Object o) {
        RecipeFluid recipe = (RecipeFluid) o;
        return new ContainerGuidebookPlateFormerRecipe(new ItemStack(SignalIndustries.prototypePlateFormer),recipe);
    }

    @Override
    public void addRecipes() {
        GuidebookPlusPlus.LOGGER.info("Adding recipes for: " + this.getClass().getSimpleName());
        ArrayList<RecipeFluid> recipes = new ArrayList<>();
        PlateFormerRecipes.getInstance().getRecipeList().forEach((I, O)->{
            recipes.add(new RecipeFluid(
                    new ArrayList<>(Collections.singletonList(new ItemStack(I, 1, 0))),
                    new ArrayList<>(),
                    new ArrayList<>(Collections.singletonList(O)),
                    new ArrayList<>(),1,0
            ));
        });
        RecipeGroup group = new RecipeGroup(SignalIndustries.MOD_ID, SignalIndustries.prototypePlateFormer,this,recipes);
        RecipeRegistry.groups.add(group);
    }

}
