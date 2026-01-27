package sunsetsatellite.signalindustries.dim.custom;

import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;

public class ChunkDecoratorWrapper implements ChunkDecorator {

    public World world;
    public CustomDimensionData data;

    public ChunkDecoratorWrapper(World world, CustomDimensionData data) {
        this.world = world;
        this.data = data;
        data.properties.chunkDecorator.world = world;
    }

    @Override
    public void decorate(Chunk chunk) {
        data.properties.chunkDecorator.decorate(chunk);
    }
}
