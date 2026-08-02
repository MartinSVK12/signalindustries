package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.gamemode.Gamemodes;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.ProgressListener;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.provider.ChunkProvider;
import net.minecraft.core.world.save.*;
import net.minecraft.core.world.settings.WorldConfiguration;
import net.minecraft.core.world.weather.Weather;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.SIWeather;
import sunsetsatellite.signalindustries.SignalIndustries;

@Mixin(value = World.class,remap = false)
public abstract class WorldMixin {

	@Shadow
	@Final
	@NotNull
	public Dimension dimension;

	@Shadow
	@Nullable
	public abstract Weather getCurrentWeather();

	@Unique
	private final World thisAs = (World) ((Object) this);


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

}
