package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.tiles.TileEntityRedstoneClock;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class BlockLogicRedstoneClock extends BlockLogic {
    public BlockLogicRedstoneClock(Block<?> block, Material material) {
        super(block, material);
        block.withEntity(TileEntityRedstoneClock::new);
    }

    @Override
    public boolean isSignalSource() {
        return true;
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }


	@Override
	public boolean renderAsNormalBlockOnCondition(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		return false;
	}

	@Override
	public boolean isEmittingDirectSignal(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side) {
		TileEntityRedstoneClock tile = (TileEntityRedstoneClock) world.getTileEntity(tilePos);
		return tile != null && tile.active;
	}

	@Override
	public boolean isEmittingSignal(@NotNull WorldSource source, @NotNull TilePosc tilePos, @NotNull Side side) {
		TileEntityRedstoneClock tile = (TileEntityRedstoneClock) source.getTileEntity(tilePos);
		return tile != null && tile.active;
	}

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		if (world.isClientSide) {
			return true;
		}
		if (player.isSneaking()) {
			TileEntityRedstoneClock tile = (TileEntityRedstoneClock) world.getTileEntity(tilePos);
			if (tile != null) {
				tile.disabled = !tile.disabled;
			}
		} else {
			TileEntityRedstoneClock tile = (TileEntityRedstoneClock) world.getTileEntity(tilePos);
			if (tile != null) {
				Catalyst.displayGui(player, tile, key("gui/redstone_clock"));
			}
		}
		return true;
	}
}
