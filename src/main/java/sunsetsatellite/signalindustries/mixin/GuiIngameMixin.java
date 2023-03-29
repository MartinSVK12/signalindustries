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
import sunsetsatellite.signalindustries.items.ItemPulsar;

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
                if(this.mc.thePlayer.inventory.getCurrentItem() != null && this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemPulsar){
                    ItemStack pulsar = this.mc.thePlayer.inventory.getCurrentItem();
                    int i = height-64;
                    drawString(fontrenderer,"The Pulsar",4,i+=16,0xFFFF0000);
                    drawString(fontrenderer,"Ability: ",4,i+=16,0xFFFFFFFF);
                    drawString(fontrenderer,((ItemPulsar)pulsar.getItem()).getAbility(pulsar),4+fontrenderer.getStringWidth("Ability: "),i,0xFFFF0000);
                    drawString(fontrenderer,"Charge: ",4,i+=10,0xFFFFFFFF);
                    drawString(fontrenderer, String.valueOf(pulsar.tag.getByte("charge"))+"%",4+fontrenderer.getStringWidth("Charge: "),i,pulsar.tag.getByte("charge") >= 100 ? 0xFFFF0000 : 0xFFFFFFFF);
                    drawString(fontrenderer,"Energy: ",4,i+=10,0xFFFFFFFF);
                    drawString(fontrenderer, String.valueOf(((ItemPulsar)pulsar.getItem()).getFluidStack(0,pulsar).getInteger("amount")),4+fontrenderer.getStringWidth("Energy: "),i,0xFFFF8080);
                }
            }
        }
    }

}
