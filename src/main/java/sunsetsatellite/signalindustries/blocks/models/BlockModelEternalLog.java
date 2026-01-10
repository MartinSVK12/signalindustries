package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;

public class BlockModelEternalLog extends BlockModelStandard {

    public static final IconCoordinate topNormal = TextureRegistry.getTexture("signalindustries:block/eternal_tree_log_top");
    public static final IconCoordinate topEmpty = TextureRegistry.getTexture("signalindustries:block/eternal_tree_log_top_empty");
    public static final IconCoordinate sideNormal = TextureRegistry.getTexture("signalindustries:block/eternal_tree_log");

    public BlockModelEternalLog(Block block) {
        super(block);
    }

    @Override
    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        if (side.getAxis() == Axis.Y && blockAccess.getBlockMetadata(x, y, z) == 1) {
            return topNormal;
        } else if (side.getAxis() == Axis.Y && blockAccess.getBlockMetadata(x, y, z) != 1) {
            return topEmpty;
        }
        return sideNormal;
    }

    @Override
    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
        if (side.getAxis() == Axis.Y && data == 1) {
            return topNormal;
        } else if (side.getAxis() == Axis.Y) {
            return topEmpty;
        }
        return sideNormal;
    }
}
