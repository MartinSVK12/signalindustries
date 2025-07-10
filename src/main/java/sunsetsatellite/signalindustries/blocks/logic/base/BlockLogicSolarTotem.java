package sunsetsatellite.signalindustries.blocks.logic.base;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
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
