package sunsetsatellite.signalindustries.weather;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import net.minecraft.core.world.weather.Weather;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.EntityCrystal;
import sunsetsatellite.signalindustries.entities.EntityFallingMeteor;

import java.util.Random;

public class WeatherMeteorShower extends Weather {
    public WeatherMeteorShower(int id) {
        super(id);
    }

    @Override
    public float[] modifyFogColor(float r, float g, float b, float intensity) {
        return new float[]{ 0, (173.0f/255.0f) * intensity, 1 * intensity };
    }

    @Override
    public void doEnvironmentUpdate(World world, Random rand, int x, int z) {
        int y = world.getHeightValue(x, z);
        EntityPlayer player = world.getClosestPlayer(x,y,z,64);
        if(rand.nextInt(50) == 0 && player != null){
            EntityFallingMeteor meteor;
            if(rand.nextInt(25) == 0){
                meteor = new EntityFallingMeteor(world, x, 255, z, SignalIndustries.signalumOre.id);
            } else {
                meteor = new EntityFallingMeteor(world, x, 255, z, Block.basalt.id);
            }
            world.entityJoinedWorld(meteor);
        }
        super.doEnvironmentUpdate(world, rand, x, z);
    }
}
