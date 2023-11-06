package sunsetsatellite.signalindustries.items;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.api.IItemFluidContainer;
import sunsetsatellite.fluidapi.api.ItemInventoryFluid;
import sunsetsatellite.fluidapi.api.SlotFluid;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidContainer;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.sunsetutils.util.ICustomDescription;

public class ItemFuelCell extends Item implements IItemFluidContainer, ICustomDescription {
    public ItemFuelCell(int id) {
        super(id);
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
    public ItemStack fill(SlotFluid slot, ItemStack stack) {
        if(slot.getFluidStack() == null){
            return null;
        }
        if(slot.getFluidStack().getLiquid() == SignalIndustries.energyFlowing){
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("fuel");
            int amount = slot.getFluidStack().amount;
            ItemStack cell = new ItemStack(SignalIndustries.fuelCell,1);
            if(remaining == 0){
                return null;
            }
            if(amount > remaining){
                slot.getFluidStack().amount -= remaining;
                if(slot.getFluidStack().amount <= 0){
                    slot.putStack(null);
                }
                CompoundTag data = new CompoundTag();
                data.putInt("fuel",getCapacity(stack));
                cell.setData(data);
                return cell;
            } else {
                slot.putStack(null);
                CompoundTag data = new CompoundTag();
                data.putInt("fuel",saturation + amount);
                cell.setData(data);
                return cell;
            }
        }
        return null;
    }

    @Override
    public ItemStack fill(int slotIndex, TileEntityFluidContainer tile, ItemStack stack) {
        if(tile.getFluidInSlot(slotIndex) == null){
            return null;
        }
        if(tile.getFluidInSlot(slotIndex).getLiquid() == SignalIndustries.energyFlowing){
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("fuel");
            int amount = tile.getFluidInSlot(slotIndex).amount;
            ItemStack cell = new ItemStack(SignalIndustries.fuelCell,1);
            if(remaining == 0){
                return null;
            }
            if(amount > remaining){
                tile.getFluidInSlot(slotIndex).amount -= remaining;
                if(tile.getFluidInSlot(slotIndex).amount <= 0){
                    tile.setFluidInSlot(slotIndex,null);
                }
                CompoundTag data = new CompoundTag();
                data.putInt("fuel",getCapacity(stack));
                cell.setData(data);
                return cell;
            } else {
                tile.setFluidInSlot(slotIndex,null);
                CompoundTag data = new CompoundTag();
                data.putInt("fuel",saturation + amount);
                cell.setData(data);
                return cell;
            }
        }
        return null;
    }

    @Override
    public ItemStack fill(int slotIndex, TileEntityFluidContainer tile, ItemStack stack, int maxAmount) {
        if(tile.getFluidInSlot(slotIndex) == null){
            return null;
        }
        if(tile.getFluidInSlot(slotIndex).getLiquid() == SignalIndustries.energyFlowing){
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("fuel");
            int amount = Math.min(tile.getFluidInSlot(slotIndex).amount,maxAmount);
            ItemStack cell = new ItemStack(SignalIndustries.fuelCell,1);
            if(remaining == 0) return null;
            int result = Math.min(amount,remaining);
            if(result == 0) return null;
            tile.getFluidInSlot(slotIndex).amount -= result;
            if(tile.getFluidInSlot(slotIndex).amount <= 0){
                tile.setFluidInSlot(slotIndex,null);
            }
            CompoundTag data = new CompoundTag();
            data.putInt("fuel", saturation+result);
            cell.setData(data);
            return cell;
        }
        return null;
    }

    @Override
    public ItemStack fill(int slotIndex, ItemInventoryFluid inv, ItemStack stack) {
        return null;
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
    public void drain(ItemStack stack, FluidStack fluid, TileEntityFluidContainer tile) {

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
    public void drain(ItemStack stack, FluidStack fluid, ItemInventoryFluid inv) {
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
