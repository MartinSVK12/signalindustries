package sunsetsatellite.signalindustries.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.tessellator.Tessellator;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.useless.DragonFly;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import org.useless.dragonfly.renderer.EntityRenderer;
import sunsetsatellite.signalindustries.entities.EntityRealityTear;

public class RealityTearRenderer extends EntityRenderer<EntityRealityTear> {
    @Override
    public void render(@NotNull Tessellator tessellator, @NotNull EntityRealityTear entity, double x, double y, double z, float yaw, float partialTick) {
        Minecraft.getMinecraft().textureManager.loadTexture("/assets/signalindustries/entity/reality_tear.png").bind();
        StaticEntityModel tear = DragonFly.loadEntityModel("geometry.signalindustries.reality_tear", 0);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);
        GL11.glScaled(1f / (Math.pow(entity.tickCount, 2)), 1f / (Math.pow(entity.tickCount, 2)), -1f / (Math.pow(entity.tickCount, 2)));
        tear.render(tessellator);
        GL11.glScaled(1f / (Math.pow(entity.tickCount, 2)), 1f / (Math.pow(entity.tickCount, 2)), -1f / (Math.pow(entity.tickCount, 2)));
        tear.render(tessellator);
        GL11.glPopMatrix();
    }
}
