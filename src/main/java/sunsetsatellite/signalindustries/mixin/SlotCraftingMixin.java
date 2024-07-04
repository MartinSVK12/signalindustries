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
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;

@Mixin(
        value = SlotCrafting.class,
        remap = false
)
public class SlotCraftingMixin {

    @Shadow private EntityPlayer thePlayer;

    @Inject(method = "onPickupFromSlot",at = @At("HEAD"))
    public void onPickupFromSlot(ItemStack itemStack, CallbackInfo ci) {
        Item item = itemStack.getItem();
        if(item.id == SIBlocks.prototypeMachineCore.id){
            thePlayer.triggerAchievement(SIAchievements.THE_PROTOTYPE);
        }
        if(item.id == SIBlocks.prototypeConduit.id){
            thePlayer.triggerAchievement(SIAchievements.TRANSFER);
        }
        if(item.id == SIBlocks.prototypeEnergyCell.id){
            thePlayer.triggerAchievement(SIAchievements.BUFFER);
        }
        if(item.id == SIBlocks.prototypeCrusher.id){
            thePlayer.triggerAchievement(SIAchievements.CRUSHER);
        }
        if(item.id == SIBlocks.prototypeAlloySmelter.id){
            thePlayer.triggerAchievement(SIAchievements.ALLOY_SMELTER);
        }
        if(item.id == SIBlocks.prototypePlateFormer.id){
            thePlayer.triggerAchievement(SIAchievements.PLATE_FORMER);
        }
        if(item.id == SIBlocks.basicMachineCore.id){
            thePlayer.triggerAchievement(SIAchievements.BASIC);
        }
        if(item.id == SIBlocks.reinforcedMachineCore.id){
            thePlayer.triggerAchievement(SIAchievements.REINFORCED);
        }
        if(item.id == SIBlocks.dilithiumBooster.id){
            thePlayer.triggerAchievement(SIAchievements.BOOST);
        }
        if(item.id == SIItems.warpOrb.id){
            thePlayer.triggerAchievement(SIAchievements.WARP_ORB);
        }
        if(item.id == SIItems.signalumSaber.id){
            thePlayer.triggerAchievement(SIAchievements.BLADE);
        }
        if(item.id == SIItems.signalumPrototypeHarness.id){
            thePlayer.triggerAchievement(SIAchievements.HARNESS);
        }
        if(item.id == SIBlocks.basicAutomaticMiner.id){
            thePlayer.triggerAchievement(SIAchievements.MINER);
        }
        if(item.id == SIBlocks.basicPump.id){
            thePlayer.triggerAchievement(SIAchievements.PUMP);
        }
        if(item.id == SIItems.raziel.id){
            thePlayer.triggerAchievement(SIAchievements.DIVINE_KNOWLEDGE);
        }
    }
}
