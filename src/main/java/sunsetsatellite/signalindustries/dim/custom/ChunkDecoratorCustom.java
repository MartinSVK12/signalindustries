package sunsetsatellite.signalindustries.dim.custom;

import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;

public class ChunkDecoratorCustom implements ChunkDecorator {

    public World world;
    private final CustomDimensionData data;

    public ChunkDecoratorCustom(World world, CustomDimensionData data) {
        this.world = world;
        this.data = data;
    }

    @Override
    public void decorate(Chunk chunk) {

    }
}
