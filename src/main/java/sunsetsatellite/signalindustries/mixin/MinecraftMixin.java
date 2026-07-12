package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.KeyboardHandler;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {

	@Shadow
	public PlayerLocal thePlayer;

	@Inject(
		method = "runTick",
		at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;next()Z", shift = At.Shift.AFTER)
	)
	public void handleKeyboard(CallbackInfo ci) {
		KeyboardHandler.handleKeyboard((Minecraft) (Object) this, ci);
	}

	@ModifyExpressionValue(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/player/PlayerLocal;hasNoPhysics()Z"))
	public boolean modifyWingsFlightSpeed(boolean original) {
		SignalumPowerSuit ps = ((IPlayerPowerSuit<SignalumPowerSuit>) thePlayer).getPowerSuit();
		if (ps != null && ps.active && ps.hasAttachment(SIItems.crystalWings)) {
			return original || ps.getAttachment(SIItems.crystalWings).getData().getBoolean("active");
		}
		return original;
	}

	@ModifyExpressionValue(method = "runTick", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/Minecraft;toggleFlyPressed:Z"))
	public boolean modifyWingsFlightSpeed2(boolean original) {
		boolean control = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
		SignalumPowerSuit ps = ((IPlayerPowerSuit<SignalumPowerSuit>) thePlayer).getPowerSuit();
		if (ps != null && ps.active && ps.hasAttachment(SIItems.crystalWings)) {
			return original || (ps.getAttachment(SIItems.crystalWings).getData().getBoolean("active") && control);
		}
		return original;
	}

}
