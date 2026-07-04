package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicConduitBase;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityItemConduit;
import sunsetsatellite.signalindustries.util.PipeMode;
import sunsetsatellite.signalindustries.util.PipeType;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.function.Supplier;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class BlockLogicItemConduit extends BlockLogicConduitBase {

    public PipeType type;

    public BlockLogicItemConduit(Block<?> block, Material material, Tier tier, PipeType type, Supplier<TileEntity> tileEntitySupplier) {
        super(block, material, tier, tileEntitySupplier, ConduitCapability.ITEM);
        this.type = type;
    }

    @Override
    public boolean onBlockRightClicked(World world, int i, int j, int k, Player entityplayer, Side side, double xHit, double yHit) {
        if (super.onBlockRightClicked(world, i, j, k, entityplayer, side, xHit, yHit)) {
            return true;
        }
        if (entityplayer.isSneaking() && type == PipeType.NORMAL && !world.isClientSide) {
            TileEntityItemConduit tile = (TileEntityItemConduit) world.getTileEntity(i, j, k);
            tile.mode = PipeMode.values()[tile.mode.ordinal() + 1 <= PipeMode.values().length - 1 ? tile.mode.ordinal() + 1 : 0];
            entityplayer.sendMessage("Pipe mode changed to: " + tile.mode);
            return true;
        }
        if (!EnvironmentHelper.isServerEnvironment() && type == PipeType.RESTRICT) {
            TileEntityItemConduit tile = (TileEntityItemConduit) world.getTileEntity(i, j, k);
            Catalyst.displayGui(entityplayer, tile, key("gui/restrict_item_conduit"));
            return true;
        }
        if (!world.isClientSide && type == PipeType.SENSOR) {
            TileEntityItemConduit tile = (TileEntityItemConduit) world.getTileEntity(i, j, k);
            Catalyst.displayGui(entityplayer, tile, key("gui/sensor_item_conduit"));
            return true;
        }
        return false;
    }

    @Override
    public boolean isSignalSource() {
        return true;
    }

    @Override
    public boolean getSignal(WorldSource worldSource, int x, int y, int z, Side side) {
        TileEntityItemConduit tile = (TileEntityItemConduit) worldSource.getTileEntity(x, y, z);
        return tile != null && tile.type == PipeType.SENSOR && tile.sensorActive;
    }

    @Override
    public boolean getDirectSignal(World world, int x, int y, int z, Side side) {
        TileEntityItemConduit tile = (TileEntityItemConduit) world.getTileEntity(x, y, z);
        return tile != null && tile.type == PipeType.SENSOR && tile.sensorActive;
    }
}
