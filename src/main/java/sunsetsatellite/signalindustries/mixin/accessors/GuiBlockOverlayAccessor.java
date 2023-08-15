package sunsetsatellite.signalindustries.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import toufoumaster.btwaila.gui.GuiBlockOverlay;

@Mixin(
        value = GuiBlockOverlay.class,
        remap = false
)
public interface GuiBlockOverlayAccessor {

    @Accessor
    int getPosX();

    @Accessor
    int getOffY();
}
