package sunsetsatellite.signalindustries.dim;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;
import net.minecraft.core.world.generate.chunk.perlin.SurfaceGenerator;
import net.minecraft.core.world.noise.FractalNoise2D;
import net.minecraft.core.world.noise.ImprovedPerlinNoise;
import net.minecraft.core.world.noise.operator.Constant2D;
import net.minecraft.core.world.noise.operator.Warp2D;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.SIBlocks;

public class ChunkGeneratorEternity extends ChunkGenerator {
	private final @NotNull Warp2D<FractalNoise2D<ImprovedPerlinNoise>, FractalNoise2D<ImprovedPerlinNoise>, Constant2D> combinedA;
	private final @NotNull Warp2D<FractalNoise2D<ImprovedPerlinNoise>, FractalNoise2D<ImprovedPerlinNoise>, Constant2D> combinedB;
	private final @NotNull Warp2D<FractalNoise2D<ImprovedPerlinNoise>, FractalNoise2D<ImprovedPerlinNoise>, Constant2D> combinedC;
	private final @NotNull Warp2D<FractalNoise2D<ImprovedPerlinNoise>, FractalNoise2D<ImprovedPerlinNoise>, Constant2D> combinedD;
	private final @NotNull FractalNoise2D<ImprovedPerlinNoise> octavesA;
	private final @NotNull FractalNoise2D<ImprovedPerlinNoise> octavesB;

    private final ChunkGeneratorEternityFarlands farlandsGenerator;

    private final SurfaceGenerator sg;

    public ChunkGeneratorEternity(World world) {
        super(world, new ChunkDecoratorEternity(world));
        this.farlandsGenerator = new ChunkGeneratorEternityFarlands(world);
        long seed = world.getRandomSeed();

		this.combinedA = new Warp2D<>(
			new FractalNoise2D<>(ImprovedPerlinNoise.genOctaves(seed, 8, 0, ImprovedPerlinNoise.LegacyNoiseType.ALPHA)),
			new FractalNoise2D<>(ImprovedPerlinNoise.genOctaves(seed, 8, 8, ImprovedPerlinNoise.LegacyNoiseType.ALPHA)),
			new Constant2D(0.0)
		);
		this.combinedB = new Warp2D<>(
			new FractalNoise2D<>(ImprovedPerlinNoise.genOctaves(seed, 8, 16, ImprovedPerlinNoise.LegacyNoiseType.ALPHA)),
			new FractalNoise2D<>(ImprovedPerlinNoise.genOctaves(seed, 8, 24, ImprovedPerlinNoise.LegacyNoiseType.ALPHA)),
			new Constant2D(0.0)
		);
		this.combinedC = new Warp2D<>(
			new FractalNoise2D<>(ImprovedPerlinNoise.genOctaves(seed, 8, 32, ImprovedPerlinNoise.LegacyNoiseType.ALPHA)),
			new FractalNoise2D<>(ImprovedPerlinNoise.genOctaves(seed, 8, 40, ImprovedPerlinNoise.LegacyNoiseType.ALPHA)),
			new Constant2D(0.0)
		);
		this.combinedD = new Warp2D<>(
			new FractalNoise2D<>(ImprovedPerlinNoise.genOctaves(seed, 8, 48, ImprovedPerlinNoise.LegacyNoiseType.ALPHA)),
			new FractalNoise2D<>(ImprovedPerlinNoise.genOctaves(seed, 8, 56, ImprovedPerlinNoise.LegacyNoiseType.ALPHA)),
			new Constant2D(0.0)
		);

		this.octavesA = new FractalNoise2D<>(ImprovedPerlinNoise.genOctaves(seed, 8, 64, ImprovedPerlinNoise.LegacyNoiseType.ALPHA));
		this.octavesB = new FractalNoise2D<>(ImprovedPerlinNoise.genOctaves(seed, 8, 70, ImprovedPerlinNoise.LegacyNoiseType.ALPHA));

        sg = new SurfaceGeneratorEternity(world);
    }

    @Override
    protected @NonNull ChunkGeneratorResult doBlockGeneration(Chunk chunk) {
        /*if(chunk.xPosition * 16 > 12550824 || chunk.zPosition * 16 > 12550824 ) {
            ((WorldTypeEternity) world.getWorldType()).fillerBlock = SIBlocks.unraveledFabric.id();
            return farlandsGenerator.doBlockGeneration(chunk);
        }*/

        ((WorldTypeEternity) world.getWorldType()).fillerBlock = SIBlocks.realityFabric.id();

        ChunkGeneratorResult result = new ChunkGeneratorResult();

        int chunkX = chunk.pos.x;
        int chunkZ = chunk.pos.z;

        final float mod = 1.3F;

        final int[] heightMap = new int[Chunk.CHUNK_SIZE_X * Chunk.CHUNK_SIZE_Z];

        // Raising..
        for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
            for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                final double noiseA = combinedA.getValue((chunkX * Chunk.CHUNK_SIZE_X + x) * mod, (chunkZ * Chunk.CHUNK_SIZE_Z + z) * mod) / 6.0D + -4;
                double noiseB = combinedB.getValue((chunkX * Chunk.CHUNK_SIZE_X + x) * mod, (chunkZ * Chunk.CHUNK_SIZE_Z + z) * mod) / 5.0D + 10.0D + -4;
                if (octavesA.getValue(chunkX * Chunk.CHUNK_SIZE_X + x, chunkZ * Chunk.CHUNK_SIZE_Z + z) / 8.0D > 0.0D) {
                    noiseB = noiseA;
                }

                double height;
                if ((height = Math.max(noiseA, noiseB) / 2.0D) < 0.0D) {
                    height *= 0.8D;
                }

                heightMap[x + z * Chunk.CHUNK_SIZE_X] = (int) height;
            }
        }

        // Eroding..
        final int[] newHeightMap = heightMap;
        for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
            for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                final double val = combinedC.getValue((chunkX * Chunk.CHUNK_SIZE_X + x) << 1, (chunkZ * Chunk.CHUNK_SIZE_Z + z) << 1) / 8.0D;
                final int val2 = combinedD.getValue((chunkX * Chunk.CHUNK_SIZE_X + x) << 1, (chunkZ * Chunk.CHUNK_SIZE_Z + z) << 1) > 0.0D ? 1 : 0;
                if (val > 2.0D) {
                    final int newHeight = ((newHeightMap[x + z * Chunk.CHUNK_SIZE_X] - val2) / 2 << 1) + val2;
                    newHeightMap[x + z * Chunk.CHUNK_SIZE_X] = newHeight;
                }
            }
        }

        // Soiling..
        for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
            for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                final int val = (int) (octavesB.getValue(chunkX * Chunk.CHUNK_SIZE_X + x, chunkZ * Chunk.CHUNK_SIZE_Z + z) / 24.0D) - 4;
                int newHeight;
                final int val2 = (newHeight = newHeightMap[x + z * Chunk.CHUNK_SIZE_X] + world.getWorldType().getOceanY()) + val;
                newHeightMap[x + z * Chunk.CHUNK_SIZE_X] = Math.max(newHeight, val2);
                if (newHeightMap[x + z * Chunk.CHUNK_SIZE_X] > world.getWorldType().getMaxY(world) - 2) {
                    newHeightMap[x + z * Chunk.CHUNK_SIZE_X] = world.getWorldType().getMaxY(world) - 2;
                }

                if (newHeightMap[x + z * Chunk.CHUNK_SIZE_X] < 1) {
                    newHeightMap[x + z * Chunk.CHUNK_SIZE_X] = 1;
                }

                for (int y = world.getWorldType().getMinY(world); y < world.getWorldType().getMaxY(world); y++) {
                    final int index = Chunk.makeBlockIndex(x, y, z);
                    int blockID = 0;

                    if (y < newHeight) {
                        blockID = world.getWorldType().getFillerBlockId();
                    } else if (y < world.getWorldType().getOceanY()) {
                        blockID = world.getWorldType().getOceanBlockIds()[0];
                    }

                    if (y == 0) {
                        blockID = Blocks.BEDROCK.id();
                    }

                    result.setBlock(x, y, z, blockID);
                }
            }
        }

        sg.generateSurface(chunk, result);

        return result;
    }
}
