package sunsetsatellite.signalindustries.tiles.base;

import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Direction;

public abstract class TileEntityTieredEnergyGenerator extends TileEntityTieredEnergyMachine {
    @Override
    public boolean canReceive(@NotNull Direction dir) {
        return false;
    }

    @Override
    public boolean canProvide(@NotNull Direction dir) {
        return true;
    }

    @Override
    public long receiveEnergy(@NotNull Direction dir, long energy) {
        return 0;
    }

    public long generateEnergy(long generated) {
        long energyGenerated = Catalyst.multiMin(generated, maxReceive, getCapacityRemaining());
        internalChangeEnergy(energyGenerated);
        return energyGenerated;
    }

}
