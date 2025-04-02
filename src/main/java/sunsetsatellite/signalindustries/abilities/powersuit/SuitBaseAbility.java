package sunsetsatellite.signalindustries.abilities.powersuit;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;

public abstract class SuitBaseAbility {

    public Tier tier;
    public String name;
    public String desc;
    public int cost;
    public int cooldown;
    public static ArrayList<SuitBaseAbility> abilities = new ArrayList<>();
    public ActivationType activationType = null;


    public SuitBaseAbility(Tier tier, String modId, String translateKey, int cost, int cooldown) {
        this.tier = tier;
        this.name = "ability."+modId+"."+ tier.name().toLowerCase()+"."+translateKey+".name";
        this.desc = "ability."+modId+"."+ tier.name().toLowerCase()+"."+translateKey+".desc";
        this.cost = cost;
        this.cooldown = cooldown;
        abilities.add(this);
    }

    public enum ActivationType {
        POSITION,
        SELF,
        TARGET
    }


    public abstract void activate(int x, int y, int z, Player player, World world, IPowerSuit powerSuit);
    public abstract void activate(Player player, World world, IPowerSuit powerSuit);
    public abstract void activate(Player player, Entity target, World world, IPowerSuit powerSuit);
}
