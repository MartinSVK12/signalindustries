package sunsetsatellite.signalindustries.api.impl.guidebookpp;

import org.slf4j.Logger;
import sunsetsatellite.guidebookpp.CustomGuidebookRecipeRegistry;
import sunsetsatellite.guidebookpp.GuidebookCustomRecipePlugin;
import sunsetsatellite.signalindustries.SignalIndustries;

public class SignalIndustriesGuidebookPlugin implements GuidebookCustomRecipePlugin {
    @Override
    public void initializePlugin(CustomGuidebookRecipeRegistry registry, Logger logger) {
        logger.info("Loading recipe plugin: "+this.getClass().getSimpleName()+" from "+ SignalIndustries.MOD_ID);
        registry.addHandler(new RecipeHandlerExtractor(), RecipeExtractor.class);
        registry.addHandler(new RecipeHandlerCrusher(), RecipeCrusher.class);
    }
}
