package sunsetsatellite.signalindustries.mixin;


import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
        value = InventoryPlayer.class,
        remap = false
)
public class InventoryPlayerMixin {

    @Shadow public ItemStack[] armorInventory;

    @Shadow public EntityPlayer player;

    @Shadow public int currentItem;

    @Inject(
            method = "decrementAnimations",
            at = @At("TAIL")
    )
    public void decrementAnimations(CallbackInfo ci) {
        for (int i = 0; i < this.armorInventory.length; i++) {
            if (this.armorInventory[i] != null)
                this.armorInventory[i].updateAnimation(this.player.world, this.player, i, (this.currentItem == i));
        }
    }
}
