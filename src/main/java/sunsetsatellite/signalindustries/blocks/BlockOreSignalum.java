package sunsetsatellite.signalindustries.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.misc.SignalIndustriesAchievementPage;
import turniplabs.halplibe.helper.TextureHelper;

import java.util.Random;

public class BlockOreSignalum extends Block {
    public BlockOreSignalum(String key, int i) {
        super(key, i, Material.stone);
    }

    @Override
    public int getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
        return TextureHelper.getOrCreateBlockTextureIndex(SignalIndustries.MOD_ID,"signalum_ore_overlay.png");
    }

    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        Random random = new Random();
        return dropCause != EnumDropCause.IMPROPER_TOOL ? new ItemStack[]{new ItemStack(SignalIndustries.rawSignalumCrystal,random.nextInt(2)+1)} : null;
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta, EntityPlayer player, Item item) {
        super.onBlockDestroyedByPlayer(world, x, y, z, meta, player, item);
        player.triggerAchievement(SignalIndustriesAchievementPage.INIT);
    }
}
