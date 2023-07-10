package sunsetsatellite.signalindustries.containers;

import net.minecraft.src.IInventory;
import sunsetsatellite.fluidapi.api.ContainerFluid;
import sunsetsatellite.signalindustries.tiles.TileEntityTiered;
import sunsetsatellite.signalindustries.tiles.TileEntityTieredMachine;
import sunsetsatellite.signalindustries.util.Tier;

public class ContainerTiered extends ContainerFluid {
    public ContainerTiered(IInventory iInventory, TileEntityTiered tileEntityTieredMachine) {
        super(iInventory, tileEntityTieredMachine);
    }

    protected TileEntityTiered tile = (TileEntityTiered) super.tile;
    protected TileEntityTieredMachine machine = (TileEntityTieredMachine) tile;
    public Tier tier = Tier.PROTOTYPE;
}
