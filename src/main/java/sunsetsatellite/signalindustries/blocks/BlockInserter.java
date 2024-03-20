package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.Direction;
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
    public int getBlockTextureFromSideAndMetadata(Side side, int meta) {

        if(SignalIndustries.textures == null) return this.atlasIndices[side.getId()];
        int index;
        int[] orientationLookUpVertical = new int[]{1, 0, 2, 3, 4, 5, /**/ 0, 1, 2, 3, 4, 5};
        if(meta == 0 || meta == 1){
            index = orientationLookUpVertical[6 * meta + side.getId()];
            return SignalIndustries.textures.get(tier.name()+".inserter.vertical").getTexture(Side.getSideById(index));
        } else {
            index = Sides.orientationLookUpHorizontal[6 * meta + side.getId()];
        }
        return this.atlasIndices[index];
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
