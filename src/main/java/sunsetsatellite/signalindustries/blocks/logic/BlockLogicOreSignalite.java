package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIItems;

import java.util.Random;

public class BlockLogicOreSignalite extends BlockLogic {
    public BlockLogicOreSignalite(Block<?> block) {
        super(block, Material.stone);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, Side side, int meta, Player player, Item item) {
        super.onBlockDestroyedByPlayer(world, x, y, z, side, meta, player, item);
        //player.triggerAchievement(SIAchievements.INIT);
    }

    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        Random random = new Random();
        return dropCause != EnumDropCause.IMPROPER_TOOL ? new ItemStack[]{new ItemStack(SIItems.rawSignalumCrystal, random.nextInt(2) + 2)} : null;
    }
}
