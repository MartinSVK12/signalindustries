package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.renderer.*;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.tessellator.TessellatorShader;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.LightIndexHelper;

public abstract class RenderSI<T extends TileEntity> extends TileEntityRenderer<T> {
	public static void drawBlock(TessellatorGeneral t, BlockModel<?> model, int meta, float alpha) {
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

	public static void renderLineBetweenTwoPoints(int x1, int y1, int z1, long x2, long y2, long z2, float red, float green, float blue, float alpha, float width, double x, double y, double z) {
		GLRenderer.pushFrame();
		Lighting.disable();
		GLRenderer.setShader(Shaders.LINES);
		GLRenderer.enableState(State.BLEND);
		GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
		GLRenderer.setColor4f(red, green, blue, alpha);
		GLRenderer.setLineWidth(width);
		GLRenderer.modelM4f().translate((float) x, (float) y, (float) z);
		GLRenderer.setDepthMask(false);
		TessellatorShader t = GLRenderer.getTessellator();
		t.startDrawing(DrawMode.LINE_STRIP);
		t.addVertex(0.5f, 0.5f, 0.5f);
		t.addVertex(-(x1 - x2) + 0.5f, -(y1 - y2) + 0.5f, -(z1 - z2) + 0.5f);
		t.draw();
		GLRenderer.setDepthMask(true);
		GLRenderer.disableState(State.BLEND);
		GLRenderer.popFrame();
	}
}
