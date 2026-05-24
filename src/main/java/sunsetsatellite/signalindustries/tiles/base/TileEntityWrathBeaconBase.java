package sunsetsatellite.signalindustries.tiles.base;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;
import sunsetsatellite.signalindustries.util.Tier;

public abstract class TileEntityWrathBeaconBase extends TileEntity implements IActiveForm {

    public Tier tier = Tier.BASIC;
    public boolean active = false;

    public abstract void activate(Player activator);

    @Override
    public boolean isBurning() {
        return active;
    }
}
