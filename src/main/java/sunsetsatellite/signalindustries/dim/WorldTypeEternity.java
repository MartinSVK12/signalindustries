package sunsetsatellite.signalindustries.dim;

import net.minecraft.core.Global;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.provider.BiomeProvider;
import net.minecraft.core.world.biome.provider.BiomeProviderSingleBiome;
import net.minecraft.core.world.config.season.SeasonConfig;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.season.Seasons;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.weather.Weather;
import net.minecraft.core.world.weather.Weathers;
import net.minecraft.core.world.wind.WindProviderGeneric;
import sunsetsatellite.signalindustries.SIBiomes;
import sunsetsatellite.signalindustries.SIBlocks;

public class WorldTypeEternity extends WorldType {
    public WorldTypeEternity(String languageKey) {
        super(Properties.of(languageKey)
                .defaultWeather(Weathers.OVERWORLD_CLEAR)
                .windManager(new WindProviderGeneric())
                .brightnessRamp(getLightRamp())
                .seasonConfig(SeasonConfig.builder().withSingleSeason(Seasons.NULL).build())
                .dayNightCycleTicks(Global.DAY_LENGTH_TICKS)
        );
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
    public int getOceanBlockId() {
        return 0;
    }

    @Override
    public int getFillerBlockId() {
        return SIBlocks.realityFabric.id();
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
    public float getCelestialAngle(World world, long l, float f) {
        return 0.5f;
    }

    @Override
    public int getSkyDarken(World world, long l, float f) {
        return 0;
    }


    @Override
    public boolean mayRespawn() {
        return false;
    }
}
