package sunsetsatellite.signalindustries.mixin;



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

    @Override
    public KeyBinding signalIndustries$getKeyOpenSuit() {
        return keyOpenSuit;
    }

    @Override
    public KeyBinding signalIndustries$getKeyActivateAbility() {
        return keyActivateAbility;
    }
}
