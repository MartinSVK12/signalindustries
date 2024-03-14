package sunsetsatellite.signalindustries.blocks;


import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.containers.ContainerFluidTank;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.gui.GuiEnergyCell;
import sunsetsatellite.signalindustries.inventories.TileEntityEnergyCell;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.util.IOPreview;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.TextureHelper;

import java.util.ArrayList;

public class BlockEnergyCell extends BlockContainerTiered {

    public BlockEnergyCell(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
        withOverbright();
    }

    @Override
    public int getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
        TileEntityEnergyCell tile = (TileEntityEnergyCell) blockAccess.getBlockTileEntity(x,y,z);
        if(tile.preview != IOPreview.NONE){
            Direction dir = Direction.getDirectionFromSide(side);
            Connection con = Connection.NONE;
            switch (tile.preview){
                case ITEM: {
                    con = tile.itemConnections.get(dir);
                    break;
                }
                case FLUID: {
                    con = tile.connections.get(dir);
                    break;
                }
            }
            switch (con){
                case INPUT:
                    return TextureHelper.getOrCreateBlockTextureIndex(SignalIndustries.MOD_ID,"input_overlay.png");
                case OUTPUT:
                    return TextureHelper.getOrCreateBlockTextureIndex(SignalIndustries.MOD_ID,"output_overlay.png");
                case BOTH:
                    return TextureHelper.getOrCreateBlockTextureIndex(SignalIndustries.MOD_ID,"both_io_overlay.png");
                case NONE:
                    return -1;
            }
        } else {
            return -1;
        }
        return -1;
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityEnergyCell();
    }

    @Override
    public void onBlockRemoved(World world, int i, int j, int k, int data) {
        TileEntityEnergyCell tile = (TileEntityEnergyCell) world.getBlockTileEntity(i, j, k);
        if (tile != null) {
            for (Direction dir : Direction.values()) {
                TileEntity tile2 = dir.getTileEntity(world,tile);
                if (tile2 instanceof TileEntityFluidPipe) {
                    tile.unpressurizePipes((TileEntityFluidPipe) tile2,new ArrayList<>());
                }
            }
        }
        super.onBlockRemoved(world, i, j, k, data);
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
    public boolean isSolidRender() {
        return false;
    }
}
