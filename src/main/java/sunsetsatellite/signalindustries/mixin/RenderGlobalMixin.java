package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.core.entity.fx.EntitySlimeFX;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.EntityDustCloudFX;
import sunsetsatellite.signalindustries.entities.EntityShockwaveFX;

@Mixin(
        value = RenderGlobal.class,
        remap = false
)
public class RenderGlobalMixin {

    @Shadow private Minecraft mc;

    @Shadow private RenderEngine renderEngine;

    @Shadow private int starGLCallList;

    @Shadow private int glSkyList2;

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
            at = @At(value = "INVOKE",target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V",ordinal = 1, shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void renderBloodMoon(float partialTicks, CallbackInfo ci) {
        if(worldObj.currentWeather == SignalIndustries.weatherBloodMoon){
            GL11.glColor4f(1.0f,0.0f,0.0f,1.0f);
        }
    }

    @Inject(
            method = "drawSky",
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
