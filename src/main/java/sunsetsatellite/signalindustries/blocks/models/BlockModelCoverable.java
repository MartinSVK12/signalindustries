package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBdc;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.signalindustries.covers.CoverBase;
import sunsetsatellite.signalindustries.tiles.base.TileEntityCoverable;

public class BlockModelCoverable extends BlockModelIOPreview {
	public BlockModelCoverable(Block<? extends BlockLogic> block) {
		super(block);
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource world, @NotNull TilePosc tilePos) {
		TileEntity tile = world.getTileEntity(tilePos);
		if (tile == null) {
			return super.render(tessellator, world, tilePos);
		}
		AABBdc bounds = this.block.getBoundsFromState(world, tilePos);
		if(tile instanceof TileEntityCoverable machine){
			for (Direction dir : machine.getCovers().keySet()) {
				CoverBase cover = machine.getCovers().get(dir);
				if (cover == null) continue;
				Vec3f vec = new Vec3f(tilePos).add(dir.getVecF().divide(100));
				IconCoordinate tex = TextureRegistry.getTexture(cover.getTexture());
				switch (dir.getSide()) {
					case BOTTOM -> {
						renderBlocks.renderBottomFace(tessellator, bounds, vec.x, vec.y, vec.z, tex);
					}
					case TOP -> {
						renderBlocks.renderTopFace(tessellator, bounds, vec.x, vec.y, vec.z, tex);
					}
					case NORTH -> {
						renderBlocks.renderNorthFace(tessellator, bounds, vec.x, vec.y, vec.z, tex);
					}
					case SOUTH -> {
						renderBlocks.renderSouthFace(tessellator, bounds, vec.x, vec.y, vec.z, tex);
					}
					case WEST -> {
						renderBlocks.renderWestFace(tessellator, bounds, vec.x, vec.y, vec.z, tex);
					}
					case EAST -> {
						renderBlocks.renderEastFace(tessellator, bounds, vec.x, vec.y, vec.z, tex);
					}
				}
			}
		}
		return super.render(tessellator, world, tilePos);
	}
}
