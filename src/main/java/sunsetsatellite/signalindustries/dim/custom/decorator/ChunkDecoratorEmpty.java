package sunsetsatellite.signalindustries.dim.custom.decorator;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.chunk.Chunk;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;

public class ChunkDecoratorEmpty extends ChunkDecoratorBase {
    public ChunkDecoratorEmpty(CustomDimensionData data) {
        super(data);
    }

    @Override
    public void decorate(Chunk chunk) {
        if (chunk.xPosition == 0 && chunk.zPosition == 0) {
            chunk.setBlockID(0, 0, 0, Blocks.BEDROCK.id());
        }
    }
}
