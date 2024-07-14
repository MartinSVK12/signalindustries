package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class AlloySmelterRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>>{
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        group.register(
                "steel_ingot",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.ingotIron)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.tinyNetherCoalDust))
                        },
                        new ItemStack(Item.ingotSteel,1),
                        new RecipeProperties(200,40,Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "crystal_alloy_ingot",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.ingotSteel)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.emptySignalumCrystalDust))
                        },
                        new ItemStack(SIItems.crystalAlloyIngot,1),
                        new RecipeProperties(200,40,Tier.PROTOTYPE,true)
                )
        );
        group.register(
                "crystal_alloy_ingot_2",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.ingotSteel)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.emptySignalumCrystalDust))
                        },
                        new ItemStack(SIItems.crystalAlloyIngot,2),
                        new RecipeProperties(200,40,Tier.BASIC,true)
                )
        );
        group.register(
                "reinforced_crystal_alloy_ingot",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.crystalAlloyIngot)),
                                new RecipeExtendedSymbol(new ItemStack(SIBlocks.glowingObsidian,2))
                        },
                        new ItemStack(SIItems.reinforcedCrystalAlloyIngot,1),
                        new RecipeProperties(100,80,Tier.BASIC,false)
                )
        );
        group.register(
                "condensed_milk_can",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.bucketMilk)),
                                new RecipeExtendedSymbol(new ItemStack(Item.dustSugar,8))
                        },
                        new ItemStack(SIItems.condensedMilkCan,1),
                        new RecipeProperties(200,40,Tier.PROTOTYPE,false).setConsumeContainers()
                )
        );
        group.register(
                "caramel_bucket",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.condensedMilkCan)),
                                new RecipeExtendedSymbol(new ItemStack(Item.dustSugar,4))
                        },
                        new ItemStack(SIItems.bucketCaramel,1),
                        new RecipeProperties(200,40,Tier.PROTOTYPE,false).setConsumeContainers()
                )
        );
        group.register(
                "void_alloy_ingot",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.reinforcedCrystalAlloyIngot,2)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.realityString,8))
                        },
                        new ItemStack(SIItems.voidAlloyIngot,1),
                        new RecipeProperties(300,80,Tier.REINFORCED,false)
                )
        );
    }
}
