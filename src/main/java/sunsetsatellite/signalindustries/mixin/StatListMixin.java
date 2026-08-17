package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.achievement.stat.StatList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIAchievements;

@Mixin(value = StatList.class, remap = false)
public class StatListMixin {

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/achievement/Achievements;init()V", shift = At.Shift.AFTER))
	private static void init(CallbackInfo ci) {
		new SIAchievements().init();
	}

}
