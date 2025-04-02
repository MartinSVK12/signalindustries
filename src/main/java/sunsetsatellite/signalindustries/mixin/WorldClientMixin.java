package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.render.camera.ICamera;
import net.minecraft.client.world.WorldClient;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.SIWeather;

@Mixin(
        value = WorldClient.class,
        remap = false
)
public abstract class WorldClientMixin extends World {

    @Inject(
            method = "getSkyColor",
            at = @At("HEAD"),
            cancellable = true)
    public void getSkyColor(ICamera camera, float renderPartialTicks, CallbackInfoReturnable<Vec3> cir) {
        if(getCurrentWeather() == SIWeather.weatherEclipse){
            cir.setReturnValue(Vec3.getTempVec3(0, 0, 0));
        } else if(dimension == SIDimensions.ETERNITY){
            cir.setReturnValue(Vec3.getTempVec3(0.70,0.70,0.70));
        }
    }

}
