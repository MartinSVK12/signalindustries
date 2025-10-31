package sunsetsatellite.signalindustries.blocks.logic.base;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;

public class BlockLogicUndroppable extends BlockLogic {
    public BlockLogicUndroppable(Block<?> block, Material material) {
        super(block, material);
    }

    @Override
    public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
        if(dropCause == EnumDropCause.PICK_BLOCK){
            return new ItemStack[]{new ItemStack(this)};
        }
        return null;
    }

    @Override
    public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        if(dropCause == EnumDropCause.PICK_BLOCK){
            return new ItemStack[]{new ItemStack(this)};
        }
        return null;
    }
}
