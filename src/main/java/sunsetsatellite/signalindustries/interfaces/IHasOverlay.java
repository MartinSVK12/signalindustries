package sunsetsatellite.signalindustries.interfaces;

import net.minecraft.src.*;

public interface IHasOverlay {
    void renderOverlay(GuiIngame guiIngame, EntityPlayer player, int height, int width, int mouseX, int mouseY, FontRenderer fontRenderer, RenderItem itemRenderer);

}
