package sunsetsatellite.signalindustries.blocks.base;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.*;
import sunsetsatellite.signalindustries.inventories.TileEntityItemConduit;
import sunsetsatellite.signalindustries.items.ItemConfigurationTablet;
import sunsetsatellite.signalindustries.util.ConfigurationTabletMode;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Objects;

public abstract class BlockConduitBase extends BlockContainerTiered implements IConduitBlock, ISideInteractable {
    public BlockConduitBase(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (!isPlayerHoldingWrench(player)) {
            return false;
        }

        Pair<Direction, BlockSection> pair = Catalyst.getBlockSurfaceClickPosition(world, player);
        Side playerFacing = Catalyst.calculatePlayerFacing(player.yRot);
        if (pairIsInvalid(pair)) {
            return false;
        }

        if (isPlayerHoldingConfigTablet(player)) {
            handleConfigTabletAction(player, pair, world, x, y, z, playerFacing);
        }
        return true;
    }

    private boolean isPlayerHoldingWrench(EntityPlayer player) {
        return player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof IWrench;
    }

    private boolean pairIsInvalid(Pair<Direction, BlockSection> pair) {
        return pair == null || pair.getLeft() == null || pair.getRight() == null;
    }

    private boolean isPlayerHoldingConfigTablet(EntityPlayer player) {
        return player.getCurrentEquippedItem().getItem() instanceof ItemConfigurationTablet;
    }

    private void handleConfigTabletAction(EntityPlayer player, Pair<Direction, BlockSection> pair,
                                          World world, int x, int y, int z, Side playerFacing) {

        ConfigurationTabletMode mode = ConfigurationTabletMode.values()[player.getCurrentEquippedItem().getData().getInteger("mode")];
        if (Objects.requireNonNull(mode) == ConfigurationTabletMode.DISCONNECTOR) {
            handlePipeDisconnect(pair, world, x, y, z, playerFacing, player);
        }
    }

    private void handlePipeDisconnect(Pair<Direction, BlockSection> pair, World world, int x, int y, int z, Side playerFacing, EntityPlayer player) {
        TileEntity tile = world.getBlockTileEntity(x,y,z);
        if(tile instanceof TileEntityItemConduit){
            Direction dir = pair.getRight().toDirection(pair.getLeft(), playerFacing);
            ((TileEntityItemConduit) tile).noConnectDirections.put(dir,!((TileEntityItemConduit) tile).noConnectDirections.get(dir));
        }
    }


}
