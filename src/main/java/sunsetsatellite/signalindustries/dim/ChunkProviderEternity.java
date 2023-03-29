package sunsetsatellite.signalindustries.dim;

import net.minecraft.shared.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.Sys;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.dim.worldgen.WorldGenEternalTreeFancy;
import sunsetsatellite.signalindustries.dim.worldgen.WorldGenEternityOre;
import sunsetsatellite.signalindustries.dim.worldgen.WorldGenObelisk;

public class ChunkProviderEternity extends ChunkProviderGenerateOverworld {
    public ChunkProviderEternity(World world, long l, int oceanHeight, int terrainMaxHeight, int terrainMiddle) {
        super(world, l, oceanHeight, terrainMaxHeight, terrainMiddle);

    }

    /*public Chunk provideChunk(int x, int z) {
        int l = 256 * Minecraft.WORLD_HEIGHT_BLOCKS;
        Chunk chunk = new Chunk(this.worldObj, new short[l], x, z);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);

        for(int i = 0; i < 256; ++i) {
            chunk.biome[i] = (byte)this.biomesForGeneration[i].id;
        }

        chunk.heightMap = new byte[256];
        chunk.func_1024_c();
        return chunk;
    }*/

    public void populate(IChunkProvider ichunkprovider, int chunkX, int chunkZ) {
        BlockSand.fallInstantly = true;
        int x = chunkX * 16;
        int z = chunkZ * 16;
        BiomeGenBase biomegenbase = this.worldObj.getWorldChunkManager().getBiomeGenAt(x + 16, z + 16);
        this.rand.setSeed(this.worldObj.getRandomSeed());
        long l1 = this.rand.nextLong() / 2L * 2L + 1L;
        long l2 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)chunkX * l1 + (long)chunkZ * l2 ^ this.worldObj.getRandomSeed());
        if(this.rand.nextInt(32) == 0){
            WorldGenerator worldgenerator = new WorldGenEternalTreeFancy(0,SignalIndustries.eternalTreeLog.blockID,8);
            int i11 = x + this.rand.nextInt(16) + 8;
            int l14 = z + this.rand.nextInt(16) + 8;
            worldgenerator.generate(this.worldObj, this.rand, i11, this.worldObj.getHeightValue(i11, l14), l14);
        }

        if(this.rand.nextInt(2) == 0) {
            int i = x + this.rand.nextInt(16) + 8;
            int j = z + this.rand.nextInt(16) + 8;
            new WorldGenObelisk().generate(this.worldObj, this.rand, i, this.worldObj.getHeightValue(i, j), j);
        }

        for(int k4 = 0; k4 < this.heightModifier; ++k4) {
            int i10 = x + this.rand.nextInt(16);
            int i11 = this.rand.nextInt(this.terrainMaxHeight / 8);
            int i12 = z + this.rand.nextInt(16);
            (new WorldGenEternityOre(SignalIndustries.dimensionalShardOre.blockID, 3)).generate(this.worldObj, this.rand, i10, i11, i12);
            //SignalIndustries.LOGGER.info(String.format("%d %d %d",i10,i11,i12));
        }


        BlockSand.fallInstantly = false;
    }

    @Override
    public void replaceBlocksForBiome(int chunkX, int chunkZ, short[] ashort0, BiomeGenBase[] abiomegenbase) {

    }

    public int[] generateTerrain(int chunkX, int chunkZ, short[] ashort0) {
        byte xzScale = 4;
        int[] heights = new int[256];
        int xSampleSize = xzScale + 1;
        int fullYHeight = (this.terrainMaxHeight - this.terrainMiddle) * 2;
        int yOffset = (fullYHeight - this.terrainMaxHeight) / 8;
        int ySampleSize = this.terrainMaxHeight / 8 + 1;
        int zSampleSize = xzScale + 1;
        this.field_4180_q = this.func_4061_a(this.field_4180_q, chunkX * xzScale, 0, chunkZ * xzScale, xSampleSize, ySampleSize, zSampleSize);

        for(int x = 0; x < xzScale; ++x) {
            for(int z = 0; z < xzScale; ++z) {
                for(int y = 0; y < this.terrainMaxHeight / 8; ++y) {
                    double d = 0.125;
                    double d1 = this.field_4180_q[((x) * zSampleSize + z) * ySampleSize + y + yOffset];
                    int i3 = ((x) * zSampleSize + z + 1) * ySampleSize;
                    double d2 = this.field_4180_q[i3 + y + yOffset];
                    int i = ((x + 1) * zSampleSize + z) * ySampleSize;
                    double d3 = this.field_4180_q[i + y + yOffset];
                    int i1 = ((x + 1) * zSampleSize + z + 1) * ySampleSize;
                    double d4 = this.field_4180_q[i1 + y + yOffset];
                    double d5 = (this.field_4180_q[((x) * zSampleSize + z) * ySampleSize + y + 1 + yOffset] - d1) * d;
                    double d6 = (this.field_4180_q[i3 + y + 1 + yOffset] - d2) * d;
                    double d7 = (this.field_4180_q[i + y + 1 + yOffset] - d3) * d;
                    double d8 = (this.field_4180_q[i1 + y + 1 + yOffset] - d4) * d;

                    for(int l1 = 0; l1 < 8; ++l1) {
                        double d9 = 0.25;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for(int i2 = 0; i2 < 4; ++i2) {
                            int j2 = i2 + x * 4 << Minecraft.WORLD_HEIGHT_BITS + 4 | z * 4 << Minecraft.WORLD_HEIGHT_BITS | y * 8 + l1;
                            int c = Minecraft.WORLD_HEIGHT_BLOCKS;
                            double d14 = 0.25;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;

                            for(int k2 = 0; k2 < 4; ++k2) {
                                int l2 = 0;

                                if (d15 > 0.0) {
                                    l2 = SignalIndustries.realityFabric.blockID;
                                }

                                ashort0[j2] = (short)l2;
                                j2 += c;
                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }

        return heights;
    }
}
