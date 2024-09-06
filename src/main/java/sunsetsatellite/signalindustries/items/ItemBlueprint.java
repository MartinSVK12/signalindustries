package sunsetsatellite.signalindustries.items;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.catalyst.core.util.ICustomDescription;

public class ItemBlueprint extends Item implements ICustomDescription {
    public ItemBlueprint(String name, int id) {
        super(name, id);
    }

    @Override
    public String getDescription(ItemStack stack) {
        return TextFormatting.GRAY + "Blank" + TextFormatting.RESET;
    }
}
