package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.entity.monster.MobMonster;
import net.minecraft.core.entity.projectile.Projectile;
import net.minecraft.core.entity.projectile.ProjectileArrow;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIWeather;

@Mixin(value = ProjectileArrow.class,remap = false)
public abstract class ProjectileArrowMixin extends Projectile {
    private ProjectileArrowMixin(World world) {
        super(world);
    }

    @Inject(method = "initProjectile",at = @At("TAIL"))
    protected void init(CallbackInfo ci) {
        if(owner != null) {
            if (owner.world != null && owner.world.getCurrentWeather() == SIWeather.weatherBloodMoon && owner instanceof MobMonster) {
                damage *= 2;
            }
        }
    }
}
