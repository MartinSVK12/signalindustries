package sunsetsatellite.signalindustries.blocks.logic.base;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.io.IFluidIO;
import sunsetsatellite.catalyst.core.util.io.IItemIO;
import sunsetsatellite.catalyst.core.util.section.BlockSection;
import sunsetsatellite.catalyst.core.util.section.ISideInteractable;
import sunsetsatellite.catalyst.core.util.vector.Vec2f;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.items.ItemConfigurationTablet;
import sunsetsatellite.signalindustries.items.covers.ItemCover;
import sunsetsatellite.signalindustries.tiles.base.TileEntityCoverable;
import sunsetsatellite.signalindustries.util.ConfigurationTabletMode;
import sunsetsatellite.signalindustries.util.IOPreview;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Objects;
import java.util.function.Supplier;

public class BlockLogicMachineBase extends BlockLogicTiered implements ISideInteractable {

    protected boolean vertical = false;

    public BlockLogicMachineBase(Block<?> block, Material material, Tier tier, Supplier<TileEntity> tileEntitySupplier) {
        super(block, material, tier);
        block.withEntity(tileEntitySupplier);
    }

    public BlockLogicMachineBase setVertical() {
        vertical = true;
        return this;
    }

    public boolean isVertical() {
        return vertical;
    }

    @Override
    public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xHit, double yHit) {
        if (!isPlayerHoldingSideInteractableItem(player)) {
            return false;
        }

        Pair<Direction, BlockSection> pair = Catalyst.getBlockSurfaceClickPosition(world, player, side, new Vec2f(xHit,yHit));
        Side playerFacing = Catalyst.calculatePlayerFacing(player.yRot);
        if (pairIsInvalid(pair)) {
            return false;
        }

        if (isPlayerHoldingConfigTablet(player)) {
            handleConfigTabletAction(player, pair, world, x, y, z, playerFacing);
        }

        if (isPlayerHoldingCover(player)) {
            handleCoverInstallation(player, pair, world, x, y, z, playerFacing);
        }
        return true;
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityCoverable) {
            Direction[] covers = ((TileEntityCoverable) tile).getCovers().keySet().toArray(new Direction[0]);
            for (Direction dir : covers) {
                ((TileEntityCoverable) tile).removeCover(dir);
            }
        }
        super.onBlockRemoved(world, x, y, z, data);
    }

    private void handleCoverInstallation(Player player, Pair<Direction, BlockSection> pair, World world, int x, int y, int z, Side playerFacing) {
        TileEntity tile = world.getTileEntity(x, y, z);
        ItemCover cover = (ItemCover) player.getCurrentEquippedItem().getItem();
        if (tile instanceof TileEntityCoverable) {
            Direction dir = pair.getRight().toDirection(pair.getLeft(), playerFacing);
            if (dir == null) return;
            if (((TileEntityCoverable) tile).installCover(dir, cover.coverSupplier.get(), player)) {
                player.getCurrentEquippedItem().stackSize--;
                if (player.getCurrentEquippedItem().stackSize <= 0) {
                    player.inventory.setCurrentItem(null, false);
                }
            }
        }
    }

    private void handleCoverRemoval(Pair<Direction, BlockSection> pair, World world, int x, int y, int z, Side playerFacing, Player player) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityCoverable) {
            Direction dir = pair.getRight().toDirection(pair.getLeft(), playerFacing);
            if (dir == null) return;
            ((TileEntityCoverable) tile).removeCover(dir, player);
        }
    }

    private boolean isPlayerHoldingCover(Player player) {
        return player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemCover;
    }

    private boolean isPlayerHoldingSideInteractableItem(Player player) {
        return player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ISideInteractable;
    }

    private boolean pairIsInvalid(Pair<Direction, BlockSection> pair) {
        return pair == null || pair.getLeft() == null || pair.getRight() == null;
    }

    private boolean isPlayerHoldingConfigTablet(Player player) {
        return player.getCurrentEquippedItem().getItem() instanceof ItemConfigurationTablet;
    }

    private void handleConfigTabletAction(Player player, Pair<Direction, BlockSection> pair,
                                          World world, int x, int y, int z, Side playerFacing) {

        ConfigurationTabletMode mode = ConfigurationTabletMode.values()[player.getCurrentEquippedItem().getData().getInteger("mode")];
        switch (mode) {
            case ROTATION:
                handleRotationAction(pair, world, x, y, z, playerFacing, player);
                break;
            case ITEM:
                handleItemIoChange(pair, world, x, y, z, playerFacing, player);
                break;
            case FLUID:
                handleFluidIoChange(pair, world, x, y, z, playerFacing, player);
                break;
            case DISCONNECTOR:
                handleCoverRemoval(pair, world, x, y, z, playerFacing, player);
                break;
            case CONFIGURATOR:
                handleCoverConfig(pair, world, x, y, z, playerFacing, player);
                break;
        }
    }

    private void handleCoverConfig(Pair<Direction, BlockSection> pair, World world, int x, int y, int z, Side playerFacing, Player player) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityCoverable) {
            Direction dir = pair.getRight().toDirection(pair.getLeft(), playerFacing);
            if (dir == null) return;
            if (((TileEntityCoverable) tile).getCovers().get(dir) != null) {
                ((TileEntityCoverable) tile).getCovers().get(dir).openConfiguration(player, dir);
            }
        }
    }

    private void handleFluidIoChange(Pair<Direction, BlockSection> pair, World world, int x, int y, int z, Side playerFacing, Player player) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IFluidIO) {
            Direction dir = pair.getRight().toDirection(pair.getLeft(), playerFacing);
            if (dir == null) return;
            //TODO: some blocks have some io blocked and this ignores it
            ((IFluidIO) tile).setFluidIOForSide(dir, Connection.values()[(((IFluidIO) tile).getFluidIOForSide(dir).ordinal() + 1) % Connection.values().length]);
            if (tile instanceof IHasIOPreview) {
                ((IHasIOPreview) tile).setTemporaryIOPreview(IOPreview.FLUID, 100);
            }
            player.sendMessage("Side " + dir.getSide() + " set to " + ((IFluidIO) tile).getFluidIOForSide(dir) + "!");
        }
    }

    private void handleItemIoChange(Pair<Direction, BlockSection> pair, World world, int x, int y, int z, Side playerFacing, Player player) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IItemIO) {
            Direction dir = pair.getRight().toDirection(pair.getLeft(), playerFacing);
            if (dir == null) return;

            ((IItemIO) tile).setItemIOForSide(dir, Connection.values()[(((IItemIO) tile).getItemIOForSide(dir).ordinal() + 1) % Connection.values().length]);
            if (tile instanceof IHasIOPreview) {
                ((IHasIOPreview) tile).setTemporaryIOPreview(IOPreview.ITEM, 100);
            }
            player.sendMessage("Side " + dir.getSide() + " set to " + ((IItemIO) tile).getItemIOForSide(dir) + "!");
        }
    }

    private void handleRotationAction(Pair<Direction, BlockSection> pair, World world,
                                      int x, int y, int z, Side playerFacing, Player player) {
        int side = Objects.requireNonNull(pair.getRight().toDirection(pair.getLeft(), playerFacing)).getSideNumber();
        if ((side == 0 || side == 1) && !vertical) {
            return;
        }
        world.setBlockMetadata(x, y, z, side);
    }
}
