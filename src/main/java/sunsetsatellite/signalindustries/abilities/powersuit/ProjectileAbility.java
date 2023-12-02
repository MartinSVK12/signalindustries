package sunsetsatellite.signalindustries.abilities.powersuit;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.EntityEnergyOrb;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.Mode;

public class ProjectileAbility extends SuitBaseAbility {
    public ProjectileAbility() {
        super(Mode.NORMAL, SignalIndustries.MOD_ID, "projectile", 50, 20);
    }

    @Override
    public void activate(int x, int y, int z, EntityPlayer player, World world, SignalumPowerSuit powerSuit) {
        world.entityJoinedWorld(new EntityEnergyOrb(world, player, true, 0));
    }

    @Override
    public void activate(EntityPlayer player, World world, SignalumPowerSuit powerSuit) {
        world.entityJoinedWorld(new EntityEnergyOrb(world, player, true, 0));
    }

    @Override
    public void activate(EntityPlayer player, Entity target, World world, SignalumPowerSuit powerSuit) {
        world.entityJoinedWorld(new EntityEnergyOrb(world, player, true, 0));
    }
}
