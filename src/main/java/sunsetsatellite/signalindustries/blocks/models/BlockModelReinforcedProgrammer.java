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
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.items.ItemRomChip;
import sunsetsatellite.signalindustries.items.applications.ItemTrigger;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithAbility;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityProgrammer;

public class BlockModelReinforcedProgrammer<T extends BlockLogic> extends BlockModelGeneric<T> {

	public BlockModelData inserted;
	public BlockModelData activated;
	public BlockModelData finished;

	public BlockModelReinforcedProgrammer(@NotNull Block<T> block) {
		super(block, BlockModelDispatcher.loadDataModel("signalindustries:block/flash_reprogrammer"));
		inserted = BlockModelDispatcher.loadDataModel("signalindustries:block/flash_reprogrammer_inserted");
		activated = BlockModelDispatcher.loadDataModel("signalindustries:block/flash_reprogrammer_activated");
		finished = BlockModelDispatcher.loadDataModel("signalindustries:block/flash_reprogrammer_finished");
	}

	@Override
	public @NotNull StaticBlockModel getModel(@NotNull WorldSource source, @NotNull TilePosc tilePosc) {
		TileEntity t = source.getTileEntity(tilePosc);
		if(t instanceof TileEntityProgrammer tile){
			if (tile.itemContents[1] != null && tile.itemContents[1].getItem() instanceof ItemWithAbility) {
				return finished.asModel();
			}
			if(tile.progressTicks > 0){
				return activated.asModel();
			}
			if(tile.itemContents[1] != null && tile.itemContents[1].getItem().equals(SIItems.abilityContainerCasing)){
				return inserted.asModel();
			}
		}

		return super.getModel(source, tilePosc);
	}
}
