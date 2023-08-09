package sunsetsatellite.signalindustries.weather;






import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.world.weather.WeatherClear;

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
    public void doEnvironmentUpdate(World world, Random random, int x, int z) {
        int y = world.getHeightValue(x, z);
        int blockId = world.getBlockId(x, y, z);
        int blockIdBelow = world.getBlockId(x,y-1,z);
        if (random.nextInt(10) == 0) {
            world.setBlockWithNotify(x,y,z, Block.fire.id);
        }
        if(random.nextInt(2) == 0){
            if(blockIdBelow == Block.grass.id){
                world.setBlockWithNotify(x,y-1,z,Block.dirt.id);
            } else if (blockIdBelow == Block.sand.id) {
                world.setBlockWithNotify(x, y - 1, z, Block.glass.id);
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
