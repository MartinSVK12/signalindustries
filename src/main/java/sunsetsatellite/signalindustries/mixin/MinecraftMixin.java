package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.core.HitResult;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.interfaces.mixins.IKeybinds;
import sunsetsatellite.signalindustries.interfaces.mixins.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

import java.util.Objects;

@Mixin(
        value = Minecraft.class,
        remap = false
)
public class MinecraftMixin {


    @Shadow public GameSettings gameSettings;

    @Shadow public GuiScreen currentScreen;

    @Shadow public EntityPlayerSP thePlayer;

    @Shadow public HitResult objectMouseOver;

    @Inject(
            method = "runTick",
            at = @At(value = "INVOKE",target = "Lorg/lwjgl/input/Keyboard;next()Z",shift = At.Shift.AFTER)
    )
    public void handleKeyboard(CallbackInfo ci){
        boolean shift = (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
        boolean control = (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157));
        KeyBinding openSuitKey = ((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyOpenSuit();
        KeyBinding activeAbilityKey = ((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyActivateAbility();
        SignalumPowerSuit powerSuit = ((IPlayerPowerSuit)thePlayer).signalIndustries$getPowerSuit();
        if(openSuitKey.isPressed()){
            if(currentScreen == null && powerSuit != null){
                if(shift){
                    powerSuit.active = !powerSuit.active;
                    return;
                }
                powerSuit.openCoreUi();
            }
        }
        if(activeAbilityKey.isPressed()){
            if(currentScreen == null && powerSuit != null && powerSuit.active) {
                if (objectMouseOver != null && objectMouseOver.entity == null) {
                    powerSuit.activateSelectedAbility(objectMouseOver.x, objectMouseOver.y, objectMouseOver.z);
                } else if (objectMouseOver != null) {
                    powerSuit.activateSelectedAbility(objectMouseOver.entity);
                } else {
                    powerSuit.activateSelectedAbility();
                }

            }
        }
        for(int i = 1; i < 10; ++i) {
            if (Objects.equals(Keyboard.getKeyName(Keyboard.getEventKey()), "NUMPAD" + i) && powerSuit != null && powerSuit.active) {
                powerSuit.selectedAbilitySlot = i-1;
            } else if (shift && powerSuit != null && powerSuit.active && Keyboard.getEventKey() == 1 + i) {
                powerSuit.selectedAbilitySlot = i-1;
            }
        }
        /*SignalIndustries.LOGGER.info(String.format("Shift: %s | Control: %S",shift,control));
        SignalIndustries.LOGGER.info(String.format("Key: %s | Char: %s | State: %s",Keyboard.getEventKey(),Keyboard.getKeyName(Keyboard.getEventKey()),Keyboard.getEventKeyState()));*/

        //0-82 1-79 2-80 3-81 4-75 5-76 6-77 7-71 8-72 9-73

        /*SignalIndustries.LOGGER.info(Keyboard.getKeyName(key));
        SignalIndustries.LOGGER.info(String.valueOf(key));*/
    }
}
