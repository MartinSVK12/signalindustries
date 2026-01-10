package sunsetsatellite.signalindustries.util;

import net.minecraft.client.render.Font;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.client.render.texture.meta.gui.GuiTextureProperties;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.lang.text.Text;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

public class DrawUtil {

    public DrawUtil() {
        zLevel = 0.0F;
    }

    public void drawLineHorizontal(int minX, int maxX, int minY, int argb) {
        if (maxX < minX) {
            int temp = minX;
            minX = maxX;
            maxX = temp;
        }
        drawRect(minX, minY, maxX + 1, minY + 1, argb);
    }

    public void drawLineHorizontalDouble(double minX, double maxX, double minY, int argb) {
        if (maxX < minX) {
            double temp = minX;
            minX = maxX;
            maxX = temp;
        }
        drawRectDouble(minX, minY, maxX + 1, minY + 1, argb);
    }

    public void drawLineVertical(int minX, int minY, int maxY, int argb) {
        if (maxY < minY) {
            int temp = minY;
            minY = maxY;
            maxY = temp;
        }
        drawRect(minX, minY + 1, minX + 1, maxY, argb);
    }

    public void drawLineVerticalDouble(double minX, double minY, double maxY, int argb) {
        if (maxY < minY) {
            double temp = minY;
            minY = maxY;
            maxY = temp;
        }
        drawRectDouble(minX, minY + 1, minX + 1, maxY, argb);
    }

    public void drawRect(int minX, int minY, int maxX, int maxY, int argb) {
        if (minX < maxX) {
            int temp = minX;
            minX = maxX;
            maxX = temp;
        }
        if (minY < maxY) {
            int temp = minY;
            minY = maxY;
            maxY = temp;
        }
        float a = (float) (argb >> 24 & 0xff) / 255F;
        float r = (float) (argb >> 16 & 0xff) / 255F;
        float g = (float) (argb >> 8 & 0xff) / 255F;
        float b = (float) (argb & 0xff) / 255F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL_BLEND);
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(r, g, b, a);
        tessellator.startDrawingQuads();
        tessellator.addVertex(minX, maxY, zLevel);
        tessellator.addVertex(maxX, maxY, zLevel);
        tessellator.addVertex(maxX, minY, zLevel);
        tessellator.addVertex(minX, minY, zLevel);
        tessellator.draw();
        GL11.glEnable(GL_TEXTURE_2D);
        GL11.glDisable(GL_BLEND);
    }

    public void drawRectDouble(double minX, double minY, double maxX, double maxY, int argb) {
        if (minX < maxX) {
            double temp = minX;
            minX = maxX;
            maxX = temp;
        }
        if (minY < maxY) {
            double temp = minY;
            minY = maxY;
            maxY = temp;
        }
        float a = (float) (argb >> 24 & 0xff) / 255F;
        float r = (float) (argb >> 16 & 0xff) / 255F;
        float g = (float) (argb >> 8 & 0xff) / 255F;
        float b = (float) (argb & 0xff) / 255F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL_BLEND);
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(r, g, b, a);
        tessellator.startDrawingQuads();
        tessellator.addVertex(minX, maxY, zLevel);
        tessellator.addVertex(maxX, maxY, zLevel);
        tessellator.addVertex(maxX, minY, zLevel);
        tessellator.addVertex(minX, minY, zLevel);
        tessellator.draw();
        GL11.glEnable(GL_TEXTURE_2D);
        GL11.glDisable(GL_BLEND);
    }

    public void drawBox(int minX, int minY, int maxX, int maxY, int argb, int lineThickness) {
        drawRect(minX, minY, maxX, minY + lineThickness, argb);
        drawRect(minX, maxY - lineThickness, maxX, maxY, argb);
        drawRect(minX, minY + lineThickness, minX + lineThickness, maxY - lineThickness, argb);
        drawRect(maxX - lineThickness, minY + lineThickness, maxX, maxY - lineThickness, argb);
    }

    public void drawRectNoBlend(int minX, int minY, int maxX, int maxY, int argb) {
        if (minX < maxX) {
            int temp = minX;
            minX = maxX;
            maxX = temp;
        }
        if (minY < maxY) {
            int temp = minY;
            minY = maxY;
            maxY = temp;
        }
        float a = (float) (argb >> 24 & 0xff) / 255F;
        float r = (float) (argb >> 16 & 0xff) / 255F;
        float g = (float) (argb >> 8 & 0xff) / 255F;
        float b = (float) (argb & 0xff) / 255F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glColor4f(r, g, b, a);
        tessellator.startDrawingQuads();
        tessellator.addVertex(minX, maxY, 0.0D);
        tessellator.addVertex(maxX, maxY, 0.0D);
        tessellator.addVertex(maxX, minY, 0.0D);
        tessellator.addVertex(minX, minY, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL_TEXTURE_2D);
    }

    public void drawRectWidthHeight(int x, int y, int width, int height, int argb) {
        float a = ((argb >> 24) & 0xFF) / 255f;
        float r = ((argb >> 16) & 0xFF) / 255f;
        float g = ((argb >> 8) & 0xFF) / 255f;
        float b = ((argb >> 0) & 0xFF) / 255f;
        glColor4f(r, g, b, a);
        glDisable(GL_TEXTURE_2D);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertex(x, y, 0);
        tessellator.addVertex(x, y + height, 0);
        tessellator.addVertex(x + width, y + height, 0);
        tessellator.addVertex(x + width, y, 0);
        tessellator.draw();
        glEnable(GL_TEXTURE_2D);
    }

    public void drawGradientRect(int minX, int minY, int maxX, int maxY, int argb1, int argb2) {
        float a1 = (float) (argb1 >> 24 & 0xff) / 255F;
        float r1 = (float) (argb1 >> 16 & 0xff) / 255F;
        float g1 = (float) (argb1 >> 8 & 0xff) / 255F;
        float b1 = (float) (argb1 & 0xff) / 255F;
        float a2 = (float) (argb2 >> 24 & 0xff) / 255F;
        float r2 = (float) (argb2 >> 16 & 0xff) / 255F;
        float g2 = (float) (argb2 >> 8 & 0xff) / 255F;
        float b2 = (float) (argb2 & 0xff) / 255F;
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glEnable(GL_BLEND);
        GL11.glDisable(GL_ALPHA_TEST);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(r1, g1, b1, a1);
        tessellator.addVertex(maxX, minY, 0.0D);
        tessellator.addVertex(minX, minY, 0.0D);
        tessellator.setColorRGBA_F(r2, g2, b2, a2);
        tessellator.addVertex(minX, maxY, 0.0D);
        tessellator.addVertex(maxX, maxY, 0.0D);
        tessellator.draw();
        GL11.glShadeModel(GL_FLAT);
        GL11.glDisable(GL_BLEND);
        GL11.glEnable(GL_ALPHA_TEST);
        GL11.glEnable(GL_TEXTURE_2D);
    }

    //color order is clockwise
    public void drawGradientRect(int x, int y, int w, int h, int color1, int color2, int color3, int color4) {
        float A1 = (color1 >> 24 & 0xFF) / 255.0F;
        float R1 = (color1 >> 16 & 0xFF) / 255.0F;
        float G1 = (color1 >> 8 & 0xFF) / 255.0F;
        float B1 = (color1 & 0xFF) / 255.0F;

        float A2 = (color2 >> 24 & 0xFF) / 255.0F;
        float R2 = (color2 >> 16 & 0xFF) / 255.0F;
        float G2 = (color2 >> 8 & 0xFF) / 255.0F;
        float B2 = (color2 & 0xFF) / 255.0F;

        float A3 = (color3 >> 24 & 0xFF) / 255.0F;
        float R3 = (color3 >> 16 & 0xFF) / 255.0F;
        float G3 = (color3 >> 8 & 0xFF) / 255.0F;
        float B3 = (color3 & 0xFF) / 255.0F;

        float A4 = (color4 >> 24 & 0xFF) / 255.0F;
        float R4 = (color4 >> 16 & 0xFF) / 255.0F;
        float G4 = (color4 >> 8 & 0xFF) / 255.0F;
        float B4 = (color4 & 0xFF) / 255.0F;
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(R1, G1, B1, A1);
        tessellator.addVertex(w, y, zLevel);
        tessellator.setColorRGBA_F(R3, G3, B3, A3);
        tessellator.addVertex(x, y, zLevel);
        tessellator.setColorRGBA_F(R2, G2, B2, A2);
        tessellator.addVertex(x, h, zLevel);
        tessellator.setColorRGBA_F(R4, G4, B4, A4);
        tessellator.addVertex(w, h, zLevel);
        tessellator.draw();
        GL11.glShadeModel(7424);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
    }

    public void drawTextCentered(Font fr, Text text, int x, int y, int argb) {
        fr.drawTextWithShadow(text, x - fr.getTextWidth(text) / 2, y, argb);
    }

    public void drawTextCenteredNoShadow(Font fr, Text text, int x, int y, int argb) {
        fr.drawText(text, x - fr.getTextWidth(text) / 2, y, argb);
    }

    public void drawText(Font fr, Text text, int x, int y, int argb) {
        fr.drawTextWithShadow(text, x, y, argb);
    }

    public void drawTextNoShadow(Font fr, Text text, int x, int y, int argb) {
        fr.drawText(text, x, y, argb);
    }

    public void drawStringCentered(Font fr, String string, int x, int y, int argb) {
        fr.drawStringWithShadow(string, x - fr.getStringWidth(string) / 2, y, argb);
    }

    public void drawStringCenteredNoShadow(Font fr, String string, int x, int y, int argb) {
        fr.drawString(string, x - fr.getStringWidth(string) / 2, y, argb);
    }

    public void drawString(Font fr, String string, int x, int y, int argb) {
        fr.drawStringWithShadow(string, x, y, argb);
    }

    public void drawStringNoShadow(Font fr, String string, int x, int y, int argb) {
        fr.drawString(string, x, y, argb);
    }

    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
        float uScale = 0.00390625F;
        float vScale = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, zLevel, (u) * uScale, (v + height) * vScale);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, (u + width) * uScale, (v + height) * vScale);
        tessellator.addVertexWithUV(x + width, y, zLevel, (u + width) * uScale, (v) * vScale);
        tessellator.addVertexWithUV(x, y, zLevel, (u) * uScale, (v) * vScale);
        tessellator.draw();
    }

    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, double uScale, double vScale) {
        final double off = 0.05d;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x - off, y + height + off, zLevel, (u) * uScale, (v + height) * vScale);
        tessellator.addVertexWithUV(x + width + off, y + height + off, zLevel, (u + width) * uScale, (v + height) * vScale);
        tessellator.addVertexWithUV(x + width + off, y - off, zLevel, (u + width) * uScale, (v) * vScale);
        tessellator.addVertexWithUV(x - off, y - off, zLevel, (u) * uScale, (v) * vScale);
        tessellator.draw();
    }

    public void drawTexturedModalRectDouble(double x, double y, double u, double v, double width, double height, double uScale, double vScale) {
        final double off = 0.05d;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x - off, y + height + off, zLevel, (u + 0) * uScale, (v + height) * vScale);
        tessellator.addVertexWithUV(x + width + off, y + height + off, zLevel, (u + width) * uScale, (v + height) * vScale);
        tessellator.addVertexWithUV(x + width + off, y - off, zLevel, (u + width) * uScale, (v + 0) * vScale);
        tessellator.addVertexWithUV(x - off, y - off, zLevel, (u + 0) * uScale, (v + 0) * vScale);
        tessellator.draw();
    }

    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, double uScale, double vScale, double off) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x - off, y + height + off, zLevel, (u) * uScale, (v + height) * vScale);
        tessellator.addVertexWithUV(x + width + off, y + height + off, zLevel, (u + width) * uScale, (v + height) * vScale);
        tessellator.addVertexWithUV(x + width + off, y - off, zLevel, (u + width) * uScale, (v) * vScale);
        tessellator.addVertexWithUV(x - off, y - off, zLevel, (u) * uScale, (v) * vScale);
        tessellator.draw();
    }

    public void drawTexturedModalRectDouble(double x, double y, double u, double v, double width, double height, double uScale, double vScale, double off) {
        final double foon = /*(0.25d * uScale)*/0;
        final double goon = /*(0.25d * vScale)*/0;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x - off, y + height + off, zLevel, (u + 0.5) * uScale, (v + height) * vScale);
        tessellator.addVertexWithUV(x + width + off, y + height + off, zLevel, (u + width - 0.5) * uScale, (v + height) * vScale);
        tessellator.addVertexWithUV(x + width + off, y - off, zLevel, (u + width - 0.5) * uScale, v * vScale);
        tessellator.addVertexWithUV(x - off, y - off, zLevel, (u + 0.5) * uScale, v * vScale);
        tessellator.draw();
    }


    public void drawTexturedModalRectDouble(double x, double y, double u, double v, double width, double height, double uWidth, double vHeight, double uScale, double vScale) {
        final double foon = (0.5f * uScale);
        final double goon = 0.0625f * (16.0f / uWidth);
        final double off = 0.05d;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x - off, y + height + off, zLevel, (u + foon) * uScale, (v + vHeight - foon) * vScale);
        tessellator.addVertexWithUV(x + width + off, y + height + off, zLevel, (u + uWidth - foon) * uScale, (v + vHeight - foon) * vScale);
        tessellator.addVertexWithUV(x + width + off, y - off, zLevel, (u + uWidth - foon) * uScale, (v + foon) * vScale);
        tessellator.addVertexWithUV(x - off, y - off, zLevel, (u + foon) * uScale, (v + foon) * vScale);
        tessellator.draw();
    }

    public void drawTexturedModalRect(double x, double y, int u, int v, int width, int height, int uvWidth, int uvHeight) {
        float uScale = 0.00390625F;
        float vScale = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, zLevel, (u) * uScale, (v + uvHeight) * vScale);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, (u + uvWidth) * uScale, (v + uvHeight) * vScale);
        tessellator.addVertexWithUV(x + width, y + 0, zLevel, (u + uvWidth) * uScale, (v) * vScale);
        tessellator.addVertexWithUV(x + 0, y + 0, zLevel, (u) * uScale, (v) * vScale);
        tessellator.draw();
    }

    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, int uvWidth, float scale) {
        float uScale = scale;
        float vScale = scale;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, zLevel, (u) * uScale, (v + uvWidth) * vScale);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, (u + uvWidth) * uScale, (v + uvWidth) * vScale);
        tessellator.addVertexWithUV(x + width, y, zLevel, (u + uvWidth) * uScale, (v) * vScale);
        tessellator.addVertexWithUV(x, y, zLevel, (u) * uScale, (v) * vScale);
        tessellator.draw();
    }

    public void drawTexturedIcon(int x, int y, int width, int height, IconCoordinate coordinate) {
        coordinate.parentAtlas.bind();
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, zLevel, coordinate.getIconUMin(), coordinate.getIconVMax());
        tessellator.addVertexWithUV(x + width, y + height, zLevel, coordinate.getIconUMax(), coordinate.getIconVMax());
        tessellator.addVertexWithUV(x + width, y, zLevel, coordinate.getIconUMax(), coordinate.getIconVMin());
        tessellator.addVertexWithUV(x, y, zLevel, coordinate.getIconUMin(), coordinate.getIconVMin());
        tessellator.draw();
    }

    public void drawGuiTexture(TextureManager re, int x, int y, int width, int height, String texture) {
        Texture tex = re.loadTexture(texture);
        tex.bind();

        Tessellator t = Tessellator.instance;
        if (tex.hasMeta("gui")) {
            GuiTextureProperties properties = Objects.requireNonNull(tex.getMeta("gui", GuiTextureProperties.class));
            switch (properties.type) {
                case GuiTextureProperties.TYPE_STRETCH: {
                    t.startDrawingQuads();
                    t.addVertexWithUV(x, y + height, zLevel, 0, 1);
                    t.addVertexWithUV(x + width, y + height, zLevel, 1, 1);
                    t.addVertexWithUV(x + width, y, zLevel, 1, 0);
                    t.addVertexWithUV(x, y, zLevel, 0, 0);
                    t.draw();
                    break;
                }
                case GuiTextureProperties.TYPE_TILE: {
                    double uScale = 1d / properties.width;
                    double vScale = 1d / properties.height;
                    t.startDrawingQuads();
                    t.addVertexWithUV(x, y + height, zLevel, 0, height * vScale);
                    t.addVertexWithUV(x + width, y + height, zLevel, width * uScale, height * vScale);
                    t.addVertexWithUV(x + width, y, zLevel, width * uScale, 0);
                    t.addVertexWithUV(x, y, zLevel, 0, 0);
                    t.draw();
                    break;
                }
                case GuiTextureProperties.TYPE_NINE_SLICE: {
                    double uScale = 1d / properties.width;
                    double vScale = 1d / properties.height;

                    int innerWidth = properties.width - (properties.border.left + properties.border.right);
                    int innerHeight = properties.height - (properties.border.top + properties.border.bottom);
                    int innerLeft = properties.border.left;
                    int innerTop = properties.border.top;
                    int innerRight = properties.width - properties.border.right;
                    int innerBottom = properties.height - properties.border.bottom;

                    int leftHeight = height - (properties.border.top + properties.border.bottom);
                    int repeatsY = leftHeight / innerHeight;
                    int remainderY = leftHeight % innerHeight;

                    int topWidth = width - (properties.border.left + properties.border.right);
                    int repeatsX = topWidth / innerWidth;
                    int remainderX = topWidth % innerWidth;

                    { // Inner
                        if (properties.stretchInner) { // Stretch Inner // TODO implement

                        } else { // Tile Inner
                            for (int j = 0; j < repeatsY; j++) {
                                drawTexturedModalRectDouble(
                                        x + innerLeft + repeatsX * innerWidth, y + innerTop + j * innerHeight,
                                        innerLeft + 1, innerTop,
                                        remainderX, innerHeight,
                                        uScale, vScale, 0.0); // Inner
                            }

                            for (int i = 0; i < repeatsX; i++) {
                                drawTexturedModalRectDouble(
                                        x + innerLeft + i * innerWidth, y + innerTop + repeatsY * innerHeight,
                                        innerLeft, innerTop,
                                        innerWidth, remainderY,
                                        uScale, vScale, 0.0); // Inner
                            }
                            drawTexturedModalRectDouble(
                                    x + innerLeft + repeatsX * innerWidth, y + innerTop + repeatsY * innerHeight,
                                    innerLeft, innerTop,
                                    remainderX, remainderY,
                                    uScale, vScale, 0.0); // Inner

                            for (int i = 0; i < repeatsX; i++) {
                                for (int j = 0; j < repeatsY; j++) {
                                    drawTexturedModalRectDouble(
                                            x + innerLeft + i * innerWidth, y + innerTop + j * innerHeight,
                                            innerLeft, innerTop,
                                            innerWidth, innerHeight,
                                            uScale, vScale, 0.0); // Inner
                                }
                            }
                        }
                    }

                    { // Left Right
                        for (int i = 0; i < repeatsY; i++) {
                            drawTexturedModalRectDouble(
                                    x, y + innerTop + i * innerHeight,
                                    0, innerTop,
                                    innerLeft, innerHeight,
                                    uScale, vScale, 0); // left

                            drawTexturedModalRectDouble(
                                    x + width - properties.border.right, y + innerTop + i * innerHeight,
                                    innerRight, innerTop,
                                    innerLeft, innerHeight,
                                    uScale, vScale, 0); // right
                        }

                        drawTexturedModalRectDouble(
                                x, y + innerTop + repeatsY * innerHeight,
                                0, innerTop,
                                innerLeft, remainderY,
                                uScale, vScale, 0); // left

                        drawTexturedModalRectDouble(
                                x + width - properties.border.right, y + innerTop + repeatsY * innerHeight,
                                innerRight, innerTop,
                                innerLeft, remainderY,
                                uScale, vScale, 0); // right
                    }
                    { // Top Bottom
                        for (int i = 0; i < repeatsX; i++) {
                            drawTexturedModalRectDouble(
                                    x + innerLeft + i * innerWidth, y,
                                    innerLeft, 0,
                                    innerWidth, properties.border.top,
                                    uScale, vScale, 0); // top

                            drawTexturedModalRectDouble(
                                    x + innerLeft + i * innerWidth, y + height - properties.border.bottom,
                                    innerLeft, innerBottom,
                                    innerWidth, properties.border.bottom,
                                    uScale, vScale, 0); // bottom
                        }

                        drawTexturedModalRectDouble(
                                x + innerLeft + repeatsX * innerWidth, y,
                                innerLeft, 0,
                                remainderX, properties.border.top,
                                uScale, vScale, 0); // top

                        drawTexturedModalRectDouble(
                                x + innerLeft + repeatsX * innerWidth, y + height - properties.border.bottom,
                                innerLeft, innerBottom,
                                remainderX, properties.border.bottom,
                                uScale, vScale, 0); // bottom
                    }

                    drawTexturedModalRectDouble(
                            x, y,
                            0, 0,
                            properties.border.left, properties.border.top,
                            uScale, vScale, 0); // top-left

                    drawTexturedModalRectDouble(
                            x + width - properties.border.right, y,
                            innerRight, 0,
                            properties.border.right, properties.border.top,
                            uScale, vScale, 0); // top-right

                    drawTexturedModalRectDouble(
                            x, y + height - properties.border.bottom,
                            0, innerBottom,
                            properties.border.left, properties.border.bottom,
                            uScale, vScale, 0); // bottom-left

                    drawTexturedModalRectDouble(
                            x + width - properties.border.right, y + height - properties.border.bottom,
                            innerRight, innerBottom,
                            properties.border.right, properties.border.bottom,
                            uScale, vScale, 0); // bottom-right

                    break;
                }
            }
        } else {
            t.startDrawingQuads();
            t.addVertexWithUV(x, y + height, zLevel, 0, 1);
            t.addVertexWithUV(x + width, y + height, zLevel, 1, 1);
            t.addVertexWithUV(x + width, y, zLevel, 1, 0);
            t.addVertexWithUV(x, y, zLevel, 0, 0);
            t.draw();
        }
    }

    public void drawGuiIcon(int x, int y, int width, int height, IconCoordinate coordinate) {
        drawGuiIconDouble(x, y, width, height, coordinate);
    }

    public void drawGuiIconDouble(double x, double y, double width, double height, IconCoordinate coordinate) {
        coordinate.parentAtlas.bind();
        Tessellator t = Tessellator.instance;
        if (coordinate.hasMeta("gui")) {
            GuiTextureProperties properties = Objects.requireNonNull(coordinate.getMeta("gui", GuiTextureProperties.class));
            switch (properties.type) {
                case GuiTextureProperties.TYPE_STRETCH: {
                    t.startDrawingQuads();
                    t.addVertexWithUV(x, y + height, zLevel, coordinate.getIconUMin(), coordinate.getIconVMax());
                    t.addVertexWithUV(x + width, y + height, zLevel, coordinate.getIconUMax(), coordinate.getIconVMax());
                    t.addVertexWithUV(x + width, y, zLevel, coordinate.getIconUMax(), coordinate.getIconVMin());
                    t.addVertexWithUV(x, y, zLevel, coordinate.getIconUMin(), coordinate.getIconVMin());
                    t.draw();
                    break;
                }
                case GuiTextureProperties.TYPE_TILE: {
                    int repeatsY = MathHelper.floor(height / properties.height);
                    double remainderY = height % properties.height;

                    int repeatsX = MathHelper.floor(width / properties.width);
                    double remainderX = width % properties.width;

                    double uScale = (double) coordinate.width / properties.width;
                    double vScale = (double) coordinate.height / properties.height;

                    for (int i = 0; i < repeatsX; i++) {
                        for (int j = 0; j < repeatsY; j++) {
                            drawIconTextureDouble(
                                    x + properties.width * i, y + properties.height * j,
                                    x + properties.width * (i + 1), y + properties.height * (j + 1),
                                    0, 0,
                                    properties.width * uScale, properties.height * vScale,
                                    coordinate);
                        }
                    }

                    for (int j = 0; j < repeatsY; j++) {
                        drawIconTextureDouble(
                                x + properties.width * repeatsX, y + properties.height * j,
                                x + properties.width * repeatsX + remainderX, y + properties.height * (j + 1),
                                0, 0,
                                remainderX * uScale, properties.height * vScale,
                                coordinate);
                    }

                    for (int i = 0; i < repeatsX; i++) {
                        drawIconTextureDouble(
                                x + properties.width * i, y + properties.height * repeatsY,
                                x + properties.width * (i + 1), y + properties.height * repeatsY + remainderY,
                                0, 0,
                                properties.width * uScale, remainderY * vScale,
                                coordinate);
                    }

                    drawIconTextureDouble(
                            x + properties.width * repeatsX, y + properties.height * repeatsY,
                            x + properties.width * repeatsX + remainderX, y + properties.height * repeatsY + remainderY,
                            0, 0,
                            remainderX * uScale, remainderY * vScale,
                            coordinate);

                    break;
                }
                case GuiTextureProperties.TYPE_NINE_SLICE: {
                    int innerWidth = properties.width - (properties.border.left + properties.border.right);
                    int innerHeight = properties.height - (properties.border.top + properties.border.bottom);
                    int innerLeft = properties.border.left;
                    int innerTop = properties.border.top;
                    int innerRight = properties.width - properties.border.right;
                    int innerBottom = properties.height - properties.border.bottom;

                    double uScale = (double) coordinate.width / properties.width;
                    double vScale = (double) coordinate.height / properties.height;

                    double leftHeight = height - (properties.border.top + properties.border.bottom);
                    int repeatsY = MathHelper.floor(leftHeight / innerHeight);
                    double remainderY = leftHeight % innerHeight;

                    double topWidth = width - (properties.border.left + properties.border.right);
                    int repeatsX = MathHelper.floor(topWidth / innerWidth);
                    double remainderX = topWidth % innerWidth;


                    { // Inner
                        if (properties.stretchInner) { // Stretch Inner // TODO implement
                            drawIconTextureDouble(
                                    x + innerLeft, y + innerTop,
                                    x + innerLeft + width - properties.border.right - properties.border.left, y + innerTop + height - properties.border.bottom - properties.border.top,
                                    innerLeft * uScale, innerTop * vScale,
                                    innerRight * uScale, innerBottom * vScale,
                                    coordinate);
                        } else {
                            for (int j = 0; j < repeatsY; j++) {
                                drawIconTextureDouble(
                                        x + innerLeft + repeatsX * innerWidth, y + innerTop + j * innerHeight,
                                        x + innerLeft + repeatsX * innerWidth + remainderX, y + innerTop + j * innerHeight + innerHeight,
                                        innerLeft * uScale, innerTop * vScale,
                                        (innerLeft + remainderX) * uScale, (innerTop + innerHeight) * vScale,
                                        coordinate);
                            }

                            for (int i = 0; i < repeatsX; i++) {
                                drawIconTextureDouble(
                                        x + innerLeft + i * innerWidth, y + innerTop + repeatsY * innerHeight,
                                        x + innerLeft + i * innerWidth + innerWidth, y + innerTop + repeatsY * innerHeight + remainderY,
                                        innerLeft * uScale, innerTop * vScale,
                                        (innerLeft + innerWidth) * uScale, (innerTop + remainderY) * vScale, coordinate);
                            }
                            drawIconTextureDouble(
                                    x + innerLeft + repeatsX * innerWidth, y + innerTop + repeatsY * innerHeight,
                                    x + innerLeft + repeatsX * innerWidth + remainderX, y + innerTop + repeatsY * innerHeight + remainderY,
                                    innerLeft * uScale, innerTop * vScale,
                                    (innerLeft + remainderX) * uScale, (innerTop + remainderY) * vScale, coordinate);

                            for (int i = 0; i < repeatsX; i++) {
                                for (int j = 0; j < repeatsY; j++) {
                                    final double xMin = (x + innerLeft) + i * innerWidth;
                                    final double yMin = (y + innerTop) + j * innerHeight;
                                    drawIconTextureDouble(
                                            xMin, yMin,
                                            xMin + innerWidth, yMin + innerHeight,
                                            innerLeft * uScale, innerTop * vScale,
                                            (innerLeft + innerWidth) * uScale, (innerTop + innerHeight) * vScale,
                                            coordinate);
                                }
                            }
                        }
                    }

                    { // Left Right
                        for (int i = 0; i < repeatsY; i++) {
                            drawIconTextureDouble(
                                    x, y + innerTop + i * innerHeight,
                                    x + innerLeft, y + innerTop + i * innerHeight + innerHeight,
                                    0, innerTop * vScale,
                                    innerLeft * uScale, (innerTop + innerHeight) * vScale,
                                    coordinate);

                            drawIconTextureDouble(
                                    x + width - properties.border.right, y + innerTop + i * innerHeight,
                                    x + width, y + innerTop + i * innerHeight + innerHeight,
                                    innerRight * uScale, innerTop * vScale,
                                    (innerRight + properties.border.right) * uScale, (innerTop + innerHeight) * vScale,
                                    coordinate);
                        }

                        drawIconTextureDouble(
                                x, y + innerTop + repeatsY * innerHeight,
                                x + innerLeft, y + innerTop + repeatsY * innerHeight + remainderY,
                                0, innerTop * vScale,
                                innerLeft * uScale, (innerTop + remainderY) * vScale,
                                coordinate);

                        drawIconTextureDouble(
                                x + width - properties.border.right, y + innerTop + repeatsY * innerHeight,
                                x + width, y + innerTop + repeatsY * innerHeight + remainderY,
                                innerRight * uScale, innerTop * vScale,
                                (innerRight + properties.border.right) * uScale, (innerTop + remainderY) * vScale,
                                coordinate);
                    }
                    { // Top Bottom
                        for (int i = 0; i < repeatsX; i++) {
                            drawIconTextureDouble(x + innerLeft + i * innerWidth, y,
                                    x + innerLeft + i * innerWidth + innerWidth, y + properties.border.top,
                                    innerLeft * uScale, 0,
                                    (innerLeft + innerWidth) * uScale, properties.border.top * vScale,
                                    coordinate);

                            drawIconTextureDouble(
                                    x + innerLeft + i * innerWidth, y + height - properties.border.bottom,
                                    x + innerLeft + i * innerWidth + innerWidth, y + height,
                                    innerLeft * uScale, innerBottom * vScale,
                                    (innerLeft + innerWidth) * uScale, (innerBottom + properties.border.bottom) * vScale,
                                    coordinate);
                        }

                        drawIconTextureDouble(
                                x + innerLeft + repeatsX * innerWidth, y,
                                x + innerLeft + repeatsX * innerWidth + remainderX, y + properties.border.top,
                                innerLeft * uScale, 0,
                                (innerLeft + remainderX) * uScale, properties.border.top * vScale,
                                coordinate);

                        drawIconTextureDouble(
                                x + innerLeft + repeatsX * innerWidth, y + height - properties.border.bottom,
                                x + innerLeft + repeatsX * innerWidth + remainderX, y + height,
                                innerLeft * uScale, innerBottom * vScale,
                                (innerLeft + remainderX) * uScale, (innerBottom + properties.border.bottom) * vScale,
                                coordinate);
                    }

                    drawIconTextureDouble(
                            x, y,
                            x + properties.border.left, y + properties.border.top,
                            0, 0,
                            properties.border.left * uScale, properties.border.top * vScale, coordinate);
                    drawIconTextureDouble(
                            x, y + height - properties.border.bottom,
                            x + properties.border.left, y + height,
                            0, innerBottom * vScale,
                            properties.border.left * uScale, (innerBottom + properties.border.bottom) * vScale, coordinate);

                    drawIconTextureDouble(
                            x + width - properties.border.right, y,
                            x + width, y + properties.border.top,
                            innerRight * uScale, 0,
                            (innerRight + properties.border.right) * uScale, properties.border.top * uScale, coordinate);
                    drawIconTextureDouble(
                            x + width - properties.border.right, y + height - properties.border.bottom,
                            x + width, y + height,
                            innerRight * uScale, innerBottom * vScale,
                            (innerRight + properties.border.right) * uScale, (innerBottom + properties.border.bottom) * vScale, coordinate);


                    break;
                }
            }
        } else {
            drawIconTextureDouble(x, y, x + width, y + height, 0, 0, coordinate.width, coordinate.height, coordinate);
        }
    }

    public void drawIconTexture(int x0, int y0, int x1, int y1, int u0, int v0, int u1, int v1, IconCoordinate coordinate) {
        coordinate.parentAtlas.bind();

        double realU0 = coordinate.getSubIconU(u0 / ((double) coordinate.width));
        double realU1 = coordinate.getSubIconU(u1 / ((double) coordinate.width));
        double realV0 = coordinate.getSubIconV(v0 / ((double) coordinate.height));
        double realV1 = coordinate.getSubIconV(v1 / ((double) coordinate.height));

//        System.out.printf("%s, %s, %s, %s\n", realU0, realU1, realV0, realV1);

        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(x0, y1, zLevel, realU0, realV1);
        t.addVertexWithUV(x1, y1, zLevel, realU1, realV1);
        t.addVertexWithUV(x1, y0, zLevel, realU1, realV0);
        t.addVertexWithUV(x0, y0, zLevel, realU0, realV0);
        t.draw();
    }

    public void drawIconTextureDouble(double x0, double y0, double x1, double y1, double u0, double v0, double u1, double v1, IconCoordinate coordinate) {
        coordinate.parentAtlas.bind();

        double realU0 = coordinate.getSubIconU(u0 / ((double) coordinate.width));
        double realU1 = coordinate.getSubIconU(u1 / ((double) coordinate.width));
        double realV0 = coordinate.getSubIconV(v0 / ((double) coordinate.height));
        double realV1 = coordinate.getSubIconV(v1 / ((double) coordinate.height));

//        System.out.printf("%s, %s, %s, %s\n", realU0, realU1, realV0, realV1);

        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(x0, y1, zLevel, realU0, realV1);
        t.addVertexWithUV(x1, y1, zLevel, realU1, realV1);
        t.addVertexWithUV(x1, y0, zLevel, realU1, realV0);
        t.addVertexWithUV(x0, y0, zLevel, realU0, realV0);
        t.draw();
    }

    public float zLevel;
}
