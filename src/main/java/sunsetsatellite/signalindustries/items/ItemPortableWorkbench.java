package sunsetsatellite.signalindustries.items;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.items.applications.ItemWithUtility;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemPortableWorkbench extends ItemWithUtility {
    public ItemPortableWorkbench(String name, int id, Tier tier) {
        super(name, id, tier);
    }

    @Override
    public void activate(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer player, World world) {
        player.displayGUIWorkbench((int) player.x, (int) player.y, (int) player.z);
    }
}
