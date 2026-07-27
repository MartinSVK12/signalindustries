package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelCrossedSquares;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGeneric;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFlower;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.useless.dragonfly.data.block.BlockModelData;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.tiles.machines.simple.TileEntityBonsaiPot;

public class BlockModelBonsaiPot<T extends BlockLogic> extends BlockModelGeneric<T> {
	public BlockModelBonsaiPot(@NotNull Block<T> block) {
		super(block, getModel(block));
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos) {
		TileEntityBonsaiPot tile = (TileEntityBonsaiPot) worldSource.getTileEntity(tilePos);
		if(tile != null){
			ItemStack stack = tile.getItem(0);
			if (stack != null && stack.getItem() instanceof ItemBlock) {
				Block<?> block = ((ItemBlock<?>) stack.getItem()).getBlock();
				BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(block);
				if(block.getLogic() instanceof BlockLogicFlower || block == SIBlocks.ashenTreeSapling){
					model.render(tessellator, worldSource, tilePos);
				}
			}
		}
		return super.render(tessellator, worldSource, tilePos);
	}

	private static BlockModelData getModel(Block<?> block){
		if(block == SIBlocks.basicBonsai){
			return BlockModelDispatcher.loadDataModel("signalindustries:block/bonsai_pot");
		} else if (block == SIBlocks.reinforcedBonsai) {
			return BlockModelDispatcher.loadDataModel("signalindustries:block/reinforced_bonsai_pot");
		}
		throw new IllegalArgumentException("Invalid block!");
	}
}
