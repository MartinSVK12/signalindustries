package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SIKeybinds;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicConduit;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicFluidConduit;

import java.util.HashMap;
import java.util.Map;

public class RenderFluidInConduit extends RenderSI<TileEntityFluidPipe>{
	@Override
	public void doRender(TessellatorGeneral tessellator, TileEntityFluidPipe pipe, double x, double y, double z, float partialTick) {
		if (!SIKeybinds.renderFluidInsideConduits.value) {
			return;
		}
		int i = pipe.tilePos.x;
		int j = pipe.tilePos.y;
		int k = pipe.tilePos.z;
		World world = pipe.worldObj;
		Block<?> block = pipe.getBlock();
		if(world == null) return;

		if(pipe.fluid == null) return;
		Block<?> fluidBlock = Blocks.getBlock(pipe.fluid.getFirstId());
		BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(fluidBlock);

		HashMap<Direction, Boolean> states = new HashMap<>();
		for (Direction direction : Direction.values()) {
			boolean show = false;
			Vec3i offset = new Vec3i(i, j, k).add(direction.getVec());
			Block<?> neighbouringBlock = world.getBlockType(offset.tilePos());
			if(pipe.getFluidIOForSide(direction) == Connection.NONE){
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

		double amount = Math.min(1, (double) pipe.sections.get(TileEntityFluidPipe.Orientation.CENTER).getAmount() / pipe.getCapacity());
		float mapped = (float) Catalyst.map(amount, 0.0d, 1.0d, 0.0d, pipe.size);

		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate((float) x + 0.15f, (float) y + 0.15f, (float) z + 0.15f);
		GLRenderer.modelM4f().rotate(0.0f, 0.0F, 1.0F, 0.0F);
		GLRenderer.modelM4f().translate(0.33F, 0.33f, 0.33f);
		GLRenderer.modelM4f().scale(pipe.size, mapped, pipe.size);
		Lighting.disable();
		drawBlock(tessellator, model, 0, 1);
		Lighting.enableLight();
		GLRenderer.popFrame();

		for (Map.Entry<Direction, Boolean> entry : states.entrySet()) {
			Direction dir = entry.getKey();
			boolean show = entry.getValue();
			if(!show) continue;
			int d = TileEntityFluidPipe.getDirOrdinal(dir);
			amount = Math.min(1, (double) pipe.sections.get(TileEntityFluidPipe.Orientation.values()[d]).getAmount() / pipe.getCapacity());
			mapped = (float) Catalyst.map(amount, 0.0d, 1.0d, 0.0d, pipe.size);
			switch (dir) {
				case X_POS -> {
					GLRenderer.pushFrame();
					GLRenderer.modelM4f().translate((float) x + 0.15f, (float) y + 0.15f, (float) z + 0.15f);
					GLRenderer.modelM4f().rotate(0.0f, 0.0F, 1.0F, 0.0F);
					GLRenderer.modelM4f().translate(0.66F, 0.33f, 0.33f);
					GLRenderer.modelM4f().scale(pipe.size, mapped, pipe.size);
					Lighting.disable();
					drawBlock(tessellator, model, 0, 1);
					Lighting.enableLight();
					GLRenderer.popFrame();
				}
				case X_NEG -> {
					GLRenderer.pushFrame();
					GLRenderer.modelM4f().translate((float) x + 0.15f, (float) y + 0.15f, (float) z + 0.15f);
					GLRenderer.modelM4f().rotate(0.0f, 0.0F, 1.0F, 0.0F);
					GLRenderer.modelM4f().translate(0.0f, 0.33f, 0.33f);
					GLRenderer.modelM4f().scale(pipe.size, mapped, pipe.size);
					Lighting.disable();
					drawBlock(tessellator, model, 0, 1);
					Lighting.enableLight();
					GLRenderer.popFrame();
				}
				case Y_POS -> {
					GLRenderer.pushFrame();
					GLRenderer.modelM4f().translate((float) x + 0.15f, (float) y + 0.15f, (float) z + 0.15f);
					GLRenderer.modelM4f().rotate(0.0f, 0.0F, 1.0F, 0.0F);
					GLRenderer.modelM4f().translate(0.33F, 0.66f, 0.33f);
					GLRenderer.modelM4f().scale(mapped, pipe.size, mapped);
					Lighting.disable();
					drawBlock(tessellator, model, 0, 1);
					Lighting.enableLight();
					GLRenderer.popFrame();
				}
				case Y_NEG -> {
					GLRenderer.pushFrame();
					GLRenderer.modelM4f().translate((float) x + 0.15f, (float) y + 0.15f, (float) z + 0.15f);
					GLRenderer.modelM4f().rotate(0.0f, 0.0F, 1.0F, 0.0F);
					GLRenderer.modelM4f().translate(0.33F, 0.0f, 0.33f);
					GLRenderer.modelM4f().scale(mapped, pipe.size, mapped);
					Lighting.disable();
					drawBlock(tessellator, model, 0, 1);
					Lighting.enableLight();
					GLRenderer.popFrame();
				}
				case Z_POS -> {
					GLRenderer.pushFrame();
					GLRenderer.modelM4f().translate((float) x + 0.15f, (float) y + 0.15f, (float) z + 0.15f);
					GLRenderer.modelM4f().rotate(0.0f, 0.0F, 1.0F, 0.0F);
					GLRenderer.modelM4f().translate(0.33F, 0.33f, 0.66f);
					GLRenderer.modelM4f().scale(pipe.size, mapped, pipe.size);
					Lighting.disable();
					drawBlock(tessellator, model, 0, 1);
					Lighting.enableLight();
					GLRenderer.popFrame();
				}
				case Z_NEG -> {
					GLRenderer.pushFrame();
					GLRenderer.modelM4f().translate((float) x + 0.15f, (float) y + 0.15f, (float) z + 0.15f);
					GLRenderer.modelM4f().rotate(0.0f, 0.0F, 1.0F, 0.0F);
					GLRenderer.modelM4f().translate(0.33F, 0.33f, 0.0f);
					GLRenderer.modelM4f().scale(pipe.size, mapped, pipe.size);
					Lighting.disable();
					drawBlock(tessellator, model, 0, 1);
					Lighting.enableLight();
					GLRenderer.popFrame();
				}
			}
		}
	}
}
