package sunsetsatellite.signalindustries.blocks.machines;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockMachineBase;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityBlockBreaker;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockBreaker extends BlockMachineBase {

    public BlockBreaker(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityBlockBreaker();
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
