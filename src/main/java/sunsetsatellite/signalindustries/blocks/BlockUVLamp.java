package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.inventories.TileEntityUVLamp;


import java.util.Random;
import java.util.stream.Collectors;

public class BlockUVLamp extends BlockTileEntity {
    public BlockUVLamp(String key, int id, Material material) {
        super(key, id, material);
        hasOverbright = true;
    }

    @Override
    public int tickRate() {
        return 1;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        SignalIndustries.uvLamps.add(new BlockInstance(this,new Vec3i(x,y,z),null));
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        SignalIndustries.uvLamps.removeIf((B)->B.pos.equals(new Vec3i(x,y,z)));
        super.onBlockRemoved(world, x, y, z, data);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityUVLamp();
    }

    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
		if(world.isBlockIndirectlyGettingPowered(i, j, k))
		{
			world.setBlockMetadataWithNotify(i,j,k,1);
		} else if (!world.isBlockIndirectlyGettingPowered(i, j, k)) {
            world.setBlockMetadataWithNotify(i,j,k,0);
		}
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
		if(l > 0 && Block.blocksList[l].canProvidePower())
		{
			boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k);
			boolean flag2 = !world.isBlockIndirectlyGettingPowered(i, j, k) && !world.isBlockIndirectlyGettingPowered(i, j + 1, k);
			if(flag || flag2)
			{
				world.scheduleBlockUpdate(i, j, k, id, tickRate());
			}
		}
    }
}
