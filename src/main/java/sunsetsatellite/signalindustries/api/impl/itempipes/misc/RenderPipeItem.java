//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sunsetsatellite.signalindustries.api.impl.itempipes.misc;

import java.util.Random;
import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class RenderPipeItem extends Render {
    private final RenderBlocks renderBlocks = new RenderBlocks();
    private final Random random = new Random();
    public boolean field_27004_a = true;

    public RenderPipeItem() {
        this.shadowSize = 0.15F;
        this.field_194_c = 0.75F;
    }

    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
        if(entity instanceof EntityPipeItem && ((EntityPipeItem) entity).canRender()){
            this.doRenderItem((EntityPipeItem) entity, d, d1, d2, f, f1);
        }
    }

    public void doRenderItem(EntityPipeItem entityitem, double d, double d1, double d2, float f, float f1) {
        this.random.setSeed(187L);
        ItemStack itemstack = entityitem.item;
        GL11.glPushMatrix();
        float f2 = 0;//MathHelper.sin(((float)entityitem.age + f1) / 10.0F + entityitem.field_804_d) * 0.1F + 0.1F;
        float f3 = 0;//(((float)entityitem.age + f1) / 20.0F + entityitem.field_804_d) * 57.29578F;
        byte renderCount = 1;
        if (entityitem.item.stackSize > 1) {
            renderCount = 2;
        }

        if (entityitem.item.stackSize > 5) {
            renderCount = 3;
        }

        if (entityitem.item.stackSize > 20) {
            renderCount = 4;
        }

        GL11.glTranslatef((float)d, (float)d1 + f2, (float)d2);
        GL11.glEnable(32826);
        int tileWidth;
        float f6;
        float f8;
        float f10;
        if (itemstack.itemID < Block.blocksList.length && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())) {
            GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
            this.loadTexture("/terrain.png");
            float itemSize = 0.25F;
            tileWidth = Block.blocksList[itemstack.itemID].getRenderType();
            if (tileWidth == 1 || tileWidth == 19 || tileWidth == 12 || tileWidth == 2) {
                itemSize = 0.5F;
            }

            GL11.glScalef(itemSize, itemSize, itemSize);

            for(int j = 0; j < renderCount; ++j) {
                GL11.glPushMatrix();
                if (j > 0) {
                    f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / itemSize;
                    f8 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / itemSize;
                    f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / itemSize;
                    GL11.glTranslatef(f6, f8, f10);
                }

                f6 = entityitem.getEntityBrightness(f1);
                if (Minecraft.getMinecraft().fullbright) {
                    f6 = 1.0F;
                }

                this.renderBlocks.renderBlockOnInventory(Block.blocksList[itemstack.itemID], itemstack.getMetadata(), f6);
                GL11.glPopMatrix();
            }
        } else {
            GL11.glTranslatef(0.0f,-0.25f,0.0f);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            int i = itemstack.getIconIndex();
            if (itemstack.itemID < Block.blocksList.length) {
                this.loadTexture("/terrain.png");
                tileWidth = TextureFX.tileWidthTerrain;
            } else {
                this.loadTexture("/gui/items.png");
                tileWidth = TextureFX.tileWidthItems;
            }

            Tessellator tessellator = Tessellator.instance;
            f6 = (float)(i % net.minecraft.shared.Minecraft.TEXTURE_ATLAS_WIDTH_TILES * tileWidth) / (float)(net.minecraft.shared.Minecraft.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
            f8 = (float)(i % net.minecraft.shared.Minecraft.TEXTURE_ATLAS_WIDTH_TILES * tileWidth + tileWidth) / (float)(net.minecraft.shared.Minecraft.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
            f10 = (float)(i / net.minecraft.shared.Minecraft.TEXTURE_ATLAS_WIDTH_TILES * tileWidth) / (float)(net.minecraft.shared.Minecraft.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
            float f11 = (float)(i / net.minecraft.shared.Minecraft.TEXTURE_ATLAS_WIDTH_TILES * tileWidth + tileWidth) / (float)(net.minecraft.shared.Minecraft.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
            float f12 = 1.0F;
            float f13 = 0.5F;
            float f14 = 0.25F;
            int j;
            float f16;
            float f18;
            float f20;
            if (this.field_27004_a) {
                j = Item.itemsList[itemstack.itemID].getColorFromDamage(itemstack.getMetadata());
                f16 = (float)(j >> 16 & 255) / 255.0F;
                f18 = (float)(j >> 8 & 255) / 255.0F;
                f20 = (float)(j & 255) / 255.0F;
                float f21 = entityitem.getEntityBrightness(f1);
                if (Minecraft.getMinecraft().fullbright) {
                    f21 = 1.0F;
                }

                GL11.glColor4f(f16 * f21, f18 * f21, f20 * f21, 1.0F);
            }

            if (Minecraft.getMinecraft().gameSettings.items3D.value) {
                GL11.glPushMatrix();
                GL11.glScaled(1.0, 1.0, 1.0);
                GL11.glRotated(f3, 0.0, 1.0, 0.0);
                GL11.glTranslated(-0.5, 0.0, -0.05 * (double)(renderCount - 1));

                for(j = 0; j < renderCount; ++j) {
                    GL11.glPushMatrix();
                    GL11.glTranslated(0.0, 0.0, 0.1 * (double)j);
                    RenderManager.instance.itemRenderer.renderItem(entityitem, itemstack, false);
                    GL11.glPopMatrix();
                }

                GL11.glPopMatrix();
            } else {
                for(j = 0; j < renderCount; ++j) {
                    GL11.glPushMatrix();
                    if (j > 0) {
                        f16 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                        f18 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                        f20 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                        GL11.glTranslatef(f16, f18, f20);
                    }

                    GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    tessellator.startDrawingQuads();
                    tessellator.setNormal(0.0F, 1.0F, 0.0F);
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

    public void drawItemIntoGui(FontRenderer fontrenderer, RenderEngine renderengine, int i, int j, int k, int l, int i1) {
        this.drawItemIntoGui(fontrenderer, renderengine, i, j, k, l, i1, 1.0F, 1.0F);
    }

    public void drawItemIntoGui(FontRenderer fontrenderer, RenderEngine renderengine, int i, int j, int k, int l, int i1, float brightness, float alpha) {
        float f1;
        float f3;
        if (i < Block.blocksList.length && RenderBlocks.renderItemIn3d(Block.blocksList[i].getRenderType())) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            renderengine.bindTexture(renderengine.getTexture("/terrain.png"));
            Block block = Block.blocksList[i];
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(l - 2), (float)(i1 + 3), -3.0F);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            int l1 = Item.itemsList[i].getColorFromDamage(j);
            f1 = (float)(l1 >> 16 & 255) / 255.0F;
            f3 = (float)(l1 >> 8 & 255) / 255.0F;
            float f5 = (float)(l1 & 255) / 255.0F;
            if (this.field_27004_a) {
                GL11.glColor4f(f1 * brightness, f3 * brightness, f5 * brightness, alpha);
            } else {
                GL11.glColor4f(brightness, brightness, brightness, alpha);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            this.renderBlocks.useInventoryTint = this.field_27004_a;
            this.renderBlocks.renderBlockOnInventory(block, j, brightness);
            this.renderBlocks.useInventoryTint = true;
            GL11.glPopMatrix();
            GL11.glDisable(3042);
        } else if (k >= 0) {
            GL11.glDisable(2896);
            int tileWidth;
            if (i < Block.blocksList.length) {
                renderengine.bindTexture(renderengine.getTexture("/terrain.png"));
                tileWidth = TextureFX.tileWidthTerrain;
            } else {
                renderengine.bindTexture(renderengine.getTexture("/gui/items.png"));
                tileWidth = TextureFX.tileWidthItems;
            }

            int k1 = Item.itemsList[i].getColorFromDamage(j);
            float f = (float)(k1 >> 16 & 255) / 255.0F;
            f1 = (float)(k1 >> 8 & 255) / 255.0F;
            f3 = (float)(k1 & 255) / 255.0F;
            if (this.field_27004_a) {
                GL11.glColor4f(f * brightness, f1 * brightness, f3 * brightness, alpha);
            } else {
                GL11.glColor4f(brightness, brightness, brightness, alpha);
            }

            this.renderTexturedQuad(l, i1, k % net.minecraft.shared.Minecraft.TEXTURE_ATLAS_WIDTH_TILES * tileWidth, k / net.minecraft.shared.Minecraft.TEXTURE_ATLAS_WIDTH_TILES * tileWidth, tileWidth, tileWidth);
            GL11.glEnable(2896);
        }

        GL11.glEnable(2884);
    }

    public void renderItemIntoGUI(FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j, float alpha) {
        if (itemstack != null) {
            this.drawItemIntoGui(fontrenderer, renderengine, itemstack.itemID, itemstack.getMetadata(), itemstack.getIconIndex(), i, j, 1.0F, alpha);
        }
    }

    public void renderItemIntoGUI(FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j, float brightness, float alpha) {
        if (itemstack != null) {
            this.drawItemIntoGui(fontrenderer, renderengine, itemstack.itemID, itemstack.getMetadata(), itemstack.getIconIndex(), i, j, brightness, alpha);
        }
    }

    public void renderItemOverlayIntoGUI(FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j, float alpha) {
        if (itemstack != null) {
            if (itemstack.stackSize != 1) {
                String s = String.valueOf(itemstack.stackSize);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                fontrenderer.drawStringWithShadow(s, i + 19 - 2 - fontrenderer.getStringWidth(s), j + 6 + 3, 16777215);
                GL11.glEnable(2896);
                GL11.glEnable(2929);
            }

            if (itemstack.isItemDamaged()) {
                int k = (int)Math.round(13.0 - (double)itemstack.getItemDamageForDisplay() * 13.0 / (double)itemstack.getMaxDamage());
                int l = (int)Math.round(255.0 - (double)itemstack.getItemDamageForDisplay() * 255.0 / (double)itemstack.getMaxDamage());
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glDisable(3553);
                Tessellator tessellator = Tessellator.instance;
                int i1 = 255 - l << 16 | l << 8;
                int j1 = (255 - l) / 4 << 16 | 16128;
                this.renderQuad(tessellator, i + 2, j + 13, 13, 2, 0);
                this.renderQuad(tessellator, i + 2, j + 13, 12, 1, j1);
                this.renderQuad(tessellator, i + 2, j + 13, k, 1, i1);
                GL11.glEnable(3553);
                GL11.glEnable(2896);
                GL11.glEnable(2929);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

        }
    }

    public void renderItemOverlayIntoGUI(FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j, boolean discovered) {
        if (itemstack != null) {
            if (itemstack.stackSize != 1 || !discovered) {
                String s = String.valueOf(itemstack.stackSize);
                if (!discovered) {
                    s = "?";
                }

                GL11.glDisable(2896);
                GL11.glDisable(2929);
                fontrenderer.drawStringWithShadow(s, i + 19 - 2 - fontrenderer.getStringWidth(s), j + 6 + 3, 16777215);
                GL11.glEnable(2896);
                GL11.glEnable(2929);
            }

            if (itemstack.isItemDamaged() && discovered) {
                int k = (int)Math.round(13.0 - (double)itemstack.getItemDamageForDisplay() * 13.0 / (double)itemstack.getMaxDamage());
                int l = (int)Math.round(255.0 - (double)itemstack.getItemDamageForDisplay() * 255.0 / (double)itemstack.getMaxDamage());
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glDisable(3553);
                Tessellator tessellator = Tessellator.instance;
                int i1 = 255 - l << 16 | l << 8;
                int j1 = (255 - l) / 4 << 16 | 16128;
                this.renderQuad(tessellator, i + 2, j + 13, 13, 2, 0);
                this.renderQuad(tessellator, i + 2, j + 13, 12, 1, j1);
                this.renderQuad(tessellator, i + 2, j + 13, k, 1, i1);
                GL11.glEnable(3553);
                GL11.glEnable(2896);
                GL11.glEnable(2929);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

        }
    }

    private void renderQuad(Tessellator tessellator, int i, int j, int k, int l, int i1) {
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(i1);
        tessellator.addVertex(i, j, 0.0);
        tessellator.addVertex(i, j + l, 0.0);
        tessellator.addVertex(i + k, j + l, 0.0);
        tessellator.addVertex(i + k, j, 0.0);
        tessellator.draw();
    }

    public void renderTexturedQuad(int x, int y, int tileX, int tileY, int tileWidth, int tileHeight) {
        float f = 0.0F;
        float f1 = 1.0F / (float)(net.minecraft.shared.Minecraft.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
        float f2 = 1.0F / (float)(net.minecraft.shared.Minecraft.TEXTURE_ATLAS_WIDTH_TILES * tileHeight);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + 16, 0.0, (float)(tileX) * f1, (float)(tileY + tileHeight) * f2);
        tessellator.addVertexWithUV(x + 16, y + 16, 0.0, (float)(tileX + tileWidth) * f1, (float)(tileY + tileHeight) * f2);
        tessellator.addVertexWithUV(x + 16, y, 0.0, (float)(tileX + tileWidth) * f1, (float)(tileY) * f2);
        tessellator.addVertexWithUV(x, y, 0.0, (float)(tileX) * f1, (float)(tileY) * f2);
        tessellator.draw();
    }
}
