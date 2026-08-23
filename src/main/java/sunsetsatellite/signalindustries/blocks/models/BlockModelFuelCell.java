package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.signalindustries.util.MachineTextures;

public class BlockModelFuelCell extends BlockModelFullbright {

	public IconCoordinate topFull = TextureRegistry.getTexture("signalindustries:block/fuel_cell_full");
	public IconCoordinate topEmpty = TextureRegistry.getTexture("signalindustries:block/fuel_cell_empty");
	public IconCoordinate topDepleted = TextureRegistry.getTexture("signalindustries:block/fuel_cell_depleted");

	public BlockModelFuelCell(Block<? extends BlockLogic> block, MachineTextures tex) {
		super(block);
		tex.defaultTextures.forEach((side, text) -> setTex(text, side));
		tex.overbrightTextures.forEach((side, text) -> fullbrightLayer.set(text, side));
	}

	@Override
	public @Nullable IconCoordinate getBlockTextureFromSideAndMetadata(@NotNull Side side, int data) {
		if(side == Side.TOP || side == Side.BOTTOM){
			return switch (data){
				case 1 -> topFull;
				case 2 -> topDepleted;
				default -> topEmpty;
			};
		}
		return super.getBlockTextureFromSideAndMetadata(side, data);
	}
}
