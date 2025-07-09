package sunsetsatellite.signalindustries.blocks.logic.base;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.catalyst.core.util.ICustomDescription;

public class BlockLogicSolarTotem extends BlockLogic implements ICustomDescription {
    public BlockLogicSolarTotem(Block<?> block, Material material) {
        super(block, material);
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + TextFormatting.BROWN + "??? (Ancient)";
    }
}
