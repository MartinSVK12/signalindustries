package sunsetsatellite.signalindustries.blocks;


import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityWrathBeacon;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockWrathBeacon extends BlockContainerTiered {

    public BlockWrathBeacon(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityWrathBeacon();
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
        if(world.isClientSide)
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
    public void onBlockRemoved(World world, int i, int j, int k, int data) {
        TileEntityWrathBeacon tile = (TileEntityWrathBeacon) world.getBlockTileEntity(i,j,k);
        if(tile != null && tile.active){
            for (EntityPlayer player : world.players) {
                player.addChatMessage("Challenge failed!");
            }
            //world.newExplosion(null,i,j,k,5f,false,false);
        }

        super.onBlockRemoved(world, i, j, k, data);
    }

    @Override
    public int getBlockTexture(WorldSource iblockaccess, int i, int j, int k, Side side) {
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
        int index = Sides.orientationLookUpHorizontal[6 * meta + side.getId()];
        if(index > 1 && index < 6){
            if(tile.active){
                return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.wrathBeaconTex[1][0],SignalIndustries.wrathBeaconTex[1][1]);
            }
            return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.wrathBeaconTex[0][0],SignalIndustries.wrathBeaconTex[0][1]);
        }
        return this.atlasIndices[index];
    }
}
