package sunsetsatellite.signalindustries.api.impl.guidebookpp;

import org.slf4j.Logger;
import sunsetsatellite.guidebookpp.CustomGuidebookRecipeRegistry;
import sunsetsatellite.guidebookpp.GuidebookCustomRecipePlugin;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.guidebookpp.handlers.*;
import sunsetsatellite.signalindustries.api.impl.guidebookpp.recipes.*;

public class SignalIndustriesGuidebookPlugin implements GuidebookCustomRecipePlugin {
    @Override
    public void initializePlugin(CustomGuidebookRecipeRegistry registry, Logger logger) {
        logger.info("Loading recipe plugin: "+this.getClass().getSimpleName()+" from "+ SignalIndustries.MOD_ID);
        registry.addHandler(new RecipeHandlerExtractor(), RecipeExtractor.class);
        registry.addHandler(new RecipeHandlerCrusher(), RecipeCrusher.class);
        registry.addHandler(new RecipeHandlerAlloySmelter(), RecipeAlloySmelter.class);
        registry.addHandler(new RecipeHandlerBasicAlloySmelter(), RecipeBasicAlloySmelter.class);
        registry.addHandler(new RecipeHandlerPlateFormer(),RecipePlateFormer.class);
        registry.addHandler(new RecipeHandlerCrystalCutter(), RecipeCrystalCutter.class);
        registry.addHandler(new RecipeHandlerInfuser(),RecipeInfuser.class);
        registry.addHandler(new RecipeHandlerBasicCrusher(),RecipeBasicCrusher.class);
    }
}