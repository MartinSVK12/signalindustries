package sunsetsatellite.signalindustries.worldgen;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import net.minecraft.core.world.generate.feature.WorldFeature;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.util.ExplosionNoDrops;
import sunsetsatellite.signalindustries.util.MeteorLocation;

import java.util.Random;

public class WorldFeatureMeteor extends WorldFeature {

    public int oreId;
    public int oreMeta;
    public int oreChance;
    public int radius = 4;

    public WorldFeatureMeteor(int oreId, int oreMeta, int oreChance){
        this.oreId = oreId;
        this.oreMeta = oreMeta;
        this.oreChance = oreChance;
    }

    public WorldFeatureMeteor(int oreId, int oreMeta, int oreChance, int radius){
        this.oreId = oreId;
        this.oreMeta = oreMeta;
        this.oreChance = oreChance;
        this.radius = radius;
    }
    @Override
    public boolean place(World world, Random random, int i, int j, int k) {
        SignalIndustries.LOGGER.info("{} Meteor fell at X:{} Y:{} Z:{}", I18n.getInstance().translateNameKey(Blocks.blocksList[oreId].getLanguageKey(oreMeta)), i, j, k);
        ExplosionNoDrops ex = new ExplosionNoDrops(world,null,i,j,k,50f);
        ex.explode();
        ex.addEffects(false);

        int oreBlocks = 0;

        for(int x = -radius; x <= radius; ++x) {
            for(int y = -radius; y <= radius; ++y) {
                for(int z = -radius; z <= radius; ++z) {
                    if (isPointInsideSphere(x, y, z, radius)) {
                        if (oreId != 0 && random.nextInt(100) < oreChance){
                            world.setBlockAndMetadataWithNotify(x+i, (y+j)-8, z+k, oreId, oreMeta);
                            //if(oreId == SIBlocks.signalumOre.id()){
                            //    SignalIndustries.ORE_BLOCK_COUNT.compute(SIBlocks.signalumOre,(ignored,v) -> v == null ? 1 : v + 1);
                            //}
                            oreBlocks++;
                        } else {
                            world.setBlockAndMetadataWithNotify(x+i, (y+j)-8, z+k, Blocks.BASALT.id(), 0);
                        }
                    }
                }
            }
        }

        SignalIndustries.addMeteorLocation(new MeteorLocation(MeteorLocation.Type.getFromBlock(Blocks.blocksList[oreId]),new Vec3i(i,j,k)));
        SignalIndustries.LOGGER.info("Meteor contains {} ore.", oreBlocks);
        return true;
    }

    public boolean isPointInsideSphere(int x, int y, int z, double radius) {
        return x*x + y*y + z*z < radius*radius;
    }

}
