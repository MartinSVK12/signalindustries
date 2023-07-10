package sunsetsatellite.signalindustries.items;

import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.material.ArmorMaterial;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.signalindustries.interfaces.ITiered;

public class ItemArmorTiered extends ItemArmor implements ITiered {
    public Tier tier;

    public ItemArmorTiered(int id, ArmorMaterial material, int armorPiece, Tier tier) {
        super(id, material, armorPiece);
        this.tier = tier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getColor() + tier.getRank();
    }

    @Override
    public Tier getTier() {
        return tier;
    }
}
