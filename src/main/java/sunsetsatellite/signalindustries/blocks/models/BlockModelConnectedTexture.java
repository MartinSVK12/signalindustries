package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.block.model.BlockModelTransparent;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.util.helper.TexturePackJsonHelper;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;

import java.util.BitSet;

//thanks Felix170905 & Useless
public class BlockModelConnectedTexture extends BlockModelTransparent<Block> {
    public BlockModelConnectedTexture(Block block, String key) {
        super(block,false);
        texCoord = new IconCoordinate[]{
                TextureRegistry.getTexture(key+"_0"),
                TextureRegistry.getTexture(key+"_14"),
                TextureRegistry.getTexture(key+"_13"),
                TextureRegistry.getTexture(key+"_12"),
                TextureRegistry.getTexture(key+"_11"),
                TextureRegistry.getTexture(key+"_10"),
                TextureRegistry.getTexture(key+"_9"),
                TextureRegistry.getTexture(key+"_8"),
                TextureRegistry.getTexture(key+"_7"),
                TextureRegistry.getTexture(key+"_6"),
                TextureRegistry.getTexture(key+"_5"),
                TextureRegistry.getTexture(key+"_4"),
                TextureRegistry.getTexture(key+"_3"),
                TextureRegistry.getTexture(key+"_2"),
                TextureRegistry.getTexture(key+"_1"),
                TextureRegistry.getTexture(key+"_15")
        };
    }

    private static final int[][] relCoords = {
            {2, 5, 3, 4}, //up 0
            {2, 5, 3, 4}, //down 1
            {1, 4, 0, 5}, //north 2
            {1, 5, 0, 4}, //south 3
            {1, 3, 0, 2}, //east 4
            {1, 2, 0, 3} //west 5
    };

    @Override
    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        int meta = checkNeighbors(blockAccess, x, y, z);
        BitSet bits = intToBitSet(meta,6), subbits = new BitSet(4);
        for (int i=0;i<4;i++) {subbits.set(i,bits.get(relCoords[side.getId()][i]));}
        //find correct texture for a given face from a subset of 4 bits of the metadata
        return texCoord[toInt(subbits)];
    }

    private int checkNeighbors(WorldSource world, int x, int y, int z) {
        int state = 0;
        for (Side s : Side.values()) {
            if (s != Side.NONE) {
                int j = x + s.getOffsetX(), k = y + s.getOffsetY(), l = z + s.getOffsetZ(); //offset coords
                if (world.getBlockId(j, k, l) == block.id) state += (int) Math.pow(2, s.getId());
            }
        }
        return state & 0x3F;
    }

    private final IconCoordinate[] texCoord;

    public static int toInt(BitSet s) {
        int v = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.get(i))v |= (1 << i);
        }
        return v;
    }
    public static BitSet intToBitSet(int v, int l){
        BitSet b=new BitSet(l);int i=0;
        while (v != 0) {if (v % 2 != 0)b.set(i);++i;v=v>>>1;}
        return b;
    }
}
