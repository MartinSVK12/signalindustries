package sunsetsatellite.signalindustries.items.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicSIFluidTank;
import sunsetsatellite.signalindustries.tiles.machines.TileEntitySIFluidTank;

public class ItemBlockSIFluidTank extends ItemBlock<BlockLogicSIFluidTank> {
	public ItemBlockSIFluidTank(@NotNull Block<BlockLogicSIFluidTank> block) {
		super(block);
	}

	@Override
	public boolean placeOnBlock(@NotNull ItemStack stack, @NotNull World world, @Nullable Player player, @NotNull TilePosc blockPos, @NotNull Side side, double xHit, double yHit) {
		boolean b = super.placeOnBlock(stack, world, player, blockPos, side, xHit, yHit);
		blockPos = blockPos.add(side.direction(), new TilePos());
		if(!b) return b;
		TileEntitySIFluidTank tile = (TileEntitySIFluidTank) world.getTileEntity(blockPos);
		if(tile != null){
			tile.fluidCapacity[0] = (int) Math.pow(2, block.getLogic().tier.ordinal()) * 16000;
			if (stack.getData().containsKey("Fluid")) {
				FluidStack fluidStack = new FluidStack(stack.getData().getCompound("Fluid"));
				tile.insertFluid(0, fluidStack);
			}
		}
		return b;
	}
}
