package sunsetsatellite.signalindustries.dim.custom.property;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.core.world.weather.Weathers;

public class DimPropertyWeather extends DimPropertyBase {
    public Weather weather;

    public DimPropertyWeather(Weather weather){
        this.weather = weather;
    }

    public DimPropertyWeather(CompoundTag nbt) {
        super(nbt);
    }

    @Override
    public void readFromNbt(CompoundTag nbt) {
        weather = Weathers.getWeatherByLanguageKey(nbt.getString("Weather"));
    }

    @Override
    public void writeToNbt(CompoundTag nbt) {
        nbt.putString("Weather", weather.getLanguageKey());
    }

    @Override
    public Weather get() {
        return weather;
    }
}
