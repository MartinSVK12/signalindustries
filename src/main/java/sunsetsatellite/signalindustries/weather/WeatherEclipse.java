package sunsetsatellite.signalindustries.weather;


import net.minecraft.core.world.weather.WeatherClear;
import net.minecraft.core.world.weather.WeatherConfig;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class WeatherEclipse extends WeatherClear {
	public WeatherEclipse(int id, @NotNull String languageKey) {
		super(id, languageKey, new WeatherConfig().setLightLevelSubtracted(7).setMobDaylightSpawnAllowed(true));
	}
    /*public WeatherEclipse(int id) {
        super(id);
        setSpawnRainParticles(false);
        setSubtractLightLevel(7);
        setMobsSpawnInDaylight();
    }*/


    @Override
    public float @NonNull [] modifyFogColor(float r, float g, float b, float intensity) {
        return new float[]{0.50f, 0.20f, 0.05f};
    }

}
