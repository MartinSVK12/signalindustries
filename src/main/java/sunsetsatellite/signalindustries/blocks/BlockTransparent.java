package sunsetsatellite.signalindustries.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;

public class BlockTransparent extends Block {

    public BlockTransparent(String key, int id, Material material) {
        super(key, id, material);
    }


    @Override
    public boolean isSolidRender() {
        return false;
    }
}
