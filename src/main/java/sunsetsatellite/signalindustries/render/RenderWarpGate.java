package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.DrawMode;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Axis;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.multiblocks.RenderMultiblock;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityWarpGate;

public class RenderWarpGate extends RenderMultiblock {

	@Override
	public void doRender(TessellatorGeneral tessellator, TileEntity tileEntity, double x, double y, double z, float g) {
		super.doRender(tessellator, tileEntity, x, y, z, g);
		if(tileEntity instanceof TileEntityWarpGate warpGate){
			if(warpGate.isActive()){
				Direction dir = Direction.getDirectionFromSide(tileEntity.getBlockMeta());
				Vec3f offset = dir.getVecF().multiply(-4);
				Axis axis = dir.shiftAxis().getAxis();

				GLRenderer.pushFrame();
				Lighting.disable();
				GLRenderer.enableState(State.BLEND);
				GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
				GLRenderer.modelM4f().translate((float) (x + offset.x), (float) (y + offset.y), (float) (z + offset.z));
				GLRenderer.setDepthMask(false);
				TextureManager renderEngine = Minecraft.getMinecraft().textureManager;
				renderEngine.bindTexture(renderEngine.loadTexture("/assets/signalindustries/textures/block/warp_gate_portal.png"));

				tessellator.startDrawingQuads();
				if(axis == Axis.X){
					tessellator.addVertexWithUV(-2, -2, 0.5, 0, 0);
					tessellator.addVertexWithUV(3, -2, 0.5, 0, 1);
					tessellator.addVertexWithUV(3, 3, 0.5, 1, 1);
					tessellator.addVertexWithUV(-2, 3, 0.5, 1, 0);

					tessellator.addVertexWithUV(-2, 3, 0.5, 1, 0);
					tessellator.addVertexWithUV(3, 3, 0.5, 1, 1);
					tessellator.addVertexWithUV(3, -2, 0.5, 0, 1);
					tessellator.addVertexWithUV(-2, -2, 0.5, 0, 0);
				} else if(axis == Axis.Z){
					tessellator.addVertexWithUV(0.5, -2, -2, 0, 0);
					tessellator.addVertexWithUV(0.5, -2, 3, 0, 1);
					tessellator.addVertexWithUV(0.5, 3, 3, 1, 1);
					tessellator.addVertexWithUV(0.5, 3, -2, 1, 0);

					tessellator.addVertexWithUV(0.5, 3, -2, 1, 0);
					tessellator.addVertexWithUV(0.5, 3, 3, 1, 1);
					tessellator.addVertexWithUV(0.5, -2, 3, 0, 1);
					tessellator.addVertexWithUV(0.5, -2, -2, 0, 0);
				}
				tessellator.draw();
				GLRenderer.disableState(State.BLEND);
				GLRenderer.setDepthMask(true);
				GLRenderer.popFrame();
			}
		}
	}
}
