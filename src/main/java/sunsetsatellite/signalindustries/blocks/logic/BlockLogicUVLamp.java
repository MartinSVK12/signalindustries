package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
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
	public void onPlacedByWorld(@NotNull World world, @NotNull TilePosc tilePos) {
		super.onPlacedByWorld(world, tilePos);
		SignalIndustries.uvLamps.add(new BlockInstance(this.block, new Vec3i(tilePos), null));
	}

	@Override
	public void onRemoved(@NotNull World world, @NotNull TilePosc tilePos, int data) {
		SignalIndustries.uvLamps.removeIf((B) -> B.pos.equals(new Vec3i(tilePos)));
		super.onRemoved(world, tilePos, data);
	}

	@Override
	public void updateTick(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Random rand, boolean isRandomTick) {
		super.updateTick(world, tilePos, rand, isRandomTick);
		if (world.hasDirectSignal(tilePos)) {
			world.setBlockDataNotify(tilePos, 1);
		} else if (!world.hasNeighborSignal(tilePos)) {
			world.setBlockDataNotify(tilePos, 0);
		}
	}

	@Override
	public void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> block) {
		if (block.isSignalSource()) {
			boolean flag = world.hasNeighborSignal(tilePos) || world.hasNeighborSignal(tilePos.x(), tilePos.y() + 1, tilePos.z());
			boolean flag2 = !world.hasNeighborSignal(tilePos) && !world.hasNeighborSignal(tilePos.x(), tilePos.y() + 1, tilePos.z());
			if (flag || flag2) {
				world.scheduleBlockUpdate(tilePos, block, tickDelay());
			}
		}
	}
}
