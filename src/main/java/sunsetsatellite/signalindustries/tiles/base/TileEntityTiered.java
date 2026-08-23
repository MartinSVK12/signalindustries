package sunsetsatellite.signalindustries.tiles.base;

import net.minecraft.core.entity.player.Player;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicTiered;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.function.Consumer;

public abstract class TileEntityTiered extends TileEntityWithName {
    public Tier tier = Tier.PROTOTYPE;

    @Override
    public void tick() {
        super.tick();
        if (worldObj != null && getBlock() != null) {
			BlockLogicTiered tiered = Catalyst.blockLogic(getBlock(), BlockLogicTiered.class);
			if(tiered != null){
				tier = tiered.getTier();
			}
        }
    }

	public void doWithNearPlayers(int range, Consumer<Player> action){
		if(worldObj == null) return;
		worldObj.getPlayersWithinRange(tilePos.x, tilePos.y, tilePos.z, range).forEach(action);
	}
}
