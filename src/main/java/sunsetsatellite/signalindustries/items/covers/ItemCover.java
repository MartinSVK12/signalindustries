package sunsetsatellite.signalindustries.items.covers;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.catalyst.core.util.ISideInteractable;
import sunsetsatellite.signalindustries.covers.CoverBase;

import java.util.function.Supplier;

public class ItemCover extends Item implements ICustomDescription, ISideInteractable {

    public Supplier<CoverBase> coverSupplier;

    public ItemCover(String name, int id, Supplier<CoverBase> coverSupplier) {
        super(name, id);
        this.coverSupplier = coverSupplier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return TextFormatting.YELLOW + "Cover" + TextFormatting.WHITE;
    }
}
