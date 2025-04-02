package sunsetsatellite.signalindustries.abilities.powersuit;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.util.Tier;

public class TestingAbility extends SuitBaseAbility{
    public TestingAbility() {
        super(Tier.REINFORCED, SignalIndustries.MOD_ID,"testingAbility",100,60);
    }

    @Override
    public void activate(int x, int y, int z, Player player, World world, IPowerSuit powerSuit) {
        //SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0));
    }

    @Override
    public void activate(Player player, World world, IPowerSuit powerSuit) {
        //SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0));
    }

    @Override
    public void activate(Player player, Entity target, World world, IPowerSuit powerSuit) {
        //SignalIndustries.spawnParticle(new EntityDustCloudFX(world, player.x, player.y, player.z,0,0,0));
    }
}
