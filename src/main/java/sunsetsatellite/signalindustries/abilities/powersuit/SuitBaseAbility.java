package sunsetsatellite.signalindustries.abilities.powersuit;


import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.Mode;

import java.util.ArrayList;

public abstract class SuitBaseAbility {

    public Mode mode;
    public String name;
    public String desc;
    public int cost;
    public int cooldown;
    public static ArrayList<SuitBaseAbility> abilities = new ArrayList<>();
    public ActivationType activationType = null;


    public SuitBaseAbility(Mode mode, String modId, String translateKey, int cost, int cooldown) {
        this.mode = mode;
        this.name = "ability."+modId+"."+mode.getName().toLowerCase()+"."+translateKey+".name";
        this.desc = "ability."+modId+"."+mode.getName().toLowerCase()+"."+translateKey+".desc";
        this.cost = cost;
        this.cooldown = cooldown;
        abilities.add(this);
    }

    public enum ActivationType {
        POSITION,
        SELF,
        TARGET
    }


    public abstract void activate(int x, int y, int z, EntityPlayer player, World world, SignalumPowerSuit powerSuit);
    public abstract void activate(EntityPlayer player, World world, SignalumPowerSuit powerSuit);
    public abstract void activate(EntityPlayer player, Entity target, World world, SignalumPowerSuit powerSuit);
}
