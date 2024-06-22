package sunsetsatellite.signalindustries.util;



import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.tessellator.Tessellator;
import org.lwjgl.opengl.GL11;

public class DrawUtil {
    public float zLevel = 0.0F;

    public void func_27100_a(int minX, int maxX, int minY, int argb) {
        if (maxX < minX) {
            int temp = minX;
            minX = maxX;
            maxX = temp;
        }

        this.drawRect(minX, minY, maxX + 1, minY + 1, argb);
    }

    public void func_27099_b(int minX, int minY, int maxY, int argb) {
        if (maxY < minY) {
            int temp = minY;
            minY = maxY;
            maxY = temp;
        }

        this.drawRect(minX, minY + 1, minX + 1, maxY, argb);
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

        float a = (float)(argb >> 24 & 0xFF) / 255.0F;
        float r = (float)(argb >> 16 & 0xFF) / 255.0F;
        float g = (float)(argb >> 8 & 0xFF) / 255.0F;
        float b = (float)(argb & 0xFF) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(r, g, b, a);
        tessellator.startDrawingQuads();
        tessellator.addVertex(minX, maxY, 0.0);
        tessellator.addVertex(maxX, maxY, 0.0);
        tessellator.addVertex(maxX, minY, 0.0);
        tessellator.addVertex(minX, minY, 0.0);
        tessellator.draw();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
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

        float a = (float)(argb >> 24 & 0xFF) / 255.0F;
        float r = (float)(argb >> 16 & 0xFF) / 255.0F;
        float g = (float)(argb >> 8 & 0xFF) / 255.0F;
        float b = (float)(argb & 0xFF) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(3553);
        GL11.glColor4f(r, g, b, a);
        tessellator.startDrawingQuads();
        tessellator.addVertex(minX, maxY, 0.0);
        tessellator.addVertex(maxX, maxY, 0.0);
        tessellator.addVertex(maxX, minY, 0.0);
        tessellator.addVertex(minX, minY, 0.0);
        tessellator.draw();
        GL11.glEnable(3553);
    }

    public void drawRectWidthHeight(int x, int y, int width, int height, int argb) {
        float a = (float)(argb >> 24 & 0xFF) / 255.0F;
        float r = (float)(argb >> 16 & 0xFF) / 255.0F;
        float g = (float)(argb >> 8 & 0xFF) / 255.0F;
        float b = (float)(argb >> 0 & 0xFF) / 255.0F;
        GL11.glColor4f(r, g, b, a);
        GL11.glDisable(3553);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertex(x, y, 0.0);
        tessellator.addVertex(x, y + height, 0.0);
        tessellator.addVertex(x + width, y + height, 0.0);
        tessellator.addVertex(x + width, y, 0.0);
        tessellator.draw();
    }

    public void drawGradientRect(int minX, int minY, int maxX, int maxY, int argb1, int argb2) {
        float a1 = (float)(argb1 >> 24 & 0xFF) / 255.0F;
        float r1 = (float)(argb1 >> 16 & 0xFF) / 255.0F;
        float g1 = (float)(argb1 >> 8 & 0xFF) / 255.0F;
        float b1 = (float)(argb1 & 0xFF) / 255.0F;
        float a2 = (float)(argb2 >> 24 & 0xFF) / 255.0F;
        float r2 = (float)(argb2 >> 16 & 0xFF) / 255.0F;
        float g2 = (float)(argb2 >> 8 & 0xFF) / 255.0F;
        float b2 = (float)(argb2 & 0xFF) / 255.0F;
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(r1, g1, b1, a1);
        tessellator.addVertex(maxX, minY, 0.0);
        tessellator.addVertex(minX, minY, 0.0);
        tessellator.setColorRGBA_F(r2, g2, b2, a2);
        tessellator.addVertex(minX, maxY, 0.0);
        tessellator.addVertex(maxX, maxY, 0.0);
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

    public void drawStringCentered(FontRenderer fr, String string, int x, int y, int argb) {
        fr.drawStringWithShadow(string, x - fr.getStringWidth(string) / 2, y, argb);
    }

    public void drawStringCenteredNoShadow(FontRenderer fr, String string, int x, int y, int argb) {
        fr.drawString(string, x - fr.getStringWidth(string) / 2, y, argb);
    }

    public void drawString(FontRenderer fr, String string, int x, int y, int argb) {
        fr.drawStringWithShadow(string, x, y, argb);
    }

    public void drawStringNoShadow(FontRenderer fr, String string, int x, int y, int argb) {
        fr.drawString(string, x, y, argb);
    }

    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
        float uScale = 0.00390625F;
        float vScale = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(
                x, y + height, this.zLevel, (float)(u) * uScale, (float)(v + height) * vScale
        );
        tessellator.addVertexWithUV(
                x + width, y + height, this.zLevel, (float)(u + width) * uScale, (float)(v + height) * vScale
        );
        tessellator.addVertexWithUV(
                x + width, y, this.zLevel, (float)(u + width) * uScale, (float)(v) * vScale
        );
        tessellator.addVertexWithUV(x, y, this.zLevel, (float)(u) * uScale, (float)(v) * vScale);
        tessellator.draw();
    }

    public void drawTexturedModalRect(double x, double y, int u, int v, int width, int height, int uvWidth, int uvHeight) {
        float uScale = 0.00390625F;
        float vScale = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0.0, y + (double)height, this.zLevel, (float)(u) * uScale, (float)(v + uvHeight) * vScale);
        tessellator.addVertexWithUV(
                x + (double)width, y + (double)height, this.zLevel, (float)(u + uvWidth) * uScale, (float)(v + uvHeight) * vScale
        );
        tessellator.addVertexWithUV(x + (double)width, y + 0.0, this.zLevel, (float)(u + uvWidth) * uScale, (float)(v) * vScale);
        tessellator.addVertexWithUV(x + 0.0, y + 0.0, this.zLevel, (float)(u) * uScale, (float)(v) * vScale);
        tessellator.draw();
    }

    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, int uvWidth, float scale) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(
                x, y + height, this.zLevel, (float)(u) * scale, (float)(v + uvWidth) * scale
        );
        tessellator.addVertexWithUV(
                x + width, y + height, this.zLevel, (float)(u + uvWidth) * scale, (float)(v + uvWidth) * scale
        );
        tessellator.addVertexWithUV(
                x + width, y, this.zLevel, (float)(u + uvWidth) * scale, (float)(v) * scale
        );
        tessellator.addVertexWithUV(x, y, this.zLevel, (float)(u) * scale, (float)(v) * scale);
        tessellator.draw();
    }
}
