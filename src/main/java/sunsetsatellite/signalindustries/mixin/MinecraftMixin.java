package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.core.HitResult;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.mixins.IKeybinds;
import sunsetsatellite.signalindustries.interfaces.mixins.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.containers.ItemSignalumDrill;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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

    @Shadow public GuiIngame ingameGUI;

    @Unique
    private static int debounce = 0;

    @Unique
    private static final List<KeyBinding> attachmentKeybinds = new ArrayList<>();

    @Inject(
            method = "startGame",
            at = @At("TAIL")
    )
    public void start(CallbackInfo ci){
        Method[] methods = IKeybinds.class.getDeclaredMethods();
        for (Method method : methods) {
            try {
                if(method.getName().contains("Attachment")){
                    KeyBinding keyBinding = (KeyBinding) method.invoke(Minecraft.getMinecraft(Minecraft.class).gameSettings);
                    attachmentKeybinds.add(keyBinding);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Inject(
            method = "runTick",
            at = @At(value = "INVOKE",target = "Lorg/lwjgl/input/Keyboard;next()Z",shift = At.Shift.AFTER)
    )
    public void handleKeyboard(CallbackInfo ci){
        if(debounce > 0) debounce--;
        boolean shift = (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
        boolean control = (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157));
        KeyBinding openSuitKey = ((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyOpenSuit();
        KeyBinding activeAbilityKey = ((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyActivateAbility();
        KeyBinding switchModeKey = ((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeySwitchMode();
        SignalumPowerSuit powerSuit = ((IPlayerPowerSuit)thePlayer).signalIndustries$getPowerSuit();
        if(debounce <= 0){
            for (KeyBinding attachmentKeybind : attachmentKeybinds) {
                if (attachmentKeybind.isPressed() && powerSuit != null && powerSuit.active) {
                    debounce = 10;
                    powerSuit.activateAttachment(attachmentKeybind);
                }
            }
            if(switchModeKey.isPressed() && currentScreen == null){
                debounce = 10;
                if(thePlayer != null && thePlayer.getCurrentEquippedItem() != null){
                    if(thePlayer.getCurrentEquippedItem().getItem() == SignalIndustries.reinforcedSignalumDrill){
                        ItemSignalumDrill.DrillMode mode = ((ItemSignalumDrill)SignalIndustries.reinforcedSignalumDrill).getMode(thePlayer.getCurrentEquippedItem());
                        switch (mode){
                            case NORMAL:
                                ((ItemSignalumDrill)SignalIndustries.reinforcedSignalumDrill).setMode(thePlayer.getCurrentEquippedItem(), ItemSignalumDrill.DrillMode.X3);
                                break;
                            case X3:
                                ((ItemSignalumDrill)SignalIndustries.reinforcedSignalumDrill).setMode(thePlayer.getCurrentEquippedItem(), ItemSignalumDrill.DrillMode.NORMAL);
                                break;
                        }
                        mode = ((ItemSignalumDrill)SignalIndustries.reinforcedSignalumDrill).getMode(thePlayer.getCurrentEquippedItem());
                        ingameGUI.addChatMessage("Mode switched to: "+mode);
                    }
                }
            }
            if(openSuitKey.isPressed()){
                debounce = 10;
                if(currentScreen == null && powerSuit != null){
                    if(shift){
                        powerSuit.active = !powerSuit.active;
                        return;
                    }
                    powerSuit.openCoreUi();
                }
            }
            if(activeAbilityKey.isPressed()){
                debounce = 10;
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
