package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.SignalIndustries;
import turniplabs.halplibe.helper.TextureHelper;

public class BlockGlowingObsidian extends Block {
    public BlockGlowingObsidian(String key, int id, Material material) {
        super(key, id, material);
        withOverbright();
    }

    @Override
    public int getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
        return TextureHelper.getOrCreateBlockTextureIndex(SignalIndustries.MOD_ID,"glowing_obsidian_overlay.png");
    }
}
