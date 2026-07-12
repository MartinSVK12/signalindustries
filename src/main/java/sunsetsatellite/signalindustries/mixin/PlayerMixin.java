package sunsetsatellite.signalindustries.mixin;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.items.base.ItemToolTiered;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixin extends Mob implements IPlayerPowerSuit<SignalumPowerSuit> {
	private PlayerMixin(@NotNull World world) {
		super(world);
	}

	@Override
	public SignalumPowerSuit getPowerSuit() {
		return null;
	}

	@Override
	public CompoundTag getPowerSuitData() {
		return null;
	}

	@Inject(method = "getAcidMeltDamage", at = @At("HEAD"), cancellable = true)
	private void getAcidMeltDamage(ItemStack stack, int acidIntensity, CallbackInfoReturnable<Integer> cir) {
		if(stack != null && stack.getItem() instanceof ITiered){
			cir.setReturnValue(0);
		}
	}
}
