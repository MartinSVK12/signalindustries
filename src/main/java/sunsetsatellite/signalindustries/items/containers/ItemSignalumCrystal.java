package sunsetsatellite.signalindustries.items.containers;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.impl.ItemInventoryFluid;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.EntityCrystal;


public class ItemSignalumCrystal extends Item implements IItemFluidContainer, ICustomDescription {

    public ItemSignalumCrystal(String name, int id) {
        super(name, id);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if(itemstack.getItem().id != SignalIndustries.signalumCrystalEmpty.id){
            itemstack.consumeItem(entityplayer);
            //world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            if (!world.isClientSide) {
                world.entityJoinedWorld(new EntityCrystal(world, entityplayer));
            }
        }

        return itemstack;
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return stack.getData().getInteger("size")*1000;
    }

    @Override
    public int getRemainingCapacity(ItemStack stack) {
        return (stack.getData().getInteger("size")*1000)-stack.getData().getInteger("saturation");
    }

    @Override
    public boolean canFill(ItemStack stack) {
        return getRemainingCapacity(stack) > 0;
    }

    @Override
    public boolean canDrain(ItemStack stack) {
        return getCapacity(stack) > getRemainingCapacity(stack) && stack.getItem().id == SignalIndustries.signalumCrystal.id;
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack) {
        if(fluidStack == null){
            return null;
        }
        if(fluidStack.getLiquid() == SignalIndustries.energyFlowing){
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("saturation");
            int amount = fluidStack.amount;
            int size = stack.getData().getInteger("size");
            ItemStack crystal = new ItemStack(SignalIndustries.signalumCrystal,1);
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
                data.putInt("saturation",saturation + amount);
                crystal.setData(data);
                return crystal;
            }
        }
        return null;
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, TileEntityFluidContainer tile) {
        return fill(fluidStack,stack);
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, TileEntityFluidContainer tile, int maxAmount) {
        if(fluidStack == null){
            return null;
        }
        if(fluidStack.getLiquid() == SignalIndustries.energyFlowing){
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("saturation");
            int amount = Math.min(fluidStack.amount,maxAmount);
            int size = stack.getData().getInteger("size");
            ItemStack crystal = new ItemStack(SignalIndustries.signalumCrystal,1);
            if(remaining == 0) return null;
            int result = Math.min(amount,remaining);
            if(result == 0) return null;
            fluidStack.amount -= result;
            CompoundTag data = new CompoundTag();
            data.putInt("size",size);
            data.putInt("saturation",saturation+result);
            crystal.setData(data);
            return crystal;
        }
        return null;
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, ItemInventoryFluid inv) {
        return fill(fluidStack,stack);
    }

    @Override
    public void drain(ItemStack stack, SlotFluid slot, TileEntityFluidContainer tile) {
        int saturation = stack.getData().getInteger("saturation");
        int capacity = tile.getFluidCapacityForSlot(slot.slotNumber);
        int size = stack.getData().getInteger("size");
        if(saturation == 0){
            stack.itemID = SignalIndustries.signalumCrystalEmpty.id;
            return;
        }
        if(slot.getFluidStack() != null){
            int amount = slot.getFluidStack().amount;
            if(amount + saturation > capacity){
                int remainder = (amount+saturation)-capacity;
                slot.getFluidStack().amount = capacity;
                stack.getData().putInt("saturation",remainder);
                stack.getData().putInt("size",size);
            } else {
                slot.getFluidStack().amount += saturation;
                stack.getData().putInt("saturation",0);
                stack.getData().putInt("size",size);
                stack.itemID = SignalIndustries.signalumCrystalEmpty.id;
            }
        } else {
            if(saturation > capacity){
                int remainder = saturation-capacity;
                FluidStack fluid = new FluidStack((BlockFluid) SignalIndustries.energyFlowing,capacity);
                slot.putStack(fluid);
                stack.getData().putInt("saturation",remainder);
                stack.getData().putInt("size",size);
            } else {
                FluidStack fluid = new FluidStack((BlockFluid) SignalIndustries.energyFlowing,saturation);
                slot.putStack(fluid);
                stack.getData().putInt("saturation",0);
                stack.getData().putInt("size",size);
                stack.itemID = SignalIndustries.signalumCrystalEmpty.id;
            }
        }
    }
    @Override
    public void drain(ItemStack stack, SlotFluid slot, ItemInventoryFluid inv) {
        int saturation = stack.getData().getInteger("saturation");
        int capacity = inv.getFluidCapacityForSlot(slot.slotNumber);
        int size = stack.getData().getInteger("size");
        if(saturation == 0){
            stack.itemID = SignalIndustries.signalumCrystalEmpty.id;
            return;
        }
        if(slot.getFluidStack() != null){
            int amount = slot.getFluidStack().amount;
            if(amount + saturation > capacity){
                int remainder = (amount+saturation)-capacity;
                slot.getFluidStack().amount = capacity;
                stack.getData().putInt("saturation",remainder);
                stack.getData().putInt("size",size);
            } else {
                slot.getFluidStack().amount += saturation;
                stack.getData().putInt("saturation",0);
                stack.getData().putInt("size",size);
                stack.itemID = SignalIndustries.signalumCrystalEmpty.id;
            }
        } else {
            if(saturation > capacity){
                int remainder = saturation-capacity;
                FluidStack fluid = new FluidStack((BlockFluid) SignalIndustries.energyFlowing,capacity);
                slot.putStack(fluid);
                stack.getData().putInt("saturation",remainder);
                stack.getData().putInt("size",size);
            } else {
                FluidStack fluid = new FluidStack((BlockFluid) SignalIndustries.energyFlowing,saturation);
                slot.putStack(fluid);
                stack.getData().putInt("saturation",0);
                stack.getData().putInt("size",size);
                stack.itemID = SignalIndustries.signalumCrystalEmpty.id;
            }
        }
    }

    @Override
    public String getDescription(ItemStack stack) {
        StringBuilder text = new StringBuilder();
        text.append("Size: ").append(stack.getData().getInteger("size"));
        if(stack.getItem().id == SignalIndustries.signalumCrystal.id){
            text.append(" | ").append("Saturation: ").append(stack.getData().getInteger("saturation"));
        }
        return text.toString();
    }
}
