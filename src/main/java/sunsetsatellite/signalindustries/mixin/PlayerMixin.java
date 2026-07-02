package sunsetsatellite.signalindustries.mixin;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
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
}
