package sunsetsatellite.signalindustries.items;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.command.ChatColor;
import sunsetsatellite.signalindustries.interfaces.ICustomDescription;

public class ItemWarpOrb extends Item implements ICustomDescription {
    public ItemWarpOrb(int i) {
        super(i);
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Points to: "+ChatColor.lightGray+"Nowhere?";
    }
}
