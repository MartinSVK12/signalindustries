package sunsetsatellite.signalindustries;

import sunsetsatellite.catalyst.core.util.DataInitializer;
import turniplabs.halplibe.util.RecipeEntrypoint;

import static sunsetsatellite.signalindustries.SignalIndustries.LOGGER;

public class SIRecipes implements RecipeEntrypoint {

    @Override
    public void onRecipesReady() {
        LOGGER.info("Loading SI recipes...");
    }

    @Override
    public void initNamespaces() {
        LOGGER.info("Loading SI recipe namespaces...");
    }

}
