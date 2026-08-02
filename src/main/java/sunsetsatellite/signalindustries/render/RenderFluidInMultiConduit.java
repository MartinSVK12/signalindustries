package sunsetsatellite.signalindustries.render;


import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.RenderBlocks;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.LightIndexHelper;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.catalyst.core.util.conduit.IConduitBlock;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicMultiConduit;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityMultiConduit;

import java.util.ArrayList;

public class RenderFluidInMultiConduit extends TileEntityRenderer<TileEntityMultiConduit> {

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
	public void doRender(TessellatorGeneral tessellator, TileEntityMultiConduit tile, double x, double y, double z, float partialTick) {
		Vec3i pos = new Vec3i(tile.tilePos);
		boolean split = false;
		for (Direction dir : Direction.values()) {
			Block<?> connectedBlock = dir.getBlock(tile.worldObj, pos);
			if (connectedBlock != null && connectedBlock.getLogic() instanceof BlockLogicMultiConduit) {
				Direction side = Direction.getDirectionFromSide(tile.worldObj.getBlockData(tile.tilePos));
				if (side != dir && side != dir.getOpposite()) {
					split = true;
					break;
				}
			}
			if (connectedBlock != null && connectedBlock.getLogic() instanceof IConduitBlock) {
				split = true;
				break;
			}
		}

		if (split) return;

		BlockModel<?>[] models = new BlockModel<?>[4];
		Vec3f[][] vecs = new Vec3f[][]{
			//X
			new Vec3f[]{
				new Vec3f(0.5f, 0.7f, 0.7f),
				new Vec3f(0.5f, 0.7f, 0.3f),
				new Vec3f(0.5f, 0.3f, 0.7f),
				new Vec3f(0.5f, 0.3f, 0.3f)
			},
			//Y
			new Vec3f[]{
				new Vec3f(0.7f, 0.5f, 0.7f),
				new Vec3f(0.3f, 0.5f, 0.7f),
				new Vec3f(0.7f, 0.5f, 0.3f),
				new Vec3f(0.3f, 0.5f, 0.3f)
			},
			//Z
			new Vec3f[]{
				new Vec3f(0.7f, 0.7f, 0.5f), //1
				new Vec3f(0.3f, 0.7f, 0.5f), //2
				new Vec3f(0.7f, 0.3f, 0.5f), //3
				new Vec3f(0.3f, 0.3f, 0.5f)  //4
			}

		};

		IConduitBlock[] conduits = tile.conduits;
		for (int i = 0; i < conduits.length; i++) {
			IConduitBlock conduit = conduits[i];
			if (conduit != null) {
				if (conduit.getConduitCapability() == ConduitCapability.SIGNALUM || conduit.getConduitCapability() == ConduitCapability.FLUID) {
					if (tile.fluidContents[i] != null) {
						models[i] = BlockModelDispatcher.getInstance().getDispatch(tile.fluidContents[i].fluid.blocks.get(0));
					} else {
						models[i] = null;
					}
				}
			}
		}

		for (int i = 0; i < models.length; i++) {
			BlockModel<?> model = models[i];
			if (model != null && tile.fluidContents[i] != null) {
				ArrayList<BlockInstance> blockInstances = new ArrayList<>();
				for (FluidStack fluidStack : tile.fluidContents) {
					if (fluidStack != null) {
						blockInstances.add(new BlockInstance(fluidStack.fluid.blocks.get(0), new Vec3i(i), null));
					}
				}
				int amount = tile.fluidContents[i].amount;
				int maxAmount = tile.fluidCapacity[i];
				float ratio = ((float) amount / maxAmount);
				float mappedRatio = (float) Catalyst.map(ratio, 0.0d, 1.0d, 0.0d, 0.3d);
				Axis axis = Side.fromId(tile.worldObj.getBlockData(tile.tilePos)).axis();
				int axisOrd = axis.ordinal();
				GLRenderer.pushFrame();
				Lighting.disable();
				GLRenderer.modelM4f().translate((float) (x + vecs[axisOrd][i].x), (float) (y + vecs[axisOrd][i].y), (float) (z + vecs[axisOrd][i].z));
				switch (axis) {
					case X:
						GLRenderer.modelM4f().scale(0.98f, mappedRatio, 0.3f);
						break;
					case Y:
						GLRenderer.modelM4f().scale(mappedRatio, 0.98f, mappedRatio);
						break;
					case Z:
						GLRenderer.modelM4f().scale(0.3f, mappedRatio, 0.98f);
						break;
				}
				drawBlock(tessellator, model, 0,1);
				Lighting.enableLight();
				GLRenderer.popFrame();
			}
		}
	}
}
