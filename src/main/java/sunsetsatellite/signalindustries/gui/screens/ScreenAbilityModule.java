package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.signalindustries.gui.menus.MenuAbilityModule;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.attachments.ItemAbilityModule;
import sunsetsatellite.signalindustries.util.InventorySerializer;
import sunsetsatellite.signalindustries.util.Tier;

public class ScreenAbilityModule extends ScreenFluid {

    public int slotIndex;
    public boolean isArmor;
    public ItemStack module;
    public Player player;

    public ScreenAbilityModule(ContainerInventory inventoryPlayer, int slotIndex, boolean isArmor) {
        super(new MenuAbilityModule(inventoryPlayer, slotIndex, isArmor));
        this.slotIndex = slotIndex;
        this.isArmor = isArmor;
        this.player = inventoryPlayer.player;
        if (isArmor) {
            this.module = ((IPlayerPowerSuit<?>) inventoryPlayer.player).getPowerSuit().getAttachmentClass(ItemAbilityModule.class);
        } else {
            this.module = inventoryPlayer.getItem(slotIndex);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        Tier tier = ((ItemAbilityModule) module.getItem()).getTier();
        int color = tier.getColor();
        int color2 = tier.getColor(0x40);
        drawGradientRect(i - 4, j - 4, i + xSize + 4, j, color, color2);
        drawGradientRect(i - 4, j + ySize, i + xSize + 4, j + ySize + 4, color2, color);
        drawGradientRect(i - 4, j - 4, i, j + ySize + 4, color2, color, color, color2);
        drawGradientRect(i + xSize, j - 4, i + xSize + 4, j + ySize + 4, color, color2, color2, color);
        drawGradientRect(i, j, i + xSize, j + ySize, color2, color2);
        //drawUtil.drawGradientRect(i,j,i+xSize,j+ySize,color,0xFF00FF00,0xFF00FF00,color);
        for (Slot inventorySlot : inventorySlots.slots) {
            int x = inventorySlot.x;
            int y = inventorySlot.y;
            //drawStringCentered(fontRenderer,String.valueOf(inventorySlot.id),i+x,j+y,color);
            drawGradientRect(i + x, j + y, i + x + 16, j + y + 16, tier.getColor(0xA0), tier.getColor(0xA0));
        }
    }

    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        String name = Catalyst.translateNameKey(fluidSlots.itemInventory.getNameTranslationKey());
        Tier tier = ((ItemAbilityModule) module.getItem()).getTier();
        if (module.getItem() instanceof ItemAbilityModule) {
            switch (tier) {
                case REINFORCED:
                    name = Catalyst.translateNameKey("container.signalindustries.abilityModule");
                    break;
                case AWAKENED:
                    name = Catalyst.translateNameKey("container.signalindustries.awakenedAbilityModule");
                    break;
            }
            drawStringCenteredShadow(fontRenderer, name, 90, 6, tier.getColor());
        }
    }

    @Override
    public void removed() {
        super.removed();
        if (isArmor) {
            this.module = ((IPlayerPowerSuit<?>) player).getPowerSuit().getAttachmentClass(ItemAbilityModule.class);
        } else {
            this.module = player.inventory.getItem(slotIndex);
        }
        InventorySerializer.saveInvToNBT(module, fluidSlots.itemInventory);
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
