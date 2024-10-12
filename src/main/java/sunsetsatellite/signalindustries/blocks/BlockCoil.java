package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class BlockCoil extends Block {
    public BlockCoil(String key, int id, Material material) {
        super(key, id, material);
    }

    public void setDefaultDirection(World world, int i, int j, int k)
    {
        if(world.isClientSide)
        {
            return;
        }
        int l = world.getBlockId(i, j, k - 1);
        int i1 = world.getBlockId(i, j, k + 1);
        int j1 = world.getBlockId(i - 1, j, k);
        int k1 = world.getBlockId(i + 1, j, k);
        byte byte0 = 3;
        if(Block.solid[l] && !Block.solid[i1])
        {
            byte0 = 3;
        }
        if(Block.solid[i1] && !Block.solid[l])
        {
            byte0 = 2;
        }
        if(Block.solid[j1] && !Block.solid[k1])
        {
            byte0 = 5;
        }
        if(Block.solid[k1] && !Block.solid[j1])
        {
            byte0 = 4;
        }
        world.setBlockMetadataWithNotify(i, j, k, byte0);
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
        world.setBlockMetadataWithNotify(x, y, z, entity.getPlacementDirection(side).getOpposite().getId());
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
        setDefaultDirection(world, x, y, z);
    }


}
