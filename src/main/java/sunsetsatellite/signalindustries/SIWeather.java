package sunsetsatellite.signalindustries;

import net.minecraft.core.world.weather.Weather;
import sunsetsatellite.signalindustries.util.DataInitializer;
import sunsetsatellite.signalindustries.weather.WeatherBloodMoon;
import sunsetsatellite.signalindustries.weather.WeatherEclipse;
import sunsetsatellite.signalindustries.weather.WeatherMeteorShower;
import sunsetsatellite.signalindustries.weather.WeatherSolarApocalypse;

import static sunsetsatellite.signalindustries.SignalIndustries.*;

public class SIWeather extends DataInitializer {
    public static Weather weatherBloodMoon;
    public static Weather weatherEclipse;
    public static Weather weatherSolarApocalypse;
    public static Weather weatherMeteorShower;

    public void init(){
        if(initialized) return;
        LOGGER.info("Initializing weather...");
        weatherBloodMoon = new WeatherBloodMoon(10).setLanguageKey("bloodMoon");
        weatherEclipse = new WeatherEclipse(11).setLanguageKey("solarEclipse");
        weatherSolarApocalypse = new WeatherSolarApocalypse(12).setLanguageKey("solarApocalypse");
        weatherMeteorShower = new WeatherMeteorShower(13).setLanguageKey("meteorShower");
        setInitialized(true);
    }
}