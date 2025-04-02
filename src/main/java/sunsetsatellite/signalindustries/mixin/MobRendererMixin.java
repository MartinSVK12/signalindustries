package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerHarness;
import sunsetsatellite.signalindustries.items.attachments.ItemSuitColorizer;

@Mixin(value = MobRenderer.class,remap = false)
public abstract class MobRendererMixin<T extends Mob> extends EntityRenderer<T> {

    @Inject(method = "render(Lnet/minecraft/client/render/tessellator/Tessellator;Lnet/minecraft/core/entity/Mob;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/ModelBase;setLivingAnimations(Lnet/minecraft/core/entity/Mob;FFF)V", ordinal = 3, shift = At.Shift.AFTER))
    public void enableAlphaForArmor(Tessellator tessellator, T entity, double x, double y, double z, float yaw, float partialTick, CallbackInfo ci){
        if(entity instanceof Player){
            Player player = (Player)entity;
            if (player.inventory.armorItemInSlot(2) != null && player.inventory.armorItemInSlot(2).getItem() instanceof ItemSignalumPowerHarness) {
                if (player.inventory.armorItemInSlot(2).getData().getBoolean("active_shield")) {
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    return;
                }
            }
            IPowerSuit powerSuit = ((IPlayerPowerSuit<?>)player).getPowerSuit();
            if(powerSuit != null && powerSuit.hasAttachmentClass(ItemSuitColorizer.class)) {
                ItemStack stack = powerSuit.getAttachmentClass(ItemSuitColorizer.class);
                if (stack != null) {
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                }
            }
        }
    }

}
