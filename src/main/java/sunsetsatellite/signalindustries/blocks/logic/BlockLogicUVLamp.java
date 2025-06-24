package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.tiles.TileEntityUVLamp;

import java.util.Random;

public class BlockLogicUVLamp extends BlockLogic {
    public BlockLogicUVLamp(Block<?> block, Material material) {
        super(block, material);
        block.withEntity(TileEntityUVLamp::new);
    }

    @Override
    public int tickDelay() {
        return 1;
    }

    @Override
    public void onBlockPlacedByWorld(World world, int x, int y, int z) {
        super.onBlockPlacedByWorld(world, x, y, z);
        SignalIndustries.uvLamps.add(new BlockInstance(this.block, new Vec3i(x, y, z), null));
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        SignalIndustries.uvLamps.removeIf((B) -> B.pos.equals(new Vec3i(x, y, z)));
        super.onBlockRemoved(world, x, y, z, data);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        super.updateTick(world, x, y, z, rand);
        if (world.hasDirectSignal(x, y, z)) {
            world.setBlockMetadataWithNotify(x, y, z, 1);
        } else if (!world.hasNeighborSignal(x, y, z)) {
            world.setBlockMetadataWithNotify(x, y, z, 0);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
        if (l > 0 && Blocks.blocksList[l] != null && Blocks.blocksList[l].isSignalSource()) {
            boolean flag = world.hasNeighborSignal(i, j, k) || world.hasNeighborSignal(i, j + 1, k);
            boolean flag2 = !world.hasNeighborSignal(i, j, k) && !world.hasNeighborSignal(i, j + 1, k);
            if (flag || flag2) {
                world.scheduleBlockUpdate(i, j, k, id(), tickDelay());
            }
        }
    }
}
