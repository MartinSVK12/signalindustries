package sunsetsatellite.signalindustries.interfaces.mixins;


import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.OptionBoolean;
import org.spongepowered.asm.mixin.Unique;

public interface IKeybinds {
    KeyBinding signalIndustries$getKeyOpenSuit();

    KeyBinding signalIndustries$getKeyActivateAbility();

    KeyBinding signalIndustries$getKeySwitchMode();
    KeyBinding signalindustries$getKeyShowIndex();

    KeyBinding signalIndustries$getKeyActivateHeadLensAttachment();

    @Unique
    OptionBoolean signalindustries$isSuitBackgroundShown();

    KeyBinding signalIndustries$getKeyActivateHeadTopAttachment();

    KeyBinding signalIndustries$getKeyActivateCoreBackAttachment();

    KeyBinding signalIndustries$getKeyActivateArmFrontLAttachment();

    KeyBinding signalIndustries$getKeyActivateArmFrontRAttachment();

    KeyBinding signalIndustries$getKeyActivateArmSideLAttachment();

    KeyBinding signalIndustries$getKeyActivateArmSideRAttachment();

    KeyBinding signalIndustries$getKeyActivateArmBackLAttachment();

    KeyBinding signalIndustries$getKeyActivateArmBackRAttachment();

    KeyBinding signalIndustries$getKeyActivateLegSideLAttachment();

    KeyBinding signalIndustries$getKeyActivateLegSideRAttachment();

    KeyBinding signalIndustries$getKeyActivateBootBackLAttachment();

    KeyBinding signalIndustries$getKeyActivateBootBackRAttachment();

}