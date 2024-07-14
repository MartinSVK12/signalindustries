package sunsetsatellite.signalindustries.items.itemblocks;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.impl.ItemInventoryFluid;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.interfaces.mixins.INBTCompound;
import sunsetsatellite.signalindustries.inventories.TileEntitySIFluidTank;

public class ItemBlockSIFluidTank extends ItemBlock implements IItemFluidContainer {
    public ItemBlockSIFluidTank(Block block) {
        super(block);
    }

    public boolean onUseItemOnBlock(ItemStack stack, EntityPlayer player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        if (stack.stackSize <= 0) {
            return false;
        } else {
            if (!world.canPlaceInsideBlock(blockX, blockY, blockZ)) {
                blockX += side.getOffsetX();
                blockY += side.getOffsetY();
                blockZ += side.getOffsetZ();
            }

            Block currentBlock = world.getBlock(blockX, blockY, blockZ);
            boolean placedInside = currentBlock != null && !(currentBlock instanceof BlockFluid);
            if (blockY >= 0 && blockY < world.getHeightBlocks()) {
                if (world.canBlockBePlacedAt(this.blockID, blockX, blockY, blockZ, false, side) && stack.consumeItem(player)) {
                    Block block = Block.blocksList[this.blockID];
                    if (placedInside && !world.isClientSide) {
                        world.playSoundEffect(2001, blockX, blockY, blockZ, world.getBlockId(blockX, blockY, blockZ));
                    }

                    if (world.setBlockAndMetadataWithNotify(blockX, blockY, blockZ, this.blockID, this.getPlacedBlockMetadata(stack.getMetadata()))) {
                        block.onBlockPlaced(world, blockX, blockY, blockZ, side, player, yPlaced);
                        world.playBlockSoundEffect(player, (double)((float)blockX + 0.5F), (double)((float)blockY + 0.5F), (double)((float)blockZ + 0.5F), block, EnumBlockSoundEffectType.PLACE);
                        TileEntitySIFluidTank tile = (TileEntitySIFluidTank) world.getBlockTileEntity(blockX,blockY,blockZ);
                        tile.tick();
                        if (stack.getData().containsKey("Fluid")) {
                            FluidStack fluidStack = new FluidStack(stack.getData().getCompound("Fluid"));
                            tile.insertFluid(0,fluidStack);
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return (int) Math.pow(2, ((BlockContainerTiered) getBlock()).tier.ordinal()) * 8000;
    }

    @Override
    public int getRemainingCapacity(ItemStack stack) {
        return getCapacity(stack) - stack.getData().getCompound("Fluid").getInteger("amount");
    }

    @Override
    public boolean canFill(ItemStack stack) {
        return getRemainingCapacity(stack) > 0;
    }

    @Override
    public boolean canDrain(ItemStack stack) {
        return getCapacity(stack) > getRemainingCapacity(stack);
    }

    @Override
    public FluidStack getCurrentFluid(ItemStack stack) {
        if(stack.getData().containsKey("Fluid")){
            return new FluidStack(stack.getData().getCompound("Fluid"));
        }
        return null;
    }

    @Override
    public void setCurrentFluid(FluidStack fluidStack, ItemStack stack) {
        CompoundTag fluidNbt = new CompoundTag();
        fluidStack.writeToNBT(fluidNbt);
        stack.getData().putCompound("Fluid", fluidNbt);
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack) {
        FluidStack thisStack = getCurrentFluid(stack);
        if(CatalystFluids.CONTAINERS.findFluidsWithAnyContainer(this).contains(fluidStack.liquid)){
            if (thisStack == null || thisStack.amount <= 0) {
                thisStack = fluidStack.copy();
                fluidStack.amount = 0;
            } else if (thisStack.isFluidEqual(fluidStack)) {
                int amount = Math.min(fluidStack.amount, getRemainingCapacity(stack));
                thisStack.amount += amount;
                fluidStack.amount -= amount;
            }
            if (thisStack.amount > 0) {
                CompoundTag fluidNbt = new CompoundTag();
                thisStack.writeToNBT(fluidNbt);
                stack.getData().putCompound("Fluid", fluidNbt);
            }
        }
        return stack;
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, TileEntityFluidContainer tile) {
        return fill(fluidStack, stack);
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, IFluidInventory tile) {
        return fill(fluidStack, stack);
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, TileEntityFluidContainer tile, int maxAmount) {
        if(CatalystFluids.CONTAINERS.findFluidsWithAnyContainer(this).contains(fluidStack.liquid)) {
            FluidStack thisStack = getCurrentFluid(stack);
            if (thisStack == null || thisStack.amount <= 0) {
                thisStack = fluidStack.copy();
                fluidStack.amount = 0;
            } else if (thisStack.isFluidEqual(fluidStack)) {
                int amount = Math.min(maxAmount, Math.min(fluidStack.amount, getRemainingCapacity(stack)));
                thisStack.amount += amount;
                fluidStack.amount -= amount;
            }
            if (thisStack.amount > 0) {
                CompoundTag fluidNbt = new CompoundTag();
                thisStack.writeToNBT(fluidNbt);
                stack.getData().putCompound("Fluid", fluidNbt);
            }
        }
        return stack;
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, ItemInventoryFluid inv) {
        return fill(fluidStack, stack);
    }

    @Override
    public FluidStack drain(ItemStack stack, int amount) {
        int actual = Math.min(getCurrentFluid(stack).amount,amount);
        FluidStack currentFluid = getCurrentFluid(stack);
        currentFluid.amount -= actual;
        setCurrentFluid(currentFluid,stack);
        return new FluidStack(currentFluid.getLiquid(),actual);
    }

    @Override
    public void drain(ItemStack stack, int slot, TileEntityFluidContainer tile) {
        this.drain(stack,slot,(IFluidInventory) tile);
    }

    @Override
    public void drain(ItemStack stack, int slot, IFluidInventory tile) {
        FluidStack fluidStack = getCurrentFluid(stack);
        if(fluidStack != null && tile.getAllowedFluidsForSlot(slot).contains(fluidStack.liquid) && tile.canInsertFluid(slot,fluidStack)){
            FluidStack returned = tile.insertFluid(slot, fluidStack);
            if(returned == null || returned.amount <= 0){
                ((INBTCompound) stack.getData()).removeTag("Fluid");
                return;
            }
            CompoundTag fluidNbt = new CompoundTag();
            returned.writeToNBT(fluidNbt);
            stack.getData().putCompound("Fluid", fluidNbt);
        }
    }

    @Override
    public void drain(ItemStack stack, int slot, ItemInventoryFluid inv) {
        this.drain(stack,slot,(IFluidInventory) inv);
    }
}
