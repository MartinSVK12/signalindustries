package sunsetsatellite.signalindustries.dim.custom.property;

import com.mojang.nbt.tags.CompoundTag;

public class DimensionPropertyString extends DimensionPropertyBase {
    public String value;

    public DimensionPropertyString(CompoundTag nbt) {
        super(nbt);
    }

    @Override
    public void readFromNbt(CompoundTag nbt) {
        value = nbt.getString("Value");
    }

    @Override
    public void writeToNbt(CompoundTag nbt) {
        nbt.putString("Value", value);
    }

    @Override
    public String get() {
        return value;
    }
}
