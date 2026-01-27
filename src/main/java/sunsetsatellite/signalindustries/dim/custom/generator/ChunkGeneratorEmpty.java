package sunsetsatellite.signalindustries.dim.custom.generator;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;

public class ChunkGeneratorEmpty extends ChunkGeneratorBase{
    public ChunkGeneratorEmpty(CustomDimensionData data) {
        super(data);
    }

    @Override
    public void init(World world) {

    }

    @Override
    public ChunkGeneratorResult doBlockGeneration(Chunk chunk) {
        return new ChunkGeneratorResult();
    }

    @Override
    public void readFromNbt(CompoundTag tag) {

    }

    @Override
    public void writeToNbt(CompoundTag tag) {

    }
}
