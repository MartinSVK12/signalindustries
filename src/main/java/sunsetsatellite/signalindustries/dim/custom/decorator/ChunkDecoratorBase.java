package sunsetsatellite.signalindustries.dim.custom.decorator;

import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;

public abstract class ChunkDecoratorBase implements ChunkDecorator {

    public World world;
    public CustomDimensionData data;

    public ChunkDecoratorBase(CustomDimensionData data) {
        this.data = data;
    }

}
