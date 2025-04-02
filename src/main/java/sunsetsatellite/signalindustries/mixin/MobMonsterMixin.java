package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.monster.MobMonster;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SIWeather;

@Mixin(value = MobMonster.class,remap = false)
public abstract class MobMonsterMixin extends Mob {

    @Shadow protected int attackStrength;

    private MobMonsterMixin(@Nullable World world) {
        super(world);
    }

    @Inject(
            method = "attackEntity",
            at = @At("HEAD"),
            cancellable = true
    )
    public void attackEntity(Entity entity, float f, CallbackInfo ci){
        if (world != null && world.getCurrentWeather() == SIWeather.weatherBloodMoon) {
            if (this.attackTime <= 0 && f < 2.0F && entity.bb.maxY > this.bb.minY && entity.bb.minY < this.bb.maxY) {
                this.attackTime = 15;
                entity.hurt(this, attackStrength * 2, DamageType.COMBAT);
                ci.cancel();
            }
        }
    }

    @Override
    public void onDeath(Entity entity) {
        super.onDeath(entity);
        if(random.nextInt(32) == 0){
            dropItem(SIItems.monsterShard.id,1);
        } else if (world != null && world.getCurrentWeather() == SIWeather.weatherBloodMoon) {
            if (random.nextInt(16) == 0) {
                dropItem(SIItems.monsterShard.id, 1);
            }
        }
    }
}
