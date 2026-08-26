package sunsetsatellite.signalindustries.items;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.IntTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.render.entity.EntityRendererItem;
import net.minecraft.client.render.font.FontRenderer;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
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
import sunsetsatellite.signalindustries.items.base.ItemArmorTiered;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.InventorySerializer;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Collection;
import java.util.Objects;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class ItemSignalumPowerHarness extends ItemArmorTiered implements IHasOverlay, IInjectable {


    public ItemSignalumPowerHarness(String translationKey, String namespaceId, int id, ArmorMaterial material, HumanArmorShape armorPiece, Tier tier) {
        super(translationKey, namespaceId, id, material, armorPiece, tier);
    }

	@Override
	public boolean onUseOnBlock(@NotNull ItemStack stack, @NotNull World world, @Nullable Player player, @NotNull TilePosc blockPos, @NotNull Side side, double xHit, double yHit) {
		if (!stack.getItem().equals(SIItems.signalumPrototypeHarness)) return false;
		if (player.isSneaking()) {
			Catalyst.displayGui(player, new InventoryHarness(stack), player.inventory.getCurrentSlot(), false, key("gui/harness"));
			//SignalIndustries.displayGui(player,() -> new GuiHarness(player.inventory,player.inventory.getCurrentItem()),new ContainerHarness(player.inventory,player.inventory.getCurrentItem()),new InventoryHarness(player.inventory.getCurrentItem()),itemstack);
			return true;
		}
		return false;
	}

    public CompoundTag getFluidStack(int id, ItemStack stack) {
        return stack.getData().getCompound("fluidInventory").getCompound(String.valueOf(id));
    }

    public void setFluidStack(int id, ItemStack stack, CompoundTag fluidStack) {
        stack.getData().getCompound("fluidInventory").putCompound(String.valueOf(id), fluidStack);
    }

    @Override
    public void renderOverlay(HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Gui gui, FontRenderer fontRenderer, EntityRendererItem itemRenderer) {
        ContainerInventory inv = player.inventory;
        ItemStack armor = inv.armorItemInSlot(HumanArmorShape.CHEST);
        if (armor != null && armor.getItem() instanceof ItemSignalumPowerHarness) {
            int i = height - 64;
            gui.drawStringShadow(fontRenderer,"S. P. Harness", 4, i += 16, 0xFFFF0000);
            if (inv.getCurrentItem() != null && inv.getCurrentItem().getItem() instanceof ItemTrigger) {
                ItemStack trigger = inv.getCurrentItem();
                ItemTrigger itemTrigger = (ItemTrigger) trigger.getItem();
                if (!Objects.equals(trigger.getData().getString("ability"), "") && itemTrigger.getAbility(trigger) != null) {
                    if (armor.getData().getInteger("cooldown_" + trigger.getData().getString("ability")) <= 0 && armor.getData().getInteger("effectTime_" + trigger.getData().getString("ability")) <= 0) {
                        gui.drawStringShadow(fontRenderer,"Ability: ", 4, i += 16, 0xFFFFFFFF);
                        gui.drawStringShadow(fontRenderer,itemTrigger.getAbility(trigger).name + TextFormatting.LIME + " READY", 4 + fontRenderer.stringWidth("Ability: "), i, 0xFFFF0000);
                        gui.drawStringShadow(fontRenderer,"Energy: ", 4, i += 10, 0xFFFFFFFF);
                        gui.drawStringShadow(fontRenderer,String.valueOf(((ItemSignalumPowerHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount")) + TextFormatting.RED + "-" + ItemTrigger.abilities.get(trigger.getData().getString("ability")).cost, 4 + fontRenderer.stringWidth("Energy: "), i, 0xFFFF8080);
                    } else if (armor.getData().getInteger("effectTime_" + trigger.getData().getString("ability")) > 0) {
                        gui.drawStringShadow(fontRenderer,"Ability: ", 4, i += 16, 0xFFFFFFFF);
                        gui.drawStringShadow(fontRenderer,TextFormatting.ORANGE + itemTrigger.getAbility(trigger).name + TextFormatting.LIME + " " + armor.getData().getInteger("effectTime_" + trigger.getData().getString("ability")) + "s", 4 + fontRenderer.stringWidth("Ability: "), i, 0xFFFF0000);
                        gui.drawStringShadow(fontRenderer,"Energy: ", 4, i += 10, 0xFFFFFFFF);
                        gui.drawStringShadow(fontRenderer,String.valueOf(((ItemSignalumPowerHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount")) + TextFormatting.LIME + "-" + ((TriggerBaseEffectAbility) ItemTrigger.abilities.get(trigger.getData().getString("ability"))).costPerTick, 4 + fontRenderer.stringWidth("Energy: "), i, 0xFFFF8080);
                    } else {
                        gui.drawStringShadow(fontRenderer,"Ability: ", 4, i += 16, 0xFFFFFFFF);
                        gui.drawStringShadow(fontRenderer,itemTrigger.getAbility(trigger).name + TextFormatting.RED + " " + armor.getData().getInteger("cooldown_" + trigger.getData().getString("ability")) + "s", 4 + fontRenderer.stringWidth("Ability: "), i, 0xFF808080);
                        gui.drawStringShadow(fontRenderer,"Energy: ", 4, i += 10, 0xFFFFFFFF);
                        gui.drawStringShadow(fontRenderer,String.valueOf(((ItemSignalumPowerHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount")) + TextFormatting.RED + "-" + ItemTrigger.abilities.get(trigger.getData().getString("ability")).cost, 4 + fontRenderer.stringWidth("Energy: "), i, 0xFFFF8080);
                    }
                } else {
                    gui.drawStringShadow(fontRenderer,"Energy: ", 4, i += 16, 0xFFFFFFFF);
                    gui.drawStringShadow(fontRenderer,String.valueOf(((ItemSignalumPowerHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount")), 4 + fontRenderer.stringWidth("Energy: "), i, 0xFFFF8080);
                }
            } else {
                gui.drawStringShadow(fontRenderer,"Energy: ", 4, i += 16, 0xFFFFFFFF);
                gui.drawStringShadow(fontRenderer,String.valueOf(((ItemSignalumPowerHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount")), 4 + fontRenderer.stringWidth("Energy: "), i, 0xFFFF8080);
            }
        }
    }

	@Override
	public void renderOverlay(ItemStack stack, IPowerSuit signalumPowerSuit, HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Gui gui, FontRenderer fontRenderer, EntityRendererItem itemRenderer) {

	}

	public int cooldownTicks = 0;


    @Override
    public void inventoryTick(@NonNull ItemStack itemstack, @NonNull World world, @NonNull Entity entity, int i, boolean flag) {
        super.inventoryTick(itemstack, world, entity, i, flag);
        if (!itemstack.getItem().equals(SIItems.signalumPrototypeHarness)) return;
        cooldownTicks++;
        if (cooldownTicks >= 20) {
            cooldownTicks = 0;
            Collection<Tag<?>> tags = itemstack.getData().getValues();
            tags.forEach((NBT) -> {
                if (NBT instanceof IntTag) {
                    if (NBT.getTagName().contains("cooldown_")) {
                        if (((IntTag) NBT).getValue() > 0) {
                            ((IntTag) NBT).setValue(((IntTag) NBT).getValue() - 1);
                        }
                    } else if (NBT.getTagName().contains("effectTime_")) {
                        if (((IntTag) NBT).getValue() > 0) {
                            ((IntTag) NBT).setValue(((IntTag) NBT).getValue() - 1);
                        }
                    }
                }
            });
        }
        if (itemstack.isItemDamaged() && getEnergy(itemstack) > 0) {
            if (decrementEnergy(itemstack, 1)) {
				//repair item
				if (itemstack.isItemStackDamageable()) {
					if (itemstack.getMetadata() <= itemstack.getMaxDamage() && itemstack.getMetadata() >= 0) {
						itemstack.setMetadata(itemstack.getMetadata()-i);
					}
				}
            }
        }
    }

    public int getEnergy(ItemStack armor) {
        return ((ItemSignalumPowerHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount");
    }

    public boolean decrementEnergy(ItemStack armor, int amount) {
        if (getEnergy(armor) < amount) return false;
        CompoundTag fluidStack = getFluidStack(0, armor);
        fluidStack.putInt("amount", fluidStack.getInteger("amount") - amount);
        setFluidStack(0, armor, fluidStack);
        return true;
    }

    @Override
    public void fill(FluidStack fluidStack, ItemStack stack, TileEntityFluidContainer tile, int maxAmount) {
        if (!stack.getItem().equals(SIItems.signalumPrototypeHarness)) return;
        InventoryHarness inv = new InventoryHarness(stack);
        InventorySerializer.loadInvFromNBT(stack, inv, 0, 1);
        inv.insertFluid(0, fluidStack.splitStack(Math.min(maxAmount, fluidStack.amount)));
        InventorySerializer.saveInvToNBT(stack, inv);
    }

    @Override
    public boolean canFill(ItemStack stack) {
        if (stack.getItem().equals(SIItems.signalumPrototypeHarness)) {
            InventoryHarness inv = new InventoryHarness(stack);
            InventorySerializer.loadInvFromNBT(stack, inv, 0, 1);
            return (inv.getFluidInSlot(0) == null ? 0 : inv.getFluidInSlot(0).amount) < inv.getFluidCapacityForSlot(0);
        }
        return false;
    }
}
