package sunsetsatellite.signalindustries.blocks;


import net.minecraft.core.block.BlockRotatable;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;

public class BlockEternalTreeLog extends BlockRotatable {
    public BlockEternalTreeLog(String key, int id, Material material) {
        super(key, id, material);
    }

    public void setDefaultDirection(World world, int i, int j, int k) {}
}
