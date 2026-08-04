package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.tileentity.TileEntityRenderer;

import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import sunsetsatellite.signalindustries.items.ItemWarpOrb;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityPulsar;

public class RenderPulsar extends TileEntityRenderer<TileEntityPulsar> {
	@Override
	public void doRender(TessellatorGeneral tessellatorGeneral, TileEntityPulsar tile, double x, double y, double z, float partialTick) {
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate((float) x + 0.5f, (float) y + 0.25f, (float) z + 0.5f);
		if (tile.isBurning()) {
			GLRenderer.modelM4f().rotate((float) Math.toRadians(tile.orbRotation * 20 + partialTick), 0, 1, 0);
		} else {
			GLRenderer.modelM4f().translate(0f, -1f, 0f);
		}
		GLRenderer.enableState(State.BLEND);
		GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
		Minecraft.getMinecraft().textureManager.loadTexture("/assets/signalindustries/textures/block/pulsar.png").bind();
		if (tile.getItem(0) != null && tile.getItem(0).getItem() instanceof ItemWarpOrb) {
			Minecraft.getMinecraft().textureManager.loadTexture("/assets/signalindustries/textures/block/pulsar_warp.png").bind();
		}
		StaticEntityModel item = EntityGeometryMojangData.Cache.getModel("geometry.signalindustries.pulsar_item", 0);
		StaticEntityModel innerCore = EntityGeometryMojangData.Cache.getModel("geometry.signalindustries.pulsar_inner_core", 0);
		StaticEntityModel outerCore = EntityGeometryMojangData.Cache.getModel("geometry.signalindustries.pulsar_outer_core", 0);
		GLRenderer.modelM4f().scale(0.0625f, 0.0625f, -0.0625f);
		if (tile.fuelBurnTicks <= 0) {
			item.render();
		}
		if (tile.progressTicks > tile.progressMaxTicks / 2) {
			innerCore.render();
		}
		if (tile.progressTicks >= tile.progressMaxTicks) {
			outerCore.render();
		}
		GLRenderer.disableState(State.BLEND);
		GLRenderer.popFrame();
	}
}
