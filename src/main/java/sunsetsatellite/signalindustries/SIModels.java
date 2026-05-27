package sunsetsatellite.signalindustries;

import net.minecraft.client.render.EntityRendererDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelFluid;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.block.model.BlockModelTransparent;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFluid;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicMachineBase;
import sunsetsatellite.signalindustries.blocks.models.BlockModelMachine;
import sunsetsatellite.signalindustries.blocks.models.BlockModelVerticalMachine;
import turniplabs.halplibe.helper.ModelHelper;

import static sunsetsatellite.signalindustries.SIBlocks.*;
import static sunsetsatellite.signalindustries.SIItems.itemTextures;

public class SIModels {
	public void initBlockModels(BlockModelDispatcher dispatcher) {
		dispatcher.addDispatch(dilithiumCrystalBlock,
			new BlockModelTransparent<>(dilithiumCrystalBlock, false)
				.setAllTextures(blockTextures.get(dilithiumCrystalBlock).defaultTextures.get(Side.TOP))
				.onRenderLayer(1)
		);

		blockTextures.forEach((block, tex) -> {
			if (dispatcher.hasDispatch(block)) return;
			if (Block.hasLogicClass(block, BlockLogicMachineBase.class)) {
				if (((BlockLogicMachineBase) block.getLogic()).isVertical()) {
					dispatcher.addDispatch(block, new BlockModelVerticalMachine(block, tex, blockVerticalTextures.get(block)));
				} else {
					dispatcher.addDispatch(block, new BlockModelMachine(block, tex));
				}
			} else if (Block.hasLogicClass(block, BlockLogicFluid.class)) {
				dispatcher.addDispatch(block, new BlockModelFluid<>(((Block<BlockLogicFluid>) block), tex.defaultTextures.get(Side.TOP), tex.defaultTextures.get(Side.BOTTOM)));
			} else {
				BlockModelStandard<? extends BlockLogic> model = new BlockModelStandard<>(block);
				tex.defaultTextures.forEach((side, text) -> model.setTex(text, side));
				dispatcher.addDispatch(block,model);
			}
		});
	}

	public void initItemModels(ItemModelDispatcher dispatcher) {
		itemTextures.forEach((item, texture) -> {
			ItemModelStandard model = new ItemModelStandard(item, false);
			model.setIcon(SignalIndustries.id("item/"+texture));
			dispatcher.addDispatch(item, model);
		});
	}


	public void initEntityModels(EntityRendererDispatcher dispatcher) {

	}

	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {

	}

	public void initBlockColors(BlockColorDispatcher dispatcher) {

	}
}
