package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.*;
import sunsetsatellite.catalyst.multiblocks.HologramWorld;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityBuilder;
import sunsetsatellite.signalindustries.items.ItemBlueprint;
import sunsetsatellite.signalindustries.util.SIMultiblock;

import java.util.ArrayList;
import java.util.Objects;

public class RenderBuilderPreview extends TileEntityRenderer<TileEntity> {

    @Override
    public void doRender(Tessellator tessellator, TileEntity tileEntity, double d, double e, double f, float g) {
        int i = tileEntity.x;
        int j = tileEntity.y;
        int k = tileEntity.z;
        World world = this.renderDispatcher.renderEngine.mc.theWorld;
        TileEntityBuilder builder = (TileEntityBuilder) tileEntity;
        Direction dir = builder.rotation;
        if(builder.itemContents[0] != null && builder.itemContents[0].getItem() instanceof ItemBlueprint){
            SIMultiblock multiblock = builder.getMultiblock();
            if (multiblock == null) {
                return;
            }
            ArrayList<BlockInstance> blocks = multiblock.getBlocks(new Vec3i(i, j, k).add(builder.offset),dir);
            blocks.add(multiblock.getOrigin(new Vec3i(i, j, k).add(builder.offset),dir.getOpposite().shiftAxis()));
            ArrayList<BlockInstance> substitutions = multiblock.getSubstitutions(new Vec3i(i, j, k).add(builder.offset),dir);
            blockRenderer = new RenderBlocks(new HologramWorld(blocks));
            for (BlockInstance block : blocks) {
                if(!block.exists(world)){
                    boolean foundSub = substitutions.stream().anyMatch((BI)-> BI.pos.equals(block.pos) && BI.exists(world));
                    if(!foundSub){
                        if (!Objects.equals(world.getLevelData().getWorldName(), "modelviewer")) {
                            GL11.glPushMatrix();
                            GL11.glDisable(GL11.GL_LIGHTING);
                            GL11.glTranslatef((float)d+(block.pos.x-i)+0.5f, (float)e+(block.pos.y-j)+0.5f, (float)f+(block.pos.z-k)+0.5f);
                            BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(block.block);
                            ((IFullbright)model).enableFullbright();
                            if(world.getBlockId(block.pos.x,block.pos.y,block.pos.z) != 0){
                                ((IColorOverride)model).enableColorOverride();
                                ((IColorOverride)model).overrideColor(1,0,0,0.90f);
                                GL11.glScalef(1.1f,1.1f,1.1f);
                            } else {
                                ((IColorOverride)model).overrideColor(1,1,1,0.75f);
                                GL11.glScalef(0.75f,0.75f,0.75f);
                            }
                            drawBlock(tessellator,
                                    model,
                                    block.meta == -1 ? 0 : block.meta);
                            GL11.glEnable(GL11.GL_LIGHTING);
                            GL11.glPopMatrix();
                            ((IColorOverride)model).overrideColor(1,1,1,1f);
                            ((IColorOverride)model).disableColorOverride();
                            ((IFullbright)model).disableFullbright();
                        }
                    }
                }
            }
            if(!Objects.equals(builder.currentlyBuilding, new Vec3i())){
                if (builder.buildingMultiblock != null && !builder.buildingBlocks.isEmpty()){
                    renderLineBetweenTwoPoints(i,j,k,builder.currentlyBuilding.x,builder.currentlyBuilding.y,builder.currentlyBuilding.z,1f,0f,0f,1f,8f,d,e,f);
                }
            }
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

    public void drawBlock(Tessellator tessellator, BlockModel<?> model, int meta) {
        TextureRegistry.blockAtlas.bindTexture();
        GL11.glPushMatrix();
        RenderBlocks renderBlocks = BlockModel.renderBlocks;
        BlockModel.setRenderBlocks(blockRenderer);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        model.renderBlockOnInventory(tessellator,meta,1,null);
        BlockModel.setRenderBlocks(renderBlocks);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    protected RenderBlocks blockRenderer;
}
