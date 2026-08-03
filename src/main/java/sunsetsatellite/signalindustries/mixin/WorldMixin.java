package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Global;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.Difficulty;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.gamemode.Gamemodes;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.ProgressListener;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.provider.ChunkProvider;
import net.minecraft.core.world.save.*;
import net.minecraft.core.world.season.SeasonManager;
import net.minecraft.core.world.season.Seasons;
import net.minecraft.core.world.settings.WorldConfiguration;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.core.world.weather.WeatherManager;
import net.minecraft.core.world.weather.Weathers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.SIWeather;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.List;
import java.util.Random;

@Mixin(value = World.class,remap = false)
public abstract class WorldMixin {

	@Shadow
	@Final
	@NotNull
	public Dimension dimension;

	@Shadow
	@Nullable
	public abstract Weather getCurrentWeather();

	@Shadow
	public abstract long getWorldTime();

	@Shadow
	@Final
	private @NotNull SeasonManager seasonManager;
	@Shadow
	@Final
	@NotNull
	public List<@NotNull Player> players;
	@Shadow
	@Final
	private @NotNull WeatherManager weatherManager;

	@Shadow
	@NotNull
	public abstract WorldType getWorldType();

	@Shadow
	public abstract Difficulty getDifficulty();

	@Shadow
	@Final
	@NotNull
	public Random rand;
	@Unique
	private final World thisAs = (World) ((Object) this);

	@Unique
	private int getDayLengthTicks() {
		float dayLength;
		float seasonProgress = seasonManager.getSeasonProgress();
		if (seasonProgress < 0.5f) {
			float lastSeasonDayLength = seasonManager.getPreviousSeason().dayLength;
			float thisSeasonDayLength = seasonManager.getCurrentSeason().dayLength;
			float seasonModifier = seasonManager.getSeasonModifier() * 0.5f + 0.5f;
			dayLength = (lastSeasonDayLength * (1.0f - seasonModifier)) + (thisSeasonDayLength * seasonModifier);
		} else {
			float thisSeasonDayLength = seasonManager.getCurrentSeason().dayLength;
			float nextSeasonDayLength = seasonManager.getNextSeason().dayLength;
			float seasonModifier = seasonManager.getSeasonModifier() * 0.5f + 0.5f;
			dayLength = (thisSeasonDayLength * seasonModifier) + (nextSeasonDayLength * (1.0f - seasonModifier));
		}

		int cycleTicks = getWorldType().getDayNightCycleTicks();
		return (int) (dayLength * cycleTicks);
	}

	@Inject(
		method = "tick",
		at = @At("TAIL")
	)
	public void doBloodMoon(CallbackInfo ci) {
		int cycleTicks = getWorldType().getDayNightCycleTicks();
		int dayTicks = getDayLengthTicks();
		int nightTicks = cycleTicks - dayTicks;
		long worldTime = getWorldTime();
		int dayLength = Global.DAY_LENGTH_TICKS;
		int dayTime = (int) (worldTime % (long) dayLength);
		int triggerTime = getWorldType().getSunriseTick(thisAs) + dayTicks;
		if (!SignalIndustries.bloodMoonsDisabled && (dayTime == triggerTime && dimension != Dimension.NETHER && (getCurrentWeather() != SIWeather.weatherBloodMoon || getCurrentWeather() != SIWeather.weatherEclipse))) {
			if (rand.nextInt(16) == 15 && !(getDifficulty() == Difficulty.PEACEFUL) && getCurrentWeather() != SIWeather.weatherBloodMoon) {
				for (Player player : players) {
					player.sendMessage(TextFormatting.RED + "A Blood Moon is rising!");
					player.triggerAchievement(SIAchievements.BLOOD_MOON);
				}
				weatherManager.overrideWeather(SIWeather.weatherBloodMoon, 13000, 1);
			}
		}
		if (dayTime > getWorldType().getSunriseTick(thisAs) && dayTime < triggerTime && getCurrentWeather() == SIWeather.weatherBloodMoon) {
			weatherManager.overrideWeather(Weathers.OVERWORLD_CLEAR);
		}
	}

	@Inject(
		method = "tick",
		at = @At("TAIL")
	)
	public void doMeteorShower(CallbackInfo ci) {
		int cycleTicks = getWorldType().getDayNightCycleTicks();
		int dayTicks = getDayLengthTicks();
		int nightTicks = cycleTicks - dayTicks;
		long worldTime = getWorldTime();
		int dayLength = Global.DAY_LENGTH_TICKS;
		int dayTime = (int) (worldTime % (long) dayLength);
		int triggerTime = getWorldType().getSunriseTick(thisAs) + dayTicks + (nightTicks / 4);
		if ((dayTime == triggerTime && dimension != Dimension.NETHER && (getCurrentWeather() != SIWeather.weatherBloodMoon || getCurrentWeather() != SIWeather.weatherEclipse))) {
			if (rand.nextInt(16) == 0 && getCurrentWeather() != SIWeather.weatherMeteorShower) {
				for (Player player : players) {
					player.sendMessage(TextFormatting.LIGHT_BLUE + "A Meteor Shower is happening!");
					player.triggerAchievement(SIAchievements.STARFALL);
				}
				weatherManager.overrideWeather(SIWeather.weatherMeteorShower, 60 * 20, 1);
			}
		}
		if (dayTime == 0 && getCurrentWeather() == SIWeather.weatherMeteorShower) {
			weatherManager.overrideWeather(Weathers.OVERWORLD_CLEAR);
		}
	}

	@Inject(
		method = "tick",
		at = @At("TAIL")
	)
	public void doSolarEclipse(CallbackInfo ci) {
		long worldTime = getWorldTime();
		int dayLength = Global.DAY_LENGTH_TICKS;
		int dayTime = (int) (worldTime % (long) dayLength);
		if (dayTime > 6680 && dayTime < 6700 && dimension != Dimension.NETHER && seasonManager.getDayInSeason() == 6 && seasonManager.getCurrentSeason() == Seasons.OVERWORLD_SUMMER && getCurrentWeather() != SIWeather.weatherEclipse) {
			for (Player player : players) {
				player.sendMessage(TextFormatting.ORANGE + "A Solar Eclipse is happening!");
				player.triggerAchievement(SIAchievements.ECLIPSE);
			}
			weatherManager.overrideWeather(SIWeather.weatherEclipse, Global.DAY_LENGTH_TICKS, 1);
		}
	}

	@Inject(method = "spawnPlayerWithLoadedChunks", at = @At("HEAD"))
	public void spawnPlayerWithLoadedChunks(Player player, boolean respawning, CallbackInfo ci) {
		if(dimension.id != SIDimensions.ETERNITY.id && !SignalIndustries.DEBUG) return;
		player.setGamemode(Gamemodes.CREATIVE);
		player.setNoclip(true);
		player.setPos(0, 80, 0);
		player.setRot(0,90);
	}

	@Inject(method = "saveWorld", at = @At("HEAD"), cancellable = true)
	public void saveWorld(boolean saveImmediately, ProgressListener progressUpdate, boolean saveLevelData, CallbackInfo ci) {
		if(dimension.id == SIDimensions.ETERNITY.id && SignalIndustries.DEBUG) {
			ci.cancel();
		}
	}

	@Inject(
		method = "getCelestialAngle",
		at = @At("HEAD"),
		cancellable = true
	)
	public void solarEclipseCelestialAngle(float f, CallbackInfoReturnable<Float> cir) {
		if (getCurrentWeather() == SIWeather.weatherEclipse) {
			cir.setReturnValue(1f);
		}
	}

	@Inject(
		method = "wakeUpAllPlayers",
		at = @At("HEAD")
	)
	protected void wakeUpAllPlayers(CallbackInfo ci) {

		if (getCurrentWeather() != null && (getCurrentWeather() == SIWeather.weatherEclipse || getCurrentWeather() == SIWeather.weatherBloodMoon)) {
			weatherManager.overrideWeather(Weathers.OVERWORLD_CLEAR);
		}

	}

}
