package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.client.Minecraft;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.screens.ScreenRedstoneClock;
import sunsetsatellite.signalindustries.tiles.TileEntityRedstoneClock;
import turniplabs.halplibe.helper.EnvironmentHelper;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class BlockLogicRedstoneClock extends BlockLogic {
    public BlockLogicRedstoneClock(Block<?> block, Material material) {
        super(block, material);
        block.withEntity(TileEntityRedstoneClock::new);
    }

    @Override
    public boolean isSignalSource() {
        return true;
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlockOnCondition(WorldSource world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean getDirectSignal(World worldSource, int x, int y, int z, Side side) {
        TileEntityRedstoneClock tile = (TileEntityRedstoneClock) worldSource.getTileEntity(x, y, z);
        return tile != null && tile.active;
    }

    @Override
    public boolean getSignal(WorldSource world, int x, int y, int z, Side side) {
        TileEntityRedstoneClock tile = (TileEntityRedstoneClock) world.getTileEntity(x, y, z);
        return tile != null && tile.active;
    }

    @Override
    public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xHit, double yHit) {
        if (world.isClientSide) {
            return true;
        }
        if(player.isSneaking()){
            TileEntityRedstoneClock tile = (TileEntityRedstoneClock) world.getTileEntity(x, y, z);
            if(tile != null){
                tile.disabled = !tile.disabled;
            }
        } else {
            TileEntityRedstoneClock tile = (TileEntityRedstoneClock) world.getTileEntity(x, y, z);
            if(tile != null){
                Catalyst.displayGui(player, tile, key("gui/redstone_clock"));
            }
        }
        return true;
    }
}
