package sunsetsatellite.signalindustries.dim;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.World;
import net.minecraft.src.WorldChunkManager;

import java.util.Arrays;

public class WorldChunkManagerEternity extends WorldChunkManager {
    private final BiomeGenBase field_4201_e;
    private final double field_4200_f;
    private final double field_4199_g;

    public WorldChunkManagerEternity(World world, BiomeGenBase biomegenbase, double d, double d1) {
        this.field_4201_e = biomegenbase;
        this.field_4200_f = d;
        this.field_4199_g = d1;
        this.worldObj = world;
    }

    public BiomeGenBase getBiomeGenAtChunkCoord(ChunkCoordIntPair chunkcoordintpair) {
        return this.field_4201_e;
    }

    public BiomeGenBase getBiomeGenAt(int i, int j) {
        return this.field_4201_e;
    }

    public double getTemperature(int i, int j) {
        return this.field_4200_f;
    }

    public BiomeGenBase[] func_4069_a(int i, int j, int k, int l) {
        this.field_4195_d = this.loadBlockGeneratorData(this.field_4195_d, i, j, k, l);
        return this.field_4195_d;
    }

    public BiomeGenBase[] func_4069_a_height(int i, int j, int k, int l) {
        return this.func_4069_a(i, j, k, l);
    }

    public BiomeGenBase[] func_4069_a_height_override(int i, int y, int j, int k, int l) {
        return this.func_4069_a(i, j, k, l);
    }

    public double[] getTemperatures(double[] ad, int i, int j, int k, int l) {
        if (ad == null || ad.length < k * l) {
            ad = new double[k * l];
        }

        Arrays.fill(ad, 0, k * l, this.field_4200_f);
        return ad;
    }

    public BiomeGenBase[] updateBlockGeneratorDataFromHeight(BiomeGenBase[] abiomegenbase, int[] heights, int oceanHeight, int maxTerrainHeight, int x, int y, int width, int length) {
        if (abiomegenbase == null || abiomegenbase.length < width * length) {
            abiomegenbase = new BiomeGenBase[width * length];
        }

        if (this.temperature == null || this.temperature.length < width * length) {
            this.temperature = new double[width * length];
            this.humidity = new double[width * length];
        }

        Arrays.fill(abiomegenbase, 0, width * length, this.field_4201_e);
        Arrays.fill(this.humidity, 0, width * length, this.field_4199_g);
        Arrays.fill(this.temperature, 0, width * length, this.field_4200_f);
        return abiomegenbase;
    }

    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] abiomegenbase, int i, int j, int k, int l) {
        if (abiomegenbase == null || abiomegenbase.length < k * l) {
            abiomegenbase = new BiomeGenBase[k * l];
        }

        if (this.temperature == null || this.temperature.length < k * l) {
            this.temperature = new double[k * l];
            this.humidity = new double[k * l];
        }

        Arrays.fill(abiomegenbase, 0, k * l, this.field_4201_e);
        Arrays.fill(this.humidity, 0, k * l, this.field_4199_g);
        Arrays.fill(this.temperature, 0, k * l, this.field_4200_f);
        return abiomegenbase;
    }
}
