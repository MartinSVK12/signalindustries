package sunsetsatellite.signalindustries.blocks;


import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.inventories.TileEntityWrathBeacon;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockWrathBeacon extends BlockContainerTiered {
    public BlockWrathBeacon(int i, Tier tier, Material material) {
        super(i, tier, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityWrathBeacon();
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityWrathBeacon tile = (TileEntityWrathBeacon) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                tile.activate();
            }
            return true;
        }
    }

    @Override
    public void onBlockRemoval(World world, int i, int j, int k) {
        TileEntityWrathBeacon tile = (TileEntityWrathBeacon) world.getBlockTileEntity(i,j,k);
        if(tile != null && tile.active){
            for (EntityPlayer player : world.players) {
                player.addChatMessage("Challenge failed!");
            }
            //world.newExplosion(null,i,j,k,5f,false,false);
        }

        super.onBlockRemoval(world, i, j, k);
    }

    @Override
    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int side) {
        TileEntityWrathBeacon tile = (TileEntityWrathBeacon) iblockaccess.getBlockTileEntity(i,j,k);
        int meta = iblockaccess.getBlockMetadata(i,j,k);
        /*
        this.atlasIndices[1] = texCoordToIndex(topX, topY);
        this.atlasIndices[0] = texCoordToIndex(bottomX, bottomY);
        this.atlasIndices[4] = texCoordToIndex(northX, northY);
        this.atlasIndices[2] = texCoordToIndex(eastX, eastY);
        this.atlasIndices[5] = texCoordToIndex(southX, southY);
        this.atlasIndices[3] = texCoordToIndex(westX, westY);
         */
        int index = Sides.orientationLookUp[6 * meta + side];
        if(index > 1 && index < 6){
            if(tile.active){
                return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.wrathBeaconTex[1][0],SignalIndustries.wrathBeaconTex[1][1]);
            }
            return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.wrathBeaconTex[0][0],SignalIndustries.wrathBeaconTex[0][1]);
        }
        return this.atlasIndices[index];
    }
}
