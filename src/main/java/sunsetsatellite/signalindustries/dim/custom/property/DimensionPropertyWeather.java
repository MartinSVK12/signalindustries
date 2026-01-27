package sunsetsatellite.signalindustries.dim.custom.property;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.core.world.weather.Weathers;

public class DimensionPropertyWeather extends DimensionPropertyBase {
    public Weather weather;

    public DimensionPropertyWeather(CompoundTag nbt) {
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
