package sunsetsatellite.signalindustries.items;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.IntTag;
import com.mojang.nbt.Tag;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.abilities.trigger.TriggerBaseEffectAbility;
import sunsetsatellite.signalindustries.containers.ContainerHarness;
import sunsetsatellite.signalindustries.gui.GuiHarness;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.IInjectable;
import sunsetsatellite.signalindustries.inventories.item.InventoryHarness;
import sunsetsatellite.signalindustries.items.applications.ItemTrigger;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.catalyst.fluids.util.NBTHelper;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Collection;
import java.util.Objects;

public class ItemSignalumPrototypeHarness extends ItemArmorTiered implements IHasOverlay, IInjectable {


    public ItemSignalumPrototypeHarness(String key, int id, ArmorMaterial material, int armorPiece, Tier tier) {
        super(key, id, material, armorPiece, tier);
    }

    @Override
    public boolean onUseItemOnBlock(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        if(entityplayer.isSneaking()){
            SignalIndustries.displayGui(entityplayer,() -> new GuiHarness(entityplayer.inventory,entityplayer.inventory.getCurrentItem()),new ContainerHarness(entityplayer.inventory,entityplayer.inventory.getCurrentItem()),new InventoryHarness(entityplayer.inventory.getCurrentItem()),itemstack);
            return true;
        }
        return false;
    }

    public CompoundTag getFluidStack(int id, ItemStack stack){
        return stack.getData().getCompound("fluidInventory").getCompound(String.valueOf(id));
    }

    @Override
    public void renderOverlay(GuiIngame guiIngame, EntityPlayer player, int height, int width, int mouseX, int mouseY, FontRenderer fontRenderer, ItemEntityRenderer itemRenderer) {
        InventoryPlayer inv = player.inventory;
        ItemStack armor = inv.armorItemInSlot(2);
        if(armor != null && armor.getItem() instanceof ItemSignalumPrototypeHarness){
            int i = height - 64;
            fontRenderer.drawStringWithShadow( "S. P. Harness", 4, i += 16, 0xFFFF0000);
            if (inv.getCurrentItem() != null && inv.getCurrentItem().getItem() instanceof ItemTrigger){
                ItemStack trigger = inv.getCurrentItem();
                ItemTrigger itemTrigger = (ItemTrigger) trigger.getItem();
                if(!Objects.equals(trigger.getData().getString("ability"), "")){
                    if(armor.getData().getInteger("cooldown_"+trigger.getData().getString("ability")) <= 0 && armor.getData().getInteger("effectTime_"+trigger.getData().getString("ability")) <= 0){
                        fontRenderer.drawStringWithShadow( "Ability: ", 4, i += 16, 0xFFFFFFFF);
                        fontRenderer.drawStringWithShadow( itemTrigger.getAbility(trigger).name+ TextFormatting.LIME+" READY", 4 + fontRenderer.getStringWidth("Ability: "), i, 0xFFFF0000);
                        fontRenderer.drawStringWithShadow( "Energy: ", 4, i += 10, 0xFFFFFFFF);
                        fontRenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPrototypeHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount"))+TextFormatting.RED+"-"+ ItemTrigger.abilities.get(trigger.getData().getString("ability")).cost, 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
                    } else if(armor.getData().getInteger("effectTime_"+trigger.getData().getString("ability")) > 0){
                        fontRenderer.drawStringWithShadow( "Ability: ", 4, i += 16, 0xFFFFFFFF);
                        fontRenderer.drawStringWithShadow( TextFormatting.ORANGE + itemTrigger.getAbility(trigger).name + TextFormatting.LIME+" "+armor.getData().getInteger("effectTime_"+trigger.getData().getString("ability"))+"s", 4 + fontRenderer.getStringWidth("Ability: "), i, 0xFFFF0000);
                        fontRenderer.drawStringWithShadow( "Energy: ", 4, i += 10, 0xFFFFFFFF);
                        fontRenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPrototypeHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount"))+TextFormatting.LIME+"-"+ ((TriggerBaseEffectAbility) ItemTrigger.abilities.get(trigger.getData().getString("ability"))).costPerTick, 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
                    } else {
                        fontRenderer.drawStringWithShadow( "Ability: ", 4, i += 16, 0xFFFFFFFF);
                        fontRenderer.drawStringWithShadow( itemTrigger.getAbility(trigger).name+ TextFormatting.RED+" "+armor.getData().getInteger("cooldown_"+trigger.getData().getString("ability"))+"s", 4 + fontRenderer.getStringWidth("Ability: "), i, 0xFF808080);
                        fontRenderer.drawStringWithShadow( "Energy: ", 4, i += 10, 0xFFFFFFFF);
                        fontRenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPrototypeHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount"))+TextFormatting.RED+"-"+ ItemTrigger.abilities.get(trigger.getData().getString("ability")).cost, 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
                    }
                } else {
                    fontRenderer.drawStringWithShadow( "Energy: ", 4, i += 16, 0xFFFFFFFF);
                    fontRenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPrototypeHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount")), 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
                }
            } else {
                fontRenderer.drawStringWithShadow( "Energy: ", 4, i += 16, 0xFFFFFFFF);
                fontRenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPrototypeHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount")), 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
            }
        }
    }

    @Override
    public void renderOverlay(ItemStack stack, SignalumPowerSuit signalumPowerSuit, GuiIngame guiIngame, EntityPlayer player, int height, int width, int mouseX, int mouseY, FontRenderer fontRenderer, ItemEntityRenderer itemRenderer) {

    }


    public int cooldownTicks = 0;


    @Override
    public void inventoryTick(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
        super.inventoryTick(itemstack, world, entity, i, flag);
        cooldownTicks++;
        if(cooldownTicks >= 20){
            cooldownTicks = 0;
            Collection<Tag<?>> tags = itemstack.getData().getValues();
            tags.forEach((NBT)->{
                if(NBT instanceof IntTag){
                    if(NBT.getTagName().contains("cooldown_")){
                        if(((IntTag) NBT).getValue() > 0){
                            ((IntTag) NBT).setValue(((IntTag) NBT).getValue()-1);
                        }
                    } else if (NBT.getTagName().contains("effectTime_")) {
                        if(((IntTag) NBT).getValue() > 0){
                            ((IntTag) NBT).setValue(((IntTag) NBT).getValue()-1);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void fill(FluidStack fluidStack, ItemStack stack, TileEntityFluidContainer tile, int maxAmount) {
        InventoryHarness inv = new InventoryHarness(stack);
        NBTHelper.loadInvFromNBT(stack,inv,0,1);
        inv.insertFluid(0,fluidStack.splitStack(Math.min(maxAmount,fluidStack.amount)));
        NBTHelper.saveInvToNBT(stack,inv);
    }

    @Override
    public boolean canFill(ItemStack stack) {
        if(stack.getItem() == SIItems.signalumPrototypeHarness){
            InventoryHarness inv = new InventoryHarness(stack);
            NBTHelper.loadInvFromNBT(stack,inv,0,1);
            return inv.getFluidAmountForSlot(0) < inv.getFluidCapacityForSlot(0);
        }
        return false;
    }
}
