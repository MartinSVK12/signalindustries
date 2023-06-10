package sunsetsatellite.signalindustries.weather;

import net.minecraft.src.Weather;

public class WeatherBloodMoon extends Weather {
    public WeatherBloodMoon(int id) {
        super(id);
        setSpawnRainParticles(false);
        setSubtractLightLevel(7);
    }

    @Override
    public float[] modifyFogColor(float r, float g, float b, float intensity) {
        return super.modifyFogColor(r,g,b,intensity);//new float[]{0f, 0f, 0f};
    }
}
