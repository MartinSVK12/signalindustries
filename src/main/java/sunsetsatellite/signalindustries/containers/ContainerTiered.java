package sunsetsatellite.signalindustries.containers;

import net.minecraft.src.ICrafting;
import net.minecraft.src.IInventory;
import sunsetsatellite.fluidapi.api.ContainerFluid;
import sunsetsatellite.signalindustries.interfaces.mixins.IEntityPlayerMP;
import sunsetsatellite.signalindustries.tiles.TileEntityTiered;
import sunsetsatellite.signalindustries.tiles.TileEntityTieredMachine;
import sunsetsatellite.signalindustries.util.Tiers;

public class ContainerTiered extends ContainerFluid {
    public ContainerTiered(IInventory iInventory, TileEntityTiered tileEntityTieredMachine) {
        super(iInventory, tileEntityTieredMachine);
    }

    protected TileEntityTiered tile = (TileEntityTiered) super.tile;
    protected TileEntityTieredMachine machine = (TileEntityTieredMachine) tile;
    public Tiers tier = Tiers.PROTOTYPE;
}
