package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.blocks.BlockUVLamp;

public class BlockModelUVLamp extends BlockModelStandard<BlockUVLamp> {

    public IconCoordinate uvInactive = TextureRegistry.getTexture("signalindustries:block/uv_lamp_inactive");
    public IconCoordinate uvActive = TextureRegistry.getTexture("signalindustries:block/uv_lamp");
    public IconCoordinate uvOverlay = TextureRegistry.getTexture("signalindustries:block/uv_lamp_overlay");


    public BlockModelUVLamp(Block block) {
        super(block);

    }

    @Override
    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        return meta == 1 ? uvActive : uvInactive;
    }

    @Override
    public IconCoordinate getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        return meta == 1 ? uvOverlay : null;
    }
}
