package sunsetsatellite.signalindustries.items.applications;


import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithUtility;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemPortableWorkbench extends ItemWithUtility {

    public ItemPortableWorkbench(String translationKey, String namespaceId, int id, Tier tier) {
        super(translationKey, namespaceId, id, tier);
    }

	@Override
	public @Nullable ItemStack onUse(@NotNull ItemStack stack, @NotNull World world, @NotNull Player player) {
		player.displayWorkbenchScreen(new TilePos(player.x, player.y, player.z));
		return stack;
	}

	@Override
	public boolean onUseOnBlock(@NotNull ItemStack stack, @NotNull World world, @Nullable Player player, @NotNull TilePosc blockPos, @NotNull Side side, double xHit, double yHit) {
		player.displayWorkbenchScreen(blockPos);
		return true;
	}

    @Override
    public void activate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world) {
		player.displayWorkbenchScreen(new TilePos(player.x, player.y, player.z));
    }
}
