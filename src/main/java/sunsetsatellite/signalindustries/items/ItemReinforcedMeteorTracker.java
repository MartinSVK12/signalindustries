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
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.util.MeteorLocation;

public class ItemReinforcedMeteorTracker extends Item implements ICustomDescription {

    public ItemReinforcedMeteorTracker(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

	@Override
	public @Nullable ItemStack onUse(@NotNull ItemStack stack, @NotNull World world, @NotNull Player player) {
		int range = 4;
		if (stack.getMetadata() == 0) {
			stack.setMetadata(1);
		} else {
			for (MeteorLocation meteorLocation : SignalIndustries.meteorLocations) {
				if(meteorLocation.type() == MeteorLocation.Type.DIMENSIONAL){
					Vec3i location = meteorLocation.location();
					int distance = (int) location.getSqDistanceTo((int) player.x, (int) player.y, (int) player.z);
					if (location.getBlock(world) == SIBlocks.dimensionalShardOre) {
						if (distance < 256) {
							player.sendStatusMessage(String.format("Interference detected %s blocks away.",distance));
							return super.onUse(stack, world, player);
						}
					}
				}
			}
			int oreFound = 0;
			int oreYLevel = -1;
			for (int i = -range; i < range; i++) {
				for (int j = -range; j < range; j++) {
					for (int k = 0; k < world.getHeightValue((int) (player.x + i), (int) (player.z + j)); k++) {
						int blockId = world.getBlockId((int) (player.x + i), k, (int) (player.z + j));
						if (blockId == SIBlocks.signalumOre.id()) {
							oreFound++;
							if (k > oreYLevel) {
								oreYLevel = k;
							}
						}
					}
				}
			}
			if (oreFound > 0 && oreYLevel < player.y) {
				player.sendStatusMessage(String.format("%d Signalite Ore blocks detected, approx. %d blocks underground.", oreFound, (int) player.y - oreYLevel));
			} else if (oreFound > 0 && oreYLevel > player.y) {
				player.sendStatusMessage(String.format("%d Signalite Ore blocks detected, approx. %d blocks above.", oreFound, oreYLevel - (int) player.y));
			} else {
				player.sendStatusMessage("No nearby traces of Signalite could be found.");
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
