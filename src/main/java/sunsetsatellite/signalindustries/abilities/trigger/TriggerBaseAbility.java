package sunsetsatellite.signalindustries.abilities.trigger;


import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public abstract class TriggerBaseAbility {
    public String name;
    public int cost;
    public int cooldown;

    public TriggerBaseAbility(String name, int cost, int cooldown) {
        this.name = name;
        this.cost = cost;
        this.cooldown = cooldown;
    }

    public abstract void activate(@NotNull TilePosc blockPos, Player player, World world, ItemStack trigger, ItemStack harness);

    public abstract void activate(Player player, World world, ItemStack trigger, ItemStack harness);

}
