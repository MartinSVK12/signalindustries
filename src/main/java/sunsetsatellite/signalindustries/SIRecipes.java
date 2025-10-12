package sunsetsatellite.signalindustries;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.DataLoader;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryFurnace;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import sunsetsatellite.signalindustries.recipes.RecipeGroupSI;
import sunsetsatellite.signalindustries.recipes.RecipeNamespaceSI;
import sunsetsatellite.signalindustries.recipes.container.*;
import sunsetsatellite.signalindustries.recipes.container.waking.WakingAlloySmelterRecipes;
import sunsetsatellite.signalindustries.recipes.container.waking.WakingCrusherRecipes;
import sunsetsatellite.signalindustries.recipes.container.waking.WakingInfuserRecipes;
import sunsetsatellite.signalindustries.recipes.container.waking.WakingPlateFormerRecipes;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineFluid;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineMultiOutput;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineRandomOutput;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static sunsetsatellite.catalyst.Catalyst.listOf;
import static sunsetsatellite.signalindustries.SignalIndustries.LOGGER;
import static sunsetsatellite.signalindustries.SignalIndustries.MOD_ID;

public class SIRecipes implements RecipeEntrypoint {

    public static RecipeNamespaceSI SIGNAL_INDUSTRIES = new RecipeNamespaceSI();
    public static RecipeGroup<RecipeEntryCrafting<?,?>> WORKBENCH;
    public static RecipeGroup<RecipeEntryFurnace> FURNACE;
    public static RecipeGroupSI<RecipeEntryMachineFluid> EXTRACTOR;
    public static RecipeGroupSI<RecipeEntryMachine> CRUSHER;
    public static RecipeGroupSI<RecipeEntryMachine> WAKING_CRUSHER;
    public static RecipeGroupSI<RecipeEntryMachine> ALLOY_SMELTER;
    public static RecipeGroupSI<RecipeEntryMachine> WAKING_ALLOY_SMELTER;
    public static RecipeGroupSI<RecipeEntryMachine> PLATE_FORMER;
    public static RecipeGroupSI<RecipeEntryMachine> WAKING_PLATE_FORMER;
    public static RecipeGroupSI<RecipeEntryMachineFluid> PUMP;
    public static RecipeGroupSI<RecipeEntryMachine> STONEWORKS;
    public static RecipeGroupSI<RecipeEntryMachine> CRYSTAL_CUTTER;
    public static RecipeGroupSI<RecipeEntryMachine> CRYSTAL_CHAMBER;
    public static RecipeGroupSI<RecipeEntryMachine> INFUSER;
    public static RecipeGroupSI<RecipeEntryMachine> WAKING_INFUSER;
    public static RecipeGroupSI<RecipeEntryMachine> CENTRIFUGE;
    public static RecipeGroupSI<RecipeEntryMachineFluid> COLLECTOR;
    public static RecipeGroupSI<RecipeEntryMachine> INDUCTION_SMELTER;
    public static RecipeGroupSI<RecipeEntryMachineRandomOutput> LASER_DRILL;
    public static RecipeGroupSI<RecipeEntryMachineMultiOutput> GREENHOUSE;

    @Override
    public void onRecipesReady() {
        LOGGER.info("Loading SI recipes...");
        //resetGroups();
        //registerNamespaces();
        load();
    }

    @Override
    public void initNamespaces() {
        LOGGER.info("Loading SI recipe namespaces...");
        resetGroups();
        registerNamespaces();
    }

    //TODO:
    public void registerNamespaces(){
        SIGNAL_INDUSTRIES.register("workbench",WORKBENCH);
        SIGNAL_INDUSTRIES.register("furnace",FURNACE);
        SIGNAL_INDUSTRIES.register("extractor",EXTRACTOR);
        SIGNAL_INDUSTRIES.register("crusher",CRUSHER);
        SIGNAL_INDUSTRIES.register("alloy_smelter",ALLOY_SMELTER);
        SIGNAL_INDUSTRIES.register("plate_former",PLATE_FORMER);
        SIGNAL_INDUSTRIES.register("pump",PUMP);
        SIGNAL_INDUSTRIES.register("stoneworks",STONEWORKS);
        SIGNAL_INDUSTRIES.register("crystal_cutter",CRYSTAL_CUTTER);
        SIGNAL_INDUSTRIES.register("crystal_chamber",CRYSTAL_CHAMBER);
        SIGNAL_INDUSTRIES.register("infuser",INFUSER);
        SIGNAL_INDUSTRIES.register("centrifuge",CENTRIFUGE);
        SIGNAL_INDUSTRIES.register("collector",COLLECTOR);
        SIGNAL_INDUSTRIES.register("waking_crusher",WAKING_CRUSHER);
        SIGNAL_INDUSTRIES.register("waking_plate_former",WAKING_PLATE_FORMER);
        SIGNAL_INDUSTRIES.register("waking_alloy_smelter",WAKING_ALLOY_SMELTER);
        SIGNAL_INDUSTRIES.register("waking_infuser",WAKING_INFUSER);
        SIGNAL_INDUSTRIES.register("induction_smelter",INDUCTION_SMELTER);
        SIGNAL_INDUSTRIES.register("laser_drill",LASER_DRILL);
        SIGNAL_INDUSTRIES.register("greenhouse",GREENHOUSE);
        Registries.RECIPES.register("signalindustries",SIGNAL_INDUSTRIES);
    }

    public void resetGroups(){
        Registries.RECIPES.unregister("signalindustries");
        SIGNAL_INDUSTRIES = new RecipeNamespaceSI();
        WORKBENCH = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Blocks.WORKBENCH)));
        FURNACE = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Blocks.FURNACE_STONE_IDLE)));
        EXTRACTOR = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SIBlocks.prototypeExtractor), new ItemStack(SIBlocks.basicExtractor), new ItemStack(SIBlocks.reinforcedExtractor))));
        CRUSHER = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SIBlocks.prototypeCrusher),new ItemStack(SIBlocks.basicCrusher),new ItemStack(SIBlocks.reinforcedCrusher))));
        ALLOY_SMELTER = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SIBlocks.prototypeAlloySmelter),new ItemStack(SIBlocks.basicAlloySmelter),new ItemStack(SIBlocks.reinforcedAlloySmelter))));
        PLATE_FORMER = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SIBlocks.prototypePlateFormer),new ItemStack(SIBlocks.basicPlateFormer),new ItemStack(SIBlocks.reinforcedPlateFormer))));
        PUMP = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SIBlocks.prototypePump),new ItemStack(SIBlocks.basicPump),new ItemStack(SIBlocks.reinforcedPump))));
        STONEWORKS = new RecipeGroupSI<>(new RecipeSymbol(Collections.singletonList(new ItemStack(SIBlocks.basicStoneworks))));
        CRYSTAL_CUTTER = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SIBlocks.prototypeCrystalCutter),new ItemStack(SIBlocks.basicCrystalCutter),new ItemStack(SIBlocks.reinforcedCrystalCutter))));
        CRYSTAL_CHAMBER = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SIBlocks.basicCrystalChamber),new ItemStack(SIBlocks.reinforcedCrystalChamber))));
        INFUSER = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SIBlocks.basicInfuser),new ItemStack(SIBlocks.reinforcedInfuser))));
        CENTRIFUGE = new RecipeGroupSI<>(new RecipeSymbol(Collections.singletonList(new ItemStack(SIBlocks.reinforcedCentrifuge))));
        COLLECTOR = new RecipeGroupSI<>(new RecipeSymbol(Arrays.asList(new ItemStack(SIBlocks.basicCollector),new ItemStack(SIBlocks.reinforcedCollector))));
        WAKING_CRUSHER = new RecipeGroupSI<>(new RecipeSymbol(Collections.singletonList(new ItemStack(SIBlocks.wakingCrusher))));
        WAKING_PLATE_FORMER = new RecipeGroupSI<>(new RecipeSymbol(Collections.singletonList(new ItemStack(SIBlocks.wakingPlateFormer))));
        WAKING_ALLOY_SMELTER = new RecipeGroupSI<>(new RecipeSymbol(Collections.singletonList(new ItemStack(SIBlocks.wakingAlloySmelter))));
        WAKING_INFUSER = new RecipeGroupSI<>(new RecipeSymbol(Collections.singletonList(new ItemStack(SIBlocks.wakingInfuser))));
        INDUCTION_SMELTER = new RecipeGroupSI<>(new RecipeSymbol(Collections.singletonList(new ItemStack(SIBlocks.basicInductionSmelter))));
        LASER_DRILL = new RecipeGroupSI<>(new RecipeSymbol(Collections.singletonList(new ItemStack(SIBlocks.reinforcedLaserDrill))));
        GREENHOUSE = new RecipeGroupSI<>(new RecipeSymbol(Collections.singletonList(new ItemStack(SIBlocks.basicGreenhouse))));
    }

    public static void loadSpecial(){
        new InductionSmelterRecipes().addRecipes(SIRecipes.INDUCTION_SMELTER);
    }

    public void load(){
        Registries.RECIPE_TYPES.register("signalindustries:machine", RecipeEntryMachine.class);
        Registries.RECIPE_TYPES.register("signalindustries:machine/fluid", RecipeEntryMachineFluid.class);
        Registries.RECIPE_TYPES.register("signalindustries:machine/random", RecipeEntryMachineRandomOutput.class);
        Registries.RECIPE_TYPES.register("signalindustries:machine/multi", RecipeEntryMachineMultiOutput.class);
        List<ItemStack> romChipGroup = new ArrayList<>();
        romChipGroup.add(SIItems.romChipBoost.getDefaultStack());
        romChipGroup.add(SIItems.romChipProjectile.getDefaultStack());
        romChipGroup.add(SIItems.romChipShield.getDefaultStack());
        romChipGroup.add(SIItems.romChipScan.getDefaultStack());
        RecipeBuilder.addItemsToGroup(MOD_ID,"rom_chips",romChipGroup.toArray());
        RecipeBuilder.addItemsToGroup("common_plates","cobblestone",SIItems.cobblestonePlate);
        RecipeBuilder.addItemsToGroup("common_plates","stone",SIItems.stonePlate);
        RecipeBuilder.addItemsToGroup("common_plates","steel",SIItems.steelPlate);
        RecipeBuilder.addItemsToGroup("common_dusts","coal",SIItems.coalDust);
        RecipeBuilder.addItemsToGroup("common_dusts","iron",SIItems.ironDust);
        RecipeBuilder.addItemsToGroup("common_dusts","gold",SIItems.goldDust);
        RecipeBuilder.addItemsToGroup("common_dusts","nether_coal",SIItems.netherCoalDust);
        RecipeBuilder.addItemsToGroup("common_tiny_dusts","nether_coal",SIItems.tinyNetherCoalDust);
        RecipeBuilder.addItemsToGroup("minecraft","saplings",
                Blocks.SAPLING_OAK, Blocks.SAPLING_BIRCH, Blocks.SAPLING_CACAO,
                Blocks.SAPLING_CHERRY, Blocks.SAPLING_EUCALYPTUS, Blocks.SAPLING_THORN,
                Blocks.SAPLING_SHRUB, Blocks.SAPLING_PALM, Blocks.SAPLING_PINE, Blocks.SAPLING_OAK_RETRO);
        Registries.ITEM_GROUPS.register("minecraft:water", listOf(new ItemStack(Blocks.FLUID_WATER_FLOWING),new ItemStack(Blocks.FLUID_WATER_FLOWING)));
        Registries.ITEM_GROUPS.register("minecraft:lava", listOf(new ItemStack(Blocks.FLUID_LAVA_STILL),new ItemStack(Blocks.FLUID_LAVA_FLOWING)));
        new ExtractorRecipes().addRecipes(EXTRACTOR);
        new CrusherRecipes().addRecipes(CRUSHER);
        new AlloySmelterRecipes().addRecipes(ALLOY_SMELTER);
        new PlateFormerRecipes().addRecipes(PLATE_FORMER);
        new PumpRecipes().addRecipes(PUMP);
        new StoneworksRecipes().addRecipes(STONEWORKS);
        new CrystalCutterRecipes().addRecipes(CRYSTAL_CUTTER);
        new CrystalChamberRecipes().addRecipes(CRYSTAL_CHAMBER);
        new InfuserRecipes().addRecipes(INFUSER);
        new CentrifugeRecipes().addRecipes(CENTRIFUGE);
        new CollectorRecipes().addRecipes(COLLECTOR);
        new WakingCrusherRecipes().addRecipes(WAKING_CRUSHER);
        new WakingPlateFormerRecipes().addRecipes(WAKING_PLATE_FORMER);
        new WakingAlloySmelterRecipes().addRecipes(WAKING_ALLOY_SMELTER);
        new WakingInfuserRecipes().addRecipes(WAKING_INFUSER);
        new LaserDrillRecipes().addRecipes(LASER_DRILL);
        new GreenhouseRecipes().addRecipes(GREENHOUSE);
        DataLoader.loadRecipesFromFile("/assets/signalindustries/recipes/workbench.json");
        DataLoader.loadRecipesFromFile("/assets/signalindustries/recipes/workbench_prototype.json");
        DataLoader.loadRecipesFromFile("/assets/signalindustries/recipes/workbench_basic.json");
        DataLoader.loadRecipesFromFile("/assets/signalindustries/recipes/workbench_reinforced.json");
        DataLoader.loadRecipesFromFile("/assets/signalindustries/recipes/workbench_awakened.json");
        DataLoader.loadRecipesFromFile("/assets/signalindustries/recipes/furnace.json");
        LOGGER.info("{} recipes in {} groups.", SIGNAL_INDUSTRIES.getAllRecipes().size(), SIGNAL_INDUSTRIES.size());
    }

    public static <T extends RecipeEntryBase<?,?,?>> List<T> getAllRecipesOfType(Class<T> clazz){
        List<T> recipes = new ArrayList<>();
        for (RecipeEntryBase<?, ?, ?> recipe : Registries.RECIPES.getAllRecipes()) {
            if (clazz.isAssignableFrom(recipe.getClass())) {
                recipes.add(clazz.cast(recipe));
            }
        }
        return Collections.unmodifiableList(recipes);
    }

}
