package sunsetsatellite.signalindustries.mixin;


import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SignalIndustries;

@Mixin(
        value = EntityMonster.class,
        remap = false
)
public class EntityMobMixin extends EntityLiving {
    @Shadow protected int attackStrength;

    public EntityMobMixin(World world) {
        super(world);
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At("HEAD")
    )
    public void onLivingUpdate(CallbackInfo ci) {
        if(world.currentWeather == SignalIndustries.weatherBloodMoon) {
            if(random.nextInt(2)==1){
                world.spawnParticle("reddust",x+random.nextFloat(),y+1,z+random.nextFloat(),0,0,0);
            } else {
                world.spawnParticle("reddust",x-random.nextFloat(),y+1,z-random.nextFloat(),0,0,0);
            }

        }
    }

    @Inject(
            method = "attackEntity",
            at = @At("HEAD"),
            cancellable = true
    )
    public void attackEntity(Entity entity, float f, CallbackInfo ci){
        if(world.currentWeather == SignalIndustries.weatherBloodMoon) {
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
        if(random.nextInt(64) == 0){
            spawnAtLocation(SignalIndustries.monsterShard.id,1);
        }
    }
}
