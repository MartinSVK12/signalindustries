package sunsetsatellite.signalindustries.powersuit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.EntityRendererItem;
import net.minecraft.client.render.font.FontRenderer;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.signalindustries.SIKeybinds;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.util.Tier;

public class SignalumPowerSuitClient extends SignalumPowerSuit implements IHasOverlay {
	public SignalumPowerSuitClient(Player player) {
		super(player);
	}

	@Override
	public void renderOverlay(HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Gui gui, FontRenderer fontRenderer, EntityRendererItem itemRenderer) {
		Minecraft mc = Minecraft.getMinecraft();
		boolean isBackgroundShown = SIKeybinds.showSuitBackground.isTrue();
		Tier mode = Tier.BASIC;
		if (!GameSettings.IMMERSIVE_MODE.drawOverlays()) {
			return;
		}
		KeyBinding openSuitKey = SIKeybinds.keyOpenSuit;
		if(!active){
			gui.drawStringCenteredShadow(fontRenderer, String.format("%s | Press [Shift+%s]", TextFormatting.GRAY + "OFFLINE" + TextFormatting.WHITE, openSuitKey.getKeyName()), width / 2, height - 64, 0xFFFFFFFF);
			return;
		}
		if(status == Status.NO_ENERGY){
			gui.drawStringCenteredShadow(fontRenderer, String.format("%s | %s %s/%s | Press [%s] | %s C", status, TextFormatting.RED + String.format("%.2f", getEnergyPercent()) + "%", "(" + getEnergy(), getMaxEnergy() + ")" + TextFormatting.WHITE, openSuitKey.getKeyName(), String.format("%.2f", temperature)), width / 2, height - 64, 0xFFFFFFFF);
			return;
		}
		gui.drawStringCenteredShadow(fontRenderer, String.format("%s | %s %s/%s | %s C", status.toString(), TextFormatting.RED + String.format("%.2f", getEnergyPercent()) + "%", "(" + getEnergy(), getMaxEnergy() + ")" + TextFormatting.WHITE, String.format("%.2f", temperature)), width / 2, height - 64, 0xFFFFFFFF);

	}

	@Override
	public void renderOverlay(ItemStack stack, IPowerSuit signalumPowerSuit, HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Gui gui, FontRenderer fontRenderer, EntityRendererItem itemRenderer) {

	}
}
