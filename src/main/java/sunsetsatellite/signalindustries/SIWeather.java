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
        if(initialized) return;
        LOGGER.info("Initializing weather...");
        weatherBloodMoon = new WeatherBloodMoon(10).setLanguageKey("bloodMoon");
        weatherEclipse = new WeatherEclipse(11).setLanguageKey("solarEclipse");
        weatherMeteorShower = new WeatherMeteorShower(13).setLanguageKey("meteorShower");
        setInitialized(true);
    }
}
