package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class FakeItemElement extends Gui {
    public Minecraft mc;

    public FakeItemElement(Minecraft mc) {
        this.mc = mc;
    }

    public void render(ItemStack itemStack, int x, int y, boolean isSelected, Slot slot, boolean showAmount) {
        render(itemStack, x, y, isSelected, slot, showAmount, 0.5f);
    }

    public void render(ItemStack itemStack, int x, int y, boolean isSelected, Slot slot, boolean showAmount, float alpha) {
        boolean hasDrawnSlotBackground = false;
        boolean discovered = true;

        // Do setup
        Lighting.enableInventoryLight();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1, 1, 1, alpha);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        // Draw slot background
        if (slot != null) {
            discovered = slot.getIsDiscovered(mc.thePlayer);
            if (slot.getItemIcon() != null) {
                final IconCoordinate iconIndex = TextureRegistry.getTexture(slot.getItemIcon());
                if (itemStack == null) {
                    GL11.glDisable(GL11.GL_LIGHTING);
                    drawTexturedIcon(x, y, 16, 16, iconIndex);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    hasDrawnSlotBackground = true;
                }
            }

            if (slot.isLocked()) {
                GL11.glColor4f(1, 1, 1, 1);
                GL11.glDisable(GL11.GL_LIGHTING);
                final IconCoordinate iconCoordinate = TextureRegistry.getTexture("minecraft:gui/slot_locked");
                drawGuiIcon(x - 1, y - 1, 18, 18, iconCoordinate);
                GL11.glEnable(GL11.GL_LIGHTING);
            }
        }

        // Draw item
        if (!hasDrawnSlotBackground) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            if (itemStack != null) {
                BlockModel.setRenderBlocks(EntityRenderDispatcher.instance.itemRenderer.renderBlocksInstance);
                ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(itemStack.getItem());
                itemModel.renderItemIntoGui(Tessellator.instance, mc.font, mc.textureManager, itemStack, x, y, discovered ? 1.0f : 0.0f, alpha);
                if (showAmount) {
                    itemModel.renderItemOverlayIntoGUI(Tessellator.instance, mc.font, mc.textureManager, itemStack, x, y, discovered ? null : "?", alpha);
                }
            }
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }

        // Draw selection overlay
        if (isSelected) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            drawRect(x, y, x + 16, y + 16, 0x80FFFFFF);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        // Clean up
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        Lighting.disable();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void render(ItemStack itemStack, int x, int y, boolean isSelected) {
        render(itemStack, x, y, isSelected, null, false);
    }

    public void render(ItemStack itemStack, int x, int y) {
        render(itemStack, x, y, false);
    }
}
