package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BlockModelConnectedTextureExtra extends BlockModelConnectedTexture {

    protected final IconCoordinate[] activeTexCoord;
    protected IconCoordinate overlayTexture;

    public BlockModelConnectedTextureExtra(Block block, String key, String activeKey) {
        this(block, key, activeKey, new ArrayList<>());
    }

    public BlockModelConnectedTextureExtra(Block block, String key, String activeKey, List<Block> connectsTo) {
        super(block, key, connectsTo);

        activeTexCoord = new IconCoordinate[]{
                TextureRegistry.getTexture(activeKey + "_0"),
                TextureRegistry.getTexture(activeKey + "_14"),
                TextureRegistry.getTexture(activeKey + "_13"),
                TextureRegistry.getTexture(activeKey + "_12"),
                TextureRegistry.getTexture(activeKey + "_11"),
                TextureRegistry.getTexture(activeKey + "_10"),
                TextureRegistry.getTexture(activeKey + "_9"),
                TextureRegistry.getTexture(activeKey + "_8"),
                TextureRegistry.getTexture(activeKey + "_7"),
                TextureRegistry.getTexture(activeKey + "_6"),
                TextureRegistry.getTexture(activeKey + "_5"),
                TextureRegistry.getTexture(activeKey + "_4"),
                TextureRegistry.getTexture(activeKey + "_3"),
                TextureRegistry.getTexture(activeKey + "_2"),
                TextureRegistry.getTexture(activeKey + "_1"),
                TextureRegistry.getTexture(activeKey + "_15")
        };
    }

    public BlockModelConnectedTextureExtra(Block block, String key, String activeKey, String overlayKey, List<Block> connectsTo) {
        this(block, key, activeKey, connectsTo);
        hasOverbright = true;
        overlayTexture = TextureRegistry.getTexture(overlayKey);
    }

    @Override
    public IconCoordinate getBlockTexture(WorldSource world, int x, int y, int z, Side side) {
        if (world.getBlockMetadata(x,y,z) == 1) {
            int state = checkNeighbors(world, x, y, z);
            BitSet bits = intToBitSet(state, 6), subbits = new BitSet(4);
            for (int i = 0; i < 4; i++) {
                subbits.set(i, bits.get(relCoords[side.getId()][i]));
            }
            //find correct texture for a given face from a subset of 4 bits of the metadata
            return activeTexCoord[toInt(subbits)];
        }
        return super.getBlockTexture(world, x, y, z, side);
    }

    @Override
    public IconCoordinate getBlockOverbrightTexture(WorldSource world, int x, int y, int z, int side) {
        if (world.getBlockMetadata(x,y,z) == 1) {
            return overlayTexture;
        }
        return super.getBlockOverbrightTexture(world, x, y, z, side);
    }
}
