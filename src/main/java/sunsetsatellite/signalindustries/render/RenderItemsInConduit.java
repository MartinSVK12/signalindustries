package sunsetsatellite.signalindustries.render;

import net.minecraft.client.option.GameSettings;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.MathHelper;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityItemConduit;

import java.util.Random;

public class RenderItemsInConduit extends TileEntityRenderer<TileEntityItemConduit> {

    private final Random random = new Random();

	@Override
	public void doRender(TessellatorGeneral tessellator, TileEntityItemConduit tileEntity, double x, double y, double z, float partialTick) {
		for (TileEntityItemConduit.PipeItem content : tileEntity.getContents()) {
			Direction begin = content.getEntry();
			Direction end = content.getExit();
			Vec3f beginVec = content.getEntry().getVecF();
			Axis beginAxis = begin.getAxis();
			Vec3f endVec = content.getExit().getVecF();
			Axis endAxis = end.getAxis();
			double v = 0;
			boolean positive = (begin == Direction.Z_POS || begin == Direction.Y_POS || begin == Direction.X_POS);
			v = Catalyst.map(content.getTicks() + partialTick, TileEntityItemConduit.TRANSFER_TICKS, 0, 1, -1);
			Vec3f base = new Vec3f(0.5d);
			Vec3f pos = new Vec3f(x, y, z);
			float lerped = 0;
			Vec3f offset = new Vec3f(0);
			switch (beginAxis) {
				case X -> {
					lerped = MathHelper.lerp((float) beginVec.x, (float) base.x, (float) v);
					if (!positive) {
						lerped = (float) Catalyst.map(lerped, -2.5f, 0.5f, -0.5f, 0.5f);
					}
					offset.x += lerped;
					offset.y += base.y;
					offset.z += base.z;
				}
				case Y -> {
					lerped = MathHelper.lerp((float) beginVec.y, (float) base.y, (float) v);
					if (!positive) {
						lerped = (float) Catalyst.map(lerped, -2.5f, 0.5f, -0.5f, 0.5f);
					}
					offset.x += base.x;
					offset.y += lerped;
					offset.z += base.z;
				}
				case Z -> {
					lerped = MathHelper.lerp((float) beginVec.z, (float) base.z, (float) v);
					if (!positive) {
						lerped = (float) Catalyst.map(lerped, -2.5f, 0.5f, -0.5f, 0.5f);
					}
					offset.x += base.x;
					offset.y += base.y;
					offset.z += lerped;
				}
				default -> {
				}
			}
			Vec3f p = pos.copy().add(offset);
			ItemStack stack = content.getStack();
			if(stack == null) return;
			ItemModel model = ItemModelDispatcher.getInstance().getDispatch(stack);
			GLRenderer.pushFrame();
			p.y -= 0.25f;
			byte renderCount = 1;
			if (stack.stackSize > 1) {
				renderCount = 2;
			}
			if (stack.stackSize > 5) {
				renderCount = 3;
			}
			if (stack.stackSize > 20) {
				renderCount = 4;
			}
			//GLRenderer.modelM4f().translate((float) (x + tileEntity.tilePos.x), (float) (y + tileEntity.tilePos.y), (float) (z + tileEntity.tilePos.z));
			GLRenderer.modelM4f().translate((float) p.x, (float) p.y, (float) p.z);
			//GLRenderer.modelM4f().translate((float) (x + tileEntity.tilePos.x + p.x), (float) (y + tileEntity.tilePos.y + p.y), (float) (z + tileEntity.tilePos.z + p.z));
			model.renderItemEntity(tessellator, stack, GameSettings.ITEMS_3D.value, renderCount, 0, 0, tileEntity.worldObj.getSavedLightIndex(tileEntity.tilePos), partialTick);
			GLRenderer.popFrame();
		}
	}
}
