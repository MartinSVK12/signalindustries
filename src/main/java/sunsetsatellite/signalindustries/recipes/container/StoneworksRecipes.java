package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class StoneworksRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>>{
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        group.register(
                "obsidian",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA,500)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,500)),
                        },
                        new ItemStack(Blocks.OBSIDIAN,1),
                        new RecipeProperties(100,20, 0, Tier.BASIC,false)
                )
        );
        group.register(
                "cobblestone",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA,0)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,0)),
                        },
                        new ItemStack(Blocks.COBBLE_STONE,1),
                        new RecipeProperties(10,20, 1, Tier.BASIC,false)
                )
        );
        group.register(
                "stone",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA,0)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,0)),
                        },
                        new ItemStack(Blocks.STONE,1),
                        new RecipeProperties(10,20, 2, Tier.BASIC,false)
                )
        );
        group.register(
                "basalt",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA,0)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,0)),
                        },
                        new ItemStack(Blocks.BASALT,1),
                        new RecipeProperties(10,20, 3, Tier.BASIC,false)
                )
        );
        group.register(
                "granite",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA,0)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,0)),
                        },
                        new ItemStack(Blocks.GRANITE,1),
                        new RecipeProperties(10,20, 4, Tier.BASIC,false)
                )
        );
        group.register(
                "limestone",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA,0)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,0)),
                        },
                        new ItemStack(Blocks.LIMESTONE,1),
                        new RecipeProperties(10,20, 5, Tier.BASIC,false)
                )
        );
        group.register(
                "permafrost",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA,0)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,1000))
                        },
                        new ItemStack(Blocks.PERMAFROST,1),
                        new RecipeProperties(100,20, 6, Tier.BASIC,false)
                )
        );
        group.register(
                "cobble_basalt",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA,0)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,0)),
                        },
                        new ItemStack(Blocks.COBBLE_BASALT,1),
                        new RecipeProperties(10,20, 7, Tier.BASIC,false)
                )
        );
        group.register(
                "cobble_granite",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA,0)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,0)),
                        },
                        new ItemStack(Blocks.COBBLE_GRANITE,1),
                        new RecipeProperties(10,20, 8, Tier.BASIC,false)
                )
        );
        group.register(
                "cobble_limestone",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA,0)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,0)),
                        },
                        new ItemStack(Blocks.COBBLE_LIMESTONE,1),
                        new RecipeProperties(10,20, 9, Tier.BASIC,false)
                )
        );
        group.register(
                "cobble_permafrost",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA,0)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,1000))
                        },
                        new ItemStack(Blocks.COBBLE_PERMAFROST,1),
                        new RecipeProperties(100,20, 10, Tier.BASIC,false)
                )
        );
        group.register(
                "marble",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA,0)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,0)),
                        },
                        new ItemStack(Blocks.MARBLE,1),
                        new RecipeProperties(10,20, 11, Tier.BASIC,false)
                )
        );
        group.register(
                "slate",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA,0)),
                                new RecipeExtendedSymbol(new FluidStack(Fluids.WATER,0))
                        },
                        new ItemStack(Blocks.SLATE,1),
                        new RecipeProperties(10,20, 12, Tier.BASIC,false)
                )
        );
    }
}
