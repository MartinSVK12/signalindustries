package sunsetsatellite.signalindustries.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelBlock;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sunsetsatellite.catalyst.core.util.IFullbright;
import sunsetsatellite.catalyst.core.util.NumberUtil;


public class GuiRenderFakeItem extends Gui
{

    Minecraft mc;

    public GuiRenderFakeItem(Minecraft mc)
    {
        this.mc = mc;
    }

    public void render(ItemStack itemStack, int x, int y, boolean isSelected, Slot slot, boolean showAmount)
    {
        boolean hasDrawnSlotBackground = false;
        boolean discovered = true;
        Lighting.enableInventoryLight();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        if (slot != null) {
            discovered = slot.getIsDiscovered(this.mc.thePlayer);
            if(slot.getBackgroundIconId() != null) {
                IconCoordinate iconIndex = TextureRegistry.getTexture(slot.getBackgroundIconId());
                if (itemStack == null) {
                    GL11.glDisable(GL11.GL_LIGHTING);
                    this.drawTexturedIcon(x, y, 16, 16, iconIndex);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    hasDrawnSlotBackground = true;
                }
            }
        }

        if (!hasDrawnSlotBackground) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            if (itemStack != null) {
                BlockModel.setRenderBlocks(EntityRenderDispatcher.instance.itemRenderer.renderBlocksInstance);

                ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(itemStack.getItem());
                if(itemModel instanceof ItemModelBlock){
                    BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[itemStack.itemID]);
                    ((IFullbright)blockModel).enableFullbright();
                }
                ((IFullbright)itemModel).enableFullbright();
                itemModel.renderItemIntoGui(Tessellator.instance, this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, discovered ? 1.0F : 0.0F, 0.5F);
                if(showAmount){
                    itemModel.renderItemOverlayIntoGUI(Tessellator.instance, this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, itemStack.stackSize <= 1 ? null : NumberUtil.format(itemStack.stackSize), 0.5F);
                }
                ((IFullbright)itemModel).disableFullbright();
                if(itemModel instanceof ItemModelBlock){
                    BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[itemStack.itemID]);
                    ((IFullbright)blockModel).disableFullbright();
                }
            }

            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }

        if (isSelected) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            this.drawRect(x, y, x + 16, y + 16, 0x80FFFFFF);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        Lighting.disable();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void render(ItemStack itemStack, int x, int y, boolean isSelected)
    {
        render(itemStack, x, y, isSelected, null, false);
    }

    public void render(ItemStack itemStack, int x, int y)
    {
        render(itemStack, x, y, false);
    }
}
