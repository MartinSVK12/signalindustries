package sunsetsatellite.signalindustries.mixin;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.entity.player.PlayerRemote;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.mixins.IWarpPlayer;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuitRemote;

@Mixin(value = PlayerRemote.class, remap = false)
public abstract class PlayerRemoteMixin extends Player implements IPlayerPowerSuit<SignalumPowerSuitRemote> {

	@Unique
	public SignalumPowerSuitRemote powerSuit = null;

	private PlayerRemoteMixin(World world) {
		super(world);
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
			powerSuit = new SignalumPowerSuitRemote((PlayerRemote) (Object) this);
			//triggerAchievement(SIAchievements.POWER_SUIT);
		} else {
			powerSuit.tick();
		}
	}

	@Override
	public SignalumPowerSuitRemote getPowerSuit() {
		return powerSuit;
	}

	@Override
	public CompoundTag getPowerSuitData() {
		return null;
	}
}
