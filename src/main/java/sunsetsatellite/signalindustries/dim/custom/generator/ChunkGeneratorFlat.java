package sunsetsatellite.signalindustries.dim.custom.generator;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;

import java.util.ArrayList;
import java.util.List;

public class ChunkGeneratorFlat extends ChunkGeneratorBase {

    public List<Layers> layers = new ArrayList<>();

    public ChunkGeneratorFlat(CustomDimensionData data, CompoundTag tag) {
        super(data, tag);
    }

    @Override
    public void init(World world) {

    }

    @Override
    public ChunkGeneratorResult doBlockGeneration(Chunk chunk) {
        ChunkGeneratorResult result = new ChunkGeneratorResult();
        for(int z = 0; z < 16; ++z) {
            for (int x = 0; x < 16; ++x) {
                for (Layers layer : layers) {
                    for (int y = layer.minY; y < layer.maxY; y++) {
                        result.setBlock(x, y, z, layer.blockId);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {

    }

    @Override
    public void writeToNbt(CompoundTag tag) {

    }

    public static class Layers {
        public int minY;
        public int maxY;
        public int blockId;

        public Layers(int minY, int maxY, int blockId) {
            this.minY = minY;
            this.maxY = maxY;
            this.blockId = blockId;
        }
    }
}
