package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBdc;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BlockModelConnectedTexture extends BlockModelFullbright {
    public List<Block<?>> connectsTo;
    protected final IconCoordinate[] texCoord;
	private final boolean renderInside;

    protected static final int[][] relCoords = {
            {2, 5, 3, 4}, //up 0
            {2, 5, 3, 4}, //down 1
            {1, 4, 0, 5}, //north 2
            {1, 5, 0, 4}, //south 3
            {1, 3, 0, 2}, //east 4
            {1, 2, 0, 3} //west 5
    };

	public BlockModelConnectedTexture(Block<? extends BlockLogic> block,  String key) {
		this(block, key, new ArrayList<>());
	}


	public BlockModelConnectedTexture(Block<? extends BlockLogic> block, String key, List<Block<?>> connectsTo) {
		super(block);
		this.renderInside = false;
		this.connectsTo = connectsTo;
		texCoord = new IconCoordinate[]{
			TextureRegistry.getTexture(key + "_0"),
			TextureRegistry.getTexture(key + "_14"),
			TextureRegistry.getTexture(key + "_13"),
			TextureRegistry.getTexture(key + "_12"),
			TextureRegistry.getTexture(key + "_11"),
			TextureRegistry.getTexture(key + "_10"),
			TextureRegistry.getTexture(key + "_9"),
			TextureRegistry.getTexture(key + "_8"),
			TextureRegistry.getTexture(key + "_7"),
			TextureRegistry.getTexture(key + "_6"),
			TextureRegistry.getTexture(key + "_5"),
			TextureRegistry.getTexture(key + "_4"),
			TextureRegistry.getTexture(key + "_3"),
			TextureRegistry.getTexture(key + "_2"),
			TextureRegistry.getTexture(key + "_1"),
			TextureRegistry.getTexture(key + "_15")
		};
		setAllTextures(TextureRegistry.getTexture(key + "_0"));
	}

	@Override
	public @Nullable IconCoordinate getBlockTexture(@NotNull WorldSource world, @NotNull TilePosc tilePos, @NotNull Side side) {
		int state = checkNeighbors(world,tilePos);
		BitSet bits = intToBitSet(state, 6), subbits = new BitSet(4);
		for (int i = 0; i < 4; i++) {
			subbits.set(i, bits.get(relCoords[side.id][i]));
		}
		return texCoord[toInt(subbits)];
	}

    protected int checkNeighbors(@NotNull WorldSource world, @NotNull TilePosc tilePos) {
        int state = 0;
		for (Side side : Side.values()) {
			if(side != Side.NONE){
				TilePos pos = new TilePos(tilePos).add(side.direction);
				if(world.getBlockType(pos) == block || connectsTo.contains(world.getBlockType(pos))){
					state += (int) Math.pow(2, side.id);
				}
			}
		}
        return state & 0x3F;
    }

    public static int toInt(BitSet s) {
        int v = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.get(i)) v |= (1 << i);
        }
        return v;
    }

    public static BitSet intToBitSet(int v, int l) {
        BitSet b = new BitSet(l);
        int i = 0;
        while (v != 0) {
            if (v % 2 != 0) b.set(i);
            ++i;
            v = v >>> 1;
        }
        return b;
    }

	@Override
	public boolean shouldSideBeRendered(@NotNull WorldSource source, @NotNull AABBdc bounds, @NotNull TilePosc tilePos, @NotNull Side side) {
		if (!this.renderInside && source.getBlockType(tilePos) == this.block) {
			return false;
		} else {
			return super.shouldSideBeRendered(source, bounds, tilePos, side);
		}
	}
}
