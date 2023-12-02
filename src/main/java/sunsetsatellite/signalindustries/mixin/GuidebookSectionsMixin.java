package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.gui.guidebook.GuidebookSections;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.gui.guidebook.SignalIndustriesSection;

@Mixin(
        value = GuidebookSections.class,
        remap = false
)
public class GuidebookSectionsMixin {

    @Inject(
            method = "init", at = @At("TAIL")
    )
    private static void init(CallbackInfo ci){
        GuidebookSections.register(new SignalIndustriesSection());
    }
}
