package sunsetsatellite.signalindustries.abilities.powersuit;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.util.Tier;

public class TestingEffectAbility extends SuitBaseEffectAbility{

    public TestingEffectAbility() {
        super(Tier.REINFORCED, SignalIndustries.MOD_ID,"testingAbility",100,200, 100);
    }

    @Override
    public void activate(int x, int y, int z, Player player, World world, IPowerSuit powerSuit) {
        for (int i = 0; i < 8; i++) {
            //SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,1,0,0));
        }
    }

    @Override
    public void activate(Player player, World world, IPowerSuit powerSuit) {
        for (int i = 0; i < 8; i++) {
            //SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,1,0,0));
        }
    }

    @Override
    public void activate(Player player, Entity target, World world, IPowerSuit powerSuit) {
        for (int i = 0; i < 8; i++) {
            //SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,1,0,0));
        }
    }

    @Override
    public void deactivate(int x, int y, int z, Player player, World world, IPowerSuit powerSuit) {
        for (int i = 0; i < 8; i++) {
            //SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,0,0,1));
        }
    }

    @Override
    public void deactivate(Player player, World world, IPowerSuit powerSuit) {
        for (int i = 0; i < 8; i++) {
            //SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,0,0,1));
        }
    }

    @Override
    public void deactivate(Player player, Entity target, World world, IPowerSuit powerSuit) {
        for (int i = 0; i < 8; i++) {
            //SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,0,0,1));
        }
    }

    @Override
    public void tick(int x, int y, int z, Player player, World world, IPowerSuit powerSuit) {
        //SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,0,1,0));
    }

    @Override
    public void tick(Player player, World world, IPowerSuit powerSuit) {
        //SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,0,1,0));
    }

    @Override
    public void tick(Player player, Entity target, World world, IPowerSuit powerSuit) {
        //SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0,0,1,0));
    }
}
