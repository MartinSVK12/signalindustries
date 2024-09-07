package sunsetsatellite.signalindustries.blocks.machines;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockMachineBase;
import sunsetsatellite.signalindustries.containers.ContainerCollector;
import sunsetsatellite.signalindustries.gui.GuiCollector;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityCollector;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockCollector extends BlockMachineBase {
    public BlockCollector(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);

    }

    @Override
    public void onBlockRemoved(World world, int i, int j, int k, int data) {
        dropContents(world, i, j, k);

        super.onBlockRemoved(world, i, j, k, data);
    }

    @Override
    public boolean onBlockRightClicked(World world, int i, int j, int k, EntityPlayer entityplayer, Side side, double xHit, double yHit) {
        if (super.onBlockRightClicked(world, i, j, k, entityplayer, side, xHit, yHit)) {
            return true;
        }
        if (world.isClientSide) {
            return true;
        } else {
            TileEntityCollector tile = (TileEntityCollector) world.getBlockTileEntity(i, j, k);
            if (tile != null) {
                SignalIndustries.displayGui(entityplayer, () -> new GuiCollector(entityplayer.inventory, tile), new ContainerCollector(entityplayer.inventory, tile), tile, i, j, k);
            }
            return true;
        }
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityCollector();
    }
}
