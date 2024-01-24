package sunsetsatellite.signalindustries.worldgen;

import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeature;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Random;

public class WorldFeatureObelisk extends WorldFeature {

    public WorldFeatureObelisk() {
    }

    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        SignalIndustries.LOGGER.info(String.format("Obelisk at X:%d Y:%d Z:%d",i,j,k));
        int l = 8;
        if (j >= 1 && j + l + 1 <= world.getHeightBlocks()) {
            for (int x = -4; x <= 4; x++) {
                for (int z = -4; z <= 4; z++) {
                    if(world.getHeightValue(i+x,k+z) < j-1){
                        return false;
                    }
                }
            }
            if (j < world.getHeightBlocks() - l - 1) {
                if((world.getBlockId(i,j-1,k) == SignalIndustries.realityFabric.id && world.getBlockId(i,j-1,k) != SignalIndustries.eternalTreeLog.id) || (world.dimension == Dimension.overworld && world.getBlockId(i,j-1,k) != 0 )){
                    for (int x = -2; x <= 2; x++) {
                        for (int z = -2; z <= 2; z++) {
                            if((x == -2 || x == 2) && (z == -2 || z == 2)){
                                world.setBlockWithNotify(i+x,j,k+z,SignalIndustries.rootedFabric.id);
                            } else {
                                world.setBlockWithNotify(i+x,j,k+z,SignalIndustries.realityFabric.id);
                            }
                        }
                    }
                    for (int x = -4; x <= 4; x++) {
                        for (int z = -4; z <= 4; z++) {
                            if(random.nextFloat() < 0.2f){
                                world.setBlockWithNotify(i+x,world.getHeightValue(i+x,k+z)-1,k+z,SignalIndustries.rootedFabric.id);
                            }
                        }
                    }
                    world.setBlockWithNotify(i,j,k,SignalIndustries.rootedFabric.id);

                    world.setBlockWithNotify(i+2,j+1,k,SignalIndustries.rootedFabric.id);
                    world.setBlockWithNotify(i-2,j+1,k,SignalIndustries.rootedFabric.id);
                    world.setBlockWithNotify(i,j+1,k+2,SignalIndustries.rootedFabric.id);
                    world.setBlockWithNotify(i,j+1,k-2,SignalIndustries.rootedFabric.id);

                    world.setBlockWithNotify(i+2,j+2,k,SignalIndustries.rootedFabric.id);
                    world.setBlockWithNotify(i-2,j+2,k,SignalIndustries.rootedFabric.id);
                    world.setBlockWithNotify(i,j+2,k+2,SignalIndustries.rootedFabric.id);
                    world.setBlockWithNotify(i,j+2,k-2,SignalIndustries.rootedFabric.id);

                    world.setBlockWithNotify(i,j+l+3,k,SignalIndustries.realityFabric.id);
                    world.setBlockWithNotify(i,j+l+2,k,SignalIndustries.realityFabric.id);
                    world.setBlockWithNotify(i,j+l+1,k,SignalIndustries.rootedFabric.id);
                    world.setBlockWithNotify(i,j+l,k,SignalIndustries.dimensionalShardOre.id);
                    world.setBlockWithNotify(i,j+l-1,k,SignalIndustries.rootedFabric.id);
                    world.setBlockWithNotify(i,j+l-2,k,SignalIndustries.realityFabric.id);
                    world.setBlockWithNotify(i,j+l-3,k,SignalIndustries.realityFabric.id);
                    SignalIndustries.LOGGER.info("Obelisk generated.");
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
