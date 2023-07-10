package sunsetsatellite.signalindustries.items;

import net.minecraft.src.ItemStack;
import net.minecraft.src.ItemTool;
import net.minecraft.src.Material;
import net.minecraft.src.material.ToolMaterial;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemToolTiered extends ItemTool implements ITiered {
    public Tier tier;
    protected ItemToolTiered(int i, int j, Tier tier, ToolMaterial toolMaterial, Material[] materialsEffectiveAgainst) {
        super(i, j, toolMaterial, materialsEffectiveAgainst);
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
