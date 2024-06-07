package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.mixins.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.ItemSmartWatch;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

@Debug(
        export = true
)
@Mixin(
        value = GuiIngame.class,
        remap = false
)
public abstract class GuiIngameMixin extends Gui {

    @Shadow
    protected FontRenderer fontrenderer;

    @Shadow
    protected Minecraft mc;

    @Shadow
    public int updateCounter;


    @Shadow
    public static ItemEntityRenderer itemRenderer;

    @Inject(
            method = "renderGameOverlay",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/client/render/WorldRenderer;setupScaledResolution()V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void renderAfterGameOverlay(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci, I18n stringtranslate, int width, int height, int sp, FontRenderer fontrenderer) {
        ItemStack headSlotItem = this.mc.thePlayer.inventory.armorItemInSlot(3);
        if(headSlotItem != null){
            if(headSlotItem.getItem().id == SIItems.signalumPrototypeHarnessGoggles.id){
                InventoryPlayer inv = this.mc.thePlayer.inventory;
                if(this.mc.thePlayer.inventory.getCurrentItem() != null) {
                    if(this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof IHasOverlay){
                        ((IHasOverlay)inv.getCurrentItem().getItem()).renderOverlay((GuiIngame) ((Object)this),this.mc.thePlayer,height,width,mouseX,mouseY,fontrenderer, itemRenderer);
                    }
                }
                if (inv.armorItemInSlot(2) != null && inv.armorItemInSlot(2).getItem() instanceof IHasOverlay) {
                    ((IHasOverlay)inv.armorItemInSlot(2).getItem()).renderOverlay((GuiIngame) ((Object)this),this.mc.thePlayer,height,width,mouseX,mouseY,fontrenderer, itemRenderer);
                }
            } else if (headSlotItem.getItem() instanceof IHasOverlay) {
                InventoryPlayer inv = this.mc.thePlayer.inventory;
                ((IHasOverlay)inv.armorItemInSlot(3).getItem()).renderOverlay((GuiIngame) ((Object)this),this.mc.thePlayer,height,width,mouseX,mouseY,fontrenderer, itemRenderer);
            }
        }
    }

    @Inject(method = "renderGameOverlay", at = @At(value = "FIELD", target = "Lnet/minecraft/core/item/ItemStack;itemID:I", ordinal = 1))
    public void smartwatchEnableText(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci, @Local(name = "item") ItemStack stack, @Local(name = "clock") LocalBooleanRef clock, @Local(name = "compass") LocalBooleanRef compass, @Local(name = "rotaryCalendar") LocalBooleanRef calendar){
        if(stack != null && stack.getItem() instanceof ItemSmartWatch){
            clock.set(true);
            compass.set(true);
            calendar.set(true);
        }
        EntityPlayer player = Minecraft.getMinecraft(this).thePlayer;
        SignalumPowerSuit powerSuit = ((IPlayerPowerSuit) player).getPowerSuit();
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
