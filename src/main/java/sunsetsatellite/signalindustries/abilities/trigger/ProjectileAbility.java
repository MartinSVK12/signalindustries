package sunsetsatellite.signalindustries.abilities.trigger;


import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.entities.ProjectileEnergyOrb;

public class ProjectileAbility extends TriggerBaseAbility {

    public ProjectileAbility(String name, int cost, int cooldown) {
        super(name, cost, cooldown);
    }

    @Override
    public void activate(int x, int y, int z, Player player, World world, ItemStack trigger, ItemStack harness) {
        world.entityJoinedWorld(new ProjectileEnergyOrb(world, player));
    }

    @Override
    public void activate(Player player, World world, ItemStack trigger, ItemStack harness) {
        world.entityJoinedWorld(new ProjectileEnergyOrb(world, player));
    }

}
