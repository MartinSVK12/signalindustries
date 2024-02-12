package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sunsetsatellite.signalindustries.entities.EntitySunbeam;

public class SunbeamRenderer extends EntityRenderer<EntitySunbeam>
{
    public void renderSunbeam(EntitySunbeam arrow, double x, double y, double z,
                              float yaw, float partialTick)
    {
        if(arrow.yRotO == 0.0F && arrow.xRotO == 0.0F)
        {
            return;
        }

        loadTexture("/assets/signalindustries/entity/sunbeam.png");
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glRotatef((arrow.yRotO + (arrow.yRot - arrow.yRotO) * partialTick) - 90F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(arrow.xRotO + (arrow.xRot - arrow.xRotO) * partialTick, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.instance;

        int arrowType = 0;

        float bodyMinU = 0F / 32F;
        float bodyMaxU = 16F / 32F;
        float bodyMinV = (float)(0) / 32F;
        float bodyMaxV = (float)(5) / 32F;
        float tailMinU = 0F / 32F;
        float tailMaxU = 5F / 32F;
        float tailMinV = (float)(5) / 32F;
        float tailMaxV = (float)(10) / 32F;
        float scale = 9F / 160F;

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        float shakeAmount = (float)arrow.arrowShake - partialTick;
        if(shakeAmount > 0.0F)
        {
            float shakeAngle = -MathHelper.sin(shakeAmount * 3F) * shakeAmount;
            GL11.glRotatef(shakeAngle, 0.0F, 0.0F, 1.0F);
        }

        // Set up arrow transformation
        GL11.glRotatef(45F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslatef(-4F, 0.0F, 0.0F);
        GL11.glNormal3f(scale, 0.0F, 0.0F);

        // Draw arrow tail
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7D, -2D, -2D, tailMinU, tailMinV);
        tessellator.addVertexWithUV(-7D, -2D, 2D, tailMaxU, tailMinV);
        tessellator.addVertexWithUV(-7D, 2D, 2D, tailMaxU, tailMaxV);
        tessellator.addVertexWithUV(-7D, 2D, -2D, tailMinU, tailMaxV);
        tessellator.draw();
        GL11.glNormal3f(-scale, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7D, 2D, -2D, tailMinU, tailMinV);
        tessellator.addVertexWithUV(-7D, 2D, 2D, tailMaxU, tailMinV);
        tessellator.addVertexWithUV(-7D, -2D, 2D, tailMaxU, tailMaxV);
        tessellator.addVertexWithUV(-7D, -2D, -2D, tailMinU, tailMaxV);
        tessellator.draw();

        // Draw arrow body
        for(int i = 0; i < 4; i++)
        {
            GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, scale);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-8D, -2D, 0.0D, bodyMinU, bodyMinV);
            tessellator.addVertexWithUV(8D, -2D, 0.0D, bodyMaxU, bodyMinV);
            tessellator.addVertexWithUV(8D, 2D, 0.0D, bodyMaxU, bodyMaxV);
            tessellator.addVertexWithUV(-8D, 2D, 0.0D, bodyMinU, bodyMaxV);
            tessellator.draw();
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(EntitySunbeam entity, double x, double y, double z,
                         float yaw, float partialTick)
    {
        renderSunbeam(entity, x, y, z, yaw, partialTick);
    }
}