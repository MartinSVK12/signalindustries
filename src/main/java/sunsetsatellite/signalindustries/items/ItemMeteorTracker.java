package sunsetsatellite.signalindustries.items;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.util.MeteorLocation;

public class ItemMeteorTracker extends Item implements ICustomDescription {

    public ItemMeteorTracker(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

	@Override
	public @Nullable ItemStack onUse(@NotNull ItemStack stack, @NotNull World world, @NotNull Player player) {
		if (stack.getMetadata() == 0) {
			stack.setMetadata(1);
		} else {

			Vec3i chunk = null;
			double distance = Double.MAX_VALUE;
			MeteorLocation.Type type = null;
			for (MeteorLocation meteorLocation : SignalIndustries.meteorLocations) {
				Vec3i location = meteorLocation.location();
				if (location.getSqDistanceTo((int) player.x, (int) player.y, (int) player.z) < distance) {
					distance = location.getSqDistanceTo((int) player.x, (int) player.y, (int) player.z);
					chunk = location;
					type = meteorLocation.type();
				}
			}
			if (chunk != null) {
				if (player.isSneaking() && distance < 5) {
					player.sendStatusMessage("This meteor will no longer be tracked.");
					final Vec3i finalChunk = chunk;
					SignalIndustries.meteorLocations.removeIf((L) -> L.location() == finalChunk);
				} else {
					player.sendStatusMessage(String.format("Distance: %.0f blocks | Type: %s", distance, type.name()));
				}
			} else {
				player.sendStatusMessage("No meteors detected nearby.");
			}
		}
		return super.onUse(stack, world, player);
	}

    @Override
    public String getDescription(ItemStack stack) {
        if (stack.getMetadata() != 1) {
            return "Uncalibrated!\n" + TextFormatting.GRAY + "Right-click while holding in your hand to calibrate.";
        }
        return "Calibrated.";
    }
}
