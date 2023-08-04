package sunsetsatellite.signalindustries.mixin;






import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SignalIndustries;

@Mixin(
        value = EntityMob.class,
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
        if(worldObj.currentWeather == SignalIndustries.weatherBloodMoon) {
            if(rand.nextInt(2)==1){
                worldObj.spawnParticle("reddust",posX+rand.nextFloat(),posY+1,posZ+rand.nextFloat(),0,0,0);
            } else {
                worldObj.spawnParticle("reddust",posX-rand.nextFloat(),posY+1,posZ-rand.nextFloat(),0,0,0);
            }

        }
    }

    @Inject(
            method = "attackEntity",
            at = @At("HEAD"),
            cancellable = true
    )
    public void attackEntity(Entity entity, float f, CallbackInfo ci){
        if(worldObj.currentWeather == SignalIndustries.weatherBloodMoon) {
            if (this.attackTime <= 0 && f < 2.0F && entity.boundingBox.maxY > this.boundingBox.minY && entity.boundingBox.minY < this.boundingBox.maxY) {
                this.attackTime = 15;
                entity.attackEntityFrom(this, attackStrength * 2, DamageType.COMBAT);
                ci.cancel();
            }
        }
    }

    @Override
    public void onDeath(Entity entity) {
        super.onDeath(entity);
        if(rand.nextInt(64) == 0){
            dropItem(SignalIndustries.monsterShard.itemID,1);
        }
    }
}
