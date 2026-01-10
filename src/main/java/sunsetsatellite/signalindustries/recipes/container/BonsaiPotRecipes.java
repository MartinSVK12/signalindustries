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
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineMultiOutput;
import sunsetsatellite.catalyst.fluids.util.RecipeOutputStack;
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
                "grow_birch",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Blocks.SAPLING_BIRCH, 1)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,100))
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new ItemStack(Blocks.LOG_BIRCH, 4)),
                                new RecipeOutputStack(new ItemStack(Blocks.SAPLING_BIRCH, 1)).randomYield(0,2),
                                new RecipeOutputStack(new ItemStack(Blocks.LEAVES_BIRCH, 1)).randomYield(0,4)
                        },
                        new RecipeProperties(800, 60, Tier.BASIC, false)
                )
        );
        group.register(
                "grow_pine",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Blocks.SAPLING_PINE, 1)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,100))
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new ItemStack(Blocks.LOG_PINE, 4)),
                                new RecipeOutputStack(new ItemStack(Blocks.SAPLING_PINE, 1)).randomYield(0,2),
                                new RecipeOutputStack(new ItemStack(Blocks.LEAVES_PINE, 1)).randomYield(0,4)
                        },
                        new RecipeProperties(800, 60, Tier.BASIC, false)
                )
        );
        group.register(
                "grow_cherry",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Blocks.SAPLING_BIRCH, 1)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,100))
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new ItemStack(Blocks.LOG_CHERRY, 4)),
                                new RecipeOutputStack(new ItemStack(Blocks.SAPLING_CHERRY, 1)).randomYield(0,2),
                                new RecipeOutputStack(new ItemStack(Blocks.LEAVES_CHERRY, 1)).randomYield(0,4)
                        },
                        new RecipeProperties(800, 60, Tier.BASIC, false)
                )
        );
        group.register(
                "grow_eucalyptus",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Blocks.SAPLING_EUCALYPTUS, 1)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,100))
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new ItemStack(Blocks.LOG_EUCALYPTUS, 4)),
                                new RecipeOutputStack(new ItemStack(Blocks.SAPLING_EUCALYPTUS, 1)).randomYield(0,2),
                                new RecipeOutputStack(new ItemStack(Blocks.LEAVES_EUCALYPTUS, 1)).randomYield(0,4)
                        },
                        new RecipeProperties(800, 60, Tier.BASIC, false)
                )
        );
        group.register(
                "grow_shrub",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Blocks.SAPLING_SHRUB, 1)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,100))
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new ItemStack(Blocks.LOG_OAK, 4)),
                                new RecipeOutputStack(new ItemStack(Blocks.SAPLING_SHRUB, 1)).randomYield(0,2),
                                new RecipeOutputStack(new ItemStack(Blocks.LEAVES_SHRUB, 1)).randomYield(0,4)
                        },
                        new RecipeProperties(800, 60, Tier.BASIC, false)
                )
        );
        group.register(
                "grow_cacao",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Blocks.SAPLING_CACAO, 1)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,100))
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new ItemStack(Blocks.LOG_OAK_MOSSY, 4)),
                                new RecipeOutputStack(new ItemStack(Blocks.SAPLING_CACAO, 1)).randomYield(0,2),
                                new RecipeOutputStack(new ItemStack(Blocks.LEAVES_CACAO, 1)).randomYield(0,4)
                        },
                        new RecipeProperties(800, 60, Tier.BASIC, false)
                )
        );
        group.register(
                "grow_thorn",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Blocks.SAPLING_THORN, 1)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,100))
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new ItemStack(Blocks.LOG_THORN, 4)),
                                new RecipeOutputStack(new ItemStack(Blocks.SAPLING_THORN, 1)).randomYield(0,2),
                                new RecipeOutputStack(new ItemStack(Blocks.LEAVES_THORN, 1)).randomYield(0,4)
                        },
                        new RecipeProperties(800, 60, Tier.BASIC, false)
                )
        );
        group.register(
                "grow_palm",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Blocks.SAPLING_PALM, 1)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,100))
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new ItemStack(Blocks.LOG_PALM, 4)),
                                new RecipeOutputStack(new ItemStack(Blocks.SAPLING_PALM, 1)).randomYield(0,2),
                                new RecipeOutputStack(new ItemStack(Blocks.LEAVES_PALM, 1)).randomYield(0,4)
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
        group.register(
                "grow_flower_red",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Blocks.FLOWER_RED, 1)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,100))
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new ItemStack(Blocks.FLOWER_RED, 8)),
                        },
                        new RecipeProperties(400, 60, Tier.BASIC, false)
                )
        );
        group.register(
                "grow_flower_yellow",
                new RecipeEntryMachineMultiOutput(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(Blocks.FLOWER_YELLOW, 1)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,100))
                        },
                        new RecipeOutputStack[]{
                                new RecipeOutputStack(new ItemStack(Blocks.FLOWER_YELLOW, 8)),
                        },
                        new RecipeProperties(400, 60, Tier.BASIC, false)
                )
        );
    }
}
