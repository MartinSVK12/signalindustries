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
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.LightIndexHelper;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidContainer;
import sunsetsatellite.signalindustries.SIKeybinds;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicConduit;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicFluidConduit;

import java.util.HashMap;

public class RenderFluidInConduit extends TileEntityRenderer<TileEntity> {
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
	public void doRender(TessellatorGeneral tessellator, TileEntity tileEntity, double x, double y, double z, float partialTick) {
		if (!SIKeybinds.renderFluidInsideConduits.value) {
			return;
		}

		int i = tileEntity.tilePos.x;
		int j = tileEntity.tilePos.y;
		int k = tileEntity.tilePos.z;
		World world = tileEntity.worldObj;
		Block<?> block = tileEntity.getBlock();

		if(world == null) return;

		float fluidAmount = 0;
		float fluidMaxAmount = 1;
		int fluidId = -1;

		TileEntityFluidContainer fluidContainer = (TileEntityFluidContainer) tileEntity;
		if (fluidContainer.fluidContents[0] != null) {
			if (fluidContainer.fluidContents[0].fluid != null) {
				fluidMaxAmount = fluidContainer.getFluidCapacityForSlot(0);
				fluidAmount = fluidContainer.fluidContents[0].amount;
				fluidId = fluidContainer.fluidContents[0].fluid.getFirstId();
			}
		}

		BlockModel<?> model = null;
		if (fluidId != -1) {
			Block<?> fluidBlock = Blocks.getBlock(fluidId);
			model = BlockModelDispatcher.getInstance().getDispatch(fluidBlock);
		}

		if (model == null) return;

		fluidAmount = Math.min(fluidAmount, fluidMaxAmount);

		HashMap<Direction, Boolean> states = new HashMap<>();
		for (Direction direction : Direction.values()) {
			boolean show = false;
			Vec3i offset = new Vec3i(i, j, k).add(direction.getVec());
			Block<?> neighbouringBlock = world.getBlockType(offset.tilePos());
			if(fluidContainer.getFluidIOForSide(direction) == Connection.NONE){
				states.put(direction, show);
				continue;
			}
			if (block.getLogic().getClass().isAssignableFrom(neighbouringBlock.getLogic().getClass())) {
				show = true;
			} else if (!(neighbouringBlock.getLogic() instanceof BlockLogicConduit || neighbouringBlock.getLogic() instanceof BlockLogicFluidConduit)) {
				if (neighbouringBlock.isEntityTile) {
					TileEntity neighbouringTile = world.getTileEntity(offset.tilePos());
					if (neighbouringTile instanceof IFluidInventory) {
						show = true;
					}
				} else if (neighbouringBlock.hasTag(SignalIndustries.SIGNALUM_CONDUITS_CONNECT) || neighbouringBlock.hasTag(SignalIndustries.FLUID_CONDUITS_CONNECT)) {
					show = true;
				}
			}
			states.put(direction, show);
		}

		float amount = (fluidAmount / fluidMaxAmount);
		float mapped = (float) Catalyst.map(amount, 0.0d, 1.0d, 0.0d, 0.3d);

		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate((float) x + 0.15f, (float) y + 0.15f, (float) z + 0.15f);
		GLRenderer.modelM4f().rotate(0.0f, 0.0F, 1.0F, 0.0F);
		GLRenderer.modelM4f().translate(0.33F, 0.33f, 0.33f);
		if (!(states.get(Direction.Y_NEG) && states.get(Direction.Y_POS))) {
			GLRenderer.modelM4f().scale(0.3f, mapped, 0.3f);
		} else {
			GLRenderer.modelM4f().scale(mapped, 0.3f, mapped);
		}

		Lighting.disable();
		drawBlock(tessellator, model, 0, 1);
		Lighting.enableLight();
		GLRenderer.popFrame();

		if (states.get(Direction.getFromName("EAST"))) {
			GLRenderer.pushFrame();
			GLRenderer.modelM4f().translate((float) x + 0.15f, (float) y + 0.15f, (float) z + 0.15f);
			GLRenderer.modelM4f().rotate(0.0f, 0.0F, 1.0F, 0.0F);
			GLRenderer.modelM4f().translate(0.66F, 0.33f, 0.33f);
			GLRenderer.modelM4f().scale(0.3f, mapped, 0.3f);
			Lighting.disable();
			drawBlock(tessellator, model, 0, 1);
			Lighting.enableLight();
			GLRenderer.popFrame();
		}
		if (states.get(Direction.getFromName("WEST"))) {
			GLRenderer.pushFrame();
			GLRenderer.modelM4f().translate((float) x + 0.15f, (float) y + 0.15f, (float) z + 0.15f);
			GLRenderer.modelM4f().rotate(0.0f, 0.0F, 1.0F, 0.0F);
			GLRenderer.modelM4f().translate(0.0f, 0.33f, 0.33f);
			GLRenderer.modelM4f().scale(0.3f, mapped, 0.3f);
			Lighting.disable();
			drawBlock(tessellator, model, 0, 1);
			Lighting.enableLight();
			GLRenderer.popFrame();
		}
		if (states.get(Direction.getFromName("UP"))) {
			GLRenderer.pushFrame();
			GLRenderer.modelM4f().translate((float) x + 0.15f, (float) y + 0.15f, (float) z + 0.15f);
			GLRenderer.modelM4f().rotate(0.0f, 0.0F, 1.0F, 0.0F);
			GLRenderer.modelM4f().translate(0.33F, 0.66f, 0.33f);
			GLRenderer.modelM4f().scale(mapped, 0.3f, mapped);
			Lighting.disable();
			drawBlock(tessellator, model, 0, 1);
			Lighting.enableLight();
			GLRenderer.popFrame();
		}
		if (states.get(Direction.getFromName("DOWN"))) {
			GLRenderer.pushFrame();
			GLRenderer.modelM4f().translate((float) x + 0.15f, (float) y + 0.15f, (float) z + 0.15f);
			GLRenderer.modelM4f().rotate(0.0f, 0.0F, 1.0F, 0.0F);
			GLRenderer.modelM4f().translate(0.33F, 0.0f, 0.33f);
			GLRenderer.modelM4f().scale(mapped, 0.3f, mapped);
			Lighting.disable();
			drawBlock(tessellator, model, 0, 1);
			Lighting.enableLight();
			GLRenderer.popFrame();
		}
		if (states.get(Direction.getFromName("SOUTH"))) {
			GLRenderer.pushFrame();
			GLRenderer.modelM4f().translate((float) x + 0.15f, (float) y + 0.15f, (float) z + 0.15f);
			GLRenderer.modelM4f().rotate(0.0f, 0.0F, 1.0F, 0.0F);
			GLRenderer.modelM4f().translate(0.33F, 0.33f, 0.66f);
			GLRenderer.modelM4f().scale(0.3f, mapped, 0.3f);
			Lighting.disable();
			drawBlock(tessellator, model, 0, 1);
			Lighting.enableLight();
			GLRenderer.popFrame();
		}
		if (states.get(Direction.getFromName("NORTH"))) {
			GLRenderer.pushFrame();
			GLRenderer.modelM4f().translate((float) x + 0.15f, (float) y + 0.15f, (float) z + 0.15f);
			GLRenderer.modelM4f().rotate(0.0f, 0.0F, 1.0F, 0.0F);
			GLRenderer.modelM4f().translate(0.33F, 0.33f, 0.0f);
			GLRenderer.modelM4f().scale(0.3f, mapped, 0.3f);
			Lighting.disable();
			drawBlock(tessellator, model, 0, 1);
			Lighting.enableLight();
			GLRenderer.popFrame();
		}
	}
}
