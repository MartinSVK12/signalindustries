package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.FogManager;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.tessellator.RenderBuffer;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.world.WorldClient;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.SIWeather;

@Debug(export = true)
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

	@Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/renderer/GLRenderer;setColor4f(FFFF)V", ordinal = 1, shift = At.Shift.AFTER))
	public void renderBloodMoon(float partialTicks, CallbackInfo ci) {
		if (world.getCurrentWeather() == SIWeather.weatherBloodMoon) {
			GLRenderer.setColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		}
	}

	@Inject(
		method = "renderSky",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/renderer/GLRenderer;setColor4f(FFFF)V", ordinal = 1, shift = At.Shift.AFTER)
	)
	public void renderMeteorShower(float partialTicks, CallbackInfo ci, @Local(name = "sunAlpha") LocalFloatRef sunAlpha) {
		if (world.getCurrentWeather() == SIWeather.weatherMeteorShower) {
			sunAlpha.set(1f);
			GLRenderer.setColor4f(1, 1, 1, 1.0f);
		}
	}

	@WrapOperation(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/WorldClient;getStarBrightness(F)F"))
	public float renderMeteorShower2(WorldClient instance, float v, Operation<Float> original, @Local(name = "sunAlpha") LocalFloatRef sunAlpha) {
		if (world.getCurrentWeather() == SIWeather.weatherMeteorShower) {
			sunAlpha.set(1f);
			return 1f;
		}
		return original.call(instance, v);
	}

	@WrapOperation(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/tessellator/TessellatorGeneral;draw()V", ordinal = 1))
	public void renderEclipse(TessellatorGeneral instance, Operation<Void> original){
		if (world.getCurrentWeather() == SIWeather.weatherEclipse) {
			textureManager.loadTexture("/assets/signalindustries/textures/misc/solar_eclipse.png").bind();
		}
		original.call(instance);
	}
}
