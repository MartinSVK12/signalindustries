package sunsetsatellite.signalindustries.dim.custom.decorator;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.chunk.Chunk;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;

public class ChunkDecoratorEmpty extends ChunkDecoratorBase {
    public ChunkDecoratorEmpty(CustomDimensionData data, CompoundTag tag) {
        super(data, tag);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {

    }

    @Override
    public void writeToNbt(CompoundTag tag) {

    }

    @Override
    public void decorate(Chunk chunk) {
        if (chunk.xPosition == 0 && chunk.zPosition == 0) {
            chunk.setBlockID(0, 0, 0, Blocks.BEDROCK.id());
        }
    }
}
