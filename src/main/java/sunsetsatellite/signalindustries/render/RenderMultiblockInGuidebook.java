package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.multiblocks.Multiblock;

import java.util.ArrayList;

public class RenderMultiblockInGuidebook {
    private RenderBlocks blockRenderer;

    public void doRender(Multiblock multiblock, RenderEngine re, FontRenderer fe, double d, double e, double f, float g) {
        blockRenderer = new RenderBlocks(re.mc.theWorld);
        int i = 0;
        int j = 0;
        int k = 0;
        Direction dir = Direction.X_NEG;
        ArrayList<BlockInstance> blocks = multiblock.getBlocks(new Vec3i(),dir);
        blocks.add(multiblock.getOrigin());
        for (BlockInstance block : blocks) {
            Vec3i pos = block.pos;
            int id = block.block.id;
            int meta = block.meta == -1 ? 0 : block.meta;
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glTranslatef((float)d+(pos.x-i), (float)e+(pos.y-j), (float)f+(pos.z-k));
            drawBlock(Tessellator.instance,
                    re,
                    id,
                    i,
                    j,
                    k);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }

    public void drawBlock(Tessellator tessellator, RenderEngine renderengine, int i, int j, int k, int l) {
        Block block = Block.blocksList[i];
        GL11.glPushMatrix();
        this.blockRenderer.renderStandardBlock(tessellator, BlockModelDispatcher.getInstance().getDispatch(block),block,j,k,l);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }
}
