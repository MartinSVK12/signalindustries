package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBdc;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.io.IFluidIO;
import sunsetsatellite.catalyst.core.util.io.IItemIO;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.util.IOPreview;

public class BlockModelIOPreview extends BlockModelStandard<BlockLogic> {

    public IconCoordinate input = TextureRegistry.getTexture("signalindustries:block/input_overlay");
    public IconCoordinate output = TextureRegistry.getTexture("signalindustries:block/output_overlay");
    public IconCoordinate both = TextureRegistry.getTexture("signalindustries:block/both_io_overlay");

    public static boolean ioConfig = false;
    public static WorldSource ioConfigWorld = null;
    public static Vec3i ioConfigPos = null;
    public static IOPreview ioType = IOPreview.NONE;

	protected final TextureLayer fullbrightLayer = new TextureLayer().setAll(BLOCK_TEXTURE_UNASSIGNED);

    public BlockModelIOPreview(Block<? extends BlockLogic> block) {
        super((Block<BlockLogic>) block);
    }

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos) {
		boolean b = super.render(tessellator, worldSource, tilePos);
		AABBdc bounds = this.block.getBoundsFromState(worldSource, tilePos);

		Lighting.disable();
		for (Side side : Side.sides) {
			IconCoordinate tex = getFullbrightLayerTexture(worldSource, tilePos, side);
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

    public IconCoordinate getFullbrightLayerTexture(@NotNull WorldSource world, @NotNull TilePosc tilePos, @NotNull Side side) {
        TileEntity tileEntity = world.getTileEntity(tilePos);
        if (tileEntity instanceof IHasIOPreview) {
            if (((IHasIOPreview) tileEntity).getPreview() != IOPreview.NONE) {
                switch (((IHasIOPreview) tileEntity).getPreview()) {
                    case ITEM:
                        if (tileEntity instanceof IItemIO itemIO) {
                            Connection io = itemIO.getItemIOForSide(Direction.getDirectionFromSide(side.id));
							return switch (io) {
		                        case INPUT -> input;
		                        case OUTPUT -> output;
		                        case BOTH -> both;
		                        case NONE -> null;
	                        };
                        }
                        break;
                    case FLUID:
                        if (tileEntity instanceof IFluidIO fluidIO) {
                            Connection io = fluidIO.getFluidIOForSide(Direction.getDirectionFromSide(side.id));
							return switch (io) {
		                        case INPUT -> input;
		                        case OUTPUT -> output;
		                        case BOTH -> both;
		                        case NONE -> null;
	                        };
                        }
                        break;
                }
            }
        }
        return null;
    }
}
