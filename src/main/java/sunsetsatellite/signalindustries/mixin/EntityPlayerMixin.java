package sunsetsatellite.signalindustries.mixin;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumSleepStatus;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SignalIndustries;

@Mixin(
        value = EntityPlayer.class,
        remap = false
)
public abstract class EntityPlayerMixin extends EntityLiving{

    @Shadow public abstract void addChatMessage(String s);

    public EntityPlayerMixin(World world) {
        super(world);
    }

    @Inject(
            method = "sleepInBedAt",
            at = @At("HEAD"),
            cancellable = true
    )
    public void sleepInBedAt(int x, int y, int z, CallbackInfoReturnable<EnumSleepStatus> cir) {
        if (!worldObj.isMultiplayerAndNotHost) {
            if(worldObj.currentWeather == SignalIndustries.weatherBloodMoon){
                addChatMessage("bed.bloodMoon");
                cir.setReturnValue(EnumSleepStatus.NOT_POSSIBLE_NOW);
            }
        }
    }
}
