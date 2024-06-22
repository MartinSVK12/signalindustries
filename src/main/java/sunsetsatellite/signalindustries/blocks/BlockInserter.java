package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.inventories.TileEntityInserter;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockInserter extends BlockContainerTiered {

    public BlockInserter(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
        withTags(SignalIndustries.ITEM_CONDUITS_CONNECT);
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
        world.setBlockMetadataWithNotify(x, y, z, entity.getPlacementDirection(side).getOpposite().getId());
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityInserter();
    }
}
