package sunsetsatellite.signalindustries.mixin;

import net.minecraft.src.Entity;
import net.minecraft.src.Vec3D;
import net.minecraft.src.Weather;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SignalIndustries;

@Mixin(
        value = World.class,
        remap = false
)
public class WorldMixin {

    @Shadow public Weather currentWeather;

    @Inject(
            method = "getSkyColor",
            at = @At("HEAD"),
            cancellable = true)
    public void getSkyColor(Entity entity, float f, CallbackInfoReturnable<Vec3D> cir) {
        if(currentWeather == SignalIndustries.weatherEclipse){
            cir.setReturnValue(Vec3D.createVector(0, 0, 0));
        } else if (currentWeather == SignalIndustries.weatherSolarApocalypse) {
            cir.setReturnValue(Vec3D.createVector(1.0, 0.5, 0));
        }
    }
}
