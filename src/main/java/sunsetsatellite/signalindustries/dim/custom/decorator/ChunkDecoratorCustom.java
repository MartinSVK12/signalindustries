package sunsetsatellite.signalindustries.dim.custom.decorator;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.block.BlockLogicFallingBlock;
import net.minecraft.core.block.BlockLogicSoulSand;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;
import sunsetsatellite.signalindustries.dim.custom.DecorationContext;
import sunsetsatellite.signalindustries.dim.custom.DimensionRegistries;
import sunsetsatellite.signalindustries.dim.custom.feature.WorldFeatureBase;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChunkDecoratorCustom extends ChunkDecoratorBase {

    public final List<WorldFeatureBase> features = new ArrayList<>();

    public ChunkDecoratorCustom(CustomDimensionData data, CompoundTag tag) {
        super(data, tag);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        if(tag.containsKey("Features")){
            ListTag featuresTag = tag.getList("Features");
            for (Tag<?> t : featuresTag) {
                CompoundTag featureTag = ((CompoundTag) t);
                String id = featureTag.getString("Type");
                CompoundTag featureData = featureTag.getCompound("Data");
                Class<? extends WorldFeatureBase> featureClass = DimensionRegistries.FEATURES.getItem(id);
                try {
                    WorldFeatureBase feature = featureClass.getDeclaredConstructor(CustomDimensionData.class, CompoundTag.class).newInstance(this.data, featureData);
                    features.add(feature);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        ListTag featuresTag = new ListTag();
        for (WorldFeatureBase feature : features) {
            CompoundTag featureTag = new CompoundTag();
            featureTag.putString("Type", DimensionRegistries.FEATURES.getKey(feature.getClass()));
            CompoundTag featureDataTag = new CompoundTag();
            feature.writeToNbt(featureDataTag);
            featureTag.put("Data", featureDataTag);
            featuresTag.addTag(featureTag);
        }
        tag.put("Features", featuresTag);
    }

    @Override
    public void decorate(Chunk chunk) {
        this.world.scheduledUpdatesAreImmediate = true;
        BlockLogicFallingBlock.fallInstantly = true;
        int chunkX = chunk.pos.x;
        int chunkZ = chunk.pos.z;
        int minY = this.world.getWorldType().getMinY(world);
        int maxY = this.world.getWorldType().getMaxY(world);
        int rangeY = maxY + 1 - minY;
        float oreHeightModifier = (float)rangeY / 128.0F;
        int x = chunkX * 16;
        int z = chunkZ * 16;
        int y = this.world.getHeightValue(x + 16, z + 16);
        Biome biome = this.world.getBlockBiome(x + 16, y, z + 16);
        Random rand = new Random(this.world.getRandomSeed());
        long l1 = rand.nextLong() / 2L * 2L + 1L;
        long l2 = rand.nextLong() / 2L * 2L + 1L;
        rand.setSeed((long)chunkX * l1 + (long)chunkZ * l2 ^ this.world.getRandomSeed());
        Random swampRand = new Random((long)chunkX * l1 + (long)chunkZ * l2 ^ this.world.getRandomSeed());
        DecorationContext ctx = new DecorationContext(this,
                chunkX, chunkZ, x, y, z, minY, maxY, rangeY, oreHeightModifier, rand, swampRand, biome
        );
        for (WorldFeatureBase feature : features) {
            feature.place(this.world, rand, x, y, z, ctx);
        }

		BlockLogicFallingBlock.fallInstantly = false;
        this.world.scheduledUpdatesAreImmediate = false;
    }

}
