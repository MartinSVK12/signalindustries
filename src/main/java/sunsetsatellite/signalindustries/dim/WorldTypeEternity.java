package sunsetsatellite.signalindustries.dim;

import net.minecraft.core.Global;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.provider.BiomeProvider;
import net.minecraft.core.world.biome.provider.BiomeProviderSingleBiome;
import net.minecraft.core.world.config.season.SeasonConfig;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.season.Seasons;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.core.world.wind.WindManagerGeneric;
import sunsetsatellite.signalindustries.SIBiomes;
import sunsetsatellite.signalindustries.SIBlocks;

public class WorldTypeEternity extends WorldType {
    public WorldTypeEternity(String languageKey) {
        super(languageKey, Weather.overworldClear, new WindManagerGeneric(), false, getLightRamp(), SeasonConfig.builder().withSingleSeason(Seasons.NULL).build());
    }

    private static float[] getLightRamp() {
        float[] brightnessRamp = new float[32];
        float f = 0.1F;

        for(int i = 0; i <= 15; ++i) {
            float f1 = 1.0F - (float)i / 15.0F;
            brightnessRamp[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
        }

        return brightnessRamp;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getMaxY() {
        return 255;
    }

    @Override
    public int getOceanY() {
        return 32;
    }

    @Override
    public int getOceanBlock() {
        return 0;
    }

    @Override
    public int getFillerBlock() {
        return SIBlocks.realityFabric.id;
    }

    @Override
    public BiomeProvider createBiomeProvider(World world) {
        return new BiomeProviderSingleBiome(SIBiomes.biomeEternity, 1.0, 1.0, 1.0);
    }

    @Override
    public ChunkGenerator createChunkGenerator(World world) {
        return new ChunkGeneratorEternity(world);
    }

    @Override
    public boolean isValidSpawn(World world, int i, int j, int k) {
        return true;
    }

    @Override
    public int getDayNightCycleLengthTicks() {
        return Global.DAY_LENGTH_TICKS;
    }

    @Override
    public float getCelestialAngle(World world, long l, float f) {
        return 0.5f;
    }

    @Override
    public float[] getSunriseColor(float f, float g) {
        return new float[4];
    }

    @Override
    public int getSkyDarken(World world, long l, float f) {
        return 0;
    }

    @Override
    public Vec3d getFogColor(World world, double d, double e, double f, float g, float h) {
        return Vec3d.createVector(0.70, 0.70, 0.70);
    }

    @Override
    public boolean mayRespawn() {
        return false;
    }

    @Override
    public float getCloudHeight() {
        return 0;
    }

    @Override
    public boolean hasGround() {
        return false;
    }
}
