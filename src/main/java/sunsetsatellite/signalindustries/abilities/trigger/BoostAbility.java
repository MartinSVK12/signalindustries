package sunsetsatellite.signalindustries.abilities.trigger;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraft.src.dynamictexture.DynamicTexture;

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
        double x = 1 * Math.cos(DynamicTexture.pmod(Math.round(player.rotationYaw), 360) * Math.PI/180);
        double z = 1 * Math.sin(DynamicTexture.pmod(Math.round(player.rotationYaw), 360) * Math.PI/180);
        player.motionZ += x;
        player.motionX -= z;
    }
}
