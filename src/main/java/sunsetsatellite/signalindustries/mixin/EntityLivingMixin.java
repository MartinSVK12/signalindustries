package sunsetsatellite.signalindustries.mixin;

import net.minecraft.src.*;
import net.minecraft.src.helper.DamageType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Locale;

@Mixin(
        value = EntityLiving.class,
        remap = false
)
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
        if (this.worldObj.isDaytime()) {
            float f = this.getEntityBrightness(1.0F);
            if (f > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.getCurrentWeather() != Weather.weatherFog) {
                if(worldObj.currentWeather == SignalIndustries.weatherSolarApocalypse){
                    if (this.isImmuneToFire || this.worldObj.isMultiplayerAndNotHost) {
                        fire = 0;
                    } else{
                        fire = 300;
                        this.attackEntityFrom(null, 1, DamageType.FIRE);
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
        if(worldObj.currentWeather == SignalIndustries.weatherBloodMoon){
            int i = this.getDropItemId();
            if (i > 0) {
                int j = this.rand.nextInt(4) + 1;

                for(int k = 0; k < j; ++k) {
                    this.dropItem(i, 1);
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
        cir.setReturnValue(worldObj.currentWeather == SignalIndustries.weatherBloodMoon ? 16 : 4);
    }

    @Shadow
    protected void entityInit() {

    }

    @Shadow
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {

    }

    @Shadow
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {

    }
}
