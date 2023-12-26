package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachine;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class InfuserRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachine>>{
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachine> group) {
        group.register(
                "glowing_obsidian",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                new RecipeExtendedSymbol(new FluidStack((BlockFluid) Block.fluidLavaFlowing,1000)),
                                new RecipeExtendedSymbol(new ItemStack(Block.obsidian,2)),
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.netherCoalDust,1))
                        },
                        new ItemStack(SignalIndustries.glowingObsidian,1),
                        new RecipeProperties(400,80,Tier.BASIC,false)
                )
        );
        group.register(
                "saturated_crystal_alloy",
                new RecipeEntryMachine(
                        new RecipeExtendedSymbol[]{
                                null,//new RecipeExtendedSymbol(new ItemStack(Block.fluidLavaFlowing,1000)),
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.crystalAlloyIngot,1)),
                                new RecipeExtendedSymbol(new ItemStack(SignalIndustries.saturatedSignalumCrystalDust,4))
                        },
                        new ItemStack(SignalIndustries.saturatedSignalumAlloyIngot,1),
                        new RecipeProperties(400,80,Tier.BASIC,false)
                )
        );
    }
}
