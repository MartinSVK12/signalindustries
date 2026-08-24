package sunsetsatellite.signalindustries.dim.custom.property;

import com.mojang.nbt.tags.CompoundTag;

public class DimPropertyString extends DimPropertyBase {
    public String value;

    public DimPropertyString(String value) {
        this.value = value;
    }

    public DimPropertyString(CompoundTag nbt) {
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
