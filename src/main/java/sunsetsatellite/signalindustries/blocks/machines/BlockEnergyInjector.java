package sunsetsatellite.signalindustries.blocks.machines;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockMachineBase;
import sunsetsatellite.signalindustries.containers.ContainerEnergyInjector;
import sunsetsatellite.signalindustries.gui.GuiEnergyInjector;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityEnergyInjector;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockEnergyInjector extends BlockMachineBase {
    public BlockEnergyInjector(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityEnergyInjector();
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
            TileEntityEnergyInjector tile = (TileEntityEnergyInjector) world.getBlockTileEntity(i, j, k);
            if (tile != null) {
                SignalIndustries.displayGui(entityplayer, () -> new GuiEnergyInjector(entityplayer.inventory, tile), new ContainerEnergyInjector(entityplayer.inventory, tile), tile, i, j, k);
            }
            return true;
        }
    }

    @Override
    public void onBlockRemoved(World world, int i, int j, int k, int data) {
        dropContents(world, i, j, k);

        super.onBlockRemoved(world, i, j, k, data);
    }
}
