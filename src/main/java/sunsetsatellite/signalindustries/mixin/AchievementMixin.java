package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.achievement.stat.StatBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
        value = Achievement.class,
        remap = false
)
public abstract class AchievementMixin extends StatBase {

    public AchievementMixin(int i, String s) {
        super(i, s);
    }

    @Inject(
            method = "registerStat", at = @At("HEAD"),
            cancellable = true
    )
    public void registerStat(CallbackInfoReturnable<StatBase> cir) {
        super.registerStat();
        cir.setReturnValue((Achievement)((Object)this));
    }
}
