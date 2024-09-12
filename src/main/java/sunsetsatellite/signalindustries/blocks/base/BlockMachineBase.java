package sunsetsatellite.signalindustries.blocks.base;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.*;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.inventories.base.TileEntityCoverable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.items.ItemConfigurationTablet;
import sunsetsatellite.signalindustries.items.covers.ItemCover;
import sunsetsatellite.signalindustries.util.ConfigurationTabletMode;
import sunsetsatellite.signalindustries.util.IOPreview;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Objects;
import java.util.Random;

public abstract class BlockMachineBase extends BlockContainerTiered implements ISideInteractable {

    public BlockMachineBase(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    public boolean onBlockRightClicked(World world, int x, int y, int z, EntityPlayer player, Side side, double xHit, double yHit) {
        if (!isPlayerHoldingSideInteractableItem(player)) {
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

        if (isPlayerHoldingCover(player)) {
            handleCoverInstallation(player, pair, world, x, y, z, playerFacing);
        }
        return true;
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile instanceof TileEntityCoverable) {
            Direction[] covers = ((TileEntityCoverable) tile).getCovers().keySet().toArray(new Direction[0]);
            for (Direction dir : covers) {
                ((TileEntityCoverable) tile).removeCover(dir);
            }
        }
        super.onBlockRemoved(world, x, y, z, data);
    }

    private void handleCoverInstallation(EntityPlayer player, Pair<Direction, BlockSection> pair, World world, int x, int y, int z, Side playerFacing) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
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

    private void handleCoverRemoval(Pair<Direction, BlockSection> pair, World world, int x, int y, int z, Side playerFacing, EntityPlayer player) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile instanceof TileEntityCoverable) {
            Direction dir = pair.getRight().toDirection(pair.getLeft(), playerFacing);
            if (dir == null) return;
            ((TileEntityCoverable) tile).removeCover(dir, player);
        }
    }

    private boolean isPlayerHoldingCover(EntityPlayer player) {
        return player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemCover;
    }

    private boolean isPlayerHoldingSideInteractableItem(EntityPlayer player) {
        return player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ISideInteractable;
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

    private void handleCoverConfig(Pair<Direction, BlockSection> pair, World world, int x, int y, int z, Side playerFacing, EntityPlayer player) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile instanceof TileEntityCoverable) {
            Direction dir = pair.getRight().toDirection(pair.getLeft(), playerFacing);
            if (dir == null) return;
            if (((TileEntityCoverable) tile).getCovers().get(dir) != null) {
                ((TileEntityCoverable) tile).getCovers().get(dir).openConfiguration(player);
            }
        }
    }

    private void handleFluidIoChange(Pair<Direction, BlockSection> pair, World world, int x, int y, int z, Side playerFacing, EntityPlayer player) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile instanceof IFluidIO) {
            Direction dir = pair.getRight().toDirection(pair.getLeft(), playerFacing);
            if (dir == null) return;
            ((IFluidIO) tile).setFluidIOForSide(dir, Connection.values()[(((IFluidIO) tile).getFluidIOForSide(dir).ordinal() + 1) % Connection.values().length]);
            if (tile instanceof IHasIOPreview) {
                ((IHasIOPreview) tile).setTemporaryIOPreview(IOPreview.FLUID, 100);
            }
            player.sendMessage("Side " + dir.getSide() + " set to " + ((IFluidIO) tile).getFluidIOForSide(dir) + "!");
        }
    }

    private void handleItemIoChange(Pair<Direction, BlockSection> pair, World world, int x, int y, int z, Side playerFacing, EntityPlayer player) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
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
                                      int x, int y, int z, Side playerFacing, EntityPlayer player) {
        int side = Objects.requireNonNull(pair.getRight().toDirection(pair.getLeft(), playerFacing)).getSideNumber();
        if ((side == 0 || side == 1) && !(this instanceof BlockVerticalMachineBase)) {
            return;
        }
        world.setBlockMetadata(x, y, z, side);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean isPoweringTo(WorldSource worldSource, int x, int y, int z, int side) {
        TileEntity tile = worldSource.getBlockTileEntity(x, y, z);
        return false;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int side) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        if (blockId > 0 && Block.blocksList[blockId].canProvidePower()) {
            boolean powered = world.isBlockIndirectlyGettingPowered(x, y, z);
            onPoweredBlockChange(world, x, y, z, powered);
        }
    }

    public void onPoweredBlockChange(World world, int x, int y, int z, boolean powered) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile instanceof TileEntityTieredMachineBase) {
            ((TileEntityTieredMachineBase) tile).onPoweredBlockChange(powered);
        }
    }

    public void dropContents(World world, int i, int j, int k) {
        IInventory tile = (IInventory) world.getBlockTileEntity(i, j, k);
        if (tile != null) {
            Random random = new Random();
            for (int l = 0; l < tile.getSizeInventory(); ++l) {
                ItemStack itemstack = tile.getStackInSlot(l);
                if (itemstack != null) {
                    float f = random.nextFloat() * 0.8F + 0.1F;
                    float f1 = random.nextFloat() * 0.8F + 0.1F;
                    float f2 = random.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0) {
                        int i1 = random.nextInt(21) + 10;
                        if (i1 > itemstack.stackSize) {
                            i1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= i1;
                        EntityItem entityitem = new EntityItem(world, (float) i + f, (float) j + f1, (float) k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getMetadata(),itemstack.getData()));
                        float f3 = 0.05F;
                        entityitem.xd = (float) random.nextGaussian() * f3;
                        entityitem.yd = (float) random.nextGaussian() * f3 + 0.2F;
                        entityitem.zd = (float) random.nextGaussian() * f3;
                        world.entityJoinedWorld(entityitem);
                    }
                }
            }
        }
    }
}
