package sunsetsatellite.signalindustries.items;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import sunsetsatellite.signalindustries.interfaces.ICustomDescription;

public class ItemTrigger extends Item implements ICustomDescription {
    public ItemTrigger(int i) {
        super(i);
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Unconfigured!";
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, double heightPlaced) {
        return true;//super.onItemUse(itemstack, entityplayer, world, i, j, k, l, heightPlaced);
    }
}
