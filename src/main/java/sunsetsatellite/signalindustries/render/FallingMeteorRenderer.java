package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.signalindustries.entities.EntityFallingMeteor;

public class FallingMeteorRenderer extends EntityRenderer<EntityFallingMeteor> {
    public FallingMeteorRenderer() {
        blockRenderer = new RenderBlocks();
        this.shadowSize = 0.5F;
    }

    public void doRenderFallingSand(Tessellator tessellator, EntityFallingMeteor entity, double x, double y, double z, float f, float f1) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        TextureRegistry.blockAtlas.bindTexture();
        Block block = Block.blocksList[entity.blockID];
        World world = entity.getWorld();
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        blockRenderer.renderBlockFallingSand(tessellator, BlockModelDispatcher.getInstance().getDispatch(block), block, world, MathHelper.floor_double(entity.x), MathHelper.floor_double(entity.y), MathHelper.floor_double(entity.z));
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Tessellator tessellator, EntityFallingMeteor entity, double x, double y, double z, float yaw, float partialTick) {
        this.doRenderFallingSand(tessellator,entity, x, y, z, yaw, partialTick);
    }

    private final RenderBlocks blockRenderer;
}
