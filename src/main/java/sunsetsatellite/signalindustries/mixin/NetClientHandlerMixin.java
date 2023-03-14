package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.util.Config;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.lang.reflect.InvocationTargetException;

@Mixin(
        value= NetClientHandler.class,
        remap = false
)
public class NetClientHandlerMixin extends NetHandler {

    @Shadow private Minecraft mc;

    @Inject(
            method = "handleOpenWindow",
            at = @At("TAIL")
    )
    public void handleOpenWindow(Packet100OpenWindow packet100openwindow, CallbackInfo ci) {
        if(packet100openwindow.inventoryType == Config.getFromConfig("GuiID",9)){
            TileEntity tile;
            try {
                tile = (TileEntity) SignalIndustries.nameToGuiMap.get(packet100openwindow.windowTitle).get(1).getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            try {
                this.mc.displayGuiScreen((GuiScreen) SignalIndustries.nameToGuiMap.get(packet100openwindow.windowTitle).get(0).getDeclaredConstructors()[0].newInstance(this.mc.thePlayer.inventory,tile));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            this.mc.thePlayer.craftingInventory.windowId = packet100openwindow.windowId;
        }
    }

    @Override
    public boolean isServerHandler() {
        return false;
    }
}
