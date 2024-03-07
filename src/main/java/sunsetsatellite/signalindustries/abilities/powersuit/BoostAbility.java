package sunsetsatellite.signalindustries.abilities.powersuit;

import net.minecraft.client.render.dynamictexture.DynamicTexture;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.Tier;

public class BoostAbility extends SuitBaseAbility{
    public BoostAbility() {
        super(Tier.BASIC, SignalIndustries.MOD_ID, "boost", 150, 100);
    }

    @Override
    public void activate(int x, int y, int z, EntityPlayer player, World world, SignalumPowerSuit powerSuit) {
        boost(player);
    }

    @Override
    public void activate(EntityPlayer player, World world, SignalumPowerSuit powerSuit) {
        boost(player);
    }

    @Override
    public void activate(EntityPlayer player, Entity target, World world, SignalumPowerSuit powerSuit) {
        boost(player);
    }

    private void boost(EntityPlayer player) {
        double x = 1 * Math.cos(DynamicTexture.pmod(Math.round(player.yRot), 360) * Math.PI/180);
        double z = 1 * Math.sin(DynamicTexture.pmod(Math.round(player.yRot), 360) * Math.PI/180);
        player.zd += x;
        player.xd -= z;
    }
}
