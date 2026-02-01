package sunsetsatellite.signalindustries.dim.custom.decorator;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.perlin.overworld.ChunkDecoratorOverworld;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;

public class ChunkDecoratorWorld extends ChunkDecoratorBase {

    private ChunkDecoratorOverworld decorator;

    public ChunkDecoratorWorld(CustomDimensionData data, CompoundTag tag) {
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
        if(decorator == null){
            decorator = new ChunkDecoratorOverworld(world);
        }
        decorator.decorate(chunk);
    }
}
