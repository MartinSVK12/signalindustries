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
import net.minecraft.core.world.LevelListener;
import net.minecraft.core.world.World;
import net.minecraft.core.world.save.LevelStorage;
import net.minecraft.core.world.season.SeasonManager;
import net.minecraft.core.world.season.Seasons;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.core.world.weather.WeatherManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.mixins.IWorldDataAccessor;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;

import java.util.List;
import java.util.Random;

@Mixin(
        value = World.class,
        remap = false
)
public abstract class WorldMixin implements IWorldDataAccessor {

    @Shadow public List<EntityPlayer> players;

    @Shadow public abstract long getWorldTime();

    @Shadow public Random rand;

    @Shadow @Final public SeasonManager seasonManager;

    @Shadow @Final public Dimension dimension;

    @Shadow public abstract Weather getCurrentWeather();

    @Shadow @Final public WeatherManager weatherManager;

    @Shadow @Final protected LevelStorage saveHandler;

    @Shadow @Final public WorldType worldType;

    @Shadow public int difficultySetting;
    @Unique
    private final World thisAs = (World)((Object)this);

    @Inject(
            method = "getSkyColor",
            at = @At("HEAD"),
            cancellable = true)
    public void getSkyColor(ICamera camera, float renderPartialTicks, CallbackInfoReturnable<Vec3d> cir) {
        if(getCurrentWeather() == SignalIndustries.weatherEclipse){
            cir.setReturnValue(Vec3d.createVector(0, 0, 0));
        } else if (getCurrentWeather() == SignalIndustries.weatherSolarApocalypse) {
            cir.setReturnValue(Vec3d.createVector(1.0, 0.5, 0));
        }
        if(dimension == SignalIndustries.dimEternity){
            cir.setReturnValue(Vec3d.createVector(0.70,0.70,0.70));
        }
    }

    @Inject(
            method = "doLightingUpdate",
            at = @At("HEAD")
    )
    public void doBloodMoon(CallbackInfo ci){
        //SignalIndustries.LOGGER.info(String.valueOf(worldType.getSunriseTick(thisAs)));
        int cycleTicks = worldType.getDayNightCycleLengthTicks();
        int dayTicks = getDayLengthTicks();
        int nightTicks = cycleTicks - dayTicks;
        long worldTime = getWorldTime();
        int dayLength = Global.DAY_LENGTH_TICKS;
        int dayTime = (int)(worldTime % (long)dayLength);
        int triggerTime = worldType.getSunriseTick(thisAs)+dayTicks;
        if((dayTime == triggerTime && (getCurrentWeather() != SignalIndustries.weatherBloodMoon || getCurrentWeather() != SignalIndustries.weatherEclipse))){
            if(rand.nextInt(16) == 15 && !(difficultySetting == 0) && getCurrentWeather() != SignalIndustries.weatherBloodMoon){
                for (EntityPlayer player : players) {
                    player.addChatMessage(TextFormatting.RED+"A Blood Moon is rising!");
                    player.triggerAchievement(SignalIndustriesAchievementPage.BLOOD_MOON);
                }
                weatherManager.overrideWeather(SignalIndustries.weatherBloodMoon,13000,1);
            }
        }
        if(dayTime == 0 && getCurrentWeather() == SignalIndustries.weatherBloodMoon){
            weatherManager.overrideWeather(Weather.overworldClear);
        }
        if (!Global.isServer){
            if(getCurrentWeather() == SignalIndustries.weatherBloodMoon){
                ColorizerWater.updateColorData(Minecraft.getMinecraft(Minecraft.class).renderEngine.getTextureImageData("/assets/signalindustries/misc/blood_moon_colorizer.png"));
            } else {
                ColorizerWater.updateColorData(Minecraft.getMinecraft(Minecraft.class).renderEngine.getTextureImageData("/misc/watercolor.png"));
            }
        }
    }

    @Inject(
            method = "doLightingUpdate",
            at = @At("HEAD")
    )
    public void doMeteorShower(CallbackInfo ci){
        //SignalIndustries.LOGGER.info(String.valueOf(worldType.getSunriseTick(thisAs)));
        int cycleTicks = worldType.getDayNightCycleLengthTicks();
        int dayTicks = getDayLengthTicks();
        int nightTicks = cycleTicks - dayTicks;
        long worldTime = getWorldTime();
        int dayLength = Global.DAY_LENGTH_TICKS;
        int dayTime = (int)(worldTime % (long)dayLength);
        int triggerTime = worldType.getSunriseTick(thisAs)+dayTicks+(nightTicks/4);
        if((dayTime == triggerTime && (getCurrentWeather() != SignalIndustries.weatherBloodMoon || getCurrentWeather() != SignalIndustries.weatherEclipse))){
            if(rand.nextInt(24) == 0 && getCurrentWeather() != SignalIndustries.weatherMeteorShower){
                for (EntityPlayer player : players) {
                    player.addChatMessage(TextFormatting.LIGHT_BLUE+"A Meteor Shower is happening!");
                    player.triggerAchievement(SignalIndustriesAchievementPage.STARFALL);
                }
                weatherManager.overrideWeather(SignalIndustries.weatherMeteorShower,60*20,1);
            }
        }
        if(dayTime == 0 && getCurrentWeather() == SignalIndustries.weatherMeteorShower){
            weatherManager.overrideWeather(Weather.overworldClear);
        }
    }

    @Unique
    private int getDayLengthTicks() {
        float dayLength;
        float seasonProgress = seasonManager.getSeasonProgress();
        if (seasonProgress < 0.5f)
        {
            float lastSeasonDayLength = seasonManager.getPreviousSeason().dayLength;
            float thisSeasonDayLength = seasonManager.getCurrentSeason().dayLength;
            float seasonModifier = seasonManager.getSeasonModifier() * 0.5f + 0.5f;
            dayLength = (lastSeasonDayLength * (1.0f - seasonModifier)) + (thisSeasonDayLength * seasonModifier);
        }
        else
        {
            float thisSeasonDayLength = seasonManager.getCurrentSeason().dayLength;
            float nextSeasonDayLength = seasonManager.getNextSeason().dayLength;
            float seasonModifier = seasonManager.getSeasonModifier() * 0.5f + 0.5f;
            dayLength = (thisSeasonDayLength * seasonModifier) + (nextSeasonDayLength * (1.0f - seasonModifier));
        }

        int cycleTicks = worldType.getDayNightCycleLengthTicks();
        return (int)(dayLength * cycleTicks);
    }

    @Inject(
            method = "doLightingUpdate",
            at = @At("HEAD")
    )
    public void doSolarEclipse(CallbackInfo ci){
        long worldTime = getWorldTime();
        int dayLength = Global.DAY_LENGTH_TICKS;
        int dayTime = (int)(worldTime % (long)dayLength);
        if(dayTime > 6680 && dayTime < 6700 && seasonManager.getDayInSeason() == 6 && seasonManager.getCurrentSeason() == Seasons.OVERWORLD_SUMMER && getCurrentWeather() != SignalIndustries.weatherEclipse ){
            for (EntityPlayer player : players) {
                player.addChatMessage(TextFormatting.ORANGE+"A Solar Eclipse is happening!");
                player.triggerAchievement(SignalIndustriesAchievementPage.ECLIPSE);
            }
            weatherManager.overrideWeather(SignalIndustries.weatherEclipse,Global.DAY_LENGTH_TICKS,1);
        }
    }

    @Inject(
            method = "wakeUpAllPlayers",
            at = @At("HEAD")
    )
    protected void wakeUpAllPlayers(CallbackInfo ci) {

        if (getCurrentWeather() != null && getCurrentWeather() == SignalIndustries.weatherEclipse) {
            weatherManager.overrideWeather(Weather.overworldClear);
        }

    }

    @Inject(
            method = "getCelestialAngle",
            at = @At("HEAD"),
            cancellable = true
    )
    public void solarEclipseCelestialAngle(float f, CallbackInfoReturnable<Float> cir){
        if(getCurrentWeather() == SignalIndustries.weatherEclipse){
            cir.setReturnValue(1f);
        }
    }

    @Override
    @Unique
    public LevelStorage getSaveHandler(){
        return saveHandler;
    }

}
