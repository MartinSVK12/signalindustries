package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.fluidapi.SignalIndustriesFluidPlugin;
import sunsetsatellite.signalindustries.recipes.CraftingRecipesSI;
import sunsetsatellite.signalindustries.recipes.RecipeGroupSI;
import sunsetsatellite.signalindustries.recipes.RecipeNamespaceSI;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineFluid;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.Arrays;

public class SIRecipes implements RecipeEntrypoint {
    public static final RecipeNamespaceSI SIGNAL_INDUSTRIES = new RecipeNamespaceSI();
    public static final RecipeGroup<RecipeEntryCrafting<?,?>> WORKBENCH = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Block.workbench)));
    public static final RecipeGroupSI<RecipeEntryMachineFluid> EXTRACTOR = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SignalIndustries.prototypeExtractor), new ItemStack(SignalIndustries.basicExtractor))));
    public static final RecipeGroupSI<RecipeEntryMachine> CRUSHER = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SignalIndustries.prototypeCrusher),new ItemStack(SignalIndustries.basicCrusher))));
    public static final RecipeGroupSI<RecipeEntryMachineFluid> PUMP = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SignalIndustries.prototypePump))));

    @Override
    public void onRecipesReady() {
        new SignalIndustriesFluidPlugin().initializePlugin(SignalIndustries.LOGGER);
        new CraftingRecipesSI().addRecipes();
        Registries.RECIPE_TYPES.register("signalindustries:machine", RecipeEntryMachine.class);
        Registries.RECIPE_TYPES.register("signalindustries:machine/fluid", RecipeEntryMachineFluid.class);
        new ExtractorRecipes().addRecipes(EXTRACTOR);
        new CrusherRecipes().addRecipes(CRUSHER);
        new PumpRecipes().addRecipes(PUMP);
        SIGNAL_INDUSTRIES.register("workbench",WORKBENCH);
        SIGNAL_INDUSTRIES.register("extractor",EXTRACTOR);
        SIGNAL_INDUSTRIES.register("crusher",CRUSHER);
        SIGNAL_INDUSTRIES.register("pump",PUMP);
        Registries.RECIPES.register("signalindustries",SIGNAL_INDUSTRIES);
        SignalIndustries.LOGGER.info(SIGNAL_INDUSTRIES.getAllRecipes().size()+" recipes in "+SIGNAL_INDUSTRIES.size()+" groups.");
    }
}
