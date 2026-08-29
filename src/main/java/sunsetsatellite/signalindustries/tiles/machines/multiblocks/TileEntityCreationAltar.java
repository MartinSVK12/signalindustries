package sunsetsatellite.signalindustries.tiles.machines.multiblocks;

import net.minecraft.core.block.Block;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.ITileEntityInit;
import sunsetsatellite.catalyst.multiblocks.IMultiblock;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.MultiblockInstance;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;

public class TileEntityCreationAltar extends TileEntityTieredMachineBase implements IMultiblock, ITileEntityInit {

	public MultiblockInstance multiblock;

	@Override
	public void init(Block<?> block) {
		super.init(block);
		multiblock = new MultiblockInstance(this, Multiblock.multiblocks.get("creationAltar"));
	}

	@Override
	public @NonNull String getNameTranslationKey() {
		return "container.signalindustries.creationAltar";
	}

	@Override
	public MultiblockInstance getMultiblock() {
		return multiblock;
	}
}
