package sunsetsatellite.signalindustries.items;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.api.IItemFluidContainer;
import sunsetsatellite.fluidapi.api.ItemInventoryFluid;
import sunsetsatellite.fluidapi.api.SlotFluid;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidContainer;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemSignalumDrill extends ItemToolPickaxe implements ITiered, IItemFluidContainer {

    public Tier tier;
    public int blockDestroyCost;
    public ItemSignalumDrill(String name, int id, Tier tier, ToolMaterial enumtoolmaterial) {
        super(name, id, enumtoolmaterial);
        this.tier = tier;
        blockDestroyCost = 5 * tier.ordinal();
    }

    public enum DrillMode{
        NORMAL,
        X3
    }

    @Override
    public Tier getTier() {
        return tier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getColor() + tier.getRank()+"\n"+"Mode: "+getModeString(stack)+"\n"+"Energy: "+ TextFormatting.RED+stack.getData().getInteger("energy")+"/"+getCapacity(stack)+TextFormatting.WHITE;
    }

    public String getModeString(ItemStack stack){
        DrillMode mode = getMode(stack);
        switch (mode){
            case NORMAL:
                return TextFormatting.GRAY+"Normal";
            case X3:
                return TextFormatting.RED+"3x3";
        }
        return TextFormatting.GRAY+"Normal";
    }

    public DrillMode getMode(ItemStack stack) {
        try {
            return DrillMode.valueOf(stack.getData().getString("mode"));
        } catch (IllegalArgumentException e){
            return DrillMode.NORMAL;
        }
    }

    public void setMode(ItemStack stack, DrillMode mode){
        stack.getData().putString("mode",mode.toString());
    }


    @Override
    public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving) {
        int energy = itemstack.getData().getInteger("energy");
        Minecraft mc = Minecraft.getMinecraft(Minecraft.class);
        Side side = mc.objectMouseOver.side;
        DrillMode mode = getMode(itemstack);
        switch (mode) {
            case NORMAL:
                if(energy >= blockDestroyCost) {
                    itemstack.getData().putInt("energy", energy - blockDestroyCost);
                }
                break;
            case X3:
                if(side.getAxis() == Axis.Y){
                    for (int x = -1; x <= 1; x++) {
                        for (int z = -1; z <= 1; z++) {
                            Block block = mc.theWorld.getBlock(j+x,k,l+z);
                            if(block != null && entityliving instanceof EntityPlayer){
                                if(energy >= blockDestroyCost) {
                                    itemstack.getData().putInt("energy", energy - blockDestroyCost);
                                    energy = itemstack.getData().getInteger("energy");
                                    int meta = mc.theWorld.getBlockMetadata(j+x,k,l+z);
                                    if(((EntityPlayer) entityliving).canHarvestBlock(block)){
                                        block.harvestBlock(mc.theWorld, (EntityPlayer) entityliving,j+x,k,l+z,meta,null);
                                        mc.theWorld.setBlockWithNotify(j+x,k,l+z,0);
                                    }
                                }
                            }
                        }
                    }
                } else if (side.getAxis() == Axis.Z) {
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            Block block = mc.theWorld.getBlock(j+x,k+y,l);
                            if(block != null && entityliving instanceof EntityPlayer){
                                if(energy >= blockDestroyCost) {
                                    itemstack.getData().putInt("energy", energy - blockDestroyCost);
                                    energy = itemstack.getData().getInteger("energy");
                                    int meta = mc.theWorld.getBlockMetadata(j+x,k+y,l);
                                    if(((EntityPlayer) entityliving).canHarvestBlock(block)){
                                        block.harvestBlock(mc.theWorld, (EntityPlayer) entityliving,j+x,k+y,l,meta,null);
                                        mc.theWorld.setBlockWithNotify(j+x,k+y,l,0);
                                    }
                                }
                            }
                        }
                    }
                } else if (side.getAxis() == Axis.X) {
                    for (int z = -1; z <= 1; z++) {
                        for (int y = -1; y <= 1; y++) {
                            Block block = mc.theWorld.getBlock(j,k+y,l+z);
                            if(block != null && entityliving instanceof EntityPlayer){
                                if(energy >= blockDestroyCost) {
                                    itemstack.getData().putInt("energy", energy - blockDestroyCost);
                                    energy = itemstack.getData().getInteger("energy");
                                    int meta = mc.theWorld.getBlockMetadata(j,k+y,l+z);
                                    if(((EntityPlayer) entityliving).canHarvestBlock(block)){
                                        block.harvestBlock(mc.theWorld, (EntityPlayer) entityliving,j,k+y,l+z,meta,null);
                                        mc.theWorld.setBlockWithNotify(j,k+y,l+z,0);
                                    }
                                }
                            }
                        }
                    }
                }
                break;
        }

        return true;
    }

    @Override
    public float getStrVsBlock(ItemStack itemstack, Block block) {
        float superValue = super.getStrVsBlock(itemstack,block);
        if(superValue == 1.0f) return 1.0f;
        if(itemstack.getData().getInteger("energy") >= blockDestroyCost){
            return this.material.getEfficiency(false);
        } else {
            return 1.0f;
        }
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return 4000 * tier.ordinal();
    }

    @Override
    public int getRemainingCapacity(ItemStack stack) {
        return getCapacity(stack)-stack.getData().getInteger("energy");
    }

    @Override
    public boolean canFill(ItemStack stack) {
        return getRemainingCapacity(stack) > 0;
    }

    @Override
    public boolean canDrain(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack fill(SlotFluid slot, ItemStack stack) {
        if(slot.getFluidStack() == null){
            return null;
        }
        if(slot.getFluidStack().getLiquid() == SignalIndustries.energyFlowing){
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("energy");
            int amount = slot.getFluidStack().amount;
            if(remaining == 0){
                return null;
            }
            if(amount > remaining){
                slot.getFluidStack().amount -= remaining;
                if(slot.getFluidStack().amount <= 0){
                    slot.putStack(null);
                }
                CompoundTag data = new CompoundTag();
                data.putInt("energy",getCapacity(stack));
                stack.setData(data);
                return stack;
            } else {
                slot.putStack(null);
                CompoundTag data = new CompoundTag();
                data.putInt("energy",saturation + amount);
                stack.setData(data);
                return stack;
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
            int saturation = stack.getData().getInteger("energy");
            int amount = tile.getFluidInSlot(slotIndex).amount;
            if(remaining == 0){
                return null;
            }
            if(amount > remaining){
                tile.getFluidInSlot(slotIndex).amount -= remaining;
                if(tile.getFluidInSlot(slotIndex).amount <= 0){
                    tile.setFluidInSlot(slotIndex,null);
                }
                CompoundTag data = new CompoundTag();
                data.putInt("energy",getCapacity(stack));
                stack.setData(data);
                return stack;
            } else {
                tile.setFluidInSlot(slotIndex,null);
                CompoundTag data = new CompoundTag();
                data.putInt("energy",saturation + amount);
                stack.setData(data);
                return stack;
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
            int saturation = stack.getData().getInteger("energy");
            int amount = Math.min(tile.getFluidInSlot(slotIndex).amount,maxAmount);
            if(remaining == 0) return null;
            int result = Math.min(amount,remaining);
            if(result == 0) return null;
            tile.getFluidInSlot(slotIndex).amount -= result;
            if(tile.getFluidInSlot(slotIndex).amount <= 0){
                tile.setFluidInSlot(slotIndex,null);
            }
            CompoundTag data = new CompoundTag();
            data.putInt("energy",saturation+result);
            stack.setData(data);
            return stack;
        }
        return null;
    }

    @Override
    public ItemStack fill(int slotIndex, ItemInventoryFluid inv, ItemStack stack) {
        return null;
    }

    @Override
    public void drain(ItemStack stack, SlotFluid slot, TileEntityFluidContainer tile) {
    }

    @Override
    public void drain(ItemStack stack, FluidStack fluid, TileEntityFluidContainer tile) {

    }

    @Override
    public void drain(ItemStack stack, SlotFluid slot, ItemInventoryFluid inv) {
    }

    @Override
    public void drain(ItemStack stack, FluidStack fluid, ItemInventoryFluid inv) {
    }
}
