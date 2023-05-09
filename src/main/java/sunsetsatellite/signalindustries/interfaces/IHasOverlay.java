package sunsetsatellite.signalindustries.interfaces;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FontRenderer;

public interface IHasOverlay {
    void renderOverlay(FontRenderer fontrenderer, EntityPlayer player, int height, int width, int mouseX, int mouseY);
}
