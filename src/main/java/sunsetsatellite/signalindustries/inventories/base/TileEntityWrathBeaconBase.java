package sunsetsatellite.signalindustries.inventories.base;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.signalindustries.util.Tier;

public abstract class TileEntityWrathBeaconBase extends TileEntity {

    public Tier tier = Tier.BASIC;
    public boolean active = false;

    public abstract void activate(EntityPlayer activator);
}
