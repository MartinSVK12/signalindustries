package sunsetsatellite.signalindustries.mixin;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.weather.Weather;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SIWeather;
import sunsetsatellite.signalindustries.interfaces.mixins.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

import java.util.Iterator;
import java.util.List;


@Mixin(value = EntityLiving.class, remap = false)
public abstract class EntityLivingMixin extends Entity {
    @Unique
    private final EntityLiving thisAs = (EntityLiving) ((Object)this);

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
                if(world.getCurrentWeather() == SIWeather.weatherSolarApocalypse){
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

    @Shadow protected abstract List<WeightedRandomLootObject> getMobDrops();

    @Inject(
            method = "dropFewItems",
            at = @At("HEAD")
    )
    protected void bloodMoonReward(CallbackInfo ci){
        if(world.getCurrentWeather() == SIWeather.weatherBloodMoon){
            List<WeightedRandomLootObject> drops = this.getMobDrops();
            if (drops != null) {
                Iterator<WeightedRandomLootObject> var2 = drops.iterator();

                while(true) {
                    ItemStack stack;
                    do {
                        if (!var2.hasNext()) {
                            return;
                        }

                        WeightedRandomLootObject lootObject = var2.next();
                        stack = lootObject.getItemStack();
                    } while(stack == null);

                    for(int i = 0; i < stack.stackSize; ++i) {
                        this.spawnAtLocation(new ItemStack(stack.itemID, 1, stack.getMetadata(), stack.getData()), 0.0F);
                    }
                }
            }
        }
    }

    @Inject(
            method = "getMaxSpawnedInChunk",
            at = @At("HEAD"),
            cancellable = true
    )
    public void bloodMoonSpawning(CallbackInfoReturnable<Integer> cir){
        cir.setReturnValue(world.getCurrentWeather() == SIWeather.weatherBloodMoon ? 16 : 4);
    }

    @Inject(method = "getCanSpawnHere",at = @At("HEAD"),cancellable = true)
    public void getCanSpawnHere(CallbackInfoReturnable<Boolean> cir)
    {
        if(world.dimension == SIDimensions.dimEternity){
            cir.setReturnValue(false);
        }
    }

    //TODO: disable flight checks in mp when wings are worn
    @ModifyExpressionValue(
            method = "moveEntityWithHeading",
            at = @At(value = "FIELD", target = "Lnet/minecraft/core/entity/EntityLiving;noPhysics:Z",opcode = Opcodes.GETFIELD)
    )
    private boolean flyWithWings(boolean original){
        if(thisAs instanceof EntityPlayer){
            EntityPlayer player = ((EntityPlayer) thisAs);
            SignalumPowerSuit ps = ((IPlayerPowerSuit)player).getPowerSuit();
            if(ps != null && ps.active && ps.hasAttachment((ItemAttachment) SIItems.crystalWings)){
                return original || ps.getAttachment((ItemAttachment) SIItems.crystalWings).getData().getBoolean("active");
            } else {
                return original;
            }
        } else {
            return original;
        }
    }

    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    protected void causeFallDamage(float f, CallbackInfo ci) {
        if(thisAs instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) thisAs);
            SignalumPowerSuit ps = ((IPlayerPowerSuit) player).getPowerSuit();
            if (ps != null && ps.active && ps.hasAttachment((ItemAttachment) SIItems.crystalWings)) {
                if(ps.getAttachment((ItemAttachment) SIItems.crystalWings).getData().getBoolean("active")){
                    ci.cancel();
                }
            }
        }
    }
}
