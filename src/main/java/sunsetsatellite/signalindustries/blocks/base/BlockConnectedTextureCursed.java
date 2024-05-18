package sunsetsatellite.signalindustries.blocks.base;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.SignalIndustries;

public class BlockConnectedTextureCursed extends BlockTransparent {

    public String baseTexture;
    public BlockConnectedTextureCursed(String key, int id, Material material, String baseTexture) {
        super(key, id, material);
        this.baseTexture = baseTexture;
    }

    //what you're about to witness is probably the worst code I have ever written, I'm very sorry
    //please oh lord, forgive me
    /*@Override
    public int _getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        int index = Sides.orientationLookUpHorizontal[side.getId()];
        boolean up = blockAccess.getBlock(x,y+1,z) == this;
        boolean down = blockAccess.getBlock(x,y-1,z) == this;
        boolean north = blockAccess.getBlock(x,y,z-1) == this;
        boolean south = blockAccess.getBlock(x,y,z+1) == this;
        boolean east = blockAccess.getBlock(x+1,y,z) == this;
        boolean west = blockAccess.getBlock(x-1,y,z) == this;

        if(up && down && north && east && south && west){
            int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
            return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
        } else if (up && !down && !north && !east && !south && !west) {
            if(side.getAxis() != Axis.Y){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if (!up && down && !north && !east && !south && !west) {
            if(side.getAxis() != Axis.Y){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if (up && down && !north && !east && !south && !west) {
            if(side.getAxis() != Axis.Y){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM || side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if (!up && !down && !north && !east && south && !west) {
            if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side != Side.NORTH) {
                if(side == Side.TOP){
                    int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_bottom.png");
                    return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
                } else if(side == Side.BOTTOM){
                    int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_bottom.png");
                    return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
                } else if (side == Side.EAST) {
                    int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_left.png");
                    return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
                } else if (side == Side.WEST) {
                    int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_right.png");
                    return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
                }
            }
        } else if (!up && !down && north && !east && !south && !west) {
            if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side != Side.SOUTH) {
                if(side == Side.TOP){
                    int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_top.png");
                    return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
                } else if(side == Side.BOTTOM){
                    int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_top.png");
                    return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
                } else if (side == Side.EAST) {
                    int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_right.png");
                    return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
                }  else if (side == Side.WEST) {
                    int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_left.png");
                    return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
                }
            }
        } else if (!up && !down && north && !east && south && !west) {
            if(side == Side.NORTH || side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side != Side.TOP && side != Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if (up && !down && north && !east && !south && !west) {
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if (up && !down && !north && !east && south && !west) {
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if (!up && down && north && !east && !south && !west) {
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if (!up && down && !north && !east && south && !west) {
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if (up && down && !north && !east && south && !west) {
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if (up && down && north && !east && !south && !west) {
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if (!up && down && north && !east && south && !west) {
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if (up && !down && north && !east && south && !west) {
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if (up && down && north && !east && south && !west) {
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && !down && !north && east && !south && !west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+".png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && !down && !north && !east && !south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+".png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && !down && !north && east && !south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && !down && !north && !east && !south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && !down && !north && east && !south && !west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && down && !north && east && !south && !west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && down && !north && !east && !south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && down && !north && !east && !south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && down && !north && east && !south && !west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && down && !north && east && !south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && !down && !north && east && !south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && down && !north && east && !south && west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && !down && north && !east && !south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && !down && !north && east && south && !west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && down && !north && east && south && !west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && down && north && !east && !south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && down && !north && !east && south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && down && north && east && !south && !west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && !down && north && east && !south && !west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && !down && !north && !east && south && west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && !down && north && !east && !south && west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && !down && !north && east && south && !west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && !down && north && east && !south && !west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && !down && !north && !east && south && west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_no_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && !down && north && east && south && west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && !down && !north && east && south && west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && !down && north && east && !south && west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && !down && north && !east && south && west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && !down && north && east && south && !west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && down && north && !east && !south && west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && down && !north && east && south && !west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && down && north && east && !south && !west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.SOUTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && down && !north && !east && south && west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && !down && north && east && south && west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && down && north && east && south && west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && down && !north && east && south && west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && down && north && !east && south && west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && down && north && east && !south && west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(!up && down && north && east && south && !west){
            if(side == Side.BOTTOM){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.TOP) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && !down && north && east && south && !west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_left.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && !down && north && east && !south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && !down && north && !east && south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.NORTH){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_right.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.EAST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && !down && !north && east && south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.EAST){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_top.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_bottom.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && down && north && !east && south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.EAST){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && down && north && east && south && !west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.EAST){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && down && north && east && !south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.EAST){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        } else if(up && down && !north && east && south && west){
            if(side == Side.TOP){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if(side == Side.EAST){
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.BOTTOM) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.NORTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.WEST) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            } else if (side == Side.SOUTH) {
                int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+"_center.png");
                return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
            }
        }
        int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+".png");
        return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
    }

    @Override
    public int _getBlockTextureFromSideAndMetadata(Side side, int meta) {
        int index = Sides.orientationLookUpHorizontal[side.getId()];
        int[] t = TextureHelper.getOrCreateBlockTexture(SignalIndustries.MOD_ID,this.baseTexture+".png");
        return this.atlasIndices[index] = Block.texCoordToIndex(t[0],t[1]);
    }*/
}
