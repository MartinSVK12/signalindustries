package sunsetsatellite.signalindustries.dim.custom;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.provider.BiomeProvider;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.type.WorldType;

public class WorldTypeCustom extends WorldType {

    public CustomDimensionData data;

    public WorldTypeCustom(CustomDimensionData data) {
        super(data.getWorldTypeProperties());
        this.data = data;
    }

    @Override
    public BiomeProvider createBiomeProvider(World world) {
        return data.getBiomeProvider(world);
    }

    @Override
    public ChunkGenerator createChunkGenerator(World world) {
        return data.getChunkGenerator(world);
    }

    @Override
    public boolean isValidSpawn(World world, int x, int y, int z) {
        return true;
    }

    @Override
    public float getCelestialAngle(World world, long tick, float partialTick) {
        return 0;
    }

    @Override
    public int getSkyDarken(World world, long tick, float partialTick) {
        return 0;
    }
}
