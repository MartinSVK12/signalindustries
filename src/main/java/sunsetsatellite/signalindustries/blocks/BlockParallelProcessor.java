package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.material.Material;
import sunsetsatellite.signalindustries.blocks.base.BlockTiered;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockParallelProcessor extends BlockTiered {

    public int maxParallel = 2;

    public BlockParallelProcessor(String key, int i, Tier tier, Material material, int maxParallel) {
        super(key, i, tier, material);
        this.maxParallel = maxParallel;
    }
}
