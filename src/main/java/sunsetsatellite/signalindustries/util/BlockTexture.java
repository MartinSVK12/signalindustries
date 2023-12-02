package sunsetsatellite.signalindustries.util;

import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.catalyst.core.util.Direction;
import turniplabs.halplibe.helper.TextureHelper;

public class BlockTexture {
    private int[] topTexture;
    private int[] bottomTexture;
    private int[] northTexture;
    private int[] eastTexture;
    private int[] southTexture;
    private int[] westTexture;
    private String modId;

    public BlockTexture(String modId){
        this.modId = modId;
    }

    public BlockTexture setAll(String texture){
        this.topTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        this.bottomTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        this.northTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        this.eastTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        this.southTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        this.westTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        return this;
    }

    public BlockTexture setSides(String texture){
        this.northTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        this.eastTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        this.southTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        this.westTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        return this;
    }

    public BlockTexture setTopAndBottom(String texture){
        this.topTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        this.bottomTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        return this;
    }

    public BlockTexture setTopTexture(String texture) {
        this.topTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        return this;
    }

    public BlockTexture setBottomTexture(String texture) {
        this.bottomTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        return this;
    }

    public BlockTexture setNorthTexture(String texture) {
        this.northTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        return this;
    }

    public BlockTexture setEastTexture(String texture) {
        this.eastTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        return this;
    }

    public BlockTexture setSouthTexture(String texture) {
        this.southTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        return this;
    }

    public BlockTexture setWestTexture(String texture) {
        this.westTexture = TextureHelper.getOrCreateBlockTexture(modId,texture);
        return this;
    }

    public BlockTexture setTexture(Direction dir, String texture){
        switch (dir){
            case X_POS:
                setEastTexture(texture);
                break;
            case X_NEG:
                setWestTexture(texture);
                break;
            case Y_POS:
                setTopTexture(texture);
                break;
            case Y_NEG:
                setBottomTexture(texture);
                break;
            case Z_POS:
                setSouthTexture(texture);
                break;
            case Z_NEG:
                setNorthTexture(texture);
                break;
        }
        return this;
    }

    public int getTexture(Side side){
        switch (side){
            case TOP:
                return Block.texCoordToIndex(topTexture[0],topTexture[1]);
            case BOTTOM:
                return Block.texCoordToIndex(bottomTexture[0],bottomTexture[1]);
            case NORTH:
                return Block.texCoordToIndex(northTexture[0],northTexture[1]);
            case EAST:
                return Block.texCoordToIndex(eastTexture[0],eastTexture[1]);
            case SOUTH:
                return Block.texCoordToIndex(southTexture[0],southTexture[1]);
            case WEST:
                return Block.texCoordToIndex(westTexture[0],westTexture[1]);
            case NONE:
                return 0;
        }
        return 0;
    }
}
