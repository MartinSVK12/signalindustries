package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.entity.projectile.EntityArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SignalIndustries;

@Mixin(
        value = EntityArrow.class,
        remap = false
)
public class EntityArrowMixin {

    @Shadow public EntityLiving owner;

    @Shadow protected int arrowDamage;

    @Inject(method = "<init>(Lnet/minecraft/core/world/World;Lnet/minecraft/core/entity/EntityLiving;ZI)V",at = @At("TAIL"))
    protected void init(CallbackInfo ci) {
        if(owner != null) {
            if(owner.world.currentWeather == SignalIndustries.weatherBloodMoon && owner instanceof EntityMonster){
                arrowDamage *= 2;
            }
        }
    }
}
