package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicConduitBase;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityItemConduit;
import sunsetsatellite.signalindustries.util.PipeMode;
import sunsetsatellite.signalindustries.util.PipeType;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.function.Supplier;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class BlockLogicItemConduit extends BlockLogicConduitBase {

    public PipeType type;

    public BlockLogicItemConduit(Block<?> block, Material material, Tier tier, PipeType type, Supplier<TileEntity> tileEntitySupplier) {
        super(block, material, tier, tileEntitySupplier, ConduitCapability.ITEM);
        this.type = type;
    }

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		if (super.onInteracted(world, tilePos, player, side, xHit, yHit)) {
			return true;
		}
		if (player.isSneaking() && type == PipeType.NORMAL && !world.isClientSide) {
			TileEntityItemConduit tile = (TileEntityItemConduit) world.getTileEntity(tilePos);
			tile.mode = PipeMode.values()[tile.mode.ordinal() + 1 <= PipeMode.values().length - 1 ? tile.mode.ordinal() + 1 : 0];
			player.sendMessage("Pipe mode changed to: " + tile.mode);
			return true;
		}
		if (!EnvironmentHelper.isMultiplayerServer() && type == PipeType.RESTRICT) {
			TileEntityItemConduit tile = (TileEntityItemConduit) world.getTileEntity(tilePos);
			Catalyst.displayGui(player, tile, key("gui/restrict_item_conduit"));
			return true;
		}
		if (!world.isClientSide && type == PipeType.SENSOR) {
			TileEntityItemConduit tile = (TileEntityItemConduit) world.getTileEntity(tilePos);
			Catalyst.displayGui(player, tile, key("gui/sensor_item_conduit"));
			return true;
		}
		return false;
	}

    @Override
    public boolean isSignalSource() {
        return true;
    }

	@Override
	public boolean isEmittingDirectSignal(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side) {
		TileEntityItemConduit tile = (TileEntityItemConduit) world.getTileEntity(tilePos);
		return tile != null && tile.type == PipeType.SENSOR && tile.sensorActive;
	}

	@Override
	public boolean isEmittingSignal(@NotNull WorldSource source, @NotNull TilePosc tilePos, @NotNull Side side) {
		TileEntityItemConduit tile = (TileEntityItemConduit) source.getTileEntity(tilePos);
		return tile != null && tile.type == PipeType.SENSOR && tile.sensorActive;
	}
}
