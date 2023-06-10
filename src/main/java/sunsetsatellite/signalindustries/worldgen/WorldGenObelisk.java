package sunsetsatellite.signalindustries.worldgen;

import net.minecraft.shared.Minecraft;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Random;

public class WorldGenObelisk extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        int l = 8;
        boolean flag = true;
        if (j >= 1 && j + l + 1 <= Minecraft.WORLD_HEIGHT_BLOCKS) {
            int i1;
            int k1;
            int j2;
            int i3;
            int k3;
            for(i1 = j; i1 <= j + 1 + l; ++i1) {
                k1 = 1;
                if (i1 == j) {
                    k1 = 0;
                }

                if (i1 >= j + 1 + l - 2) {
                    k1 = 2;
                }

                for(j2 = i - k1; j2 <= i + k1 && flag; ++j2) {
                    for(i3 = k - k1; i3 <= k + k1 && flag; ++i3) {
                        if (i1 >= 0 && i1 < Minecraft.WORLD_HEIGHT_BLOCKS) {
                            k3 = world.getBlockId(j2, i1, i3);
                            if (k3 != 0) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else {
                if (j < Minecraft.WORLD_HEIGHT_BLOCKS - l - 1) {
                    //world.setBlockWithNotify(i, j - 1, k, Block.dirt.blockID);

                    if(world.getBlockId(i,j-1,k) == SignalIndustries.realityFabric.blockID){
                        for(k1 = 0; k1 < l; ++k1) {
                            world.setBlockWithNotify(i, j + k1, k, SignalIndustries.rootedFabric.blockID);
                        }
                        world.setBlockWithNotify(i,j+l,k,SignalIndustries.dimensionalShardOre.blockID);
                        //world.setBlockAndMetadataWithNotify()
                        return true;
                    } else {
                        return false;
                    }



                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}
