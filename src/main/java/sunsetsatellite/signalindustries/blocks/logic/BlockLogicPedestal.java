package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicMachine;
import sunsetsatellite.signalindustries.tiles.TileEntityPedestal;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockLogicPedestal extends BlockLogicMachine {
    public BlockLogicPedestal(Block<?> block) {
        super(block, Material.stone, Tier.PROTOTYPE, TileEntityPedestal::new, null);
        setNonSolid();
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + TextFormatting.BROWN + "??? (Ancient)";
    }

    public AABB getBlockBoundsFromState(WorldSource world, int x, int y, int z) {
        return AABB.getTemporaryBB(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    }
}
