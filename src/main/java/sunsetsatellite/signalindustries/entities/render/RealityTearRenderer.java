package sunsetsatellite.signalindustries.entities.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import sunsetsatellite.signalindustries.entities.EntityRealityTear;

public class RealityTearRenderer extends EntityRenderer<EntityRealityTear> {
	@Override
	public void render(@NotNull TessellatorGeneral tessellator, @NonNull EntityRealityTear entity, double x, double y, double z, float yaw, float partialTick) {
		Minecraft.getMinecraft().textureManager.loadTexture("/assets/signalindustries/entity/reality_tear.png").bind();
		StaticEntityModel tear = EntityGeometryMojangData.Cache.getModel("geometry.signalindustries.reality_tear", 0);
		GLRenderer.pushFrame();
		GLRenderer.enableState(State.BLEND);
		GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
		GLRenderer.modelM4f().translate((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);
		GLRenderer.setColor4f(1, 1, 1, 1f / entity.tickCount);
		GLRenderer.modelM4f().scale(1f / (float) Math.pow(entity.tickCount, 2), 1f / (float) Math.pow(entity.tickCount, 2), -1f / (float) Math.pow(entity.tickCount, 2));
		tear.render();
		GLRenderer.modelM4f().scale(1f / (float) Math.pow(entity.tickCount, 2), 1f / (float) Math.pow(entity.tickCount, 2), -1f / (float) Math.pow(entity.tickCount, 2));
		tear.render();
		GLRenderer.disableState(State.BLEND);
		GLRenderer.popFrame();
	}
}
