package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.renderer.*;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityAutoMiner;

import java.util.Objects;

public class RenderAutoMiner extends RenderSI<TileEntityAutoMiner> {

	@Override
	public void doRender(TessellatorGeneral tessellator, TileEntityAutoMiner tile, double x, double y, double z, float partialTick) {
		int tx = tile.tilePos.x;
		int ty = tile.tilePos.y;
		int tz = tile.tilePos.z;
		int cx = tile.current.x;
		int cy = tile.current.y;
		int cz = tile.current.z;
		World world = tile.worldObj;

		if (!Objects.equals(world.getLevelData().getWorldName(), "modelviewer")) {
			GLRenderer.pushFrame();
			GLRenderer.modelM4f().translate((float) x - (tx - cx) + 0.25f, (float) y + 4.25f, (float) z + (cz - tz) + 0.25f);
			GLRenderer.modelM4f().scale(0.5f, 0.5f, 0.5f);
			GLRenderer.popFrame();

			if (cy - (ty + 4) < 0) {
				GLRenderer.pushFrame();
				GLRenderer.modelM4f().translate((float) x - (tx - cx) + 0.5f, (float) y - (ty - cy) + 1.2f, (float) z + (cz - tz) + 0.5f);
				GLRenderer.modelM4f().scale(0.50f, 1f, 0.50f);
				drawBlock(tessellator, BlockModelDispatcher.getInstance().getDispatch(Blocks.BASALT), 0, 1);
				GLRenderer.popFrame();
			}

			if (cy - (ty + 4) < 0) {
				GLRenderer.pushFrame();
				GLRenderer.modelM4f().translate((float) x - (tx - cx) + 0.5f, (float) y - (ty - cy) + 0.3f, (float) z + (cz - tz) + 0.5f);
				GLRenderer.modelM4f().scale(0.25f, 0.75f, 0.25f);
				if (tile.hasSilkTouch()) {
					drawBlock(tessellator, BlockModelDispatcher.getInstance().getDispatch(Blocks.BLOCK_GOLD), 0, 1);
				} else {
					drawBlock(tessellator, BlockModelDispatcher.getInstance().getDispatch(Blocks.BLOCK_DIAMOND), 0, 1);
				}
				GLRenderer.popFrame();
			}

			//square above
			renderLineBetweenTwoPoints(tx, ty, tz, tx - (tile.size.x - 1), ty, tz, 1, 0.5f, 0, 1, 8, x, y + 4, z);
			renderLineBetweenTwoPoints(tx, ty, tz, tx, ty, tz + (tile.size.y - 1), 1, 0.5f, 0, 1, 8, x, y + 4, z);
			renderLineBetweenTwoPoints(tx, ty, tz, tx - (tile.size.x - 1), ty, tz, 1, 0.5f, 0, 1, 8, x, y + 4, z + (tile.size.y - 1));
			renderLineBetweenTwoPoints(tx, ty, tz, tx, ty, tz + (tile.size.y - 1), 1, 0.5f, 0, 1, 8, x - (tile.size.x - 1), y + 4, z);
			//square
			renderLineBetweenTwoPoints(tx, ty, tz, tx - (tile.size.x - 1), ty, tz, 1, 0.5f, 0, 1, 8, x, y, z);
			renderLineBetweenTwoPoints(tx, ty, tz, tx, ty, tz + (tile.size.y - 1), 1, 0.5f, 0, 1, 8, x, y, z);
			renderLineBetweenTwoPoints(tx, ty, tz, tx - (tile.size.x - 1), ty, tz, 1, 0.5f, 0, 1, 8, x, y, z + (tile.size.y - 1));
			renderLineBetweenTwoPoints(tx, ty, tz, tx, ty, tz + (tile.size.y - 1), 1, 0.5f, 0, 1, 8, x - (tile.size.x - 1), y, z);
			//down
			renderLineBetweenTwoPoints(tx, ty, tz, tx, ty - 4, tz, 1, 0.5f, 0, 1, 8, x, y + 4, z);
			renderLineBetweenTwoPoints(tx, ty, tz, tx, ty - 4, tz, 1, 0.5f, 0, 1, 8, x - (tile.size.x - 1), y + 4, z);
			renderLineBetweenTwoPoints(tx, ty, tz, tx, ty - 4, tz, 1, 0.5f, 0, 1, 8, x, y + 4, z + (tile.size.y - 1));
			renderLineBetweenTwoPoints(tx, ty, tz, tx, ty - 4, tz, 1, 0.5f, 0, 1, 8, x - (tile.size.x - 1), y + 4, z + (tile.size.y - 1));
			//down 2
			renderLineBetweenTwoPoints(tx, ty, tz, tx, 0, tz, 1, 1, 1, 1, 2, x, y, z);
			renderLineBetweenTwoPoints(tx, ty, tz, tx, 0, tz, 1, 1, 1, 1, 2, x - (tile.size.x - 1), y, z);
			renderLineBetweenTwoPoints(tx, ty, tz, tx, 0, tz, 1, 1, 1, 1, 2, x, y, z + (tile.size.y - 1));
			renderLineBetweenTwoPoints(tx, ty, tz, tx, 0, tz, 1, 1, 1, 1, 2, x - (tile.size.x - 1), y, z + (tile.size.y - 1));
			//square down
			renderLineBetweenTwoPoints(tx, ty, tz, tx - (tile.size.x - 1), ty, tz, 1, 1, 1, 1, 2, x, y - ty, z);
			renderLineBetweenTwoPoints(tx, ty, tz, tx, ty, tz + (tile.size.y - 1), 1, 1, 1, 1, 2, x, y - ty, z);
			renderLineBetweenTwoPoints(tx, ty, tz, tx - (tile.size.x - 1), ty, tz, 1, 1, 1, 1, 2, x, y - ty, z + (tile.size.y - 1));
			renderLineBetweenTwoPoints(tx, ty, tz, tx, ty, tz + (tile.size.y - 1), 1, 1, 1, 1, 2, x - (tile.size.x - 1), y - ty, z);

			//current lines
			renderLineBetweenTwoPoints(tx, ty, tz, cx, ty, tz, 1, 0, 0, 1, 8, x, y + 4, z + (cz - tz));
			renderLineBetweenTwoPoints(tx, ty, cz, tx, ty, tz, 0, 0, 1, 1, 8, x - (tx - cx), y + 4, z + (cz - tz));
			renderLineBetweenTwoPoints(tx, ty, tz, tx, cy - 5, tz, 0, 1, 0, 1, 8, x - (tx - cx), y + 4, z + (cz - tz));
		}
	}


}
