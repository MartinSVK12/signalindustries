package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class CrusherRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>>{
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        group.register(
                "cobble_to_gravel",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol("minecraft:cobblestones")
                        },
                        new ItemStack(Block.gravel,1),
                        new RecipeProperties(200,40, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "gravel_to_sand",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Block.gravel,1))
                        },
                        new ItemStack(Block.sand,1),
                        new RecipeProperties(200,40, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "prototype_raw_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.rawSignalumCrystal))
                        },
                        new ItemStack(SIItems.saturatedSignalumCrystalDust,1),
                        new RecipeProperties(200,40, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "prototype_coal_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.coal))
                        },
                        new ItemStack(SIItems.coalDust,1),
                        new RecipeProperties(200,40, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "prototype_raw_iron_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.oreRawIron))
                        },
                        new ItemStack(SIItems.ironDust,1),
                        new RecipeProperties(100,40, Tier.PROTOTYPE,true)
                )
        );
        group.register(
                "prototype_raw_gold_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.oreRawGold))
                        },
                        new ItemStack(SIItems.goldDust,1),
                        new RecipeProperties(100,40, Tier.PROTOTYPE,true)
                )
        );
        group.register(
                "basic_raw_iron_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.oreRawIron))
                        },
                        new ItemStack(SIItems.ironDust,2),
                        new RecipeProperties(100,40, Tier.BASIC,false)
                )
        );
        group.register(
                "basic_raw_gold_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.oreRawGold))
                        },
                        new ItemStack(SIItems.goldDust,2),
                        new RecipeProperties(100,40, Tier.BASIC,false)
                )
        );
        group.register(
                "prototype_empty_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.signalumCrystalEmpty))
                        },
                        new ItemStack(SIItems.emptySignalumCrystalDust,2),
                        new RecipeProperties(200,40, Tier.PROTOTYPE,true)
                )
        );
        group.register(
                "basic_empty_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.signalumCrystalEmpty))
                        },
                        new ItemStack(SIItems.emptySignalumCrystalDust,4),
                        new RecipeProperties(200,40, Tier.BASIC,false)
                )
        );
        group.register(
                "prototype_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.signalumCrystal))
                        },
                        new ItemStack(SIItems.saturatedSignalumCrystalDust,2),
                        new RecipeProperties(200,40, Tier.PROTOTYPE,true)
                )
        );
        group.register(
                "basic_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.signalumCrystal))
                        },
                        new ItemStack(SIItems.saturatedSignalumCrystalDust,4),
                        new RecipeProperties(200,40, Tier.BASIC,false)
                )
        );
        group.register(
                "basic_nether_coal_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.nethercoal))
                        },
                        new ItemStack(SIItems.netherCoalDust,1),
                        new RecipeProperties(100,80, Tier.BASIC,false)
                )
        );
        group.register(
                "basic_tiny_nether_coal_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.netherCoalDust))
                        },
                        new ItemStack(SIItems.tinyNetherCoalDust,9),
                        new RecipeProperties(100,80, Tier.BASIC,false)
                )
        );
        group.register(
                "string_of_reality",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIBlocks.rootedFabric))
                        },
                        new ItemStack(SIItems.realityString,2),
                        new RecipeProperties(300,160, Tier.REINFORCED,false).setChance(0.5f)
                )
        );
    }
}
