package sunsetsatellite.signalindustries.tiles;

import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.util.Tiers;

public class TileEntityTieredMachine extends TileEntityTiered {
    public int fuelBurnTicks = 0;
    public int fuelMaxBurnTicks = 0;
    public int progressTicks = 0;
    public int progressMaxTicks = 200;
    public int efficiency = 1;
    public int speedMultiplier = 1;
    public int cost = 0;

    //TODO: Generify code for all machines
}
