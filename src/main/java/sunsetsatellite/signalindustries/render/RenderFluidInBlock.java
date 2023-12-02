package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.render.RenderFluid;


public class RenderFluidInBlock extends TileEntityRenderer<TileEntity> {
    private final RenderFluid blockRenderer = new RenderFluid();

    public RenderFluidInBlock() {
    }

    public void doRender(TileEntity tileEntity1, double d2, double d4, double d6, float f8) {
        float fluidAmount = 0.0F;
        float fluidMaxAmount = 1.0F;
        int fluidId = 0;
        if (Minecraft.getMinecraft(Minecraft.class).theWorld.isClientSide) {
            if (((TileEntityFluidContainer)tileEntity1).shownFluid != null) {
                fluidId = ((TileEntityFluidContainer)tileEntity1).shownFluid.getLiquid().id;
                fluidAmount = (float)((TileEntityFluidContainer)tileEntity1).shownFluid.amount;
                fluidMaxAmount = (float)((TileEntityFluidContainer)tileEntity1).shownMaxAmount;
            }
        } else if (((TileEntityFluidContainer)tileEntity1).fluidContents[0] != null && ((TileEntityFluidContainer)tileEntity1).fluidContents[0].getLiquid() != null) {
            fluidMaxAmount = (float)((TileEntityFluidContainer)tileEntity1).getFluidCapacityForSlot(0);
            fluidAmount = (float)((TileEntityFluidContainer)tileEntity1).fluidContents[0].amount;
            fluidId = ((TileEntityFluidContainer)tileEntity1).fluidContents[0].getLiquid().id;
        }

        float amount = Math.abs(fluidAmount / fluidMaxAmount - 0.02F);
        if (fluidId != 0) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d2, (float)d4, (float)d6);
            GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(0.98F, amount, 0.98F);
            GL11.glTranslatef(0.01F, 0.01F, 0.01F);
            GL11.glDisable(2896);
            this.drawBlock(this.getFontRenderer(), this.renderDispatcher.renderEngine.mc.renderEngine, fluidId, 0, 0, 0, 0, tileEntity1);
            GL11.glEnable(2896);
            GL11.glPopMatrix();
        }

    }

    public void drawBlock(FontRenderer fontrenderer, RenderEngine renderengine, int i, int j, int k, int l, int i1, TileEntity tile) {
        renderengine.bindTexture(renderengine.getTexture("/terrain.png"));
        Block f1 = Block.blocksList[i];
        GL11.glPushMatrix();
        this.blockRenderer.renderBlock(f1, j, renderengine.mc.theWorld, tile.x, tile.y, tile.z);
        GL11.glPopMatrix();
        GL11.glEnable(2884);
    }
}
