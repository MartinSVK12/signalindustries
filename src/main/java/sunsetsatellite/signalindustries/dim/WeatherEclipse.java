package sunsetsatellite.signalindustries.dim;

import net.minecraft.src.Weather;
import net.minecraft.src.WeatherClear;

public class WeatherEclipse extends WeatherClear {
    public WeatherEclipse(int id) {
        super(id);
        setSpawnRainParticles(false);
        setSubtractLightLevel(7);
        setMobsSpawnInDaylight();
    }

}
