package sunsetsatellite.signalindustries.interfaces;


import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.entity.EntityRendererItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;

public interface IHasOverlay {
    void renderOverlay(HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Font fontRenderer, EntityRendererItem itemRenderer);

    void renderOverlay(ItemStack stack, IPowerSuit signalumPowerSuit, HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Font fontRenderer, EntityRendererItem itemRenderer);
}
