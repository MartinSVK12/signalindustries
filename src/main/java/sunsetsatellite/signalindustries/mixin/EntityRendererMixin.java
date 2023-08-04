package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;



import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SignalIndustries;

@Mixin(
        value = EntityRenderer.class,
        remap = false
)
public class EntityRendererMixin {

    @Shadow private Minecraft mc;

    @Shadow private float fogColorRed;

    @Shadow private float fogColorGreen;

    @Shadow private float fogColorBlue;

    @Inject(
            method = "updateFogColor",
            at = @At("HEAD"),
            cancellable = true
    )
    private void updateFogColor(float f, CallbackInfo ci) {
        if(this.mc.theWorld.dimension == SignalIndustries.dimEternity){
            float f1 = 1.0F / (float)(5 - this.mc.gameSettings.renderDistance.value);
            f1 = 1.0F - (float)Math.pow(f1, 0.25);
            World world = this.mc.theWorld;
            Vec3D vec3d = world.getSkyColor(this.mc.renderViewEntity, f);
            float f2 = (float)vec3d.xCoord;
            float f3 = (float)vec3d.yCoord;
            float f4 = (float)vec3d.zCoord;
            Vec3D vec3d1 = world.getFogColor(f);
            this.fogColorRed = (float)vec3d1.xCoord;
            this.fogColorGreen = (float)vec3d1.yCoord;
            this.fogColorBlue = (float)vec3d1.zCoord;
            this.fogColorRed += (f2 - this.fogColorRed) * f1;
            this.fogColorGreen += (f3 - this.fogColorGreen) * f1;
            this.fogColorBlue += (f4 - this.fogColorBlue) * f1;
            GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
            //GL11.glClearColor(0.7f, 0.7f, 0.7f, 1.0F);
            ci.cancel();
        }
    }


}
