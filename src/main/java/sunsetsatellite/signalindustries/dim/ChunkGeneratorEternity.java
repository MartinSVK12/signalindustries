package sunsetsatellite.signalindustries.dim;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.MapGenBase;
import net.minecraft.core.world.generate.MapGenCaves;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;
import net.minecraft.core.world.generate.chunk.classic.ChunkGeneratorClassic;
import net.minecraft.core.world.generate.chunk.perlin.ChunkGeneratorPerlin;
import net.minecraft.core.world.generate.chunk.perlin.SurfaceGenerator;
import net.minecraft.core.world.generate.chunk.perlin.overworld.TerrainGeneratorOverworld;
import net.minecraft.core.world.generate.chunk.perlin.overworld.retro.ChunkDecoratorOverworldRetro;
import net.minecraft.core.world.generate.chunk.perlin.overworld.retro.SurfaceGeneratorOverworldRetro;
import net.minecraft.core.world.generate.chunk.perlin.overworld.retro.TerrainGeneratorOverworldRetro;
import net.minecraft.core.world.noise.CombinedPerlinNoise;
import net.minecraft.core.world.noise.PerlinNoise;

public class ChunkGeneratorEternity extends ChunkGenerator
{
    private final CombinedPerlinNoise combinedA;
    private final CombinedPerlinNoise combinedB;
    private final CombinedPerlinNoise combinedC;
    private final CombinedPerlinNoise combinedD;
    private final PerlinNoise octavesA;
    private final PerlinNoise octavesB;

    private final SurfaceGenerator sg;
    private final MapGenCaves cg;

    public ChunkGeneratorEternity(World world)
    {
        super(world, new ChunkDecoratorEternity(world));
        long seed = world.getRandomSeed();

        combinedA = new CombinedPerlinNoise(new PerlinNoise(seed, 8, 0), new PerlinNoise(seed, 8, 8));
        combinedB = new CombinedPerlinNoise(new PerlinNoise(seed, 8, 16), new PerlinNoise(seed, 8, 24));
        combinedC = new CombinedPerlinNoise(new PerlinNoise(seed, 8, 32), new PerlinNoise(seed, 8, 40));
        combinedD = new CombinedPerlinNoise(new PerlinNoise(seed, 8, 48), new PerlinNoise(seed, 8, 56));

        octavesA = new PerlinNoise(seed, 6, 64);
        octavesB = new PerlinNoise(seed, 8, 70);

        sg = new SurfaceGeneratorEternity(world);
        cg = new MapGenCaves(true);
    }

    @Override
    protected ChunkGeneratorResult doBlockGeneration(Chunk chunk)
    {
        ChunkGeneratorResult result = new ChunkGeneratorResult();

        int chunkX = chunk.xPosition;
        int chunkZ = chunk.zPosition;

        final float mod = 1.3F;

        final int[] heightMap = new int[Chunk.CHUNK_SIZE_X * Chunk.CHUNK_SIZE_Z];

        // Raising..
        for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++)
        {
            for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++)
            {
                final double noiseA = combinedA.get((chunkX * Chunk.CHUNK_SIZE_X + x) * mod, (chunkZ * Chunk.CHUNK_SIZE_Z + z) * mod) / 6.0D + -4;
                double noiseB = combinedB.get((chunkX * Chunk.CHUNK_SIZE_X + x) * mod, (chunkZ * Chunk.CHUNK_SIZE_Z + z) * mod) / 5.0D + 10.0D + -4;
                if (octavesA.get(chunkX * Chunk.CHUNK_SIZE_X + x, chunkZ * Chunk.CHUNK_SIZE_Z + z) / 8.0D > 0.0D)
                {
                    noiseB = noiseA;
                }

                double height;
                if ((height = Math.max(noiseA, noiseB) / 2.0D) < 0.0D)
                {
                    height *= 0.8D;
                }

                heightMap[x + z * Chunk.CHUNK_SIZE_X] = (int) height;
            }
        }

        // Eroding..
        final int[] newHeightMap = heightMap;
        for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++)
        {
            for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++)
            {
                final double val = combinedC.get((chunkX * Chunk.CHUNK_SIZE_X + x) << 1, (chunkZ * Chunk.CHUNK_SIZE_Z + z) << 1) / 8.0D;
                final int val2 = combinedD.get((chunkX * Chunk.CHUNK_SIZE_X + x) << 1, (chunkZ * Chunk.CHUNK_SIZE_Z + z) << 1) > 0.0D ? 1 : 0;
                if (val > 2.0D)
                {
                    final int newHeight = ((newHeightMap[x + z * Chunk.CHUNK_SIZE_X] - val2) / 2 << 1) + val2;
                    newHeightMap[x + z * Chunk.CHUNK_SIZE_X] = newHeight;
                }
            }
        }

        // Soiling..
        for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++)
        {
            for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++)
            {
                final int val = (int) (octavesB.get(chunkX * Chunk.CHUNK_SIZE_X + x, chunkZ * Chunk.CHUNK_SIZE_Z + z) / 24.0D) - 4;
                int newHeight;
                final int val2 = (newHeight = newHeightMap[x + z * Chunk.CHUNK_SIZE_X] + world.getWorldType().getOceanY()) + val;
                newHeightMap[x + z * Chunk.CHUNK_SIZE_X] = Math.max(newHeight, val2);
                if (newHeightMap[x + z * Chunk.CHUNK_SIZE_X] > world.getWorldType().getMaxY() - 2)
                {
                    newHeightMap[x + z * Chunk.CHUNK_SIZE_X] = world.getWorldType().getMaxY() - 2;
                }

                if (newHeightMap[x + z * Chunk.CHUNK_SIZE_X] < 1)
                {
                    newHeightMap[x + z * Chunk.CHUNK_SIZE_X] = 1;
                }

                for (int y = world.getWorldType().getMinY(); y < world.getWorldType().getMaxY(); y++)
                {
                    final int index = Chunk.makeBlockIndex(x, y, z);
                    int blockID = 0;

                    if (y < newHeight)
                    {
                        blockID = world.getWorldType().getFillerBlock();
                    }
                    else if (y < world.getWorldType().getOceanY())
                    {
                        blockID = world.getWorldType().getOceanBlock();
                    }

                    if (y == 0)
                    {
                        blockID = Block.bedrock.id;
                    }

                    result.setBlock(x, y, z, blockID);
                }
            }
        }

        sg.generateSurface(chunk, result);
        cg.generate(world, chunkX, chunkZ, result);

        return result;
    }
}
