package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.ContainerWorkbench;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.interfaces.mixins.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.ItemPortableWorkbench;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

@Mixin(value = ContainerWorkbench.class,remap = false)
public class ContainerWorkbenchMixin {

    @Inject(method = "isUsableByPlayer",at = @At("HEAD"), cancellable = true)
    public void isUsableByPlayer(EntityPlayer entityplayer, CallbackInfoReturnable<Boolean> cir) {
        SignalumPowerSuit powerSuit = ((IPlayerPowerSuit) entityplayer).getPowerSuit();
        if(powerSuit != null && powerSuit.active && powerSuit.module != null){
            for (ItemStack content : powerSuit.module.contents) {
                if(content != null && content.getItem() instanceof ItemPortableWorkbench){
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
