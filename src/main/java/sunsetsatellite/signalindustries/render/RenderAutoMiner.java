package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.LightIndexHelper;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityAutoMiner;

import java.util.Objects;

public class RenderAutoMiner extends TileEntityRenderer<TileEntityAutoMiner> {

	public void drawBlock(TessellatorGeneral t, BlockModel<?> model, int meta, float alpha) {
		TextureRegistry.worldAtlas.bind();
		GLRenderer.pushFrame();
		GLRenderer.setShader(Shaders.WORLD);
		GLRenderer.enableState(State.BLEND);
		GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
		GLRenderer.setColor4f(1,1,1,alpha);
		model.renderStandalone(t, meta, LightIndexHelper.lightIndex2i(15,15));
		GLRenderer.setColor4f(1,1,1,1);
		GLRenderer.disableState(State.BLEND);
		GLRenderer.popFrame();
		GLRenderer.enableState(State.CULL_FACE);
	}

	@Override
	public void doRender(TessellatorGeneral tessellator, TileEntityAutoMiner tile, double x, double y, double z, float partialTick) {
		int tx = tile.tilePos.x;
		int ty = tile.tilePos.y;
		int tz = tile.tilePos.z;
		int cx = tile.current.x;
		int cy = tile.current.y;
		int cz = tile.current.z;
		World world = tile.worldObj;

		if (!Objects.equals(world.getLevelData().getWorldName(), "modelviewer")) {
			GLRenderer.pushFrame();
			GLRenderer.modelM4f().translate((float) x - (tx - cx) + 0.25f, (float) y + 4.25f, (float) z + (cz - tz) + 0.25f);
			GLRenderer.modelM4f().scale(0.5f, 0.5f, 0.5f);
			GLRenderer.popFrame();

			if (cy - (ty + 4) < 0) {
				GLRenderer.pushFrame();
				GLRenderer.modelM4f().translate((float) x - (tx - cx) + 0.5f, (float) y - (ty - cy) + 1.2f, (float) z + (cz - tz) + 0.5f);
				GLRenderer.modelM4f().scale(0.50f, 1f, 0.50f);
				drawBlock(tessellator, BlockModelDispatcher.getInstance().getDispatch(Blocks.BASALT), 0, 1);
				GLRenderer.popFrame();
			}

			if (cy - (ty + 4) < 0) {
				GLRenderer.pushFrame();
				GLRenderer.modelM4f().translate((float) x - (tx - cx) + 0.5f, (float) y - (ty - cy) + 0.3f, (float) z + (cz - tz) + 0.5f);
				GLRenderer.modelM4f().scale(0.25f, 0.75f, 0.25f);
				if (tile.hasSilkTouch()) {
					drawBlock(tessellator, BlockModelDispatcher.getInstance().getDispatch(Blocks.BLOCK_GOLD), 0, 1);
				} else {
					drawBlock(tessellator, BlockModelDispatcher.getInstance().getDispatch(Blocks.BLOCK_DIAMOND), 0, 1);
				}
				GLRenderer.popFrame();
			}
		}
	}
}
