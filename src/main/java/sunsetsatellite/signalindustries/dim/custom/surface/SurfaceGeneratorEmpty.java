package sunsetsatellite.signalindustries.dim.custom.surface;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;

public class SurfaceGeneratorEmpty extends SurfaceGeneratorBase {
    public SurfaceGeneratorEmpty(CustomDimensionData data, CompoundTag tag) {
        super(data, tag);
    }

    @Override
    public void init(World world) {

    }

    @Override
    public void generateSurface(Chunk chunk, ChunkGeneratorResult chunkGeneratorResult) {

    }

    @Override
    public void readFromNbt(CompoundTag tag) {

    }

    @Override
    public void writeToNbt(CompoundTag tag) {

    }
}
