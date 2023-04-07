package sunsetsatellite.signalindustries.api.impl.itempipes.misc;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IInventory;
import net.minecraft.src.RenderBlocks;
import sunsetsatellite.fluidapi.api.IFluidInventory;

public class RenderItemPipe {
    public static boolean render(RenderBlocks renderblocks, IBlockAccess blockAccess, int i, int j, int k, Block block, int l) {
        float width = 0.5f;

        float halfWidth = (1.0F - width) / 2.0F;
        block.setBlockBounds(halfWidth, halfWidth, halfWidth, halfWidth + width, halfWidth + width, halfWidth + width);
        renderblocks.renderStandardBlock(block, i, j, k);
        boolean flag = blockAccess.getBlockId(i + 1, j, k) == block.blockID || (blockAccess.getBlockTileEntity(i + 1, j, k) instanceof IItemConnection) || (blockAccess.getBlockTileEntity(i + 1, j, k) instanceof IInventory);
        boolean flag1 = blockAccess.getBlockId(i - 1, j, k) == block.blockID || (blockAccess.getBlockTileEntity(i - 1, j, k) instanceof IItemConnection) || (blockAccess.getBlockTileEntity(i - 1, j, k) instanceof IInventory);
        boolean flag2 = blockAccess.getBlockId(i, j + 1, k) == block.blockID || (blockAccess.getBlockTileEntity(i, j + 1, k) instanceof IItemConnection) || (blockAccess.getBlockTileEntity(i, j + 1, k) instanceof IInventory);
        boolean flag3 = blockAccess.getBlockId(i, j - 1, k) == block.blockID || (blockAccess.getBlockTileEntity(i, j - 1, k) instanceof IItemConnection) || (blockAccess.getBlockTileEntity(i, j - 1, k) instanceof IInventory);
        boolean flag4 = blockAccess.getBlockId(i, j, k + 1) == block.blockID || (blockAccess.getBlockTileEntity(i, j, k + 1) instanceof IItemConnection) || (blockAccess.getBlockTileEntity(i, j, k + 1) instanceof IInventory);
        boolean flag5 = blockAccess.getBlockId(i, j, k - 1) == block.blockID || (blockAccess.getBlockTileEntity(i, j, k - 1) instanceof IItemConnection) || (blockAccess.getBlockTileEntity(i, j, k - 1) instanceof IInventory);
        if(flag)
        {
            block.setBlockBounds(halfWidth + width, halfWidth, halfWidth, 1.0F, halfWidth + width, halfWidth + width);
            renderblocks.renderStandardBlock(block, i, j, k);

        }
        if(flag2)
        {
            block.setBlockBounds(halfWidth, halfWidth + width, halfWidth, halfWidth + width, 1.0F, halfWidth + width);
            renderblocks.renderStandardBlock(block, i, j, k);

        }
        if(flag4)
        {
            block.setBlockBounds(halfWidth, halfWidth, halfWidth + width, halfWidth + width, halfWidth + width, 1.0F);
            renderblocks.renderStandardBlock(block, i, j, k);

        }
        if(flag1)
        {
            block.setBlockBounds(0.0F, halfWidth, halfWidth, halfWidth, halfWidth + width, halfWidth + width);
            renderblocks.renderStandardBlock(block, i, j, k);

        }
        if(flag3)
        {
            block.setBlockBounds(halfWidth, 0.0F, halfWidth, halfWidth + width, halfWidth, halfWidth + width);
            renderblocks.renderStandardBlock(block, i, j, k);

        }
        if(flag5)
        {
            block.setBlockBounds(halfWidth, halfWidth, 0.0F, halfWidth + width, halfWidth + width, halfWidth);
            renderblocks.renderStandardBlock(block, i, j, k);

        }
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }
}
