package sunsetsatellite.signalindustries.dim.custom.generator;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;

public abstract class ChunkGeneratorBase {

    public CustomDimensionData data;

    public ChunkGeneratorBase(CustomDimensionData data) {
        this.data = data;
    }

    public abstract void init(World world);

    public abstract ChunkGeneratorResult doBlockGeneration(Chunk chunk);

    public abstract void readFromNbt(CompoundTag tag);
    public abstract void writeToNbt(CompoundTag tag);
}
