package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.helper.LightIndexHelper;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.HologramWorld;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;

import java.util.List;

public class RenderMultiblockInGUI {

	public void render(List<BlockInstance> blocks, float alpha){
		hologram = new HologramWorld(blocks);
		for (BlockInstance block : blocks) {
			Vec3i pos = block.pos;
			GLRenderer.pushFrame();
			Lighting.disable();
			GLRenderer.modelM4f().translate((float) pos.x, (float) pos.y, (float) pos.z);
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(block.block);
			drawBlock(GLRenderer.getTessellator(),
				model,
				block.meta, alpha);
			Lighting.enableLight();
			GLRenderer.popFrame();
		}

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

	protected HologramWorld hologram;

}
