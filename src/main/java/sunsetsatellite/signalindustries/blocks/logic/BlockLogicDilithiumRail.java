package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRail;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public class BlockLogicDilithiumRail extends BlockLogicRail {
    public BlockLogicDilithiumRail(Block<?> block, boolean isPowered) {
        super(block, isPowered);
    }

	@Override
	public void onPlacedByWorld(@NotNull World world, @NotNull TilePosc tilePos) {
		super.onPlacedByWorld(world, tilePos);
		onNeighborChanged(world, tilePos, block);
	}
}
