package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;



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

    @Shadow public int skylightSubtracted;

    @Shadow public abstract int calculateSkylightSubtracted(float f);

    @Shadow public Random rand;

    @Shadow public Weather newWeather;

    @Shadow public long weatherDuration;

    @Shadow public List<EntityPlayer> players;

    @Shadow public abstract boolean isDaytime();

    @Shadow public abstract float getCelestialAngle(float f);

    @Shadow public abstract long getWorldTime();

    @Shadow public abstract void playSoundAtEntity(Entity entity, String s, float f, float f1);

    @Shadow public float weatherIntensity;

    @Shadow public float weatherPower;

    @Shadow protected float seasonProgress;

    @Shadow protected int dayInSeason;

    @Shadow protected Season nextSeason;

    @Shadow public abstract boolean canBlockBePlacedAt(int i, int j, int k, int l, boolean flag, int i1);

    @Shadow public abstract Explosion newExplosion(Entity entity, double d, double d1, double d2, float f, boolean flag, boolean isCannonBall);

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

    @Inject(
            method = "doLightingUpdate",
            at = @At("HEAD")
    )
    public void doBloodMoon(CallbackInfo ci){
        long worldTime = getWorldTime();
        int dayLength = net.minecraft.shared.Minecraft.DAY_LENGTH_TICKS;
        int dayTime = (int)(worldTime % (long)dayLength);
        if(dayTime == 10500 && (currentWeather != SignalIndustries.weatherBloodMoon || currentWeather != SignalIndustries.weatherEclipse)){
            if(rand.nextInt(16) == 15 && !(Minecraft.getMinecraft().gameSettings.difficulty.value == 0) && currentWeather != SignalIndustries.weatherBloodMoon){
                for (EntityPlayer player : players) {
                    player.addChatMessage(ChatColor.red+"A Blood Moon is rising!");
                }
                currentWeather = SignalIndustries.weatherBloodMoon;
                newWeather = null;
                weatherDuration = 13000; //duration of night in ticks
                weatherIntensity = 1.0f;
                weatherPower = 1.0f;
            }
        }
        if(dayTime == 0 && currentWeather == SignalIndustries.weatherBloodMoon){
            currentWeather = Weather.weatherClear;
        }
        if(currentWeather == SignalIndustries.weatherBloodMoon){
            ColorizerWater.updateColorData(Minecraft.getMinecraft().renderEngine.getTextureImageData("/assets/signalindustries/misc/bloodmooncolorizer.png"));
        } else {
            ColorizerWater.updateColorData(Minecraft.getMinecraft().renderEngine.getTextureImageData("/misc/watercolor.png"));
        }
    }

    @Inject(
            method = "doLightingUpdate",
            at = @At("HEAD")
    )
    public void doSolarEclipse(CallbackInfo ci){
        long worldTime = getWorldTime();
        int dayLength = net.minecraft.shared.Minecraft.DAY_LENGTH_TICKS;
        int dayTime = (int)(worldTime % (long)dayLength);
        if(dayTime == 4800 && dayInSeason == 6 && nextSeason == Season.surfaceSpring){
            for (EntityPlayer player : players) {
                player.addChatMessage(ChatColor.orange+"A Solar Eclipse is happening!");
            }
            currentWeather = SignalIndustries.weatherEclipse;
            newWeather = null;
            weatherDuration = net.minecraft.shared.Minecraft.DAY_LENGTH_TICKS;
            weatherIntensity = 1.0f;
            weatherPower = 1.0f;
        }
    }

    @Inject(
            method = "wakeUpAllPlayers",
            at = @At("HEAD")
    )
    protected void wakeUpAllPlayers(CallbackInfo ci) {

        if (this.currentWeather != null && currentWeather == SignalIndustries.weatherEclipse) {
            this.currentWeather = Weather.weatherClear;
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
