package sunsetsatellite.signalindustries.dim;

import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;
import net.minecraft.core.world.generate.feature.WorldFeature;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.worldgen.WorldFeatureDilithiumCrystal;
import sunsetsatellite.signalindustries.worldgen.WorldFeatureEternalTree;
import sunsetsatellite.signalindustries.worldgen.WorldFeatureObelisk;

import java.util.Random;

public class ChunkDecoratorEternity implements ChunkDecorator {

    public World world;

    public ChunkDecoratorEternity(World world) {
        this.world = world;
    }

    @Override
    public void decorate(Chunk chunk) {
        Random random = new Random();
        int x = chunk.xPosition * 16;
        int z = chunk.zPosition * 16;
        int y = this.world.getHeightValue(x, z);
        if(random.nextInt(32) == 0){
            WorldFeature tree = new WorldFeatureEternalTree(0, SignalIndustries.eternalTreeLog.id);
            tree.generate(world,random,x,y,z);
        }
        if(random.nextInt(128) == 0){
            WorldFeature obelisk = new WorldFeatureObelisk();
            obelisk.generate(world,random,x,y,z);
        }
        if(random.nextInt(64) == 0){
            WorldFeature crystal = new WorldFeatureDilithiumCrystal();
            crystal.generate(world,random,x,y,z);
        }
    }
}
