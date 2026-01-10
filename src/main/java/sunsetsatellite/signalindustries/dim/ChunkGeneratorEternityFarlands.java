package sunsetsatellite.signalindustries.dim;

import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.LargeFeature;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;
import net.minecraft.core.world.generate.chunk.perlin.ChunkGeneratorPerlin;
import net.minecraft.core.world.generate.chunk.perlin.overworld.TerrainGeneratorOverworld;

public class ChunkGeneratorEternityFarlands extends ChunkGeneratorPerlin {

    public ChunkGeneratorEternityFarlands(World world) {
        super(world, new ChunkDecoratorEternity(world), new TerrainGeneratorOverworld(world), new SurfaceGeneratorEternity(world), new LargeFeature[]{});
    }

    @Override
    public ChunkGeneratorResult doBlockGeneration(Chunk chunk) {
        return super.doBlockGeneration(chunk);
    }
}
