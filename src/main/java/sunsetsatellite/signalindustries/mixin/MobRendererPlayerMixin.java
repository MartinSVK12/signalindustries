package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.MobRendererPlayer;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;

@Mixin(value = MobRendererPlayer.class, remap = false)
public class MobRendererPlayerMixin {

	@Inject(
		method = "renderAdditional(Lnet/minecraft/client/render/tessellator/TessellatorGeneral;Lnet/minecraft/core/entity/player/Player;F)V",
        at = @At(
			value = "INVOKE",
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/render/entity/MobRendererBipedArmored;renderAdditional(Lnet/minecraft/client/render/tessellator/TessellatorGeneral;Lnet/minecraft/core/entity/Mob;F)V"
		)
	)
	public void renderAdditional(TessellatorGeneral tessellator, Player player, float partialTick, CallbackInfo ci, @Local(name = "activeModel") StaticEntityModel activeModel){
		IPowerSuit powerSuit = ((IPlayerPowerSuit<?>) player).getPowerSuit();
		if(powerSuit != null){
			for (ItemStack content : powerSuit.getArmorPiece(HumanArmorShape.HEAD).contents) {
				if (content != null) {
					GLRenderer.pushFrame();
					((ItemAttachment) content.getItem()).renderWhenAttached(player, powerSuit, activeModel, content);
					GLRenderer.popFrame();
				}
			}
			for (ItemStack content : powerSuit.getArmorPiece(HumanArmorShape.CHEST).contents) {
				if (content != null) {
					GLRenderer.pushFrame();
					((ItemAttachment) content.getItem()).renderWhenAttached(player, powerSuit, activeModel, content);
					GLRenderer.popFrame();
				}
			}
			for (ItemStack content : powerSuit.getArmorPiece(HumanArmorShape.LEGS).contents) {
				if (content != null) {
					GLRenderer.pushFrame();
					((ItemAttachment) content.getItem()).renderWhenAttached(player, powerSuit, activeModel, content);
					GLRenderer.popFrame();
				}
			}
			for (ItemStack content : powerSuit.getArmorPiece(HumanArmorShape.BOOTS).contents) {
				if (content != null) {
					GLRenderer.pushFrame();
					((ItemAttachment) content.getItem()).renderWhenAttached(player, powerSuit, activeModel, content);
					GLRenderer.popFrame();
				}
			}
		}
	}
}
