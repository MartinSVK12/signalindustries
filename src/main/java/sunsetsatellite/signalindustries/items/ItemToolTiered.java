package sunsetsatellite.signalindustries.items;


import net.minecraft.core.block.Block;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemTool;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemToolTiered extends ItemTool implements ITiered {
    public Tier tier;
    protected ItemToolTiered(String key, int i, int j, Tier tier, ToolMaterial toolMaterial, Tag<Block> materialsEffectiveAgainst) {
        super(key, i, j, toolMaterial, materialsEffectiveAgainst);
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
