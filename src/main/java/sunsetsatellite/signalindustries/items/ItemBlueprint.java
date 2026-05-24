package sunsetsatellite.signalindustries.items;

import net.minecraft.core.block.entity.TileEntity;
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
import sunsetsatellite.catalyst.multiblocks.IMultiblock;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.signalindustries.invs.InventoryBlueprint;
import sunsetsatellite.signalindustries.util.SIMultiblock;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class ItemBlueprint extends Item implements ICustomDescription {

    public ItemBlueprint(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

	@Override
	public boolean onUseOnBlock(@NotNull ItemStack stack, @NotNull World world, @Nullable Player player, @NotNull TilePosc blockPos, @NotNull Side side, double xHit, double yHit) {
		TileEntity tile = world.getTileEntity(blockPos);
		if (tile instanceof IMultiblock multiblock) {
			if (stack.getData().containsKey("structure")) {
				player.sendMessage("This blueprint already contains data for a different structure!");
				player.sendMessage("Clear it by shift right clicking it first.");
				return super.onUseOnBlock(stack, world, player, blockPos, side, xHit, yHit);
			}
			stack.getData().putString("multiblock", multiblock.getMultiblock().data.translateKey);
			player.sendMessage("Blueprint written down!");
		} else {
			if (player.isSneaking()) {
				stack.getData().getValue().remove("multiblock");
				stack.getData().getValue().remove("structure");
				player.sendMessage("Blueprint cleared!");
			}
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
        String key = stack.getData().getStringOrDefault("multiblock", "");
        String key2 = stack.getData().getStringOrDefault("structure", "");
        if (!key.isEmpty()) {
            SIMultiblock multiblock = (SIMultiblock) Multiblock.multiblocks.get(key.replace("multiblock.signalindustries.", ""));
            return "Tier: " + multiblock.tier.getTextColor() + multiblock.tier.getRank() + "\n" + TextFormatting.LIGHT_BLUE + multiblock.getTranslatedName() + TextFormatting.RESET;
        } else if (!key2.isEmpty()) {
            return TextFormatting.GRAY + key2 + TextFormatting.RESET;
        }
        return TextFormatting.GRAY + "Blank" + TextFormatting.RESET;
    }
}
