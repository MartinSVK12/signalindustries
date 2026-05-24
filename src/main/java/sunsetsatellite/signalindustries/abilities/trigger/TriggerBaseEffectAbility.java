package sunsetsatellite.signalindustries.abilities.trigger;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public abstract class TriggerBaseEffectAbility extends TriggerBaseAbility {
    public int effectTime;
    public int costPerTick;

    public TriggerBaseEffectAbility(String name, int cost, int cooldown, int effectTime, int costPerTick) {
        super(name, cost, cooldown);
        this.effectTime = effectTime;
        this.costPerTick = costPerTick;
    }

    public abstract void deactivate(@NotNull TilePosc blockPos, Player player, World world, ItemStack trigger, ItemStack harness);

    public abstract void deactivate(Player player, World world, ItemStack trigger, ItemStack harness);

    //public abstract void tick(int x, int y, int z, Player player, World world, ItemStack stack);
    public abstract void tick(Player player, World world, ItemStack trigger, ItemStack harness);
}
