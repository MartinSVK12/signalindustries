package sunsetsatellite.signalindustries.items;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.catalyst.core.util.ICustomDescription;


public class ItemWarpOrb extends Item implements ICustomDescription {

    public ItemWarpOrb(String name, int id) {
        super(name, id);
    }

    @Override
    public String getDescription(ItemStack stack) {
        CompoundTag warpPosition = stack.getData().getCompound("position");
        if(warpPosition.containsKey("x") && warpPosition.containsKey("y") && warpPosition.containsKey("z")){
            return String.format("Points to: %sX: %s Y: %s Z: %s%s",TextFormatting.MAGENTA,warpPosition.getInteger("x"),warpPosition.getInteger("y"),warpPosition.getInteger("z"), TextFormatting.WHITE);
        }
        return "Points to: "+TextFormatting.LIGHT_GRAY+"Nowhere?";
    }
}
