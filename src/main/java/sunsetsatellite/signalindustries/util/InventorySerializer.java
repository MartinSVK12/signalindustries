package sunsetsatellite.signalindustries.util;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.FluidStack;

public class InventorySerializer {

    private static boolean preventSaving = false;

    public static void saveInvToNBT(ItemStack container, Container inv) {
        if (preventSaving) return;
        CompoundTag itemData = container.getData().getCompound("inventory");
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack item = inv.getItem(i);
            CompoundTag itemNBT = new CompoundTag();
            if (item != null) {
                item.writeToNBT(itemNBT);
                itemData.putCompound(String.valueOf(i), itemNBT);
            } else {
                itemData.getValue().remove(String.valueOf(i));
            }
        }
        container.getData().putCompound("inventory", itemData);
        if (inv instanceof IFluidInventory) {
            IFluidInventory fluidInv = (IFluidInventory) inv;
            CompoundTag fluidData = container.getData().getCompound("fluidInventory");
            for (int i = 0; i < fluidInv.getFluidInventorySize(); i++) {
                FluidStack fluid = ((IFluidInventory) inv).getFluidInSlot(i);
                CompoundTag fluidNBT = new CompoundTag();
                if (fluid != null) {
                    fluid.writeToNBT(fluidNBT);
                    fluidData.putCompound(String.valueOf(i), fluidNBT);
                } else {
                    fluidData.getValue().remove(String.valueOf(i));
                }
            }
            container.getData().putCompound("fluidInventory", fluidData);
        }
    }

    public static void loadInvFromNBT(ItemStack container, Container inv, int amount, int fluidAmount) {
        preventSaving = true;
        CompoundTag itemNBT = container.getData().getCompound("inventory");
        CompoundTag fluidNBT = container.getData().getCompound("fluidInventory");
        for (int i = 0; i < amount; i++) {
            if (itemNBT.containsKey(String.valueOf(i))) {
                ItemStack item = ItemStack.readItemStackFromNbt(itemNBT.getCompound(String.valueOf(i)));
                inv.setItem(i, item);
            }
        }
        for (int i = 0; i < fluidAmount; i++) {
            if (inv instanceof IFluidInventory) {
                IFluidInventory fluidInv = (IFluidInventory) inv;
                CompoundTag tag = fluidNBT.getCompound(String.valueOf(i));
                if (tag != null && tag.containsKey("fluid")) {
                    FluidStack fluid = new FluidStack(tag);
                    fluidInv.setFluidInSlot(i, fluid);
                } else {
                    fluidInv.setFluidInSlot(i, null);
                }
            }
        }
        preventSaving = false;
    }
}
