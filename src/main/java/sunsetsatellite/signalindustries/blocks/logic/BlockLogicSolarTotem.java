package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.weather.Weathers;
import net.minecraft.server.entity.player.PlayerServer;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.signalindustries.SIConfig;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SIWeather;
import turniplabs.halplibe.helper.EnvironmentHelper;

public class BlockLogicSolarTotem extends BlockLogic implements ICustomDescription {
    public BlockLogicSolarTotem(Block<?> block, Material material) {
        super(block, material);
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xHit, double yHit) {
        super.onBlockRightClicked(world, x, y, z, player, side, xHit, yHit);

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
                if (stack.itemID == SIItems.infernalFragment.id && time >= 13000) {
                    player.getCurrentEquippedItem().consumeItem(player);
                    world.setWorldTime(world.getWorldTime() - world.getWorldTime() % 24000L + 1000L);
                    if (world.getCurrentWeather() == SIWeather.weatherBloodMoon) {
                        player.sendTranslatedChatMessage("event.signalindustries.solarTotemOvertake");
                        world.weatherManager.overrideWeather(Weathers.OVERWORLD_CLEAR);
                        return true;
                    }
                    player.sendTranslatedChatMessage("event.signalindustries.solarTotemUse");
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
