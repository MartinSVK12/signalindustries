package sunsetsatellite.signalindustries.dim.custom.surface;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.BiomeTags;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;
import net.minecraft.core.world.noise.*;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;

import java.util.Random;

public class SurfaceGeneratorOverworld extends SurfaceGeneratorBase {

	private World world;

	private Noise3D beachNoise;
	private Noise3D soilNoise;
	private Noise3D mainNoise;

	private boolean generateStoneVariants;

    public SurfaceGeneratorOverworld(CustomDimensionData data, CompoundTag tag) {
        super(data, tag);
    }

    @Override
    public void init(World world) {
		init(world,
			new FractalNoise3D<>(ImprovedPerlinNoise.genOctaves(world.getRandomSeed(), 4, 40)),
			new FractalNoise3D<>(ImprovedPerlinNoise.genOctaves(world.getRandomSeed(), 4, 44)),
			new FractalNoise3D<>(ImprovedPerlinNoise.genOctaves(world.getRandomSeed(), 8, 32)),
			true
		);
    }

    public void init( World world,
					  Noise3D beachNoise,
					  Noise3D soilNoise,
					  Noise3D mainNoise,
					  boolean generateStoneVariants) {
        this.world = world;
        this.beachNoise = beachNoise;
        this.soilNoise = soilNoise;
        this.mainNoise = mainNoise;
        this.generateStoneVariants = generateStoneVariants;
    }

    @Override
    public void generateSurface(Chunk chunk, ChunkGeneratorResult result) {
		int oceanY = world.getWorldType().getOceanY();
		int minY = world.getWorldType().getMinY(world);
		int maxY = world.getWorldType().getMaxY(world);
		int terrainHeight = (maxY + 1) - minY;
		int chunkX = chunk.pos.x;
		int chunkZ = chunk.pos.z;

		int oceanBlock = world.getWorldType().getOceanBlockIds()[0];
		int worldFillBlock = world.getWorldType().getFillerBlockId();

		Random rand = new Random((long) chunkX * 0x4F9939F508L + (long) chunkZ * 0x1EF1565BD5L);

		double beachScale = 0.03125;
		double[] sandBeachNoise = beachNoise.getRegion(null, chunkX * Chunk.CHUNK_SIZE_X, chunkZ * Chunk.CHUNK_SIZE_Z, 0.0D, Chunk.CHUNK_SIZE_X, Chunk.CHUNK_SIZE_Z, 1, beachScale, beachScale, 1.0D);
		double[] gravelBeachNoise = beachNoise.getRegion(null, chunkX * Chunk.CHUNK_SIZE_X, 109.0134D, chunkZ * Chunk.CHUNK_SIZE_Z, Chunk.CHUNK_SIZE_X, 1, Chunk.CHUNK_SIZE_Z, beachScale, 1.0D, beachScale);
		double[] soilThicknessNoise = soilNoise.getRegion(null, chunkX * Chunk.CHUNK_SIZE_X, chunkZ * Chunk.CHUNK_SIZE_Z, 0.0D, Chunk.CHUNK_SIZE_X, Chunk.CHUNK_SIZE_Z, 1, beachScale * 2D, beachScale * 2D, beachScale * 2D);

		double[] stoneLayerNoiseBasalt = null;
		double[] stoneLayerNoiseGranite = null;
		double[] stoneLayerNoiseLimestone = null;
		if (generateStoneVariants)
		{
			stoneLayerNoiseBasalt = soilNoise.getRegion(null, chunkX * Chunk.CHUNK_SIZE_X, chunkZ * Chunk.CHUNK_SIZE_Z, 0.0D, Chunk.CHUNK_SIZE_X, Chunk.CHUNK_SIZE_Z, 1, beachScale * 4D, beachScale * 4D, beachScale * 4D);
			stoneLayerNoiseGranite = mainNoise.getRegion(null, chunkX * Chunk.CHUNK_SIZE_X, chunkZ * Chunk.CHUNK_SIZE_Z, 0.0D, Chunk.CHUNK_SIZE_X, Chunk.CHUNK_SIZE_Z, 1, beachScale * 4D, beachScale * 4D, beachScale * 4D);
			stoneLayerNoiseLimestone = beachNoise.getRegion(null, chunkX * Chunk.CHUNK_SIZE_X, chunkZ * Chunk.CHUNK_SIZE_Z, 0.0D, Chunk.CHUNK_SIZE_X, Chunk.CHUNK_SIZE_Z, 1, beachScale * 4D, beachScale * 4D, beachScale * 4D);
		}

		for(int z = 0; z < Chunk.CHUNK_SIZE_Z; z++)
		{
			for(int x = 0; x < Chunk.CHUNK_SIZE_X; x++)
			{
				boolean generateSandBeach = sandBeachNoise[z + (x * Chunk.CHUNK_SIZE_Z)] + rand.nextDouble() * 0.2D > 0.0D;
				boolean generateGravelBeach = gravelBeachNoise[z + (x * Chunk.CHUNK_SIZE_Z)] + rand.nextDouble() * 0.2D > 3D;
				int soilThickness = (int)(soilThicknessNoise[z + (x * Chunk.CHUNK_SIZE_Z)] / 3D + 3D + rand.nextDouble() * 0.25D);

				boolean generateBasaltLayer = false;
				boolean generateGraniteLayer = false;
				boolean generateLimestoneLayer = false;

				int basaltThicknessLevel = 0;
				int graniteThicknessLevel = 0;
				int limestoneThicknessLevel = 0;

				if (generateStoneVariants)
				{
					generateBasaltLayer = stoneLayerNoiseBasalt[z + (x * Chunk.CHUNK_SIZE_Z)] + rand.nextDouble() * 0.2D > 0D;
					generateGraniteLayer = stoneLayerNoiseGranite[z + (x * Chunk.CHUNK_SIZE_Z)] + rand.nextDouble() * 0.2D > 2D;
					generateLimestoneLayer = stoneLayerNoiseLimestone[z + (x * Chunk.CHUNK_SIZE_Z)] + rand.nextDouble() * 0.2D > 3D;
					basaltThicknessLevel = (int)(stoneLayerNoiseBasalt[z + x] + rand.nextDouble() * 0.5D);
					graniteThicknessLevel = (int)(stoneLayerNoiseGranite[z + x] + rand.nextDouble() * 0.5D);
					limestoneThicknessLevel = (int)(stoneLayerNoiseLimestone[z + x] + rand.nextDouble() * 0.5D);
				}

				int currentLayerDepth = -1;
				short topBlock = -1;
				short fillerBlock = -1;

				Biome lastBiome = null;

				for(int y = maxY; y >= minY; y--)
				{
					Biome biome = chunk.getBlockBiome(x, y, z);
					if (biome == null) biome = world.getBiomeProvider().getBiome(chunkX * Chunk.CHUNK_SIZE_X + x, y >> 3, chunkZ * Chunk.CHUNK_SIZE_Z + z);

					int block = result.getBlock(x, y, z);

					if ((biome != lastBiome || topBlock == -1 || fillerBlock == -1) && block == 0)
					{
						topBlock = (short) biome.getSurfaceProperties().getTopBlock().id();
						fillerBlock = (short) biome.getSurfaceProperties().getFillerBlock().id();
					}
					lastBiome = biome;

					// reset the currently generating surface thickness to -1 if air encountered
					if(block == 0)
					{
						currentLayerDepth = -1;
						continue;
					}

					// will skip a generation loop if it encounters something other than stone
					if(block != worldFillBlock)
					{
						continue;
					}

					// if thickness == -1, find what block to place on layer level based on biome
					if(currentLayerDepth == -1)
					{
						// if soil thickness is below 0, generate a stone basin where there is no top block layer
						if(soilThickness <= 0)
						{
							topBlock = 0;
							fillerBlock = (short) worldFillBlock;
						}
						else {
							//todo: biome tags
							boolean biomeGeneratesMud = biome.hasTag(BiomeTags.HAS_SURFACE_MUD);

							if(y >= minY + oceanY - 4 && y <= minY + oceanY + 1)
							{
								// generate coastlines
								topBlock = (short) biome.getSurfaceProperties().getTopBlock().id();
								fillerBlock = (short) biome.getSurfaceProperties().getFillerBlock().id();
								if(biomeGeneratesMud)
								{
									topBlock = (short) Blocks.MUD.id();
									fillerBlock = (short) Blocks.MUD.id();
								}
								else if(generateGravelBeach)
								{
									topBlock = 0;
									fillerBlock = (short) Blocks.GRAVEL.id();
								}
								else if(generateSandBeach)
								{
									topBlock = (short) Blocks.SAND.id();
									fillerBlock = (short) Blocks.SAND.id();
								}
							} else if (y <= oceanY) {
								if(biomeGeneratesMud) // Make mud under the ocean always be regular mud
								{
									topBlock = (short) Blocks.MUD.id();
									fillerBlock = (short) Blocks.MUD.id();
								}
							}
						}

						//failsafe so that if a basin is generated under the ocean, the ocean is not replaced with air
						if(y < minY + oceanY && topBlock == 0)
						{
							topBlock = (short) oceanBlock;
						}
						// if a new surface layer has been chosen to be generated, set the current layer depth to the generated soil thickness level and begin generating downwards
						currentLayerDepth = soilThickness;

						// set block at index to designated surface block above ocean level
						if(y >= minY + oceanY - 1)
						{
							result.setBlock(x, y, z, topBlock);
						} else
						{
							// if the block is below the water level, set it to the chosen filler block
							result.setBlock(x, y, z, fillerBlock);
						}
						continue;
					}

					if (generateStoneVariants)
					{
						// begin stone layer pass when current surface layer has finished generating
						if(currentLayerDepth <= 0)
						{
							// basalt only generates at y 30 + the basalt thickness value at the current block level + a random number between 0 and 2 (gives the layer a random gradiant at the edge)
							if (y >= (minY + basaltThicknessLevel - rand.nextInt(3)) && y <= (minY + 30 + basaltThicknessLevel - rand.nextInt(3)))
							{
								if (generateBasaltLayer)
								{
									result.setBlock(x, y, z, Blocks.BASALT.id());
									continue;
								}
							}
							if(biome == Biomes.OVERWORLD_GLACIER){
								if (y >= (minY + 56 + graniteThicknessLevel/4 - rand.nextInt(3)) && y <= maxY)
								{
									result.setBlock(x, y, z, Blocks.PERMAFROST.id());
									continue;
								}
							}
							if(biome == Biomes.OVERWORLD_TUNDRA){
								if (y >= (minY + 56 + graniteThicknessLevel/4 - rand.nextInt(3)) && y <= (minY + (56 + 36) + graniteThicknessLevel/8 - rand.nextInt(3)))
								{
									result.setBlock(x, y, z, Blocks.PERMAFROST.id());
									continue;
								}
							}
							if (y >= ((minY + 128 / 2) + graniteThicknessLevel - rand.nextInt(3)) && y <= (minY + 128 + graniteThicknessLevel - rand.nextInt(3)))
							{
								if (generateGraniteLayer)
								{
									result.setBlock(x, y, z, Blocks.GRANITE.id());
									continue;
								}
							}
							if (y >= ((minY + 128 / 2) + limestoneThicknessLevel - rand.nextInt(3)) && y <= (minY + 128 + limestoneThicknessLevel - rand.nextInt(3)))
							{
								if (generateLimestoneLayer)
								{
									result.setBlock(x, y, z, Blocks.LIMESTONE.id());
									continue;
								}
							}
							continue;
						}
					}

					// fill blocks with filler block until current layer level = -1 or air is encountered.
					if (currentLayerDepth > 0)
					{
						currentLayerDepth--;
						result.setBlock(x, y, z, fillerBlock);
					}

					// if reached end of filler layer, fill world with sandstone/permafrost if inside a glacier or desert biome
					if(currentLayerDepth == 0)
					{
						SubSurfaceLayer subSurfaceLayer = getSubSurfaceLayer(biome, fillerBlock, y, minY, oceanY, rand);
						if(subSurfaceLayer != null){
							currentLayerDepth = subSurfaceLayer.depth;
							fillerBlock = subSurfaceLayer.block;
						}
					}
				}
			}
		}
    }

	protected SubSurfaceLayer getSubSurfaceLayer(Biome biome, short fillerBlock, int y, int minY, int oceanY, Random rand)
	{
		if(biome == Biomes.OVERWORLD_DESERT && fillerBlock == Blocks.SAND.id()) {
			return new SubSurfaceLayer((short) Blocks.SANDSTONE.id(), rand.nextInt(8) + 2);
		} else if(biome == Biomes.OVERWORLD_GLACIER && fillerBlock == Blocks.BLOCK_SNOW.id()) {
			return new SubSurfaceLayer((short) Blocks.PERMAFROST.id(), rand.nextInt(8) + 14);
		}
		return null;
	}

	protected record SubSurfaceLayer(short block, int depth) { }

    @Override
    public void readFromNbt(CompoundTag tag) {

    }

    @Override
    public void writeToNbt(CompoundTag tag) {

    }
}
