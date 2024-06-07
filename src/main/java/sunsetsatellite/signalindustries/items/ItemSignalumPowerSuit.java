package sunsetsatellite.signalindustries.items;


import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.IInjectable;
import sunsetsatellite.signalindustries.interfaces.mixins.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.powersuit.InventoryPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.catalyst.fluids.util.NBTHelper;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemSignalumPowerSuit extends ItemArmorTiered implements IHasOverlay, IInjectable {
    public ItemSignalumPowerSuit(String key, int id, ArmorMaterial material, int armorPiece, Tier tier) {
        super(key, id, material, armorPiece, tier);
    }

    @Override
    public void renderOverlay(GuiIngame guiIngame, EntityPlayer player, int height, int width, int mouseX, int mouseY, FontRenderer fontRenderer, ItemEntityRenderer itemRenderer) {
        SignalumPowerSuit ps = ((IPlayerPowerSuit)player).getPowerSuit();
        if(ps != null && armorPiece == 0){
            ps.renderOverlay(guiIngame, fontRenderer, itemRenderer, player, height, width, mouseX, mouseY);
        }
    }

    @Override
    public void renderOverlay(ItemStack stack, SignalumPowerSuit signalumPowerSuit, GuiIngame guiIngame, EntityPlayer player, int height, int width, int mouseX, int mouseY, FontRenderer fontRenderer, ItemEntityRenderer itemRenderer) {

    }

    @Override
    public void fill(FluidStack fluidStack, ItemStack stack, TileEntityFluidContainer tile, int maxAmount) {
        InventoryPowerSuit inv = new InventoryPowerSuit(stack);
        NBTHelper.loadInvFromNBT(stack,inv,8,1);
        inv.insertFluid(0,fluidStack.splitStack(Math.min(maxAmount,fluidStack.amount)));
        NBTHelper.saveInvToNBT(stack,inv);
    }

    @Override
    public boolean canFill(ItemStack stack) {
        if(stack.getItem() == SIItems.signalumPowerSuitChestplate){
            InventoryPowerSuit inv = new InventoryPowerSuit(stack);
            NBTHelper.loadInvFromNBT(stack,inv,8,1);
            return inv.getFluidAmountForSlot(0) < inv.getFluidCapacityForSlot(0);
        }
        return false;
    }
}
