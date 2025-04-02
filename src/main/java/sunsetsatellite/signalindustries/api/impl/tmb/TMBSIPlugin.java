package sunsetsatellite.signalindustries.api.impl.tmb;

import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.tmb.category.*;
import sunsetsatellite.signalindustries.api.impl.tmb.category.waking.WakingAlloySmelterRecipeCategory;
import sunsetsatellite.signalindustries.api.impl.tmb.category.waking.WakingCrusherRecipeCategory;
import sunsetsatellite.signalindustries.api.impl.tmb.category.waking.WakingInfuserRecipeCategory;
import sunsetsatellite.signalindustries.api.impl.tmb.category.waking.WakingPlateFormerRecipeCategory;
import turing.tmb.TMB;
import turing.tmb.TypedIngredient;
import turing.tmb.api.ITMBPlugin;
import turing.tmb.api.TMBEntrypoint;
import turing.tmb.api.runtime.ITMBRuntime;
import turing.tmb.vanilla.VanillaPlugin;

public class TMBSIPlugin implements ITMBPlugin, TMBEntrypoint {

    public static ExtractorRecipeCategory extractorRecipeCategory;
    public static CollectorRecipeCategory collectorRecipeCategory;
    public static AlloySmelterRecipeCategory alloySmelterCategory;
    public static CrusherRecipeCategory crusherCategory;
    public static PlateFormerRecipeCategory plateFormerCategory;
    public static CrystalCutterRecipeCategory crystalCutterCategory;
    public static CrystalChamberRecipeCategory crystalChamberCategory;
    public static InfuserRecipeCategory infuserRecipeCategory;
    public static CentrifugeRecipeCategory centrifugeCategory;
    public static StoneworksRecipeCategory stoneworksCategory;
    public static PumpRecipeCategory pumpCategory;

    public static WakingAlloySmelterRecipeCategory wAlloySmelterCategory;
    public static WakingCrusherRecipeCategory wCrusherCategory;
    public static WakingPlateFormerRecipeCategory wPlateFormerCategory;
    public static WakingInfuserRecipeCategory wInfuserRecipeCategory;

    @Override
    public void registerRecipeCatalysts(ITMBRuntime runtime) {
        runtime.getRecipeIndex().registerCatalyst(VanillaPlugin.furnaceCategory, TypedIngredient.itemStackIngredient(SIBlocks.basicInductionSmelter.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(VanillaPlugin.shapedCraftingCategory, TypedIngredient.itemStackIngredient(SIItems.portableWorkbench.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(VanillaPlugin.shapelessCraftingCategory, TypedIngredient.itemStackIngredient(SIItems.portableWorkbench.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(VanillaPlugin.shapedCraftingCategory, TypedIngredient.itemStackIngredient(SIBlocks.basicAssembler.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(VanillaPlugin.shapelessCraftingCategory, TypedIngredient.itemStackIngredient(SIBlocks.basicAssembler.getDefaultStack()));

        runtime.getRecipeIndex().registerCatalyst(extractorRecipeCategory, TypedIngredient.itemStackIngredient(SIBlocks.prototypeExtractor.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(extractorRecipeCategory, TypedIngredient.itemStackIngredient(SIBlocks.basicExtractor.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(extractorRecipeCategory, TypedIngredient.itemStackIngredient(SIBlocks.reinforcedExtractor.getDefaultStack()));

        runtime.getRecipeIndex().registerCatalyst(collectorRecipeCategory, TypedIngredient.itemStackIngredient(SIBlocks.basicCollector.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(collectorRecipeCategory, TypedIngredient.itemStackIngredient(SIBlocks.reinforcedCollector.getDefaultStack()));

        runtime.getRecipeIndex().registerCatalyst(alloySmelterCategory, TypedIngredient.itemStackIngredient(SIBlocks.prototypeAlloySmelter.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(alloySmelterCategory, TypedIngredient.itemStackIngredient(SIBlocks.basicAlloySmelter.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(alloySmelterCategory, TypedIngredient.itemStackIngredient(SIBlocks.reinforcedAlloySmelter.getDefaultStack()));

        runtime.getRecipeIndex().registerCatalyst(crusherCategory, TypedIngredient.itemStackIngredient(SIBlocks.prototypeCrystalCutter.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(crusherCategory, TypedIngredient.itemStackIngredient(SIBlocks.basicCrusher.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(crusherCategory, TypedIngredient.itemStackIngredient(SIBlocks.reinforcedCrusher.getDefaultStack()));

        runtime.getRecipeIndex().registerCatalyst(plateFormerCategory, TypedIngredient.itemStackIngredient(SIBlocks.prototypePlateFormer.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(plateFormerCategory, TypedIngredient.itemStackIngredient(SIBlocks.basicPlateFormer.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(plateFormerCategory, TypedIngredient.itemStackIngredient(SIBlocks.reinforcedPlateFormer.getDefaultStack()));

        runtime.getRecipeIndex().registerCatalyst(crystalCutterCategory, TypedIngredient.itemStackIngredient(SIBlocks.prototypeCrystalCutter.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(crystalCutterCategory, TypedIngredient.itemStackIngredient(SIBlocks.basicCrystalCutter.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(crystalCutterCategory, TypedIngredient.itemStackIngredient(SIBlocks.reinforcedCrystalCutter.getDefaultStack()));

        runtime.getRecipeIndex().registerCatalyst(crystalChamberCategory, TypedIngredient.itemStackIngredient(SIBlocks.basicCrystalChamber.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(crystalChamberCategory, TypedIngredient.itemStackIngredient(SIBlocks.reinforcedCrystalChamber.getDefaultStack()));

        runtime.getRecipeIndex().registerCatalyst(infuserRecipeCategory, TypedIngredient.itemStackIngredient(SIBlocks.basicInfuser.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(infuserRecipeCategory, TypedIngredient.itemStackIngredient(SIBlocks.reinforcedInfuser.getDefaultStack()));

        runtime.getRecipeIndex().registerCatalyst(centrifugeCategory, TypedIngredient.itemStackIngredient(SIBlocks.reinforcedCentrifuge.getDefaultStack()));

        runtime.getRecipeIndex().registerCatalyst(stoneworksCategory, TypedIngredient.itemStackIngredient(SIBlocks.basicStoneworks.getDefaultStack()));

        runtime.getRecipeIndex().registerCatalyst(pumpCategory, TypedIngredient.itemStackIngredient(SIBlocks.prototypePump.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(pumpCategory, TypedIngredient.itemStackIngredient(SIBlocks.basicPump.getDefaultStack()));

        runtime.getRecipeIndex().registerCatalyst(wAlloySmelterCategory, TypedIngredient.itemStackIngredient(SIBlocks.wakingAlloySmelter.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(wCrusherCategory, TypedIngredient.itemStackIngredient(SIBlocks.wakingCrusher.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(wInfuserRecipeCategory, TypedIngredient.itemStackIngredient(SIBlocks.wakingInfuser.getDefaultStack()));
        runtime.getRecipeIndex().registerCatalyst(wPlateFormerCategory, TypedIngredient.itemStackIngredient(SIBlocks.wakingPlateFormer.getDefaultStack()));
    }

    @Override
    public void registerRecipeCategories(ITMBRuntime runtime) {
        extractorRecipeCategory = runtime.getRecipeIndex().registerCategory(new ExtractorRecipeCategory());
        collectorRecipeCategory = runtime.getRecipeIndex().registerCategory(new CollectorRecipeCategory());
        alloySmelterCategory = runtime.getRecipeIndex().registerCategory(new AlloySmelterRecipeCategory());
        crusherCategory = runtime.getRecipeIndex().registerCategory(new CrusherRecipeCategory());
        plateFormerCategory = runtime.getRecipeIndex().registerCategory(new PlateFormerRecipeCategory());
        crystalCutterCategory = runtime.getRecipeIndex().registerCategory(new CrystalCutterRecipeCategory());
        crystalChamberCategory = runtime.getRecipeIndex().registerCategory(new CrystalChamberRecipeCategory());
        infuserRecipeCategory = runtime.getRecipeIndex().registerCategory(new InfuserRecipeCategory());
        centrifugeCategory = runtime.getRecipeIndex().registerCategory(new CentrifugeRecipeCategory());
        stoneworksCategory = runtime.getRecipeIndex().registerCategory(new StoneworksRecipeCategory());
        pumpCategory = runtime.getRecipeIndex().registerCategory(new PumpRecipeCategory());

        wAlloySmelterCategory = runtime.getRecipeIndex().registerCategory(new WakingAlloySmelterRecipeCategory());
        wCrusherCategory = runtime.getRecipeIndex().registerCategory(new WakingCrusherRecipeCategory());
        wPlateFormerCategory = runtime.getRecipeIndex().registerCategory(new WakingPlateFormerRecipeCategory());
        wInfuserRecipeCategory = runtime.getRecipeIndex().registerCategory(new WakingInfuserRecipeCategory());
    }

    @Override
    public void registerRecipes(ITMBRuntime runtime) {
        runtime.getRecipeIndex().registerRecipes(extractorRecipeCategory, SIRecipes.EXTRACTOR.getAllRecipes(), FluidMachineRecipeTranslator::new);
        runtime.getRecipeIndex().registerRecipes(collectorRecipeCategory, SIRecipes.COLLECTOR.getAllRecipes(), FluidMachineRecipeTranslator::new);
        runtime.getRecipeIndex().registerRecipes(alloySmelterCategory, SIRecipes.ALLOY_SMELTER.getAllRecipes(), MachineRecipeTranslator::new);
        runtime.getRecipeIndex().registerRecipes(crusherCategory, SIRecipes.CRUSHER.getAllRecipes(), MachineRecipeTranslator::new);
        runtime.getRecipeIndex().registerRecipes(plateFormerCategory, SIRecipes.PLATE_FORMER.getAllRecipes(), MachineRecipeTranslator::new);
        runtime.getRecipeIndex().registerRecipes(crystalCutterCategory, SIRecipes.CRYSTAL_CUTTER.getAllRecipes(), MachineRecipeTranslator::new);
        runtime.getRecipeIndex().registerRecipes(crystalChamberCategory, SIRecipes.CRYSTAL_CHAMBER.getAllRecipes(), MachineRecipeTranslator::new);
        runtime.getRecipeIndex().registerRecipes(infuserRecipeCategory, SIRecipes.INFUSER.getAllRecipes(), MachineRecipeTranslator::new);
        runtime.getRecipeIndex().registerRecipes(centrifugeCategory, SIRecipes.CENTRIFUGE.getAllRecipes(), MachineRecipeTranslator::new);
        runtime.getRecipeIndex().registerRecipes(stoneworksCategory, SIRecipes.STONEWORKS.getAllRecipes(), MachineRecipeTranslator::new);
        runtime.getRecipeIndex().registerRecipes(pumpCategory, SIRecipes.PUMP.getAllRecipes(), FluidMachineRecipeTranslator::new);

        runtime.getRecipeIndex().registerRecipes(wAlloySmelterCategory, SIRecipes.WAKING_ALLOY_SMELTER.getAllRecipes(), MachineRecipeTranslator::new);
        runtime.getRecipeIndex().registerRecipes(wCrusherCategory, SIRecipes.WAKING_CRUSHER.getAllRecipes(), MachineRecipeTranslator::new);
        runtime.getRecipeIndex().registerRecipes(wPlateFormerCategory, SIRecipes.WAKING_PLATE_FORMER.getAllRecipes(), MachineRecipeTranslator::new);
        runtime.getRecipeIndex().registerRecipes(wInfuserRecipeCategory, SIRecipes.WAKING_INFUSER.getAllRecipes(), MachineRecipeTranslator::new);
    }

    @Override
    public void onGatherPlugins(boolean isReload) {
        TMB.LOGGER.info("Loading plugin: "+this.getClass().getSimpleName()+" from "+ SignalIndustries.MOD_ID);
        TMB.registerPlugin(this);
    }
}
