package sunsetsatellite.signalindustries.worldgen;


import net.minecraft.core.block.Block;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeature;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.ExplosionNoDrops;

import java.util.Random;

public class WorldFeatureGeode extends WorldFeature {

    public int oreId;
    public int oreMeta;
    public int oreChance;
    public int radius = 4;

    public WorldFeatureGeode(int oreId, int oreMeta, int oreChance){
        this.oreId = oreId;
        this.oreMeta = oreMeta;
        this.oreChance = oreChance;
    }

    public WorldFeatureGeode(int oreId, int oreMeta, int oreChance, int radius){
        this.oreId = oreId;
        this.oreMeta = oreMeta;
        this.oreChance = oreChance;
        this.radius = radius;
    }
    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        //SignalIndustries.LOGGER.info(String.format("%s Geode at X:%d Y:%d Z:%d", I18n.getInstance().translateNameKey(Block.blocksList[oreId].getLanguageKey(oreMeta)),i,j,k));
        int oreBlocks = 0;

        int radius1 = radius+1;

        for(int x = -radius1+1; x <= radius1; ++x) {
            for (int y = -radius1; y <= radius1; ++y) {
                for (int z = -radius1; z <= radius1; ++z) {
                    if (isPointInsideSphere(x, y, z, radius1)) {
                        world.setBlockAndMetadataWithNotify(x+i, (y+j)-8, z+k, 0, 0);
                    }
                }
            }
        }

        for(int x = -radius; x <= radius; ++x) {
            for(int y = -radius; y <= radius; ++y) {
                for(int z = -radius; z <= radius; ++z) {
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
