package sunsetsatellite.signalindustries.dim.custom.surface;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.world.World;
import net.minecraft.core.world.noise.RetroPerlinNoise;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;

public class SurfaceGeneratorRetro extends SurfaceGeneratorOverworld {
    public SurfaceGeneratorRetro(CustomDimensionData data, CompoundTag tag) {
        super(data, tag);
    }

    @Override
    public void init(World world) {
        super.init(world, new RetroPerlinNoise(world.getRandomSeed(), 4, 40), new RetroPerlinNoise(world.getRandomSeed(), 4, 44), new RetroPerlinNoise(world.getRandomSeed(), 8, 32), false);
    }
}
