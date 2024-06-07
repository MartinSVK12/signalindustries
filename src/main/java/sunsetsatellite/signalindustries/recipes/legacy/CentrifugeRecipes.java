package sunsetsatellite.signalindustries.recipes.legacy;

import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;

import java.util.HashMap;
import java.util.Map;
@Deprecated
public class CentrifugeRecipes extends MachineRecipesBase<FluidStack[], ItemStack> {
    public static final CentrifugeRecipes instance = new CentrifugeRecipes();

    protected CentrifugeRecipes(){
        addRecipe(new FluidStack[]{new FluidStack((BlockFluid) SIBlocks.burntSignalumFlowing,250),new FluidStack((BlockFluid) SIBlocks.burntSignalumFlowing,250),new FluidStack((BlockFluid) SIBlocks.burntSignalumFlowing,250),new FluidStack((BlockFluid) SIBlocks.burntSignalumFlowing,250)},new ItemStack(SIItems.awakenedSignalumFragment,1));
    }

    @Override
    public HashMap<FluidStack[], ItemStack> getRecipeList() {
        return this.recipeList;
    }

    @Override
    public void addRecipe(FluidStack[] input, ItemStack output) {
        recipeList.put(input,output);
    }

    @Override
    public ItemStack getResult(FluidStack[] input) {
        for (Map.Entry<FluidStack[], ItemStack> entry : recipeList.entrySet()) {
            if(input.length != entry.getKey().length) return null;
            int s = 0;
            for (int i = 0; i < input.length; i++) {
                if (input[i] == null && entry.getKey()[i] != null || input[i] != null && entry.getKey()[i] == null) {
                    s = 0;
                    continue;
                }
                if(input[i] != null && entry.getKey()[i] != null){
                    if(!(input[i].isFluidEqual(entry.getKey()[i]))){
                        s = 0;
                    } else {
                        s++;
                    }
                }
            }
            if(s == input.length){
                return entry.getValue().copy();
            }
        }
        return null;
    }

    public Map.Entry<FluidStack[], ItemStack> getValidRecipe(FluidStack[] input) {
        for (Map.Entry<FluidStack[], ItemStack> entry : recipeList.entrySet()) {
            if(input.length != entry.getKey().length) return null;
            int s = 0;
            for (int i = 0; i < input.length; i++) {
                if(!(input[i].isFluidEqual(entry.getKey()[i]))){
                    s = 0;
                } else {
                    s++;
                }
            }
            if(s == input.length){
                return entry;
            }
        }
        return null;
    }
}
