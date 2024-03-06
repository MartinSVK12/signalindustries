package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;

@Mixin(value = Slot.class,remap = false)
public class SlotMixin {
    @Inject(method = "onPickupFromSlot",at = @At("HEAD"))
    public void onPickupFromSlot(ItemStack itemStack, CallbackInfo ci) {
        Item item = itemStack.getItem();
        //TODO: check for item group instead
        if(item.id == SignalIndustries.romChipProjectile.id || item.id == SignalIndustries.romChipBoost.id){
            Minecraft.getMinecraft(Minecraft.class).thePlayer.triggerAchievement(SignalIndustriesAchievementPage.ROM_CHIP);
        }
    }
}
