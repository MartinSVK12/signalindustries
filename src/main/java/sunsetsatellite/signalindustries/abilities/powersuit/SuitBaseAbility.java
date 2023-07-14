package sunsetsatellite.signalindustries.abilities.powersuit;

import net.minecraft.src.*;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.misc.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.Mode;

import java.util.ArrayList;

public abstract class SuitBaseAbility {

    public Mode mode;
    public String name;
    public String desc;
    public int cost;
    public int cooldown;
    public static ArrayList<SuitBaseAbility> abilities = new ArrayList<>();

    public SuitBaseAbility(Mode mode, String modId, String translateKey, int cost, int cooldown) {
        StringTranslate t = StringTranslate.getInstance();
        this.mode = mode;
        this.name = t.translateKey("ability."+modId+"."+mode.getName().toLowerCase()+"."+translateKey+".name");
        this.desc = t.translateKey("ability."+modId+"."+mode.getName().toLowerCase()+"."+translateKey+".desc");
        this.cost = cost;
        this.cooldown = cooldown;
        abilities.add(this);
    }


    public abstract void activate(int x, int y, int z, EntityPlayer player, World world, SignalumPowerSuit powerSuit);
    public abstract void activate(EntityPlayer player, World world, SignalumPowerSuit powerSuit);
    public abstract void activate(EntityPlayer player, Entity target, World world, SignalumPowerSuit powerSuit);
}
