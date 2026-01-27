package sunsetsatellite.signalindustries.dim.custom.property;

import com.mojang.nbt.tags.CompoundTag;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.core.util.vector.Vec4f;

public class DimensionPropertyColor extends DimensionPropertyBase {

    public Vec4f color;

    public DimensionPropertyColor(CompoundTag nbt) {
        super(nbt);
    }

    @Override
    public void readFromNbt(CompoundTag nbt) {
        Vec4f vec4f = new Vec4f();
        vec4f.readFromNBT(nbt.getCompound("Color"));
        color = vec4f;
    }

    @Override
    public void writeToNbt(CompoundTag nbt) {
        CompoundTag colorTag = new CompoundTag();
        color.writeToNBT(colorTag);
        nbt.put("Color", colorTag);
    }

    @Override
    public Vec4f get() {
        return color;
    }
}
