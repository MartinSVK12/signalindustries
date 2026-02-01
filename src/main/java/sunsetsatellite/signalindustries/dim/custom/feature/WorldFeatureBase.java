package sunsetsatellite.signalindustries.dim.custom.feature;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;
import sunsetsatellite.signalindustries.dim.custom.DecorationContext;

import java.util.Random;

public abstract class WorldFeatureBase {

    public CustomDimensionData data;

    public WorldFeatureBase(CustomDimensionData data, CompoundTag tag) {
        this.data = data;
        readFromNbt(tag);
    }

    public abstract boolean place(World world, Random random, int x, int y, int z, DecorationContext context);

    public abstract void readFromNbt(CompoundTag tag);
    public abstract void writeToNbt(CompoundTag tag);
}
