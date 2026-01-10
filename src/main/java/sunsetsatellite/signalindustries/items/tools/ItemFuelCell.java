package sunsetsatellite.signalindustries.items.tools;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;

import java.util.Collections;
import java.util.List;

public class ItemFuelCell extends Item implements IItemFluidContainer, ICustomDescription {


    public ItemFuelCell(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return 4000;
    }

    @Override
    public int getRemainingCapacity(ItemStack stack) {
        return 4000 - (stack.getData().getInteger("fuel") + stack.getData().getInteger("depleted"));
    }

    @Override
    public int getFluidAmount(ItemStack stack) {
        return stack.getData().getInteger("fuel");
    }

    @Override
    public boolean canFill(ItemStack stack) {
        return getRemainingCapacity(stack) > 0;
    }

    @Override
    public boolean canDrain(ItemStack stack) {
        return getCapacity(stack) > getRemainingCapacity(stack);
    }

    @Override
    public FluidStack getCurrentFluid(ItemStack stack) {
        return new FluidStack(SIFluids.ENERGY, getCapacity(stack));
    }

    @Override
    public void setCurrentFluid(FluidStack fluidStack, ItemStack stack) {

    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack) {
        if (fluidStack == null) {
            return null;
        }
        if (fluidStack.fluid == SIFluids.ENERGY) {
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("fuel");
            int amount = fluidStack.amount;
            ItemStack cell = new ItemStack(SIItems.fuelCell, 1);
            if (remaining == 0) {
                return null;
            }
            if (amount > remaining) {
                fluidStack.amount -= remaining;
                CompoundTag data = new CompoundTag();
                data.putInt("fuel", getCapacity(stack));
                cell.setData(data);
                return cell;
            } else {
                CompoundTag data = new CompoundTag();
                data.putInt("fuel", saturation + amount);
                cell.setData(data);
                return cell;
            }
        }
        return null;
    }


    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, IFluidInventory tile) {
        return fill(fluidStack, stack);
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, IFluidInventory tile, int maxAmount) {
        if (fluidStack == null) {
            return null;
        }
        if (fluidStack.fluid == SIFluids.ENERGY) {
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("fuel");
            int amount = Math.min(fluidStack.amount, maxAmount);
            ItemStack cell = new ItemStack(SIItems.fuelCell, 1);
            if (remaining == 0) return null;
            int result = Math.min(amount, remaining);
            if (result == 0) return null;
            fluidStack.amount -= result;
            CompoundTag data = new CompoundTag();
            data.putInt("fuel", saturation + result);
            cell.setData(data);
            return cell;
        }
        return null;
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, IItemFluidContainer inv) {
        return fill(fluidStack, stack);
    }

    @Override
    public void drain(ItemStack stack, int slot, IFluidInventory tile) {
        int saturation = stack.getData().getInteger("depleted");
        int capacity = tile.getFluidCapacityForSlot(slot);
        if (saturation == 0) {
            return;
        }
        FluidStack fluidStack = tile.getFluidInSlot(slot);
        if (fluidStack != null) {
            int amount = fluidStack.amount;
            if (amount + saturation > capacity) {
                int remainder = (amount + saturation) - capacity;
                fluidStack.amount = capacity;
                stack.getData().putInt("depleted", remainder);
            } else {
                fluidStack.amount += saturation;
                stack.getData().putInt("depleted", 0);
            }
        } else {
            if (saturation > capacity) {
                int remainder = saturation - capacity;
                FluidStack fluid = new FluidStack(SIFluids.BURNT_ENERGY, capacity);
                tile.setFluidInSlot(slot, fluid);
                stack.getData().putInt("depleted", remainder);
            } else {
                FluidStack fluid = new FluidStack(SIFluids.BURNT_ENERGY, saturation);
                tile.setFluidInSlot(slot, fluid);
                stack.getData().putInt("depleted", 0);
            }
        }
    }

    @Override
    public void drain(ItemStack stack, ItemStack other, int slot, IItemFluidContainer inv) {
        int saturation = stack.getData().getInteger("depleted");
        int capacity = inv.getCapacity(other);
        if (saturation == 0) {
            return;
        }
        FluidStack fluidStack = inv.getCurrentFluid(other);
        if (fluidStack != null) {
            int amount = fluidStack.amount;
            if (amount + saturation > capacity) {
                int remainder = (amount + saturation) - capacity;
                fluidStack.amount = capacity;
                stack.getData().putInt("depleted", remainder);
            } else {
                fluidStack.amount += saturation;
                stack.getData().putInt("depleted", 0);
            }
        } else {
            if (saturation > capacity) {
                int remainder = saturation - capacity;
                FluidStack fluid = new FluidStack(SIFluids.BURNT_ENERGY, capacity);
                inv.setCurrentFluid(fluid, other);
                stack.getData().putInt("depleted", remainder);
            } else {
                FluidStack fluid = new FluidStack(SIFluids.BURNT_ENERGY, saturation);
                inv.setCurrentFluid(fluid, other);
                stack.getData().putInt("depleted", 0);
            }
        }
    }

    @Override
    public FluidStack drain(ItemStack stack, int amount) {
        return null;
    }

    @Override
    public List<Fluid> getAllowedFluids(ItemStack stack) {
        return Collections.singletonList(SIFluids.ENERGY);
    }

    @Override
    public ItemStack getFilled(ItemStack stack, FluidStack fluidStack) {
        return stack;
    }

    @Override
    public String getDescription(ItemStack itemStack) {
        int fuel = itemStack.getData().getInteger("fuel");
        int depleted = itemStack.getData().getInteger("depleted");
        String text = "Fuel: " +
                TextFormatting.RED +
                fuel +
                TextFormatting.WHITE +
                " | " +
                TextFormatting.BROWN +
                depleted +
                TextFormatting.WHITE +
                " Depletion: " +
                TextFormatting.LIGHT_GRAY +
                String.format("%.2f", ((float) depleted / getCapacity(itemStack)) * 100) +
                "%" +
                "\n" +
                "Capacity: " +
                TextFormatting.RED +
                getCapacity(itemStack);
        return text;
    }
}
