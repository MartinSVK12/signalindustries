package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.render.renderer.GLRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HudIngame.class, remap = false)
public class HudIngameMixin {

	@Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/renderer/GLRenderer;getTessellator()Lnet/minecraft/client/render/tessellator/TessellatorShader;"))
	public void renderGameOverlay(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci) {
		GLRenderer.modelM4f().scale(0.9f, 0.9f, 1f);
	}

}
