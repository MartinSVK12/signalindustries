package sunsetsatellite.signalindustries.dim.custom;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.biome.Biome;
import sunsetsatellite.signalindustries.dim.custom.property.DimPropertyBlock;
import sunsetsatellite.signalindustries.dim.custom.property.DimPropertyInt;

public class BiomeCustom extends Biome {

    public CustomDimensionData data;

    public BiomeCustom(String key, CustomDimensionData data, CompoundTag nbt) {
        super(key);
        this.data = data;
        readFromNbt(nbt);
    }

    public void readFromNbt(CompoundTag nbt) {
        color = data.getProperty("Color", nbt, DimPropertyInt.class);
        topBlock = data.getProperty("TopBlock", nbt, DimPropertyBlock.class);
        fillerBlock = data.getProperty("FillerBlock", nbt, DimPropertyBlock.class);
    }

    public void writeToNbt(CompoundTag nbt) {
        data.saveProperty("Color", new DimPropertyInt(color), nbt);
        data.saveProperty("TopBlock", new DimPropertyBlock(Blocks.getBlock(topBlock)), nbt);
        data.saveProperty("FillerBlock", new DimPropertyBlock(Blocks.getBlock(topBlock)), nbt);
    }
}
