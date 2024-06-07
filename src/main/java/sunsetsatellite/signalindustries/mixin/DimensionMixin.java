package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.world.Dimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.SIWorldTypes;
import sunsetsatellite.signalindustries.SignalIndustries;

@Mixin(value = Dimension.class, remap = false)
public class DimensionMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addDimension(CallbackInfo ci){

    }
}
