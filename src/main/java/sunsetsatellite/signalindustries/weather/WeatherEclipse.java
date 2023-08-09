package sunsetsatellite.signalindustries.weather;


import net.minecraft.core.world.weather.WeatherClear;

public class WeatherEclipse extends WeatherClear {
    public WeatherEclipse(int id) {
        super(id);
        setSpawnRainParticles(false);
        setSubtractLightLevel(7);
        setMobsSpawnInDaylight();
    }

}
