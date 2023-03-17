package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SignalIndustries;

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
                drawCenteredString(fontrenderer,"No compatible equipment equipped.",width/2,4,0xFFFF0000);
            }
        }
    }

}
