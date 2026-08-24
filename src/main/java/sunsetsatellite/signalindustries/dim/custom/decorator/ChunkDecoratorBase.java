package sunsetsatellite.signalindustries.dim.custom.decorator;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;

public abstract class ChunkDecoratorBase implements ChunkDecorator {

    public World world;
    public CustomDimensionData data;

    public ChunkDecoratorBase(CustomDimensionData data, CompoundTag tag) {
        this.data = data;
        readFromNbt(tag);
    }

    public abstract void readFromNbt(CompoundTag tag);
    public abstract void writeToNbt(CompoundTag tag);

}
