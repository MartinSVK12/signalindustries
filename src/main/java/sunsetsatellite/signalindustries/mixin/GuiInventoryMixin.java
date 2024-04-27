package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.interfaces.mixins.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.ItemPortableWorkbench;
import sunsetsatellite.signalindustries.items.ItemSmartWatch;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

@Mixin(value = GuiInventory.class,remap = false)
public abstract class GuiInventoryMixin {

    @Inject(method = "updateOverlayButtons",at = @At(value = "FIELD", target = "Lnet/minecraft/core/item/ItemStack;itemID:I"))
    public void updateOverlayButtons(CallbackInfo ci, @Local(name = "item") ItemStack stack, @Local(name = "clock") LocalBooleanRef clock, @Local(name = "compass") LocalBooleanRef compass, @Local(name = "rotaryCalendar") LocalBooleanRef calendar) {
        if(stack != null && stack.getItem() instanceof ItemSmartWatch){
            clock.set(true);
            compass.set(true);
            calendar.set(true);
        }
        EntityPlayer player = Minecraft.getMinecraft(this).thePlayer;
        SignalumPowerSuit powerSuit = ((IPlayerPowerSuit) player).getPowerSuit();
        if(powerSuit != null && powerSuit.active && powerSuit.module != null){
            for (ItemStack content : powerSuit.module.contents) {
                if(content != null && content.getItem() instanceof ItemSmartWatch){
                    clock.set(true);
                    compass.set(true);
                    calendar.set(true);
                }
            }
        }
    }
}
