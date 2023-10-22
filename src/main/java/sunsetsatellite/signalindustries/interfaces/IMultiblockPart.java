package sunsetsatellite.signalindustries.interfaces;

import net.minecraft.core.block.entity.TileEntity;

public interface IMultiblockPart {
    boolean isConnected();

    TileEntity getConnectedTileEntity();

    boolean connect(TileEntity tileEntity);
}
