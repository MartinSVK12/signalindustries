package sunsetsatellite.signalindustries.dim.custom.property;

import com.mojang.nbt.tags.CompoundTag;

public class DimPropertyFloat extends DimPropertyBase {

    public float value;

    public DimPropertyFloat(float value) {
        this.value = value;
    }

    public DimPropertyFloat(CompoundTag nbt) {
        super(nbt);
    }

    @Override
    public void readFromNbt(CompoundTag nbt) {
        value = nbt.getFloat("Value");
    }

    @Override
    public void writeToNbt(CompoundTag nbt) {
        nbt.putFloat("Value", value);
    }

    @Override
    public Float get() {
        return value;
    }
}
