package sunsetsatellite.signalindustries.mixin.client;

import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.font.FontRenderer;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.render.RenderStorageContainer;

@Mixin(value = ItemModelStandard.class,remap = false)
public class ItemModelStandardMixin {

	@Inject(method = "renderItemOverlayIntoGUI", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/renderer/GLRenderer;disableState(Lnet/minecraft/client/render/renderer/State;)V", shift = At.Shift.AFTER))
	public void renderItemOverlayIntoGUI(TessellatorGeneral tessellator, FontRenderer fontRenderer, TextureManager textureManager, ItemStack itemstack, int x, int y, String override, float alpha, CallbackInfo ci){
		if(RenderStorageContainer.rendering){
			GLRenderer.enableState(State.DEPTH_TEST);
		}
	}

}
