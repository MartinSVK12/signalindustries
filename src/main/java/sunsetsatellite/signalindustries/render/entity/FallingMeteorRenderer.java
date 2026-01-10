package sunsetsatellite.signalindustries.render.entity;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.signalindustries.entities.ProjectileFallingMeteor;

public class FallingMeteorRenderer extends EntityRenderer<ProjectileFallingMeteor> {
    public FallingMeteorRenderer() {
        blockRenderer = new RenderBlocks();
        this.shadowSize = 0.5F;
    }

    public void renderMeteor(Tessellator tessellator, ProjectileFallingMeteor entity, double x, double y, double z, float f, float f1) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y - 0.5f, (float) z + 0.5f);
        TextureRegistry.blockAtlas.bind();
        Block<?> block = Blocks.getBlock(entity.blockID);
        World world = entity.world;
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        blockRenderer.renderBlockFallingSand(tessellator, BlockModelDispatcher.getInstance().getDispatch(block), block, world, MathHelper.floor(entity.x), MathHelper.floor(entity.y), MathHelper.floor(entity.z));
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glPopMatrix();
    }

    @Override
    public void render(Tessellator tessellator, ProjectileFallingMeteor entity, double x, double y, double z, float yaw, float partialTick) {
        this.renderMeteor(tessellator, entity, x, y, z, yaw, partialTick);
    }

    private final RenderBlocks blockRenderer;
}
