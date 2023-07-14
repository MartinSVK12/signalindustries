package sunsetsatellite.signalindustries.abilities.powersuit;

import net.minecraft.src.*;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.EntityDustCloudFX;
import sunsetsatellite.signalindustries.misc.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.Mode;

public class TestingAbility extends SuitBaseAbility{
    public TestingAbility() {
        super(Mode.NORMAL, SignalIndustries.MOD_ID,"testingAbility",100,60);
    }

    @Override
    public void activate(int x, int y, int z, EntityPlayer player, World world, SignalumPowerSuit powerSuit) {
        SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.posX, player.posY, player.posZ,0,0,0));
    }

    @Override
    public void activate(EntityPlayer player, World world, SignalumPowerSuit powerSuit) {
        SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.posX, player.posY, player.posZ,0,0,0));
    }

    @Override
    public void activate(EntityPlayer player, Entity target, World world, SignalumPowerSuit powerSuit) {
        SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.posX, player.posY, player.posZ,0,0,0));
    }
}
