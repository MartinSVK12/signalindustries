package sunsetsatellite.signalindustries.dim;

import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;
import net.minecraft.core.world.generate.feature.WorldFeatureInterface;
import net.minecraft.core.world.pos.TilePos;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.worldgen.WorldFeatureDilithiumCrystal;
import sunsetsatellite.signalindustries.worldgen.WorldFeatureEternalTree;
import sunsetsatellite.signalindustries.worldgen.WorldFeatureObelisk;

import java.util.Random;

public class ChunkDecoratorEternity implements ChunkDecorator {

    public World world;
    public Random random = new Random();

    public ChunkDecoratorEternity(World world) {
        this.world = world;
        this.random.setSeed(world.getRandomSeed());
    }

    @Override
    public void decorate(Chunk chunk) {
        int x = chunk.pos.x * 16;
        int z = chunk.pos.z * 16;
        int y = this.world.getHeightValue(x, z);
        if (random.nextInt(16) == 0) {
            WorldFeatureInterface tree = new WorldFeatureEternalTree(0, SIBlocks.eternalTreeLog.id());
            tree.place(world, random, new TilePos(x, y, z));
        }
        if (random.nextInt(128) == 0) {
			WorldFeatureInterface obelisk = new WorldFeatureObelisk();
            obelisk.place(world, random, new TilePos(x, y, z));
        }
        if (random.nextInt(64) == 0) {
			WorldFeatureInterface crystal = new WorldFeatureDilithiumCrystal();
            crystal.place(world, random, new TilePos(x, y, z));
        }
    }
}
