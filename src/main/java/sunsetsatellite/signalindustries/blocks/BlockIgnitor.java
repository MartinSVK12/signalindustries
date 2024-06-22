package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.inventories.TileEntityIgnitor;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockIgnitor extends BlockContainerTiered {

    public BlockIgnitor(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);

        withTags(SignalIndustries.SIGNALUM_CONDUITS_CONNECT);
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
        if (world.getBlock(x, y - 1, z) == this && world.getBlockMetadata(x, y - 1, z) != 1) {
            world.setBlockMetadataWithNotify(x, y, z, 1);
        } else {
            world.setBlockMetadataWithNotify(x, y, z, 0);
        }
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        world.setBlockTileEntity(i, j, k, this.getNewBlockEntity());
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityIgnitor();
    }
}
