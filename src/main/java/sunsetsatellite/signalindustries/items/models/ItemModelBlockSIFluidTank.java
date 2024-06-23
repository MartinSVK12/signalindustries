package sunsetsatellite.signalindustries.items.models;

import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelBlock;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.multiblocks.HologramWorld;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.ArrayList;

public class ItemModelBlockSIFluidTank extends ItemModelBlock {
    public ItemModelBlockSIFluidTank(ItemBlock itemBlock) {
        super(itemBlock);
    }

    @Override
    public void renderItemIntoGui(Tessellator tessellator, FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemStack, int x, int y, float brightness, float alpha) {
        if (itemStack != null) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2884);
            BlockFluid fluid = (BlockFluid) Block.blocksList[itemStack.getData().getCompound("Fluid").getShort("liquid")];
            int fluidAmount = itemStack.getData().getCompound("Fluid").getInteger("amount");

            BlockModel<?> model = ((BlockModel<?>)BlockModelDispatcher.getInstance().getDispatch(fluid));
            if (model!= null) {
                RenderBlocks blockRenderer = new RenderBlocks(new HologramWorld((ArrayList<BlockInstance>) SignalIndustries.listOf(new BlockInstance(fluid,new Vec3i(),0,null))));
                GL11.glBlendFunc(770, 771);
                TextureRegistry.blockAtlas.bindTexture();
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(x - 2), (float)(y + 3), -3.0F);
                GL11.glScalef(10.0F, 10.0F, 10.0F);
                GL11.glTranslatef(1.0F, 0.5F, 1.0F);
                GL11.glScalef(1.0F, 1.0F, -1.0F);
                GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                if (this.useColor) {
                    int color = this.getColor(itemStack);
                    float r = (float)(color >> 16 & 255) / 255.0F;
                    float g = (float)(color >> 8 & 255) / 255.0F;
                    float b = (float)(color & 255) / 255.0F;
                    GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
                } else {
                    GL11.glColor4f(brightness, brightness, brightness, alpha);
                }

                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                BlockModel.renderBlocks.useInventoryTint = this.useColor;
                RenderBlocks original = BlockModel.renderBlocks;
                BlockModel.setRenderBlocks(blockRenderer);
                model.renderBlockOnInventory(tessellator, 0, brightness, alpha, null);
                BlockModel.renderBlocks.useInventoryTint = true;
                BlockModel.setRenderBlocks(original);
                GL11.glPopMatrix();

            }
            GL11.glEnable(2884);
            GL11.glDisable(3042);
        }
        super.renderItemIntoGui(tessellator, fontrenderer, renderengine, itemStack, x, y, brightness, alpha);
    }
}
