package sunsetsatellite.signalindustries.abilities.powersuit;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.util.Tier;

public abstract class SuitBaseEffectAbility extends SuitBaseAbility{

    public int effectTime = 0;

    public SuitBaseEffectAbility(Tier tier, String modId, String translateKey, int cost, int cooldown, int effectTime) {
        super(tier, modId, translateKey, cost, cooldown);
        this.effectTime = effectTime;
    }

    public abstract void deactivate(int x, int y, int z, Player player, World world, IPowerSuit powerSuit);
    public abstract void deactivate(Player player, World world, IPowerSuit powerSuit);
    public abstract void deactivate(Player player, Entity target, World world, IPowerSuit powerSuit);

    public abstract void tick(int x, int y, int z, Player player, World world, IPowerSuit powerSuit);
    public abstract void tick(Player player, World world, IPowerSuit powerSuit);
    public abstract void tick(Player player, Entity target, World world, IPowerSuit powerSuit);

}
