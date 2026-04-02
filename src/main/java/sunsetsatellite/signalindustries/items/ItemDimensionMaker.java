package sunsetsatellite.signalindustries.items;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class ItemDimensionMaker extends Item {
    public ItemDimensionMaker(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
        /*if(EnvironmentHelper.isSinglePlayer()){
            return SignalIndustriesClient.createTestDimension();
        }*/

        return itemstack; //warpOrb;
    }
}
