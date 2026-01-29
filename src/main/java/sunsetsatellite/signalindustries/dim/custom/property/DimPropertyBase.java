package sunsetsatellite.signalindustries.dim.custom.property;

import com.mojang.nbt.tags.CompoundTag;

public abstract class DimPropertyBase {

    public DimPropertyBase(){}

    public DimPropertyBase(CompoundTag nbt) {
        readFromNbt(nbt);
    }

    public abstract void readFromNbt(CompoundTag nbt);
    public abstract void writeToNbt(CompoundTag nbt);
    public abstract Object get();

}
