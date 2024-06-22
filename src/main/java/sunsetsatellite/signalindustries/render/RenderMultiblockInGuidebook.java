package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.*;
import sunsetsatellite.catalyst.multiblocks.HologramWorld;
import sunsetsatellite.catalyst.multiblocks.Multiblock;

import java.util.ArrayList;

public class RenderMultiblockInGuidebook {
    private RenderBlocks blockRenderer;

    public void doRender(Multiblock multiblock, RenderEngine re, FontRenderer fe, double d, double e, double f, float g) {

        int i = 0;
        int j = 0;
        int k = 0;
        Direction dir = Direction.X_NEG;
        ArrayList<BlockInstance> blocks = multiblock.getBlocks(new Vec3i(),dir);
        blocks.add(multiblock.getOrigin());
        blockRenderer = new RenderBlocks(new HologramWorld(blocks));
        for (BlockInstance block : blocks) {
            BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(block.block);
            ((IFullbright)model).enableFullbright();
            Vec3i pos = block.pos;
            int id = block.block.id;
            int meta = block.meta == -1 ? 0 : block.meta;
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glTranslatef((float)d+(pos.x-i), (float)e+(pos.y-j), (float)f+(pos.z-k));
            drawBlock(Tessellator.instance, model, meta);
            ((IFullbright)model).disableFullbright();
            GL11.glEnable(GL11.GL_LIGHTING);
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
        model.renderBlockOnInventory(tessellator,meta,1,0.75f,null);
        BlockModel.setRenderBlocks(renderBlocks);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }
}
