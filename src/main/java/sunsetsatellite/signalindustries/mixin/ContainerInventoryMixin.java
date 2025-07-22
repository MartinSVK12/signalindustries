package sunsetsatellite.signalindustries.mixin;

import net.fabricmc.api.Environment;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.menus.MenuBackpack;

@Mixin(
        value = ContainerInventory.class,
        remap = false
)
public class ContainerInventoryMixin {

    @Shadow public ItemStack[] armorInventory;

    @Shadow public Player player;

    @Shadow
    protected int currentItem;

    @Inject(
            method = "decrementAnimations",
            at = @At("TAIL")
    )
    public void decrementAnimations(CallbackInfo ci) {
        for (int i = 0; i < this.armorInventory.length; i++) {
            if (this.armorInventory[i] != null && this.player.world != null)
                this.armorInventory[i].updateAnimation(this.player.world, this.player, i, (this.currentItem == i));
        }
    }

    @Inject(method = "currentItemLocked", at = @At("HEAD"), cancellable = true)
    public void currentItemLocked(CallbackInfoReturnable<Boolean> cir) {
        if(this.player.craftingInventory instanceof MenuBackpack){
            cir.setReturnValue(true);
        }
    }
}
