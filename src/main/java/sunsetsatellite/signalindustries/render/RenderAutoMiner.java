package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.IFullbright;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityAutoMiner;

import java.util.ArrayList;
import java.util.Objects;

public class RenderAutoMiner extends TileEntityRenderer<TileEntityAutoMiner> {

    private RenderBlocks blockRenderer = new RenderBlocks();

    public void drawBlock(Tessellator tessellator, BlockModel<?> model, int meta, int alpha) {
        TextureRegistry.blockAtlas.bindTexture();
        GL11.glPushMatrix();
        RenderBlocks renderBlocks = BlockModel.renderBlocks;
        BlockModel.setRenderBlocks(blockRenderer);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        ((IFullbright)model).enableFullbright();
        model.renderBlockOnInventory(tessellator,meta,1,alpha,null);
        BlockModel.setRenderBlocks(renderBlocks);
        ((IFullbright)model).disableFullbright();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    @Override
    public void doRender(Tessellator tessellator, TileEntityAutoMiner tileEntity, double x, double y, double z, float f) {
        int tx = tileEntity.x;
        int ty = tileEntity.y;
        int tz = tileEntity.z;
        int cx = tileEntity.current.x;
        int cy = tileEntity.current.y;
        int cz = tileEntity.current.z;
        World world = this.renderDispatcher.renderEngine.mc.theWorld;

        if (!Objects.equals(world.getLevelData().getWorldName(), "modelviewer")) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x - (tx - cx) + 0.25f, (float) y + 4.25f, (float) z + (cz - tz) + 0.25f);
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            GL11.glDisable(2896);
            GL11.glEnable(2896);
            GL11.glPopMatrix();

            if (cy - (ty + 4) < 0) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float) x - (tx - cx) + 0.5f, (float) y - (ty - cy) + 1.2f, (float) z + (cz - tz) + 0.5f);
                GL11.glScalef(0.50f, 1f, 0.50f);
                GL11.glDisable(2896);
                drawBlock(tessellator, BlockModelDispatcher.getInstance().getDispatch(Block.basalt),0,1);
                GL11.glEnable(2896);
                GL11.glPopMatrix();
            }

            if (cy - (ty + 4) < 0) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float) x - (tx - cx) + 0.5f, (float) y - (ty - cy) + 0.3f, (float) z + (cz - tz) + 0.5f);
                GL11.glScalef(0.25f, 0.75f, 0.25f);
                GL11.glDisable(2896);
                if(tileEntity.hasSilkTouch()){
                    drawBlock(tessellator, BlockModelDispatcher.getInstance().getDispatch(Block.blockGold),0,1);
                } else {
                    drawBlock(tessellator, BlockModelDispatcher.getInstance().getDispatch(Block.blockDiamond),0,1);
                }
                GL11.glEnable(2896);
                GL11.glPopMatrix();
            }

            //square above
            renderLineBetweenTwoPoints(tx, ty, tz, tx - 15, ty, tz, 1, 0.5f, 0, 1, 8, x, y + 4, z);
            renderLineBetweenTwoPoints(tx, ty, tz, tx, ty, tz + 15, 1, 0.5f, 0, 1, 8, x, y + 4, z);
            renderLineBetweenTwoPoints(tx, ty, tz, tx - 15, ty, tz, 1, 0.5f, 0, 1, 8, x, y + 4, z + 15);
            renderLineBetweenTwoPoints(tx, ty, tz, tx, ty, tz + 15, 1, 0.5f, 0, 1, 8, x - 15, y + 4, z);
            //square
            renderLineBetweenTwoPoints(tx, ty, tz, tx - 15, ty, tz, 1, 0.5f, 0, 1, 8, x, y, z);
            renderLineBetweenTwoPoints(tx, ty, tz, tx, ty, tz + 15, 1, 0.5f, 0, 1, 8, x, y, z);
            renderLineBetweenTwoPoints(tx, ty, tz, tx - 15, ty, tz, 1, 0.5f, 0, 1, 8, x, y, z + 15);
            renderLineBetweenTwoPoints(tx, ty, tz, tx, ty, tz + 15, 1, 0.5f, 0, 1, 8, x - 15, y, z);
            //down
            renderLineBetweenTwoPoints(tx, ty, tz, tx, ty - 4, tz, 1, 0.5f, 0, 1, 8, x, y + 4, z);
            renderLineBetweenTwoPoints(tx, ty, tz, tx, ty - 4, tz, 1, 0.5f, 0, 1, 8, x - 15, y + 4, z);
            renderLineBetweenTwoPoints(tx, ty, tz, tx, ty - 4, tz, 1, 0.5f, 0, 1, 8, x, y + 4, z + 15);
            renderLineBetweenTwoPoints(tx, ty, tz, tx, ty - 4, tz, 1, 0.5f, 0, 1, 8, x - 15, y + 4, z + 15);
            //down 2
            renderLineBetweenTwoPoints(tx, ty, tz, tx, 0, tz, 1, 1, 1, 1, 2, x, y, z);
            renderLineBetweenTwoPoints(tx, ty, tz, tx, 0, tz, 1, 1, 1, 1, 2, x - 15, y, z);
            renderLineBetweenTwoPoints(tx, ty, tz, tx, 0, tz, 1, 1, 1, 1, 2, x, y, z + 15);
            renderLineBetweenTwoPoints(tx, ty, tz, tx, 0, tz, 1, 1, 1, 1, 2, x - 15, y, z + 15);
            //square down
            renderLineBetweenTwoPoints(tx, ty, tz, tx - 15, ty, tz, 1, 1, 1, 1, 2, x, y - ty, z);
            renderLineBetweenTwoPoints(tx, ty, tz, tx, ty, tz + 15, 1, 1, 1, 1, 2, x, y - ty, z);
            renderLineBetweenTwoPoints(tx, ty, tz, tx - 15, ty, tz, 1, 1, 1, 1, 2, x, y - ty, z + 15);
            renderLineBetweenTwoPoints(tx, ty, tz, tx, ty, tz + 15, 1, 1, 1, 1, 2, x - 15, y - ty, z);

            //current lines
            renderLineBetweenTwoPoints(tx, ty, tz, cx, ty, tz, 1, 0, 0, 1, 8, x, y + 4, z + (cz - tz));
            renderLineBetweenTwoPoints(tx, ty, cz, tx, ty, tz, 0, 0, 1, 1, 8, x - (tx - cx), y + 4, z + (cz - tz));
            renderLineBetweenTwoPoints(tx, ty, tz, tx, cy - 5, tz, 0, 1, 0, 1, 8, x - (tx - cx), y + 4, z + (cz - tz));
        }
    }

    public void renderLineBetweenTwoPoints(int x1, int y1, int z1, long x2, long y2, long z2, float red, float green, float blue, float alpha, float width, double x, double y, double z) {
        renderLineBetweenTwoPoints(x1,y1,z1,x2,y2,z2,red,green,blue,alpha,width,new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),false,false,false,x,y,z);
    }

    public void renderLineBetweenTwoPoints(int x1, int y1, int z1, long x2, long y2, long z2, float red, float green, float blue, float alpha, float width, ArrayList<Integer> xadd, ArrayList<Integer> yadd, ArrayList<Integer> zadd, ArrayList<Integer> sideadd, boolean backwired, boolean firstblocksided, boolean secondblocksided, double x, double y, double z){
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glLineWidth(width);
        GL11.glTranslated(x, y, z);
        GL11.glDepthMask(false);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        LightmapHelper.setLightmapCoord(15,15);
        //First
        if(!backwired) {
            if(firstblocksided){
                float xoffset = 0;
                float yoffset = 0;
                float zoffset = 0;
                switch(Minecraft.getMinecraft(Minecraft.class).theWorld.getBlockMetadata(x1, y1, z1)){
                    case 0:
                        xoffset = 0.5f;
                        yoffset = 0.9f;
                        zoffset = 0.5f;
                        break;
                    case 1:
                        xoffset = 0.1f;
                        yoffset = 0.5f;
                        zoffset = 0.5f;
                        break;
                    case 2:
                        xoffset = 0.9f;
                        yoffset = 0.5f;
                        zoffset = 0.5f;
                        break;
                    case 3:
                        xoffset = 0.5f;
                        yoffset = 0.5f;
                        zoffset = 0.1f;
                        break;
                    case 4:
                        xoffset = 0.5f;
                        yoffset = 0.5f;
                        zoffset = 0.9f;
                        break;
                    case 5:
                        xoffset = 0.5f;
                        yoffset = 0.1f;
                        zoffset = 0.5f;
                        break;
                }
                GL11.glVertex3f(xoffset, yoffset, zoffset);
            } else {
                GL11.glVertex3f(0.5f, 0.5f, 0.5f);
            }
        } else{
            if(secondblocksided) {
                float xoffset = 0;
                float yoffset = 0;
                float zoffset = 0;
                switch (Minecraft.getMinecraft(Minecraft.class).theWorld.getBlockMetadata((int) x2, (int) y2, (int) z2)) {
                    case 0:
                        xoffset = 0.5f;
                        yoffset = 0.9f;
                        zoffset = 0.5f;
                        break;
                    case 1:
                        xoffset = 0.1f;
                        yoffset = 0.5f;
                        zoffset = 0.5f;
                        break;
                    case 2:
                        xoffset = 0.9f;
                        yoffset = 0.5f;
                        zoffset = 0.5f;
                        break;
                    case 3:
                        xoffset = 0.5f;
                        yoffset = 0.5f;
                        zoffset = 0.1f;
                        break;
                    case 4:
                        xoffset = 0.5f;
                        yoffset = 0.5f;
                        zoffset = 0.9f;
                        break;
                    case 5:
                        xoffset = 0.5f;
                        yoffset = 0.1f;
                        zoffset = 0.5f;
                        break;
                }
                GL11.glVertex3d(-(x1 - x2) + xoffset,-(y1 - y2) + yoffset, -(z1 - z2) + zoffset);
            } else {
                GL11.glVertex3d(-(x1 - x2) + 0.5f, -(y1 - y2) + 0.5f, -(z1 - z2) + 0.5f);
            }
        }
        //Middle
        for(int i = xadd.size() - 1; i >= 0 ; i--){
            float xoffset = 0;
            float yoffset = 0;
            float zoffset = 0;
            switch(sideadd.get(i)){
                case 0:
                    xoffset = 0.5f;
                    yoffset = -0.1f;
                    zoffset = 0.5f;
                    break;
                case 1:
                    xoffset = 0.5f;
                    yoffset = 1.1f;
                    zoffset = 0.5f;
                    break;
                case 2:
                    xoffset = 0.5f;
                    yoffset = 0.5f;
                    zoffset = -0.1f;
                    break;
                case 3:
                    xoffset = 0.5f;
                    yoffset = 0.5f;
                    zoffset = 1.1f;
                    break;
                case 4:
                    xoffset = -0.1f;
                    yoffset = 0.5f;
                    zoffset = 0.5f;
                    break;
                case 5:
                    xoffset = 1.1f;
                    yoffset = 0.5f;
                    zoffset = 0.5f;
                    break;
            }
            GL11.glVertex3d(-(x1 - xadd.get(i)) + xoffset, -(y1 - yadd.get(i)) + yoffset, -(z1 - zadd.get(i)) + zoffset);
        }
        //Second
        if(backwired) {
            if(firstblocksided){
                float xoffset = 0;
                float yoffset = 0;
                float zoffset = 0;
                switch(Minecraft.getMinecraft(Minecraft.class).theWorld.getBlockMetadata(x1, y1, z1)){
                    case 0:
                        xoffset = 0.5f;
                        yoffset = 0.9f;
                        zoffset = 0.5f;
                        break;
                    case 1:
                        xoffset = 0.1f;
                        yoffset = 0.5f;
                        zoffset = 0.5f;
                        break;
                    case 2:
                        xoffset = 0.9f;
                        yoffset = 0.5f;
                        zoffset = 0.5f;
                        break;
                    case 3:
                        xoffset = 0.5f;
                        yoffset = 0.5f;
                        zoffset = 0.1f;
                        break;
                    case 4:
                        xoffset = 0.5f;
                        yoffset = 0.5f;
                        zoffset = 0.9f;
                        break;
                    case 5:
                        xoffset = 0.5f;
                        yoffset = 0.1f;
                        zoffset = 0.5f;
                        break;
                }
                GL11.glVertex3f(xoffset, yoffset, zoffset);
            } else {
                GL11.glVertex3f(0.5f, 0.5f, 0.5f);
            }
        } else{
            if(secondblocksided) {
                float xoffset = 0;
                float yoffset = 0;
                float zoffset = 0;
                switch (Minecraft.getMinecraft(Minecraft.class).theWorld.getBlockMetadata((int) x2, (int) y2, (int) z2)) {
                    case 0:
                        xoffset = 0.5f;
                        yoffset = 0.9f;
                        zoffset = 0.5f;
                        break;
                    case 1:
                        xoffset = 0.1f;
                        yoffset = 0.5f;
                        zoffset = 0.5f;
                        break;
                    case 2:
                        xoffset = 0.9f;
                        yoffset = 0.5f;
                        zoffset = 0.5f;
                        break;
                    case 3:
                        xoffset = 0.5f;
                        yoffset = 0.5f;
                        zoffset = 0.1f;
                        break;
                    case 4:
                        xoffset = 0.5f;
                        yoffset = 0.5f;
                        zoffset = 0.9f;
                        break;
                    case 5:
                        xoffset = 0.5f;
                        yoffset = 0.1f;
                        zoffset = 0.5f;
                        break;
                }
                GL11.glVertex3d(-(x1 - x2) + xoffset, -(y1 - y2) + yoffset, -(z1 - z2) + zoffset);
            } else {
                GL11.glVertex3d(-(x1 - x2) + 0.5f, -(y1 - y2) + 0.5f, -(z1 - z2) + 0.5f);
            }
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
    }
}
