package sunsetsatellite.signalindustries.abilities.powersuit;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.util.Tier;

public class JumpAbility extends SuitBaseAbility {
    public JumpAbility() {
        super(Tier.BASIC, SignalIndustries.MOD_ID, "jump", 150, 40);
    }

    @Override
    public void activate(int x, int y, int z, Player player, World world, IPowerSuit powerSuit) {
        boost(player);
    }

    @Override
    public void activate(Player player, World world, IPowerSuit powerSuit) {
        boost(player);
    }

    @Override
    public void activate(Player player, Entity target, World world, IPowerSuit powerSuit) {
        boost(player);
    }

    private void boost(Player player) {
        double x = 5 * Math.cos(Math.floorMod(Math.round(player.yRot), 360) * Math.PI / 180);
        double z = 5 * Math.sin(Math.floorMod(Math.round(player.yRot), 360) * Math.PI / 180);
        player.zd += x;
        player.xd -= z;
    }
}
