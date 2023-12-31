package sunsetsatellite.signalindustries.worldgen;


import net.minecraft.core.block.Block;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeature;
import sunsetsatellite.signalindustries.entities.ExplosionNoDrops;

import java.util.Random;

public class WorldFeatureMeteor extends WorldFeature {

    public int oreId;
    public int oreMeta;
    public int oreChance;

    public WorldFeatureMeteor(int oreId, int oreMeta, int oreChance){
        this.oreId = oreId;
        this.oreMeta = oreMeta;
        this.oreChance = oreChance;
    }
    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        ExplosionNoDrops ex = new ExplosionNoDrops(world,null,i,j,k,50f);
        ex.doExplosionA();
        ex.doExplosionB(false);
        //world.setBlockWithNotify(i,j,k, Block.bedrock.id);

        int radius = 4;
        int blockRadius = 4;
        int oreBlocks = 0;

        for(int x = -blockRadius; x <= blockRadius; ++x) {
            for(int y = -blockRadius; y <= blockRadius; ++y) {
                for(int z = -blockRadius; z <= blockRadius; ++z) {
                    if (isPointInsideSphere(x, y, z, radius)) {
                        if (oreId != 0 && random.nextInt(100) < oreChance){
                            world.setBlockAndMetadataWithNotify(x+i, (y+j)-8, z+k, oreId, oreMeta);
                            oreBlocks++;
                        } else {
                            world.setBlockAndMetadataWithNotify(x+i, (y+j)-8, z+k, Block.basalt.id, 0);
                        }
                    }
                }
            }
        }

        //SignalIndustries.LOGGER.info("Meteor contains "+oreBlocks+" ore.");
        return true;
    }

    public boolean isPointInsideSphere(int x, int y, int z, double radius) {
        return x*x + y*y + z*z < radius*radius;
    }

}
