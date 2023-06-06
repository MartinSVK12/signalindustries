package sunsetsatellite.signalindustries.util;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.fluidapi.render.RenderFluid;
import sunsetsatellite.signalindustries.interfaces.IMultiblock;

import java.util.Collection;

public class RenderMultiblock extends TileEntitySpecialRenderer {
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d, double e, double f, float g) {
        int i = tileEntity.xCoord;
        int j = tileEntity.yCoord;
        int k = tileEntity.zCoord;
        World world = this.tileEntityRenderer.renderEngine.minecraft.theWorld;
        if(tileEntity instanceof IMultiblock){
            Collection blocks = ((IMultiblock) tileEntity).getMultiblock().data.func_28110_c();

            for (Object block : blocks) {
                int x = ((NBTTagCompound) block).getInteger("x");
                int y = ((NBTTagCompound) block).getInteger("y");
                int z = ((NBTTagCompound) block).getInteger("z");
                int id = Structure.getBlockId((NBTTagCompound) block);
                int meta = ((NBTTagCompound) block).getInteger("meta");
                if((Structure.getBlockId((NBTTagCompound) block) != tileEntity.getBlockType().blockID)){
                    if(world.getBlockId(i+x,j+y,k+z) != id || (world.getBlockId(i+x,j+y,k+z) != id && world.getBlockMetadata(i+x,j+y,k+z) != meta)){
                        GL11.glPushMatrix();
                        GL11.glDisable(GL11.GL_LIGHTING);
                        GL11.glColor4f(1f,0,0,1.0f);
                        GL11.glTranslatef((float)d+x, (float)e+y, (float)f+z);
                        drawBlock(this.getFontRenderer(),
                                this.tileEntityRenderer.renderEngine,
                                id,
                                meta,
                                i,
                                j,
                                k,
                                tileEntity);
                        GL11.glEnable(GL11.GL_LIGHTING);
                        GL11.glPopMatrix();
                    }
                }
            }
        }

    }

    public void drawBlock(FontRenderer fontrenderer, RenderEngine renderengine, int i, int j, int x, int y, int z, TileEntity tile) {
        renderengine.bindTexture(renderengine.getTexture("/terrain.png"));
        Block f1 = Block.blocksList[i];
        GL11.glPushMatrix();
        this.blockRenderer.renderBlock(f1, j, renderengine.minecraft.theWorld, x, y, z);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    private final RenderBlockSimple blockRenderer = new RenderBlockSimple();
}
