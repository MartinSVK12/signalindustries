package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.mixins.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.applications.ItemRaziel;
import sunsetsatellite.signalindustries.items.applications.ItemSmartWatch;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.IndexRenderer;

import java.util.Arrays;

@Debug(export = true)
@Mixin(value = GuiInventory.class,remap = false)
public abstract class GuiInventoryMixin extends GuiContainer {


    private GuiInventoryMixin(Container container) {
        super(container);
    }

    @Override
    public void keyTyped(char c, int i, int mouseX, int mouseY) {
        IndexRenderer.keyTyped(c, i, mouseX, mouseY);
        super.keyTyped(c, i, mouseX, mouseY);
    }

    @Inject(method = "drawScreen",at = @At("TAIL"))
    public void drawScreen(int mouseX, int mouseY, float partialTick, CallbackInfo ci){
        if(Arrays.stream(mc.thePlayer.inventory.mainInventory).anyMatch((S)->S != null && S.isItemEqual(SIItems.raziel.getDefaultStack()))){
            IndexRenderer.drawScreen(mc,mouseX,mouseY,width,height,partialTick);
        } else {
            SignalumPowerSuit powerSuit = ((IPlayerPowerSuit) mc.thePlayer).getPowerSuit();
            if(powerSuit != null && powerSuit.active && powerSuit.module != null){
                for (ItemStack content : powerSuit.module.contents) {
                    if(content != null && content.getItem() instanceof ItemRaziel){
                        IndexRenderer.drawScreen(mc,mouseX,mouseY,width,height,partialTick);
                    }
                }
            }
        }
    }

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
