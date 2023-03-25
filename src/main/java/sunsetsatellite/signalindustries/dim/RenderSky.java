package sunsetsatellite.signalindustries.dim;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SignalIndustries;

public class RenderSky {
    public static void renderSky(Minecraft mc, World worldObj, RenderEngine renderEngine, int glSkyList, int glSkyList2, int starGLCallList, float partialTicks) {
        if (mc.theWorld.dimension == SignalIndustries.dimEternity) {
            float celestialAngle = worldObj.getCelestialAngle(partialTicks);
            GL11.glDisable(3553);
            Vec3D vec3d = worldObj.getSkyColor(mc.renderViewEntity, partialTicks);
            float f1 = (float) vec3d.xCoord;
            float f2 = (float) vec3d.yCoord;
            float f3 = (float) vec3d.zCoord;
            float f10;
            if ((Boolean) mc.gameSettings.anaglyph.value) {
                float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
                float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
                f10 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
                f1 = f4;
                f2 = f5;
                f3 = f10;
            }

            GL11.glColor3f(f1, f2, f3);

            GL11.glDepthMask(false);
            GL11.glEnable(2912);
            GL11.glColor3f(f1, f2, f3);
            GL11.glCallList(glSkyList);
            GL11.glDisable(2912);
            GL11.glDisable(3008);
            GL11.glEnable(3042);
            //GL11.glBlendFunc(770, 771);
            RenderHelper.disableStandardItemLighting();
            //float[] af = worldObj.dimension.worldType.worldProvider.calcSunriseSunsetColors(celestialAngle, partialTicks);
            Tessellator tessellator = Tessellator.instance;
            float f12;
            float f14;
            float f16;
            float f18;
            float f20;
            /*if (af != null) {
                GL11.glDisable(3553);
                GL11.glShadeModel(7425);
                GL11.glPushMatrix();
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(celestialAngle <= 0.5F ? 0.0F : 180.0F, 0.0F, 0.0F, 1.0F);
                f10 = af[0];
                f12 = af[1];
                f14 = af[2];
                if ((Boolean) mc.gameSettings.anaglyph.value) {
                    f16 = (f10 * 30.0F + f12 * 59.0F + f14 * 11.0F) / 100.0F;
                    f18 = (f10 * 30.0F + f12 * 70.0F) / 100.0F;
                    f20 = (f10 * 30.0F + f14 * 70.0F) / 100.0F;
                    f10 = f16;
                    f12 = f18;
                    f14 = f20;
                }

                tessellator.startDrawing(6);
                tessellator.setColorRGBA_F(f10, f12, f14, af[3]);
                tessellator.addVertex(0.0, 100.0, 0.0);
                int i = 16;
                tessellator.setColorRGBA_F(af[0], af[1], af[2], 0.0F);

                for (int j = 0; j <= i; ++j) {
                    f20 = (float) j * 3.141593F * 2.0F / (float) i;
                    float f21 = MathHelper.sin(f20);
                    float f22 = MathHelper.cos(f20);
                    tessellator.addVertex((double) (f21 * 120.0F), (double) (f22 * 120.0F), (double) (-f22 * 40.0F * af[3]));
                }

                tessellator.draw();
                GL11.glPopMatrix();
                GL11.glShadeModel(7424);
            }*/

            GL11.glEnable(3553);
            //GL11.glBlendFunc(770, 771);
            GL11.glPushMatrix();
            //f10 = 1.0F - (worldObj.currentWeather != null && worldObj.currentWeather != Weather.weatherClear ? worldObj.weatherIntensity * worldObj.weatherPower * 1.5F : 0.0F);
            f12 = 0.0F;
            f14 = 0.0F;
            f16 = 0.0F;
            //GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0f);
            GL11.glTranslatef(f12, f14, f16);
            GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(celestialAngle * 360.0F, 0.0F, 0.0F, 1.0F);
            f18 = 30.0F;
            GL11.glBindTexture(3553, renderEngine.getTexture("/assets/signalindustries/misc/black_hole.png"));
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double) (-f18), 100.0, (double) (-f18), 0.0, 0.0);
            tessellator.addVertexWithUV((double) f18, 100.0, (double) (-f18), 1.0, 0.0);
            tessellator.addVertexWithUV((double) f18, 100.0, (double) f18, 1.0, 1.0);
            tessellator.addVertexWithUV((double) (-f18), 100.0, (double) f18, 0.0, 1.0);
            tessellator.draw();

            GL11.glDisable(3553);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(3042);
            GL11.glEnable(3008);
            GL11.glEnable(2912);
            GL11.glPopMatrix();
            GL11.glColor3f(f1, f2, f3);
            GL11.glDisable(3553);
            //GL11.glCallList(glSkyList2);
            GL11.glEnable(3553);
            GL11.glDepthMask(true);
        }
    }
}
