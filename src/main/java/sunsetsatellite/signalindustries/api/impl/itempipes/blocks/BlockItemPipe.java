package sunsetsatellite.signalindustries.api.impl.itempipes.blocks;

import net.minecraft.src.BlockContainerRotatable;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import sunsetsatellite.signalindustries.api.impl.itempipes.tiles.TileEntityItemPipe;

public class BlockItemPipe extends BlockContainerRotatable {
    public BlockItemPipe(int i, Material material) {
        super(i, material);
    }

    public int getRenderType() {
        return 33;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }


    @Override
    public void onBlockRemoval(World world, int i, int j, int k) {
        TileEntityItemPipe tile = (TileEntityItemPipe) world.getBlockTileEntity(i,j,k);
        tile.destroyItems();
        super.onBlockRemoval(world, i, j, k);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityItemPipe();
    }
}
