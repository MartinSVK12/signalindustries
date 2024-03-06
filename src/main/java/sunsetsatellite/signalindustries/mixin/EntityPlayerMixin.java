package sunsetsatellite.signalindustries.mixin;


import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.shader.ShadersRenderer;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumSleepStatus;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.fx.EntityColorParticleFX;
import sunsetsatellite.signalindustries.interfaces.mixins.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerSuit;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.items.attachments.ItemNVGAttachment;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.render.ShadersRendererSI;

@Mixin(
        value = EntityPlayer.class,
        remap = false
)
public abstract class EntityPlayerMixin extends EntityLiving implements IPlayerPowerSuit {

    @Unique
    public SignalumPowerSuit powerSuit = null;

    @Shadow public abstract void addChatMessage(String s);

    @Shadow public InventoryPlayer inventory;

    @Shadow protected float baseSpeed;

    public EntityPlayerMixin(World world) {
        super(world);
    }


    @Inject(
            method = "sleepInBedAt",
            at = @At("HEAD"),
            cancellable = true
    )
    public void sleepInBedAt(int x, int y, int z, CallbackInfoReturnable<EnumSleepStatus> cir) {
        if (!world.isClientSide) {
            if(world.getCurrentWeather() == SignalIndustries.weatherBloodMoon){
                addChatMessage("bed.bloodMoon");
                cir.setReturnValue(EnumSleepStatus.NOT_POSSIBLE_NOW);
            }
        }
    }

    @Override
    public SignalumPowerSuit signalIndustries$getPowerSuit() {
        return powerSuit;
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At("HEAD")
    )
    public void powerSuitUpdate(CallbackInfo ci) {
        ItemStack[] armorInventory = inventory.armorInventory;
        for (ItemStack itemStack : armorInventory) {
            if(itemStack == null){
                powerSuit = null;
                ItemNVGAttachment.disable();
                return;
            } else if(!(itemStack.getItem() instanceof ItemSignalumPowerSuit)){
                ItemNVGAttachment.disable();
                powerSuit = null;
                return;
            }
        }
        if(powerSuit == null){
            powerSuit = new SignalumPowerSuit(armorInventory,((EntityPlayer) (Object) this));
            ((EntityPlayer) (Object) this).triggerAchievement(SignalIndustriesAchievementPage.POWER_SUIT);
        } else {
            powerSuit.tick();
        }
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At("TAIL")
    )
    public void updateSpeed(CallbackInfo ci) {
        if(powerSuit != null && powerSuit.active){
            if(powerSuit.hasAttachment((ItemAttachment) SignalIndustries.movementBoosters,SignalIndustries.listOf("bootBackL","bootBackR"))){
                if(powerSuit.getAttachment((ItemAttachment) SignalIndustries.movementBoosters) != null && powerSuit.getAttachment((ItemAttachment) SignalIndustries.movementBoosters).getData().getBoolean("active")){
                    speed += (float) (baseSpeed * 0.3);
                }
            }
        }
    }

    @Inject(
            method = "damageEntity",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void damageEntity(int damage, DamageType damageType, CallbackInfo ci) {
        float protection = 1.0f - this.inventory.getTotalProtectionAmount(damageType);
        protection = Math.max(protection, 0.01f);
        double d = (float)damage * protection;
        int newDamage = (int)((double)this.random.nextFloat() > 0.5 ? Math.floor(d) : Math.ceil(d));
        int preventedDamage = damage - newDamage;
        if (powerSuit != null && powerSuit.active && powerSuit.status != SignalumPowerSuit.Status.OVERHEAT ) {
            if(powerSuit.getEnergy() >= newDamage){
                if (damageType != null && damageType.shouldDamageArmor()) {
                    int armorDamage = (int)Math.ceil((double)preventedDamage / 4.0);
                    this.inventory.damageArmor(armorDamage);
                }
                powerSuit.decrementEnergy(newDamage);
                ci.cancel();
            }
            if(damageType == DamageType.FIRE){
                powerSuit.temperature += 0.5f;
            }
        }
    }

    @Inject(
            method = "addAdditionalSaveData",
            at = @At("HEAD")
    )
    public void saveSuitData(CompoundTag nbttagcompound, CallbackInfo ci) {
        if(powerSuit != null){
            powerSuit.saveToStacks();
        }
    }
}
