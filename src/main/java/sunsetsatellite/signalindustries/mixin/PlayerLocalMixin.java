package sunsetsatellite.signalindustries.mixin;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.entity.player.PlayerLocalMultiplayer;
import net.minecraft.client.render.shader.Shaders;
import net.minecraft.client.render.shader.ShadersRenderer;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustriesClient;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.mixins.IWarpPlayer;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerSuit;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuitClient;
import sunsetsatellite.signalindustries.render.ShadersRendererSI;

@Mixin(value = PlayerLocal.class, remap = false)
public abstract class PlayerLocalMixin extends Player implements IWarpPlayer, IPlayerPowerSuit<SignalumPowerSuitClient> {

    @Shadow protected Minecraft mc;
    @Unique
    public SignalumPowerSuitClient powerSuit = null;

    @Unique
    public CompoundTag powerSuitData = null;

    @Unique
    public boolean nightVisionShader = false;

    private PlayerLocalMixin(World world) {
        super(world);
    }

    @Override
    public void warp(int dim) {
        SignalIndustriesClient.movePlayerToDimension(((PlayerLocal) (Object) this), dim);
    }

    @Override
    public SignalumPowerSuitClient getPowerSuit() {
        return powerSuit;
    }

    @Override
    public CompoundTag getPowerSuitData() {
        return powerSuitData;
    }

    @Inject(
            method = "addAdditionalSaveData",
            at = @At("HEAD")
    )
    public void saveSuitData(CompoundTag tag, CallbackInfo ci) {
        if(powerSuit != null){
            //powerSuit.saveToStacks();
            powerSuit.saveData(tag);
        }
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
                toggleNightVision(false, null);
                return;
            } else if(!(itemStack.getItem() instanceof ItemSignalumPowerSuit)){
                toggleNightVision(false, null);
                powerSuit = null;
                return;
            }
        }
        if(powerSuit == null){
            powerSuit = new SignalumPowerSuitClient((PlayerLocal) (Object)this);
            triggerAchievement(SIAchievements.POWER_SUIT);
        } else {
            powerSuit.tick();
        }

        SignalumPowerSuit ps = this.getPowerSuit();
        if(ps != null && ps.active &&ps.hasAttachment(SIItems.nightVisionLens)) {
            if (ps.getAttachment(SIItems.nightVisionLens).getData().getBoolean("active") && !nightVisionShader && ps.getEnergy() > 1) {
                toggleNightVision(true, ps);
            } else if((!ps.getAttachment(SIItems.nightVisionLens).getData().getBoolean("active") || ps.getEnergy() < 1) && nightVisionShader) {
                toggleNightVision(false, ps);
            }
        }
    }

    @Unique
    private void toggleNightVision(boolean nightVision, SignalumPowerSuit ps) {
        if(nightVision && !nightVisionShader) {
            nightVisionShader = true;
            if (Shaders.enableShaders) {
                mc.setRenderer(new ShadersRendererSI(mc, "nightvision/", ps));
                mc.renderer.reload();
                mc.fullbright = true;
                mc.renderGlobal.loadRenderers();
            }
        } else if(!nightVision && nightVisionShader) {
            nightVisionShader = false;

            if (Shaders.enableShaders) {
                mc.setRenderer(new ShadersRenderer(mc));
                mc.renderer.reload();
                mc.fullbright = false;
                mc.renderGlobal.loadRenderers();
            }
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    public void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci)
    {
        if (tag.containsKey("PowerSuit")) {
            powerSuitData = tag.getCompound("PowerSuit");
        }
    }

    // power suit protection logic
    @Override
    protected void damageEntity(int damage, DamageType damageType) {
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
                return;
            }
            if(damageType == DamageType.FIRE){
                powerSuit.temperature += 0.5f;
            }
        }
        if(inventory.armorItemInSlot(2) != null && inventory.armorItemInSlot(2).getData().getBoolean("active_shield") && damageType == DamageType.COMBAT){
            return;
        }

        super.damageEntity(damage, damageType);
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At("TAIL")
    )
    public void updateSpeed(CallbackInfo ci) {
        if(powerSuit != null && powerSuit.active){
            if(powerSuit.hasAttachment(SIItems.movementBoosters, Catalyst.listOf(SignalumPowerSuit.AttachmentLocation.BOOT_BACK_R, SignalumPowerSuit.AttachmentLocation.BOOT_BACK_L))){
                if(powerSuit.getAttachment(SIItems.movementBoosters) != null && powerSuit.getAttachment(SIItems.movementBoosters).getData().getBoolean("active")){
                    speed += (float) (baseSpeed * 1.5);
                }
            }
        }
    }
}
