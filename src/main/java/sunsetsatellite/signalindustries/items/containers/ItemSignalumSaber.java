package sunsetsatellite.signalindustries.items.containers;


import com.mojang.nbt.CompoundTag;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolSword;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.impl.ItemInventoryFluid;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.interfaces.IVariableDamageWeapon;
import sunsetsatellite.signalindustries.items.ItemSignalumPrototypeHarness;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemSignalumSaber extends ItemToolSword implements ITiered, IItemFluidContainer, IVariableDamageWeapon, IHasOverlay {

    public Tier tier;

    public ItemSignalumSaber(String key, int i, Tier tier, ToolMaterial enumtoolmaterial) {
        super(key, i, enumtoolmaterial);
        this.tier = tier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getColor() + tier.getRank()+"\n"+"Energy: "+ TextFormatting.RED+stack.getData().getInteger("energy")+TextFormatting.WHITE;
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return 500;
    }

    @Override
    public int getRemainingCapacity(ItemStack stack) {
        return 500-stack.getData().getInteger("energy");
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
    public ItemStack fill(FluidStack fluidStack, ItemStack stack) {
        if(fluidStack == null){
            return null;
        }
        if(fluidStack.getLiquid() == SignalIndustries.energyFlowing){
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
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, TileEntityFluidContainer tile, int maxAmount) {
        if(fluidStack == null){
            return null;
        }
        if(fluidStack.getLiquid() == SignalIndustries.energyFlowing){
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
    public void drain(ItemStack stack, SlotFluid slot, TileEntityFluidContainer tile) {}

    @Override
    public void drain(ItemStack stack, SlotFluid slot, ItemInventoryFluid inv) {}

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLiving victim, EntityLiving attacker) {
        int energy = itemstack.getData().getInteger("energy");
        if(itemstack.getData().getBoolean("active")){
            if(energy > 0){
                itemstack.getData().putInt("energy",energy-1);
                victim.remainingFireTicks = 60;
            }
        }
        if(energy <= 0){
            itemstack.getData().putBoolean("active",false);
        }
        return true;
    }

    @Override
    public boolean onBlockDestroyed(World world, ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving) {
        Block block = Block.blocksList[i];
        int energy = itemstack.getData().getInteger("energy");
        if (block != null && block.getHardness() > 0.0F){
            if(energy > 1 && itemstack.getData().getBoolean("active")){
                itemstack.getData().putInt("energy",energy-2);
            }
        }
        return true;
    }

    @Override
    public int getIconIndex(ItemStack itemstack) {
        if(itemstack.getData().getBoolean("active")){
            return Item.iconCoordToIndex(SignalIndustries.saberTex[1][0],SignalIndustries.saberTex[1][1]);
        }
        return Item.iconCoordToIndex(SignalIndustries.saberTex[0][0],SignalIndustries.saberTex[0][1]);
    }

    @Override
    public int getDamageVsEntity(Entity entity, ItemStack stack) {
        if(stack.getData().getBoolean("active")){
            return 10;
        } else {
            return 4;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if(itemstack.getData().getInteger("energy") > 0){
            itemstack.getData().putBoolean("active",!itemstack.getData().getBoolean("active"));
        }
        return super.onItemRightClick(itemstack, world, entityplayer);
    }

    @Override
    public void renderOverlay(GuiIngame guiIngame, EntityPlayer player, int height, int width, int mouseX, int mouseY, FontRenderer fontRenderer, ItemEntityRenderer itemRenderer) {
        InventoryPlayer inv = player.inventory;
        ItemStack saber = inv.getCurrentItem();
        int i = (inv.armorItemInSlot(2) != null && inv.armorItemInSlot(2).getItem() instanceof ItemSignalumPrototypeHarness) ? height - 128 : height - 64;
        fontRenderer.drawStringWithShadow("Signalum Saber", 4, i += 16, 0xFFFF0000);
        fontRenderer.drawStringWithShadow("Energy: ", 4, i += 16, 0xFFFFFFFF);
        fontRenderer.drawStringWithShadow(String.valueOf(saber.getData().getInteger("energy")), 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
        fontRenderer.drawStringWithShadow(saber.getData().getBoolean("active") ? "Active" : "Inactive", 4, i +=10, saber.getData().getBoolean("active") ? 0xFF00FF00 : 0xFF808080);
    }

    @Override
    public void renderOverlay(ItemStack stack, SignalumPowerSuit signalumPowerSuit, GuiIngame guiIngame, EntityPlayer player, int height, int width, int mouseX, int mouseY, FontRenderer fontRenderer, ItemEntityRenderer itemRenderer) {

    }


    @Override
    public Tier getTier() {
        return tier;
    }
}
