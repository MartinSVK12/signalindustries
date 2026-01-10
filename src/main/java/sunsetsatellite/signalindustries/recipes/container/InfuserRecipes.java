package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class InfuserRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>> {
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        group.register(
                "glowing_obsidian",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(Fluids.LAVA, 1000)),
                                new RecipeExtendedSymbol(new ItemStack(Blocks.OBSIDIAN, 1)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.netherCoalDust, 1))
                        },
                        new ItemStack(SIBlocks.glowingObsidian, 1),
                        new RecipeProperties(400, 80, Tier.BASIC, false)
                )
        );
        group.register(
                "saturated_crystal_alloy",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.crystalAlloyIngot, 1)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.saturatedSignalumCrystalDust, 2))
                        },
                        new ItemStack(SIItems.saturatedSignalumAlloyIngot, 1),
                        new RecipeProperties(400, 80, Tier.BASIC, true)
                )
        );
        group.register(
                "saturated_crystal_alloy_reinforced",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new ItemStack(SIItems.crystalAlloyIngot, 1)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.saturatedSignalumCrystalDust, 1))
                        },
                        new ItemStack(SIItems.saturatedSignalumAlloyIngot, 1),
                        new RecipeProperties(400, 80, Tier.REINFORCED, false)
                )
        );
        group.register(
                "reborn_ashen_tree_sapling",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack(SIFluids.WORLD_RESIN, 8000)),
                                new RecipeExtendedSymbol("minecraft:saplings"),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.awakenedSignalumFragment, 1))
                        },
                        new ItemStack(SIBlocks.ashenTreeSapling, 1),
                        new RecipeProperties(800, 160, Tier.REINFORCED, false)
                )
        );
    }
}
