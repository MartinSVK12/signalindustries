package sunsetsatellite.signalindustries.items.applications;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithUtility;
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

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, EntityPlayer player) {
        player.displayGUIWorkbench((int) player.x, (int) player.y, (int) player.z);
        return itemstack;
    }

    @Override
    public boolean onUseItemOnBlock(ItemStack itemstack, EntityPlayer player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        player.displayGUIWorkbench((int) player.x, (int) player.y, (int) player.z);
        return true;
    }
}
