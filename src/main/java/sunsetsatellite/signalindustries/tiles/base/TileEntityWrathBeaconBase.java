package sunsetsatellite.signalindustries.tiles.base;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.function.Consumer;

public abstract class TileEntityWrathBeaconBase extends TileEntity implements IActiveForm {

    public Tier tier = Tier.BASIC;
    public boolean active = false;

    public abstract void activate(Player activator);

    @Override
    public boolean isBurning() {
        return active;
    }

	public void doWithNearPlayers(int range, Consumer<Player> action){
		if(worldObj == null) return;
		worldObj.getPlayersWithinRange(tilePos.x, tilePos.y, tilePos.z, range).forEach(action);
	}
}
