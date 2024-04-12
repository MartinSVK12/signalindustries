package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextureFX;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tag.ItemTags;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.RenderBlockSimple;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityEnergyInjector;

import java.util.Random;

public class RenderEnergyInjector extends TileEntityRenderer<TileEntityEnergyInjector> {

    private final RenderBlockSimple sideRenderer = new RenderBlockSimple();
    private final Random random = new Random();
    private final RenderBlocks renderBlocks = new RenderBlocks();
    private int counter = 0;

    @Override
    public void doRender(TileEntityEnergyInjector tileEntity, double x, double y, double z, float g) {
        counter++;
        if(counter >= 360){
            counter = 0;
        }
        //super.doRender(tileEntity, x, y, z, g);
        if(tileEntity.getStackInSlot(0) != null){
            EntityItem entityItem = new EntityItem(tileEntity.worldObj, tileEntity.x, tileEntity.y, tileEntity.z, tileEntity.getStackInSlot(0));
            doRenderItem(entityItem,x+0.5,y+0.5,z+0.5,0,0);
        }

    }

    public void doRenderItem(EntityItem entity, double d, double d1, double d2, float f, float f1) {
        this.random.setSeed(187L);
        ItemStack itemstack = entity.item;
        if (itemstack == null) {
            return;
        }
        Item item = itemstack.getItem();
        if (item == null) {
            return;
        }
        GL11.glPushMatrix();
        float f2 = 0;//MathHelper.sin(((float)entity.age + f1) / 10.0f + entity.field_804_d) * 0.1f + 0.1f;
        float f3 = counter;//(((float)entity.age + f1) / 20.0f + entity.field_804_d) * 57.29578f;
        int renderCount = 1;
        if (entity.item.stackSize > 1) {
            renderCount = 2;
        }
        if (entity.item.stackSize > 5) {
            renderCount = 3;
        }
        if (entity.item.stackSize > 20) {
            renderCount = 4;
        }
        GL11.glTranslatef((float)d, (float)d1 + f2, (float)d2);
        GL11.glEnable(32826);
        if (itemstack.itemID < Block.blocksList.length && Block.blocksList[itemstack.itemID] != null && BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[itemstack.itemID]).shouldItemRender3d()) {
            GL11.glRotatef(f3, 0.0f, 1.0f, 0.0f);
            this.loadTexture("/terrain.png");
            BlockModelRenderBlocks.setRenderBlocks(this.renderBlocks);
            BlockModel model = BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[itemstack.itemID]);
            float itemSize = model.getItemRenderScale();
            GL11.glScalef(itemSize, itemSize, itemSize);
            for (int j = 0; j < renderCount; ++j) {
                GL11.glPushMatrix();
                if (j > 0) {
                    float f5 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.2f / itemSize;
                    float f7 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.2f / itemSize;
                    float f9 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.2f / itemSize;
                    GL11.glTranslatef(f5, f7, f9);
                }
                float f4 = entity.getBrightness(f1);
                if (Minecraft.getMinecraft(this).fullbright) {
                    f4 = 1.0f;
                }
                this.renderBlocks.renderBlockOnInventory(Block.blocksList[itemstack.itemID], itemstack.getMetadata(), f4);
                GL11.glPopMatrix();
            }
        } else {
            int tileWidth;
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            int i = itemstack.getIconIndex();
            if (itemstack.itemID < Block.blocksList.length) {
                this.loadTexture("/terrain.png");
                tileWidth = TextureFX.tileWidthTerrain;
            } else {
                this.loadTexture("/gui/items.png");
                tileWidth = TextureFX.tileWidthItems;
            }
            Tessellator tessellator = Tessellator.instance;
            float f6 = (float)(i % Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth) / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
            float f8 = (float)(i % Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth + tileWidth) / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
            float f10 = (float)(i / Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth) / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
            float f11 = (float)(i / Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth + tileWidth) / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
            float f12 = 1.0f;
            float f13 = 0.5f;
            float f14 = 0.25f;
            if (true) {
                int k = 0xFFFFFF;//Item.itemsList[itemstack.itemID].getColorFromDamage(itemstack.getMetadata());
                float f15 = (float)(k >> 16 & 0xFF) / 255.0f;
                float f17 = (float)(k >> 8 & 0xFF) / 255.0f;
                float f19 = (float)(k & 0xFF) / 255.0f;
                float f21 = entity.getBrightness(f1);
                if (Minecraft.getMinecraft(this).fullbright || entity.item.getItem().hasTag(ItemTags.renderFullbright)) {
                    f21 = 1.0f;
                }
                GL11.glColor4f(f15 * f21, f17 * f21, f19 * f21, 1.0f);
            }
            if (Minecraft.getMinecraft(this).gameSettings.items3D.value) {
                GL11.glPushMatrix();
                GL11.glScaled(1.0, 1.0, 1.0);
                GL11.glRotated(f3, 0.0, 1.0, 0.0);
                GL11.glTranslated(-0.5, 0.0, -0.05 * (double)(renderCount - 1));
                for (int j = 0; j < renderCount; ++j) {
                    GL11.glPushMatrix();
                    GL11.glTranslated(0.0, 0.0, 0.1 * (double)j);
                    EntityRenderDispatcher.instance.itemRenderer.renderItem(entity, itemstack, false);
                    GL11.glPopMatrix();
                }
                GL11.glPopMatrix();
            } else {
                for (int l = 0; l < renderCount; ++l) {
                    GL11.glPushMatrix();
                    if (l > 0) {
                        float f16 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.3f;
                        float f18 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.3f;
                        float f20 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.3f;
                        GL11.glTranslatef(f16, f18, f20);
                    }
                    GL11.glRotatef(180.0f - this.renderDispatcher.viewLerpYaw, 0.0f, 1.0f, 0.0f);
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0.0f, 1.0f, 0.0f);
                    tessellator.addVertexWithUV(-0.5, -0.25, 0.0, f6, f11);
                    tessellator.addVertexWithUV(0.5, -0.25, 0.0, f8, f11);
                    tessellator.addVertexWithUV(0.5, 0.75, 0.0, f8, f10);
                    tessellator.addVertexWithUV(-0.5, 0.75, 0.0, f6, f10);
                    tessellator.draw();
                    GL11.glPopMatrix();
                }
            }
        }
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }
}
