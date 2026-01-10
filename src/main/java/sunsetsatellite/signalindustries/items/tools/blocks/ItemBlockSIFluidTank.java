package sunsetsatellite.signalindustries.items.tools.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
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
    public boolean onUseItemOnBlock(ItemStack stack, @Nullable Player player, World world, int x, int y, int z, Side side, double xPlaced, double yPlaced) {
        if (stack.stackSize <= 0) {
            return false;
        } else {
            if (!world.canPlaceInsideBlock(x, y, z)) {
                x += side.getOffsetX();
                y += side.getOffsetY();
                z += side.getOffsetZ();
            }

            if (y >= 0 && y < world.getHeightBlocks()) {
                if (world.canBlockBePlacedAt(this.block.id(), x, y, z, false, side) && stack.consumeItem(player)) {
                    int meta = this.getPlacedBlockMetadata(player, stack, world, x, y, z, side, xPlaced, yPlaced);
                    if (world.setBlockAndMetadataWithNotify(x, y, z, this.block.id(), meta)) {
                        if (player == null) {
                            this.block.onBlockPlacedOnSide(world, x, y, z, side, xPlaced, yPlaced);
                        } else {
                            this.block.onBlockPlacedByMob(world, x, y, z, side, player, xPlaced, yPlaced);
                        }

                        TileEntitySIFluidTank tile = (TileEntitySIFluidTank) world.getTileEntity(x, y, z);
                        tile.fluidCapacity[0] = (int) Math.pow(2, block.getLogic().tier.ordinal()) * 16000;
                        if (stack.getData().containsKey("Fluid")) {
                            FluidStack fluidStack = new FluidStack(stack.getData().getCompound("Fluid"));
                            tile.insertFluid(0, fluidStack);
                        }

                        world.playBlockSoundEffect(player, (float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F, this.block, EnumBlockSoundEffectType.PLACE);
                        return true;
                    }

                    if (player == null || player.getGamemode().consumeBlocks()) {
                        ++stack.stackSize;
                    }
                }

                return false;
            } else {
                return false;
            }
        }
    }
}
