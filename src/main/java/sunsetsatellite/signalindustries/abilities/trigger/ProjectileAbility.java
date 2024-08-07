package sunsetsatellite.signalindustries.abilities.trigger;


import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.entities.EntityEnergyOrb;

public class ProjectileAbility extends TriggerBaseAbility {
    public int effectTime;

    public ProjectileAbility(String name, int cost, int cooldown) {
        super(name, cost, cooldown);
    }

    @Override
    public void activate(int x, int y, int z, EntityPlayer player, World world, ItemStack trigger, ItemStack harness) {
        world.entityJoinedWorld(new EntityEnergyOrb(world, player, true, 0));
    }

    @Override
    public void activate(EntityPlayer player, World world, ItemStack trigger, ItemStack harness) {
        world.entityJoinedWorld(new EntityEnergyOrb(world, player, true, 0));
    }

}
