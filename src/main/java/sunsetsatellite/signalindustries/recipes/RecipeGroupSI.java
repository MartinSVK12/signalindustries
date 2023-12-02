package sunsetsatellite.signalindustries.recipes;

import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.recipes.entry.RecipeEntrySI;
import sunsetsatellite.signalindustries.util.RecipeExtendedSymbol;
import sunsetsatellite.signalindustries.util.RecipeProperties;
import sunsetsatellite.signalindustries.util.Tier;

public class RecipeGroupSI<T extends RecipeEntrySI<?,?, RecipeProperties>> extends RecipeGroup<T> {
    public RecipeGroupSI(RecipeSymbol machine) {
        super(machine);
    }

    public FluidStack findFluidOutput(ItemStack stack){
        for (T recipe : this.getAllRecipes()) {
            RecipeExtendedSymbol symbol = new RecipeExtendedSymbol(new ItemStack(stack.itemID, 1, stack.getMetadata()));
            if (recipe.matches(new RecipeExtendedSymbol[]{symbol})) {
                return ((FluidStack)recipe.getOutput()).copy();
            }
        }
        return null;
    }
    public FluidStack findFluidOutput(ItemStack stack, Tier tier){
        for (T recipe : this.getAllRecipes()) {
            RecipeExtendedSymbol symbol = new RecipeExtendedSymbol(new ItemStack(stack.itemID, 1, stack.getMetadata()));
            if (recipe.matches(new RecipeExtendedSymbol[]{symbol}) && recipe.getData().isCorrectTier(tier)) {
                return ((FluidStack)recipe.getOutput()).copy();
            }
        }
        return null;
    }

    public ItemStack findOutput(ItemStack stack){
        for (T recipe : this.getAllRecipes()) {
            RecipeExtendedSymbol symbol = new RecipeExtendedSymbol(new ItemStack(stack.itemID, 1, stack.getMetadata()));
            if (recipe.matches(new RecipeExtendedSymbol[]{symbol})) {
                return ((ItemStack)recipe.getOutput()).copy();
            }
        }
        return null;
    }
    public ItemStack findOutput(ItemStack stack, Tier tier){
        for (T recipe : this.getAllRecipes()) {
            RecipeExtendedSymbol symbol = new RecipeExtendedSymbol(new ItemStack(stack.itemID, 1, stack.getMetadata()));
            if (recipe.matches(new RecipeExtendedSymbol[]{symbol}) && recipe.getData().isCorrectTier(tier)) {
                return ((ItemStack)recipe.getOutput()).copy();
            }
        }
        return null;
    }
}
