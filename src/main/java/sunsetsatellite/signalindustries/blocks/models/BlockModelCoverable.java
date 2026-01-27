package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.covers.CoverBase;
import sunsetsatellite.signalindustries.tiles.base.TileEntityCoverable;

public class BlockModelCoverable extends BlockModelIOPreview {
    public BlockModelCoverable(Block<? extends BlockLogic> block) {
        super(block);
    }

    @Override
    public boolean render(Tessellator tessellator, int x, int y, int z) {
        TileEntity tile = renderBlocks.blockAccess.getTileEntity(x, y, z);
        if (tile == null) {
            return super.render(tessellator, x, y, z);
        }
        if (tile instanceof TileEntityCoverable) {
            TileEntityCoverable machine = (TileEntityCoverable) tile;
            for (Direction dir : machine.getCovers().keySet()) {
                CoverBase cover = machine.getCovers().get(dir);
                Vec3f vec = dir.getVecF().divide(100);
                if (cover == null) continue;
                IconCoordinate tex = TextureRegistry.getTexture(cover.getTexture());
                renderBlocks.overrideBlockTexture = TextureRegistry.getTexture(cover.getTexture());
                renderBlocks.useInventoryTint = false;
                renderBlocks.enableAO = true;
                int side = dir.getSideNumber();
                if (side == 0) {
                    this.renderBottomFace(tessellator, block.getBounds(), x + vec.x, y + vec.y, z + vec.z, tex);
                } else if (side == 1) {
                    this.renderTopFace(tessellator, block.getBounds(), x + vec.x, y + vec.y, z + vec.z, tex);
                } else if (side == 2) {
                    this.renderNorthFace(tessellator, block.getBounds(), x + vec.x, y + vec.y, z + vec.z, tex);
                } else if (side == 3) {
                    this.renderSouthFace(tessellator, block.getBounds(), x + vec.x, y + vec.y, z + vec.z, tex);
                } else if (side == 4) {
                    this.renderWestFace(tessellator, block.getBounds(), x + vec.x, y + vec.y, z + vec.z, tex);
                } else if (side == 5) {
                    this.renderEastFace(tessellator, block.getBounds(), x + vec.x, y + vec.y, z + vec.z, tex);
                }
                renderBlocks.useInventoryTint = true;
                renderBlocks.overrideBlockTexture = null;
            }
        }
        return super.render(tessellator, x, y, z);
    }
}
