package sunsetsatellite.signalindustries.interfaces.mixins;


import com.mojang.nbt.CompoundTag;

public interface INBTCompound {
    void removeTag(String s);

    boolean equals(CompoundTag tag);
}
