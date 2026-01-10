package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.model.IFullbright;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityEncapsulator;

import java.util.ArrayList;
import java.util.Objects;

public class RenderEncapsulator extends TileEntityRenderer<TileEntityEncapsulator> {

    private final RenderBlocks blockRenderer = new RenderBlocks();

    public void drawBlock(Tessellator tessellator, BlockModel<?> model, int meta, int alpha) {
        TextureRegistry.blockAtlas.bind();
        GL11.glPushMatrix();
        RenderBlocks renderBlocks = BlockModel.renderBlocks;
        BlockModel.setRenderBlocks(blockRenderer);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        ((IFullbright) model).enableFullbright();
        model.renderBlockOnInventory(tessellator, meta, 1, alpha, null);
        BlockModel.setRenderBlocks(renderBlocks);
        ((IFullbright) model).disableFullbright();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    @Override
    public void doRender(Tessellator tessellator, TileEntityEncapsulator tile, double x, double y, double z, float f) {
        int tx = tile.x;
        int ty = tile.y;
        int tz = tile.z;
        World world = this.renderDispatcher.textureManager.mc.currentWorld;

        if (!tile.areMarkersValid()) return;

        if (!Objects.equals(world.getLevelData().getWorldName(), "modelviewer")) {

            Direction side = Direction.getDirectionFromSide(tile.getBlockMeta()).getOpposite();
            Vec3f pos = new Vec3f(x, y, z).add(side.getVecF());

            int ox = tile.originMarker.pos.x;
            int oy = tile.originMarker.pos.y;
            int oz = tile.originMarker.pos.z;

            int hx = tile.heightMarker.pos.x;
            int hy = tile.heightMarker.pos.y;
            int hz = tile.heightMarker.pos.z;

            int dx = tile.depthMarker.pos.x;
            int dy = tile.depthMarker.pos.y;
            int dz = tile.depthMarker.pos.z;

            int wx = tile.widthMarker.pos.x;
            int wy = tile.widthMarker.pos.y;
            int wz = tile.widthMarker.pos.z;

            int offsetX = ox - dx;
            int offsetY = oy - hy;
            int offsetZ = oz - wz;

            GL11.glPushMatrix();
            GL11.glTranslatef((float) x - (tx) + 0.25f, (float) y + 0.25f, (float) z + (tz) + 0.25f);
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            GL11.glDisable(2896);
            GL11.glEnable(2896);
            GL11.glPopMatrix();

            renderLineBetweenTwoPoints(ox, oy, oz, hx, hy, hz, 0, 0, 1, 1, 8, pos.x, pos.y, pos.z);
            renderLineBetweenTwoPoints(ox, oy, oz, hx, hy, hz, 0, 0, 1, 1, 8, pos.x - offsetX, pos.y, pos.z);
            renderLineBetweenTwoPoints(ox, oy, oz, hx, hy, hz, 0, 0, 1, 1, 8, pos.x - offsetX, pos.y, pos.z - offsetZ);
            renderLineBetweenTwoPoints(ox, oy, oz, hx, hy, hz, 0, 0, 1, 1, 8, pos.x, pos.y, pos.z - offsetZ);

            renderLineBetweenTwoPoints(ox, oy, oz, dx, dy, dz, 0, 0, 1, 1, 8, pos.x, pos.y - offsetY, pos.z - offsetZ);
            renderLineBetweenTwoPoints(ox, oy, oz, dx, dy, dz, 0, 0, 1, 1, 8, pos.x, pos.y - offsetY, pos.z);
            renderLineBetweenTwoPoints(ox, oy, oz, wx, wy, wz, 0, 0, 1, 1, 8, pos.x, pos.y - offsetY, pos.z);
            renderLineBetweenTwoPoints(ox, oy, oz, wx, wy, wz, 0, 0, 1, 1, 8, pos.x - offsetX, pos.y - offsetY, pos.z);

            renderLineBetweenTwoPoints(ox, oy, oz, dx, dy, dz, 0, 0, 1, 1, 8, pos.x, pos.y, pos.z - offsetZ);
            renderLineBetweenTwoPoints(ox, oy, oz, dx, dy, dz, 0, 0, 1, 1, 8, pos.x, pos.y, pos.z);
            renderLineBetweenTwoPoints(ox, oy, oz, wx, wy, wz, 0, 0, 1, 1, 8, pos.x, pos.y, pos.z);
            renderLineBetweenTwoPoints(ox, oy, oz, wx, wy, wz, 0, 0, 1, 1, 8, pos.x - offsetX, pos.y, pos.z);
        }
    }

    public void renderLineBetweenTwoPoints(int x1, int y1, int z1, long x2, long y2, long z2, float red, float green, float blue, float alpha, float width, double x, double y, double z) {
        renderLineBetweenTwoPoints(x1, y1, z1, x2, y2, z2, red, green, blue, alpha, width, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, false, false, x, y, z);
    }

    public void renderLineBetweenTwoPoints(int x1, int y1, int z1, long x2, long y2, long z2, float red, float green, float blue, float alpha, float width, ArrayList<Integer> xadd, ArrayList<Integer> yadd, ArrayList<Integer> zadd, ArrayList<Integer> sideadd, boolean backwired, boolean firstblocksided, boolean secondblocksided, double x, double y, double z) {
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
        LightmapHelper.setLightmapCoord(15, 15);
        //First
        if (!backwired) {
            if (firstblocksided) {
                float xoffset = 0;
                float yoffset = 0;
                float zoffset = 0;
                switch (Minecraft.getMinecraft().currentWorld.getBlockMetadata(x1, y1, z1)) {
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
        } else {
            if (secondblocksided) {
                float xoffset = 0;
                float yoffset = 0;
                float zoffset = 0;
                switch (Minecraft.getMinecraft().currentWorld.getBlockMetadata((int) x2, (int) y2, (int) z2)) {
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
        //Middle
        for (int i = xadd.size() - 1; i >= 0; i--) {
            float xoffset = 0;
            float yoffset = 0;
            float zoffset = 0;
            switch (sideadd.get(i)) {
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
        if (backwired) {
            if (firstblocksided) {
                float xoffset = 0;
                float yoffset = 0;
                float zoffset = 0;
                switch (Minecraft.getMinecraft().currentWorld.getBlockMetadata(x1, y1, z1)) {
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
        } else {
            if (secondblocksided) {
                float xoffset = 0;
                float yoffset = 0;
                float zoffset = 0;
                switch (Minecraft.getMinecraft().currentWorld.getBlockMetadata((int) x2, (int) y2, (int) z2)) {
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
