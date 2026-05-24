package sunsetsatellite.signalindustries.abilities.trigger;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public class ShieldAbility extends TriggerBaseEffectAbility {

    public ShieldAbility(String name, int cost, int cooldown, int effectTime, int costPerTick) {
        super(name, cost, cooldown, effectTime, costPerTick);
    }

	@Override
	public void deactivate(@NotNull TilePosc blockPos, Player player, World world, ItemStack trigger, ItemStack harness) {

	}

    @Override
    public void deactivate(Player player, World world, ItemStack trigger, ItemStack harness) {
        //SignalIndustries.LOGGER.info("shield deactivated");
    }

    @Override
    public void tick(Player player, World world, ItemStack trigger, ItemStack harness) {
        //SignalIndustries.LOGGER.info("shield tick");
    }

	@Override
	public void activate(@NotNull TilePosc blockPos, Player player, World world, ItemStack trigger, ItemStack harness) {

	}

	@Override
    public void activate(Player player, World world, ItemStack trigger, ItemStack harness) {
        //SignalIndustries.LOGGER.info("shield activated");
    }
}
