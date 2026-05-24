package sunsetsatellite.signalindustries.tiles.base;

import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.signalindustries.interfaces.INamedTileEntity;

public abstract class TileEntityWithName extends TileEntity implements INamedTileEntity {
    public abstract String getName();
}
