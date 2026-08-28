package sunsetsatellite.signalindustries.abilities.trigger;


import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.signalindustries.entities.ProjectileEnergyOrb;
import turniplabs.halplibe.helper.EnvironmentHelper;

public class ProjectileAbility extends TriggerBaseAbility {

    public ProjectileAbility(String name, int cost, int cooldown) {
        super(name, cost, cooldown);
    }

    @Override
    public void activate(@NotNull TilePosc blockPos, Player player, World world, ItemStack trigger, ItemStack harness) {
        if(!EnvironmentHelper.isMultiplayerClient()) world.entityJoinedWorld(new ProjectileEnergyOrb(world, player));
    }

    @Override
    public void activate(Player player, World world, ItemStack trigger, ItemStack harness) {
		if(!EnvironmentHelper.isMultiplayerClient()) world.entityJoinedWorld(new ProjectileEnergyOrb(world, player));
    }

}
