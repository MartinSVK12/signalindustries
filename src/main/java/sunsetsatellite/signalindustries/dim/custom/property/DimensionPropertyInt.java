package sunsetsatellite.signalindustries.dim.custom.property;

import com.mojang.nbt.tags.CompoundTag;

public class DimensionPropertyInt extends DimensionPropertyBase {

    public int value;

    public DimensionPropertyInt(CompoundTag nbt) {
        super(nbt);
    }

    @Override
    public void readFromNbt(CompoundTag nbt) {
        value = nbt.getInteger("Value");
    }

    @Override
    public void writeToNbt(CompoundTag nbt) {
        nbt.putInt("Value", value);
    }

    @Override
    public Integer get() {
        return value;
    }
}
