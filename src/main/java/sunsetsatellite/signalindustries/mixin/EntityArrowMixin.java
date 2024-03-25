package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.entity.projectile.EntityArrow;
import net.minecraft.core.entity.projectile.EntityProjectile;
import net.minecraft.core.world.World;
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
public class EntityArrowMixin extends EntityProjectile {

    public EntityArrowMixin(World world) {
        super(world);
    }

    @Inject(method = "<init>(Lnet/minecraft/core/world/World;Lnet/minecraft/core/entity/EntityLiving;ZI)V",at = @At("TAIL"))
    protected void init(CallbackInfo ci) {
        if(owner != null) {
            if(owner.world.getCurrentWeather() == SignalIndustries.weatherBloodMoon && owner instanceof EntityMonster){
                damage *= 2;
            }
        }
    }
}
