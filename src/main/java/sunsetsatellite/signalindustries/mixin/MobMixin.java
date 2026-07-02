package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

@Mixin(value = Mob.class, remap = false)
public class MobMixin {

	@Unique
	private final Mob thisAs = (Mob) ((Object) this);

	@ModifyExpressionValue(
		method = "moveEntityWithHeading",
		at = @At(value = "FIELD", target = "Lnet/minecraft/core/entity/Mob;noPhysics:Z", opcode = Opcodes.GETFIELD)
	)
	private boolean flyWithWings(boolean original) {
		if (thisAs instanceof Player player) {
			SignalumPowerSuit ps = ((IPlayerPowerSuit<SignalumPowerSuit>) player).getPowerSuit();
			if (ps != null && ps.active && ps.hasAttachment(SIItems.crystalWings)) {
				return original || ps.getAttachment(SIItems.crystalWings).getData().getBoolean("active");
			} else {
				return original;
			}
		} else {
			return original;
		}
	}

	@Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
	protected void causeFallDamage(float f, CallbackInfo ci) {
		if (thisAs instanceof Player player) {
			SignalumPowerSuit ps = ((IPlayerPowerSuit<SignalumPowerSuit>) player).getPowerSuit();
			if (ps != null && ps.active && ps.hasAttachment(SIItems.crystalWings)) {
				if (ps.getAttachment(SIItems.crystalWings).getData().getBoolean("active")) {
					ci.cancel();
				}
			}
		}
	}

}
