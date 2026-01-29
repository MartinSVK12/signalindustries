package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.data.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.dim.custom.DimensionRegistries;

@Mixin(value = Registries.class, remap = false)
public class RegistriesMixin {

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/data/registry/Registry;register(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 0, shift = At.Shift.BEFORE))
    private void init(CallbackInfo ci) {
        DimensionRegistries.init();
    }

}
