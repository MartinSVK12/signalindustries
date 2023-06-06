package sunsetsatellite.signalindustries.items;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.command.ChatColor;
import sunsetsatellite.signalindustries.interfaces.ICustomDescription;

public class ItemWarpOrb extends Item implements ICustomDescription {
    public ItemWarpOrb(int i) {
        super(i);
    }

    @Override
    public String getDescription(ItemStack stack) {
        NBTTagCompound warpPosition = stack.tag.getCompoundTag("position");
        if(warpPosition.hasKey("x") && warpPosition.hasKey("y") && warpPosition.hasKey("z")){
            return String.format("Points to: %sX: %s Y: %s Z: %s%s",ChatColor.magenta,warpPosition.getInteger("x"),warpPosition.getInteger("y"),warpPosition.getInteger("z"),ChatColor.white);
        }
        return "Points to: "+ChatColor.lightGray+"Nowhere?";
    }
}
