package sunsetsatellite.signalindustries.items;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.signalindustries.entities.ProjectileCrystal;

public class ItemVolatileSignalumCrystal extends Item {
    public ItemVolatileSignalumCrystal(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

	@Override
	public @Nullable ItemStack onUse(@NotNull ItemStack selfStack, @NotNull World world, @NotNull Player player) {
		if (world.isClientSide) {
			return super.onUse(selfStack, world, player);
		}
		world.entityJoinedWorld(new ProjectileCrystal(world, player));
		return super.onUse(selfStack, world, player);
	}
}
