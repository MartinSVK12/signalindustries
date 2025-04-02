package sunsetsatellite.signalindustries.items;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.IntTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.entity.EntityRendererItem;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.abilities.trigger.TriggerBaseEffectAbility;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.IInjectable;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.invs.InventoryHarness;
import sunsetsatellite.signalindustries.items.applications.ItemTrigger;
import sunsetsatellite.signalindustries.util.InventorySerializer;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Collection;
import java.util.Objects;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class ItemSignalumPowerHarness extends ItemArmorTiered implements IHasOverlay, IInjectable {


    public ItemSignalumPowerHarness(String translationKey, String namespaceId, int id, ArmorMaterial material, int armorPiece, Tier tier) {
        super(translationKey, namespaceId, id, material, armorPiece, tier);
    }

    @Override
    public boolean onUseItemOnBlock(ItemStack itemstack, Player player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        if(!itemstack.getItem().equals(SIItems.signalumPrototypeHarness)) return false;
        if(player.isSneaking()){
            Catalyst.displayGui(player, new InventoryHarness(itemstack), player.inventory.getCurrentItemIndex(), false, key("gui/harness"));
            //SignalIndustries.displayGui(player,() -> new GuiHarness(player.inventory,player.inventory.getCurrentItem()),new ContainerHarness(player.inventory,player.inventory.getCurrentItem()),new InventoryHarness(player.inventory.getCurrentItem()),itemstack);
            return true;
        }
        return false;
    }

    public CompoundTag getFluidStack(int id, ItemStack stack){
        return stack.getData().getCompound("fluidInventory").getCompound(String.valueOf(id));
    }

    @Override
    public void renderOverlay(HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Font fontRenderer, EntityRendererItem itemRenderer) {
        ContainerInventory inv = player.inventory;
        ItemStack armor = inv.armorItemInSlot(2);
        if(armor != null && armor.getItem() instanceof ItemSignalumPowerHarness){
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
                        fontRenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPowerHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount"))+TextFormatting.RED+"-"+ ItemTrigger.abilities.get(trigger.getData().getString("ability")).cost, 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
                    } else if(armor.getData().getInteger("effectTime_"+trigger.getData().getString("ability")) > 0){
                        fontRenderer.drawStringWithShadow( "Ability: ", 4, i += 16, 0xFFFFFFFF);
                        fontRenderer.drawStringWithShadow( TextFormatting.ORANGE + itemTrigger.getAbility(trigger).name + TextFormatting.LIME+" "+armor.getData().getInteger("effectTime_"+trigger.getData().getString("ability"))+"s", 4 + fontRenderer.getStringWidth("Ability: "), i, 0xFFFF0000);
                        fontRenderer.drawStringWithShadow( "Energy: ", 4, i += 10, 0xFFFFFFFF);
                        fontRenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPowerHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount"))+TextFormatting.LIME+"-"+ ((TriggerBaseEffectAbility) ItemTrigger.abilities.get(trigger.getData().getString("ability"))).costPerTick, 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
                    } else {
                        fontRenderer.drawStringWithShadow( "Ability: ", 4, i += 16, 0xFFFFFFFF);
                        fontRenderer.drawStringWithShadow( itemTrigger.getAbility(trigger).name+ TextFormatting.RED+" "+armor.getData().getInteger("cooldown_"+trigger.getData().getString("ability"))+"s", 4 + fontRenderer.getStringWidth("Ability: "), i, 0xFF808080);
                        fontRenderer.drawStringWithShadow( "Energy: ", 4, i += 10, 0xFFFFFFFF);
                        fontRenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPowerHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount"))+TextFormatting.RED+"-"+ ItemTrigger.abilities.get(trigger.getData().getString("ability")).cost, 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
                    }
                } else {
                    fontRenderer.drawStringWithShadow( "Energy: ", 4, i += 16, 0xFFFFFFFF);
                    fontRenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPowerHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount")), 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
                }
            } else {
                fontRenderer.drawStringWithShadow( "Energy: ", 4, i += 16, 0xFFFFFFFF);
                fontRenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPowerHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount")), 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
            }
        }
    }

    @Override
    public void renderOverlay(ItemStack stack, IPowerSuit signalumPowerSuit, HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Font fontRenderer, EntityRendererItem itemRenderer) {

    }


    public int cooldownTicks = 0;


    @Override
    public void inventoryTick(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
        super.inventoryTick(itemstack, world, entity, i, flag);
        if(!itemstack.getItem().equals(SIItems.signalumPrototypeHarness)) return;
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
        if(!stack.getItem().equals(SIItems.signalumPrototypeHarness)) return;
        InventoryHarness inv = new InventoryHarness(stack);
        InventorySerializer.loadInvFromNBT(stack,inv,0,1);
        inv.insertFluid(0,fluidStack.splitStack(Math.min(maxAmount,fluidStack.amount)));
        InventorySerializer.saveInvToNBT(stack,inv);
    }

    @Override
    public boolean canFill(ItemStack stack) {
        if(stack.getItem().equals(SIItems.signalumPrototypeHarness)){
            InventoryHarness inv = new InventoryHarness(stack);
            InventorySerializer.loadInvFromNBT(stack,inv,0,1);
            return (inv.getFluidInSlot(0) == null ? 0 : inv.getFluidInSlot(0).amount) < inv.getFluidCapacityForSlot(0);
        }
        return false;
    }
}
