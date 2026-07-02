package sunsetsatellite.signalindustries.powersuit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.EntityRendererItem;
import net.minecraft.client.render.font.FontRenderer;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Color;
import net.minecraft.core.util.helper.LightIndexHelper;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.signalindustries.SIKeybinds;
import sunsetsatellite.signalindustries.abilities.powersuit.SuitBaseAbility;
import sunsetsatellite.signalindustries.abilities.powersuit.SuitBaseEffectAbility;
import sunsetsatellite.signalindustries.interfaces.IApplicationItem;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithAbility;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithUtility;
import sunsetsatellite.signalindustries.util.ApplicationType;
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

		int color = mode.getColor(0x40);//0x40808080;
		int color2 = mode.getColor();//0xFF808080;

		if (isBackgroundShown) {
			//top
			gui.drawGradientRect(0, 0, width, 16, color, color);
			gui.drawGradientRect(0, 16, width / 2 - 100, 20, color, color2);
			gui.drawGradientRect(width / 2 + 100, 16, width, 20, color, color2);
			gui.drawGradientRect(width / 2 - 100, 36, width / 2 + 100, 40, color, color2);
			gui.drawGradientRect(width / 2 - 102, 20, width / 2 - 100, 40, color2, color2);
			gui.drawGradientRect(width / 2 + 100, 20, width / 2 + 102, 40, color2, color2);

			gui.drawGradientRect(width / 2 - 100, 16, width / 2 + 100, 36, color, color);
		}

		if (module == null) {
			gui.drawStringCenteredShadow(fontRenderer, String.format("%s", "No module."), width / 2, 25, color2);
		} else {
			if (module.contents[selectedApplicationSlot] != null) {
				IApplicationItem<?> app = (IApplicationItem<?>) module.contents[selectedApplicationSlot].getItem();
				if (app.getType() == ApplicationType.ABILITY) {
					SuitBaseAbility selectedAbility = ((ItemWithAbility) module.contents[selectedApplicationSlot].getItem()).getApplication();
					I18n t = I18n.getInstance();
					if (selectedAbility instanceof SuitBaseEffectAbility) {
						if (effectTimes.containsKey(selectedAbility)) {
							gui.drawStringCenteredShadow(fontRenderer,String.format("%s | %s | %s", selectedAbility.tier.getTextColor() + t.translateKey(selectedAbility.name) + TextFormatting.WHITE, TextFormatting.RED + "-" + selectedAbility.cost + TextFormatting.WHITE, TextFormatting.LIME + String.valueOf(effectTimes.get(selectedAbility) / 20) + "s"), width / 2, 25, color2);
						} else if (cooldowns.containsKey(selectedAbility)) {
							gui.drawStringCenteredShadow(fontRenderer,String.format("%s | %s | %s", selectedAbility.tier.getTextColor() + t.translateKey(selectedAbility.name) + TextFormatting.WHITE, TextFormatting.RED + "-" + selectedAbility.cost + TextFormatting.WHITE, TextFormatting.RED + String.valueOf(cooldowns.get(selectedAbility) / 20) + "s"), width / 2, 25, color2);
						} else {
							gui.drawStringCenteredShadow(fontRenderer,String.format("%s | %s | %s", selectedAbility.tier.getTextColor() + t.translateKey(selectedAbility.name) + TextFormatting.WHITE, TextFormatting.RED + "-" + selectedAbility.cost + TextFormatting.WHITE, TextFormatting.LIME + "READY"), width / 2, 25, color2);
						}
					} else {
						if (cooldowns.containsKey(selectedAbility)) {
							gui.drawStringCenteredShadow(fontRenderer,String.format("%s | %s | %s", selectedAbility.tier.getTextColor() + t.translateKey(selectedAbility.name) + TextFormatting.WHITE, TextFormatting.RED + "-" + selectedAbility.cost + TextFormatting.WHITE, TextFormatting.RED + String.valueOf(cooldowns.get(selectedAbility) / 20) + "s"), width / 2, 25, color2);
						} else {
							gui.drawStringCenteredShadow(fontRenderer,String.format("%s | %s | %s", selectedAbility.tier.getTextColor() + t.translateKey(selectedAbility.name) + TextFormatting.WHITE, TextFormatting.RED + "-" + selectedAbility.cost + TextFormatting.WHITE, TextFormatting.LIME + "READY"), width / 2, 25, color2);
						}
					}
				} else if (app.getType() == ApplicationType.UTILITY) {
					ItemWithUtility item = (ItemWithUtility) module.contents[selectedApplicationSlot].getItem();
					gui.drawStringCenteredShadow(fontRenderer,String.format("%s%s%s", item.getTier().getTextColor(), item.getTranslatedName(module.contents[selectedApplicationSlot]), TextFormatting.WHITE), width / 2, 25, color2);
				}
			} else {
				gui.drawStringCenteredShadow(fontRenderer,String.format("%s", "No application selected."), width / 2, 25, color2);
			}
		}

		if (isBackgroundShown) {
			//bottom
			gui.drawGradientRect(0, height - 20, width, height, color, color);
			gui.drawGradientRect(width / 2 - 170, height - 24, width / 2 - 100, height - 20, color2, color);
			gui.drawGradientRect(width / 2 + 100, height - 24, width, height - 20, color2, color);
			gui.drawGradientRect(width / 2 - 100, height - 44, width / 2 + 100, height - 40, color2, color);
			gui.drawGradientRect(width / 2 - 102, height - 44, width / 2 - 100, height - 24, color2, color2);
			gui.drawGradientRect(width / 2 + 100, height - 44, width / 2 + 102, height - 24, color2, color2);

			gui.drawGradientRect(width / 2 - 100, height - 40, width / 2 + 100, height - 20, color, color);

			//armor display
			gui.drawGradientRect(0, height - 74, width / 2 - 170, height - 70, color2, color);
			gui.drawGradientRect(width / 2 - 168, height - 24, width / 2 - 170, height - 74, color2, color2);
			gui.drawGradientRect(0, height - 74, width / 2 - 170, height - 20, color, color);
		}

		if (module == null) {
			GLRenderer.setColor4f(0.5F, 0.5F, 0.5F, 1.0F);
			mc.textureManager.loadTexture("/gui/gui.png").bind();
			int x = width / 2 - 91;
			int y = 0;
			gui.drawTexturedModalRect(x, y, 0, 0, 182, 22);
			//selected ability
			//GL11.glColor4f(1F, 0F, 0F, 1.0F);
			//drawUtil.drawTexturedModalRect(x - 1 + selectedAbilitySlot % 9 * 20, y - 1, 0, 22, 24, 22 + 2);
			//GL11.glBindTexture(3553, mc.renderEngine.getTexture("/gui/icons.png"));
		} else {
			Color c = new Color().setARGB(mode.getColor());
			GLRenderer.setColor4f((float) c.getRed() / 255, (float) c.getGreen() / 255, (float) c.getBlue() / 255, (float) c.getAlpha() / 255);
			mc.textureManager.loadTexture("/gui/gui.png").bind();
			int x = width / 2 - 91;
			int y = 0;
			gui.drawTexturedModalRect(x, y, 0, 0, 182, 22);
			int i = x;
			int j = y;
			for (int i1 = 0; i1 < module.contents.length; i1++) {
				ItemModel model = ItemModelDispatcher.getInstance().getDispatch(module.contents[i1]);
				model.renderGui(GLRenderer.getTessellator(), null, module.contents[i1], i + 3, j + 3, LightIndexHelper.lightIndex2i(15,15),0);
				i += 20;
			}
			//selected ability
			mc.textureManager.loadTexture("/gui/gui.png").bind();
			GLRenderer.setColor4f(1F, 1F, 1F, 1.0F);
			gui.drawTexturedModalRect(x - 1 + selectedApplicationSlot % 9 * 20, y - 1, 0, 22, 24, 22 + 2);
			mc.textureManager.loadTexture("/gui/icons.png").bind();
		}

		GLRenderer.setColor4f(1F, 1F, 1F, 1.0F);
		//draw armor pieces and attachments
		for (int i = 0; i < 4; i++) {
			ItemStack stack = this.player.inventory.armorInventory[3-i];
			if (stack != null) {
				int x = 2;
				int y = height - 64 + ((3 - i) * 16);
				ItemModel model = ItemModelDispatcher.getInstance().getDispatch(stack);
				model.renderGui(GLRenderer.getTessellator(), null, stack, x, y, LightIndexHelper.lightIndex2i(15,15),1.0F);
				InventoryPowerSuit pieceInv = getArmorPiece(HumanArmorShape.values()[3-i]);
				if (!pieceInv.isEmpty()) {
					int k = 16;
					for (ItemStack content : pieceInv.contents) {
						if (content != null) {
							model = ItemModelDispatcher.getInstance().getDispatch(content);
							model.renderGui(GLRenderer.getTessellator(), null, content, x + k, y, LightIndexHelper.lightIndex2i(15,15),1.0F);
							k += 16;
						}
					}
				}
			}
		}

		//render attachment info
		InventoryPowerSuit[] pieces = new InventoryPowerSuit[]{helmet, chestplate, leggings, boots};
		for (InventoryPowerSuit piece : pieces) {
			for (ItemStack content : piece.contents) {
				if (content != null) {
					if (content.getItem() instanceof IHasOverlay) {
						((IHasOverlay) content.getItem()).renderOverlay(guiIngame, player, height, width, mouseX, mouseY, gui, fontRenderer, itemRenderer);
					}
				}
			}
		}
	}

	@Override
	public void renderOverlay(ItemStack stack, IPowerSuit signalumPowerSuit, HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Gui gui, FontRenderer fontRenderer, EntityRendererItem itemRenderer) {

	}

	public void drawGradientRect(final int minX, final int minY, final int maxX, final int maxY, final int argb1, final int argb2, final int argb3, final int argb4 ) {
		final float a1 = (float) (argb1 >> 24 & 0xff) / 255F;
		final float r1 = (float) (argb1 >> 16 & 0xff) / 255F;
		final float g1 = (float) (argb1 >> 8 & 0xff) / 255F;
		final float b1 = (float) (argb1 & 0xff) / 255F;
		final float a2 = (float) (argb2 >> 24 & 0xff) / 255F;
		final float r2 = (float) (argb2 >> 16 & 0xff) / 255F;
		final float g2 = (float) (argb2 >> 8 & 0xff) / 255F;
		final float b2 = (float) (argb2 & 0xff) / 255F;
		final float a3 = (float) (argb3 >> 24 & 0xff) / 255F;
		final float r3 = (float) (argb3 >> 16 & 0xff) / 255F;
		final float g3 = (float) (argb3 >> 8 & 0xff) / 255F;
		final float b3 = (float) (argb3 & 0xff) / 255F;
		final float a4 = (float) (argb4 >> 24 & 0xff) / 255F;
		final float r4 = (float) (argb4 >> 16 & 0xff) / 255F;
		final float g4 = (float) (argb4 >> 8 & 0xff) / 255F;
		final float b4 = (float) (argb4 & 0xff) / 255F;
		GLRenderer.pushFrame();
		GLRenderer.setShader(Shaders.COLOR);
		GLRenderer.enableState(State.BLEND);
		GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
		final TessellatorGeneral tessellator = GLRenderer.getTessellator();
		tessellator.startDrawingQuads();
		tessellator.setColor4f(r1, g1, b1, a1);
		tessellator.addVertex(maxX, minY, 0.0D);
		tessellator.setColor4f(r3, g3, b3, a3);
		tessellator.addVertex(minX, minY, 0.0D);
		tessellator.setColor4f(r2, g2, b2, a2);
		tessellator.addVertex(minX, maxY, 0.0D);
		tessellator.setColor4f(r4, g4, b4, a4);
		tessellator.addVertex(maxX, maxY, 0.0D);
		tessellator.draw();
		GLRenderer.disableState(State.BLEND);
		GLRenderer.popFrame();
	}
}
