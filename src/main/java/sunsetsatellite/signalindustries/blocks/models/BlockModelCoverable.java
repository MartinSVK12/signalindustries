package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.Vec3f;
import sunsetsatellite.signalindustries.covers.CoverBase;
import sunsetsatellite.signalindustries.inventories.base.TileEntityCoverable;

public class BlockModelCoverable extends BlockModelIOPreview {
    public BlockModelCoverable(Block block) {
        super(block);
        hasOverbright = true;
    }

    @Override
    public boolean render(Tessellator tessellator, int x, int y, int z) {
        TileEntity tile = renderBlocks.blockAccess.getBlockTileEntity(x, y, z);
        if(tile == null) return false;
        if (tile instanceof TileEntityCoverable) {
            TileEntityCoverable machine = (TileEntityCoverable) tile;
            for (Direction dir : machine.getCovers().keySet()) {
                CoverBase cover = machine.getCovers().get(dir);
                Vec3f vec = dir.getVecF().divide(100);
                if (cover == null) continue;
                IconCoordinate tex = cover.getTexture();
                renderBlocks.overrideBlockTexture = cover.getTexture();
                renderBlocks.useInventoryTint = false;
                renderBlocks.enableAO = true;
                int side = dir.getSideNumber();
                if (side == 0) {
                    this.renderBottomFace(tessellator, block, x + vec.x, y + vec.y, z + vec.z, tex);
                } else if (side == 1) {
                    this.renderTopFace(tessellator, block, x + vec.x, y + vec.y, z + vec.z, tex);
                } else if (side == 2) {
                    this.renderNorthFace(tessellator, block, x + vec.x, y + vec.y, z + vec.z, tex);
                } else if (side == 3) {
                    this.renderSouthFace(tessellator, block, x + vec.x, y + vec.y, z + vec.z, tex);
                } else if (side == 4) {
                    this.renderWestFace(tessellator, block, x + vec.x, y + vec.y, z + vec.z, tex);
                } else if (side == 5) {
                    this.renderEastFace(tessellator, block, x + vec.x, y + vec.y, z + vec.z, tex);
                }
                renderBlocks.useInventoryTint = true;
                renderBlocks.overrideBlockTexture = null;
            }
        }
        return super.render(tessellator, x, y, z);
    }
}
