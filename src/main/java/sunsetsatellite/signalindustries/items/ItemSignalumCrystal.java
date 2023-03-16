package sunsetsatellite.signalindustries.items;

import net.minecraft.src.*;
import net.minecraft.src.command.ChatColor;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.api.IFluidInventory;
import sunsetsatellite.fluidapi.api.IItemFluidContainer;
import sunsetsatellite.fluidapi.api.SlotFluid;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidContainer;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.EntityCrystal;
import sunsetsatellite.signalindustries.interfaces.ICustomDescription;

public class ItemSignalumCrystal extends Item implements IItemFluidContainer, ICustomDescription {

    public ItemSignalumCrystal(int id) {
        super(id);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        itemstack.consumeItem(entityplayer);
        world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        if (!world.isMultiplayerAndNotHost) {
            world.entityJoinedWorld(new EntityCrystal(world, entityplayer));
        }

        return itemstack;
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return stack.tag.getInteger("size")*1000;
    }

    @Override
    public int getRemainingCapacity(ItemStack stack) {
        return (stack.tag.getInteger("size")*1000)-stack.tag.getInteger("saturation");
    }

    @Override
    public boolean canFill(ItemStack stack) {
        return getRemainingCapacity(stack) > 0;
    }

    @Override
    public boolean canDrain(ItemStack stack) {
        return getCapacity(stack) > getRemainingCapacity(stack) && stack.getItem().itemID == SignalIndustries.signalumCrystal.itemID;
    }

    @Override
    public ItemStack fill(SlotFluid slot, ItemStack stack) {
        if(slot.getFluidStack() == null){
            return null;
        }
        if(slot.getFluidStack().getLiquid() == SignalIndustries.energyFlowing){
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.tag.getInteger("saturation");
            int amount = slot.getFluidStack().amount;
            int size = stack.tag.getInteger("size");
            ItemStack crystal = new ItemStack(SignalIndustries.signalumCrystal,1);
            if(remaining == 0){
                return null;
            }
            if(amount >= remaining){
                slot.getFluidStack().amount -= remaining;
                if(slot.getFluidStack().amount <= 0){
                    slot.putStack(null);
                }
                NBTTagCompound data = new NBTTagCompound();
                data.setInteger("size",size);
                data.setInteger("saturation",getCapacity(stack));
                crystal.tag = data;
                return crystal;
            } else {
                slot.putStack(null);
                NBTTagCompound data = new NBTTagCompound();
                data.setInteger("size",size);
                data.setInteger("saturation",saturation + amount);
                crystal.tag = data;
                return crystal;
            }
        }
        return null;
    }

    @Override
    public ItemStack fill(int slotIndex, TileEntityFluidContainer tile, ItemStack stack) {
        return null;
    }

    @Override
    public void drain(ItemStack stack, SlotFluid slot, TileEntityFluidContainer tile) {
        int saturation = stack.tag.getInteger("saturation");
        int capacity = tile.getFluidCapacityForSlot(slot.slotNumber);
        int size = stack.tag.getInteger("size");
        if(saturation == 0){
            stack.itemID = SignalIndustries.signalumCrystalEmpty.itemID;
            return;
        }
        if(slot.getFluidStack() != null){
            int amount = slot.getFluidStack().amount;
            if(amount + saturation > capacity){
                int remainder = (amount+saturation)-capacity;
                slot.getFluidStack().amount = capacity;
                stack.tag.setInteger("saturation",remainder);
                stack.tag.setInteger("size",size);
            } else {
                slot.getFluidStack().amount += saturation;
                stack.tag.setInteger("saturation",0);
                stack.tag.setInteger("size",size);
                stack.itemID = SignalIndustries.signalumCrystalEmpty.itemID;
            }
        } else {
            if(saturation > capacity){
                int remainder = saturation-capacity;
                FluidStack fluid = new FluidStack((BlockFluid) SignalIndustries.energyFlowing,capacity);
                slot.putStack(fluid);
                stack.tag.setInteger("saturation",remainder);
                stack.tag.setInteger("size",size);
            } else {
                FluidStack fluid = new FluidStack((BlockFluid) SignalIndustries.energyFlowing,saturation);
                slot.putStack(fluid);
                stack.tag.setInteger("saturation",0);
                stack.tag.setInteger("size",size);
                stack.itemID = SignalIndustries.signalumCrystalEmpty.itemID;
            }
        }
    }

    @Override
    public void drain(ItemStack stack, FluidStack fluid, TileEntityFluidContainer tile) {

    }

    @Override
    public String getDescription(ItemStack stack) {
        StringBuilder text = new StringBuilder();
        text.append("Size: ").append(stack.tag.getInteger("size"));
        if(stack.getItem().itemID == SignalIndustries.signalumCrystal.itemID){
            text.append(" | ").append("Saturation: ").append(stack.tag.getInteger("saturation"));
        }
        return text.toString();
    }
}
