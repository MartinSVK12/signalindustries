package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.SignalIndustries;


public class BlockGlowingObsidian extends Block {
    public BlockGlowingObsidian(String key, int id, Material material) {
        super(key, id, material);
        withOverbright();
    }
}
