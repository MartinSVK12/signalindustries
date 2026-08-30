package sunsetsatellite.signalindustries.blocks.logic.base;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.IntTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.io.IFluidIO;
import sunsetsatellite.catalyst.core.util.io.IItemIO;
import sunsetsatellite.catalyst.core.util.section.BlockSection;
import sunsetsatellite.catalyst.core.util.section.ISideInteractable;
import sunsetsatellite.catalyst.core.util.vector.Vec2f;
import sunsetsatellite.signalindustries.covers.RedstoneCover;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.items.ItemConfigurationTablet;
import sunsetsatellite.signalindustries.items.covers.ItemCover;
import sunsetsatellite.signalindustries.tiles.base.TileEntityCoverable;
import sunsetsatellite.signalindustries.util.ConfigurationTabletMode;
import sunsetsatellite.signalindustries.util.IO;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class BlockLogicMachineBase extends BlockLogicTiered implements ISideInteractable {

    protected boolean vertical = false;
    protected boolean solid = false;
    protected boolean forceNonSolid = false;

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
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		if (!isPlayerHoldingSideInteractableItem(player)) {
			return false;
		}

		Pair<Direction, BlockSection> pair = Catalyst.getBlockSurfaceClickPosition(world, player, side, new Vec2f(xHit, yHit));
		Side playerFacing = Catalyst.calculatePlayerFacing(player.yRot);
		if (pairIsInvalid(pair)) {
			return false;
		}

		if (isPlayerHoldingConfigTablet(player)) {
			handleConfigTabletAction(player, pair, world, tilePos, playerFacing);
		}

		if (isPlayerHoldingCover(player)) {
			handleCoverInstallation(player, pair, world, tilePos, playerFacing);
		}
		return true;
	}

	@Override
	public void onRemoved(@NotNull World world, @NotNull TilePosc tilePos, int data) {
		if(!EnvironmentHelper.isClientWorld()){
			TileEntity tile = world.getTileEntity(tilePos);
			if(tile instanceof TileEntityCoverable coverable){
				Direction[] covers = coverable.getCovers().keySet().toArray(new Direction[0]);
				for (Direction dir : covers) {
					coverable.removeCover(dir);
				}
			}
		}
		super.onRemoved(world, tilePos, data);
	}

    private void handleCoverInstallation(Player player, Pair<Direction, BlockSection> pair, World world, @NotNull TilePosc tilePos, Side playerFacing) {
        TileEntity tile = world.getTileEntity(tilePos);
        ItemCover cover = (ItemCover) player.getCurrentEquippedItem().getItem();
        if (tile instanceof TileEntityCoverable) {
            Direction dir = pair.getRight().toDirection(pair.getLeft(), playerFacing);
            if (dir == null) return;
            if (((TileEntityCoverable) tile).installCover(dir, cover.coverSupplier.get(), player)) {
                player.getCurrentEquippedItem().stackSize--;
                if (player.getCurrentEquippedItem().stackSize <= 0) {
                    player.inventory.setCurrentItem(null);
                }
            }
        }
    }

    private void handleCoverRemoval(Pair<Direction, BlockSection> pair, World world, @NotNull TilePosc tilePos, Side playerFacing, Player player) {
        TileEntity tile = world.getTileEntity(tilePos);
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
                                          World world, TilePosc tilePos, Side playerFacing) {

        ConfigurationTabletMode mode = ConfigurationTabletMode.values()[player.getCurrentEquippedItem().getData().getInteger("mode")];
		switch (mode) {
		    case ROTATION -> handleRotationAction(pair, world, tilePos, playerFacing, player);
			case ITEM -> handleItemIoChange(pair, world, tilePos, playerFacing, player);
			case FLUID -> handleFluidIoChange(pair, world, tilePos, playerFacing, player);
			case DISCONNECTOR -> handleCoverRemoval(pair, world, tilePos, playerFacing, player);
			case CONFIGURATOR -> handleCoverConfig(pair, world, tilePos, playerFacing, player);
			case COPY_PASTE -> handleCopyPaste(pair, world, tilePos, playerFacing, player);
		}
    }

	private void handleCopyPaste(Pair<Direction, BlockSection> pair, World world, TilePosc tilePos, Side playerFacing, Player player) {
		ItemStack tablet = player.getCurrentEquippedItem();
		TileEntity tile = world.getTileEntity(tilePos);
		if(tablet.getData().containsKey("CopyPaste")){
			CompoundTag copyPaste = tablet.getData().getCompound("CopyPaste");
			if(tile instanceof IFluidIO fluidIO){
				CompoundTag fluidIoTag = copyPaste.getCompound("Fluid");
				CompoundTag connectionsTag = fluidIoTag.getCompound("fluidConnections");
				for (Object con : connectionsTag.getValues()) {
					fluidIO.setFluidIOForSide(Direction.values()[Integer.parseInt(((IntTag) con).getTagName())], Connection.values()[((IntTag) con).getValue()]);
				}
			}
			player.sendStatusMessage(I18n.getInstance().translateKey("event.signalindustries.pasted"));
		} else {
			CompoundTag copyPaste = new CompoundTag();
			if(tile instanceof IFluidIO fluidIO){
				CompoundTag fluidIoTag = new CompoundTag();
				CompoundTag connectionsTag = new CompoundTag();
				for (Direction dir : Direction.values()) {
					connectionsTag.putInt(String.valueOf(dir.ordinal()), fluidIO.getFluidIOForSide(dir).ordinal());
				}
				fluidIoTag.putCompound("fluidConnections", connectionsTag);
				copyPaste.put("Fluid", fluidIoTag);
			}
			tablet.getData().put("CopyPaste", copyPaste);
			player.sendStatusMessage(I18n.getInstance().translateKey("event.signalindustries.copied"));
		}
	}

	private void handleCoverConfig(Pair<Direction, BlockSection> pair, World world, @NotNull TilePosc tilePos, Side playerFacing, Player player) {
        TileEntity tile = world.getTileEntity(tilePos);
        if (tile instanceof TileEntityCoverable) {
            Direction dir = pair.getRight().toDirection(pair.getLeft(), playerFacing);
            if (dir == null) return;
            if (((TileEntityCoverable) tile).getCovers().get(dir) != null) {
                ((TileEntityCoverable) tile).getCovers().get(dir).openConfiguration(player, dir);
            }
        }
    }

    private void handleFluidIoChange(Pair<Direction, BlockSection> pair, World world, @NotNull TilePosc tilePos, Side playerFacing, Player player) {
        TileEntity tile = world.getTileEntity(tilePos);
        if (tile instanceof IFluidIO) {
            Direction dir = pair.getRight().toDirection(pair.getLeft(), playerFacing);
            if (dir == null) return;
            //TODO: some blocks have some io blocked and this ignores it
            ((IFluidIO) tile).setFluidIOForSide(dir, Connection.values()[(((IFluidIO) tile).getFluidIOForSide(dir).ordinal() + 1) % Connection.values().length]);
            if (tile instanceof IHasIOPreview) {
                ((IHasIOPreview) tile).setTemporaryIOPreview(IO.FLUID, 100);
            }
            player.sendMessage("Side " + dir.getSide() + " set to " + ((IFluidIO) tile).getFluidIOForSide(dir) + "!");
        }
    }

    private void handleItemIoChange(Pair<Direction, BlockSection> pair, World world, @NotNull TilePosc tilePos, Side playerFacing, Player player) {
        TileEntity tile = world.getTileEntity(tilePos);
        if (tile instanceof IItemIO) {
            Direction dir = pair.getRight().toDirection(pair.getLeft(), playerFacing);
            if (dir == null) return;

            ((IItemIO) tile).setItemIOForSide(dir, Connection.values()[(((IItemIO) tile).getItemIOForSide(dir).ordinal() + 1) % Connection.values().length]);
            if (tile instanceof IHasIOPreview) {
                ((IHasIOPreview) tile).setTemporaryIOPreview(IO.ITEM, 100);
            }
            player.sendMessage("Side " + dir.getSide() + " set to " + ((IItemIO) tile).getItemIOForSide(dir) + "!");
        }
    }

    private void handleRotationAction(Pair<Direction, BlockSection> pair, World world,
                                      @NotNull TilePosc tilePos, Side playerFacing, Player player) {
        int side = Objects.requireNonNull(pair.getRight().toDirection(pair.getLeft(), playerFacing)).getSideNumber();
        if ((side == 0 || side == 1) && !vertical) {
            return;
        }
        world.setBlockData(tilePos, side);
    }

    @Override
    public boolean isSignalSource() {
        return true;
    }

	@Override
	public boolean isEmittingSignal(@NotNull WorldSource world, @NotNull TilePosc tilePos, @NotNull Side side) {
		TileEntity tile = world.getTileEntity(tilePos);
		if(tile instanceof TileEntityCoverable coverable){
			if(coverable.hasCoverAnywhere(RedstoneCover.class)){
				RedstoneCover cover = coverable.getCover(RedstoneCover.class);
				solid = false;
				return cover.sensorActive;
			}
		}
		solid = true;
		return false;
	}

	@Override
	public boolean isEmittingDirectSignal(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side) {
		TileEntity tile = world.getTileEntity(tilePos);
		if(tile instanceof TileEntityCoverable coverable){
			if(coverable.hasCoverAnywhere(RedstoneCover.class)){
				RedstoneCover cover = coverable.getCover(RedstoneCover.class);
				solid = false;
				return cover.sensorActive;
			}
		}
		solid = true;
		return false;
	}

    public BlockLogicMachineBase setNonSolid() {
        this.forceNonSolid = true;
        return this;
    }

    @Override
    public boolean isSolidRender() {
        return solid && !forceNonSolid;
    }

	@Override
	public boolean renderAsNormalBlockOnCondition(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		return solid && !forceNonSolid;
	}
}
