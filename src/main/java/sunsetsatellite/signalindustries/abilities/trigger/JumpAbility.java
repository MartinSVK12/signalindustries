package sunsetsatellite.signalindustries.abilities.trigger;


import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public class JumpAbility extends TriggerBaseAbility {

    public JumpAbility(String name, int cost, int cooldown) {
        super(name, cost, cooldown);
    }

    @Override
    public void activate(@NotNull TilePosc blockPos, Player player, World world, ItemStack trigger, ItemStack harness) {
        jump(player, world);
    }

    @Override
    public void activate(Player player, World world, ItemStack trigger, ItemStack harness) {
        jump(player, world);
    }

    private void jump(Player player, World world) {
        double x = 2 * Math.cos(Math.floorMod(Math.round(player.yRot), 360) * Math.PI / 180);
        double z = 2 * Math.sin(Math.floorMod(Math.round(player.yRot), 360) * Math.PI / 180);
        player.zd += x;
        player.xd -= z;
    }
}
