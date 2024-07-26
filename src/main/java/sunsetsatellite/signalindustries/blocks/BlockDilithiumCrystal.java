package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.BlockTransparent;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.SIItems;

import java.util.Random;

public class BlockDilithiumCrystal extends BlockTransparent {

    public BlockDilithiumCrystal(String key, int id, Material material, boolean renderInside) {
        super(key, id, material);
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        switch (dropCause) {
            case PICK_BLOCK:
            case SILK_TOUCH: {
                return new ItemStack[]{new ItemStack(this)};
            }
            case PROPER_TOOL:
                Random random = new Random();
                if (random.nextFloat() < 0.5f) {
                    return new ItemStack[]{new ItemStack(SIItems.dilithiumShard, 1)};
                } else {
                    return null;
                }
        }
        return null;
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, Side side, int meta, EntityPlayer player, Item item) {
        super.onBlockDestroyedByPlayer(world, x, y, z, side, meta, player, item);
        if (player.dimension == SIDimensions.dimEternity.id) {
            player.triggerAchievement(SIAchievements.ETERNITY);
        }
    }
}
