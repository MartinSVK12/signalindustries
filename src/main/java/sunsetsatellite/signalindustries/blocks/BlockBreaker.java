package sunsetsatellite.signalindustries.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.inventories.TileEntityBlockBreaker;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockBreaker extends BlockContainerTiered{

    public BlockBreaker(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityBlockBreaker();
    }

    @Override
    public int getBlockTexture(WorldSource iblockaccess, int i, int j, int k, Side side) {
        TileEntityBlockBreaker tile = (TileEntityBlockBreaker) iblockaccess.getBlockTileEntity(i,j,k);
        int meta = iblockaccess.getBlockMetadata(i,j,k);
        /*
        this.atlasIndices[1] = texCoordToIndex(topX, topY);
        this.atlasIndices[0] = texCoordToIndex(bottomX, bottomY);
        this.atlasIndices[4] = texCoordToIndex(northX, northY); //front face
        this.atlasIndices[2] = texCoordToIndex(eastX, eastY);
        this.atlasIndices[5] = texCoordToIndex(southX, southY);
        this.atlasIndices[3] = texCoordToIndex(westX, westY);
         */
        int index = Sides.orientationLookUpHorizontal[6 * meta + side.getId()];
        int offset = tier.ordinal()+1;
        if(tier.ordinal() == 0){
            offset = 0;
        }
        if(index == 2){
            if(tile.isActive()){
                return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.breakerTex[1+offset][0],SignalIndustries.breakerTex[1+offset][1]);
            }
            return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.breakerTex[offset][0],SignalIndustries.breakerTex[offset][1]);
        }
        if((index == 1 || index == 0) && (meta == 2 || meta == 3)){
            if(tile.isActive()){
                return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.breakerTex[5+offset][0],SignalIndustries.breakerTex[5+offset][1]);
            }
            return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.breakerTex[4+offset][0],SignalIndustries.breakerTex[4+offset][1]);
        }
        if(index != 5){
            if(tile.isActive()){
                return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.breakerTex[3+offset][0],SignalIndustries.breakerTex[3+offset][1]);
            }
            return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.breakerTex[2+offset][0],SignalIndustries.breakerTex[2+offset][1]);
        } else {
            return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.breakerTex[6][0],SignalIndustries.breakerTex[6][1]);
        }
        //return this.atlasIndices[index];
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
        if(l > 0 && Block.blocksList[l].canProvidePower())
        {
            TileEntityBlockBreaker tile = (TileEntityBlockBreaker) world.getBlockTileEntity(i,j,k);
            tile.active = world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k);
            tile.blockBroken = false;
        }
    }
}
