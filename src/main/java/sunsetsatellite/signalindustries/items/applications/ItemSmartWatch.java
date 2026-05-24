package sunsetsatellite.signalindustries.items.applications;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithUtility;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemSmartWatch extends ItemWithUtility {

    public ItemSmartWatch(String translationKey, String namespaceId, int id, Tier tier) {
        super(translationKey, namespaceId, id, tier);
    }

    @Override
    public void activate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world) {

    }
}
