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
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.LightIndexHelper;
import net.minecraft.core.util.helper.MathHelper;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidContainer;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityPump;

public class RenderPump extends TileEntityRenderer<TileEntityPump> {
	@Override
	public void doRender(TessellatorGeneral tessellator, TileEntityPump tileEntity, double x, double y, double z, float partialTick) {
		Block<?> block = null;
		float amount = 0;
		float maxAmount = 1;
		if(tileEntity.fluidContents[1] != null){
			maxAmount = tileEntity.fluidCapacity[1];
			amount = tileEntity.fluidContents[1].amount;
			block = tileEntity.fluidContents[1].fluid.blocks.get(0);
		}
		float fill = Math.abs(amount / maxAmount - 0.02f);
		fill = MathHelper.clamp(fill,0,1);
		if(block != null) {
			Vec3f pos = new Vec3f(0.51f, 0.505f, 0.51f);
			GLRenderer.pushFrame();
			Lighting.disable();
			GLRenderer.modelM4f().translate((float) x, (float) y, (float) z);
			GLRenderer.modelM4f().scale(0.98f, fill, 0.98f);
			GLRenderer.modelM4f().translate((float) pos.x, (float) pos.y, (float) pos.z);
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(block);
			drawBlock(GLRenderer.getTessellator(),
				model,
				0, 1);
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
}
