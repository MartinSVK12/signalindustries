package sunsetsatellite.signalindustries.blocks.base;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;

public class BlockNonSolid extends Block {

    public BlockNonSolid(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
}
