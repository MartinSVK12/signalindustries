package sunsetsatellite.signalindustries.abilities.trigger;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;

public class ShieldAbility extends TriggerBaseEffectAbility{

    public ShieldAbility(String name, int cost, int cooldown, int effectTime, int costPerTick) {
        super(name, cost, cooldown, effectTime, costPerTick);
    }

    @Override
    public void deactivate(int x, int y, int z, EntityPlayer player, World world, ItemStack trigger, ItemStack harness) {
        SignalIndustries.LOGGER.info("shield deactivated");
    }

    @Override
    public void deactivate(EntityPlayer player, World world, ItemStack trigger, ItemStack harness) {
        SignalIndustries.LOGGER.info("shield deactivated");
    }

    @Override
    public void tick(EntityPlayer player, World world, ItemStack trigger, ItemStack harness) {
        SignalIndustries.LOGGER.info("shield tick");
    }

    @Override
    public void activate(int x, int y, int z, EntityPlayer player, World world, ItemStack trigger, ItemStack harness) {
        SignalIndustries.LOGGER.info("shield activated");
    }

    @Override
    public void activate(EntityPlayer player, World world, ItemStack trigger, ItemStack harness) {
        SignalIndustries.LOGGER.info("shield activated");
    }
}
