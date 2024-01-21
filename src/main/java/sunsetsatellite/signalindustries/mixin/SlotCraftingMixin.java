package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.SlotCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;

@Mixin(
        value = SlotCrafting.class,
        remap = false
)
public class SlotCraftingMixin {

    @Shadow private EntityPlayer thePlayer;

    @Inject(method = "onPickupFromSlot",at = @At("HEAD"))
    public void onPickupFromSlot(ItemStack itemStack, CallbackInfo ci) {
        Item item = itemStack.getItem();
        if(item.id == SignalIndustries.prototypeMachineCore.id){
            thePlayer.triggerAchievement(SignalIndustriesAchievementPage.THE_PROTOTYPE);
        }
        if(item.id == SignalIndustries.prototypeConduit.id){
            thePlayer.triggerAchievement(SignalIndustriesAchievementPage.TRANSFER);
        }
        if(item.id == SignalIndustries.prototypeEnergyCell.id){
            thePlayer.triggerAchievement(SignalIndustriesAchievementPage.BUFFER);
        }
        if(item.id == SignalIndustries.prototypeCrusher.id){
            thePlayer.triggerAchievement(SignalIndustriesAchievementPage.CRUSHER);
        }
        if(item.id == SignalIndustries.prototypeAlloySmelter.id){
            thePlayer.triggerAchievement(SignalIndustriesAchievementPage.ALLOY_SMELTER);
        }
        if(item.id == SignalIndustries.prototypePlateFormer.id){
            thePlayer.triggerAchievement(SignalIndustriesAchievementPage.PLATE_FORMER);
        }
        if(item.id == SignalIndustries.basicMachineCore.id){
            thePlayer.triggerAchievement(SignalIndustriesAchievementPage.BASIC);
        }
    }
}
