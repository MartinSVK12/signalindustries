package sunsetsatellite.signalindustries.recipes.container.waking;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.Fluids;
import sunsetsatellite.catalyst.fluids.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.recipes.container.MachineRecipesBase;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class WakingInfuserRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>> {
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
                        new ItemStack(SIBlocks.glowingObsidian, 2),
                        new RecipeProperties(100, 80, Tier.REINFORCED, false)
                )
        );
        group.register(
                "saturated_crystal_alloy",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                //new RecipeExtendedSymbol(new ItemStack(Block.fluidLavaFlowing,1000)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.crystalAlloyIngot, 1)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.saturatedSignalumCrystalDust, 1))
                        },
                        new ItemStack(SIItems.saturatedSignalumAlloyIngot, 2),
                        new RecipeProperties(100, 80, Tier.REINFORCED, false)
                )
        );
        group.register(
                "awakened_alloy",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                //new RecipeExtendedSymbol(new ItemStack(Block.fluidLavaFlowing,1000)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.saturatedSignalumAlloyIngot, 4)),
                                new RecipeExtendedSymbol(new ItemStack(SIItems.awakenedSignalumCrystalDust, 1))
                        },
                        new ItemStack(SIItems.awakenedAlloyIngot, 1),
                        new RecipeProperties(600, 120, Tier.REINFORCED, false)
                )
        );
    }
}
