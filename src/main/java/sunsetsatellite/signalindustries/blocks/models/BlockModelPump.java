package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityPump;
import sunsetsatellite.signalindustries.util.MachineTextures;

public class BlockModelPump extends BlockModelMachine {
    public BlockModelPump(Block<? extends BlockLogic> block) {
        super(block);
    }

    public BlockModelPump(Block<? extends BlockLogic> block, MachineTextures machineTextures) {
        super(block, machineTextures);
    }

    @Override
    public boolean render(Tessellator tessellator, int x, int y, int z) {
        TileEntityPump tile = (TileEntityPump) renderBlocks.blockAccess.getTileEntity(x, y, z);
        if (tile != null && tile.currentFluid != null) {
            IconCoordinate tex = BlockModelDispatcher.getInstance().getDispatch(tile.currentFluid.blocks.get(0)).getBlockTextureFromSideAndMetadata(Side.TOP, 0);
            Vec3f vec = new Vec3f(1).divide(100);
            renderBlocks.overrideBlockTexture = tex;
            renderBlocks.useInventoryTint = false;
            renderBlocks.enableAO = true;
            this.renderBottomFace(tessellator, block.getBounds(), x - vec.x, y + vec.y, z - vec.z, tex);
            this.renderTopFace(tessellator, block.getBounds(), x - vec.x, y - vec.y, z - vec.z, tex);
            this.renderNorthFace(tessellator, block.getBounds(), x - vec.x, y - vec.y, z + vec.z, tex);
            this.renderSouthFace(tessellator, block.getBounds(), x - vec.x, y - vec.y, z - vec.z, tex);
            this.renderWestFace(tessellator, block.getBounds(), x + vec.x, y - vec.y, z - vec.z, tex);
            this.renderEastFace(tessellator, block.getBounds(), x - vec.x, y - vec.y, z - vec.z, tex);
            renderBlocks.useInventoryTint = true;
            renderBlocks.overrideBlockTexture = null;
        }
        return super.render(tessellator, x, y, z);
    }
}
