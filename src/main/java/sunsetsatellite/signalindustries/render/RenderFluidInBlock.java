package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;


public class RenderFluidInBlock extends TileEntityRenderer<TileEntity> {
    private RenderBlocks blockRenderer;

    public RenderFluidInBlock() {
    }

    public void doRender(Tessellator tessellator, TileEntity tileEntity1, double d2, double d4, double d6, float f8) {
        blockRenderer = new RenderBlocks(tileEntity1.worldObj);
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
            this.drawBlock(tessellator, fluidId, tileEntity1);
            GL11.glEnable(2896);
            GL11.glPopMatrix();
        }

    }

    public void drawBlock(Tessellator tessellator, int i, TileEntity tile) {
        Block block = Block.blocksList[i];
        GL11.glPushMatrix();
        this.blockRenderer.renderStandardBlock(tessellator, BlockModelDispatcher.getInstance().getDispatch(block),block,tile.x,tile.y,tile.z);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }
}
