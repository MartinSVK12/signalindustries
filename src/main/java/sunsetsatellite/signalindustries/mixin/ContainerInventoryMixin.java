package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.gui.menus.MenuBackpack;

@Mixin(
        value = ContainerInventory.class,
        remap = false
)
public class ContainerInventoryMixin {

    @Final
    @Shadow
    public ItemStack[] armorInventory;

    @Final
    @Shadow
    public Player player;

	@Shadow
	private int currentSlot;

	@Inject(
            method = "decrementAnimations",
            at = @At("TAIL")
    )
    public void decrementAnimations(CallbackInfo ci) {
        for (int i = 0; i < this.armorInventory.length; i++) {
            if (this.armorInventory[i] != null)
                this.armorInventory[i].updateAnimation(this.player.world, this.player, i, (this.currentSlot == i));
        }
    }

    @Inject(method = "currentSlotLocked", at = @At("HEAD"), cancellable = true)
    public void currentSlotLocked(CallbackInfoReturnable<Boolean> cir) {
        if (this.player.containerMenu instanceof MenuBackpack) {
            cir.setReturnValue(true);
        }
    }
}
