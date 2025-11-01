package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineMultiOutput;
import sunsetsatellite.signalindustries.util.RecipeOutputStack;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class BonsaiPotRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachineMultiOutput>>{

    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachineMultiOutput> group) {
        group.register(
                "grow_oak",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Blocks.SAPLING_OAK, 1)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,100))
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new ItemStack(Blocks.LOG_OAK, 4)),
                                new RecipeOutputStack(new ItemStack(Blocks.SAPLING_OAK, 1)).randomYield(0,2),
                                new RecipeOutputStack(new ItemStack(Blocks.LEAVES_OAK, 1)).randomYield(0,4)
                        },
                        new RecipeProperties(800, 60, Tier.BASIC, false)
                )
        );
        group.register(
                "grow_ashen",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIBlocks.ashenTreeSapling, 1)),
                                //new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,100))
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new ItemStack(SIBlocks.eternalTreeLog, 4)),
                                new RecipeOutputStack(new ItemStack(SIBlocks.ashenTreeSapling, 1)),
                                new RecipeOutputStack(new ItemStack(SIBlocks.etherealLeaves, 1)).randomYield(0,4),
                                new RecipeOutputStack(new FluidStack(SIFluids.WORLD_RESIN,100))
                        },
                        new RecipeProperties(1800, 120, Tier.REINFORCED, false)
                )
        );
    }
}
