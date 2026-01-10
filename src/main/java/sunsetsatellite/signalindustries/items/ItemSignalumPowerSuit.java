package sunsetsatellite.signalindustries.items;


import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.entity.EntityRendererItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.IInjectable;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.powersuit.InventoryPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuitClient;
import sunsetsatellite.signalindustries.util.InventorySerializer;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemSignalumPowerSuit extends ItemArmorTiered implements IHasOverlay, IInjectable {
    public ItemSignalumPowerSuit(String translationKey, String namespaceId, int id, ArmorMaterial material, int armorPiece, Tier tier) {
        super(translationKey, namespaceId, id, material, armorPiece, tier);
    }

    @Override
    public void fill(FluidStack fluidStack, ItemStack stack, TileEntityFluidContainer tile, int maxAmount) {
        InventoryPowerSuit inv = new InventoryPowerSuit(stack);
        InventorySerializer.loadInvFromNBT(stack, inv, 8, 1);
        inv.insertFluid(0, fluidStack.splitStack(Math.min(maxAmount, fluidStack.amount)));
        InventorySerializer.saveInvToNBT(stack, inv);
    }

    @Override
    public boolean canFill(ItemStack stack) {
        if (stack.getItem().equals(SIItems.signalumPowerSuitChestplate)) {
            InventoryPowerSuit inv = new InventoryPowerSuit(stack);
            InventorySerializer.loadInvFromNBT(stack, inv, 8, 1);
            FluidStack fluidStack = inv.getFluidInSlot(0);
            if (fluidStack == null) return true;
            return fluidStack.amount < inv.getFluidCapacityForSlot(0);
        }
        return false;
    }

    @Override
    public void renderOverlay(HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Font fontRenderer, EntityRendererItem itemRenderer) {
        IPowerSuit ps = ((IPlayerPowerSuit<?>) player).getPowerSuit();
        if (ps instanceof SignalumPowerSuitClient && getArmorPiece() == 3) {
            ((SignalumPowerSuitClient) ps).renderOverlay(guiIngame, player, height, width, mouseX, mouseY, fontRenderer, itemRenderer);
        }
    }

    @Override
    public void renderOverlay(ItemStack stack, IPowerSuit signalumPowerSuit, HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Font fontRenderer, EntityRendererItem itemRenderer) {

    }
}
