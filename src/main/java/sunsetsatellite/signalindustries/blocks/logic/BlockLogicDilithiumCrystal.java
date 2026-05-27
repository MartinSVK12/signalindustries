package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicTransparent;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.SIItems;

public class BlockLogicDilithiumCrystal extends BlockLogicTransparent {
    public BlockLogicDilithiumCrystal(Block<?> block) {
        super(block, Materials.GLASS);
    }

	@Override
	public @NotNull ItemStack @Nullable [] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, @NotNull TilePosc tilePos, int data, @Nullable TileEntity tileEntity) {
		return switch (dropCause) {
			case PICK_BLOCK, SILK_TOUCH -> new ItemStack[]{new ItemStack(this)};
			case PROPER_TOOL -> new ItemStack[]{new ItemStack(SIItems.dilithiumShard, 1)};
			default -> null;
		};
	}
}
