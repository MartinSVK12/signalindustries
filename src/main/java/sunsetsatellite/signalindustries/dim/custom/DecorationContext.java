package sunsetsatellite.signalindustries.dim.custom;

import net.minecraft.core.world.biome.Biome;
import sunsetsatellite.signalindustries.dim.custom.decorator.ChunkDecoratorCustom;

import java.util.Random;

public class DecorationContext {
    public int chunkX;
    public int chunkZ;
    public int x;
    public int y;
    public int z;
    public int minY;
    public int maxY;
    public int rangeY;
    public float oreHeightModifier;
    public Random rand;
    public Random swampRand;
    public Biome biome;
    public ChunkDecoratorCustom decorator;

    public DecorationContext(ChunkDecoratorCustom decorator, int chunkX, int chunkZ, int x, int y, int z, int minY, int maxY, int rangeY, float oreHeightModifier, Random rand, Random swampRand, Biome biome) {
        this.decorator = decorator;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.x = x;
        this.y = y;
        this.z = z;
        this.minY = minY;
        this.maxY = maxY;
        this.rangeY = rangeY;
        this.oreHeightModifier = oreHeightModifier;
        this.rand = rand;
        this.swampRand = swampRand;
        this.biome = biome;
    }
}
