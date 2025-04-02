package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Axis;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.multiblocks.RenderMultiblock;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityWarpGate;

public class RenderWarpGate extends RenderMultiblock {

    @Override
    public void doRender(Tessellator tessellator, TileEntity tileEntity, double x, double y, double z, float f) {
        super.doRender(tessellator, tileEntity, x, y, z, f);
        if(tileEntity instanceof TileEntityWarpGate){
            if(((TileEntityWarpGate) tileEntity).isActive()){
                Direction dir = Direction.getDirectionFromSide(tileEntity.getBlockMeta());
                Vec3f offset = dir.getVecF().multiply(-4);
                Axis axis = dir.shiftAxis().getAxis();

                GL11.glPushMatrix();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glTranslated(x + offset.x, y + offset.y, z + offset.z);
                GL11.glDepthMask(false);
                LightmapHelper.setLightmapCoord(15,15);
                TextureManager renderEngine = Minecraft.getMinecraft().textureManager;
                renderEngine.bindTexture(renderEngine.loadTexture("/assets/signalindustries/textures/block/warp_gate_portal.png"));
                GL11.glBegin(GL11.GL_POLYGON);
                if(axis == Axis.X){
                    GL11.glTexCoord2d(0,0);
                    GL11.glVertex3d(-2,-2,0.5);
                    GL11.glTexCoord2d(0,1);
                    GL11.glVertex3d(3,-2,0.5);
                    GL11.glTexCoord2d(1,1);
                    GL11.glVertex3d(3,3,0.5);
                    GL11.glTexCoord2d(1,0);
                    GL11.glVertex3d(-2,3,0.5);

                    GL11.glTexCoord2d(1,0);
                    GL11.glVertex3d(-2,3,0.5);
                    GL11.glTexCoord2d(1,1);
                    GL11.glVertex3d(3,3,0.5);
                    GL11.glTexCoord2d(0,1);
                    GL11.glVertex3d(3,-2,0.5);
                    GL11.glTexCoord2d(0,0);
                    GL11.glVertex3d(-2,-2,0.5);
                } else if (axis == Axis.Z) {
                    GL11.glTexCoord2d(0,0);
                    GL11.glVertex3d(0.5,-2,-2);
                    GL11.glTexCoord2d(0,1);
                    GL11.glVertex3d(0.5,-2,3);
                    GL11.glTexCoord2d(1,1);
                    GL11.glVertex3d(0.5,3,3);
                    GL11.glTexCoord2d(1,0);
                    GL11.glVertex3d(0.5,3,-2);

                    GL11.glTexCoord2d(1,0);
                    GL11.glVertex3d(0.5,3,-2);
                    GL11.glTexCoord2d(1,1);
                    GL11.glVertex3d(0.5,3,3);
                    GL11.glTexCoord2d(0,1);
                    GL11.glVertex3d(0.5,-2,3);
                    GL11.glTexCoord2d(0,0);
                    GL11.glVertex3d(0.5,-2,-2);
                }

                GL11.glEnd();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDepthMask(true);
                GL11.glPopMatrix();
            }
        }
    }
}
