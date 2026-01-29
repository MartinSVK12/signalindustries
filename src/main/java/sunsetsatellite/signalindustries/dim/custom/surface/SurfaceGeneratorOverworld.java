package sunsetsatellite.signalindustries.dim.custom.surface;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;
import net.minecraft.core.world.noise.BasePerlinNoise;
import net.minecraft.core.world.noise.PerlinNoise;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;

import java.util.Random;

public class SurfaceGeneratorOverworld extends SurfaceGeneratorBase {

    public BasePerlinNoise<?> beachNoise;
    public BasePerlinNoise<?> soilNoise;
    public BasePerlinNoise<?> mainNoise;
    public boolean generateStoneVariants;
    public World world;

    public SurfaceGeneratorOverworld(CustomDimensionData data, CompoundTag tag) {
        super(data, tag);
    }

    @Override
    public void init(World world) {
        init(world,  new PerlinNoise(world.getRandomSeed(), 4, 40), new PerlinNoise(world.getRandomSeed(), 4, 44), new PerlinNoise(world.getRandomSeed(), 8, 32), true);
    }

    public void init(World world, BasePerlinNoise<?> beachNoise, BasePerlinNoise<?> soilNoise, BasePerlinNoise<?> mainNoise, boolean generateStoneVariants) {
        this.world = world;
        this.beachNoise = beachNoise;
        this.soilNoise = soilNoise;
        this.mainNoise = mainNoise;
        this.generateStoneVariants = generateStoneVariants;
    }

    @Override
    public void generateSurface(Chunk chunk, ChunkGeneratorResult result) {
        int oceanY = this.world.getWorldType().getOceanY();
        int minY = this.world.getWorldType().getMinY();
        int maxY = this.world.getWorldType().getMaxY();
        int terrainHeight = maxY + 1 - minY;
        int chunkX = chunk.xPosition;
        int chunkZ = chunk.zPosition;
        int oceanBlock = this.world.getWorldType().getOceanBlockId();
        int worldFillBlock = this.world.getWorldType().getFillerBlockId();
        Random rand = new Random((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        double beachScale = 0.03125F;
        double[] sandBeachNoise = this.beachNoise.get(null, chunkX * 16, chunkZ * 16, 0.0F, 16, 16, 1, beachScale, beachScale, 1.0F);
        double[] gravelBeachNoise = this.beachNoise.get(null, chunkX * 16, 109.0134, chunkZ * 16, 16, 1, 16, beachScale, 1.0F, beachScale);
        double[] soilThicknessNoise = this.soilNoise.get(null, chunkX * 16, chunkZ * 16, 0.0F, 16, 16, 1, beachScale * (double)2.0F, beachScale * (double)2.0F, beachScale * (double)2.0F);
        double[] stoneLayerNoise = null;
        double[] stoneLayerNoiseGranite = null;
        double[] stoneLayerNoiseLimestone = null;
        if (this.generateStoneVariants) {
            stoneLayerNoise = this.soilNoise.get(null, chunkX * 16, chunkZ * 16, 0.0F, 16, 16, 1, beachScale * (double)4.0F, beachScale * (double)4.0F, beachScale * (double)4.0F);
            stoneLayerNoiseGranite = this.mainNoise.get(null, chunkX * 16, chunkZ * 16, 0.0F, 16, 16, 1, beachScale * (double)4.0F, beachScale * (double)4.0F, beachScale * (double)4.0F);
            stoneLayerNoiseLimestone = this.beachNoise.get(null, chunkX * 16, chunkZ * 16, 0.0F, 16, 16, 1, beachScale * (double)4.0F, beachScale * (double)4.0F, beachScale * (double)4.0F);
        }

        for(int z = 0; z < 16; ++z) {
            for(int x = 0; x < 16; ++x) {
                boolean generateSandBeach = sandBeachNoise[z + x * 16] + rand.nextDouble() * 0.2 > (double)0.0F;
                boolean generateGravelBeach = gravelBeachNoise[z + x * 16] + rand.nextDouble() * 0.2 > (double)3.0F;
                int soilThickness = (int)(soilThicknessNoise[z + x * 16] / (double)3.0F + (double)3.0F + rand.nextDouble() * (double)0.25F);
                boolean generateBasaltLayer = false;
                boolean generateGraniteLayer = false;
                boolean generateLimestoneLayer = false;
                int basaltThicknessLevel = 0;
                int graniteThicknessLevel = 0;
                int limestoneThicknessLevel = 0;
                if (this.generateStoneVariants) {
                    generateBasaltLayer = stoneLayerNoise[z + x * 16] + rand.nextDouble() * 0.2 > (double)0.0F;
                    generateGraniteLayer = stoneLayerNoiseGranite[z + x * 16] + rand.nextDouble() * 0.2 > (double)2.0F;
                    generateLimestoneLayer = stoneLayerNoiseLimestone[z + x * 16] + rand.nextDouble() * 0.2 > (double)3.0F;
                    basaltThicknessLevel = (int)(stoneLayerNoise[z + x] + rand.nextDouble() * (double)0.5F);
                    graniteThicknessLevel = (int)(stoneLayerNoiseGranite[z + x] + rand.nextDouble() * (double)0.5F);
                    limestoneThicknessLevel = (int)(stoneLayerNoiseLimestone[z + x] + rand.nextDouble() * (double)0.5F);
                }

                int currentLayerDepth = -1;
                short topBlock = -1;
                short fillerBlock = -1;
                Biome lastBiome = null;

                for(int y = maxY; y >= minY; --y) {
                    Biome biome = chunk.getBlockBiome(x, y, z);
                    if (biome == null) {
                        biome = this.world.getBiomeProvider().getBiome(chunkX * 16 + x, y >> 3, chunkZ * 16 + z);
                    }

                    int block = result.getBlock(x, y, z);
                    if ((biome != lastBiome || topBlock == -1 || fillerBlock == -1) && block == 0) {
                        topBlock = biome.topBlock;
                        fillerBlock = biome.fillerBlock;
                    }

                    lastBiome = biome;
                    if (block == 0) {
                        currentLayerDepth = -1;
                    } else if (block == worldFillBlock) {
                        if (currentLayerDepth == -1) {
                            if (soilThickness <= 0) {
                                topBlock = 0;
                                fillerBlock = (short)worldFillBlock;
                            } else {
                                boolean biomeGeneratesMud = biome == Biomes.OVERWORLD_CAATINGA_PLAINS || biome == Biomes.OVERWORLD_CAATINGA || biome == Biomes.OVERWORLD_SWAMPLAND || biome == Biomes.OVERWORLD_SWAMPLAND_MUDDY;
                                if (y >= minY + oceanY - 4 && y <= minY + oceanY + 1) {
                                    topBlock = biome.topBlock;
                                    fillerBlock = biome.fillerBlock;
                                    if (biomeGeneratesMud) {
                                        topBlock = (short) Blocks.MUD.id();
                                        fillerBlock = (short)Blocks.MUD.id();
                                    } else if (generateGravelBeach) {
                                        topBlock = 0;
                                        fillerBlock = (short)Blocks.GRAVEL.id();
                                    } else if (generateSandBeach) {
                                        topBlock = (short)Blocks.SAND.id();
                                        fillerBlock = (short)Blocks.SAND.id();
                                    }
                                } else if (y <= oceanY && biomeGeneratesMud) {
                                    topBlock = (short)Blocks.MUD.id();
                                    fillerBlock = (short)Blocks.MUD.id();
                                }
                            }

                            if (y < minY + oceanY && topBlock == 0) {
                                topBlock = (short)oceanBlock;
                            }

                            currentLayerDepth = soilThickness;
                            if (y >= minY + oceanY - 1) {
                                result.setBlock(x, y, z, topBlock);
                            } else {
                                result.setBlock(x, y, z, fillerBlock);
                            }
                        } else if (this.generateStoneVariants && currentLayerDepth <= 0) {
                            if (y >= minY + basaltThicknessLevel - rand.nextInt(3) && y <= minY + 30 + basaltThicknessLevel - rand.nextInt(3) && generateBasaltLayer) {
                                result.setBlock(x, y, z, Blocks.BASALT.id());
                            } else if (biome == Biomes.OVERWORLD_GLACIER && y >= minY + 56 + graniteThicknessLevel / 4 - rand.nextInt(3) && y <= maxY) {
                                result.setBlock(x, y, z, Blocks.PERMAFROST.id());
                            } else if (biome == Biomes.OVERWORLD_TUNDRA && y >= minY + 56 + graniteThicknessLevel / 4 - rand.nextInt(3) && y <= minY + 92 + graniteThicknessLevel / 8 - rand.nextInt(3)) {
                                result.setBlock(x, y, z, Blocks.PERMAFROST.id());
                            } else if (y >= minY + 64 + graniteThicknessLevel - rand.nextInt(3) && y <= minY + 128 + graniteThicknessLevel - rand.nextInt(3) && generateGraniteLayer) {
                                result.setBlock(x, y, z, Blocks.GRANITE.id());
                            } else if (y >= minY + 64 + limestoneThicknessLevel - rand.nextInt(3) && y <= minY + 128 + limestoneThicknessLevel - rand.nextInt(3) && generateLimestoneLayer) {
                                result.setBlock(x, y, z, Blocks.LIMESTONE.id());
                            }
                        } else {
                            if (currentLayerDepth > 0) {
                                --currentLayerDepth;
                                result.setBlock(x, y, z, fillerBlock);
                            }

                            if (currentLayerDepth == 0) {
                                if (biome == Biomes.OVERWORLD_DESERT && fillerBlock == Blocks.SAND.id()) {
                                    currentLayerDepth = rand.nextInt(8) + 2;
                                    fillerBlock = (short)Blocks.SANDSTONE.id();
                                } else if (biome == Biomes.OVERWORLD_GLACIER && fillerBlock == Blocks.BLOCK_SNOW.id()) {
                                    currentLayerDepth = rand.nextInt(8) + 14;
                                    fillerBlock = (short)Blocks.PERMAFROST.id();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void readFromNbt(CompoundTag tag) {

    }

    @Override
    public void writeToNbt(CompoundTag tag) {

    }
}
