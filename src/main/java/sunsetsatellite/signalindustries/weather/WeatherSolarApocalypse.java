package sunsetsatellite.signalindustries.weather;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.WeatherClear;
import net.minecraft.src.World;

import java.util.Random;

public class WeatherSolarApocalypse extends WeatherClear {
    public WeatherSolarApocalypse(int id) {
        super(id);
        setSpawnRainParticles(false);
        setMobsSpawnInDaylight();
    }

    @Override
    public float[] modifyFogColor(float r, float g, float b, float intensity) {
        return new float[]{1f,0.7f,0f};
    }

    @Override
    public void doEnvironmentUpdate(World world, Random rand, int x, int z) {
        int y = world.getHeightValue(x, z);
        int blockId = world.getBlockId(x, y, z);
        int blockIdBelow = world.getBlockId(x,y-1,z);
        if (rand.nextInt(10) == 0) {
            world.setBlockWithNotify(x,y,z, Block.fire.blockID);
        }
        if(rand.nextInt(2) == 0){
            if(blockIdBelow == Block.grass.blockID){
                world.setBlockWithNotify(x,y-1,z,Block.dirt.blockID);
            } else if (blockIdBelow == Block.sand.blockID) {
                world.setBlockWithNotify(x, y - 1, z, Block.glass.blockID);
            }
        }
        if (world.getBlockMaterial(x,y-1,z) == Material.water) {
            world.setBlockWithNotify(x,y-1,z,0);
        }
        if (world.getBlockMaterial(x-1,y-1,z) == Material.water) {
            world.setBlockWithNotify(x-1,y-1,z,0);
        }
        if (world.getBlockMaterial(x+1,y-1,z) == Material.water) {
            world.setBlockWithNotify(x+1,y-1,z,0);
        }
        if (world.getBlockMaterial(x,y-1,z-1) == Material.water) {
            world.setBlockWithNotify(x,y-1,z-1,0);
        }
        if (world.getBlockMaterial(x,y-1,z+1) == Material.water) {
            world.setBlockWithNotify(x,y-1,z+1,0);
        }
    }
}
