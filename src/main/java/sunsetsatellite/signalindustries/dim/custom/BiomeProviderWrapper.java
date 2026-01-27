package sunsetsatellite.signalindustries.dim.custom;

import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.provider.BiomeProvider;

public class BiomeProviderWrapper extends BiomeProvider {

    public CustomDimensionData data;
    public BiomeProvider provider;

    public BiomeProviderWrapper(World world, CustomDimensionData data) {
        this.data = data;
        this.provider = data.properties.biomeProvider.get(world);
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        return provider.getBiome(x, y, z);
    }

    @Override
    public double getTemperature(int x, int z) {
        return provider.getTemperature(x, z);
    }

    @Override
    public double getHumidity(int x, int z) {
        return provider.getHumidity(x, z);
    }

    @Override
    public double getVariety(int x, int z) {
        return provider.getVariety(x, z);
    }

    @Override
    public double getBiomeness(int x, int y, int z) {
        return provider.getBiomeness(x, y, z);
    }

    @Override
    public Biome[] getBiomes(Biome[] biomes, int x, int y, int z, int xSize, int ySize, int zSize) {
        return provider.getBiomes(biomes, x, y, z, xSize, ySize, zSize);
    }

    @Override
    public Biome[] getBiomes(Biome[] biomes, double[] ds, double[] es, double[] fs, int i, int j, int k, int l, int m, int n) {
        return provider.getBiomes(biomes, ds, es, fs, i, j, k, l, m, n);
    }

    @Override
    public double[] getTemperatures(double[] ds, int i, int j, int k, int l) {
        return provider.getTemperatures(ds, i, j, k, l);
    }

    @Override
    public double[] getHumidities(double[] ds, int i, int j, int k, int l) {
        return provider.getHumidities(ds, i, j, k, l);
    }

    @Override
    public double[] getVarieties(double[] ds, int i, int j, int k, int l) {
        return provider.getVarieties(ds, i, j, k, l);
    }

    @Override
    public double[] getBiomenesses(double[] ds, int i, int j, int k, int l, int m, int n) {
        return provider.getBiomenesses(ds, i, j, k, l, m, n);
    }

    @Override
    public Biome lookupBiome(double d, double e, double f, double g) {
        return provider.lookupBiome(d, e, f, g);
    }
}
