package sunsetsatellite.signalindustries.dim.custom.property;

import com.mojang.nbt.tags.CompoundTag;

public abstract class DimensionPropertyBase {

    public DimensionPropertyBase(){}

    public DimensionPropertyBase(CompoundTag nbt) {
        readFromNbt(nbt);
    }

    public abstract void readFromNbt(CompoundTag nbt);
    public abstract void writeToNbt(CompoundTag nbt);
    public abstract Object get();

}
