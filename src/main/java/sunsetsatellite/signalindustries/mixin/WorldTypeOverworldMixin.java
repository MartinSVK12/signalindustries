package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.config.season.SeasonConfig;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.type.WorldTypeOverworld;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.core.world.wind.WindManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SIWeather;

@Mixin(value = WorldTypeOverworld.class, remap = false)
public abstract class WorldTypeOverworldMixin extends WorldType {
    public WorldTypeOverworldMixin(String languageKey, Weather defaultWeather, WindManager windManager, boolean hasCeiling, float[] brightnessRamp, SeasonConfig defaultSeasonConfig) {
        super(languageKey, defaultWeather, windManager, hasCeiling, brightnessRamp, defaultSeasonConfig);
    }

    @Inject(method = "getSkyDarken",at = @At("HEAD"), cancellable = true)
    public void getSkyDarken(World world, long tick, float partialTick, CallbackInfoReturnable<Integer> cir)
    {
        if(world.getCurrentWeather() == SIWeather.weatherMeteorShower){
            float f1 = this.getCelestialAngle(world, tick, partialTick);
            float f2 = 1.0F - (MathHelper.cos(f1 * 3.141593F * 2.0F) * 2.0F + 0.5F);
            if (f2 < 0.0F) {
                f2 = 0.0F;
            }

            if (f2 > 1.0F) {
                f2 = 1.0F;
            }

            float weatherOffset = 0.0F;
            Weather currentWeather = world.getCurrentWeather();
            if (currentWeather != null) {
                weatherOffset = (float)/*currentWeather.subtractLightLevel*/4 * world.weatherManager.getWeatherIntensity() * world.weatherManager.getWeatherPower();
            }
            cir.setReturnValue((int)(f2 * (11.0F - weatherOffset) - weatherOffset));
        }
    }
}
