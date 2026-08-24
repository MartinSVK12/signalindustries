package sunsetsatellite.signalindustries.dim.custom.surface;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.world.World;
import net.minecraft.core.world.noise.FractalNoise3D;
import net.minecraft.core.world.noise.ImprovedPerlinNoise;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;

public class SurfaceGeneratorRetro extends SurfaceGeneratorOverworld {
    public SurfaceGeneratorRetro(CustomDimensionData data, CompoundTag tag) {
        super(data, tag);
    }

    @Override
    public void init(World world) {
		super.init(world,
			new FractalNoise3D<>(ImprovedPerlinNoise.genOctaves(world.getRandomSeed(), 4, 40, ImprovedPerlinNoise.LegacyNoiseType.ALPHA)),
			new FractalNoise3D<>(ImprovedPerlinNoise.genOctaves(world.getRandomSeed(), 4, 44, ImprovedPerlinNoise.LegacyNoiseType.ALPHA)),
			new FractalNoise3D<>(ImprovedPerlinNoise.genOctaves(world.getRandomSeed(), 8, 32, ImprovedPerlinNoise.LegacyNoiseType.ALPHA)),
			false
		);
    }
}
