package sunsetsatellite.signalindustries.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.tessellator.Tessellator;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.useless.DragonFly;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import org.useless.dragonfly.renderer.EntityRenderer;
import sunsetsatellite.signalindustries.entities.EntityShockwave;

public class ShockwaveRenderer extends EntityRenderer<EntityShockwave> {
    @Override
    public void render(@NotNull Tessellator tessellator, @NotNull EntityShockwave entity, double x, double y, double z, float yaw, float partialTick) {
        Minecraft.getMinecraft().textureManager.loadTexture("/assets/signalindustries/textures/block/pulsar.png").bind();
        StaticEntityModel s = DragonFly.loadEntityModel("geometry.signalindustries.shockwave", 0);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTranslatef((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);
        GL11.glColor4f(1, 1, 1, 1f / entity.tickCount);
        GL11.glScalef(entity.tickCount / 10f, entity.tickCount / 10f, -entity.tickCount / 10f);
        s.render(tessellator);
        GL11.glScalef(entity.tickCount / 10f, entity.tickCount / 10f, -entity.tickCount / 10f);
        s.render(tessellator);
        //GL11.glScaled(1f/(Math.pow(entity.tickCount,2)),1f/(Math.pow(entity.tickCount,2)),-1f/(Math.pow(entity.tickCount,2)));

        //GL11.glScaled(1f/(Math.pow(entity.tickCount,2)),1f/(Math.pow(entity.tickCount,2)),-1f/(Math.pow(entity.tickCount,2)));
        //tear.render(tessellator);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
