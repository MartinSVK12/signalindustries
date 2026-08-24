package sunsetsatellite.signalindustries.dim.custom;

import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.biome.provider.BiomeProvider;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.type.WorldType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class WorldTypeWrapper extends WorldType {

    public CustomDimensionData data;

    public WorldTypeWrapper(CustomDimensionData data) {
        super(data.getWorldTypeProperties());
        this.data = data;
    }

    @Override
    public @NonNull BiomeProvider createBiomeProvider(World world) {
        return data.getBiomeProvider(world);
    }

	@Override
	public @NotNull Biome @NotNull [] allBiomes() {
		return new Biome[]{Biomes.OVERWORLD_PLAINS};
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
