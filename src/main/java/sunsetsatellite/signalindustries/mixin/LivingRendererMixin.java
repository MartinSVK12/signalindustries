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
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.items.attachments.ItemCrownAttachment;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

@Mixin(value = LivingRenderer.class,remap = false)
public abstract class LivingRendererMixin<T extends EntityLiving> extends EntityRenderer<T> {


    @Inject(method = "passSpecialRender",at = @At("HEAD"))
    public void renderCannonOnTarget(T entity, double d, double d1, double d2, CallbackInfo ci){
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
}
