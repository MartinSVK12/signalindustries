package sunsetsatellite.signalindustries.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;

import java.util.Random;

public class BlockOreDilithium extends Block {
    public BlockOreDilithium(String key, int i) {
        super(key, i, Material.stone);
    }

    /*@Override
    public int idDropped(int i, Random random) {
        return SignalIndustries.dilithiumShard.id;
    }

    @Override
    public int quantityDropped(int metadata, Random random) {
        return random.nextInt(1) + 1;
    }*/

    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        Random random = new Random();
        return dropCause != EnumDropCause.IMPROPER_TOOL ? new ItemStack[]{new ItemStack(SignalIndustries.dilithiumShard,random.nextInt(1)+1)} : null;
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta, EntityPlayer player, Item item) {
        super.onBlockDestroyedByPlayer(world, x, y, z, meta, player, item);
        player.triggerAchievement(SignalIndustriesAchievementPage.DILITHIUM);
    }
}
