package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.FogManager;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.tessellator.RenderBuffer;
import net.minecraft.client.world.WorldClient;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIDimensions;

import java.util.Random;

@Mixin(
        value = RenderGlobal.class,
        remap = false
)
public class RenderGlobalMixin {

    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    @Final
    private TextureManager textureManager;


	@Shadow
	private @NotNull RenderBuffer starBuffer;

	@Shadow
	private WorldClient world;

	@Shadow
	@Final
	private @NotNull RenderBuffer skyBuffer;

	@Inject(
            method = "renderSky",
            at = @At("HEAD")
    )
    public void eternitySky(float partialTick, CallbackInfo ci) {
        if (this.mc.currentWorld != null && this.mc.currentWorld.dimension == SIDimensions.ETERNITY) {
			GLRenderer.pushFrame();
			this.mc.worldRenderer.fogManager.setupFog(FogManager.FOG_MODE_SKY, this.mc.worldRenderer.farPlaneDistance, partialTick, GLRenderer.getFogState());
			GLRenderer.setShader(Shaders.COLOR_WORLD);
			Vector3fc skyColor = new Vector3f(0.6f,0.6f,0.6f);//this.world.getSkyColor(this.mc.activeCamera, partialTick);
			float r = skyColor.x();
			float g = skyColor.y();
			float b = skyColor.z();
			GLRenderer.setDepthMask(false);
			GLRenderer.setColor3f(r, g, b);
			GLRenderer.render(this.skyBuffer);
			GLRenderer.popFrame();

			GLRenderer.pushFrame();
			float starVis = 0.9f;
			this.textureManager.loadTexture("/assets/signalindustries/textures/colormap/stars/default.png").bind();
			GLRenderer.setColor4f(starVis, starVis, starVis, starVis);
			GLRenderer.render(this.starBuffer);
			GLRenderer.popFrame();


        }
    }

}
