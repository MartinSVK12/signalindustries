package sunsetsatellite.signalindustries.weather;




public class WeatherEclipse extends WeatherClear {
    public WeatherEclipse(int id) {
        super(id);
        setSpawnRainParticles(false);
        setSubtractLightLevel(7);
        setMobsSpawnInDaylight();
    }

}
