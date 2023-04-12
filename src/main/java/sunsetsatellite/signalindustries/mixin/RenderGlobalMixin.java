package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.dim.RenderSky;
import sunsetsatellite.signalindustries.entities.EntityDustCloudFX;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.EntityShockwaveFX;

@Mixin(
        value = RenderGlobal.class,
        remap = false
)
public class RenderGlobalMixin {

    @Shadow private Minecraft mc;

    @Shadow private World worldObj;

    @Shadow private RenderEngine renderEngine;

    @Shadow private int starGLCallList;

    @Shadow private int glSkyList2;

    @Shadow private int glSkyList;

    @Inject(
            method = "spawnParticle",
            at = @At("HEAD")
    )
    public void spawnParticle(String s, double x, double y, double z, double motionX, double motionY, double motionZ, CallbackInfo ci) {
        if (this.mc != null && this.mc.renderViewEntity != null && this.mc.effectRenderer != null) {
            double d6 = this.mc.renderViewEntity.posX - x;
            double d7 = this.mc.renderViewEntity.posY - y;
            double d8 = this.mc.renderViewEntity.posZ - z;
            double d9 = 16.0;
            if (!(d6 * d6 + d7 * d7 + d8 * d8 > d9 * d9)) {
                if (s.equals("crystalbreak")) {
                    this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, x, y, z, SignalIndustries.signalumCrystal));
                }
                if(s.equals("dustcloud")){
                    this.mc.effectRenderer.addEffect(new EntityDustCloudFX(this.worldObj,x,y,z,motionX,motionY,motionZ));
                }
                if(s.equals("pulse_shockwave")){
                    this.mc.effectRenderer.addEffect(new EntityShockwaveFX(this.worldObj,x,y,z,motionX,motionY,motionZ,this.mc.renderViewEntity));
                }
            }
        }
    }

    @Inject(
            method = "renderSky",
            at = @At("HEAD"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void renderEternitySky(float partialTicks, CallbackInfo ci) {
        if (this.mc.theWorld.dimension == SignalIndustries.dimEternity) {
            RenderSky.renderSky(this.mc,this.worldObj,this.renderEngine,glSkyList,glSkyList2,starGLCallList,partialTicks);
            ci.cancel();
        }
    }

    @Inject(
            method = "renderSky",
            at = @At(value = "INVOKE",target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V",ordinal = 1, shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void renderBloodMoon(float partialTicks, CallbackInfo ci) {
        if(worldObj.currentWeather == SignalIndustries.weatherBloodMoon){
            GL11.glColor4f(1.0f,0.0f,0.0f,1.0f);
        }
    }

    @Inject(
            method = "renderSky",
            at = @At(value = "INVOKE",target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V",ordinal = 0, shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void renderSolar(float partialTicks, CallbackInfo ci) {
        if(worldObj.currentWeather == SignalIndustries.weatherEclipse){
            GL11.glDisable(3553);
            GL11.glColor4f(1,1,1,1);
            GL11.glBindTexture(3553, this.renderEngine.getTexture("/assets/signalindustries/misc/solar_eclipse.png"));
            GL11.glEnable(3553);
        } else if (worldObj.currentWeather == SignalIndustries.weatherSolarApocalypse) {
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
}
