package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.block.Block;
import net.minecraft.core.data.DataLoader;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.catalyst.SignalIndustriesFluidPlugin;
import sunsetsatellite.signalindustries.recipes.RecipeGroupSI;
import sunsetsatellite.signalindustries.recipes.RecipeNamespaceSI;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineFluid;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SIRecipes implements RecipeEntrypoint {
    public static final RecipeNamespaceSI SIGNAL_INDUSTRIES = new RecipeNamespaceSI();
    public static final RecipeGroup<RecipeEntryCrafting<?,?>> WORKBENCH = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Block.workbench)));
    public static final RecipeGroupSI<RecipeEntryMachineFluid> EXTRACTOR = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SignalIndustries.prototypeExtractor), new ItemStack(SignalIndustries.basicExtractor))));
    public static final RecipeGroupSI<RecipeEntryMachine> CRUSHER = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SignalIndustries.prototypeCrusher),new ItemStack(SignalIndustries.basicCrusher))));
    public static final RecipeGroupSI<RecipeEntryMachine> ALLOY_SMELTER = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SignalIndustries.prototypeAlloySmelter),new ItemStack(SignalIndustries.basicAlloySmelter))));
    public static final RecipeGroupSI<RecipeEntryMachine> PLATE_FORMER = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SignalIndustries.prototypePlateFormer))));
    public static final RecipeGroupSI<RecipeEntryMachineFluid> PUMP = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SignalIndustries.prototypePump))));
    public static final RecipeGroupSI<RecipeEntryMachine> CRYSTAL_CUTTER = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SignalIndustries.prototypeCrystalCutter),new ItemStack(SignalIndustries.basicCrystalCutter))));
    public static final RecipeGroupSI<RecipeEntryMachine> CRYSTAL_CHAMBER = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SignalIndustries.basicCrystalChamber))));
    public static final RecipeGroupSI<RecipeEntryMachine> INFUSER = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SignalIndustries.basicInfuser))));
    public static final RecipeGroupSI<RecipeEntryMachine> CENTRIFUGE = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SignalIndustries.reinforcedCentrifuge))));


    @Override
    public void onRecipesReady() {
        new SignalIndustriesFluidPlugin().initializePlugin(SignalIndustries.LOGGER);
        Registries.RECIPE_TYPES.register("signalindustries:machine", RecipeEntryMachine.class);
        Registries.RECIPE_TYPES.register("signalindustries:machine/fluid", RecipeEntryMachineFluid.class);
        List<ItemStack> abilityGroup = new ArrayList<>();
        abilityGroup.add(SignalIndustries.boostAbilityContainer.getDefaultStack());
        abilityGroup.add(SignalIndustries.projectileAbilityContainer.getDefaultStack());
        Registries.ITEM_GROUPS.register("signalindustries:ability_containers",abilityGroup);
        new ExtractorRecipes().addRecipes(EXTRACTOR);
        new CrusherRecipes().addRecipes(CRUSHER);
        new AlloySmelterRecipes().addRecipes(ALLOY_SMELTER);
        new PlateFormerRecipes().addRecipes(PLATE_FORMER);
        new PumpRecipes().addRecipes(PUMP);
        new CrystalCutterRecipes().addRecipes(CRYSTAL_CUTTER);
        new CrystalChamberRecipes().addRecipes(CRYSTAL_CHAMBER);
        new InfuserRecipes().addRecipes(INFUSER);
        new CentrifugeRecipes().addRecipes(CENTRIFUGE);
        SIGNAL_INDUSTRIES.register("workbench",WORKBENCH);
        SIGNAL_INDUSTRIES.register("extractor",EXTRACTOR);
        SIGNAL_INDUSTRIES.register("crusher",CRUSHER);
        SIGNAL_INDUSTRIES.register("alloy_smelter",ALLOY_SMELTER);
        SIGNAL_INDUSTRIES.register("plate_former",PLATE_FORMER);
        SIGNAL_INDUSTRIES.register("pump",PUMP);
        SIGNAL_INDUSTRIES.register("crystal_cutter",CRYSTAL_CUTTER);
        SIGNAL_INDUSTRIES.register("crystal_chamber",CRYSTAL_CHAMBER);
        SIGNAL_INDUSTRIES.register("infuser",INFUSER);
        SIGNAL_INDUSTRIES.register("centrifuge",CENTRIFUGE);
        Registries.RECIPES.register("signalindustries",SIGNAL_INDUSTRIES);
        DataLoader.loadRecipes("/assets/signalindustries/recipes/workbench.json");
        SignalIndustries.LOGGER.info(SIGNAL_INDUSTRIES.getAllRecipes().size()+" recipes in "+SIGNAL_INDUSTRIES.size()+" groups.");
    }
}
