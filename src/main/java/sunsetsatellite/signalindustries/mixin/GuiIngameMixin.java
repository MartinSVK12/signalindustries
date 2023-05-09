package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.command.ChatColor;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.items.ItemPulsar;
import sunsetsatellite.signalindustries.items.ItemSignalumPrototypeHarness;
import sunsetsatellite.signalindustries.items.ItemTrigger;

import java.util.Objects;

@Debug(
        export = true
)
@Mixin(
        value = GuiIngame.class,
        remap = false
)
public class GuiIngameMixin extends Gui {

    @Shadow private FontRenderer fontrenderer;

    @Shadow private Minecraft mc;

    @Inject(
            method = "renderGameOverlay",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/src/EntityRenderer;setupScaledResolution()V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void renderGameOverlay(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci, StringTranslate stringtranslate, int width, int height, int sp, FontRenderer fontrenderer) {
        ItemStack headSlotItem = this.mc.thePlayer.inventory.armorItemInSlot(3);
        if(headSlotItem != null){
            if(headSlotItem.getItem().itemID == SignalIndustries.signalumPrototypeHarnessGoggles.itemID){
                InventoryPlayer inv = this.mc.thePlayer.inventory;
                if(this.mc.thePlayer.inventory.getCurrentItem() != null) {
                    if(this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof IHasOverlay){
                        ((IHasOverlay)inv.getCurrentItem().getItem()).renderOverlay(fontrenderer,this.mc.thePlayer,height,width,mouseX,mouseY);
                    }
                }
                if (inv.armorItemInSlot(2) != null && inv.armorItemInSlot(2).getItem() instanceof IHasOverlay) {
                    ((IHasOverlay)inv.armorItemInSlot(2).getItem()).renderOverlay(fontrenderer,this.mc.thePlayer,height,width,mouseX,mouseY);
                }
            }
        }
    }

}
