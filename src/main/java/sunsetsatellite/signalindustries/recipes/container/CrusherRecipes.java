package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

import static sunsetsatellite.signalindustries.SignalIndustries.*;

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
                                new RecipeExtendedSymbol(new ItemStack(rawSignalumCrystal))
                        },
                        new ItemStack(saturatedSignalumCrystalDust,1),
                        new RecipeProperties(200,40, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "prototype_coal_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.coal))
                        },
                        new ItemStack(coalDust,1),
                        new RecipeProperties(200,40, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "prototype_empty_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(signalumCrystalEmpty))
                        },
                        new ItemStack(emptySignalumCrystalDust,2),
                        new RecipeProperties(200,40, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "prototype_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(signalumCrystal))
                        },
                        new ItemStack(saturatedSignalumCrystalDust,2),
                        new RecipeProperties(200,40, Tier.PROTOTYPE,false)
                )
        );
        group.register(
                "basic_nether_coal_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Item.nethercoal))
                        },
                        new ItemStack(netherCoalDust,1),
                        new RecipeProperties(100,80, Tier.BASIC,false)
                )
        );
        group.register(
                "basic_tiny_nether_coal_dust",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(netherCoalDust))
                        },
                        new ItemStack(tinyNetherCoalDust,9),
                        new RecipeProperties(100,80, Tier.BASIC,false)
                )
        );
        group.register(
                "string_of_reality",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(rootedFabric))
                        },
                        new ItemStack(realityString,2),
                        new RecipeProperties(300,160, Tier.REINFORCED,false).setChance(0.5f)
                )
        );
    }
}
