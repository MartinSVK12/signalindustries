package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import net.minecraft.server.entity.player.PlayerServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.signalindustries.SIConfig;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import turniplabs.halplibe.helper.EnvironmentHelper;

public class BlockLogicLunarTotem extends BlockLogic implements ICustomDescription {
    public BlockLogicLunarTotem(Block<?> block, Material material) {
        super(block, material);
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		super.onInteracted(world, tilePos, player, side, xHit, yHit);

		if (!world.isClientSide) {
			if (EnvironmentHelper.isServerEnvironment() && SIConfig.config.getBoolean("Other.totemsRequireOP")) {
				if (!((PlayerServer) player).mcServer.playerList.isOp(player.uuid)) {
					player.sendTranslatedChatMessage("event.signalindustries.totemNoPermission");
					return true;
				}
			}
			long time = world.getWorldTime() % 24000L;
			ItemStack stack = player.getCurrentEquippedItem();
			if (stack != null) {
				if (stack.itemID == SIItems.clearKey.id) {
					SignalIndustries.bloodMoonsDisabled = !SignalIndustries.bloodMoonsDisabled;
					if (SignalIndustries.bloodMoonsDisabled) {
						player.sendTranslatedChatMessage("event.signalindustries.bloodMoonBlock");
					} else {
						player.sendTranslatedChatMessage("event.signalindustries.bloodMoonUnblock");
					}
					return true;
				} else if (stack.itemID == SIItems.monsterShard.id && time >= 0 && time < 13000L) {
					player.getCurrentEquippedItem().consumeItem(player);
					world.setWorldTime(world.getWorldTime() - world.getWorldTime() % 24000L + 13000L);
					player.sendTranslatedChatMessage("event.signalindustries.lunarTotemUse");
					return true;
				}
			}
		}

		return false;
	}

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + TextFormatting.BROWN + "??? (Ancient)";
    }
}
