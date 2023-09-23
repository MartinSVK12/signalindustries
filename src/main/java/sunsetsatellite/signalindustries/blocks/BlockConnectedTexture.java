package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.SignalIndustries;
import turniplabs.halplibe.helper.TextureHelper;

import java.util.BitSet;

//thanks Felix170905
public class BlockConnectedTexture extends BlockTransparent {

    public BlockConnectedTexture(String key, int id) {
        super(key, id, Material.glass);
    }

    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        switch (dropCause) {
            case SILK_TOUCH:
            case PICK_BLOCK:
                return new ItemStack[]{new ItemStack(this)};
            default:
                return null;
        }
    }

    private int checkNeighbors(WorldSource world, int x, int y, int z) {
        int state = 0;
        for (Side s : Side.values()) {
            if (s != Side.NONE) {
                int j = x + s.getOffsetX(), k = y + s.getOffsetY(), l = z + s.getOffsetZ(); //offset coords
                if (world.getBlockId(j, k, l) == this.id) state += (int) Math.pow(2, s.getId());
            }
        }
        //world.setBlockMetadata(x,y,z,(state & 0x3F));
        return state & 0x3F;
    }

    private int t(String tex) { //get index from texture name string
        return texCoordToIndex(TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID, tex)[0], TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID, tex)[1]);
    }

    private final int[] texCoord = { // I gave up on the layering idea, sadly
            t("clearGlass.png"),
            t("clearglass14.png"),
            t("clearglass13.png"),
            t("clearglass12.png"),
            t("clearglass11.png"),
            t("clearglass10.png"),
            t("clearglass9.png"),
            t("clearglass8.png"),
            t("clearglass7.png"),
            t("clearglass6.png"),
            t("clearglass5.png"),
            t("clearglass4.png"),
            t("clearglass3.png"),
            t("clearglass2.png"),
            t("clearglass1.png"),
            texCoordToIndex(13, 9)
    };

    private static final int[][] relCoords = {
            {2, 5, 3, 4}, //up 0
            {2, 5, 3, 4}, //down 1
            {1, 4, 0, 5}, //north 2
            {1, 5, 0, 4}, //south 3
            {1, 3, 0, 2}, //east 4
            {1, 2, 0, 3} //west 5
    };


    @Override
    public int getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        int meta = checkNeighbors(blockAccess, x, y, z);
        BitSet bits = intToBitSet(meta,6), subbits = new BitSet(4);
        for (int i=0;i<4;i++) {subbits.set(i,bits.get(relCoords[side.getId()][i]));}
        //find correct texture for a given face from a subset of 4 bits of the metadata
        return texCoord[toInt(subbits)];
    }

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
