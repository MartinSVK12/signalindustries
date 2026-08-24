package sunsetsatellite.signalindustries.dim.custom.decorator;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.pos.ChunkTilePos;
import net.minecraft.core.world.pos.TilePos;
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
        if (chunk.pos.x == 0 && chunk.pos.z == 0) {
            chunk.setBlockData(new ChunkTilePos(0,0,0), Blocks.BEDROCK.id());
        }
    }
}
