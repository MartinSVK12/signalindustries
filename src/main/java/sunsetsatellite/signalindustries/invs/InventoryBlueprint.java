package sunsetsatellite.signalindustries.invs;

import net.minecraft.core.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class InventoryBlueprint extends InventoryItemFluid {
    public InventoryBlueprint(ItemStack container) {
        super(container);
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.blueprint";
    }
}
