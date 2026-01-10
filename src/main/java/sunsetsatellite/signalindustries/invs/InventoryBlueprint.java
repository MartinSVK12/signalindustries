package sunsetsatellite.signalindustries.invs;

import net.minecraft.core.item.ItemStack;

public class InventoryBlueprint extends InventoryItemFluid {
    public InventoryBlueprint(ItemStack container) {
        super(container);
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.blueprint";
    }
}
