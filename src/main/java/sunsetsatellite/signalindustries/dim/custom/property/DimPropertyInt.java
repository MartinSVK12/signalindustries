package sunsetsatellite.signalindustries.dim.custom.property;

import com.mojang.nbt.tags.CompoundTag;

public class DimPropertyInt extends DimPropertyBase {

    public int value;

    public DimPropertyInt(int value) {
        this.value = value;
    }

    public DimPropertyInt(CompoundTag nbt) {
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
