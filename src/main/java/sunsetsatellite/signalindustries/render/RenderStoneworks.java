package sunsetsatellite.signalindustries.render;

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
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.HologramWorld;
import sunsetsatellite.catalyst.core.util.model.IColorOverride;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

import java.util.ArrayList;


public class RenderStoneworks extends TileEntityRenderer<TileEntity> {
    private RenderBlocks blockRenderer;

    public RenderStoneworks() {
    }

    public void doRender(Tessellator tessellator, TileEntity tileEntity1, double d2, double d4, double d6, float f8) {

        TileEntityTieredMachineSimple tile = (TileEntityTieredMachineSimple) tileEntity1;
        FluidStack[] fluidContents = tile.fluidContents;
        float fluidAmount;
        float fluidMaxAmount = 0.0F;
        int fluidId;
        for (int i = 0; i < tile.fluidCapacity.length; i++) {
            if(i == tile.energySlot) continue;
            fluidMaxAmount += tile.fluidCapacity[i];
        }
        ArrayList<BlockInstance> list = new ArrayList<>();
        for (int i = 0; i < fluidContents.length; i++) {
            if(i == tile.energySlot) continue;
            FluidStack fluidStack = fluidContents[i];
            if (fluidStack == null) continue;
            Block<?> block = Blocks.blocksList[fluidStack.fluid.getFirstId()];
            list.add(new BlockInstance(block, new Vec3i(), 0, null));
        }
        if(fluidMaxAmount <= 0) fluidMaxAmount = 1;
        blockRenderer = new RenderBlocks(new HologramWorld(list));
        float i = 0;
        for (int j = 0; j < fluidContents.length; j++) {
            FluidStack fluidStack = fluidContents[j];
            if (j == tile.energySlot) continue;
            if (fluidStack == null) continue;
            fluidAmount = fluidStack.amount;
            fluidId = fluidStack.fluid.getFirstId();

            float amount = 0.49f;
            if (fluidId != 0) {
                Block<?> block = Blocks.blocksList[fluidStack.fluid.getFirstId()];
                BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(block);
                GL11.glPushMatrix();
                GL11.glTranslatef((float) d2, (float) d4 + i, (float) d6);
                GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(0.98F, amount, 0.98F);
                GL11.glTranslatef(0.51F, 0.50F, 0.51F);
                GL11.glDisable(2896);
                i += 1 * amount;
                if (block == Blocks.FLUID_WATER_FLOWING || block == Blocks.FLUID_WATER_STILL) {
                    ((IColorOverride) model).enableColorOverride();
                    ((IColorOverride) model).overrideColor(0, 0.5f, 1, 0.75f);
                }
                this.drawBlock(tessellator, model, 0);
                GL11.glEnable(2896);
                ((IColorOverride) model).overrideColor(1, 1, 1, 1f);
                ((IColorOverride) model).disableColorOverride();
                GL11.glPopMatrix();
            }
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
