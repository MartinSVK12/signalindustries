package sunsetsatellite.signalindustries;

import net.minecraft.client.input.InputDevice;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.OptionBoolean;

public class SIKeybinds {

	public static OptionBoolean showSuitBackground = new OptionBoolean("signalindustries.showSuitBackground", true);

	public static OptionBoolean renderFluidInsideConduits = new OptionBoolean("signalindustries.renderFluidInsideConduits", true);

	public static KeyBinding keyOpenSuit = new KeyBinding("key.signalindustries.openSuit").bind(InputDevice.keyboard, 24);

	public static KeyBinding keyActivateAbility = new KeyBinding("key.signalindustries.activateAbility").bind(InputDevice.keyboard, 54);

	public static KeyBinding keySwitchMode = new KeyBinding("key.signalindustries.switchMode").bind(InputDevice.keyboard, 50);

	//public static KeyBinding keyRazielIndex = new KeyBinding("key.signalindustries.razielIndex").bind(InputDevice.keyboard, 23);

	public static KeyBinding keyActivateHeadTopAttachment = new KeyBinding("key.signalindustries.headTopActivate");

	public static KeyBinding keyActivateHeadLensAttachment = new KeyBinding("key.signalindustries.headLensActivate");

	public static KeyBinding keyActivateCoreBackAttachment = new KeyBinding("key.signalindustries.coreBackActivate");

	public static KeyBinding keyActivateArmFrontLAttachment = new KeyBinding("key.signalindustries.armFrontLActivate");

	public static KeyBinding keyActivateArmFrontRAttachment = new KeyBinding("key.signalindustries.armFrontRActivate");

	public static KeyBinding keyActivateArmSideLAttachment = new KeyBinding("key.signalindustries.armSideLActivate");

	public static KeyBinding keyActivateArmSideRAttachment = new KeyBinding("key.signalindustries.armSideRActivate");

	public static KeyBinding keyActivateArmBackLAttachment = new KeyBinding("key.signalindustries.armBackLActivate");

	public static KeyBinding keyActivateArmBackRAttachment = new KeyBinding("key.signalindustries.armBackRActivate");

	public static KeyBinding keyActivateLegSideLAttachment = new KeyBinding("key.signalindustries.legSideLActivate");

	public static KeyBinding keyActivateLegSideRAttachment = new KeyBinding("key.signalindustries.legSideRActivate");

	public static KeyBinding keyActivateBootBackLAttachment = new KeyBinding("key.signalindustries.bootBackLActivate");

	public static KeyBinding keyActivateBootBackRAttachment = new KeyBinding("key.signalindustries.bootBackRActivate");

}
