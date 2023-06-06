package sunsetsatellite.signalindustries.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.Material;

public class BlockTransparent extends Block {
    public BlockTransparent(int i, Material material) {
        super(i, material);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
