package sunsetsatellite.signalindustries.dim.custom;

import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;

public class ChunkGeneratorWrapper extends ChunkGenerator {

    public CustomDimensionData data;

    public ChunkGeneratorWrapper(World world, CustomDimensionData data) {
        super(world, data.getChunkDecorator(world));
        this.data = data;
        data.properties.chunkGenerator.init(world);
    }

    @Override
    protected ChunkGeneratorResult doBlockGeneration(Chunk chunk) {
        return data.properties.chunkGenerator.doBlockGeneration(chunk);
    }
}
