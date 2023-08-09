package sunsetsatellite.signalindustries.abilities.trigger;


import net.minecraft.client.render.dynamictexture.DynamicTexture;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class BoostAbility extends TriggerBaseAbility {
    public BoostAbility(String name, int cost, int cooldown) {
        super(name, cost, cooldown);
    }

    @Override
    public void activate(int i, int j, int k, EntityPlayer player, World world, ItemStack stack) {
        boost(player,world);
    }

    @Override
    public void activate(EntityPlayer player, World world, ItemStack stack) {
        boost(player,world);
    }

    private void boost(EntityPlayer player, World world) {
        double x = 1 * Math.cos(DynamicTexture.pmod(Math.round(player.yRot), 360) * Math.PI/180);
        double z = 1 * Math.sin(DynamicTexture.pmod(Math.round(player.yRot), 360) * Math.PI/180);
        player.zd += x;
        player.xd -= z;
    }
}
