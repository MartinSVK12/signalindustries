package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.core.block.Block;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.RenderBlockSimple;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.multiblocks.Multiblock;

import java.util.ArrayList;

public class RenderMultiblockInGuidebook {
    private final RenderBlockSimple blockRenderer = new RenderBlockSimple();

    public void doRender(Multiblock multiblock, RenderEngine re, FontRenderer fe, double d, double e, double f, float g) {
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
            drawBlock(fe,
                    re,
                    id,
                    meta,
                    i,
                    j,
                    k);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }

    public void drawBlock(FontRenderer fontrenderer, RenderEngine renderengine, int i, int j, int x, int y, int z) {
        renderengine.bindTexture(renderengine.getTexture("/terrain.png"));
        Block f1 = Block.blocksList[i];
        GL11.glPushMatrix();
        this.blockRenderer.renderBlock(f1, j, renderengine.mc.theWorld, x, y, z);
        GL11.glPopMatrix();
        GL11.glEnable(2884);
    }
}
