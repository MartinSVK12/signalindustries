package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityEncapsulator;

import java.util.Objects;

public class RenderEncapsulator extends RenderSI<TileEntityEncapsulator> {
	@Override
	public void doRender(TessellatorGeneral tessellator, TileEntityEncapsulator tile, double x, double y, double z, float partialTick) {
		int tx = tile.tilePos.x;
		int ty = tile.tilePos.y;
		int tz = tile.tilePos.z;
		World world = this.renderDispatcher.textureManager.mc.currentWorld;

		if (!tile.areMarkersValid()) return;

		if (!Objects.equals(world.getLevelData().getWorldName(), "modelviewer")) {

			Direction side = Direction.getDirectionFromSide(tile.getBlockMeta()).getOpposite();
			Vec3f pos = new Vec3f(x, y, z).add(side.getVecF());

			int ox = tile.originMarker.pos.x;
			int oy = tile.originMarker.pos.y;
			int oz = tile.originMarker.pos.z;

			int hx = tile.heightMarker.pos.x;
			int hy = tile.heightMarker.pos.y;
			int hz = tile.heightMarker.pos.z;

			int dx = tile.depthMarker.pos.x;
			int dy = tile.depthMarker.pos.y;
			int dz = tile.depthMarker.pos.z;

			int wx = tile.widthMarker.pos.x;
			int wy = tile.widthMarker.pos.y;
			int wz = tile.widthMarker.pos.z;

			int offsetX = ox - dx;
			int offsetY = oy - hy;
			int offsetZ = oz - wz;

			/*GLRenderer.pushFrame();
			GLRenderer.modelM4f().translate((float) x - (tx) + 0.25f, (float) y + 0.25f, (float) z + (tz) + 0.25f);
			GLRenderer.modelM4f().scale(0.5f, 0.5f, 0.5f);
			GLRenderer.popFrame();*/

			renderLineBetweenTwoPoints(ox, oy, oz, hx, hy, hz, 0, 0, 1, 1, 8, pos.x, pos.y, pos.z);
			renderLineBetweenTwoPoints(ox, oy, oz, hx, hy, hz, 0, 0, 1, 1, 8, pos.x - offsetX, pos.y, pos.z);
			renderLineBetweenTwoPoints(ox, oy, oz, hx, hy, hz, 0, 0, 1, 1, 8, pos.x - offsetX, pos.y, pos.z - offsetZ);
			renderLineBetweenTwoPoints(ox, oy, oz, hx, hy, hz, 0, 0, 1, 1, 8, pos.x, pos.y, pos.z - offsetZ);

			renderLineBetweenTwoPoints(ox, oy, oz, dx, dy, dz, 0, 0, 1, 1, 8, pos.x, pos.y - offsetY, pos.z - offsetZ);
			renderLineBetweenTwoPoints(ox, oy, oz, dx, dy, dz, 0, 0, 1, 1, 8, pos.x, pos.y - offsetY, pos.z);
			renderLineBetweenTwoPoints(ox, oy, oz, wx, wy, wz, 0, 0, 1, 1, 8, pos.x, pos.y - offsetY, pos.z);
			renderLineBetweenTwoPoints(ox, oy, oz, wx, wy, wz, 0, 0, 1, 1, 8, pos.x - offsetX, pos.y - offsetY, pos.z);

			renderLineBetweenTwoPoints(ox, oy, oz, dx, dy, dz, 0, 0, 1, 1, 8, pos.x, pos.y, pos.z - offsetZ);
			renderLineBetweenTwoPoints(ox, oy, oz, dx, dy, dz, 0, 0, 1, 1, 8, pos.x, pos.y, pos.z);
			renderLineBetweenTwoPoints(ox, oy, oz, wx, wy, wz, 0, 0, 1, 1, 8, pos.x, pos.y, pos.z);
			renderLineBetweenTwoPoints(ox, oy, oz, wx, wy, wz, 0, 0, 1, 1, 8, pos.x - offsetX, pos.y, pos.z);
		}
	}
}
