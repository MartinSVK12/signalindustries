package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicTiered;
import sunsetsatellite.signalindustries.interfaces.IMultiblockPartBlock;
import sunsetsatellite.signalindustries.util.MultiblockPart;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockLogicParallelProcessor extends BlockLogicTiered implements IMultiblockPartBlock {

    public int maxParallel = 2;
    public final MultiblockPart.Type type;
    public final MultiblockPart.IO io;

    public BlockLogicParallelProcessor(Block<?> block, Material material, Tier tier, int maxParallel, MultiblockPart.Type type, MultiblockPart.IO io) {
        super(block, material, tier);
        this.maxParallel = maxParallel;
        this.type = type;
        this.io = io;
    }

    @Override
    public MultiblockPart.Type getType() {
        return type;
    }

    @Override
    public MultiblockPart.IO getIO() {
        return io;
    }
}
