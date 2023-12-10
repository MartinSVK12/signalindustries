package sunsetsatellite.signalindustries.api.impl.guidebookpp;

import org.slf4j.Logger;
import sunsetsatellite.guidebookpp.GuidebookCustomRecipePlugin;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.guidebookpp.handlers.*;

public class SignalIndustriesGuidebookPlugin implements GuidebookCustomRecipePlugin {
    @Override
    public void initializePlugin(Logger logger) {
        logger.info("Loading recipe plugin: "+this.getClass().getSimpleName()+" from "+ SignalIndustries.MOD_ID);
        new RecipeHandlerExtractor().addRecipes();
        new RecipeHandlerBasicExtractor().addRecipes();
        new RecipeHandlerCrusher().addRecipes();
        new RecipeHandlerBasicCrusher().addRecipes();
        new RecipeHandlerAlloySmelter().addRecipes();
        new RecipeHandlerBasicAlloySmelter().addRecipes();
        new RecipeHandlerPlateFormer().addRecipes();
        new RecipeHandlerInfuser().addRecipes();
        new RecipeHandlerCrystalCutter().addRecipes();
        new RecipeHandlerCrystalChamber().addRecipes();
        new RecipeHandlerBasicCrystalCutter().addRecipes();
        new RecipeHandlerCraftingSI().addRecipes(); //preferably at the bottom

    }
}
