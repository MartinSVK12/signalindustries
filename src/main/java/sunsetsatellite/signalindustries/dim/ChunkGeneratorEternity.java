package sunsetsatellite.signalindustries.dim;

import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.MapGenBase;
import net.minecraft.core.world.generate.chunk.perlin.ChunkGeneratorPerlin;
import net.minecraft.core.world.generate.chunk.perlin.overworld.TerrainGeneratorOverworld;

public class ChunkGeneratorEternity extends ChunkGeneratorPerlin {
    protected ChunkGeneratorEternity(World world) {
        super(world, new ChunkDecoratorEternity(world), new TerrainGeneratorOverworld(world), new SurfaceGeneratorEternity(), new MapGenBase());
    }
}
