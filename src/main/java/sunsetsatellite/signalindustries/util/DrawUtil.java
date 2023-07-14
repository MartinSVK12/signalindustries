package sunsetsatellite.signalindustries.util;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

public class DrawUtil {
    public float zLevel = 0.0F;

    public void func_27100_a(int i, int j, int k, int l) {
        if (j < i) {
            int i1 = i;
            i = j;
            j = i1;
        }
        drawRect(i, k, j + 1, k + 1, l);
    }

    public void func_27099_b(int i, int j, int k, int l) {
        if (k < j) {
            int i1 = j;
            j = k;
            k = i1;
        }
        drawRect(i, j + 1, i + 1, k, l);
    }

    public void drawRect(int minX, int minY, int maxX, int maxY, int i1) {
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
        float f = (i1 >> 24 & 0xFF) / 255.0F;
        float f1 = (i1 >> 16 & 0xFF) / 255.0F;
        float f2 = (i1 >> 8 & 0xFF) / 255.0F;
        float f3 = (i1 & 0xFF) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f1, f2, f3, f);
        tessellator.startDrawingQuads();
        tessellator.addVertex(minX, maxY, 0.0D);
        tessellator.addVertex(maxX, maxY, 0.0D);
        tessellator.addVertex(maxX, minY, 0.0D);
        tessellator.addVertex(minX, minY, 0.0D);
        tessellator.draw();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public void drawRectBetter(int x, int y, int w, int h, int color) {
        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color >> 0 & 0xFF) / 255.0F;
        GL11.glColor4f(r, g, b, a);
        GL11.glDisable(3553);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertex(x, y, 0.0D);
        tessellator.addVertex(x, (y + h), 0.0D);
        tessellator.addVertex((x + w), (y + h), 0.0D);
        tessellator.addVertex((x + w), y, 0.0D);
        tessellator.draw();
        GL11.glEnable(3553);
    }

    public void drawGradientRect(int i, int j, int k, int l, int i1, int j1) {
        float f = (i1 >> 24 & 0xFF) / 255.0F;
        float f1 = (i1 >> 16 & 0xFF) / 255.0F;
        float f2 = (i1 >> 8 & 0xFF) / 255.0F;
        float f3 = (i1 & 0xFF) / 255.0F;
        float f4 = (j1 >> 24 & 0xFF) / 255.0F;
        float f5 = (j1 >> 16 & 0xFF) / 255.0F;
        float f6 = (j1 >> 8 & 0xFF) / 255.0F;
        float f7 = (j1 & 0xFF) / 255.0F;
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(k, j, zLevel);
        tessellator.addVertex(i, j, zLevel);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(i, l, zLevel);
        tessellator.addVertex(k, l, zLevel);
        tessellator.draw();
        GL11.glShadeModel(7424);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
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

    public void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawStringWithShadow(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }

    public void drawCenteredStringNoShadow(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }

    public void drawString(FontRenderer fontrenderer, String s, int x, int y, int color) {
        fontrenderer.drawStringWithShadow(s, x, y, color);
    }

    public void drawStringNoShadow(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawString(s, i, j, k);
    }

    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((x), (y + height), this.zLevel, ((u) * f), ((v + height) * f1));
        tessellator.addVertexWithUV((x + width), (y + height), this.zLevel, ((u + width) * f), ((v + height) * f1));
        tessellator.addVertexWithUV((x + width), (y), this.zLevel, ((u + width) * f), ((v) * f1));
        tessellator.addVertexWithUV((x), (y), this.zLevel, ((u) * f), ((v) * f1));
        tessellator.draw();
    }

    public void drawTexturedModalRect(double x, double y, int u, int v, int width, int height, int uvWidth, int uvHeight) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0.0D, y + height, this.zLevel, ((u) * f), ((v + uvHeight) * f1));
        tessellator.addVertexWithUV(x + width, y + height, this.zLevel, ((u + uvWidth) * f), ((v + uvHeight) * f1));
        tessellator.addVertexWithUV(x + width, y + 0.0D, this.zLevel, ((u + uvWidth) * f), ((v) * f1));
        tessellator.addVertexWithUV(x + 0.0D, y + 0.0D, this.zLevel, ((u) * f), ((v) * f1));
        tessellator.draw();
    }

    public void drawTexturedModalRect(int i, int j, int k, int l, int i1, int j1, int tileWidth, float factor) {
        float f = factor;
        float f1 = factor;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((i), (j + j1), this.zLevel, ((k) * f), ((l + tileWidth) * f1));
        tessellator.addVertexWithUV((i + i1), (j + j1), this.zLevel, ((k + tileWidth) * f), ((l + tileWidth) * f1));
        tessellator.addVertexWithUV((i + i1), (j), this.zLevel, ((k + tileWidth) * f), ((l) * f1));
        tessellator.addVertexWithUV((i), (j), this.zLevel, ((k) * f), ((l) * f1));
        tessellator.draw();
    }
}
