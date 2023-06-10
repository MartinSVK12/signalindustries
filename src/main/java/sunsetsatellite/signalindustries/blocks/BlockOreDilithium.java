package sunsetsatellite.signalindustries.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Random;

public class BlockOreDilithium extends Block {
    public BlockOreDilithium(int i) {
        super(i, Material.rock);
    }

    @Override
    public int idDropped(int i, Random random) {
        return SignalIndustries.dilithiumShard.itemID;
    }

    @Override
    public int quantityDropped(int metadata, Random random) {
        return random.nextInt(1) + 1;
    }
}
