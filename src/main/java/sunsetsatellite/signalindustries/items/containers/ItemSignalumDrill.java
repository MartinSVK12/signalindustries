package sunsetsatellite.signalindustries.items.containers;

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
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.impl.ItemInventoryFluid;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemSignalumDrill extends ItemToolPickaxe implements ITiered, IItemFluidContainer {

    public Tier tier;
    public int blockDestroyCost;
    public ItemSignalumDrill(String name, int id, Tier tier, ToolMaterial enumtoolmaterial) {
        super(name, id, enumtoolmaterial);
        this.tier = tier;
        blockDestroyCost = 1;
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
        return "Tier: " + tier.getTextColor() + tier.getRank()+"\n"+"Mode: "+getModeString(stack)+"\n"+"Energy: "+ TextFormatting.RED+stack.getData().getInteger("energy")+"/"+getCapacity(stack)+TextFormatting.WHITE;
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
    public boolean onBlockDestroyed(World world, ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving) {
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
    public FluidStack getCurrentFluid(ItemStack stack) {
        return new FluidStack(SIBlocks.energyFlowing,getCapacity(stack));
    }

    @Override
    public void setCurrentFluid(FluidStack fluidStack, ItemStack stack) {

    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack) {
        if(fluidStack == null){
            return null;
        }
        if(fluidStack.getLiquid() == SIBlocks.energyFlowing){
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("energy");
            int amount = fluidStack.amount;
            if(remaining == 0){
                return null;
            }
            if(amount > remaining){
                fluidStack.amount -= remaining;
                CompoundTag data = new CompoundTag();
                data.putInt("energy",getCapacity(stack));
                stack.setData(data);
                return stack;
            } else {
                CompoundTag data = new CompoundTag();
                data.putInt("energy",saturation + amount);
                fluidStack.amount -= amount;
                stack.setData(data);
                return stack;
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
            int saturation = stack.getData().getInteger("energy");
            int amount = Math.min(fluidStack.amount,maxAmount);
            if(remaining == 0) return null;
            int result = Math.min(amount,remaining);
            if(result == 0) return null;
            fluidStack.amount -= result;
            CompoundTag data = new CompoundTag();
            data.putInt("energy",saturation+result);
            stack.setData(data);
            return stack;
        }
        return null;
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, ItemInventoryFluid inv) {
        return fill(fluidStack,stack);
    }


    @Override
    public void drain(ItemStack stack, int slot, TileEntityFluidContainer tile) {}

    @Override
    public void drain(ItemStack stack, int slot, IFluidInventory tile) {}

    @Override
    public void drain(ItemStack stack, int slot, ItemInventoryFluid inv) {}

    @Override
    public FluidStack drain(ItemStack stack, int amount) {
        return null;
    }

}
