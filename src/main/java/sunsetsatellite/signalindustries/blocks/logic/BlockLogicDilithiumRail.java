package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRail;
import net.minecraft.core.world.World;

public class BlockLogicDilithiumRail extends BlockLogicRail {
    public BlockLogicDilithiumRail(Block<?> block, boolean isPowered) {
        super(block, isPowered);
    }

    @Override
    public void onBlockPlacedByWorld(World world, int x, int y, int z) {
        super.onBlockPlacedByWorld(world, x, y, z);
        onNeighborBlockChange(world, x, y, z, id());
    }
}
