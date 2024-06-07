package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIItems;

@Mixin(value = Slot.class,remap = false)
public class SlotMixin {
    @Inject(method = "onPickupFromSlot",at = @At("HEAD"))
    public void onPickupFromSlot(ItemStack itemStack, CallbackInfo ci) {
        Item item = itemStack.getItem();
        //TODO: check for item group instead
        if(item.id == SIItems.romChipProjectile.id || item.id == SIItems.romChipBoost.id){
            Minecraft.getMinecraft(Minecraft.class).thePlayer.triggerAchievement(SIAchievements.ROM_CHIP);
        }
    }
}
