package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.achievement.stat.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
        value = Achievement.class,
        remap = false
)
public abstract class AchievementMixin extends Stat {

    public AchievementMixin(int i, String s) {
        super(i, s);
    }

    @Inject(
            method = "registerStat", at = @At("HEAD"),
            cancellable = true
    )
    public void registerStat(CallbackInfoReturnable<Stat> cir) {
        super.registerStat();
        cir.setReturnValue((Achievement)((Object)this));
    }
}
