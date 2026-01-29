package sunsetsatellite.signalindustries.dim.custom.property;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.world.season.Season;
import net.minecraft.core.world.season.Seasons;

public class DimPropertySeason extends DimPropertyBase {

    public Season season;
    public int length;

    public DimPropertySeason(Season season, int length) {
        this.season = season;
        this.length = length;
    }

    public DimPropertySeason(CompoundTag nbt) {
        super(nbt);
    }

    @Override
    public void readFromNbt(CompoundTag nbt) {
        season = Seasons.getSeason(nbt.getString("Season"));
        length = nbt.getInteger("Length");
    }

    @Override
    public void writeToNbt(CompoundTag nbt) {
        nbt.putInt("Length", length);
        nbt.putString("Season", season.getId());
    }

    @Override
    public Pair<Season, Integer> get() {
        return Pair.of(season, length);
    }
}
