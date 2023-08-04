package sunsetsatellite.signalindustries.inventories;


import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.api.ItemInventoryFluid;
import sunsetsatellite.signalindustries.util.NBTHelper;

public class InventoryAbilityModule extends ItemInventoryFluid {
    public InventoryAbilityModule(ItemStack item) {
        super(item);
        fluidContents = new FluidStack[0];
        fluidCapacity = new int[0];
        contents = new ItemStack[9];
        NBTHelper.loadInvFromNBT(item,this,9,0);
    }
}
