package sunsetsatellite.signalindustries.dim.custom.generator;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.LargeFeature;
import net.minecraft.core.world.generate.chunk.ChunkGeneratorResult;
import net.minecraft.core.world.noise.CombinedPerlinNoise;
import net.minecraft.core.world.noise.PerlinNoise;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;
import sunsetsatellite.signalindustries.dim.custom.DimensionRegistries;
import sunsetsatellite.signalindustries.dim.custom.surface.SurfaceGeneratorBase;
import sunsetsatellite.signalindustries.dim.custom.surface.SurfaceGeneratorEmpty;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ChunkGeneratorClassic extends ChunkGeneratorBase {

    public CombinedPerlinNoise combinedA;
    public CombinedPerlinNoise combinedB;
    public CombinedPerlinNoise combinedC;
    public CombinedPerlinNoise combinedD;
    public PerlinNoise octavesA;
    public PerlinNoise octavesB;

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
        this.combinedA = new CombinedPerlinNoise(new PerlinNoise(seed, 8, 0), new PerlinNoise(seed, 8, 8));
        this.combinedB = new CombinedPerlinNoise(new PerlinNoise(seed, 8, 16), new PerlinNoise(seed, 8, 24));
        this.combinedC = new CombinedPerlinNoise(new PerlinNoise(seed, 8, 32), new PerlinNoise(seed, 8, 40));
        this.combinedD = new CombinedPerlinNoise(new PerlinNoise(seed, 8, 48), new PerlinNoise(seed, 8, 56));
        this.octavesA = new PerlinNoise(seed, 6, 64);
        this.octavesB = new PerlinNoise(seed, 8, 70);

        sg.init(world);
    }

    @Override
    public ChunkGeneratorResult doBlockGeneration(Chunk chunk) {
        ChunkGeneratorResult result = new ChunkGeneratorResult();
        World world = chunk.world;

        int chunkX = chunk.xPosition;
        int chunkZ = chunk.zPosition;
        //float mod = 1.3F;
        int[] heightMap = new int[256];

        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                double noiseA = this.combinedA.get((float)(chunkX * 16 + x) * 1.3F, (float)(chunkZ * 16 + z) * 1.3F) / (double)6.0F + (double)-4.0F;
                double noiseB = this.combinedB.get((float)(chunkX * 16 + x) * 1.3F, (float)(chunkZ * 16 + z) * 1.3F) / (double)5.0F + (double)10.0F + (double)-4.0F;
                if (this.octavesA.get(chunkX * 16 + x, chunkZ * 16 + z) / (double)8.0F > (double)0.0F) {
                    noiseB = noiseA;
                }

                double height;
                if ((height = Math.max(noiseA, noiseB) / (double)2.0F) < (double)0.0F) {
                    height *= 0.8;
                }

                heightMap[x + z * 16] = (int)height;
            }
        }

        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                double val = this.combinedC.get(chunkX * 16 + x << 1, chunkZ * 16 + z << 1) / (double)8.0F;
                int val2 = this.combinedD.get(chunkX * 16 + x << 1, chunkZ * 16 + z << 1) > (double)0.0F ? 1 : 0;
                if (val > (double)2.0F) {
                    int newHeight = ((heightMap[x + z * 16] - val2) / 2 << 1) + val2;
                    heightMap[x + z * 16] = newHeight;
                }
            }
        }

        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                int val = (int)(this.octavesB.get(chunkX * 16 + x, chunkZ * 16 + z) / (double)24.0F) - 4;
                int newHeight;
                int val2 = (newHeight = heightMap[x + z * 16] + world.getWorldType().getOceanY()) + val;
                heightMap[x + z * 16] = Math.max(newHeight, val2);
                if (heightMap[x + z * 16] > world.getWorldType().getMaxY() - 2) {
                    heightMap[x + z * 16] = world.getWorldType().getMaxY() - 2;
                }

                if (heightMap[x + z * 16] < 1) {
                    heightMap[x + z * 16] = 1;
                }

                for(int y = world.getWorldType().getMinY(); y < world.getWorldType().getMaxY(); ++y) {
                    //Chunk.makeBlockIndex(x, y, z);
                    int blockID = 0;
                    if (y < newHeight) {
                        blockID = world.getWorldType().getFillerBlockId();
                    } else if (y < world.getWorldType().getOceanY()) {
                        blockID = world.getWorldType().getOceanBlockId();
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
