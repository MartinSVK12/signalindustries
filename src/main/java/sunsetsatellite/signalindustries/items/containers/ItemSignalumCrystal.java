package sunsetsatellite.signalindustries.items.containers;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.impl.ItemInventoryFluid;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;


public class ItemSignalumCrystal extends Item implements IItemFluidContainer, ICustomDescription {

    public final boolean infinite;

    public ItemSignalumCrystal(String name, int id, boolean infinite) {
        super(name, id);
        this.infinite = infinite;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        /*if(itemstack.getItem().id != SignalIndustries.signalumCrystalEmpty.id){
            itemstack.consumeItem(entityplayer);
            //world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            if (!world.isClientSide) {
                world.entityJoinedWorld(new EntityCrystal(world, entityplayer));
            }
        }*/

        return itemstack;
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
        return getCapacity(stack) > getRemainingCapacity(stack) && stack.getItem().id == SIItems.signalumCrystal.id;
    }

    @Override
    public FluidStack getCurrentFluid(ItemStack stack) {
        return new FluidStack(SIBlocks.energyFlowing,getCapacity(stack));
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack) {
        if(fluidStack == null){
            return null;
        }
        if(fluidStack.getLiquid() == SIBlocks.energyFlowing){
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("saturation");
            if(infinite) saturation = Integer.MAX_VALUE;
            int amount = fluidStack.amount;
            int size = stack.getData().getInteger("size");
            ItemStack crystal = new ItemStack(SIItems.signalumCrystal,1);
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
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, TileEntityFluidContainer tile) {
        return fill(fluidStack,stack);
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, IFluidInventory tile) {
        return fill(fluidStack,stack);
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, TileEntityFluidContainer tile, int maxAmount) {
        if(fluidStack == null){
            return null;
        }
        if(fluidStack.getLiquid() == SIBlocks.energyFlowing){
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("saturation");
            if(infinite) saturation = Integer.MAX_VALUE;
            int amount = Math.min(fluidStack.amount,maxAmount);
            int size = stack.getData().getInteger("size");
            ItemStack crystal = new ItemStack(SIItems.signalumCrystal,1);
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
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, ItemInventoryFluid inv) {
        return fill(fluidStack,stack);
    }

    @Override
    public void drain(ItemStack stack, SlotFluid slot, TileEntityFluidContainer tile) {
        int saturation = stack.getData().getInteger("saturation");
        if(infinite) saturation = Integer.MAX_VALUE;
        int capacity = tile.getFluidCapacityForSlot(slot.slotNumber);
        int size = stack.getData().getInteger("size");
        if(saturation == 0){
            stack.itemID = SIItems.signalumCrystalEmpty.id;
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
                stack.itemID = SIItems.signalumCrystalEmpty.id;
            }
        } else {
            if(saturation > capacity){
                int remainder = saturation-capacity;
                FluidStack fluid = new FluidStack(SIBlocks.energyFlowing,capacity);
                slot.putStack(fluid);
                stack.getData().putInt("saturation",remainder);
                stack.getData().putInt("size",size);
            } else {
                FluidStack fluid = new FluidStack(SIBlocks.energyFlowing,saturation);
                slot.putStack(fluid);
                stack.getData().putInt("saturation",0);
                stack.getData().putInt("size",size);
                stack.itemID = SIItems.signalumCrystalEmpty.id;
            }
        }
    }

    @Override
    public void drain(ItemStack stack, SlotFluid slot, IFluidInventory tile) {
        int saturation = stack.getData().getInteger("saturation");
        if(infinite) saturation = Integer.MAX_VALUE;
        int capacity = tile.getFluidCapacityForSlot(slot.slotNumber);
        int size = stack.getData().getInteger("size");
        if(saturation == 0){
            stack.itemID = SIItems.signalumCrystalEmpty.id;
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
                stack.itemID = SIItems.signalumCrystalEmpty.id;
            }
        } else {
            if(saturation > capacity){
                int remainder = saturation-capacity;
                FluidStack fluid = new FluidStack(SIBlocks.energyFlowing,capacity);
                slot.putStack(fluid);
                stack.getData().putInt("saturation",remainder);
                stack.getData().putInt("size",size);
            } else {
                FluidStack fluid = new FluidStack(SIBlocks.energyFlowing,saturation);
                slot.putStack(fluid);
                stack.getData().putInt("saturation",0);
                stack.getData().putInt("size",size);
                stack.itemID = SIItems.signalumCrystalEmpty.id;
            }
        }
    }

    @Override
    public void drain(ItemStack stack, SlotFluid slot, ItemInventoryFluid inv) {
        int saturation = stack.getData().getInteger("saturation");
        if(infinite) saturation = Integer.MAX_VALUE;
        int capacity = inv.getFluidCapacityForSlot(slot.slotNumber);
        int size = stack.getData().getInteger("size");
        if(saturation == 0){
            stack.itemID = SIItems.signalumCrystalEmpty.id;
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
                stack.itemID = SIItems.signalumCrystalEmpty.id;
            }
        } else {
            if(saturation > capacity){
                int remainder = saturation-capacity;
                FluidStack fluid = new FluidStack(SIBlocks.energyFlowing,capacity);
                slot.putStack(fluid);
                stack.getData().putInt("saturation",remainder);
                stack.getData().putInt("size",size);
            } else {
                FluidStack fluid = new FluidStack(SIBlocks.energyFlowing,saturation);
                slot.putStack(fluid);
                stack.getData().putInt("saturation",0);
                stack.getData().putInt("size",size);
                stack.itemID = SIItems.signalumCrystalEmpty.id;
            }
        }
    }

    @Override
    public String getDescription(ItemStack stack) {
        StringBuilder text = new StringBuilder();
        if(infinite){
            text.append("Size: 1 | Saturation: Infinite");
        } else {
            text.append("Size: ").append(stack.getData().getInteger("size"));
            if(stack.getItem().id == SIItems.signalumCrystal.id){
                text.append(" | ").append("Saturation: ").append(stack.getData().getInteger("saturation"));
            }
        }
        return text.toString();
    }
}
