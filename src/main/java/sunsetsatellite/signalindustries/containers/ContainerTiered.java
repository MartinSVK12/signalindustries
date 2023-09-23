package sunsetsatellite.signalindustries.containers;


import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.fluidapi.api.ContainerFluid;
import sunsetsatellite.signalindustries.inventories.TileEntityTieredContainer;
import sunsetsatellite.signalindustries.inventories.TileEntityTieredMachine;
import sunsetsatellite.signalindustries.util.Tier;

public class ContainerTiered extends ContainerFluid {
    public ContainerTiered(IInventory iInventory, TileEntityTieredContainer tileEntityTieredMachine) {
        super(iInventory, tileEntityTieredMachine);
    }

    protected TileEntityTieredContainer tile = (TileEntityTieredContainer) super.tile;
    protected TileEntityTieredMachine machine = (TileEntityTieredMachine) tile;
    public Tier tier = Tier.PROTOTYPE;
}
