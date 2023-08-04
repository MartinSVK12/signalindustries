package sunsetsatellite.signalindustries.abilities.trigger;




import sunsetsatellite.signalindustries.entities.EntityEnergyOrb;

public class ProjectileAbility extends TriggerBaseAbility {
    public ProjectileAbility(String name, int cost, int cooldown) {
        super(name, cost, cooldown);
    }

    @Override
    public void activate(int x, int y, int z, EntityPlayer player, World world, ItemStack stack) {
        world.entityJoinedWorld(new EntityEnergyOrb(world, player, true, 0));
    }

    @Override
    public void activate(EntityPlayer player, World world, ItemStack stack) {
        world.entityJoinedWorld(new EntityEnergyOrb(world, player, true, 0));
    }
}
