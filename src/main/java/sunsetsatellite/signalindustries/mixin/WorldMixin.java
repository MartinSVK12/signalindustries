package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.Global;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.Difficulty;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.core.world.season.SeasonManager;
import net.minecraft.core.world.season.Seasons;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.core.world.weather.WeatherManager;
import net.minecraft.core.world.weather.Weathers;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIWeather;

import java.util.List;
import java.util.Random;

@Mixin(value = World.class, remap = false)
public abstract class WorldMixin {

    @Shadow public WorldType worldType;

    @Shadow public abstract SeasonManager getSeasonManager();

    @Shadow public abstract long getWorldTime();

    @Shadow public abstract @Nullable Weather getCurrentWeather();

    @Shadow public abstract Difficulty getDifficulty();

    @Shadow public Random rand;
    @Shadow public List<Player> players;
    @Shadow public WeatherManager weatherManager;
    @Shadow public SeasonManager seasonManager;
    @Shadow public Dimension dimension;
    @Unique
    private final World thisAs = (World)((Object)this);

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    public void doBloodMoon(CallbackInfo ci){
        int cycleTicks = worldType.getDayNightCycleTicks();
        int dayTicks = getDayLengthTicks();
        int nightTicks = cycleTicks - dayTicks;
        long worldTime = getWorldTime();
        int dayLength = Global.DAY_LENGTH_TICKS;
        int dayTime = (int)(worldTime % (long)dayLength);
        int triggerTime = worldType.getSunriseTick(thisAs)+dayTicks;
        if((dayTime == triggerTime && dimension != Dimension.NETHER && (getCurrentWeather() != SIWeather.weatherBloodMoon || getCurrentWeather() != SIWeather.weatherEclipse))){
            if(rand.nextInt(16) == 15 && !(getDifficulty() == Difficulty.PEACEFUL) && getCurrentWeather() != SIWeather.weatherBloodMoon){
                for (Player player : players) {
                    player.sendMessage(TextFormatting.RED+"A Blood Moon is rising!");
                    player.triggerAchievement(SIAchievements.BLOOD_MOON);
                }
                weatherManager.overrideWeather(SIWeather.weatherBloodMoon,13000,1);
            }
        }
        if(dayTime == 0 && getCurrentWeather() == SIWeather.weatherBloodMoon){
            weatherManager.overrideWeather(Weathers.OVERWORLD_CLEAR);
        }
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    public void doSolarEclipse(CallbackInfo ci){
        long worldTime = getWorldTime();
        int dayLength = Global.DAY_LENGTH_TICKS;
        int dayTime = (int)(worldTime % (long)dayLength);
        if(dayTime > 6680 && dayTime < 6700 && dimension != Dimension.NETHER && seasonManager.getDayInSeason() == 6 && seasonManager.getCurrentSeason() == Seasons.OVERWORLD_SUMMER && getCurrentWeather() != SIWeather.weatherEclipse){
            for (Player player : players) {
                player.sendMessage(TextFormatting.ORANGE+"A Solar Eclipse is happening!");
                player.triggerAchievement(SIAchievements.ECLIPSE);
            }
            weatherManager.overrideWeather(SIWeather.weatherEclipse,Global.DAY_LENGTH_TICKS,1);
        }
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    public void doMeteorShower(CallbackInfo ci){
        int cycleTicks = worldType.getDayNightCycleTicks();
        int dayTicks = getDayLengthTicks();
        int nightTicks = cycleTicks - dayTicks;
        long worldTime = getWorldTime();
        int dayLength = Global.DAY_LENGTH_TICKS;
        int dayTime = (int)(worldTime % (long)dayLength);
        int triggerTime = worldType.getSunriseTick(thisAs)+dayTicks+(nightTicks/4);
        if((dayTime == triggerTime && dimension != Dimension.NETHER && (getCurrentWeather() != SIWeather.weatherBloodMoon || getCurrentWeather() != SIWeather.weatherEclipse))){
            if(rand.nextInt(16) == 0 && getCurrentWeather() != SIWeather.weatherMeteorShower){
                for (Player player : players) {
                    player.sendMessage(TextFormatting.LIGHT_BLUE+"A Meteor Shower is happening!");
                    player.triggerAchievement(SIAchievements.STARFALL);
                }
                weatherManager.overrideWeather(SIWeather.weatherMeteorShower,60*20,1);
            }
        }
        if(dayTime == 0 && getCurrentWeather() == SIWeather.weatherMeteorShower){
            weatherManager.overrideWeather(Weathers.OVERWORLD_CLEAR);
        }
    }

    @Unique
    private int getDayLengthTicks() {
        float dayLength;
        float seasonProgress = getSeasonManager().getSeasonProgress();
        if (seasonProgress < 0.5f)
        {
            float lastSeasonDayLength = getSeasonManager().getPreviousSeason().dayLength;
            float thisSeasonDayLength = getSeasonManager().getCurrentSeason().dayLength;
            float seasonModifier = getSeasonManager().getSeasonModifier() * 0.5f + 0.5f;
            dayLength = (lastSeasonDayLength * (1.0f - seasonModifier)) + (thisSeasonDayLength * seasonModifier);
        }
        else
        {
            float thisSeasonDayLength = getSeasonManager().getCurrentSeason().dayLength;
            float nextSeasonDayLength = getSeasonManager().getNextSeason().dayLength;
            float seasonModifier = getSeasonManager().getSeasonModifier() * 0.5f + 0.5f;
            dayLength = (thisSeasonDayLength * seasonModifier) + (nextSeasonDayLength * (1.0f - seasonModifier));
        }

        int cycleTicks = worldType.getDayNightCycleTicks();
        return (int)(dayLength * cycleTicks);
    }

    @Inject(
            method = "getCelestialAngle",
            at = @At("HEAD"),
            cancellable = true
    )
    public void solarEclipseCelestialAngle(float f, CallbackInfoReturnable<Float> cir){
        if(getCurrentWeather() == SIWeather.weatherEclipse){
            cir.setReturnValue(1f);
        }
    }
}
