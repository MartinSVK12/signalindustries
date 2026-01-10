package sunsetsatellite.signalindustries.items.applications;


import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithUtility;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemPortableWorkbench extends ItemWithUtility {

    public ItemPortableWorkbench(String translationKey, String namespaceId, int id, Tier tier) {
        super(translationKey, namespaceId, id, tier);
    }

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, Player player) {
        player.displayWorkbenchScreen((int) player.x, (int) player.y, (int) player.z);
        return itemstack;
    }

    @Override
    public boolean onUseItemOnBlock(ItemStack itemstack, Player player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        player.displayWorkbenchScreen((int) player.x, (int) player.y, (int) player.z);
        return true;
    }

    @Override
    public void activate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world) {
        player.displayWorkbenchScreen((int) player.x, (int) player.y, (int) player.z);
    }
}
