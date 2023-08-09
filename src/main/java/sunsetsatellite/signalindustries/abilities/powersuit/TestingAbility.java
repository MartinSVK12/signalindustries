package sunsetsatellite.signalindustries.abilities.powersuit;


import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.EntityDustCloudFX;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.Mode;

public class TestingAbility extends SuitBaseAbility{
    public TestingAbility() {
        super(Mode.NORMAL, SignalIndustries.MOD_ID,"testingAbility",100,60);
    }

    @Override
    public void activate(int x, int y, int z, EntityPlayer player, World world, SignalumPowerSuit powerSuit) {
        SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0));
    }

    @Override
    public void activate(EntityPlayer player, World world, SignalumPowerSuit powerSuit) {
        SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0));
    }

    @Override
    public void activate(EntityPlayer player, Entity target, World world, SignalumPowerSuit powerSuit) {
        SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0));
    }
}
