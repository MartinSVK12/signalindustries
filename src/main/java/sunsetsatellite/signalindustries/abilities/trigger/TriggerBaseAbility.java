package sunsetsatellite.signalindustries.abilities.trigger;


import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public abstract class TriggerBaseAbility {
    public String name;
    public int cost;
    public int cooldown;

    public TriggerBaseAbility(String name, int cost, int cooldown) {
        this.name = name;
        this.cost = cost;
        this.cooldown = cooldown;
    }

    public abstract void activate(int x, int y, int z, EntityPlayer player, World world, ItemStack trigger, ItemStack harness);
    public abstract void activate(EntityPlayer player, World world, ItemStack trigger, ItemStack harness);

}
