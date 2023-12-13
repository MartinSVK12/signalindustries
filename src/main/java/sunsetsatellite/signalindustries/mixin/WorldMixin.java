package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.option.enums.Difficulty;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.client.render.colorizer.ColorizerWater;
import net.minecraft.core.Global;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.Explosion;
import net.minecraft.core.world.World;
import net.minecraft.core.world.season.SeasonManager;
import net.minecraft.core.world.season.Seasons;
import net.minecraft.core.world.weather.Weather;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.List;
import java.util.Random;

@Mixin(
        value = World.class,
        remap = false
)
public abstract class WorldMixin {

    @Shadow public Weather currentWeather;

    @Shadow public Weather newWeather;

    @Shadow public long weatherDuration;

    @Shadow public List<EntityPlayer> players;

    @Shadow public abstract boolean isDaytime();

    @Shadow public abstract float getCelestialAngle(float f);

    @Shadow public abstract long getWorldTime();

    @Shadow public abstract void playSoundAtEntity(Entity entity, String s, float f, float f1);

    @Shadow public float weatherIntensity;

    @Shadow public float weatherPower;

    @Shadow public abstract Explosion newExplosion(Entity entity, double d, double d1, double d2, float f, boolean flag, boolean isCannonBall);

    @Shadow public Random rand;

    @Shadow @Final public SeasonManager seasonManager;

    @Shadow @Final public Dimension dimension;

    @Inject(
            method = "getSkyColor",
            at = @At("HEAD"),
            cancellable = true)
    public void getSkyColor(ICamera camera, float renderPartialTicks, CallbackInfoReturnable<Vec3d> cir) {
        if(currentWeather == SignalIndustries.weatherEclipse){
            cir.setReturnValue(Vec3d.createVector(0, 0, 0));
        } else if (currentWeather == SignalIndustries.weatherSolarApocalypse) {
            cir.setReturnValue(Vec3d.createVector(1.0, 0.5, 0));
        }
        if(dimension == SignalIndustries.dimEternity){
            cir.setReturnValue(Vec3d.createVector(0.35,0.35,0.35));
        }
    }

    @Inject(
            method = "doLightingUpdate",
            at = @At("HEAD")
    )
    public void doBloodMoon(CallbackInfo ci){
        long worldTime = getWorldTime();
        int dayLength = Global.DAY_LENGTH_TICKS;
        int dayTime = (int)(worldTime % (long)dayLength);
        if(dayTime == 10500 && dimension == Dimension.overworld && (currentWeather != SignalIndustries.weatherBloodMoon || currentWeather != SignalIndustries.weatherEclipse)){
            if(rand.nextInt(16) == 15 && !(Minecraft.getMinecraft(Minecraft.class).gameSettings.difficulty.value == Difficulty.PEACEFUL) && currentWeather != SignalIndustries.weatherBloodMoon){
                for (EntityPlayer player : players) {
                    player.addChatMessage(TextFormatting.RED+"A Blood Moon is rising!");
                }
                currentWeather = SignalIndustries.weatherBloodMoon;
                newWeather = null;
                weatherDuration = 13000; //duration of night in ticks
                weatherIntensity = 1.0f;
                weatherPower = 1.0f;
            }
        }
        if(dayTime == 0 && currentWeather == SignalIndustries.weatherBloodMoon){
            currentWeather = Weather.overworldClear;
        }
        if(currentWeather == SignalIndustries.weatherBloodMoon){
            ColorizerWater.updateColorData(Minecraft.getMinecraft(Minecraft.class).renderEngine.getTextureImageData("/assets/signalindustries/misc/blood_moon_colorizer.png"));
        } else {
            ColorizerWater.updateColorData(Minecraft.getMinecraft(Minecraft.class).renderEngine.getTextureImageData("/misc/watercolor.png"));
        }
    }

    @Inject(
            method = "doLightingUpdate",
            at = @At("HEAD")
    )
    public void doSolarEclipse(CallbackInfo ci){
        long worldTime = getWorldTime();
        int dayLength = Global.DAY_LENGTH_TICKS;
        int dayTime = (int)(worldTime % (long)dayLength);
        if(dayTime > 6680 && dayTime < 6700 && dimension == Dimension.overworld && seasonManager.getDayInSeason() == 6 && seasonManager.getCurrentSeason() == Seasons.OVERWORLD_SUMMER && currentWeather != SignalIndustries.weatherEclipse ){
            for (EntityPlayer player : players) {
                player.addChatMessage(TextFormatting.ORANGE+"A Solar Eclipse is happening!");
            }
            currentWeather = SignalIndustries.weatherEclipse;
            newWeather = null;
            weatherDuration = Global.DAY_LENGTH_TICKS;
            weatherIntensity = 0;
            weatherPower = 1;
        }
    }

    @Inject(
            method = "wakeUpAllPlayers",
            at = @At("HEAD")
    )
    protected void wakeUpAllPlayers(CallbackInfo ci) {

        if (this.currentWeather != null && currentWeather == SignalIndustries.weatherEclipse) {
            this.currentWeather = Weather.overworldClear;
        }

    }

    @Inject(
            method = "getCelestialAngle",
            at = @At("HEAD"),
            cancellable = true
    )
    public void solarEclipseCelestialAngle(float f, CallbackInfoReturnable<Float> cir){
        if(currentWeather == SignalIndustries.weatherEclipse){
            cir.setReturnValue(1f);
        }
    }
}
