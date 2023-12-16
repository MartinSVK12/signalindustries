package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.gui.guidebook.GuiGuidebook;
import net.minecraft.client.gui.guidebook.PageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SignalIndustries;

@Mixin(value = GuiGuidebook.class,remap = false)
public abstract class GuiGuidebookMixin {

    @Shadow private static PageManager pageManager;

    @Shadow protected abstract void updateTabList();

    @Inject(method = "keyTyped",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/guidebook/PageManager;getLeftPage()Lnet/minecraft/client/gui/guidebook/GuidebookPage;",
                    shift = At.Shift.BEFORE,ordinal = 1),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    public void keyTyped(char c, int key, int mouseX, int mouseY, CallbackInfo ci, int xOffset, int top, int left) {
        if (pageManager.getLeftPage() != null) {
            pageManager.getLeftPage().keyTyped(c, key, left, top, mouseX, mouseY);
            this.updateTabList();
        }
        if (pageManager.getRightPage() != null) {
            pageManager.getRightPage().keyTyped(c, key, left + 158, top, mouseX, mouseY);
            this.updateTabList();
        }
        ci.cancel();
    }
}
