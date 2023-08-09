package sunsetsatellite.signalindustries.blocks;


import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.fluidapi.template.containers.ContainerFluidTank;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.gui.GuiEnergyCell;
import sunsetsatellite.signalindustries.inventories.TileEntityEnergyCell;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.sunsetutils.util.Direction;

import java.util.ArrayList;

public class BlockEnergyCell extends BlockContainerTiered {

    public BlockEnergyCell(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityEnergyCell();
    }

    @Override
    public void onBlockRemoval(World world, int i, int j, int k) {
        TileEntityEnergyCell tile = (TileEntityEnergyCell) world.getBlockTileEntity(i, j, k);
        if (tile != null) {
            for (Direction dir : Direction.values()) {
                TileEntity tile2 = dir.getTileEntity(world,tile);
                if (tile2 instanceof TileEntityFluidPipe) {
                    tile.unpressurizePipes((TileEntityFluidPipe) tile2,new ArrayList<>());
                }
            }
        }
        super.onBlockRemoval(world, i, j, k);
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isClientSide)
        {
            return true;
        } else
        {
            TileEntityEnergyCell tile = (TileEntityEnergyCell) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                SignalIndustries.displayGui(entityplayer,new GuiEnergyCell(entityplayer.inventory, tile),new ContainerFluidTank(entityplayer.inventory,tile),tile,i,j,k);
            }
            return true;
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
