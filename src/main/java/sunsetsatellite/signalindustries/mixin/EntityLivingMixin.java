package sunsetsatellite.signalindustries.mixin;


import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.weather.Weather;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SignalIndustries;

@Debug(export = true)
@Mixin(value = EntityLiving.class, remap = false)
public abstract class EntityLivingMixin extends Entity {
    @Shadow protected abstract int getDropItemId();

    public EntityLivingMixin(World world) {
        super(world);
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At("HEAD")
    )
    public void burnBabyBurn(CallbackInfo ci) {
        if (this.world.isDaytime()) {
            float f = this.getBrightness(1.0F);
            if (f > 0.5F && this.world.canBlockSeeTheSky(MathHelper.floor_double(this.x), MathHelper.floor_double(this.y), MathHelper.floor_double(this.z)) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.getCurrentWeather() != Weather.overworldFog) {
                if(world.getCurrentWeather() == SignalIndustries.weatherSolarApocalypse){
                    if (this.fireImmune || this.world.isClientSide) {
                        remainingFireTicks = 0;
                    } else{
                        remainingFireTicks = 300;
                        this.hurt(null, 1, DamageType.FIRE);
                    }
                }
            }
        }
    }

    @Inject(
            method = "dropFewItems",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void bloodMoonReward(CallbackInfo ci){
        if(world.getCurrentWeather() == SignalIndustries.weatherBloodMoon){
            int i = this.getDropItemId();
            if (i > 0) {
                int j = this.random.nextInt(4) + 1;

                for(int k = 0; k < j; ++k) {
                    this.spawnAtLocation(i, 1);
                }
                ci.cancel();
            }
        }
    }

    @Inject(
            method = "getMaxSpawnedInChunk",
            at = @At("HEAD"),
            cancellable = true
    )
    public void bloodMoonSpawning(CallbackInfoReturnable<Integer> cir){
        cir.setReturnValue(world.getCurrentWeather() == SignalIndustries.weatherBloodMoon ? 16 : 4);
    }

    @Inject(method = "getCanSpawnHere",at = @At("HEAD"),cancellable = true)
    public void getCanSpawnHere(CallbackInfoReturnable<Boolean> cir)
    {
        if(world.dimension == SignalIndustries.dimEternity){
            cir.setReturnValue(false);
        }
    }
}
