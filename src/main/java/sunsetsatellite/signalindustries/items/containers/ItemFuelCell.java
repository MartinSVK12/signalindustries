package sunsetsatellite.signalindustries.items.containers;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.impl.ItemInventoryFluid;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.SignalIndustries;


public class ItemFuelCell extends Item implements IItemFluidContainer, ICustomDescription {

    public ItemFuelCell(String name, int id) {
        super(name, id);
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return 4000;
    }

    @Override
    public int getRemainingCapacity(ItemStack stack) {
        return 4000-(stack.getData().getInteger("fuel")+stack.getData().getInteger("depleted"));
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
    public ItemStack fill(FluidStack slot, ItemStack stack) {
        if(slot== null){
            return null;
        }
        if(slot.getLiquid() == SignalIndustries.energyFlowing){
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("fuel");
            int amount = slot.amount;
            ItemStack cell = new ItemStack(SignalIndustries.fuelCell,1);
            if(remaining == 0){
                return null;
            }
            if(amount > remaining){
                slot.amount -= remaining;
                CompoundTag data = new CompoundTag();
                data.putInt("fuel",getCapacity(stack));
                cell.setData(data);
                return cell;
            } else {
                CompoundTag data = new CompoundTag();
                data.putInt("fuel",saturation + amount);
                cell.setData(data);
                return cell;
            }
        }
        return null;
    }

    @Override
    public ItemStack fill(FluidStack slot, ItemStack stack, TileEntityFluidContainer tile) {
        return fill(slot, stack);
    }

    @Override
    public ItemStack fill(FluidStack slot, ItemStack stack, TileEntityFluidContainer tile, int maxAmount) {
        if(slot == null){
            return null;
        }
        if(slot.getLiquid() == SignalIndustries.energyFlowing){
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("fuel");
            int amount = Math.min(slot.amount,maxAmount);
            ItemStack cell = new ItemStack(SignalIndustries.fuelCell,1);
            if(remaining == 0) return null;
            int result = Math.min(amount,remaining);
            if(result == 0) return null;
            slot.amount -= result;
            CompoundTag data = new CompoundTag();
            data.putInt("fuel", saturation+result);
            cell.setData(data);
            return cell;
        }
        return null;
    }

    @Override
    public ItemStack fill(FluidStack slot, ItemStack stack, ItemInventoryFluid inv) {
        return fill(slot, stack);
    }


    @Override
    public void drain(ItemStack stack, SlotFluid slot, TileEntityFluidContainer tile) {
        int saturation = stack.getData().getInteger("depleted");
        int capacity = tile.getFluidCapacityForSlot(slot.slotNumber);
        if(saturation == 0){
            return;
        }
        if(slot.getFluidStack() != null){
            int amount = slot.getFluidStack().amount;
            if(amount + saturation > capacity){
                int remainder = (amount+saturation)-capacity;
                slot.getFluidStack().amount = capacity;
                stack.getData().putInt("depleted",remainder);
            } else {
                slot.getFluidStack().amount += saturation;
                stack.getData().putInt("depleted",0);
            }
        } else {
            if(saturation > capacity){
                int remainder = saturation-capacity;
                FluidStack fluid = new FluidStack((BlockFluid) SignalIndustries.burntSignalumFlowing,capacity);
                slot.putStack(fluid);
                stack.getData().putInt("depleted",remainder);
            } else {
                FluidStack fluid = new FluidStack((BlockFluid) SignalIndustries.burntSignalumFlowing,saturation);
                slot.putStack(fluid);
                stack.getData().putInt("depleted",0);
            }
        }
    }

    @Override
    public void drain(ItemStack stack, SlotFluid slot, ItemInventoryFluid inv) {
        int saturation = stack.getData().getInteger("depleted");
        int capacity = inv.getFluidCapacityForSlot(slot.slotNumber);
        if(saturation == 0){
            return;
        }
        if(slot.getFluidStack() != null){
            int amount = slot.getFluidStack().amount;
            if(amount + saturation > capacity){
                int remainder = (amount+saturation)-capacity;
                slot.getFluidStack().amount = capacity;
                stack.getData().putInt("depleted",remainder);
            } else {
                slot.getFluidStack().amount += saturation;
                stack.getData().putInt("depleted",0);
            }
        } else {
            if(saturation > capacity){
                int remainder = saturation-capacity;
                FluidStack fluid = new FluidStack((BlockFluid) SignalIndustries.burntSignalumFlowing,capacity);
                slot.putStack(fluid);
                stack.getData().putInt("depleted",remainder);
            } else {
                FluidStack fluid = new FluidStack((BlockFluid) SignalIndustries.burntSignalumFlowing,saturation);
                slot.putStack(fluid);
                stack.getData().putInt("depleted",0);
            }
        }
    }

    @Override
    public String getDescription(ItemStack itemStack) {
        int fuel = itemStack.getData().getInteger("fuel");
        int depleted = itemStack.getData().getInteger("depleted");
        StringBuilder text = new StringBuilder();
        text.append("Fuel: ")
                .append(TextFormatting.RED)
                .append(fuel)
                .append(TextFormatting.WHITE)
                .append(" | ")
                .append(TextFormatting.BROWN)
                .append(depleted)
                .append(TextFormatting.WHITE)
                .append(" Depletion: ")
                .append(TextFormatting.LIGHT_GRAY)
                .append(String.format("%.2f",((float)depleted/getCapacity(itemStack))*100))
                .append("%")
                .append("\n")
                .append("Capacity: ")
                .append(TextFormatting.RED)
                .append(getCapacity(itemStack));
        return text.toString();
    }

    @Override
    public int getIconIndex(ItemStack itemstack) {
        int fuel = itemstack.getData().getInteger("fuel");
        int depleted = itemstack.getData().getInteger("depleted");
        if(fuel <= 0 && depleted <= 0){
            return Item.iconCoordToIndex(SignalIndustries.fuelCellTex[0][0],SignalIndustries.fuelCellTex[0][1]);
        } else if (fuel <= 0) {
            return Item.iconCoordToIndex(SignalIndustries.fuelCellTex[2][0],SignalIndustries.fuelCellTex[2][1]);
        } else {
            return Item.iconCoordToIndex(SignalIndustries.fuelCellTex[1][0],SignalIndustries.fuelCellTex[1][1]);
        }
    }
}
