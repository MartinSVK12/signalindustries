package sunsetsatellite.signalindustries.entities.render;

import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.util.helper.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.entities.ProjectileSunbeam;

public class SunbeamRenderer extends EntityRenderer<ProjectileSunbeam> {

	@Override
	public void render(@NotNull TessellatorGeneral tessellator, @NonNull ProjectileSunbeam arrow, double x, double y, double z, float yaw, float partialTick) {
		if (arrow.yRotO == 0.0F && arrow.xRotO == 0.0F) {
			return;
		}

		bindTexture("/assets/signalindustries/textures/entity/sunbeam.png");
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate((float) x, (float) y, (float) z);
		GLRenderer.modelM4f().rotate((float) Math.toRadians((arrow.yRotO + (arrow.yRot - arrow.yRotO) * partialTick) - 90F), 0.0F, 1.0F, 0.0F);
		GLRenderer.modelM4f().rotate((float) Math.toRadians(arrow.xRotO + (arrow.xRot - arrow.xRotO) * partialTick), 0.0F, 0.0F, 1.0F);

		float bodyMinU = 0F / 32F;
		float bodyMaxU = 16F / 32F;
		float bodyMinV = (float) (0) / 32F;
		float bodyMaxV = (float) (5) / 32F;
		float tailMinU = 0F / 32F;
		float tailMaxU = 5F / 32F;
		float tailMinV = (float) (5) / 32F;
		float tailMaxV = (float) (10) / 32F;
		float scale = 9F / 160F;

		Lighting.disable();
		float shakeAmount = (float) 0 - partialTick;
		if (shakeAmount > 0.0F) {
			float shakeAngle = -MathHelper.sin(shakeAmount * 3F) * shakeAmount;
			GLRenderer.modelM4f().rotate((float) Math.toRadians(shakeAngle), 0.0F, 0.0F, 1.0F);
		}

		// Set up arrow transformation
		GLRenderer.modelM4f().rotate((float) Math.toRadians(45F), 1.0F, 0.0F, 0.0F);
		GLRenderer.modelM4f().scale(scale, scale, scale);
		GLRenderer.modelM4f().translate(-4F, 0.0F, 0.0F);

		// Draw arrow tail
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(-7D, -2D, -2D, tailMinU, tailMinV);
		tessellator.addVertexWithUV(-7D, -2D, 2D, tailMaxU, tailMinV);
		tessellator.addVertexWithUV(-7D, 2D, 2D, tailMaxU, tailMaxV);
		tessellator.addVertexWithUV(-7D, 2D, -2D, tailMinU, tailMaxV);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(-7D, 2D, -2D, tailMinU, tailMinV);
		tessellator.addVertexWithUV(-7D, 2D, 2D, tailMaxU, tailMinV);
		tessellator.addVertexWithUV(-7D, -2D, 2D, tailMaxU, tailMaxV);
		tessellator.addVertexWithUV(-7D, -2D, -2D, tailMinU, tailMaxV);
		tessellator.draw();

		// Draw arrow body
		for (int i = 0; i < 4; i++) {
			GLRenderer.modelM4f().rotate((float) Math.toRadians(90F), 1.0F, 0.0F, 0.0F);
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(-8D, -2D, 0.0D, bodyMinU, bodyMinV);
			tessellator.addVertexWithUV(8D, -2D, 0.0D, bodyMaxU, bodyMinV);
			tessellator.addVertexWithUV(8D, 2D, 0.0D, bodyMaxU, bodyMaxV);
			tessellator.addVertexWithUV(-8D, 2D, 0.0D, bodyMinU, bodyMaxV);
			tessellator.draw();
		}

		Lighting.enableLight();
		GLRenderer.popFrame();
	}
}
