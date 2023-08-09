package sunsetsatellite.signalindustries.abilities.powersuit;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.Mode;

public abstract class SuitBaseEffectAbility extends SuitBaseAbility{

    public int effectTime = 0;

    public SuitBaseEffectAbility(Mode mode, String modId, String translateKey, int cost, int cooldown, int effectTime) {
        super(mode, modId, translateKey, cost, cooldown);
        this.effectTime = effectTime;
    }

    public abstract void deactivate(int x, int y, int z, EntityPlayer player, World world, SignalumPowerSuit powerSuit);
    public abstract void deactivate(EntityPlayer player, World world, SignalumPowerSuit powerSuit);
    public abstract void deactivate(EntityPlayer player, Entity target, World world, SignalumPowerSuit powerSuit);

    public abstract void tick(int x, int y, int z, EntityPlayer player, World world, SignalumPowerSuit powerSuit);
    public abstract void tick(EntityPlayer player, World world, SignalumPowerSuit powerSuit);
    public abstract void tick(EntityPlayer player, Entity target, World world, SignalumPowerSuit powerSuit);

}
