package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiRenderItem;
import net.minecraft.client.render.ItemRenderer;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sunsetsatellite.catalyst.core.util.IColorOverride;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityAssembler;

public class RenderAssemblerItemSprite3D extends TileEntityRenderer<TileEntityAssembler> {

    private final GuiRenderItem itemRenderer = new GuiRenderItem(Minecraft.getMinecraft(this));
    @Override
    public void doRender(Tessellator tessellator, TileEntityAssembler tileEntity, double x, double y, double z, float g) {
        GL11.glPushMatrix();
        float scale = 0.6666667F;
        float rot;
        int i16 = tileEntity.getMovedData();
        rot = 0.0F;
        if(i16 == 2) {
            rot = 180.0F;
        }

        if(i16 == 4) {
            rot = 90.0F;
        }

        if(i16 == 5) {
            rot = -90.0F;
        }

        GL11.glTranslatef((float)x + 0.5F, (float)y + 0.75F * scale, (float)z + 0.5F);
        GL11.glRotatef(-rot, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, -0.3125F, -0.4375F);

        GL11.glPushMatrix();
        GL11.glScalef(scale, -scale, -scale);
        GL11.glPopMatrix();
        rot = 0.016666668F * scale;
        GL11.glTranslatef(0.0F, 0.5F * scale, 0.07F * scale);
        GL11.glScalef(rot, -rot, rot);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * rot);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_LIGHTING);
        int color = 0xFFFFFFFF;
        ItemStack stack = null;
        String name = "";
        String amount = "";

        if(tileEntity.recipe != null) {
            stack = tileEntity.recipe.getOutput();
            name = stack.getDisplayName();
            //amount = ((Integer) tileEntity.storedAmount).toString();
        }
        int i14 = -6;
        GL11.glTranslatef(0,28,82);
        //getFontRenderer().drawString(name, - getFontRenderer().getStringWidth(name) / 2, i14 * 10 - 5, color);
        getFontRenderer().drawString(amount, - getFontRenderer().getStringWidth(amount) / 2, i14 * 10 + 64, color);
        GL11.glTranslatef(0,-28,-82);
        GL11.glScalef(2.5f,2.5f,0.3f);
        GL11.glTranslatef(0,0,240);
        drawItemStack(stack,-8, -8);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthMask(true);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }


    private void drawItemStack(ItemStack stack, int x, int y) {
        if(stack != null) {
            GL11.glPushMatrix();
            GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(stack.getItem());
            ((IColorOverride) itemModel).enableFullbright();
            ((IColorOverride) getFontRenderer()).enableFullbright();
            if(stack.itemID < 16384){
                BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(Block.getBlock(stack.itemID));
                ((IColorOverride) blockModel).enableFullbright();
            }
            itemRenderer.render(stack, x, y);
            ((IColorOverride) itemModel).disableFullbright();
            if(stack.itemID < 16384){
                BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(Block.getBlock(stack.itemID));
                ((IColorOverride) blockModel).disableFullbright();
            }
            ((IColorOverride) getFontRenderer()).disableFullbright();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glPopMatrix();
        }
    }
}