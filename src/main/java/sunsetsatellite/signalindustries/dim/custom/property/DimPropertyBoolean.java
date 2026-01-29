package sunsetsatellite.signalindustries.dim.custom.property;

import com.mojang.nbt.tags.CompoundTag;

public class DimPropertyBoolean extends DimPropertyBase {
    public boolean value;

    public DimPropertyBoolean(boolean value) {
        this.value = value;
    }

    public DimPropertyBoolean(CompoundTag nbt) {
        super(nbt);
    }

    @Override
    public void readFromNbt(CompoundTag nbt) {
        value = nbt.getBoolean("Value");
    }

    @Override
    public void writeToNbt(CompoundTag nbt) {
        nbt.putBoolean("Value", value);
    }

    @Override
    public Boolean get() {
        return value;
    }
}
