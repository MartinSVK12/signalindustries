package sunsetsatellite.signalindustries.mixin;


import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import sunsetsatellite.signalindustries.interfaces.mixins.IKeybinds;

@Mixin(
        value = GameSettings.class,
        remap = false
)
public class GameSettingsMixin
    implements IKeybinds
{
    @Unique
    public KeyBinding keyOpenSuit = new KeyBinding("key.signalindustries.openSuit",24);

    @Unique
    public KeyBinding keyActivateAbility = new KeyBinding("key.signalindustries.activateAbility",54);

    @Unique
    public KeyBinding keySwitchMode = new KeyBinding("key.signalindustries.switchMode",50);

    @Unique
    public KeyBinding keyActivateHeadTopAttachment = new KeyBinding("key.signalindustries.headTopActivate",0);

    @Unique
    public KeyBinding keyActivateCoreBackAttachment = new KeyBinding("key.signalindustries.coreBackActivate",0);

    @Unique
    public KeyBinding keyActivateArmFrontLAttachment = new KeyBinding("key.signalindustries.armFrontLActivate",0);

    @Unique
    public KeyBinding keyActivateArmFrontRAttachment = new KeyBinding("key.signalindustries.armFrontRActivate",0);

    @Unique
    public KeyBinding keyActivateArmSideLAttachment = new KeyBinding("key.signalindustries.armSideLActivate",0);

    @Unique
    public KeyBinding keyActivateArmSideRAttachment = new KeyBinding("key.signalindustries.armSideRActivate",0);

    @Unique
    public KeyBinding keyActivateArmBackLAttachment = new KeyBinding("key.signalindustries.armBackLActivate",0);

    @Unique
    public KeyBinding keyActivateArmBackRAttachment = new KeyBinding("key.signalindustries.armBackRActivate",0);

    @Unique
    public KeyBinding keyActivateLegSideLAttachment = new KeyBinding("key.signalindustries.legSideLActivate",0);

    @Unique
    public KeyBinding keyActivateLegSideRAttachment = new KeyBinding("key.signalindustries.legSideRActivate",0);

    @Unique
    public KeyBinding keyActivateBootBackLAttachment = new KeyBinding("key.signalindustries.bootBackLActivate",0);

    @Unique
    public KeyBinding keyActivateBootBackRAttachment = new KeyBinding("key.signalindustries.bootBackRActivate",0);

    @Override
    public KeyBinding signalIndustries$getKeyActivateHeadTopAttachment() {
        return keyActivateHeadTopAttachment;
    }

    @Override
    public KeyBinding signalIndustries$getKeyActivateCoreBackAttachment() {
        return keyActivateCoreBackAttachment;
    }

    @Override
    public KeyBinding signalIndustries$getKeyActivateArmFrontLAttachment() {
        return keyActivateArmFrontLAttachment;
    }

    @Override
    public KeyBinding signalIndustries$getKeyActivateArmFrontRAttachment() {
        return keyActivateArmFrontRAttachment;
    }

    @Override
    public KeyBinding signalIndustries$getKeyActivateArmSideLAttachment() {
        return keyActivateArmSideLAttachment;
    }

    @Override
    public KeyBinding signalIndustries$getKeyActivateArmSideRAttachment() {
        return keyActivateArmSideRAttachment;
    }

    @Override
    public KeyBinding signalIndustries$getKeyActivateArmBackLAttachment() {
        return keyActivateArmBackLAttachment;
    }

    @Override
    public KeyBinding signalIndustries$getKeyActivateArmBackRAttachment() {
        return keyActivateArmBackRAttachment;
    }

    @Override
    public KeyBinding signalIndustries$getKeyActivateLegSideLAttachment() {
        return keyActivateLegSideLAttachment;
    }

    @Override
    public KeyBinding signalIndustries$getKeyActivateLegSideRAttachment() {
        return keyActivateLegSideRAttachment;
    }

    @Override
    public KeyBinding signalIndustries$getKeyActivateBootBackLAttachment() {
        return keyActivateBootBackLAttachment;
    }

    @Override
    public KeyBinding signalIndustries$getKeyActivateBootBackRAttachment() {
        return keyActivateBootBackRAttachment;
    }

    @Override
    public KeyBinding signalIndustries$getKeyOpenSuit() {
        return keyOpenSuit;
    }

    @Override
    public KeyBinding signalIndustries$getKeyActivateAbility() {
        return keyActivateAbility;
    }

    @Override
    public KeyBinding signalIndustries$getKeySwitchMode() {
        return keySwitchMode;
    }
}
