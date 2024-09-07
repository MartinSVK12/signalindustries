package sunsetsatellite.signalindustries.blocks.machines;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockMachineBase;
import sunsetsatellite.signalindustries.containers.ContainerSignalumDynamo;
import sunsetsatellite.signalindustries.gui.GuiSignalumDynamo;
import sunsetsatellite.signalindustries.inventories.machines.TileEntitySignalumDynamo;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockSignalumDynamo extends BlockMachineBase {
    public BlockSignalumDynamo(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntitySignalumDynamo();
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean onBlockRightClicked(World world, int i, int j, int k, EntityPlayer entityplayer, Side side, double xHit, double yHit) {
        if (super.onBlockRightClicked(world, i, j, k, entityplayer, side, xHit, yHit)) {
            return true;
        }
        if (world.isClientSide) {
            return true;
        } else {
            TileEntitySignalumDynamo tile = (TileEntitySignalumDynamo) world.getBlockTileEntity(i, j, k);
            if (tile != null) {
                SignalIndustries.displayGui(entityplayer, () -> new GuiSignalumDynamo(entityplayer.inventory, tile), new ContainerSignalumDynamo(entityplayer.inventory, tile), tile, i, j, k);
            }
            return true;
        }
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        dropContents(world, x,y,z);
        super.onBlockRemoved(world, x, y, z, data);
    }

}
