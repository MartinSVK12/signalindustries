package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicTransparent;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.SIAchievements;
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

	@Override
	public void onDestroyedByPlayer(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side, int data, @NotNull Player player, @Nullable Item item) {
		super.onDestroyedByPlayer(world, tilePos, side, data, player, item);
		player.triggerAchievement(SIAchievements.ETERNITY);
	}
}
