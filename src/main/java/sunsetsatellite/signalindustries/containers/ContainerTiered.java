package sunsetsatellite.signalindustries.containers;


import sunsetsatellite.fluidapi.api.ContainerFluid;
import sunsetsatellite.signalindustries.inventories.TileEntityTiered;
import sunsetsatellite.signalindustries.inventories.TileEntityTieredMachine;
import sunsetsatellite.signalindustries.util.Tier;

public class ContainerTiered extends ContainerFluid {
    public ContainerTiered(IInventory iInventory, TileEntityTiered tileEntityTieredMachine) {
        super(iInventory, tileEntityTieredMachine);
    }

    protected TileEntityTiered tile = (TileEntityTiered) super.tile;
    protected TileEntityTieredMachine machine = (TileEntityTieredMachine) tile;
    public Tier tier = Tier.PROTOTYPE;
}
