package sunsetsatellite.signalindustries.items.tools;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;

import java.util.List;


public class ItemSignalumCrystal extends Item implements IItemFluidContainer, ICustomDescription {

    public final boolean infinite;

    public ItemSignalumCrystal(String translationKey, String namespaceId, int id, boolean infinite) {
        super(translationKey, namespaceId, id);
        this.infinite = infinite;
    }

    @Override
    public int getCapacity(ItemStack stack) {
        if(infinite) return Integer.MAX_VALUE;
        return stack.getData().getInteger("size")*1000;
    }

    @Override
    public int getRemainingCapacity(ItemStack stack) {
        if(infinite) return Integer.MAX_VALUE;
        return (stack.getData().getInteger("size")*1000)-stack.getData().getInteger("saturation");
    }

    @Override
    public boolean canFill(ItemStack stack) {
        return getRemainingCapacity(stack) > 0;
    }

    @Override
    public boolean canDrain(ItemStack stack) {
        if(infinite) return true;
        return getCapacity(stack) > getRemainingCapacity(stack) && stack.getItem().id == SIItems.signalumCrystalBattery.id;
    }

    @Override
    public FluidStack getCurrentFluid(ItemStack stack) {
        return new FluidStack(SIFluids.ENERGY,getCapacity(stack));
    }

    @Override
    public void setCurrentFluid(FluidStack fluidStack, ItemStack stack) {
        if(fluidStack.fluid != SIFluids.ENERGY) return;
        stack.getData().putInt("saturation",fluidStack.amount);

    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack) {
        if(fluidStack == null){
            return null;
        }
        if(fluidStack.fluid == SIFluids.ENERGY){
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("saturation");
            if(infinite) saturation = Integer.MAX_VALUE;
            int amount = fluidStack.amount;
            int size = stack.getData().getInteger("size");
            ItemStack crystal = new ItemStack(SIItems.signalumCrystalBattery,1);
            if(infinite) crystal = new ItemStack(SIItems.infiniteSignalumCrystal);
            if(remaining == 0){
                return null;
            }
            if(amount > remaining){
                fluidStack.amount -= remaining;
                if(fluidStack.amount <= 0){
                    fluidStack.amount = 0;
                }
                CompoundTag data = new CompoundTag();
                data.putInt("size",size);
                data.putInt("saturation",getCapacity(stack));
                crystal.setData(data);
                return crystal;
            } else {
                fluidStack.amount = 0;
                CompoundTag data = new CompoundTag();
                data.putInt("size",size);
                if(!infinite) data.putInt("saturation",saturation + amount);
                crystal.setData(data);
                return crystal;
            }
        }
        return null;
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, IFluidInventory tile) {
        return fill(fluidStack,stack);
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, IFluidInventory tile, int maxAmount) {
        if(fluidStack == null){
            return null;
        }
        if(fluidStack.fluid == SIFluids.ENERGY){
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("saturation");
            if(infinite) saturation = Integer.MAX_VALUE;
            int amount = Math.min(fluidStack.amount,maxAmount);
            int size = stack.getData().getInteger("size");
            ItemStack crystal = new ItemStack(SIItems.signalumCrystalBattery,1);
            if(infinite) crystal = new ItemStack(SIItems.infiniteSignalumCrystal);
            if(remaining == 0) return null;
            int result = Math.min(amount,remaining);
            if(result == 0) return null;
            fluidStack.amount -= result;
            CompoundTag data = new CompoundTag();
            data.putInt("size",size);
            if(!infinite) data.putInt("saturation",saturation+result);
            crystal.setData(data);
            return crystal;
        }
        return null;
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, IItemFluidContainer inv) {
        return fill(fluidStack,stack);
    }

    @Override
    public void drain(ItemStack stack, int slot, IFluidInventory tile) {
        int saturation = stack.getData().getInteger("saturation");
        if(infinite) saturation = Integer.MAX_VALUE;
        int capacity = tile.getFluidCapacityForSlot(slot);
        int size = stack.getData().getInteger("size");
        if(saturation == 0){
            return;
        }
        FluidStack fluidStack = tile.getFluidInSlot(slot);
        if(fluidStack != null){
            if(infinite){
                fluidStack.amount = capacity;
                return;
            }
            int amount = fluidStack.amount;
            if(amount + saturation > capacity){
                int remainder = (amount+saturation)-capacity;
                fluidStack.amount = capacity;
                stack.getData().putInt("saturation",remainder);
                stack.getData().putInt("size",size);
            } else {
                fluidStack.amount += saturation;
                stack.getData().putInt("saturation",0);
                stack.getData().putInt("size",size);
            }
        } else {
            if(saturation > capacity){
                int remainder = saturation-capacity;
                FluidStack fluid = new FluidStack(SIFluids.ENERGY,capacity);
                tile.setFluidInSlot(slot,fluid);
                stack.getData().putInt("saturation",remainder);
                stack.getData().putInt("size",size);
            } else {
                FluidStack fluid = new FluidStack(SIFluids.ENERGY,saturation);
                tile.setFluidInSlot(slot,fluid);
                stack.getData().putInt("saturation",0);
                stack.getData().putInt("size",size);
            }
        }
    }

    @Override
    public void drain(ItemStack stack, ItemStack other, int slot, IItemFluidContainer inv) {
        int saturation = stack.getData().getInteger("saturation");
        if(infinite) saturation = Integer.MAX_VALUE;
        int capacity = inv.getCapacity(other);
        int size = stack.getData().getInteger("size");
        if(saturation == 0){
            return;
        }
        FluidStack fluidStack = inv.getCurrentFluid(other);
        if(fluidStack != null){
            if(infinite) {
                fluidStack.amount = inv.getCapacity(other);
                return;
            }
            int amount = fluidStack.amount;
            if(amount + saturation > capacity){
                int remainder = (amount+saturation)-capacity;
                fluidStack.amount = capacity;
                stack.getData().putInt("saturation",remainder);
                stack.getData().putInt("size",size);
            } else {
                fluidStack.amount += saturation;
                stack.getData().putInt("saturation",0);
                stack.getData().putInt("size",size);
            }
        } else {
            if(saturation > capacity){
                int remainder = saturation-capacity;
                FluidStack fluid = new FluidStack(SIFluids.ENERGY,capacity);
                inv.setCurrentFluid(fluid, other);
                if(infinite) {
                    fluid.amount = inv.getCapacity(other);
                    return;
                }
                stack.getData().putInt("saturation",remainder);
                stack.getData().putInt("size",size);
            } else {
                FluidStack fluid = new FluidStack(SIFluids.ENERGY,saturation);
                inv.setCurrentFluid(fluid, other);
                if(infinite) {
                    fluid.amount = inv.getCapacity(other);
                    return;
                }
                stack.getData().putInt("saturation",0);
                stack.getData().putInt("size",size);
            }
        }
    }

    @Override
    public FluidStack drain(ItemStack stack, int amount) {
        return null;
    }

    @Override
    public List<Fluid> getAllowedFluids(ItemStack stack) {
        return Catalyst.listOf(SIFluids.ENERGY);
    }

    @Override
    public ItemStack getFilled(ItemStack stack, FluidStack fluidStack) {
        return stack;
    }


    @Override
    public String getDescription(ItemStack stack) {
        StringBuilder text = new StringBuilder();
        if(infinite){
            text.append("Size: 1 | Saturation: Infinite");
        } else {
            text.append("Size: ").append(stack.getData().getInteger("size"));
            text.append(" | ").append("Saturation: ").append(stack.getData().getInteger("saturation"));
        }
        return text.toString();
    }
}
