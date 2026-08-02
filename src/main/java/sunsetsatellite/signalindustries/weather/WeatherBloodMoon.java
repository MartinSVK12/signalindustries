package sunsetsatellite.signalindustries.weather;


import net.minecraft.core.world.weather.Weather;
import net.minecraft.core.world.weather.WeatherConfig;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class WeatherBloodMoon extends Weather {
	public WeatherBloodMoon(int id, @NotNull String languageKey) {
		super(id, languageKey, new WeatherConfig().setLightLevelSubtracted(7));
	}

    @Override
    public float @NonNull [] modifyFogColor(float r, float g, float b, float intensity) {
        return new float[]{0,0,0};
    }
}
