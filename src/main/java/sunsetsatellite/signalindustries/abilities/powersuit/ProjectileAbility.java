package sunsetsatellite.signalindustries.abilities.powersuit;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.ProjectileEnergyOrb;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.EnvironmentHelper;

public class ProjectileAbility extends SuitBaseAbility {
    public ProjectileAbility() {
        super(Tier.BASIC, SignalIndustries.MOD_ID, "projectile", 50, 20);
    }

    @Override
    public void activate(int x, int y, int z, Player player, World world, IPowerSuit powerSuit) {
		if(!EnvironmentHelper.isMultiplayerClient()) world.entityJoinedWorld(new ProjectileEnergyOrb(world, player));
    }

    @Override
    public void activate(Player player, World world, IPowerSuit powerSuit) {
		if(!EnvironmentHelper.isMultiplayerClient()) world.entityJoinedWorld(new ProjectileEnergyOrb(world, player));
    }

    @Override
    public void activate(Player player, Entity target, World world, IPowerSuit powerSuit) {
		if(!EnvironmentHelper.isMultiplayerClient()) world.entityJoinedWorld(new ProjectileEnergyOrb(world, player));
    }
}
