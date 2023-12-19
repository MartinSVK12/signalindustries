package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.fx.EntitySlimeFX;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.Tessellator;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.fx.EntityDustCloudFX;
import sunsetsatellite.signalindustries.entities.fx.EntityShockwaveFX;

@Debug(
        export = true
)
@Mixin(
        value = RenderGlobal.class,
        remap = false
)
public class RenderGlobalMixin {

    @Shadow private Minecraft mc;

    @Shadow private RenderEngine renderEngine;

    @Shadow private int starGLCallList;

    @Shadow private int glSkyList;

    @Shadow private World worldObj;

    @Inject(
            method = "addParticle(Ljava/lang/String;DDDDDD)V",
            at = @At("HEAD")
    )
    public void spawnParticle(String s, double x, double y, double z, double xd, double yd, double zd, CallbackInfo ci) {
        if (this.mc != null && this.mc.thePlayer != null && this.mc.effectRenderer != null) {
            double d6 = this.mc.thePlayer.x - x;
            double d7 = this.mc.thePlayer.y - y;
            double d8 = this.mc.thePlayer.z - z;
            double d9 = 16.0;
            if (!(d6 * d6 + d7 * d7 + d8 * d8 > d9 * d9)) {
                if (s.equals("crystalbreak")) {
                    this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, x, y, z, SignalIndustries.signalumCrystal));
                }
                if(s.equals("dustcloud")){
                    this.mc.effectRenderer.addEffect(new EntityDustCloudFX(this.worldObj,x,y,z,xd,yd,zd));
                }
                if(s.equals("pulse_shockwave")){
                    this.mc.effectRenderer.addEffect(new EntityShockwaveFX(this.worldObj,x,y,z,xd,yd,zd,this.mc.thePlayer));
                }
            }
        }
    }

    /*@Inject(
            method = "drawSky",
            at = @At("HEAD"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void renderEternitySky(float partialTicks, CallbackInfo ci) {
        if (this.mc.theWorld.dimension == SignalIndustries.dimEternity) {
            RenderSky.renderSky(this.mc,this.world,this.renderEngine,glSkyList,glSkyList2,starGLCallList,partialTicks);
            ci.cancel();
        }
    }*/

    @Inject(
            method = "drawSky",
            at = @At(value = "INVOKE",target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V", ordinal = 1, shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void renderBloodMoon(float partialTicks, CallbackInfo ci) {
        if(worldObj.getCurrentWeather() == SignalIndustries.weatherBloodMoon){
            GL11.glColor4f(1.0f,0.0f,0.0f,1.0f);
        }
    }

    @Inject(
            method = "drawSky",
            at = @At(value = "INVOKE",target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V",ordinal = 0, shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void renderSolar(float renderPartialTicks, CallbackInfo ci, float celestialAngle, Vec3d vec3d, float f1, float f2, float f3, Tessellator tessellator, float[] af, float f6, float f9, float f11, float f13, float f15) {
        if(worldObj.getCurrentWeather() == SignalIndustries.weatherEclipse){
            GL11.glDisable(3553);
            GL11.glColor4f(1,1,1,1);
            GL11.glBindTexture(3553, this.renderEngine.getTexture("/assets/signalindustries/misc/solar_eclipse.png"));
            GL11.glEnable(3553);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-f15, 100.0, -f15, 0.0, 0.0);
            tessellator.addVertexWithUV(f15, 100.0, -f15, 1.0, 0.0);
            tessellator.addVertexWithUV(f15, 100.0, f15, 1.0, 1.0);
            tessellator.addVertexWithUV(-f15, 100.0, f15, 0.0, 1.0);
            tessellator.draw();
        } else if (worldObj.getCurrentWeather() == SignalIndustries.weatherSolarApocalypse) {
            GL11.glDisable(3553);
            GL11.glColor4f(1,1,1,1);
            GL11.glBindTexture(3553, this.renderEngine.getTexture("/assets/signalindustries/misc/solar_apocalypse.png"));
            GL11.glEnable(3553);
        }
    }

    @Inject(
            method = "renderClouds",
            at = @At("HEAD"),
            cancellable = true
    )
    public void renderClouds(float f, CallbackInfo ci) {
        if (this.mc.theWorld.dimension == SignalIndustries.dimEternity) {
            ci.cancel();
        }
    }

    @Inject(
            method = "drawSky",
            at = @At("HEAD"),
            cancellable = true
    )
    public void eternitySky(float renderPartialTicks, CallbackInfo ci){
        if(this.mc.theWorld.dimension == SignalIndustries.dimEternity){
            float celestialAngle = this.worldObj.getCelestialAngle(renderPartialTicks);
            GL11.glDisable(3553);
            Vec3d vec3d = this.worldObj.getSkyColor(this.mc.activeCamera, renderPartialTicks);
            float f1 = (float)vec3d.xCoord;
            float f2 = (float)vec3d.yCoord;
            float f3 = (float)vec3d.zCoord;
            GL11.glColor3f(f1, f2, f3);
            Tessellator tessellator = Tessellator.instance;
            GL11.glDepthMask(false);
            GL11.glEnable(2912);
            GL11.glColor3f(f1, f2, f3);
            GL11.glCallList(this.glSkyList);
            GL11.glDisable(2912);
            GL11.glDisable(3008);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            Lighting.enableLight();
            float[] af = this.worldObj.worldType.getSunriseColor(celestialAngle, renderPartialTicks);
            float f6;
            float f9;
            float f11;
            float f20;
            if (af != null) {
                GL11.glDisable(3553);
                GL11.glShadeModel(7425);
                GL11.glPushMatrix();
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(celestialAngle <= 0.5F ? 0.0F : 180.0F, 0.0F, 0.0F, 1.0F);
                f6 = af[0];
                f9 = af[1];
                f11 = af[2];
                tessellator.startDrawing(6);
                tessellator.setColorRGBA_F(f6, f9, f11, af[3]);
                tessellator.addVertex(0.0, 100.0, 0.0);
                int i = 16;
                tessellator.setColorRGBA_F(af[0], af[1], af[2], 0.0F);

                for(int j = 0; j <= i; ++j) {
                    f20 = (float)j * 3.141593F * 2.0F / (float)i;
                    float f21 = MathHelper.sin(f20);
                    float f22 = MathHelper.cos(f20);
                    tessellator.addVertex(f21 * 120.0F, f22 * 120.0F, -f22 * 40.0F * af[3]);
                }

                tessellator.draw();
                GL11.glPopMatrix();
                GL11.glShadeModel(7424);
            }

            GL11.glEnable(3553);
            GL11.glBlendFunc(770, 1);
            GL11.glPushMatrix();
            f6 = 1.0F; //- (this.worldObj.currentWeather != null && this.worldObj.currentWeather != Weather.overworldClear ? this.worldObj.weatherIntensity * this.worldObj.weatherPower * 1.5F : 0.0F);
            f9 = 0.0F;
            f11 = 0.0F;
            float f13 = 0.0F;
            GL11.glTranslatef(f9, f11, f13);
            GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(celestialAngle * 360.0F, 0.0F, 0.0F, 1.0F);
            float f15 = 30.0F;
            //GL11.glBindTexture(3553, this.renderEngine.getTexture("/terrain/sun.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f6);
            /*tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-f15, 100.0, -f15, 0.0, 0.0);
            tessellator.addVertexWithUV(f15, 100.0, -f15, 1.0, 0.0);
            tessellator.addVertexWithUV(f15, 100.0, f15, 1.0, 1.0);
            tessellator.addVertexWithUV(-f15, 100.0, f15, 0.0, 1.0);
            tessellator.draw();*/
            f15 = 20.0F;
            //GL11.glBindTexture(3553, this.renderEngine.getTexture("/terrain/moon.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f6);
            /*tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-f15, -100.0, f15, 1.0, 1.0);
            tessellator.addVertexWithUV(f15, -100.0, f15, 0.0, 1.0);
            tessellator.addVertexWithUV(f15, -100.0, -f15, 0.0, 0.0);
            tessellator.addVertexWithUV(-f15, -100.0, -f15, 1.0, 0.0);
            tessellator.draw();*/
            GL11.glDisable(3553);
            f20 = this.worldObj.getStarBrightness(renderPartialTicks) * f6;
            if (f20 > 0.0F) {
                GL11.glColor4f(f20, f20, f20, f20);
                GL11.glCallList(this.starGLCallList);
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(3042);
            GL11.glEnable(3008);
            GL11.glEnable(2912);
            GL11.glPopMatrix();
            if (this.worldObj.worldType.hasGround()) {
                GL11.glColor3f(f1 * 0.2F + 0.04F, f2 * 0.2F + 0.04F, f3 * 0.6F + 0.1F);
            } else {
                GL11.glColor3f(f1, f2, f3);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(3553);
            ci.cancel();
        }
    }
}
