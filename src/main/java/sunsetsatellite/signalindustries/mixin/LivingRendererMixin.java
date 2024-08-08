package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingRenderer;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.catalyst.core.util.Vec3f;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.mixins.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.ItemSignalumPrototypeHarness;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.items.attachments.ItemCrownAttachment;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

@Mixin(value = LivingRenderer.class,remap = false)
public abstract class LivingRendererMixin<T extends EntityLiving> extends EntityRenderer<T> {


    @Inject(method = "passSpecialRender",at = @At("HEAD"))
    public void renderCannonOnTarget(T entity, double d, double d1, double d2, CallbackInfo ci){
        if(entity.world == null) return;
        EntityPlayer player = entity.world.getClosestPlayerToEntity(entity, 16);
        if(player != null){
            SignalumPowerSuit powerSuit = ((IPlayerPowerSuit)player).getPowerSuit();
            if(powerSuit != null){
                if (powerSuit.hasAttachment((ItemAttachment) SIItems.annihilationCrown)) {
                    for (SignalumPowerSuit.LaserCannon laserCannon : powerSuit.laserCannons) {
                        if(laserCannon.target == entity){
                            GL11.glPushMatrix();
                            GL11.glTranslated(d,d1,d2);
                            ItemCrownAttachment.renderCannon(laserCannon.position,new Vec3f(),laserCannon.rotationAxis,laserCannon.angle);
                            GL11.glPopMatrix();
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/ModelBase;setLivingAnimations(Lnet/minecraft/core/entity/EntityLiving;FFF)V", ordinal = 3, shift = At.Shift.AFTER))
    public void enableAlphaForArmor(T entity, double x, double y, double z, float yaw, float partialTick, CallbackInfo ci){
        if(entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)entity;
            if (player.inventory.armorItemInSlot(2) != null && player.inventory.armorItemInSlot(2).getItem() instanceof ItemSignalumPrototypeHarness) {
                if (player.inventory.armorItemInSlot(2).getData().getBoolean("active_shield")) {
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                }
            }
        }
    }
}
