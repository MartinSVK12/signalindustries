package sunsetsatellite.signalindustries.recipes.container.waking;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.recipes.container.MachineRecipesBase;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class WakingCrusherRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>> {
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        group.register(
                "cobble_to_gravel",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol("minecraft:cobblestones")
                        },
                        new ItemStack(Blocks.GRAVEL,1),
                        new RecipeProperties(100,40, Tier.REINFORCED,false)
                )
        );
        group.register(
                "gravel_to_sand",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Blocks.GRAVEL,1))
                        },
                        new ItemStack(Blocks.SAND,1),
                        new RecipeProperties(100,40, Tier.REINFORCED,false)
                )
        );
        group.register(
                "prototype_raw_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.rawSignalumCrystal))
                        },
                        new ItemStack(SIItems.saturatedSignalumCrystalDust,1),
                        new RecipeProperties(100,40, Tier.REINFORCED,false)
                )
        );
        group.register(
                "prototype_coal_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Items.COAL))
                        },
                        new ItemStack(SIItems.coalDust,1),
                        new RecipeProperties(100,40, Tier.REINFORCED,false)
                )
        );
        group.register(
                "basic_raw_iron_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Items.ORE_RAW_IRON))
                        },
                        new ItemStack(SIItems.ironDust,2),
                        new RecipeProperties(50,40, Tier.REINFORCED,false)
                )
        );
        group.register(
                "basic_raw_gold_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Items.ORE_RAW_GOLD))
                        },
                        new ItemStack(SIItems.goldDust,2),
                        new RecipeProperties(50,40, Tier.REINFORCED,false)
                )
        );
        group.register(
                "basic_empty_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.signalumCrystalEmpty))
                        },
                        new ItemStack(SIItems.emptySignalumCrystalDust,4),
                        new RecipeProperties(100,40, Tier.REINFORCED,false)
                )
        );
        group.register(
                "basic_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.signalumCrystal))
                        },
                        new ItemStack(SIItems.saturatedSignalumCrystalDust,4),
                        new RecipeProperties(100,40, Tier.REINFORCED,false)
                )
        );
        group.register(
                "basic_nether_coal_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Items.NETHERCOAL))
                        },
                        new ItemStack(SIItems.netherCoalDust,1),
                        new RecipeProperties(50,80, Tier.REINFORCED,false)
                )
        );
        group.register(
                "basic_tiny_nether_coal_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.netherCoalDust))
                        },
                        new ItemStack(SIItems.tinyNetherCoalDust,9),
                        new RecipeProperties(50,80, Tier.REINFORCED,false)
                )
        );
        group.register(
                "string_of_reality",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIBlocks.rootedFabric))
                        },
                        new ItemStack(SIItems.realityString,2),
                        new RecipeProperties(200,80, Tier.REINFORCED,false).setChance(0.75f)
                )
        );
        group.register(
                "awakened_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.awakenedSignalumCrystal))
                        },
                        new ItemStack(SIItems.awakenedSignalumCrystalDust,2),
                        new RecipeProperties(600,120, Tier.REINFORCED,false)
                )
        );
    }
}
