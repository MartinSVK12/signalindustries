package sunsetsatellite.signalindustries.items;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.signalindustries.invs.InventoryBlueprint;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class ItemGoldprint extends Item implements ICustomDescription {

    public ItemGoldprint(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

	@Override
	public boolean onUseOnBlock(@NotNull ItemStack stack, @NotNull World world, @Nullable Player player, @NotNull TilePosc blockPos, @NotNull Side side, double xHit, double yHit) {
		if (player.isSneaking()) {
			stack.getData().getValue().remove("multiblock");
			stack.getData().getValue().remove("structure");
			player.sendMessage("Blueprint cleared!");
		}
		return super.onUseOnBlock(stack, world, player, blockPos, side, xHit, yHit);
	}

	@Override
	public @Nullable ItemStack onUse(@NotNull ItemStack stack, @NotNull World world, @NotNull Player player) {
		if (!player.isSneaking()) {
			Catalyst.displayGui(player, new InventoryBlueprint(stack), player.inventory.getCurrentSlot(), false, key("gui/blueprint"));
		}
		return stack;
	}

    @Override
    public String getDescription(ItemStack stack) {
        String key2 = stack.getData().getStringOrDefault("structure", "");
        if (!key2.isEmpty()) {
            return TextFormatting.GRAY + key2 + TextFormatting.RESET;
        }
        return TextFormatting.GRAY + "Blank" + TextFormatting.RESET;
    }
}
