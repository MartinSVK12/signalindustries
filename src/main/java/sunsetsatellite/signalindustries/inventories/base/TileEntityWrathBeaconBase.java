package sunsetsatellite.signalindustries.inventories.base;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;
import sunsetsatellite.signalindustries.util.Tier;

public abstract class TileEntityWrathBeaconBase extends TileEntity implements IActiveForm {

    public Tier tier = Tier.BASIC;
    public boolean active = false;

    public abstract void activate(EntityPlayer activator);

    @Override
    public boolean isBurning() {
        return active;
    }
}
