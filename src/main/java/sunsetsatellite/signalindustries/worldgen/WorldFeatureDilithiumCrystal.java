package sunsetsatellite.signalindustries.worldgen;

import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeature;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Random;

public class WorldFeatureDilithiumCrystal extends WorldFeature {
    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        int maxRadius = 2;
        int maxRadiusRepeat = 4 + random.nextInt(4);

        int radius = 0;
        int worldHeight = world.getHeightBlocks();
        int height = ((worldHeight + j) / 4) - (random.nextInt(16)-8);
        /*if(world.getBlockId(i,height-1,k) != SignalIndustries.realityFabric.id){
            return false;
        }*/

        generateCircle(world,random,i,height+((maxRadius + maxRadiusRepeat)/2)+1,k);

        boolean flip = false;
        int id = SignalIndustries.dilithiumCrystalBlock.id;
        int y = 0;
        for (int l = 0; l <= maxRadius*2; l++) {
            int x = 0;
            boolean first = true;
            for (int lineRadius = radius; lineRadius >= 0; lineRadius--) {
                if(!first){
                    for (int z = -lineRadius; z <= lineRadius; z++) {
                        world.setBlockWithNotify(i+x,height+y,k+z, id);
                        world.setBlockWithNotify(i+(x*-1),height+y,k+z, id);
                    }
                    x++;
                } else {
                    for (int z = -radius; z <= radius; z++) {
                        world.setBlockWithNotify(i+x,height+y,k+z, id);
                    }
                    x++;
                    first = false;
                }
            }
            if(!flip){
                radius++;
            } else {
                radius--;
            }
            if(radius >= maxRadius){
                y++;
                for (int m = 0; m < maxRadiusRepeat; m++) {
                    x = 0;
                    for (int lineRadius = maxRadius; lineRadius >= 0; lineRadius--) {
                        if (!first) {
                            for (int z = -lineRadius; z <= lineRadius; z++) {
                                world.setBlockWithNotify(i + x, height + y, k + z, id);
                                world.setBlockWithNotify(i + (x * -1), height + y, k + z, id);
                            }
                            x++;
                        }
                    }
                    y++;
                }
                flip = true;
                radius--;
                y--;
            }
            y++;
        }
        return true;
    }

    private void generateCircle(World world, Random random, int i, int j, int k) {
        int blockRadius = 5;

        for(int x = -blockRadius; x <= blockRadius; ++x) {
            for(int z = -blockRadius; z <= blockRadius; ++z) {
                if (isPointInsideCircle(x, z, blockRadius)) {
                    if(random.nextFloat() < 0.33f){
                        world.setBlockAndMetadataWithNotify(x+i, j, z+k, SignalIndustries.rootedFabric.id, 0);
                    } else {
                        world.setBlockAndMetadataWithNotify(x+i, j, z+k, SignalIndustries.realityFabric.id, 0);
                    }

                }
            }
        }

        blockRadius--;

        for(int x = -blockRadius; x <= blockRadius; ++x) {
            for(int z = -blockRadius; z <= blockRadius; ++z) {
                if (isPointInsideCircle(x, z, blockRadius)) {
                    world.setBlockAndMetadataWithNotify(x+i, j, z+k, 0, 0);
                }
            }
        }
    }

    public boolean isPointInsideSphere(int x, int y, int z, double radius) {
        return x*x + y*y + z*z < radius*radius;
    }

    public boolean isPointInsideCircle(int x, int z, double radius) {
        return x*x + z*z <= radius*radius;
    }

    //TODO: doesn't do what it is supposed to do but creates interesting patterns, reuse for something else maybe?
    public boolean unused(int x, int y, int z, double radius) {
        return (x*x + y*y + z*z)+1 == radius*radius || (x*x + y*y + z*z)-1 == radius*radius;
    }
}
