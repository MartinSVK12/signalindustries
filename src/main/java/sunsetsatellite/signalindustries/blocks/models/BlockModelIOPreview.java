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
import sunsetsatellite.signalindustries.util.IO;

public class BlockModelIOPreview extends BlockModelFullbright {

    public IconCoordinate input = TextureRegistry.getTexture("signalindustries:block/input_overlay");
    public IconCoordinate output = TextureRegistry.getTexture("signalindustries:block/output_overlay");
    public IconCoordinate both = TextureRegistry.getTexture("signalindustries:block/both_io_overlay");

    public static boolean ioConfig = false;
    public static WorldSource ioConfigWorld = null;
    public static TilePosc ioConfigPos = null;
    public static IO ioType = IO.NONE;

    public BlockModelIOPreview(Block<? extends BlockLogic> block) {
        super(block);
    }

	@Override
	public void renderStandalone(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		super.renderStandalone(tessellator, metadata, lightIndex);

		if(!ioConfig || ioConfigPos == null || ioConfigWorld == null) return;

		AABBdc bounds = getBlockBoundsForItemRender();
		tessellator.offsetTranslation(-0.5, -0.5, -0.5);

		tessellator.startDrawingQuads();
		tessellator.setLightmapCoord1i(lightIndex);

		tessellator.setNormal(0.0f, -1.0f, 0.0f);
		IconCoordinate bottom = getFullbrightTexture(ioConfigWorld, ioConfigPos, Side.BOTTOM);
		renderBlocks.renderBottomFace(tessellator, bounds, 0.0, 0.0, 0.0, bottom);

		tessellator.setNormal(0.0f, 1.0f, 0.0f);
		IconCoordinate top = getFullbrightTexture(ioConfigWorld, ioConfigPos, Side.TOP);
		renderBlocks.renderTopFace(tessellator, bounds, 0.0, 0.0, 0.0, top);

		tessellator.setNormal(0.0f, 0.0f, -1.0f);
		IconCoordinate north = getFullbrightTexture(ioConfigWorld, ioConfigPos, Side.NORTH);
		renderBlocks.renderNorthFace(tessellator, bounds, 0.0, 0.0, 0.0, north);

		tessellator.setNormal(0.0f, 0.0f, 1.0f);
		IconCoordinate south = getFullbrightTexture(ioConfigWorld, ioConfigPos, Side.SOUTH);
		renderBlocks.renderSouthFace(tessellator, bounds, 0.0, 0.0, 0.0, south);

		tessellator.setNormal(-1.0f, 0.0f, 0.0f);
		IconCoordinate west = getFullbrightTexture(ioConfigWorld, ioConfigPos, Side.WEST);
		renderBlocks.renderWestFace(tessellator, bounds, 0.0, 0.0, 0.0, west);

		tessellator.setNormal(1.0f, 0.0f, 0.0f);
		IconCoordinate east = getFullbrightTexture(ioConfigWorld, ioConfigPos, Side.EAST);
		renderBlocks.renderEastFace(tessellator, bounds, 0.0, 0.0, 0.0, east);

		tessellator.draw();

		tessellator.offsetTranslation(0.5F, 0.5, 0.5F);
	}

	@Override
	public IconCoordinate getFullbrightTexture(@NotNull WorldSource world, @NotNull TilePosc tilePos, @NotNull Side side) {
        TileEntity tileEntity = world.getTileEntity(tilePos);
        if (tileEntity instanceof IHasIOPreview) {
			if(ioConfig){
				switch (ioType) {
					case ITEM -> {
						if (tileEntity instanceof IItemIO itemIO) {
							Connection io = itemIO.getItemIOForSide(Direction.getDirectionFromSide(side.id));
							return switch (io) {
								case INPUT -> input;
								case OUTPUT -> output;
								case BOTH -> both;
								case NONE -> null;
							};
						}
					}
					case FLUID -> {
						if (tileEntity instanceof IFluidIO fluidIO) {
							Connection io = fluidIO.getFluidIOForSide(Direction.getDirectionFromSide(side.id));
							return switch (io) {
								case INPUT -> input;
								case OUTPUT -> output;
								case BOTH -> both;
								case NONE -> null;
							};
						}
					}
				}
			}
            if (((IHasIOPreview) tileEntity).getPreview() != IO.NONE) {
                switch (((IHasIOPreview) tileEntity).getPreview()) {
					case ITEM -> {
						if (tileEntity instanceof IItemIO itemIO) {
							Connection io = itemIO.getItemIOForSide(Direction.getDirectionFromSide(side.id));
							return switch (io) {
								case INPUT -> input;
								case OUTPUT -> output;
								case BOTH -> both;
								case NONE -> null;
							};
						}
					}
					case FLUID -> {
						if (tileEntity instanceof IFluidIO fluidIO) {
							Connection io = fluidIO.getFluidIOForSide(Direction.getDirectionFromSide(side.id));
							return switch (io) {
								case INPUT -> input;
								case OUTPUT -> output;
								case BOTH -> both;
								case NONE -> null;
							};
						}
					}
                }
            }
        }
        return null;
    }
}
