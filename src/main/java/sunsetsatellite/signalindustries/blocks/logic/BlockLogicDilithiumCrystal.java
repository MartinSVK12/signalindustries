package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicTransparent;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.SIItems;

public class BlockLogicDilithiumCrystal extends BlockLogicTransparent {
    public BlockLogicDilithiumCrystal(Block<?> block) {
        super(block, Materials.GLASS);
    }

    @Override
    public ItemStack[] getBreakResult(@NonNull World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
		return switch (dropCause) {
		    case PICK_BLOCK, SILK_TOUCH -> new ItemStack[]{new ItemStack(this)};
		    case PROPER_TOOL -> new ItemStack[]{new ItemStack(SIItems.dilithiumShard, 1)};
		    default -> null;
	    };
	}
}
