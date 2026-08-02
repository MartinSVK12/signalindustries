package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicMachine;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityPulsar;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockLogicPulsar extends BlockLogicMachine {
    public BlockLogicPulsar(Block<?> block, Material material, Tier tier) {
        super(block, material, tier, TileEntityPulsar::new, "pulsar_block");
    }

	@Override
	public void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> block) {
		super.onNeighborChanged(world, tilePos, block);
		TileEntityPulsar tile = (TileEntityPulsar) world.getTileEntity(tilePos);
		if (tile != null) {
			if (world.hasNeighborSignal(tilePos)) {
				tile.activate();
			}
		}
	}
}
