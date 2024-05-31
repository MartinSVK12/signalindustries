package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.catalyst.multiblocks.HologramWorld;
import sunsetsatellite.catalyst.multiblocks.IColorOverride;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.ArrayList;


public class RenderFluidInBlock extends TileEntityRenderer<TileEntity> {
    private RenderBlocks blockRenderer;

    public RenderFluidInBlock() {
    }

    public void doRender(Tessellator tessellator, TileEntity tileEntity1, double d2, double d4, double d6, float f8) {

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
            Block block = Block.blocksList[fluidId];
            blockRenderer = new RenderBlocks(new HologramWorld((ArrayList<BlockInstance>) SignalIndustries.listOf(new BlockInstance(block,new Vec3i(),0,null))));
            BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(block);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d2, (float)d4, (float)d6);
            GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(0.98F, amount, 0.98F);
            GL11.glTranslatef(0.51F, 0.50F, 0.51F);
            GL11.glDisable(2896);
            if(block == Block.fluidWaterFlowing || block == Block.fluidWaterStill){
                ((IColorOverride)model).overrideColor(0,0.5f,1,0.75f);
            }
            this.drawBlock(tessellator, model, 0);
            GL11.glEnable(2896);
            ((IColorOverride)model).overrideColor(1,1,1,1f);
            GL11.glPopMatrix();
        }

    }

    public void drawBlock(Tessellator tessellator, BlockModel<?> model, int meta) {
        TextureRegistry.blockAtlas.bindTexture();
        GL11.glPushMatrix();
        RenderBlocks renderBlocks = BlockModel.renderBlocks;
        BlockModel.setRenderBlocks(blockRenderer);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        model.renderBlockOnInventory(tessellator,meta,1,0.75f);
        BlockModel.setRenderBlocks(renderBlocks);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }
}
