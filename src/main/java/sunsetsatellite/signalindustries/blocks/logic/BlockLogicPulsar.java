package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import sunsetsatellite.retrostorage.tiles.TileEntityImporter;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicMachine;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityPulsar;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.function.Supplier;

public class BlockLogicPulsar extends BlockLogicMachine {
    public BlockLogicPulsar(Block<?> block, Material material, Tier tier) {
        super(block, material, tier, TileEntityPulsar::new, "pulsar_block");
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        super.onNeighborBlockChange(world, x, y, z, blockId);
        TileEntityPulsar tile = (TileEntityPulsar) world.getTileEntity(x, y, z);
        if(tile != null) {
            if (world.hasNeighborSignal(x, y, z)) {
                tile.activate();
            }
        }
    }
}
