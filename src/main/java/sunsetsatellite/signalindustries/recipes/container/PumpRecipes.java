package sunsetsatellite.signalindustries.recipes.container;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntryMachineFluid;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class PumpRecipes implements MachineRecipesBase<RecipeGroup<RecipeEntryMachineFluid>> {
    @Override
    public void addRecipes(RecipeGroup<RecipeEntryMachineFluid> group) {
        group.register("water",new RecipeEntryMachineFluid(
                new RecipeExtendedSymbol[]{
                        new RecipeExtendedSymbol("minecraft:water")
                },
                new FluidStack((BlockFluid) Block.fluidWaterFlowing,1000),
                new RecipeProperties(600,10, Tier.PROTOTYPE,false)
        ));
        group.register("lava",new RecipeEntryMachineFluid(
                new RecipeExtendedSymbol[]{
                        new RecipeExtendedSymbol("minecraft:lava")
                },
                new FluidStack((BlockFluid) Block.fluidLavaFlowing,1000),
                new RecipeProperties(600,30, Tier.BASIC,false)
        ));
    }
}
