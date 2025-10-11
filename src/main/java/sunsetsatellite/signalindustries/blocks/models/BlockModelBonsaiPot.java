package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;
import org.useless.dragonfly.data.block.BlockModelData;
import org.useless.dragonfly.models.block.BlockModelDFJava;
import org.useless.dragonfly.models.block.StaticBlockModel;

public class BlockModelBonsaiPot extends BlockModelDFJava {

    public BlockModelBonsaiPot(@NotNull Block block, @NotNull StaticBlockModel baseModel) {
        super(block, baseModel);
    }

    public BlockModelBonsaiPot(@NotNull Block block, @NotNull BlockModelData baseModel) {
        super(block, baseModel);
    }

    @Override
    public boolean render(@NotNull final Tessellator tessellator, @NotNull final WorldSource worldSource, final int x, final int y, final int z){
        BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(Blocks.SAPLING_OAK);
        model.render(tessellator,x,y,z);
        return super.render(tessellator, worldSource, x, y, z);
    }
}
