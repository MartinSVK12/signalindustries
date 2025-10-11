package sunsetsatellite.signalindustries.util;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;

public class RecipeOutputStack {

    public ItemStack stack;
    public FluidStack fluid;
    public float chance;

    public boolean randomAmount;
    public int amountMin;
    public int amountMax;

    public RecipeOutputStack(ItemStack stack){
        this.stack = stack;
        this.fluid = null;
        this.chance = 1;
        this.randomAmount = false;
    }

    public RecipeOutputStack(FluidStack fluid){
        this.stack = null;
        this.fluid = fluid;
        this.chance = 1;
        this.randomAmount = false;
    }

    public RecipeOutputStack(ItemStack stack, float chance){
        this.stack = stack;
        this.fluid = null;
        this.chance = chance;
        this.randomAmount = false;
    }

    public RecipeOutputStack(FluidStack fluid, float chance){
        this.stack = null;
        this.fluid = fluid;
        this.chance = chance;
        this.randomAmount = false;
    }

    public RecipeOutputStack randomYield(int min, int max){
        this.randomAmount = true;
        this.amountMin = min;
        this.amountMax = max;
        return this;
    }

    public boolean isItem() {
        return stack != null;
    }

    public boolean isFluid() {
        return fluid != null;
    }
}
