package sunsetsatellite.signalindustries.api.impl.itempipes.blocks;


import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.itempipes.tiles.TileEntityInserter;
import sunsetsatellite.signalindustries.api.impl.itempipes.tiles.TileEntityItemPipe;
import sunsetsatellite.sunsetutils.util.Direction;

public class BlockInserter extends BlockContainerRotatable {
    public BlockInserter(int i, Material material) {
        super(i, material);
    }

    public void onBlockPlaced(World world, int x, int y, int z, net.minecraft.src.helper.Direction side, EntityLiving player, double sideHeight) {
        world.setBlockMetadataWithNotify(x, y, z, player.getPlacementDirection(side).meta());
    }

    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        if(side == meta){
            return this.atlasIndices[4];
        }
        Direction dir = Direction.getDirectionFromSide(meta).getOpposite();
        if(side == dir.getSide()){
            return this.atlasIndices[5];
        } else {
            return this.atlasIndices[0];
        }
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityInserter();
    }
}
