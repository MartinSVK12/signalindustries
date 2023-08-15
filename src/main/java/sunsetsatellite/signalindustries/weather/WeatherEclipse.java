package sunsetsatellite.signalindustries.weather;


import net.minecraft.core.world.weather.WeatherClear;

public class WeatherEclipse extends WeatherClear {
    public WeatherEclipse(int id) {
        super(id);
        setSpawnRainParticles(false);
        setSubtractLightLevel(7);
        setMobsSpawnInDaylight();
    }

    @Override
    public float[] modifyFogColor(float r, float g, float b, float intensity) {
        return new float[]{0.50f, 0.20f, 0.05f};
    }

}
