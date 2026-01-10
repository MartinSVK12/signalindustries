package sunsetsatellite.signalindustries.dim.custom;

import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;

public class ChunkGeneratorCustom extends ChunkGenerator {
    public ChunkGeneratorCustom(World world, CustomDimensionData data) {
        super(world, data.getChunkDecorator(world));
    }

    @Override
    protected ChunkGeneratorResult doBlockGeneration(Chunk chunk) {
        return new ChunkGeneratorResult();
    }
}
