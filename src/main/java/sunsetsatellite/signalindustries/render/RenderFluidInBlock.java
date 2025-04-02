package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.HologramWorld;
import sunsetsatellite.catalyst.core.util.model.IColorOverride;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidContainer;

import java.util.ArrayList;


public class RenderFluidInBlock extends TileEntityRenderer<TileEntity> {
    private RenderBlocks blockRenderer;
    private final EntityRendererFakeItem itemRenderer = new EntityRendererFakeItem();

    public RenderFluidInBlock() {
    }

    public void doRender(Tessellator tessellator, TileEntity tileEntity1, double d2, double d4, double d6, float f8) {

        float fluidAmount = 0.0F;
        float fluidMaxAmount = 1.0F;
        int fluidId = -1;
       if (((TileEntityFluidContainer)tileEntity1).fluidContents[0] != null && ((TileEntityFluidContainer)tileEntity1).fluidContents[0].fluid != null) {
            fluidMaxAmount = (float)((TileEntityFluidContainer)tileEntity1).getFluidCapacityForSlot(0);
            fluidAmount = (float)((TileEntityFluidContainer)tileEntity1).fluidContents[0].amount;
            fluidId = ((TileEntityFluidContainer)tileEntity1).fluidContents[0].fluid.getFirstId();
        }

        float amount = Math.abs(fluidAmount / fluidMaxAmount - 0.02F);
        if (fluidId != -1) {
            Block<?> block = Blocks.blocksList[fluidId];
            blockRenderer = new RenderBlocks(new HologramWorld((ArrayList<BlockInstance>) Catalyst.listOf(new BlockInstance(block,new Vec3i(),0,null))));
            BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(block);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d2, (float)d4, (float)d6);
            GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(0.98F, amount, 0.98F);
            GL11.glTranslatef(0.51F, 0.505F, 0.51F);
            GL11.glDisable(2896);
            if(block == Blocks.FLUID_WATER_FLOWING || block == Blocks.FLUID_WATER_STILL){
                ((IColorOverride) model).enableColorOverride();
                ((IColorOverride)model).overrideColor(0,0.5f,1,0.75f);
            }
            this.drawBlock(tessellator, model, 0);
            GL11.glEnable(2896);
            ((IColorOverride)model).overrideColor(1,1,1,1f);
            ((IColorOverride) model).disableColorOverride();
            GL11.glPopMatrix();
        }

    }

    public void drawBlock(Tessellator tessellator, BlockModel<?> model, int meta) {
        TextureRegistry.blockAtlas.bind();
        GL11.glPushMatrix();
        RenderBlocks renderBlocks = BlockModel.renderBlocks;
        BlockModel.setRenderBlocks(blockRenderer);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        model.renderBlockOnInventory(tessellator,meta,1,0.75f,null);
        BlockModel.setRenderBlocks(renderBlocks);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }
}
