package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.multiblocks.RenderMultiblock;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.items.tools.ItemFuelCell;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntitySignalumReactor;

import java.util.HashMap;
import java.util.Map;

public class RenderReactor extends RenderMultiblock {

	public final Vec3f renderPos = new Vec3f();
	public final Vec3i tilePos = new Vec3i();

	public static final Map<Direction,Vec3i[]> cellPositions = new HashMap<>();

	static {
		cellPositions.put(Direction.X_POS, new Vec3i[]{
			new Vec3i(-3,0,0),
			new Vec3i(-3,1,0),
			new Vec3i(-3,-1,0),
			new Vec3i(-1,0,1),
			new Vec3i(-1,1,1),
			new Vec3i(-1,-1,1),
			new Vec3i(-1,0,-1),
			new Vec3i(-1,1,-1),
			new Vec3i(-1,-1,-1),
		});
		cellPositions.put(Direction.X_NEG, new Vec3i[]{
			new Vec3i(3,0,0),
			new Vec3i(3,1,0),
			new Vec3i(3,-1,0),
			new Vec3i(1,0,-1),
			new Vec3i(1,1,-1),
			new Vec3i(1,-1,-1),
			new Vec3i(1,0,1),
			new Vec3i(1,1,1),
			new Vec3i(1,-1,1),
		});
		cellPositions.put(Direction.Z_NEG, new Vec3i[]{
			new Vec3i(0,0,3),
			new Vec3i(0,1,3),
			new Vec3i(0,-1,3),
			new Vec3i(1,0,1),
			new Vec3i(1,1,1),
			new Vec3i(1,-1,1),
			new Vec3i(-1,0,1),
			new Vec3i(-1,1,1),
			new Vec3i(-1,-1,1),
		});
		cellPositions.put(Direction.Z_POS, new Vec3i[]{
			new Vec3i(0,0,-3),
			new Vec3i(0,1,-3),
			new Vec3i(0,-1,-3),
			new Vec3i(-1,0,-1),
			new Vec3i(-1,1,-1),
			new Vec3i(-1,-1,-1),
			new Vec3i(1,0,-1),
			new Vec3i(1,1,-1),
			new Vec3i(1,-1,-1),
		});
	}

	@Override
	public void doRender(TessellatorGeneral tessellator, TileEntity tileEntity, double d, double e, double f, float g) {
		super.doRender(tessellator, tileEntity, d, e, f, g);
		if(tileEntity instanceof TileEntitySignalumReactor reactor && reactor.input != null && reactor.output != null){
			renderPos.set(d,e,f);
			tilePos.set(tileEntity.tilePos);
			Direction dir = Direction.getDirectionFromSide(tileEntity.getBlockMeta());
			ItemStack[] input = reactor.input.itemContents;
			ItemStack[] output = reactor.output.itemContents;
			for (int i = 0; i < input.length; i++) {
				ItemStack stack = input[i];
				if (stack == null) {
					if(output[i] != null){
						stack = output[i];
					} else {
						continue;
					}
				}
				if (!(stack.getItem() instanceof ItemFuelCell)) continue;
				if(cellPositions.get(dir) == null) continue;
				drawFuelCell(cellPositions.get(dir)[i], stack, tessellator);
			}
		}
	}

	public void drawFuelCell(@NotNull Vec3i pos, @NotNull ItemStack fuelCell, TessellatorGeneral t) {
		BlockModel<?> fuelCellModel = BlockModelDispatcher.getInstance().getDispatch(SIBlocks.fuelCellBlock);
		BlockModel<?> energyModel = BlockModelDispatcher.getInstance().getDispatch(SIBlocks.energyFlowing);
		BlockModel<?> burntModel = BlockModelDispatcher.getInstance().getDispatch(SIBlocks.burntSignalumFlowing);
		ItemFuelCell item = (ItemFuelCell) fuelCell.getItem();
		int meta = 0;
		if(item.getRemainingCapacity(fuelCell) < item.getCapacity(fuelCell)){
			meta = 1;
		}
		if(item.getRemainingCapacity(fuelCell) == 0 && item.getFluidAmount(fuelCell) == 0){
			meta = 2;
		}
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate((float) renderPos.x + 0.5f + pos.x, (float) renderPos.y + 0.5f + pos.y, (float) renderPos.z + 0.5f + pos.z);
		drawBlock(t, fuelCellModel, meta, 1);
		GLRenderer.popFrame();
		float fuelRatio = item.getFluidAmount(fuelCell) / (float) item.getCapacity(fuelCell);
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate((float) renderPos.x + 0.5f + pos.x, (float) renderPos.y + 0.51f + pos.y, (float) renderPos.z + 0.5f + pos.z);
		GLRenderer.modelM4f().translate(0, ((0.96f-fuelRatio) - 1.0f) / 2.0f, 0);
		GLRenderer.modelM4f().scale(0.98f, (1-fuelRatio), 0.98f);
		drawBlock(t, burntModel, 1, 1);
		GLRenderer.popFrame();
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate((float) renderPos.x + 0.5f + pos.x, (float) renderPos.y + 0.49f + pos.y, (float) renderPos.z + 0.5f + pos.z);
		GLRenderer.modelM4f().translate(0, (1.0F - fuelRatio) / 2.0f, 0);
		GLRenderer.modelM4f().scale(0.98f, fuelRatio, 0.98f);
		drawBlock(t, energyModel, 1, 1);
		GLRenderer.popFrame();
	}
}
