package sunsetsatellite.signalindustries.mixin.invokers;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(
        value = Gui.class,
        remap = false
)
public interface GuiInvoker {

    @Invoker
    void callDrawGradientRect(int minX, int minY, int maxX, int maxY, int argb1, int argb2);
}
