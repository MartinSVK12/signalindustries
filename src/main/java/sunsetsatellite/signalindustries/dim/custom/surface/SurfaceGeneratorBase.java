package sunsetsatellite.signalindustries.dim.custom.surface;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;

public abstract class SurfaceGeneratorBase {

    public CustomDimensionData data;

    public SurfaceGeneratorBase(CustomDimensionData data, CompoundTag tag) {
        this.data = data;
        readFromNbt(tag);
    }

    public abstract void init(World world);

    public abstract void generateSurface(Chunk chunk, ChunkGeneratorResult chunkGeneratorResult);

    public abstract void readFromNbt(CompoundTag tag);
    public abstract void writeToNbt(CompoundTag tag);

}
