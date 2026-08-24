package sunsetsatellite.signalindustries.dim.custom.generator;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.LargeFeature;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;
import net.minecraft.core.world.noise.FractalNoise2D;
import net.minecraft.core.world.noise.ImprovedPerlinNoise;
import net.minecraft.core.world.noise.operator.Constant2D;
import net.minecraft.core.world.noise.operator.Warp2D;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;
import sunsetsatellite.signalindustries.dim.custom.DimensionRegistries;
import sunsetsatellite.signalindustries.dim.custom.surface.SurfaceGeneratorBase;
import sunsetsatellite.signalindustries.dim.custom.surface.SurfaceGeneratorEmpty;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ChunkGeneratorClassic extends ChunkGeneratorBase {

	private Warp2D<FractalNoise2D<ImprovedPerlinNoise>, FractalNoise2D<ImprovedPerlinNoise>, Constant2D> combinedA;
	private Warp2D<FractalNoise2D<ImprovedPerlinNoise>, FractalNoise2D<ImprovedPerlinNoise>, Constant2D> combinedB;
	private Warp2D<FractalNoise2D<ImprovedPerlinNoise>, FractalNoise2D<ImprovedPerlinNoise>, Constant2D> combinedC;
	private Warp2D<FractalNoise2D<ImprovedPerlinNoise>, FractalNoise2D<ImprovedPerlinNoise>, Constant2D> combinedD;
	private FractalNoise2D<ImprovedPerlinNoise> octavesA;
	private FractalNoise2D<ImprovedPerlinNoise> octavesB;

    public SurfaceGeneratorBase sg;
    public List<LargeFeature> largeFeatures;

    public ChunkGeneratorClassic(CustomDimensionData data, CompoundTag tag) {
        super(data, tag);
        if(largeFeatures == null){
            largeFeatures = new ArrayList<>();
        }
    }

    @Override
    public void init(World world) {
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

        sg.init(world);
    }

    @Override
    public ChunkGeneratorResult doBlockGeneration(Chunk chunk) {
		ChunkGeneratorResult result = new ChunkGeneratorResult();

		World world = chunk.world;
		int chunkX = chunk.pos.x;
		int chunkZ = chunk.pos.z;

		final int minY = world.getWorldType().getMinY(world);
		final int maxY = world.getWorldType().getMaxY(world);

		final float mod = 1.3F;

		final int[] heightMap = new int[Chunk.CHUNK_SIZE_X * Chunk.CHUNK_SIZE_Z];

		// Raising..
		for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
			for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
				final double noiseA = this.combinedA.getValue((chunkX * Chunk.CHUNK_SIZE_X + x) * mod, (chunkZ * Chunk.CHUNK_SIZE_Z + z) * mod) / 6.0D + -4;
				double noiseB = this.combinedB.getValue((chunkX * Chunk.CHUNK_SIZE_X + x) * mod, (chunkZ * Chunk.CHUNK_SIZE_Z + z) * mod) / 5.0D + 10.0D + -4;
				if (this.octavesA.getValue(chunkX * Chunk.CHUNK_SIZE_X + x, chunkZ * Chunk.CHUNK_SIZE_Z + z) / 8.0D > 0.0D) {
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
				final double val = this.combinedC.getValue((chunkX * Chunk.CHUNK_SIZE_X + x) << 1, (chunkZ * Chunk.CHUNK_SIZE_Z + z) << 1) / 8.0D;
				final int val2 = this.combinedD.getValue((chunkX * Chunk.CHUNK_SIZE_X + x) << 1, (chunkZ * Chunk.CHUNK_SIZE_Z + z) << 1) > 0.0D ? 1 : 0;
				if (val > 2.0D) {
					final int newHeight = ((newHeightMap[x + z * Chunk.CHUNK_SIZE_X] - val2) / 2 << 1) + val2;
					newHeightMap[x + z * Chunk.CHUNK_SIZE_X] = newHeight;
				}
			}
		}

		// Soiling..
		for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
			for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
				final int val = (int) (this.octavesB.getValue(chunkX * Chunk.CHUNK_SIZE_X + x, chunkZ * Chunk.CHUNK_SIZE_Z + z) / 24.0D) - 4;
				int newHeight;
				final int val2 = (newHeight = newHeightMap[x + z * Chunk.CHUNK_SIZE_X] + world.getWorldType().getOceanY()) + val;
				newHeightMap[x + z * Chunk.CHUNK_SIZE_X] = Math.max(newHeight, val2);
				if (newHeightMap[x + z * Chunk.CHUNK_SIZE_X] > maxY - 2) {
					newHeightMap[x + z * Chunk.CHUNK_SIZE_X] = maxY - 2;
				}

				if (newHeightMap[x + z * Chunk.CHUNK_SIZE_X] < 1) {
					newHeightMap[x + z * Chunk.CHUNK_SIZE_X] = 1;
				}

				for (int y = minY; y < maxY; y++) {
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

        if(sg != null){
            sg.generateSurface(chunk, result);
        }

        for (LargeFeature largeFeature : largeFeatures) {
            largeFeature.generate(world, chunkX, chunkZ, result);
        }

        return result;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        sg = this.data.getProperty("SurfaceGenerator", tag, DimensionRegistries.SURFACE_GENERATORS);
        if(sg == null){
            sg = new SurfaceGeneratorEmpty(this.data, new CompoundTag());
        }
        if(tag.containsKey("LargeFeatures")){
            ListTag features = tag.getList("LargeFeatures");
            for (Tag<?> feature : features) {
                CompoundTag featureTag = ((CompoundTag) feature);
                String id = featureTag.getString("Type");
                Class<? extends LargeFeature> largefeatureClass = DimensionRegistries.LARGE_FEATURES.getItem(id);
                try {
                    if(largeFeatures == null){
                        largeFeatures = new ArrayList<>();
                    }
                    LargeFeature largeFeature = largefeatureClass.getDeclaredConstructor().newInstance();
                    largeFeatures.add(largeFeature);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        CompoundTag sgTag = new CompoundTag();

        sgTag.putString("Type", DimensionRegistries.SURFACE_GENERATORS.getKey(sg.getClass()));
        CompoundTag data = new CompoundTag();
        sg.writeToNbt(data);
        sgTag.put("Data", data);

        ListTag lfTag = new ListTag();
        for(LargeFeature lf : largeFeatures){
            CompoundTag inner = new CompoundTag();
            inner.putString("Type", DimensionRegistries.LARGE_FEATURES.getKey(lf.getClass()));
            lfTag.addTag(inner);
        }

        tag.putList("LargeFeatures", lfTag);
        tag.putCompound("SurfaceGenerator", sgTag);
    }
}
