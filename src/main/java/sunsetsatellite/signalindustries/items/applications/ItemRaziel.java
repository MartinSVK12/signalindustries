package sunsetsatellite.signalindustries.items.applications;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithUtility;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemRaziel extends ItemWithUtility {
    public ItemRaziel(String name, int id, Tier tier) {
        super(name, id, tier);
    }

    @Override
    public void activate(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer player, World world) {

    }
}
