package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.entity.EntityRendererItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.applications.ItemSmartWatch;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuitClient;

@Mixin(value = HudIngame.class,remap = false)
public class HudIngameMixin {

    @Shadow protected Minecraft mc;

    @Shadow protected Font font;

    @Shadow public static EntityRendererItem itemRenderer;

    @Inject(
            method = "renderGameOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;setupScaledResolution()V", shift = At.Shift.AFTER)
    )
    public void renderAfterGameOverlay(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci) {
        ItemStack headSlotItem = this.mc.thePlayer.inventory.armorItemInSlot(3);
        int width = this.mc.resolution.getScaledWidthScreenCoords();
        int height = this.mc.resolution.getScaledHeightScreenCoords();
        if(headSlotItem != null){
            if(headSlotItem.getItem().id == SIItems.signalumPrototypeHarnessGoggles.id){
                ContainerInventory inv = this.mc.thePlayer.inventory;
                if(this.mc.thePlayer.inventory.getCurrentItem() != null) {
                    if(this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof IHasOverlay){
                        ((IHasOverlay)inv.getCurrentItem().getItem()).renderOverlay((HudIngame) ((Object)this),this.mc.thePlayer,height,width,mouseX,mouseY,font, itemRenderer);
                    }
                }
                if (inv.armorItemInSlot(2) != null && inv.armorItemInSlot(2).getItem() instanceof IHasOverlay) {
                    ((IHasOverlay)inv.armorItemInSlot(2).getItem()).renderOverlay((HudIngame) ((Object)this),this.mc.thePlayer,height,width,mouseX,mouseY,font, itemRenderer);
                }
            } else if (headSlotItem.getItem() instanceof IHasOverlay) {
                ContainerInventory inv = this.mc.thePlayer.inventory;
                ((IHasOverlay)inv.armorItemInSlot(3).getItem()).renderOverlay((HudIngame) ((Object)this),this.mc.thePlayer,height,width,mouseX,mouseY,font, itemRenderer);
            }
        }
    }

    @Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/container/ContainerInventory;getItem(I)Lnet/minecraft/core/item/ItemStack;", ordinal = 0))
    public void smartwatchEnableText(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci, @Local(name = "iinv") int iinv, @Local(name = "clock") LocalBooleanRef clock, @Local(name = "compass") LocalBooleanRef compass, @Local(name = "rotaryCalendar") LocalBooleanRef calendar){
        ItemStack stack = mc.thePlayer.inventory.getItem(iinv);
        if(stack != null && stack.getItem() instanceof ItemSmartWatch){
            clock.set(true);
            compass.set(true);
            calendar.set(true);
        }
        Player player = mc.thePlayer;
        SignalumPowerSuit powerSuit = ((IPlayerPowerSuit<SignalumPowerSuitClient>) player).getPowerSuit();
        if(powerSuit != null && powerSuit.active && powerSuit.module != null){
            for (ItemStack content : powerSuit.module.contents) {
                if(content != null && content.getItem() instanceof ItemSmartWatch){
                    clock.set(true);
                    compass.set(true);
                    calendar.set(true);
                }
            }
        }
    }

}
