package sunsetsatellite.signalindustries.items.tools;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.signalindustries.items.base.ItemTiered;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemMagnet extends ItemTiered {
	public ItemMagnet(String translationKey, String namespaceId, int id, Tier tier) {
		super(translationKey, namespaceId, id, tier);
	}

	@Override
	public @Nullable ItemStack onUse(@NotNull ItemStack stack, @NotNull World world, @NotNull Player player) {
		stack.getData().putBoolean("active", !stack.getData().getBoolean("active"));
		return super.onUse(stack, world, player);
	}
}
