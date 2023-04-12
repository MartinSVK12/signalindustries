package sunsetsatellite.signalindustries.dim;

import net.minecraft.src.Weather;

public class WeatherBloodMoon extends Weather {
    public WeatherBloodMoon(int id) {
        super(id);
        setSpawnRainParticles(false);
        setSubtractLightLevel(7);
        setFogDistance(0.99f);
    }

    @Override
    public float[] modifyFogColor(float r, float g, float b, float intensity) {
        return new float[]{0f, 0f, 0f};
    }
}
