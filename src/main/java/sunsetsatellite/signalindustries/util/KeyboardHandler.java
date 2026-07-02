package sunsetsatellite.signalindustries.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.util.phys.HitResult;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SIKeybinds;
import sunsetsatellite.signalindustries.SignalIndustriesClient;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.tools.ItemSignalumDrill;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageDrillModeChange;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageOpenSuit;
import sunsetsatellite.signalindustries.mp.message.NetworkMessagePowerSuitAction;
import sunsetsatellite.signalindustries.powersuit.InventoryPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.Objects;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

@Environment(EnvType.CLIENT)
public class KeyboardHandler {

    private static int debounce = 0;

    public static void handleKeyboard(Minecraft mc, CallbackInfo ci) {
        if (debounce > 0) debounce--;
        boolean shift = (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT));
        boolean control = (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL));
        boolean alt = (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU));
        KeyBinding openSuitKey = SIKeybinds.keyOpenSuit;
        KeyBinding activeAbilityKey = SIKeybinds.keyActivateAbility;
        KeyBinding switchModeKey = SIKeybinds.keySwitchMode;
        SignalumPowerSuit powerSuit = ((IPlayerPowerSuit<SignalumPowerSuit>) mc.thePlayer).getPowerSuit();
        if (debounce <= 0) {
            for (KeyBinding attachmentKeybind : SignalIndustriesClient.attachmentKeybinds.values()) {
                if (attachmentKeybind.isPressed() && powerSuit != null && powerSuit.active && mc.currentWorld != null) {
                    debounce = 10;
                    if (mc.currentWorld.isClientSide) {
                        NetworkHandler.sendToServer(new NetworkMessagePowerSuitAction(NetworkMessagePowerSuitAction.ACTIVATE_ATTACHMENT).setKeybind(attachmentKeybind.getId()).setShift(shift).setCtrl(control).setAlt(alt));
                    } else {
                        powerSuit.activateAttachment(attachmentKeybind.getId(), shift, alt, control);
                    }
                }
            }
            if (switchModeKey.isPressed() && mc.currentScreen == null) {
                debounce = 10;
                if (mc.thePlayer != null && mc.thePlayer.getCurrentEquippedItem() != null) {
                    if (mc.thePlayer.getCurrentEquippedItem().getItem().equals(SIItems.reinforcedSignalumDrill)) {
                        ItemSignalumDrill.DrillMode mode = ((ItemSignalumDrill) SIItems.reinforcedSignalumDrill).getMode(mc.thePlayer.getCurrentEquippedItem());
                        switch (mode) {
                            case NORMAL:
                                ((ItemSignalumDrill) SIItems.reinforcedSignalumDrill).setMode(mc.thePlayer.getCurrentEquippedItem(), ItemSignalumDrill.DrillMode.X3);
                                break;
                            case X3:
                                ((ItemSignalumDrill) SIItems.reinforcedSignalumDrill).setMode(mc.thePlayer.getCurrentEquippedItem(), ItemSignalumDrill.DrillMode.X3_UNSAFE);
                                break;
                            case X3_UNSAFE:
                                ((ItemSignalumDrill) SIItems.reinforcedSignalumDrill).setMode(mc.thePlayer.getCurrentEquippedItem(), ItemSignalumDrill.DrillMode.NORMAL);
                                break;
                        }
                        mode = ((ItemSignalumDrill) SIItems.reinforcedSignalumDrill).getMode(mc.thePlayer.getCurrentEquippedItem());
                        mc.hudIngame.addChatMessage("Mode switched to: " + mode);
                        if (EnvironmentHelper.isClientWorld()) {
                            NetworkHandler.sendToServer(new NetworkMessageDrillModeChange(mode));
                        }
                    }
                }
            }
            if (openSuitKey.isPressed()) {
                debounce = 10;
                if (!shift && mc.currentScreen == null
                        && mc.thePlayer.inventory.armorItemInSlot(HumanArmorShape.CHEST) != null
                        && mc.thePlayer.inventory.armorItemInSlot(HumanArmorShape.CHEST).getItem().equals(SIItems.signalumPowerSuitChestplate)
                        && powerSuit != null
                ) {
                    if (mc.currentWorld.isClientSide) {
                        NetworkHandler.sendToServer(new NetworkMessageOpenSuit(HumanArmorShape.CHEST));
                    } else {
                        Catalyst.displayGui(mc.thePlayer, new InventoryPowerSuit(mc.thePlayer.inventory.armorItemInSlot(HumanArmorShape.CHEST)), HumanArmorShape.CHEST.getSlotIndex(), true, key("gui/power_suit"));
                    }
                }
                if (mc.currentScreen == null && powerSuit != null) {
                    if (shift) {
                        if (mc.currentWorld.isClientSide) {
                            NetworkHandler.sendToServer(new NetworkMessagePowerSuitAction(NetworkMessagePowerSuitAction.ACTIVATE).setShift(true));
                        } else {
                            powerSuit.active = !powerSuit.active;
                        }
                        return;
                    }
                }
            }
            if (activeAbilityKey.isPressed()) {
                debounce = 10;
                if (mc.currentScreen == null && powerSuit != null && powerSuit.active && mc.currentWorld != null) {
					HitResult hitResult = mc.objectMouseOver;
	                if(hitResult != null){
						if(hitResult instanceof HitResult.Tile tile){
							if (mc.currentWorld.isClientSide) {
								NetworkHandler.sendToServer(new NetworkMessagePowerSuitAction(NetworkMessagePowerSuitAction.ACTIVATE_APP)
									.setPos(new Vec3i((int) hitResult.location.x(), (int) hitResult.location.y(), (int) hitResult.location.z())));
							} else {
								powerSuit.activateApplication((int) hitResult.location.x(), (int) hitResult.location.y(), (int) hitResult.location.z());
							}
						} else if (hitResult instanceof HitResult.Entity entity) {
							if (mc.currentWorld.isClientSide) {
								NetworkHandler.sendToServer(new NetworkMessagePowerSuitAction(NetworkMessagePowerSuitAction.ACTIVATE_APP)
									.setPos(new Vec3i((int) hitResult.location.x(), (int) hitResult.location.y(), (int) hitResult.location.z()))
									.setEntityClass(entity.entity.getClass())
								);
							} else {
								powerSuit.activateApplication(entity.entity);
							}
						} else {
							if (mc.currentWorld.isClientSide) {
								NetworkHandler.sendToServer(new NetworkMessagePowerSuitAction(NetworkMessagePowerSuitAction.ACTIVATE_APP));
							} else {
								powerSuit.activateApplication();
							}
						}
					}
                }
            }
        }
        for (int i = 1; i < 10; ++i) {
            int eventKey = Keyboard.getEventKey();
            if (eventKey == -1) return;
			if(mc.currentWorld == null) return;
            if (Objects.equals(Keyboard.getKeyName(eventKey), "NUMPAD" + i) && powerSuit != null && powerSuit.active) {
                if (mc.currentWorld.isClientSide) {
                    NetworkHandler.sendToServer(new NetworkMessagePowerSuitAction(i - 1));
                } else {
                    powerSuit.selectedApplicationSlot = i - 1;
                }
            } else if (shift && powerSuit != null && powerSuit.active && eventKey == 1 + i) {
                if (mc.currentWorld.isClientSide) {
                    NetworkHandler.sendToServer(new NetworkMessagePowerSuitAction(i - 1));
                } else {
                    powerSuit.selectedApplicationSlot = i - 1;
                }
            }
        }
    }

}
