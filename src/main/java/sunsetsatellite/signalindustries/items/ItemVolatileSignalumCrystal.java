package sunsetsatellite.signalindustries.items;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.entities.ProjectileCrystal;

public class ItemVolatileSignalumCrystal extends Item {
    public ItemVolatileSignalumCrystal(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
        if (world.isClientSide) {
            return super.onUseItem(itemstack, world, entityplayer);
        }
        world.entityJoinedWorld(new ProjectileCrystal(world, entityplayer));
        return super.onUseItem(itemstack, world, entityplayer);
    }
}
