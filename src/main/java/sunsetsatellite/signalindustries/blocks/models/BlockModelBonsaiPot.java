package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelCrossedSquares;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;
import org.useless.dragonfly.data.block.BlockModelData;
import org.useless.dragonfly.models.block.BlockModelDFJava;
import org.useless.dragonfly.models.block.StaticBlockModel;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityBonsaiPot;

public class BlockModelBonsaiPot extends BlockModelDFJava {

    public BlockModelBonsaiPot(@NotNull Block block, @NotNull StaticBlockModel baseModel) {
        super(block, baseModel);
    }

    public BlockModelBonsaiPot(@NotNull Block block, @NotNull BlockModelData baseModel) {
        super(block, baseModel);
    }

    @Override
    public boolean render(@NotNull final Tessellator tessellator, @NotNull final WorldSource worldSource, final int x, final int y, final int z){
        TileEntityBonsaiPot tile = (TileEntityBonsaiPot) worldSource.getTileEntity(x,y,z);
        ItemStack stack = tile.getItem(0);
        if(stack != null && stack.getItem() instanceof ItemBlock){
            Block<?> block = ((ItemBlock<?>) stack.getItem()).getBlock();
            BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(block);
            if(model instanceof BlockModelCrossedSquares){
                model.render(tessellator,x,y,z);
            }
        }
        return super.render(tessellator, worldSource, x, y, z);
    }
}
