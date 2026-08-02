package sunsetsatellite.signalindustries.entities.render;

import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.LightIndexHelper;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.entities.ProjectileFallingMeteor;

public class FallingMeteorRenderer extends EntityRenderer<ProjectileFallingMeteor> {
	@Override
	public void render(@NotNull TessellatorGeneral tessellator, @NonNull ProjectileFallingMeteor entity, double x, double y, double z, float yaw, float partialTick) {
		GLRenderer.pushFrame();
		Lighting.disable();
		GLRenderer.modelM4f().translate((float) x + 0.5f, (float) y - 1.5f, (float) z + 0.5f);
		Block<?> block = Blocks.getBlock(entity.blockID);
		BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(block);
		drawBlock(GLRenderer.getTessellator(),
			model,
			0, 1);
		Lighting.enableLight();
		GLRenderer.popFrame();
	}

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
}
