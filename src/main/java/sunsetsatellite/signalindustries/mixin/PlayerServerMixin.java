package sunsetsatellite.signalindustries.mixin;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.mixins.IWarpPlayer;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

@Mixin(value = PlayerServer.class, remap = false)
public abstract class PlayerServerMixin extends Player implements IWarpPlayer, IPlayerPowerSuit<SignalumPowerSuit> {
	@Shadow
	public MinecraftServer mcServer;

	@Unique
	public SignalumPowerSuit powerSuit = null;

	@Unique
	public CompoundTag powerSuitData = null;

	private PlayerServerMixin(World world) {
		super(world);
	}

	@Inject(
		method = "addAdditionalSaveData",
		at = @At("HEAD")
	)
	public void saveSuitData(CompoundTag tag, CallbackInfo ci) {
		if (powerSuit != null) {
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
			if (itemStack == null) {
				powerSuit = null;
				//ItemNVGAttachment.disable();
				return;
			} else if (!(itemStack.getItem() instanceof ItemSignalumPowerSuit)) {
				//ItemNVGAttachment.disable();
				powerSuit = null;
				return;
			}
		}
		if (powerSuit == null) {
			powerSuit = new SignalumPowerSuit(this);
			triggerAchievement(SIAchievements.POWER_SUIT);
		} else {
			powerSuit.tick();
		}
	}

	@Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
	public void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
		if (tag.containsKey("PowerSuit")) {
			powerSuitData = tag.getCompound("PowerSuit");
		}
	}

	// power suit protection logic
	@Override
	protected void damageEntity(int damage, DamageType damageType) {
		float protection = 1.0f - this.getTotalProtectionAmount(damageType);
		protection = Math.max(protection, 0.01f);
		double d = (float) damage * protection;
		int newDamage = (int) ((double) this.random.nextFloat() > 0.5 ? Math.floor(d) : Math.ceil(d));
		int preventedDamage = damage - newDamage;
		if (powerSuit != null && powerSuit.active && powerSuit.status != SignalumPowerSuit.Status.OVERHEAT) {
			if (powerSuit.getEnergy() >= newDamage) {
				if (damageType != null && damageType.shouldDamageArmor()) {
					int armorDamage = (int) Math.ceil((double) preventedDamage / 4.0);
					this.damageArmor(armorDamage);
				}
				powerSuit.decrementEnergy(newDamage);
				return;
			}
			if (damageType == DamageType.FIRE) {
				powerSuit.temperature += 0.5f;
			}
		}
		if (inventory.armorItemInSlot(HumanArmorShape.CHEST) != null && inventory.armorItemInSlot(HumanArmorShape.CHEST).getData().getBoolean("active_shield") && damageType == DamageType.COMBAT) {
			return;
		}

		super.damageEntity(damage, damageType);
	}

	@Inject(
		method = "onLivingUpdate",
		at = @At("TAIL")
	)
	public void updateSpeed(CallbackInfo ci) {
		if (powerSuit != null && powerSuit.active) {
			if (powerSuit.hasAttachment(SIItems.movementBoosters, Catalyst.listOf(SignalumPowerSuit.AttachmentLocation.BOOT_BACK_R, SignalumPowerSuit.AttachmentLocation.BOOT_BACK_L))) {
				if (powerSuit.getAttachment(SIItems.movementBoosters) != null && powerSuit.getAttachment(SIItems.movementBoosters).getData().getBoolean("active")) {
					speed += (float) (baseSpeed * 1.5);
				}
			}
		}
	}

	@Override
	public SignalumPowerSuit getPowerSuit() {
		return powerSuit;
	}

	@Override
	public CompoundTag getPowerSuitData() {
		return powerSuitData;
	}

	@Override
	public void warp(int dim) {
		mcServer.playerList.sendPlayerToOtherDimension((PlayerServer) (Object) this, dim, null, false);
	}
}
