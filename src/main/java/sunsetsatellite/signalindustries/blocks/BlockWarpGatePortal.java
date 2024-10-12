package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.BlockTransparent;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

import java.util.Random;

public class BlockWarpGatePortal extends BlockTransparent {
    public BlockWarpGatePortal(String key, int id, Material material) {
        super(key, id, material);
    }

    public AABB getCollisionBoundingBoxFromPool(WorldSource world, int x, int y, int z) {
        return null;
    }

    public void setBlockBoundsBasedOnState(WorldSource world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        float f1;
        float f3;
        //if ((meta & 1) > 0) {
            f1 = 0.125F;
            f3 = 0.5F;
            this.setBlockBounds(0.5F - f1, 0.0, 0.5F - f3, 0.5F + f1, 1.0, 0.5F + f3);
        //} else {
        //   f1 = 0.5F;
        //   f3 = 0.125F;
        //   this.setBlockBounds(0.5F - f1, 0.0, 0.5F - f3, 0.5F + f1, 1.0, 0.5F + f3);
        //}
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int getRenderBlockPass() {
        return 1;
    }

    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        for(int l = 0; l < 8; ++l) {
            double d = (double)x + (double)rand.nextFloat();
            double d1 = (double)y + (double)rand.nextFloat();
            double d2 = (double)z + (double)rand.nextFloat();
            int i1 = rand.nextInt(2) * 2 - 1;
            double d3 = ((double)rand.nextFloat() - 0.5) * 0.5;
            double d4 = ((double)rand.nextFloat() - 0.5) * 0.5;
            double d5 = ((double)rand.nextFloat() - 0.5) * 0.5;
            if (world.getBlockId(x - 1, y, z) != this.id && world.getBlockId(x + 1, y, z) != this.id) {
                d = (double)x + 0.5 + 0.25 * (double)i1;
                d3 = (double)rand.nextFloat() * 2.0 * (double)i1;
            } else {
                d2 = (double)z + 0.5 + 0.25 * (double)i1;
                d5 = (double)rand.nextFloat() * 2.0 * (double)i1;
            }

            world.spawnParticle("portal", d, d1, d2, d3, d4, d5, 0);
        }

    }
}
