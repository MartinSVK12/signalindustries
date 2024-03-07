package sunsetsatellite.signalindustries.items;


import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemArmorTiered extends ItemArmor implements ITiered {
    public Tier tier;

    public ItemArmorTiered(String key, int id, ArmorMaterial material, int armorPiece, Tier tier) {
        super(key, id, material, armorPiece);
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
