package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBdc;

public class BlockModelFullbright extends BlockModelStandard<BlockLogic> {

	public final TextureLayer fullbrightLayer = new TextureLayer().setAll(BLOCK_TEXTURE_UNASSIGNED);

	public BlockModelFullbright(Block<? extends BlockLogic> block) {
		super((Block<BlockLogic>) block);
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos) {
		boolean b = super.render(tessellator, worldSource, tilePos);
		AABBdc bounds = this.block.getBoundsFromState(worldSource, tilePos);

		Lighting.disable();
		tessellator.setLightmapCoord2i(15,15);
		GLRenderer.setLightmapCoord2i(15,15);
		for (Side side : Side.sides) {
			IconCoordinate tex = getFullbrightTexture(worldSource, tilePos, side);
			if(tex == null) continue;
			switch (side) {
				case BOTTOM:
					renderBlocks.renderBottomFace(tessellator, bounds, tilePos, tex);
					break;
				case TOP:
					renderBlocks.renderTopFace(tessellator, bounds, tilePos, tex);
					break;
				case NORTH:
					renderBlocks.renderNorthFace(tessellator, bounds, tilePos, tex);
					break;
				case SOUTH:
					renderBlocks.renderSouthFace(tessellator, bounds, tilePos, tex);
					break;
				case WEST:
					renderBlocks.renderWestFace(tessellator, bounds, tilePos, tex);
					break;
				case EAST:
					renderBlocks.renderEastFace(tessellator, bounds, tilePos, tex);
					break;
			}
		}
		Lighting.enableLight();
		return b;
	}

	public IconCoordinate getFullbrightTexture(@NotNull WorldSource world, @NotNull TilePosc tilePos, @NotNull Side side) {
		if(fullbrightLayer.get(side) != BLOCK_TEXTURE_UNASSIGNED){
			return fullbrightLayer.get(side);
		}
		return null;
	}

}
