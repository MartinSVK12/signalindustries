package sunsetsatellite.signalindustries.blocks;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import sunsetsatellite.fluidapi.template.containers.ContainerFluidTank;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.gui.GuiSIFluidTank;
import sunsetsatellite.signalindustries.tiles.TileEntitySIFluidTank;

import java.util.ArrayList;

public class BlockSIFluidTank extends BlockContainerTiered {
    public BlockSIFluidTank(int i, Tier tier, Material material) {
        super(i, tier, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntitySIFluidTank();
    }

    @Override
    public void onBlockRemoval(World world, int i, int j, int k) {
        TileEntitySIFluidTank tile = (TileEntitySIFluidTank) world.getBlockTileEntity(i, j, k);
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
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntitySIFluidTank tile = (TileEntitySIFluidTank) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                SignalIndustries.displayGui(entityplayer,new GuiSIFluidTank(entityplayer.inventory, tile),new ContainerFluidTank(entityplayer.inventory,tile),tile,i,j,k);
            }
            return true;
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
