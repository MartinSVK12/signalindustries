package sunsetsatellite.signalindustries.gui.guidebook.pages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookSection;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.core.lang.I18n;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.signalindustries.render.RenderMultiblockInGuidebook;

public class MultiblockPage
    extends GuidebookPage {
    public final Multiblock multiblock;


    public MultiblockPage(GuidebookSection section, Multiblock multiblock) {
        super(section);
        this.multiblock = multiblock;
    }

    @Override
    protected void renderBackground(RenderEngine re, int x, int y) {
        super.renderBackground(re,x,y);
    }

    @Override
    protected void renderForeground(RenderEngine re, FontRenderer fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if(multiblock != null){
            drawStringCenteredNoShadow(fr, I18n.getInstance().translateNameKey(multiblock.translateKey), x + 158 / 2, y + 10, 0x000000);
        } else {
            drawStringCenteredNoShadow(fr,"No results :(" ,x+width/2,y+height/2,0xFF808080);
        }

        renderMultiblock(re,fr,x, y, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onMouseDown(int x, int y, int mouseX, int mouseY, int button) {
        super.onMouseDown(x, y, mouseX, mouseY, button);
    }

    private void renderMultiblock(RenderEngine re, FontRenderer fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if(multiblock == null) return;

        float heightFactor = 50f / 1.8f;

        Minecraft mc = Minecraft.getMinecraft(this);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x  * mc.resolution.scale, y * mc.resolution.scale, 158 * mc.resolution.scale, 220 * mc.resolution.scale); //158, 220

        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(x + 68, y + 44 + 62, 50F);
        float f1 = 12F;
        GL11.glScalef(-f1, f1, f1);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        float f5 = (float)(x + 78) - mouseX;
        float f6 = (float)(y + 44 + 32) - (heightFactor * 1) - mouseY;
        GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        Lighting.enableLight();
        GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float)Math.atan(f6 / 40F) * 60F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-(float)Math.atan(f5 / 40F) * 80F,0,1F,0);
        GL11.glTranslatef(0.0F, 0, 0.0F);
        RenderMultiblockInGuidebook r = new RenderMultiblockInGuidebook();
        r.doRender(multiblock,re,fr,0,0,0,0);
        GL11.glPopMatrix();
        Lighting.disable();
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
