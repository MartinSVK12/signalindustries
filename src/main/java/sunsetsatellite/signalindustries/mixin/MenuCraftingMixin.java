package sunsetsatellite.signalindustries.mixin;


import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.menu.MenuCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.applications.ItemPortableWorkbench;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

@Mixin(value = MenuCrafting.class, remap = false)
public class MenuCraftingMixin {

    @Inject(method = "stillValid", at = @At("HEAD"), cancellable = true)
    public void isUsableByPlayer(Player entityplayer, CallbackInfoReturnable<Boolean> cir) {
        SignalumPowerSuit powerSuit = ((IPlayerPowerSuit<SignalumPowerSuit>) entityplayer).getPowerSuit();
        if (powerSuit != null && powerSuit.active && powerSuit.module != null) {
            for (ItemStack content : powerSuit.module.contents) {
                if (content != null && content.getItem() instanceof ItemPortableWorkbench) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
        for (ItemStack content : entityplayer.inventory.mainInventory) {
            if (content != null && content.getItem() instanceof ItemPortableWorkbench) {
                cir.setReturnValue(true);
                return;
            }
        }
    }
}
