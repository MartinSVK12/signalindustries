package sunsetsatellite.signalindustries.items.base;


import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemArmorTiered extends ItemArmor<HumanArmorShape> implements ITiered {
    public Tier tier;

	public ItemArmorTiered(@NotNull String name, @NotNull String namespaceId, int id, @NotNull ArmorMaterial material, @NonNull HumanArmorShape armorShape, @NotNull Tier tier) {
		super(name, namespaceId, id, material, armorShape);
		this.tier = tier;
	}

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getTextColor() + tier.getRank();
    }

    @Override
    public Tier getTier() {
        return tier;
    }
}
