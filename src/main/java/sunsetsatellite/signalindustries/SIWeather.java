package sunsetsatellite.signalindustries;

import net.minecraft.core.world.weather.Weather;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import sunsetsatellite.signalindustries.weather.WeatherBloodMoon;
import sunsetsatellite.signalindustries.weather.WeatherEclipse;
import sunsetsatellite.signalindustries.weather.WeatherMeteorShower;

import static sunsetsatellite.signalindustries.SignalIndustries.LOGGER;

public class SIWeather extends DataInitializer {

    public static Weather weatherBloodMoon;
    public static Weather weatherEclipse;
    public static Weather weatherSolarApocalypse;
    public static Weather weatherMeteorShower;

    @Override
    public void init() {
        if (initialized) return;
        LOGGER.info("Initializing weather...");
        weatherBloodMoon = new WeatherBloodMoon(10, "bloodMoon");
        weatherEclipse = new WeatherEclipse(11, "solarEclipse");
        weatherMeteorShower = new WeatherMeteorShower(13, "meteorShower");
        setInitialized(true);
    }
}
