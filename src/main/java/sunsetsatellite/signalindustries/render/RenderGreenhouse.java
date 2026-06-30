package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.HologramWorld;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.multiblocks.RenderMultiblock;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityGreenhouse;

import java.util.ArrayList;

public class RenderGreenhouse extends RenderMultiblock {

	@Override
	public void doRender(TessellatorGeneral tessellator, TileEntity tileEntity, double d, double e, double f, float g) {
		TileEntityGreenhouse greenhouse = (TileEntityGreenhouse) tileEntity;
		int i = tileEntity.tilePos.x;
		int j = tileEntity.tilePos.y;
		int k = tileEntity.tilePos.z;
		Direction dir = Direction.getDirectionFromSide(tileEntity.getBlockMeta());

		if(!greenhouse.getMultiblock().isValid()) {
			super.doRender(tessellator, tileEntity, d, e, f, g);
			return;
		}

		Vec3i middle = greenhouse.getPosition().add(dir.getOpposite().getVec().multiply(3));
		Vec3i offset = middle.copy().add(new Vec3i(2, 0, 2));

		ArrayList<BlockInstance> blocks = new ArrayList<>();
		for (int v = 0; v < 5; v++) {
			for (int w = 0; w < 5; w++) {
				if (offset.x - v == middle.x && offset.z - w == middle.z) continue;
				BlockInstance block = new BlockInstance(Blocks.CROPS_WHEAT, new Vec3i(offset.x - v, offset.y, offset.z - w), greenhouse.getProgressScaled(7), greenhouse);
				blocks.add(block);
			}
		}

		hologram = new HologramWorld(blocks);
		for (BlockInstance block : blocks) {
			GLRenderer.pushFrame();
			GLRenderer.modelM4f().translate((float) d + (block.pos.x - i) + 0.5f, (float) e + (block.pos.y - j) + 0.5f, (float) f + (block.pos.z - k) + 0.5f);
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(block.block);
			drawBlock(GLRenderer.getTessellator(),
				model,
				block.meta, 1);
			GLRenderer.popFrame();
			GLRenderer.setColor4f(1,1,1,1);
		}


		super.doRender(tessellator, tileEntity, d, e, f, g);
	}
}
