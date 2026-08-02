package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.useless.dragonfly.data.block.BlockModelData;
import org.useless.dragonfly.models.block.StaticBlockModel;
import sunsetsatellite.signalindustries.items.ItemWarpOrb;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityPulsar;

public class BlockModelPulsar<T extends BlockLogic> extends BlockModelGeneric<T> {

	public BlockModelData active;
	public BlockModelData warp;

	public BlockModelPulsar(@NotNull Block<T> block) {
		super(block, BlockModelDispatcher.loadDataModel("signalindustries:block/pulsar/inactive"));
		active = BlockModelDispatcher.loadDataModel("signalindustries:block/pulsar/active");
		warp = BlockModelDispatcher.loadDataModel("signalindustries:block/pulsar/warp");
	}

	@Override
	public @NotNull StaticBlockModel getModel(@NotNull WorldSource source, @NotNull TilePosc tilePosc) {

		TileEntity t = source.getTileEntity(tilePosc);
		if(t instanceof TileEntityPulsar pulsar){
			if(pulsar.getItem(0) != null && pulsar.getItem(0).getItem() instanceof ItemWarpOrb){
				return warp.asModel();
			}
			if(pulsar.isBurning()){
				return active.asModel();
			}
		}

		return super.getModel(source, tilePosc);
	}
}
