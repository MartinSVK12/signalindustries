package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.world.Dimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SignalIndustries;

@Mixin(value = Dimension.class, remap = false)
public class DimensionMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addDimension(CallbackInfo ci){
        SignalIndustries.dimEternity = new Dimension(SignalIndustries.key("eternity"),Dimension.overworld,1,SignalIndustries.portalEternity.id).setDefaultWorldType(SignalIndustries.eternityWorld);
        Dimension.registerDimension(SignalIndustries.config.getInt("Other.eternityDimId"),SignalIndustries.dimEternity);
    }
}
