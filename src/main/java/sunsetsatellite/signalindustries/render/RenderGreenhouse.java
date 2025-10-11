package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.HologramWorld;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.multiblocks.RenderMultiblock;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityGreenhouse;

import java.util.ArrayList;

public class RenderGreenhouse extends RenderMultiblock {

    @Override
    public void doRender(Tessellator tessellator, TileEntity tileEntity, double d, double e, double f, float g) {
        TileEntityGreenhouse greenhouse = (TileEntityGreenhouse) tileEntity;
        int i = tileEntity.x;
        int j = tileEntity.y;
        int k = tileEntity.z;
        Direction dir = Direction.getDirectionFromSide(tileEntity.getBlockMeta());
        World world = this.renderDispatcher.textureManager.mc.currentWorld;
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        Vec3i middle = greenhouse.getPosition().add(Direction.getDirectionFromSide(greenhouse.getBlockMeta()).getVec().multiply(3));
        Vec3i offset = middle.copy().add(new Vec3i(2,0,2));
        ArrayList<BlockInstance> blocks = new ArrayList<>();
        for (int v = 0; v < 5; v++) {
            for (int w = 0; w < 5; w++) {
                if (offset.x - v == middle.x && offset.z - w == middle.z) continue;
                BlockInstance block = new BlockInstance(Blocks.CROPS_WHEAT, new Vec3i(offset.x-v, offset.y, offset.z-w), greenhouse.getProgressScaled(7), greenhouse);
                blocks.add(block);
            }
        }
        GL11.glTranslated(d,e,f);
        blockRenderer = new RenderBlocks(new HologramWorld(blocks));
        for (BlockInstance block : blocks) {
            BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(block.block);
            betterDrawBlock(tessellator, model, block.pos, greenhouse.getPosition());
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        super.doRender(tessellator, tileEntity, d, e, f, g);
    }


    public void betterDrawBlock(Tessellator tessellator, BlockModel<?> model, Vec3i blockPos, Vec3i tilePos) {
        int i = tilePos.x;
        int j = tilePos.y;
        int k = tilePos.z;
        int x = blockPos.x;
        int y = blockPos.y;
        int z = blockPos.z;
        TextureRegistry.blockAtlas.bind();
        GL11.glPushMatrix();
        RenderBlocks renderBlocks = BlockModel.renderBlocks;
        BlockModel.setRenderBlocks(blockRenderer);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.startDrawingQuads();
        GL11.glTranslated(-x + (i-x),-y + (j-y),-z + (k-z));
        model.render(tessellator,x,y,z);
        tessellator.draw();
        BlockModel.setRenderBlocks(renderBlocks);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

}
