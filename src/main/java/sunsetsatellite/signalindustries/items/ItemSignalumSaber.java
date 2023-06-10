package sunsetsatellite.signalindustries.items;

import net.minecraft.src.*;
import net.minecraft.src.command.ChatColor;
import net.minecraft.src.material.ToolMaterial;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.api.IItemFluidContainer;
import sunsetsatellite.fluidapi.api.ItemInventoryFluid;
import sunsetsatellite.fluidapi.api.SlotFluid;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidContainer;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.interfaces.IVariableDamageWeapon;
import sunsetsatellite.signalindustries.util.Tiers;

public class ItemSignalumSaber extends ItemToolSword implements ITiered, IItemFluidContainer, IVariableDamageWeapon, IHasOverlay {

    public Tiers tier;

    public ItemSignalumSaber(int i, Tiers tier, ToolMaterial enumtoolmaterial) {
        super(i, enumtoolmaterial);
        this.tier = tier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getColor() + tier.getRank()+"\n"+"Energy: "+ ChatColor.red+stack.tag.getInteger("energy")+ChatColor.white;
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return 500;
    }

    @Override
    public int getRemainingCapacity(ItemStack stack) {
        return 500-stack.tag.getInteger("energy");
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
    public boolean hitEntity(ItemStack itemstack, EntityLiving victim, EntityLiving attacker) {
        int energy = itemstack.tag.getInteger("energy");
        if(itemstack.tag.getBoolean("active")){
            if(energy > 0){
                itemstack.tag.setInteger("energy",energy-1);
                victim.fire = 60;
            }
        }
        if(energy <= 0){
            itemstack.tag.setBoolean("active",false);
        }
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving) {
        Block block = Block.blocksList[i];
        int energy = itemstack.tag.getInteger("energy");
        if (block != null && block.getHardness() > 0.0F){
            if(energy > 1 && itemstack.tag.getBoolean("active")){
                itemstack.tag.setInteger("energy",energy-2);
            }
        }
        return true;
    }

    @Override
    public int getIconIndex(ItemStack itemstack) {
        if(itemstack.tag.getBoolean("active")){
            return Item.iconCoordToIndex(SignalIndustries.saberTex[1][0],SignalIndustries.saberTex[1][1]);
        }
        return Item.iconCoordToIndex(SignalIndustries.saberTex[0][0],SignalIndustries.saberTex[0][1]);
    }

    @Override
    public ItemStack fill(SlotFluid slot, ItemStack stack) {
        if(slot.getFluidStack() == null){
            return null;
        }
        if(slot.getFluidStack().getLiquid() == SignalIndustries.energyFlowing){
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.tag.getInteger("energy");
            int amount = slot.getFluidStack().amount;
            //int size = stack.tag.getInteger("size");
            //ItemStack crystal = new ItemStack(SignalIndustries.signalumCrystal,1);
            if(remaining == 0){
                return null;
            }
            if(amount > remaining){
                slot.getFluidStack().amount -= remaining;
                if(slot.getFluidStack().amount <= 0){
                    slot.putStack(null);
                }
                NBTTagCompound data = new NBTTagCompound();
                //data.setInteger("size",size);
                data.setInteger("energy",getCapacity(stack));
                stack.tag = data;
                return stack;
            } else {
                slot.putStack(null);
                NBTTagCompound data = new NBTTagCompound();
                //data.setInteger("size",size);
                data.setInteger("energy",saturation + amount);
                stack.tag = data;
                return stack;
            }
        }
        return null;
    }

    @Override
    public ItemStack fill(int slotIndex, TileEntityFluidContainer tile, ItemStack stack) {
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

    @Override
    public int getDamageVsEntity(Entity entity, ItemStack stack) {
        if(stack.tag.getBoolean("active")){
            return 10;
        } else {
            return 4;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if(itemstack.tag.getInteger("energy") > 0){
            itemstack.tag.setBoolean("active",!itemstack.tag.getBoolean("active"));
        }
        return super.onItemRightClick(itemstack, world, entityplayer);
    }

    @Override
    public void renderOverlay(FontRenderer fontrenderer, EntityPlayer player, int height, int width, int mouseX, int mouseY) {
        InventoryPlayer inv = player.inventory;
        ItemStack saber = inv.getCurrentItem();
        int i = (inv.armorItemInSlot(2) != null && inv.armorItemInSlot(2).getItem() instanceof ItemSignalumPrototypeHarness) ? height - 128 : height - 64;
        fontrenderer.drawStringWithShadow("Signalum Saber", 4, i += 16, 0xFFFF0000);
        fontrenderer.drawStringWithShadow("Energy: ", 4, i += 16, 0xFFFFFFFF);
        fontrenderer.drawStringWithShadow(String.valueOf(saber.tag.getInteger("energy")), 4 + fontrenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
        fontrenderer.drawStringWithShadow(saber.tag.getBoolean("active") ? "Active" : "Inactive", 4, i +=10, saber.tag.getBoolean("active") ? 0xFF00FF00 : 0xFF808080);
    }
}
