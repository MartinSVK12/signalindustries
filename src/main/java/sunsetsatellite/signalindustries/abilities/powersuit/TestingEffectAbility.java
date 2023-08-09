package sunsetsatellite.signalindustries.abilities.powersuit;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.EntityDustCloudFX;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.Mode;

public class TestingEffectAbility extends SuitBaseEffectAbility{

    public TestingEffectAbility() {
        super(Mode.NORMAL, SignalIndustries.MOD_ID,"testingAbility",100,200, 100);
    }

    @Override
    public void activate(int x, int y, int z, EntityPlayer player, World world, SignalumPowerSuit powerSuit) {
        for (int i = 0; i < 8; i++) {
            SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,1,0,0));
        }
    }

    @Override
    public void activate(EntityPlayer player, World world, SignalumPowerSuit powerSuit) {
        for (int i = 0; i < 8; i++) {
            SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,1,0,0));
        }
    }

    @Override
    public void activate(EntityPlayer player, Entity target, World world, SignalumPowerSuit powerSuit) {
        for (int i = 0; i < 8; i++) {
            SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,1,0,0));
        }
    }

    @Override
    public void deactivate(int x, int y, int z, EntityPlayer player, World world, SignalumPowerSuit powerSuit) {
        for (int i = 0; i < 8; i++) {
            SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,0,0,1));
        }
    }

    @Override
    public void deactivate(EntityPlayer player, World world, SignalumPowerSuit powerSuit) {
        for (int i = 0; i < 8; i++) {
            SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,0,0,1));
        }
    }

    @Override
    public void deactivate(EntityPlayer player, Entity target, World world, SignalumPowerSuit powerSuit) {
        for (int i = 0; i < 8; i++) {
            SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,0,0,1));
        }
    }

    @Override
    public void tick(int x, int y, int z, EntityPlayer player, World world, SignalumPowerSuit powerSuit) {
        SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,0,1,0));
    }

    @Override
    public void tick(EntityPlayer player, World world, SignalumPowerSuit powerSuit) {
        SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,0,1,0));
    }

    @Override
    public void tick(EntityPlayer player, Entity target, World world, SignalumPowerSuit powerSuit) {
        SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,0,1,0));
    }
}
